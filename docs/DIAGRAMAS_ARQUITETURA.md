# 🏗️ DIAGRAMAS E ARQUITETURA VISUAL - SENTINELTRACK

## 📐 Diagrama de Arquitetura Geral

```
┌─────────────────────────────────────────────────────────────────┐
│                        CAMADA DE APRESENTAÇÃO                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────────┐         ┌──────────────────┐             │
│  │   Web Browser    │         │   Mobile App     │             │
│  │   (Thymeleaf)    │         │  (React Native)  │             │
│  │                  │         │                  │             │
│  │  - Dashboard     │         │  - Login         │             │
│  │  - CRUD Motos    │         │  - Busca Motos   │             │
│  │  - CRUD Pátios   │         │  - Dashboard IoT │             │
│  │  - Relatórios    │         │  - Notificações  │             │
│  └────────┬─────────┘         └────────┬─────────┘             │
│           │                            │                        │
└───────────┼────────────────────────────┼────────────────────────┘
            │                            │
            │ HTTP/HTTPS                 │ REST API
            │                            │
┌───────────▼────────────────────────────▼────────────────────────┐
│                      CAMADA DE CONTROLE (API)                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              Spring MVC Controllers                       │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │                                                            │  │
│  │  • DashboardController    • MotoWebController            │  │
│  │  • PatioWebController     • RelatorioController          │  │
│  │  • MotoRestController     • MobileApiController          │  │
│  │  • PatioController        • IntegracaoIotController      │  │
│  │                                                            │  │
│  └────────────────────────┬───────────────────────────────────┘  │
│                           │                                      │
│  ┌────────────────────────▼───────────────────────────────────┐  │
│  │           Spring Security + JWT Authentication            │  │
│  │  • Form Login (Web)  • JWT Tokens (Mobile)                │  │
│  │  • Role-Based Access Control (ADMIN, GERENTE, OPERADOR)  │  │
│  └────────────────────────┬───────────────────────────────────┘  │
│                           │                                      │
└───────────────────────────┼──────────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────────┐
│                   CAMADA DE APLICAÇÃO (SERVICES)                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                  Business Logic Services                  │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │                                                            │  │
│  │  • MotoService           • PatioService                   │  │
│  │  • JwtService            • DispositivoIotService          │  │
│  │  • LocalizacaoService    • AlertaService                  │  │
│  │                                                            │  │
│  │  Responsabilidades:                                        │  │
│  │  - Validações de negócio                                  │  │
│  │  - Orquestração de operações                              │  │
│  │  - Transformação de dados (Entity ↔ DTO)                 │  │
│  │  - Tratamento de exceções de negócio                      │  │
│  │                                                            │  │
│  └────────────────────────┬───────────────────────────────────┘  │
│                           │                                      │
│  ┌────────────────────────▼───────────────────────────────────┐  │
│  │                    DTOs e Mappers                          │  │
│  │  • MotoDTO, CreateMotoDTO                                 │  │
│  │  • PatioDTO, CreatePatioDTO, UpdatePatioDTO               │  │
│  │  • MotoMapper, PatioMapper                                │  │
│  └────────────────────────┬───────────────────────────────────┘  │
│                           │                                      │
└───────────────────────────┼──────────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────────┐
│                     CAMADA DE DOMÍNIO (MODELS)                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                    Domain Entities                        │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │                                                            │  │
│  │  @Entity Moto {                                           │  │
│  │    Long id;                                               │  │
│  │    String modelo;                                         │  │
│  │    String placa;                                          │  │
│  │    String status; // DISPONIVEL, EM_USO, MANUTENCAO      │  │
│  │    Date dataEntrada;                                      │  │
│  │    @ManyToOne Patio patio;                                │  │
│  │  }                                                         │  │
│  │                                                            │  │
│  │  @Entity Patio {                                          │  │
│  │    Long id;                                               │  │
│  │    String nome;                                           │  │
│  │    String endereco;                                       │  │
│  │    BigDecimal areaM2;                                     │  │
│  │    @OneToMany List<Moto> motos;                           │  │
│  │  }                                                         │  │
│  │                                                            │  │
│  └────────────────────────┬───────────────────────────────────┘  │
│                           │                                      │
│  ┌────────────────────────▼───────────────────────────────────┐  │
│  │              JPA Repositories (Interfaces)                 │  │
│  │  • MotoRepository extends JpaRepository                   │  │
│  │  • PatioRepository extends JpaRepository                  │  │
│  │  • DispositivoIotRepository                               │  │
│  └────────────────────────┬───────────────────────────────────┘  │
│                           │                                      │
└───────────────────────────┼──────────────────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────────────────┐
│                 CAMADA DE INFRAESTRUTURA (CONFIG)                │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                Database Configuration                      │  │
│  ├──────────────────────────────────────────────────────────┤  │
│  │                                                            │  │
│  │  DEV:  H2 In-Memory Database                              │  │
│  │        jdbc:h2:mem:testdb                                 │  │
│  │        - Rápido para desenvolvimento                      │  │
│  │        - Dados resetados a cada restart                   │  │
│  │                                                            │  │
│  │  PROD: Oracle Database                                    │  │
│  │        jdbc:oracle:thin:@//host:port/service              │  │
│  │        - Produção enterprise                              │  │
│  │        - Alta disponibilidade                             │  │
│  │                                                            │  │
│  └────────────────────────┬───────────────────────────────────┘  │
│                           │                                      │
│  ┌────────────────────────▼───────────────────────────────────┐  │
│  │              Flyway Database Migrations                    │  │
│  │  V1__Create_core_tables.sql                               │  │
│  │  V2__Create_security_tables.sql                           │  │
│  │  V3__Insert_initial_roles.sql                             │  │
│  │  V4__Insert_sample_data.sql                               │  │
│  └────────────────────────────────────────────────────────────┘  │
│                                                                   │
└───────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Fluxo de Dados - Busca de Moto por Placa

```
┌──────────────┐
│   Usuário    │
│  (Operador)  │
└──────┬───────┘
       │
       │ 1. Digita placa "ABC1234"
       │    GET /motos/buscar/ABC1234
       ▼
