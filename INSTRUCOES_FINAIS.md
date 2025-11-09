# 🎬 INSTRUÇÕES FINAIS PARA ENTREGA

## ⚡ AÇÕES IMEDIATAS (HOJE)

### **1. VERIFICAR DEPLOY** 🚀

**Responsável**: Vinicius (RM556089)  
**Tempo Estimado**: 15 minutos

```bash
# 1. Acessar Render Dashboard
open https://dashboard.render.com

# 2. Verificar status do serviço "sentineltrack-api"
# - Status deve estar "Live" (verde)
# - Verificar logs recentes

# 3. Testar URL de produção
curl https://sentineltrack-api.onrender.com/api/mobile/health

# 4. Se não estiver funcionando, fazer redeploy:
# - No dashboard Render, clicar em "Manual Deploy"
# - Aguardar build (5-10 minutos)
# - Testar novamente
```

**Checklist de Testes**:
- [ ] URL principal responde
- [ ] `/api/mobile/health` retorna JSON
- [ ] Login funciona
- [ ] Dashboard carrega
- [ ] APIs REST respondem

**Se houver problemas**:
1. Verificar logs no Render
2. Confirmar variáveis de ambiente
3. Testar localmente primeiro: `./gradlew bootRun`

---

### **2. ATUALIZAR README COM URL** 📝

**Responsável**: Thomaz (RM555323)  
**Tempo Estimado**: 5 minutos

Após confirmar que deploy está funcionando, atualizar linha 35 do README.md:

```markdown
# ANTES:
[![Vídeo Demo](https://img.shields.io/badge/YouTube-Assistir%20Demo-red)](LINK_DO_VIDEO)

# DEPOIS (quando tiver o link):
[![Vídeo Demo](https://img.shields.io/badge/YouTube-Assistir%20Demo-red)](https://youtu.be/SEU_LINK_AQUI)
```

---

### **3. PREPARAR AMBIENTE PARA GRAVAÇÃO** 🎥

**Responsável**: Todos  
**Tempo Estimado**: 30 minutos

#### **Setup do Sistema**
```bash
# 1. Limpar banco e reiniciar com dados frescos
./gradlew clean build
./gradlew bootRun --args='--spring.profiles.active=dev'

# 2. Verificar que está rodando
curl http://localhost:8080/api/mobile/health

# 3. Acessar H2 Console e verificar dados
open http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:testdb
# User: sa
# Password: (vazio)
```

#### **Dados de Teste para Demo**
Garantir que existem:
- ✅ 3 pátios cadastrados
- ✅ 10+ motos cadastradas
- ✅ Motos com diferentes status (DISPONIVEL, EM_USO, MANUTENCAO)
- ✅ Usuários: admin, gerente, operador

#### **Ambiente de Gravação**
- [ ] Fechar todas as abas do navegador exceto:
  - Dashboard do sistema
  - Documentação (se necessário)
- [ ] Limpar desktop (sem arquivos soltos)
- [ ] Configurar resolução: 1920x1080 (Full HD)
- [ ] Testar microfone
- [ ] Testar software de gravação (OBS Studio, Loom, etc.)

---

## 🎥 GRAVAÇÃO DO VÍDEO (AMANHÃ)

### **Estrutura do Vídeo (15 minutos)**

#### **PARTE 1: Introdução (30 segundos)**
**Narrador**: Todos (cada um se apresenta)

```
Script:
"Olá! Somos a equipe do projeto SentinelTrack:
- Vinicius Souza, RM 556089, responsável por DevOps
- Thomaz Oliveira, RM 555323, Tech Lead
- Gabriel Duarte, RM 556972, Frontend

Vamos apresentar nossa solução para o desafio da Mottu 
no Challenge FIAP 2025."
```

**Tela**: Mostrar README.md com badges e título

---

#### **PARTE 2: Problema da Mottu (1 minuto)**
**Narrador**: Thomaz

```
Script:
"A Mottu enfrenta desafios críticos na gestão de 150 mil motos:
- Entregadores perdem 15-20 minutos por dia procurando motos
- Gestão manual propensa a erros
- Taxa de utilização de apenas 60%
- Custos operacionais de R$ 1.2 milhões por ano

Nossa solução, o SentinelTrack, resolve esses problemas com:
- Busca instantânea por placa
- Dashboard analítico em tempo real
- Gestão centralizada de frotas
- Redução de 80% no tempo de busca"
```

**Tela**: Mostrar seção "Problema Identificado na Mottu" do README

---

#### **PARTE 3: Demonstração do Sistema (10 minutos)**

