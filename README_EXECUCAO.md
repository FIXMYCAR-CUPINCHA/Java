# 🚀 GUIA DE EXECUÇÃO - INTEGRAÇÃO MULTIDISCIPLINAR

## 📋 Pré-requisitos

### 🔧 **Software Necessário**
- **Java 21+** (OpenJDK recomendado)
- **Node.js 18+** e npm
- **Python 3.8+** e pip
- **MongoDB** (opcional, para NoSQL)
- **Oracle Database** (ou H2 para desenvolvimento)
- **Git**

### 📱 **Para Mobile**
- **Expo CLI**: `npm install -g @expo/cli`
- **EAS CLI**: `npm install -g @expo/eas-cli`
- **Expo Go** (app no celular)

---

## 🚀 EXECUÇÃO AUTOMÁTICA (RECOMENDADO)

### 1. **Iniciar Sistema Completo**
```bash
# No diretório raiz do projeto
./start_integration.sh
```

Este script irá:
- ✅ Configurar banco de dados H2/Oracle
- ✅ Executar migrações Flyway
- ✅ Iniciar API Java (porta 8080)
- ✅ Iniciar API Python (porta 5001)
- ✅ Configurar MongoDB (se disponível)
- ✅ Verificar status das APIs

### 2. **Iniciar Mobile App**
```bash
cd mobileSentinelTrack
npx expo start
```

### 3. **Parar Sistema**
```bash
./stop_integration.sh
```

---

## 🔧 EXECUÇÃO MANUAL (PASSO A PASSO)

### 📊 **1. Database (Oracle/H2)**

```bash
cd Java-main

# Configurar variáveis de ambiente
export DB_URL="jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
export DB_DRIVER="org.h2.Driver"
export DB_USER="sa"
export DB_PASS=""
export JPA_DDL_AUTO="create-drop"

# Executar aplicação (cria tabelas automaticamente)
./mvnw spring-boot:run
```

**Para Oracle:**
```bash
export DB_URL="jdbc:oracle:thin:@localhost:1521:XE"
export DB_USER="sentineltrack"
export DB_PASS="password"
export FLYWAY_ENABLED="true"

./mvnw flyway:migrate
./mvnw spring-boot:run
```

### 🍃 **2. MongoDB (Opcional)**

```bash
# Iniciar MongoDB
mongod --dbpath /data/db

# Em outro terminal, executar script de importação
mongo < Java-main/src/main/resources/scripts/mongodb_import.js
```

### 🐍 **3. Python VisionMoto API**

```bash
cd VisionMoto

# Instalar dependências
pip install -r requirements.txt

# Iniciar API de integração
python start_integration.py
```

### 📱 **4. Mobile App**

```bash
cd mobileSentinelTrack

# Instalar dependências
npm install --legacy-peer-deps

# Configurar Firebase (copiar .env.example para .env)
cp .env.example .env
# Editar .env com credenciais Firebase

# Iniciar app
npx expo start
```

---

## 🔍 VERIFICAÇÃO DO SISTEMA

### ✅ **URLs para Teste**

| Serviço | URL | Status |
|---------|-----|--------|
| **Java API** | http://localhost:8080 | ✅ |
| **Python API** | http://localhost:5001 | ✅ |
| **H2 Console** | http://localhost:8080/h2-console | ✅ |
| **Health Check Java** | http://localhost:8080/actuator/health | ✅ |
| **Health Check Python** | http://localhost:5001/health | ✅ |

### 🧪 **Endpoints de Integração**

```bash
# Testar API Java
curl http://localhost:8080/api/mobile/motos

# Testar API Python
curl http://localhost:5001/api/mobile/motos

# Buscar moto por placa (Java)
curl http://localhost:8080/api/mobile/motos/buscar/ABC1234

# Buscar moto por placa (Python)
curl http://localhost:5001/api/mobile/motos/buscar/ABC1234

# Listar dispositivos IoT
curl http://localhost:5001/api/iot/devices

# Status das APIs
curl http://localhost:5001/health
```

---

## 📱 FUNCIONALIDADES DO MOBILE APP

### 🏠 **Telas Disponíveis**
1. **Login/Registro** - Autenticação Firebase
2. **Dashboard** - Visualização em matriz
3. **Dashboard IoT** - **NOVA** - Integração multi-disciplinar
4. **Cadastro de Motos** - CRUD básico
5. **Gerenciar Motos** - CRUD completo
6. **Relatórios** - PDF/CSV
7. **Sobre o App** - Informações

### 🌐 **Dashboard IoT - Funcionalidades**
- ✅ Status das APIs Java e Python
- ✅ Estatísticas em tempo real
- ✅ Busca por placa integrada
- ✅ Alertas IoT ativos
- ✅ Dispositivos online/offline
- ✅ Fallback automático entre APIs
- ✅ Notificações push
- ✅ Modo claro/escuro
- ✅ Português e Espanhol

---

## 🗄️ ESTRUTURA DO BANCO DE DADOS

### 📊 **Tabelas Relacionais (Oracle/H2)**
```sql
-- Tabelas originais
T_MT_ENDERECO
T_MT_MOTO  
T_MT_USUARIO
T_MT_FUNCIONARIO
T_MT_ROLE

-- Novas tabelas IoT
T_MT_DISPOSITIVO_IOT
T_MT_LOCALIZACAO_MOTO
T_MT_ALERTA
T_MT_HISTORICO_USO
T_MT_SENSOR_DADOS
T_MT_EVENTO_IOT
T_MT_PUSH_TOKEN
T_MT_AUDITORIA
```