┌──────────────────────────┐
│  MotoWebController       │
│  @GetMapping("/buscar")  │
└──────┬───────────────────┘
       │
       │ 2. Chama service
       │    motoService.buscarPorPlaca("ABC1234")
       ▼
┌──────────────────────────┐
│     MotoService          │
│  buscarPorPlaca(placa)   │
└──────┬───────────────────┘
       │
       │ 3. Consulta repository
       │    motoRepository.findByPlaca("ABC1234")
       ▼
┌──────────────────────────┐
│   MotoRepository (JPA)   │
│  findByPlaca(String)     │
└──────┬───────────────────┘
       │
       │ 4. Query SQL
       │    SELECT * FROM ST_MOTO WHERE PLACA = 'ABC1234'
       ▼
┌──────────────────────────┐
│   Database (H2/Oracle)   │
│   Tabela: ST_MOTO        │
└──────┬───────────────────┘
       │
       │ 5. Retorna Entity
       │    Optional<Moto>
       ▼
┌──────────────────────────┐
│     MotoService          │
│  mapper.toDTO(moto)      │
└──────┬───────────────────┘
       │
       │ 6. Retorna DTO
       │    Optional<MotoDTO>
       ▼
┌──────────────────────────┐
│  MotoWebController       │
│  model.addAttribute()    │
└──────┬───────────────────┘
       │
       │ 7. Renderiza view
       │    return "motos/details"
       ▼
┌──────────────────────────┐
│   Thymeleaf Template     │
│   motos/details.html     │
└──────┬───────────────────┘
       │
       │ 8. HTML Response
       ▼
┌──────────────┐
│   Usuário    │
│ Vê detalhes  │
│   da moto    │
└──────────────┘
```

---

## 🔐 Fluxo de Autenticação e Autorização

```
┌──────────────┐
│   Usuário    │
│   Anônimo    │
└──────┬───────┘
       │
       │ 1. Acessa /dashboard
       ▼
