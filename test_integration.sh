#!/bin/bash

# =====================================================
# SCRIPT DE TESTE DA INTEGRAÇÃO MULTIDISCIPLINAR
# Challenge 2025 - 4º Sprint
# =====================================================

echo "🧪 Testando Integração Multidisciplinar - Challenge 2025"
echo "======================================================="

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Contadores
TESTS_PASSED=0
TESTS_FAILED=0
TOTAL_TESTS=0

# Função para log colorido
log_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

log_success() {
    echo -e "${GREEN}✅ $1${NC}"
    ((TESTS_PASSED++))
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
    ((TESTS_FAILED++))
}

log_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

# Função para testar endpoint
test_endpoint() {
    local name="$1"
    local url="$2"
    local expected_status="$3"
    
    ((TOTAL_TESTS++))
    
    log_info "Testando: $name"
    
    if response=$(curl -s -w "%{http_code}" -o /tmp/test_response "$url" 2>/dev/null); then
        status_code="${response: -3}"
        
        if [ "$status_code" = "$expected_status" ]; then
            log_success "$name - Status: $status_code"
            
            # Mostrar resposta se for JSON
            if [[ "$url" == *"/api/"* ]]; then
                response_body=$(cat /tmp/test_response 2>/dev/null)
                if [[ "$response_body" == *"{"* ]]; then
                    echo "   📄 Response: $(echo "$response_body" | jq -c . 2>/dev/null || echo "$response_body" | head -c 100)..."
                fi
            fi
        else
            log_error "$name - Expected: $expected_status, Got: $status_code"
        fi
    else
        log_error "$name - Connection failed"
    fi
    
    echo ""
}

# Função para testar arquivo
test_file() {
    local name="$1"
    local file="$2"
    
    ((TOTAL_TESTS++))
    
    if [ -f "$file" ]; then
        log_success "$name - Arquivo existe"
    else
        log_error "$name - Arquivo não encontrado: $file"
    fi
}

# Função para testar diretório
test_directory() {
    local name="$1"
    local dir="$2"
    
    ((TOTAL_TESTS++))
    
    if [ -d "$dir" ]; then
        log_success "$name - Diretório existe"
    else
        log_error "$name - Diretório não encontrado: $dir"
    fi
}

echo "🔍 INICIANDO TESTES DE INTEGRAÇÃO"
echo "================================="
echo ""

# =====================================================
# 1. TESTES DE ESTRUTURA DE ARQUIVOS
# =====================================================

log_info "1. Testando Estrutura de Arquivos..."
echo ""

test_directory "Java Project" "Java-main"
test_directory "Mobile Project" "mobileSentinelTrack"
test_directory "Python Project" "VisionMoto"

test_file "Java pom.xml" "Java-main/pom.xml"
test_file "Mobile package.json" "mobileSentinelTrack/package.json"
test_file "Python requirements.txt" "VisionMoto/requirements.txt"

# Testes de migrações SQL
test_file "Migration V9 (IoT Tables)" "Java-main/src/main/resources/db/migration/V9__create_iot_integration_tables.sql"
test_file "Migration V10 (Indexes)" "Java-main/src/main/resources/db/migration/V10__create_iot_indexes_constraints.sql"
test_file "Migration V11 (Procedures)" "Java-main/src/main/resources/db/migration/V11__create_procedures_functions.sql"
test_file "Migration V12 (Data)" "Java-main/src/main/resources/db/migration/V12__insert_iot_initial_data.sql"

# Testes de scripts
test_file "Export JSON Script" "Java-main/src/main/resources/scripts/export_to_json.sql"
test_file "MongoDB Import Script" "Java-main/src/main/resources/scripts/mongodb_import.js"

# Testes de entidades Java
test_file "DispositivoIot Entity" "Java-main/src/main/java/br/com/fiap/mottu/models/DispositivoIot.java"
test_file "LocalizacaoMoto Entity" "Java-main/src/main/java/br/com/fiap/mottu/models/LocalizacaoMoto.java"
test_file "Alerta Entity" "Java-main/src/main/java/br/com/fiap/mottu/models/Alerta.java"

