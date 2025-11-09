# 🏗️ Arquitetura do Sistema SentinelTrack

## Visão Geral

O SentinelTrack é uma aplicação web desenvolvida com arquitetura em camadas (Layered Architecture) seguindo os princípios de Clean Architecture e Domain-Driven Design (DDD).

## Estrutura de Camadas

```
┌─────────────────────────────────────────────┐
│          API Layer (Controllers)            │
│  - REST Controllers (@RestController)       │
│  - Web Controllers (@Controller)            │
│  - Exception Handlers                       │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│       Application Layer (Services)          │
│  - Business Logic                           │
│  - DTOs (Data Transfer Objects)             │
│  - Mappers                                  │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│         Domain Layer (Models)               │
│  - Entities (@Entity)                       │
│  - Repositories (Interfaces)                │
│  - Business Rules                           │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│    Infrastructure Layer (Config)            │
│  - Database Configuration                   │
│  - Security Configuration                   │
│  - External Services                        │
└─────────────────────────────────────────────┘
```

## Componentes Principais

### 1. API Layer (`fiap.com.br.SentinelTrack.Api`)

**Controllers REST:**
- `MobileApiController` - APIs para aplicativo mobile
- `MotoRestController` - CRUD REST de motos
- `PatioController` - CRUD REST de pátios

**Controllers Web:**
- `DashboardController` - Dashboard principal
- `MotoWebController` - Interface web de motos
- `PatioWebController` - Interface web de pátios
- `RelatorioController` - Relatórios e analytics

**Exception Handling:**
- `GlobalExceptionHandler` - Tratamento centralizado
- `PatioNotFoundException` - Pátio não encontrado
- `DuplicatePlacaException` - Placa duplicada
- `MotoNotFoundException` - Moto não encontrada
- `BusinessException` - Exceções de negócio

### 2. Application Layer (`fiap.com.br.SentinelTrack.Application`)

**Services:**
- `MotoService` - Lógica de negócio de motos
- `PatioService` - Lógica de negócio de pátios
- `JwtService` - Autenticação JWT

**DTOs:**
- `MotoDTO` - Transferência de dados de moto
- `CreateMotoDTO` - Criação de moto
- `PatioDTO` - Transferência de dados de pátio
- `CreatePatioDTO` - Criação de pátio
- `UpdatePatioDTO` - Atualização de pátio
- `LoginRequestDTO` - Request de login

**Mappers:**
- `MotoMapper` - Conversão Entity ↔ DTO
- `PatioMapper` - Conversão Entity ↔ DTO

### 3. Domain Layer (`fiap.com.br.SentinelTrack.Domain`)

**Entities:**
```java
@Entity
class Moto {
    Long id;
    String modelo;
    String placa;
    String status; // DISPONIVEL, EM_USO, MANUTENCAO
    Date dataEntrada;
    Patio patio; // @ManyToOne
}

@Entity
class Patio {
    Long id;
    String nome;
    String endereco;
    String complemento;
    BigDecimal areaM2;
    Long idLocalidade;
    List<Moto> motos; // @OneToMany
}
```

**Repositories:**
- `MotoRepository extends JpaRepository`
- `PatioRepository extends JpaRepository`

### 4. Infrastructure Layer (`fiap.com.br.SentinelTrack.Infrastructure`)

**Configurações:**
- `SecurityConfig` - Spring Security
- `CorsConfig` - CORS para APIs
- `SwaggerConfig` - Documentação OpenAPI

## Padrões de Projeto Aplicados

### 1. **Repository Pattern**
Abstração da camada de persistência através de interfaces JPA.

```java
public interface MotoRepository extends JpaRepository<Moto, Long> {
    Optional<Moto> findByPlaca(String placa);
    List<Moto> findByStatus(String status);
    List<Moto> findByPatioId(Long idPatio);
}
```

### 2. **Service Layer Pattern**
Lógica de negócio centralizada nos services.

```java
@Service
public class MotoService {
    private final MotoRepository repository;
    private final MotoMapper mapper;
    // Business logic here
}
```

### 3. **DTO Pattern**
Transferência segura de dados entre camadas.

```java
public class MotoDTO {
    private Long id;
    private String modelo;
    private String placa;
    // Sem expor entidade diretamente
}
```

### 4. **Mapper Pattern**
Conversão entre entidades e DTOs.

```java
@Component
public class MotoMapper {
    public MotoDTO toDTO(Moto entity) { }
    public Moto toEntity(CreateMotoDTO dto) { }
}
```

### 5. **Dependency Injection**
Inversão de controle via Spring.

```java
@Controller
public class MotoWebController {
    private final MotoService service; // Injetado pelo Spring
    
    public MotoWebController(MotoService service) {
        this.service = service;
    }
}
```

## Princípios SOLID Aplicados

### ✅ **S - Single Responsibility Principle**
Cada classe tem uma única responsabilidade:
- `MotoService` - apenas lógica de motos
- `MotoMapper` - apenas conversão de dados
- `MotoWebController` - apenas controle web