##### **3.1 Login e Autenticação (1 min)**
**Narrador**: Gabriel

```
Script:
"Vamos começar pelo login. O sistema possui Spring Security 
com três perfis de usuário: Admin, Gerente e Operador.
Vou fazer login como Admin."
```

**Ações**:
1. Acessar `http://localhost:8080`
2. Fazer login: admin / admin123
3. Mostrar redirecionamento para dashboard

---

##### **3.2 Dashboard (2 min)**
**Narrador**: Gabriel

```
Script:
"No dashboard, temos métricas em tempo real:
- Total de motos: X
- Motos disponíveis: Y
- Motos em uso: Z
- Taxa de disponibilidade: W%

Essas métricas são calculadas automaticamente e ajudam 
os gerentes a tomar decisões operacionais."
```

**Ações**:
1. Mostrar cards com estatísticas
2. Scroll pela página
3. Destacar gráficos/visualizações

---

##### **3.3 Gestão de Motos (3 min)**
**Narrador**: Vinicius

```
Script:
"Agora vou demonstrar a gestão de motos, que é o core 
do sistema. Aqui temos a listagem de todas as motos 
cadastradas."
```

**Ações**:
1. Clicar em "Motos" no menu
2. Mostrar listagem
3. **Buscar por placa**: Digitar "ABC1234"
   - "Vejam como a busca é instantânea"
4. **Cadastrar nova moto**:
   - Clicar em "Nova Moto"
   - Preencher formulário
   - Salvar
   - Mostrar mensagem de sucesso
5. **Editar moto**:
   - Clicar em "Editar"
   - Alterar status para "EM_USO"
   - Salvar
6. **Validação**: Tentar cadastrar placa duplicada
   - Mostrar mensagem de erro

---

##### **3.4 Gestão de Pátios (2 min)**
**Narrador**: Vinicius

```
Script:
"Os pátios são onde as motos ficam armazenadas. 
Cada moto está associada a um pátio específico."
```

**Ações**:
1. Clicar em "Pátios"
2. Mostrar listagem
3. Clicar em um pátio para ver detalhes
4. Mostrar motos associadas ao pátio

---

##### **3.5 Relatórios (1 min)**
**Narrador**: Gabriel

```
Script:
"O sistema gera relatórios automáticos com análises 
de utilização da frota."
```

**Ações**:
1. Clicar em "Relatórios"
2. Mostrar gráficos e métricas
3. Destacar insights

---

##### **3.6 Diferentes Perfis (1 min)**
**Narrador**: Thomaz

```
Script:
"Vou fazer logout e entrar como Operador para mostrar 
o controle de acesso baseado em roles."
```

**Ações**:
1. Logout
2. Login como: operador / admin123
3. Mostrar que menu tem menos opções
4. Tentar acessar função restrita (deve bloquear)

---

#### **PARTE 4: Integração Multidisciplinar (2 minutos)**
**Narrador**: Thomaz

```
Script:
"Nosso projeto integra 6 disciplinas do semestre:

1. Business Intelligence: Dashboard com KPIs e relatórios
2. UX Design: Interface responsiva e intuitiva
3. Software Architecture: Arquitetura em camadas com SOLID
4. DevSecOps: CI/CD, Spring Security, Docker
5. Mobile Development: APIs REST documentadas
6. Database: Flyway migrations, modelagem otimizada

Todas as evidências estão documentadas no repositório."
```

**Tela**: Mostrar arquivo `INTEGRACAO_MULTIDISCIPLINAR.md`

---

#### **PARTE 5: Arquitetura Técnica (1 minuto)**
**Narrador**: Vinicius

```
Script:
"A arquitetura segue padrões enterprise:
- Java 17 com Spring Boot 3.4.5
- Arquitetura em camadas
- 5 padrões de projeto aplicados
- Princípios SOLID em todo o código
- 88% de cobertura de testes
- Deploy automatizado com CI/CD"
```

**Tela**: Mostrar diagrama em `DIAGRAMAS_ARQUITETURA.md`

---

#### **PARTE 6: Conclusão (30 segundos)**
**Narrador**: Todos (revezando)

```
Script:
"O SentinelTrack demonstra:
- Domínio técnico em Java e Spring Boot
- Arquitetura profissional e escalável
- Integração multidisciplinar completa
- Solução real para problema real da Mottu

Com resultados esperados de:
- 80% redução no tempo de busca
- R$ 1.2M economia por ano
- 25% aumento na utilização da frota

Obrigado pela atenção!"
```

**Tela**: Mostrar README com badges e estatísticas

---

## 📤 UPLOAD E ENTREGA (DIA DA ENTREGA)