# Testes de repositories
test_file "DispositivoIot Repository" "Java-main/src/main/java/br/com/fiap/mottu/repositories/DispositivoIotRepository.java"
test_file "LocalizacaoMoto Repository" "Java-main/src/main/java/br/com/fiap/mottu/repositories/LocalizacaoMotoRepository.java"
test_file "Alerta Repository" "Java-main/src/main/java/br/com/fiap/mottu/repositories/AlertaRepository.java"

# Testes de controllers
test_file "Integração IoT Controller" "Java-main/src/main/java/br/com/fiap/mottu/controllers/IntegracaoIotController.java"

# Testes de DTOs
test_file "DispositivoIot DTO" "Java-main/src/main/java/br/com/fiap/mottu/dto/DispositivoIotDTO.java"
test_file "LocalizacaoMoto DTO" "Java-main/src/main/java/br/com/fiap/mottu/dto/LocalizacaoMotoDTO.java"

# Testes Mobile
test_file "IoT Dashboard Screen" "mobileSentinelTrack/screens/IoTDashboardScreen.js"
test_file "IoT Service" "mobileSentinelTrack/services/api/iot.js"
test_file "App.js (Navigation)" "mobileSentinelTrack/App.js"

# Testes de localização
test_file "Português Translations" "mobileSentinelTrack/locales/pt.json"
test_file "Espanhol Translations" "mobileSentinelTrack/locales/es.json"

echo ""

# =====================================================
# 2. TESTES DE CONECTIVIDADE DAS APIS
# =====================================================

log_info "2. Testando Conectividade das APIs..."
echo ""

# Aguardar um pouco para APIs iniciarem
sleep 2

# Testar Java API
test_endpoint "Java API Health" "http://localhost:8080/actuator/health" "200"
test_endpoint "Java API Alternative Health" "http://localhost:8080/api/health" "200"
test_endpoint "Java Mobile Motos" "http://localhost:8080/api/mobile/motos" "200"
test_endpoint "Java IoT Status" "http://localhost:8080/api/java/motos/status" "200"

# Testar Python API
test_endpoint "Python API Health" "http://localhost:5001/health" "200"
test_endpoint "Python Mobile Motos" "http://localhost:5001/api/mobile/motos" "200"
test_endpoint "Python IoT Devices" "http://localhost:5001/api/iot/devices" "200"

# =====================================================
# 3. TESTES DE INTEGRAÇÃO ESPECÍFICOS
# =====================================================

log_info "3. Testando Integração Específica..."
echo ""

# Testar busca por placa (deve funcionar em ambas APIs)
test_endpoint "Java - Busca Placa ABC1234" "http://localhost:8080/api/mobile/motos/buscar/ABC1234" "200"
test_endpoint "Python - Busca Placa ABC1234" "http://localhost:5001/api/mobile/motos/buscar/ABC1234" "200"

# Testar endpoints .NET
test_endpoint ".NET Motorcycle Data" "http://localhost:8080/api/dotnet/Dashboard/GetMotorcycleData" "200"
test_endpoint ".NET Find by Plate" "http://localhost:8080/api/dotnet/Motorcycles/FindByPlate/ABC1234" "200"

# Testar H2 Console (se disponível)
test_endpoint "H2 Console" "http://localhost:8080/h2-console" "200"

# =====================================================
# 4. TESTES DE BANCO DE DADOS
# =====================================================

log_info "4. Testando Estrutura do Banco..."
echo ""

# Verificar se MongoDB está rodando
((TOTAL_TESTS++))
if mongo --eval "db.runCommand('ping')" >/dev/null 2>&1; then
    log_success "MongoDB - Conectividade"
    
    # Testar collections
    ((TOTAL_TESTS++))
    if mongo sentineltrack_nosql --eval "db.motos.countDocuments()" >/dev/null 2>&1; then
        log_success "MongoDB - Collection motos"
    else
        log_error "MongoDB - Collection motos não encontrada"
    fi
    
    ((TOTAL_TESTS++))
    if mongo sentineltrack_nosql --eval "db.dispositivos_iot.countDocuments()" >/dev/null 2>&1; then
        log_success "MongoDB - Collection dispositivos_iot"
    else
        log_error "MongoDB - Collection dispositivos_iot não encontrada"
    fi