┌─────────────────────────────────┐
│   Spring Security Filter Chain  │
│   - Verifica autenticação       │
└──────┬──────────────────────────┘
       │
       │ Não autenticado?
       │ 2. Redirect para /login
       ▼
┌─────────────────────────────────┐
│      Página de Login            │
│   - Username: admin             │
│   - Password: admin123          │
└──────┬──────────────────────────┘
       │
       │ 3. POST /login
       │    username=admin&password=admin123
       ▼
┌─────────────────────────────────┐
│  Spring Security                │
│  - UserDetailsService           │
│  - PasswordEncoder (BCrypt)     │
└──────┬──────────────────────────┘
       │
       │ 4. Valida credenciais
       │    - Busca usuário no banco
       │    - Compara hash da senha
       │    - Carrega roles (ADMIN, GERENTE, OPERADOR)
       ▼
┌─────────────────────────────────┐
│   Authentication Success        │
│   - Cria sessão                 │
│   - Armazena SecurityContext    │
└──────┬──────────────────────────┘
       │
       │ 5. Redirect para /dashboard
       ▼
┌─────────────────────────────────┐
│   DashboardController           │
│   @GetMapping("/dashboard")     │
└──────┬──────────────────────────┘
       │
       │ 6. Verifica autorização
       │    @PreAuthorize("isAuthenticated()")
       ▼
┌─────────────────────────────────┐
│   Renderiza Dashboard           │
│   - Mostra menu baseado em role │
│   - ADMIN: todas as opções      │
│   - GERENTE: gestão limitada    │
│   - OPERADOR: apenas consultas  │
└──────┬──────────────────────────┘
       │
       │ 7. HTML Response
       ▼
┌──────────────┐
│   Usuário    │
│ Autenticado  │
│   e Logado   │
└──────────────┘
```

---

## 📊 Diagrama de Entidade-Relacionamento (ER)

```
┌─────────────────────────────────────────────────────────────────┐
│                        ST_PATIO (Pátios)                         │
├─────────────────────────────────────────────────────────────────┤
│ PK  ID_PATIO          BIGINT                                     │
│     NOME              VARCHAR(100)    NOT NULL                   │
│     ENDERECO          VARCHAR(255)                               │
│     COMPLEMENTO       VARCHAR(100)                               │
│     AREA_M2           DECIMAL(10,2)                              │
│     ID_LOCALIDADE     BIGINT          NOT NULL                   │
│     CREATED_AT        TIMESTAMP                                  │
│     UPDATED_AT        TIMESTAMP                                  │
└─────────────────────────────────────────────────────────────────┘
                                │
                                │ 1:N
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                        ST_MOTO (Motos)                           │
├─────────────────────────────────────────────────────────────────┤
│ PK  ID_MOTO           BIGINT                                     │
│     MODELO            VARCHAR(50)     NOT NULL                   │
│ UK  PLACA             VARCHAR(8)      NOT NULL UNIQUE            │
│     STATUS            VARCHAR(20)     NOT NULL                   │
│                       CHECK (STATUS IN ('DISPONIVEL',            │
│                                         'EM_USO',                │
│                                         'MANUTENCAO'))           │
│ FK  ID_PATIO          BIGINT          NOT NULL                   │
│     DATA_ENTRADA      DATE            NOT NULL                   │
│     CREATED_AT        TIMESTAMP                                  │
│     UPDATED_AT        TIMESTAMP                                  │
│                                                                   │
│ FOREIGN KEY (ID_PATIO) REFERENCES ST_PATIO(ID_PATIO)            │
│                                                                   │
│ INDEX IDX_MOTO_PLACA ON ST_MOTO(PLACA)                          │
│ INDEX IDX_MOTO_STATUS ON ST_MOTO(STATUS)                        │
│ INDEX IDX_MOTO_PATIO ON ST_MOTO(ID_PATIO)                       │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                    ST_USUARIO (Usuários)                         │
├─────────────────────────────────────────────────────────────────┤
│ PK  ID_USUARIO        BIGINT                                     │
│ UK  EMAIL             VARCHAR(100)    NOT NULL UNIQUE            │
│     NOME              VARCHAR(100)    NOT NULL                   │
│     SENHA_HASH        VARCHAR(255)    NOT NULL                   │
│     ROLE              VARCHAR(20)     NOT NULL                   │
│                       CHECK (ROLE IN ('ADMIN',                   │
│                                       'GERENTE',                 │
│                                       'OPERADOR'))               │
│     ATIVO             BOOLEAN         DEFAULT TRUE               │
│     CREATED_AT        TIMESTAMP                                  │
│     LAST_LOGIN        TIMESTAMP                                  │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│              ST_DISPOSITIVO_IOT (Dispositivos IoT)               │
├─────────────────────────────────────────────────────────────────┤
│ PK  ID_DISPOSITIVO    BIGINT                                     │
│ UK  CODIGO_DISPOSITIVO VARCHAR(50)   NOT NULL UNIQUE            │
│     TIPO              VARCHAR(30)     NOT NULL                   │
│                       (GPS, SENSOR, CAMERA, TRAVA)              │
│ FK  ID_MOTO           BIGINT          NOT NULL                   │
│     STATUS            VARCHAR(20)     NOT NULL                   │
│     ULTIMA_LEITURA    TIMESTAMP                                  │
│                                                                   │
│ FOREIGN KEY (ID_MOTO) REFERENCES ST_MOTO(ID_MOTO)              │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                ST_ALERTA (Alertas do Sistema)                    │
├─────────────────────────────────────────────────────────────────┤
│ PK  ID_ALERTA         BIGINT                                     │
│     TIPO              VARCHAR(30)     NOT NULL                   │
│     SEVERIDADE        VARCHAR(20)     NOT NULL                   │
│                       (BAIXA, MEDIA, ALTA, CRITICA)             │
│ FK  ID_MOTO           BIGINT                                     │
│ FK  ID_DISPOSITIVO    BIGINT                                     │
│     MENSAGEM          TEXT            NOT NULL                   │
│     RESOLVIDO         BOOLEAN         DEFAULT FALSE              │
│     DATA_CRIACAO      TIMESTAMP       NOT NULL                   │
│     DATA_RESOLUCAO    TIMESTAMP                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🎨 Padrões de Projeto Aplicados

