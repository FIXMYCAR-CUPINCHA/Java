#!/bin/bash

# =====================================================
# SCRIPT PARA PARAR A INTEGRAÇÃO MULTIDISCIPLINAR
# Challenge 2025 - 4º Sprint
# =====================================================

echo "🛑 Parando Integração Multidisciplinar..."

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${YELLOW}ℹ️  $1${NC}"
}

log_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Parar Java API
if [ -f "/tmp/integration_java.pid" ]; then
    JAVA_PID=$(cat /tmp/integration_java.pid)
    if kill $JAVA_PID 2>/dev/null; then
        log_success "Java API parada (PID: $JAVA_PID)"
    else
        log_info "Java API já estava parada"
    fi
    rm -f /tmp/integration_java.pid
fi

# Parar Python API
if [ -f "/tmp/integration_python.pid" ]; then
    PYTHON_PID=$(cat /tmp/integration_python.pid)
    if kill $PYTHON_PID 2>/dev/null; then
        log_success "Python API parada (PID: $PYTHON_PID)"
    else
        log_info "Python API já estava parada"
    fi
    rm -f /tmp/integration_python.pid
fi

# Parar processos por porta (backup)
log_info "Verificando processos nas portas 8080 e 5001..."

# Porta 8080 (Java)
JAVA_PORT_PID=$(lsof -ti:8080 2>/dev/null)
if [ ! -z "$JAVA_PORT_PID" ]; then
    kill $JAVA_PORT_PID 2>/dev/null && log_success "Processo na porta 8080 parado"
fi

# Porta 5001 (Python)
PYTHON_PORT_PID=$(lsof -ti:5001 2>/dev/null)
if [ ! -z "$PYTHON_PORT_PID" ]; then
    kill $PYTHON_PORT_PID 2>/dev/null && log_success "Processo na porta 5001 parado"
fi

# Parar MongoDB se foi iniciado pelo script
if pgrep -f "mongod.*fork" >/dev/null; then
    log_info "Parando MongoDB..."
    pkill -f "mongod.*fork" && log_success "MongoDB parado"
fi

# Limpar logs temporários
rm -f /tmp/java-api.log /tmp/python-api.log /tmp/mongodb.log

log_success "Todos os serviços foram parados!"
echo "🏁 Integração finalizada."