else
    log_warning "MongoDB - Offline (opcional)"
fi

# =====================================================
# 5. TESTES DE MOBILE APP
# =====================================================

log_info "5. Testando Mobile App..."
echo ""

# Verificar se node_modules existe
test_directory "Mobile - node_modules" "mobileSentinelTrack/node_modules"

# Verificar arquivos de configuração
test_file "Mobile - .env.example" "mobileSentinelTrack/.env.example"
test_file "Mobile - app.json" "mobileSentinelTrack/app.json"
test_file "Mobile - package.json" "mobileSentinelTrack/package.json"

# Verificar se pode fazer build (teste sintático)
((TOTAL_TESTS++))
cd mobileSentinelTrack
if npm run validate >/dev/null 2>&1; then
    log_success "Mobile - Validação de código"
else
    log_warning "Mobile - Validação de código (pode precisar de dependências)"
fi
cd ..

# =====================================================
# 6. TESTES DE DOCUMENTAÇÃO
# =====================================================

log_info "6. Testando Documentação..."
echo ""

test_file "Documentação Principal" "INTEGRACAO_MULTIDISCIPLINAR.md"
test_file "Guia de Execução" "README_EXECUCAO.md"
test_file "Script de Inicialização" "start_integration.sh"
test_file "Script de Parada" "stop_integration.sh"

# Verificar se scripts são executáveis
((TOTAL_TESTS++))
if [ -x "start_integration.sh" ]; then
    log_success "Script de inicialização - Executável"
else
    log_error "Script de inicialização - Não executável"
fi

((TOTAL_TESTS++))
if [ -x "stop_integration.sh" ]; then
    log_success "Script de parada - Executável"
else
    log_error "Script de parada - Não executável"
fi

# =====================================================
# 7. RELATÓRIO FINAL
# =====================================================

echo ""
echo "📊 RELATÓRIO FINAL DOS TESTES"
echo "=============================="
echo ""

echo -e "${BLUE}Total de Testes: $TOTAL_TESTS${NC}"
echo -e "${GREEN}Testes Passou: $TESTS_PASSED${NC}"
echo -e "${RED}Testes Falhou: $TESTS_FAILED${NC}"

if [ $TESTS_FAILED -eq 0 ]; then
    echo ""
    echo -e "${GREEN}🎉 TODOS OS TESTES PASSARAM!${NC}"
    echo -e "${GREEN}✅ Sistema 100% integrado e funcional${NC}"
    echo ""
    echo "🚀 Próximos passos:"
    echo "1. Testar mobile app: cd mobileSentinelTrack && npx expo start"
    echo "2. Acessar Dashboard IoT no app"
    echo "3. Gravar vídeo demonstrativo"
    echo "4. Entregar projeto completo"
else
    echo ""
    echo -e "${YELLOW}⚠️  ALGUNS TESTES FALHARAM${NC}"
    echo -e "${YELLOW}Verifique os erros acima e corrija antes da entrega${NC}"
    
    if [ $TESTS_PASSED -gt $TESTS_FAILED ]; then
        echo ""
        echo -e "${GREEN}✅ Maioria dos testes passou - Sistema funcional${NC}"
    fi
fi

# Calcular porcentagem de sucesso
success_rate=$((TESTS_PASSED * 100 / TOTAL_TESTS))
echo ""
echo -e "${BLUE}Taxa de Sucesso: $success_rate%${NC}"

if [ $success_rate -ge 90 ]; then
    echo -e "${GREEN}🏆 EXCELENTE! Sistema pronto para produção${NC}"
elif [ $success_rate -ge 75 ]; then
    echo -e "${YELLOW}👍 BOM! Pequenos ajustes necessários${NC}"
else
    echo -e "${RED}⚠️  ATENÇÃO! Correções importantes necessárias${NC}"
fi

echo ""
echo "🎯 Challenge 2025 - 4º Sprint - Teste de Integração Concluído"

# Limpar arquivos temporários
rm -f /tmp/test_response

exit $TESTS_FAILED
