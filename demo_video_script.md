# 🎥 ROTEIRO PARA VÍDEO DEMONSTRATIVO

## 📋 **Informações Gerais**
- **Duração**: 6 minutos
- **Formato**: Screencast + Narração
- **Objetivo**: Demonstrar integração completa das 3 disciplinas
- **Audiência**: Professores e avaliadores FIAP

---

## 🎬 **ROTEIRO DETALHADO**

### **INTRODUÇÃO (30 segundos)**

**[TELA: Documentação INTEGRACAO_MULTIDISCIPLINAR.md]**

**Narração:**
> "Olá! Sou [Nome] e vou apresentar nossa integração multidisciplinar do Challenge 2025 - 4º Sprint. Este projeto integra três disciplinas: Database, Mobile Development e IoT, criando um sistema completo de gestão inteligente de motocicletas."

**[MOSTRAR: Diagrama da arquitetura no README]**

> "Nossa solução conecta um banco Oracle relacional, MongoDB NoSQL, APIs Java e Python, e um aplicativo React Native, demonstrando uma arquitetura híbrida real de mercado."

---

### **1. DATABASE - BANCO RELACIONAL E NOSQL (2 minutos)**

**[TELA: Terminal]**

**Narração:**
> "Começando pela disciplina de Database. Vou iniciar nosso sistema integrado."

**[EXECUTAR]:**
```bash
./start_integration.sh
```

**[AGUARDAR INICIALIZAÇÃO - MOSTRAR LOGS]**

> "Este script configura automaticamente o banco H2, executa as migrações Flyway com nossas 8 novas tabelas IoT, e inicia as APIs Java e Python."