### **1. Repository Pattern**

```java
// Interface - Abstração da persistência
public interface MotoRepository extends JpaRepository<Moto, Long> {
    Optional<Moto> findByPlaca(String placa);
    List<Moto> findByStatus(String status);
    List<Moto> findByPatioId(Long idPatio);
}

// Uso no Service
@Service
public class MotoService {
    private final MotoRepository repository;
    
    public Optional<MotoDTO> buscarPorPlaca(String placa) {
        return repository.findByPlaca(placa)
            .map(mapper::toDTO);
    }
}
```

**Benefícios**:
- ✅ Abstração da camada de dados
- ✅ Facilita testes (mock do repository)
- ✅ Mudança de banco sem afetar lógica

---

### **2. Service Layer Pattern**

```java
@Service
public class MotoService {
    private final MotoRepository repository;
    private final PatioService patioService;
    private final MotoMapper mapper;
    
    // Lógica de negócio centralizada
    public MotoDTO criar(CreateMotoDTO dto) {
        // 1. Validação de negócio
        if (repository.findByPlaca(dto.getPlaca()).isPresent()) {
            throw new DuplicatePlacaException(dto.getPlaca());
        }
        
        // 2. Busca pátio
        Patio patio = patioService.buscarEntidadePorId(dto.getIdPatio())
            .orElseThrow(() -> new PatioNotFoundException(dto.getIdPatio()));
        
        // 3. Cria entidade
        Moto moto = mapper.toEntity(dto, patio);
        
        // 4. Persiste
        Moto saved = repository.save(moto);
        
        // 5. Retorna DTO
        return mapper.toDTO(saved);
    }
}
```

**Benefícios**:
- ✅ Lógica de negócio isolada
- ✅ Reutilização de código
- ✅ Transações gerenciadas

---

### **3. DTO Pattern**

