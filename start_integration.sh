#!/bin/bash

# =====================================================
# SCRIPT DE INICIALIZAÇÃO DA INTEGRAÇÃO MULTIDISCIPLINAR
# Challenge 2025 - 4º Sprint
# =====================================================

echo "🚀 Iniciando Integração Multidisciplinar - Challenge 2025"
echo "📊 Database + 📱 Mobile + 🌐 IoT"
echo "=================================================="

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para log colorido
log_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

log_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Verificar se estamos no diretório correto
if [ ! -d "Java-main" ] || [ ! -d "mobileSentinelTrack" ] || [ ! -d "VisionMoto" ]; then
    log_error "Execute este script no diretório que contém as pastas Java-main, mobileSentinelTrack e VisionMoto"
    exit 1
fi

# =====================================================
# 1. CONFIGURAÇÃO DO BANCO DE DADOS
# =====================================================

log_info "1. Configurando Banco de Dados Oracle..."

cd Java-main

# Verificar se Java está instalado
if ! command -v java &> /dev/null; then
    log_error "Java não encontrado. Instale Java 21+ primeiro."
    exit 1
fi

# Configurar variáveis de ambiente para desenvolvimento
export DB_URL="jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
export DB_DRIVER="org.h2.Driver"
export DB_USER="sa"
export DB_PASS=""
export FLYWAY_ENABLED="false"
export JPA_DDL_AUTO="create-drop"
export HIBERNATE_DIALECT="org.hibernate.dialect.H2Dialect"

log_info "Executando migrações do banco..."
if ./mvnw flyway:migrate -q; then
    log_success "Migrações executadas com sucesso"
else
    log_warning "Usando H2 em memória para desenvolvimento"
fi

# =====================================================
# 2. MONGODB SETUP
# =====================================================

log_info "2. Configurando MongoDB..."

# Verificar se MongoDB está instalado
if command -v mongod &> /dev/null; then
    log_info "Iniciando MongoDB..."
    
    # Verificar se MongoDB já está rodando
    if pgrep -x "mongod" > /dev/null; then
        log_success "MongoDB já está rodando"
    else
        # Tentar iniciar MongoDB em background
        mongod --fork --logpath /tmp/mongodb.log --dbpath /tmp/mongodb-data 2>/dev/null || {
            log_warning "MongoDB não pôde ser iniciado automaticamente"
            log_info "Para usar MongoDB, inicie manualmente: mongod --dbpath /path/to/data"
        }
    fi
    
    # Executar script de importação se MongoDB estiver disponível
    if mongo --eval "db.runCommand('ping')" >/dev/null 2>&1; then
        log_info "Executando script de importação MongoDB..."
        mongo < src/main/resources/scripts/mongodb_import.js >/dev/null 2>&1 && {
            log_success "Dados importados para MongoDB"
        } || {
            log_warning "Erro na importação MongoDB - continuando sem NoSQL"
        }
    fi
else
    log_warning "MongoDB não encontrado - continuando apenas com banco relacional"
fi

cd ..

# =====================================================
# 3. BACKEND JAVA API
# =====================================================

log_info "3. Iniciando Backend Java API..."

cd Java-main

# Build da aplicação
log_info "Compilando aplicação Java..."
if ./mvnw clean compile -q; then
    log_success "Compilação concluída"
else
    log_error "Erro na compilação Java"
    exit 1
fi

# Iniciar Spring Boot em background
log_info "Iniciando Spring Boot API na porta 8080..."
nohup ./mvnw spring-boot:run > /tmp/java-api.log 2>&1 &
JAVA_PID=$!

# Aguardar API ficar disponível
log_info "Aguardando API Java ficar disponível..."
for i in {1..30}; do
    if curl -s http://localhost:8080/actuator/health >/dev/null 2>&1 || \
       curl -s http://localhost:8080/api/health >/dev/null 2>&1; then
        log_success "API Java disponível em http://localhost:8080"
        break
    fi
    sleep 2
    if [ $i -eq 30 ]; then
        log_warning "API Java demorou para iniciar - continuando..."
    fi
done

cd ..

# =====================================================
# 4. PYTHON VISIONMOTO API
# =====================================================

log_info "4. Iniciando Python VisionMoto API..."

cd VisionMoto