**[TELA: H2 Console - http://localhost:8080/h2-console]**

**Configurações H2:**
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: (vazio)

**[MOSTRAR TABELAS]**
> "Aqui temos nossa estrutura relacional. Além das tabelas originais, criamos 8 novas tabelas para IoT: dispositivos, alertas, localização das motos, histórico de uso, e dados de sensores."

**[EXECUTAR QUERY]:**
```sql
SELECT * FROM T_MT_DISPOSITIVO_IOT;
```

> "Vemos os dispositivos IoT cadastrados: sensores de movimento, câmeras, travas inteligentes."

**[EXECUTAR PROCEDURE]:**
```sql
CALL SP_REGISTRAR_EVENTO_IOT('EVT-DEMO-001', 'SENSOR001', 'movimento_detectado', '{"confidence": 0.95}', 1, ?);
```

> "Esta procedure registra eventos IoT com tratamento de exceções e idempotência, criando alertas automaticamente."

**[MOSTRAR RESULTADO]:**
```sql
SELECT * FROM T_MT_ALERTA WHERE ID_ALERTA LIKE 'ALR-%';
```

**[TELA: Terminal - MongoDB]**
```bash
mongo
use sentineltrack_nosql
show collections
```

**[EXECUTAR CONSULTA NOSQL]:**
```javascript
db.motos.find({"status.atual": "disponivel"}).pretty()
```

> "No MongoDB, temos a mesma informação estruturada como documentos NoSQL, permitindo consultas flexíveis e escalabilidade horizontal."

---

### **2. MOBILE APPLICATION DEVELOPMENT (2 minutos)**

**[TELA: Terminal]**
```bash
cd mobileSentinelTrack
npx expo start
```

**[TELA: Expo QR Code]**

**Narração:**
> "Agora vou demonstrar nosso aplicativo React Native. Vou abrir no simulador."

**[ABRIR APP NO SIMULADOR/DISPOSITIVO]**

**[TELA: Login Screen]**
> "Temos autenticação completa com Firebase. Vou fazer login."

**[FAZER LOGIN]**

**[TELA: Drawer Navigation]**
> "O app possui navegação drawer com todas as telas funcionais. Vou mostrar nossa nova tela de Dashboard IoT."

**[NAVEGAR: Dashboard IoT]**

**[TELA: IoT Dashboard]**
> "Esta é nossa tela de integração multidisciplinar. Aqui vemos:"
> "- Status das APIs Java e Python em tempo real"
> "- Estatísticas das motos e dispositivos IoT"
> "- Alertas ativos do sistema"

**[DEMONSTRAR: Busca por Placa]**
> "Vou testar a busca por placa, que integra com nossas APIs backend."

**[DIGITAR: ABC1234]**

> "A busca funciona com fallback automático - se a API Java falhar, usa a Python automaticamente."

**[MOSTRAR RESULTADO]**
> "Vemos a localização completa da moto com instruções detalhadas de como encontrá-la."

**[DEMONSTRAR: Troca de Idioma]**
> "O app suporta português e espanhol com internacionalização completa."

**[CONFIGURAÇÕES → Idioma → Español]**

**[DEMONSTRAR: Tema]**
> "E modo claro/escuro seguindo Material Design."

**[ALTERNAR TEMA]**

---

### **3. IOT E INTEGRAÇÃO DE APIS (1 minuto)**

**[TELA: Browser - APIs]**

**Narração:**
> "Vou mostrar a integração das APIs. Temos endpoints específicos para cada tecnologia."

**[ABRIR: http://localhost:8080/api/mobile/motos]**
> "API Java para mobile com dados relacionais."

**[ABRIR: http://localhost:5001/api/mobile/motos]**
> "API Python para IoT com dados NoSQL."

**[ABRIR: http://localhost:8080/api/dotnet/Dashboard/GetMotorcycleData]**
> "Endpoint .NET compatível para integração corporativa."

**[TELA: Terminal - Teste de Integração]**
```bash
./test_integration.sh
```

> "Nosso script de teste valida toda a integração automaticamente."

**[MOSTRAR RESULTADOS DOS TESTES]**
> "Vemos que todos os componentes estão funcionando: banco de dados, APIs, mobile app, e integração IoT."

---

### **4. DEMONSTRAÇÃO FINAL (30 segundos)**

**[TELA: Mobile App - Dashboard IoT]**

**Narração:**
> "Para finalizar, vou simular um evento IoT em tempo real."

**[TELA: Terminal]**
```bash
curl -X POST http://localhost:5001/api/iot/eventos \
  -H "Content-Type: application/json" \
  -d '{"id":"EVT-DEMO-002","type":"bateria_baixa","deviceId":"SENSOR001"}'
```

**[VOLTAR PARA APP - REFRESH]**
> "O alerta aparece imediatamente no dashboard, demonstrando a integração em tempo real."

**[TELA: Documentação]**
> "Nossa solução está completamente documentada com guias de instalação, arquitetura, e códigos-fonte."

---

### **CONCLUSÃO (30 segundos)**

**[TELA: Resumo da Arquitetura]**

**Narração:**
> "Resumindo nossa integração multidisciplinar:"
> "- Database: Oracle relacional + MongoDB NoSQL com procedures e functions"
> "- Mobile: React Native completo com IoT Dashboard e internacionalização"  
> "- IoT: APIs Java e Python com sistema de alertas e fallback automático"

> "Esta é uma solução real de mercado, escalável e robusta, que demonstra domínio técnico nas três disciplinas do Challenge 2025."

> "Obrigado pela atenção!"

---

## 📝 **CHECKLIST PRÉ-GRAVAÇÃO**

### ✅ **Preparação Técnica**
- [ ] Sistema iniciado com `./start_integration.sh`
- [ ] APIs Java (8080) e Python (5001) funcionando
- [ ] H2 Console acessível
- [ ] MongoDB rodando (opcional)
- [ ] Mobile app compilado e funcionando
- [ ] Simulador/dispositivo configurado

### ✅ **Preparação de Telas**
- [ ] H2 Console aberto e logado
- [ ] Terminal com comandos preparados
- [ ] Browser com abas das APIs
- [ ] Mobile app aberto na tela inicial
- [ ] Documentação aberta

### ✅ **Dados de Teste**
- [ ] Placa ABC1234 cadastrada
- [ ] Dispositivos IoT com dados
- [ ] Alertas de exemplo
- [ ] Usuário de teste logado

---

## 🎯 **PONTOS-CHAVE A DESTACAR**

### 📊 **Database (25 pontos cada)**
1. **Procedures com tratamento de exceções** - Mostrar SP_REGISTRAR_EVENTO_IOT
2. **Export JSON** - Mencionar script de exportação
3. **MongoDB** - Consultas NoSQL funcionando
4. **Modelo físico** - 8 novas tabelas IoT

### 📱 **Mobile (10 pontos cada)**
1. **Telas funcionais** - Dashboard IoT integrado
2. **Integração API** - Busca por placa com fallback
3. **Push notifications** - Sistema configurado
4. **Internacionalização** - PT/ES funcionando
5. **Temas** - Claro/escuro
6. **Publicação** - Firebase App Distribution

### 🌐 **IoT/Java**
1. **APIs integradas** - Java + Python
2. **Endpoints multi-disciplinares** - Mobile/Java/.NET
3. **Sistema de alertas** - Tempo real
4. **Arquitetura híbrida** - Relacional + NoSQL

---

## ⏱️ **CRONOMETRAGEM**
- **00:00-00:30** - Introdução e arquitetura
- **00:30-02:30** - Database (Oracle + MongoDB)
- **02:30-04:30** - Mobile App (React Native)
- **04:30-05:30** - IoT e APIs
- **05:30-06:00** - Conclusão

---

## 📹 **CONFIGURAÇÕES DE GRAVAÇÃO**
- **Resolução**: 1080p mínimo
- **FPS**: 30fps
- **Áudio**: Narração clara e sem ruído
- **Tela**: Captura completa do desktop
- **Cursor**: Destacar cliques importantes

---

**🎬 Boa gravação! Sistema 100% pronto para demonstração.**