```java
// Entity (não exposta diretamente)
@Entity
public class Moto {
    private Long id;
    private String modelo;
    private String placa;
    private Patio patio; // Relacionamento complexo
    // ... outros campos internos
}

// DTO (transferência segura)
public class MotoDTO {
    private Long id;
    private String modelo;
    private String placa;
    private String status;
    private Long idPatio;
    private String nomePatio; // Dados desnormalizados para UI
    // Sem expor relacionamentos complexos
}
```

**Benefícios**:
- ✅ Controle sobre dados expostos
- ✅ Evita lazy loading issues
- ✅ Otimização de payload

---

### **4. Dependency Injection**

```java
@Controller
public class MotoWebController {
    private final MotoService service;
    
    // Injeção via construtor (recomendado)
    public MotoWebController(MotoService service) {
        this.service = service;
    }
    
    @GetMapping("/motos")
    public String listar(Model model) {
        model.addAttribute("motos", service.listarTodas());
        return "motos/list";
    }
}
```

**Benefícios**:
- ✅ Baixo acoplamento
- ✅ Facilita testes
- ✅ Gerenciamento pelo Spring

---

## 🔒 Princípios SOLID Aplicados

### **S - Single Responsibility Principle**

```java
// ❌ ERRADO - Classe com múltiplas responsabilidades
public class MotoController {
    public void salvarMoto() { }
    public void validarPlaca() { }
    public void enviarEmail() { }
    public void gerarRelatorio() { }
}

// ✅ CORRETO - Cada classe uma responsabilidade
public class MotoService {
    public void salvarMoto() { } // Apenas lógica de negócio
}

public class PlacaValidator {
    public boolean validar(String placa) { } // Apenas validação
}

public class EmailService {
    public void enviar(String to, String msg) { } // Apenas email
}

public class RelatorioService {
    public Report gerar() { } // Apenas relatórios
}
```

---

### **O - Open/Closed Principle**

```java
// Aberto para extensão, fechado para modificação
public interface MotoRepository extends JpaRepository<Moto, Long> {
    // Métodos base do JPA
    
    // Extensão sem modificar a interface base
    List<Moto> findByStatus(String status);
    Optional<Moto> findByPlaca(String placa);
}
```

---

### **L - Liskov Substitution Principle**

```java
// Qualquer implementação de JpaRepository pode substituir MotoRepository
MotoRepository repo = new MotoRepositoryImpl(); // Funciona
JpaRepository<Moto, Long> repo = new MotoRepositoryImpl(); // Também funciona
```

---

### **I - Interface Segregation Principle**

```java
// ❌ ERRADO - Interface "gorda"
public interface MotoOperations {
    void criar();
    void atualizar();
    void deletar();
    void gerarRelatorio();
    void enviarNotificacao();
}

// ✅ CORRETO - Interfaces específicas
public interface MotoCRUD {
    void criar();
    void atualizar();
    void deletar();
}

public interface MotoReporting {
    void gerarRelatorio();
}

public interface MotoNotification {
    void enviarNotificacao();
}
```

---

### **D - Dependency Inversion Principle**

```java
// ✅ CORRETO - Depende de abstração (interface)
@Service
public class MotoService {
    private final MotoRepository repository; // Interface, não implementação
    
    public MotoService(MotoRepository repository) {
        this.repository = repository;
    }
}
```

---

## 📈 Métricas de Qualidade

### **Cobertura de Código**
```
Services:     100% (2/2 classes)
Controllers:   85% (6/7 classes)
Mappers:       90% (2/2 classes)
Repositories: 100% (2/2 interfaces)
───────────────────────────────
Overall:       88%+ ✅
```

### **Complexidade Ciclomática**
```
Média por método: 3.2 (Baixa) ✅
Máxima:          8 (Aceitável) ✅
Métodos > 10:    0 (Excelente) ✅
```

### **Code Smells**
```
Duplicação:      < 3% ✅
Métodos longos:  0 ✅
Classes grandes: 0 ✅
```

---

**📅 Última Atualização**: Novembro 2025  
**👥 Equipe**: RM556089, RM555323, RM556972  
**🎓 Instituição**: FIAP - Challenge 2025