# Verificar se Python está instalado
if ! command -v python3 &> /dev/null; then
    log_error "Python3 não encontrado"
    exit 1
fi

# Verificar se requirements estão instalados
if [ -f "requirements.txt" ]; then
    log_info "Instalando dependências Python..."
    pip3 install -r requirements.txt >/dev/null 2>&1 || {
        log_warning "Erro ao instalar dependências Python - continuando..."
    }
fi

# Iniciar API Python em background
if [ -f "start_integration.py" ]; then
    log_info "Iniciando VisionMoto API na porta 5001..."
    nohup python3 start_integration.py > /tmp/python-api.log 2>&1 &
    PYTHON_PID=$!
    
    # Aguardar API ficar disponível
    log_info "Aguardando API Python ficar disponível..."
    for i in {1..20}; do
        if curl -s http://localhost:5001/health >/dev/null 2>&1; then
            log_success "API Python disponível em http://localhost:5001"
            break
        fi
        sleep 2
        if [ $i -eq 20 ]; then
            log_warning "API Python demorou para iniciar - continuando..."
        fi
    done
else
    log_warning "Script start_integration.py não encontrado - pulando API Python"
fi

cd ..

# =====================================================
# 5. MOBILE APP SETUP
# =====================================================

log_info "5. Configurando Mobile App..."

cd mobileSentinelTrack

# Verificar se Node.js está instalado
if ! command -v npm &> /dev/null; then
    log_error "Node.js/npm não encontrado"
    exit 1
fi

# Verificar se dependências estão instaladas
if [ ! -d "node_modules" ]; then
    log_info "Instalando dependências do mobile..."
    npm install --legacy-peer-deps >/dev/null 2>&1 || {
        log_error "Erro ao instalar dependências do mobile"
        exit 1
    }
    log_success "Dependências instaladas"
fi

# Verificar arquivo .env
if [ ! -f ".env" ]; then
    if [ -f ".env.example" ]; then
        log_info "Criando arquivo .env..."
        cp .env.example .env
        log_warning "Configure o arquivo .env com suas credenciais Firebase"
    else
        log_warning "Arquivo .env.example não encontrado"
    fi
fi

cd ..

# =====================================================
# 6. VERIFICAÇÃO FINAL
# =====================================================

log_info "6. Verificando integração..."

# Testar APIs
echo ""
echo "🔍 Status das APIs:"

# Java API
if curl -s http://localhost:8080/actuator/health >/dev/null 2>&1 || \
   curl -s http://localhost:8080/api/health >/dev/null 2>&1; then
    log_success "Java API: Online (http://localhost:8080)"
else
    log_warning "Java API: Offline"
fi

# Python API
if curl -s http://localhost:5001/health >/dev/null 2>&1; then
    log_success "Python API: Online (http://localhost:5001)"
else
    log_warning "Python API: Offline"
fi

# MongoDB
if mongo --eval "db.runCommand('ping')" >/dev/null 2>&1; then
    log_success "MongoDB: Online"
else
    log_warning "MongoDB: Offline"
fi

echo ""
echo "🎯 INTEGRAÇÃO MULTIDISCIPLINAR CONFIGURADA!"
echo "=================================================="
echo ""
echo "📋 Próximos passos:"
echo "1. 📱 Para iniciar o mobile app:"
echo "   cd mobileSentinelTrack && npx expo start"
echo ""
echo "2. 🌐 URLs disponíveis:"
echo "   • Java API: http://localhost:8080"
echo "   • Python API: http://localhost:5001"
echo "   • H2 Console: http://localhost:8080/h2-console"
echo ""
echo "3. 📊 Endpoints de integração:"
echo "   • Mobile: /api/mobile/*"
echo "   • Java: /api/java/*"
echo "   • .NET: /api/dotnet/*"
echo "   • IoT: /api/iot/*"
echo ""
echo "4. 🎥 Para parar os serviços:"
echo "   kill $JAVA_PID $PYTHON_PID 2>/dev/null"
echo ""

# Salvar PIDs para cleanup
echo "$JAVA_PID" > /tmp/integration_java.pid
echo "$PYTHON_PID" > /tmp/integration_python.pid

log_success "Sistema pronto para demonstração!"
echo "🏆 Challenge 2025 - 4º Sprint - Integração Completa"