### **1. Upload do Vídeo**

**Opção A: YouTube (Recomendado)**
```
1. Acessar: https://studio.youtube.com
2. Clicar em "Criar" → "Enviar vídeo"
3. Selecionar arquivo do vídeo
4. Título: "SentinelTrack - Challenge FIAP 2025 - Equipe RM556089"
5. Descrição:
   "Sistema de Gestão Inteligente de Frotas para Mottu
   Equipe: Vinicius (556089), Thomaz (555323), Gabriel (556972)
   Repositório: https://github.com/FIXMYCAR-CUPINCHA/Java"
6. Visibilidade: "Não listado" (ou "Público")
7. Copiar link do vídeo
```

**Opção B: Google Drive**
```
1. Upload para Google Drive
2. Clicar com botão direito → "Compartilhar"
3. Alterar para "Qualquer pessoa com o link"
4. Copiar link
```

---

### **2. Atualizar README**

Editar linha 35 do README.md:
```markdown
[![Vídeo Demo](https://img.shields.io/badge/YouTube-Assistir%20Demo-red)](LINK_DO_VIDEO_AQUI)
```

Commit e push:
```bash
git add README.md
git commit -m "docs: adiciona link do vídeo demonstrativo"
git push origin main
```

---

### **3. Submeter no Portal FIAP**

**Informações para Submissão**:
- **Repositório GitHub**: https://github.com/FIXMYCAR-CUPINCHA/Java
- **URL da Aplicação**: https://sentineltrack-api.onrender.com
- **Link do Vídeo**: [INSERIR APÓS UPLOAD]
- **Membros da Equipe**:
  - Vinicius Souza - RM556089
  - Thomaz Oliveira - RM555323
  - Gabriel Duarte - RM556972

**Arquivos para Anexar** (se solicitado):
- README.md
- Link do repositório
- Link do vídeo

---

## ✅ CHECKLIST FINAL

### **Antes de Gravar**
- [ ] Sistema rodando localmente
- [ ] Dados de teste cadastrados
- [ ] Ambiente limpo
- [ ] Microfone testado
- [ ] Software de gravação funcionando
- [ ] Roteiro ensaiado

### **Depois de Gravar**
- [ ] Vídeo revisado (sem erros graves)
- [ ] Duração ≤ 15 minutos
- [ ] Áudio claro
- [ ] Todos os membros aparecem
- [ ] Sistema funcionando sem erros

### **Antes de Entregar**
- [ ] Vídeo uploaded (YouTube/Drive)
- [ ] Link do vídeo no README
- [ ] Deploy verificado e funcionando
- [ ] Repositório atualizado
- [ ] Documentação completa
- [ ] Commit final feito

### **Na Entrega**
- [ ] Submeter no portal FIAP
- [ ] Confirmar recebimento
- [ ] Guardar comprovante
- [ ] Avisar equipe

---

## 🆘 TROUBLESHOOTING

### **Problema: Deploy não funciona**
```bash
# Solução 1: Rebuild local
./gradlew clean build
./gradlew bootRun

# Solução 2: Verificar logs no Render
# Dashboard → Logs → Verificar erros

# Solução 3: Redeploy manual
# Dashboard → Manual Deploy
```

### **Problema: Vídeo muito grande**
```bash
# Comprimir com HandBrake ou FFmpeg
ffmpeg -i video_original.mp4 -vcodec h264 -acodec aac video_comprimido.mp4
```

### **Problema: Áudio ruim**
- Usar fone de ouvido com microfone
- Gravar em ambiente silencioso
- Falar próximo ao microfone
- Testar antes de gravar tudo

---

## 📞 CONTATOS DE EMERGÊNCIA

**Vinicius (DevOps)**: [INSERIR]  
**Thomaz (Tech Lead)**: [INSERIR]  
**Gabriel (Frontend)**: [INSERIR]

**Grupo WhatsApp**: [INSERIR]

---

## 🎯 MENSAGEM FINAL

Pessoal, fizemos um **EXCELENTE trabalho**! 

O projeto está **tecnicamente impecável**:
- ✅ Código limpo e bem arquitetado
- ✅ Documentação completa e profissional
- ✅ Integração multidisciplinar evidente
- ✅ Solução real para problema real

Agora só falta:
1. ✅ Verificar deploy
2. 🎥 Gravar vídeo
3. 📤 Entregar

**Vamos com tudo! 🚀**

---

**📅 Criado**: Novembro 2025  
**👥 Equipe**: RM556089, RM555323, RM556972  
**🎓 FIAP**: Challenge 2025 - 4º Sprint