### 🍃 **Collections MongoDB**
```javascript
// Collections NoSQL
enderecos
motos
usuarios  
dispositivos_iot
localizacoes_motos
alertas
historico_uso
sensor_dados
eventos_iot
metadados
```

---

## 🔧 PROCEDURES E FUNCTIONS

### 📝 **Principais Procedures**
```sql
-- Registrar evento IoT com idempotência
SP_REGISTRAR_EVENTO_IOT(...)

-- Atualizar localização da moto
SP_ATUALIZAR_LOCALIZACAO_MOTO(...)

-- Iniciar/Finalizar uso da moto
SP_INICIAR_USO_MOTO(...)
SP_FINALIZAR_USO_MOTO(...)

-- Gerar relatório de uso
SP_GERAR_RELATORIO_USO(...)
```

### 🧮 **Functions Utilitárias**
```sql
-- Validar CPF
FN_VALIDAR_CPF(p_cpf VARCHAR2) RETURN NUMBER

-- Calcular distância (Haversine)
FN_CALCULAR_DISTANCIA(lat1, lon1, lat2, lon2) RETURN NUMBER

-- Obter status da moto
FN_OBTER_STATUS_MOTO(p_placa VARCHAR2) RETURN VARCHAR2
```

---

## 🎯 DEMONSTRAÇÃO COMPLETA

### 1. **Iniciar Sistema**
```bash
./start_integration.sh
```

### 2. **Abrir Mobile App**
```bash
cd mobileSentinelTrack
npx expo start
```

### 3. **Testar Integração**
- ✅ Login no app
- ✅ Navegar para "Dashboard IoT"
- ✅ Verificar status das APIs
- ✅ Buscar moto por placa
- ✅ Ver alertas em tempo real
- ✅ Testar notificações
- ✅ Trocar idioma (PT/ES)
- ✅ Alternar tema (claro/escuro)

### 4. **Verificar Dados**
- ✅ H2 Console: http://localhost:8080/h2-console
- ✅ MongoDB: `mongo` → `use sentineltrack_nosql` → `show collections`

---

## 🚨 SOLUÇÃO DE PROBLEMAS

### ❌ **Java API não inicia**
```bash
# Verificar Java
java -version

# Verificar porta 8080
lsof -i :8080

# Logs
tail -f /tmp/java-api.log
```

### ❌ **Python API não inicia**
```bash
# Verificar Python
python3 --version

# Instalar dependências
pip install flask flask-cors

# Verificar porta 5001
lsof -i :5001
```

### ❌ **Mobile App não conecta**
```bash
# Verificar .env
cat mobileSentinelTrack/.env

# Limpar cache
cd mobileSentinelTrack
npx expo start -c
```

### ❌ **MongoDB não conecta**
```bash
# Verificar se está rodando
pgrep mongod

# Iniciar manualmente
mongod --dbpath /tmp/mongodb-data
```

---

## 📊 EXPORTAÇÃO E IMPORTAÇÃO

### 📤 **Exportar dados Oracle para JSON**
```bash
cd Java-main
sqlplus user/pass@db @src/main/resources/scripts/export_to_json.sql
```

### 📥 **Importar dados no MongoDB**
```bash
mongo < src/main/resources/scripts/mongodb_import.js
```

---

## 🎥 ROTEIRO PARA VÍDEO

### 1. **Introdução (30s)**
- Mostrar arquitetura integrada
- Explicar disciplinas envolvidas

### 2. **Database (2min)**
- Executar procedures no H2 Console
- Mostrar exportação JSON
- Importar no MongoDB
- Consultas NoSQL

### 3. **Mobile App (2min)**
- Login e navegação
- Dashboard IoT funcionando
- Busca por placa
- Alertas em tempo real
- Troca de idioma/tema

### 4. **APIs Integration (1min)**
- Status das APIs
- Fallback automático
- Endpoints funcionando

### 5. **Conclusão (30s)**
- Resumo da integração
- Benefícios da solução

---

## 🏆 CHECKLIST FINAL

### ✅ **Database (90 pontos)**
- [x] Modelo físico com 8+ tabelas IoT
- [x] 6 procedures com tratamento de exceções
- [x] 3 functions personalizadas  
- [x] Triggers automáticos
- [x] Export JSON completo
- [x] Estrutura MongoDB otimizada
- [x] Índices de performance
- [x] Dados de exemplo

### ✅ **Mobile (100 pontos)**
- [x] Todas as telas funcionais
- [x] Dashboard IoT integrado
- [x] Navegação fluida
- [x] Validações completas
- [x] Push notifications
- [x] Integração API Java/Python
- [x] Português + Espanhol
- [x] Modo claro/escuro
- [x] Arquitetura limpa
- [x] Publicação configurada

### ✅ **IoT/Java (Integração)**
- [x] API Java/Spring Boot
- [x] Endpoints multi-disciplinares
- [x] Integração IoT completa
- [x] Sistema de alertas
- [x] Fallback entre APIs
- [x] Documentação completa

---

## 📞 SUPORTE

### 🐛 **Problemas Comuns**
1. **Porta ocupada**: `lsof -i :8080` e `kill <PID>`
2. **Dependências**: `npm install --legacy-peer-deps`
3. **Firebase**: Configurar .env corretamente
4. **MongoDB**: Verificar se está rodando

### 📧 **Contato**
- **Equipe**: Challenge 2025 - 4º Sprint
- **GitHub**: Repositórios individuais
- **Documentação**: `INTEGRACAO_MULTIDISCIPLINAR.md`

---

**🎯 Sistema 100% funcional e integrado - Pronto para demonstração!**

**🏆 Challenge 2025 - 4º Sprint - Integração Multidisciplinar Completa**