### ✅ **O - Open/Closed Principle**
Classes abertas para extensão, fechadas para modificação:
- Interfaces de repositórios podem ser estendidas
- Services podem ser especializados via herança

### ✅ **L - Liskov Substitution Principle**
Implementações podem substituir abstrações:
- Qualquer `JpaRepository` pode substituir `MotoRepository`

### ✅ **I - Interface Segregation Principle**
Interfaces específicas e coesas:
- `MotoRepository` - apenas operações de moto
- `PatioRepository` - apenas operações de pátio

### ✅ **D - Dependency Inversion Principle**
Dependência de abstrações, não implementações:
- Services dependem de interfaces de repositórios
- Controllers dependem de interfaces de services

## Fluxo de Dados

### Exemplo: Criar Nova Moto

```
1. Cliente HTTP POST /api/mobile/motos
   ↓
2. MotoRestController recebe CreateMotoDTO
   ↓
3. Validação com @Valid (Bean Validation)
   ↓
4. MotoService.criar(createDTO)
   ↓
5. Validações de negócio:
   - Pátio existe?
   - Placa já cadastrada?
   ↓
6. MotoMapper.toEntity(dto, patio)
   ↓
7. MotoRepository.save(entity)
   ↓
8. MotoMapper.toDTO(savedEntity)
   ↓
9. Response HTTP 201 Created
```

## Segurança

### Spring Security
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    - Form Login para web
    - JWT para APIs mobile
    - Roles: ADMIN, GERENTE, OPERADOR
    - CSRF protection
    - Session management
}
```

### Autorização por Perfil
```java
@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
public String deletar(@PathVariable Long id) { }
```

## Banco de Dados

### Flyway Migrations
```
V1__Create_core_tables.sql
V2__Create_security_tables.sql
V3__Insert_initial_roles.sql
V4__Insert_sample_data.sql
```

### Estratégia de Ambientes
- **DEV:** H2 in-memory
- **PROD:** Oracle Database

## APIs REST

### Endpoints Mobile (`/api/mobile`)
```
GET    /health              - Health check
POST   /auth/login          - Autenticação JWT
GET    /dashboard           - Estatísticas
GET    /motos               - Listar motos
POST   /motos               - Criar moto
PUT    /motos/{id}          - Atualizar moto
DELETE /motos/{id}          - Deletar moto
GET    /patios              - Listar pátios
POST   /relatorios/uso      - Relatório de uso
POST   /sync                - Sincronização
```

### Endpoints Web (`/`)
```
GET  /dashboard            - Dashboard principal
GET  /motos                - Lista de motos
GET  /motos/new            - Formulário nova moto
POST /motos/new            - Criar moto
GET  /motos/edit/{id}      - Formulário editar
POST /motos/edit/{id}      - Atualizar moto
POST /motos/delete/{id}    - Deletar moto
GET  /patios               - Lista de pátios
GET  /relatorios           - Relatórios
```

## Tecnologias

- **Java 17** - Linguagem
- **Spring Boot 3.2.5** - Framework
- **Spring Data JPA** - Persistência
- **Spring Security** - Segurança
- **Thymeleaf** - Template engine
- **Flyway** - Migrações de BD
- **H2 / Oracle** - Bancos de dados
- **JWT** - Autenticação mobile
- **Lombok** - Redução de boilerplate
- **JUnit 5** - Testes
- **Mockito** - Mocks para testes
- **AssertJ** - Assertions fluentes

## Qualidade de Código

### Testes
- Testes unitários com JUnit 5 + Mockito
- Cobertura de testes > 80%
- Testes de integração com Spring Boot Test

### Boas Práticas
- Clean Code
- SOLID principles
- DRY (Don't Repeat Yourself)
- KISS (Keep It Simple, Stupid)
- YAGNI (You Aren't Gonna Need It)

## Deploy

### Docker
```dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test
CMD ["java", "-jar", "build/libs/SentinelTrack-0.0.1-SNAPSHOT.jar"]
```

### Render / Railway
- Build: `./build.sh`
- Start: `./start.sh`
- Port: `${PORT:-8080}`
- Profile: `prod`

## Escalabilidade

### Horizontal Scaling
- Stateless application
- JWT para autenticação (sem sessão)
- Load balancer ready

### Vertical Scaling
- Connection pooling
- Query optimization
- Caching strategies (futuro)

## Monitoramento

### Health Check
```
GET /api/mobile/health
{
  "status": "healthy",
  "service": "SentinelTrack Java API",
  "version": "1.0.0"
}
```

### Métricas (Futuro)
- Spring Boot Actuator
- Prometheus
- Grafana

## Roadmap

### Próximas Implementações
- [ ] Cache com Redis
- [ ] Message Queue (RabbitMQ/Kafka)
- [ ] Elasticsearch para busca
- [ ] GraphQL API
- [ ] WebSocket para real-time
- [ ] Kubernetes deployment
- [ ] CI/CD com GitHub Actions
- [ ] Testes E2E com Selenium
- [ ] Performance testing com JMeter
