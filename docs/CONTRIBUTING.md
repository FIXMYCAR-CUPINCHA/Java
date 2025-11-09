# 🤝 Guia de Contribuição - SentinelTrack

## Bem-vindo!

Obrigado por considerar contribuir com o SentinelTrack! Este documento fornece diretrizes para contribuir com o projeto.

## 📋 Índice

- [Código de Conduta](#código-de-conduta)
- [Como Contribuir](#como-contribuir)
- [Padrões de Código](#padrões-de-código)
- [Processo de Pull Request](#processo-de-pull-request)
- [Testes](#testes)
- [Documentação](#documentação)

## 🤝 Código de Conduta

- Seja respeitoso e inclusivo
- Aceite críticas construtivas
- Foque no que é melhor para a comunidade
- Mostre empatia com outros membros

## 🚀 Como Contribuir

### 1. Fork o Repositório

```bash
git clone https://github.com/FIXMYCAR-CUPINCHA/Java.git
cd Java
```

### 2. Crie uma Branch

```bash
git checkout -b feature/minha-feature
# ou
git checkout -b fix/meu-bugfix
```

### 3. Faça suas Alterações

Siga os padrões de código descritos abaixo.

### 4. Commit suas Mudanças

```bash
git add .
git commit -m "feat: adiciona nova funcionalidade X"
```

### 5. Push para o GitHub

```bash
git push origin feature/minha-feature
```

### 6. Abra um Pull Request

Descreva suas mudanças detalhadamente.

## 📝 Padrões de Código

### Convenção de Commits

Seguimos o [Conventional Commits](https://www.conventionalcommits.org/):

```
<tipo>(<escopo>): <descrição>

[corpo opcional]

[rodapé opcional]
```

**Tipos:**
- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Documentação
- `style`: Formatação (sem mudança de código)
- `refactor`: Refatoração
- `test`: Adição de testes
- `chore`: Tarefas de manutenção

**Exemplos:**
```bash
feat(moto): adiciona busca por placa
fix(patio): corrige validação de área
docs(readme): atualiza instruções de deploy
test(moto): adiciona testes unitários do service
```

### Estilo de Código Java

#### Nomenclatura

```java
// Classes: PascalCase
public class MotoService { }

// Métodos e variáveis: camelCase
public void buscarPorPlaca(String placa) { }

// Constantes: UPPER_SNAKE_CASE
public static final String STATUS_DISPONIVEL = "DISPONIVEL";

// Pacotes: lowercase
package fiap.com.br.sentineltrack.application.services;
```

#### Formatação

```java
// Indentação: 4 espaços
public class Example {
    private String field;
    
    public void method() {
        if (condition) {
            // código
        }
    }
}

// Chaves na mesma linha
if (condition) {
    // código
} else {
    // código
}

// Espaços ao redor de operadores
int result = a + b;
```

#### Comentários

```java
/**
 * Busca uma moto pela placa.
 * 
 * @param placa A placa da moto (formato ABC1234 ou ABC1D23)
 * @return Optional contendo a moto se encontrada
 * @throws DuplicatePlacaException se a placa já existe
 */
public Optional<MotoDTO> buscarPorPlaca(String placa) {
    // Implementação
}
```

### Princípios SOLID

Sempre aplique os princípios SOLID:

1. **Single Responsibility**: Uma classe, uma responsabilidade
2. **Open/Closed**: Aberto para extensão, fechado para modificação
3. **Liskov Substitution**: Subtipos devem ser substituíveis
4. **Interface Segregation**: Interfaces específicas
5. **Dependency Inversion**: Dependa de abstrações

### Clean Code

```java
// ❌ Ruim
public List<MotoDTO> getM() {
    List<Moto> m = motoRepository.findAll();
    List<MotoDTO> dtos = new ArrayList<>();
    for (Moto moto : m) {
        dtos.add(mapper.toDTO(moto));
    }
    return dtos;
}

// ✅ Bom
public List<MotoDTO> listarTodas() {
    return motoRepository.findAll()
        .stream()
        .map(mapper::toDTO)
        .collect(Collectors.toList());
}
```

## 🔄 Processo de Pull Request

### Checklist antes de abrir PR

- [ ] Código segue os padrões do projeto
- [ ] Testes unitários adicionados/atualizados
- [ ] Testes passando (`./gradlew test`)
- [ ] Documentação atualizada
- [ ] Commits seguem convenção
- [ ] Branch atualizada com `main`

### Template de PR

```markdown
## Descrição
Breve descrição das mudanças

## Tipo de Mudança
- [ ] Bug fix
- [ ] Nova funcionalidade
- [ ] Breaking change
- [ ] Documentação

## Como Testar
1. Passo 1
2. Passo 2

## Checklist
- [ ] Testes passando
- [ ] Documentação atualizada
- [ ] Code review solicitado
```

### Review Process

1. Pelo menos 1 aprovação necessária
2. CI/CD deve passar
3. Sem conflitos com `main`
4. Code review por membro sênior

## 🧪 Testes

### Executar Testes

```bash
# Todos os testes
./gradlew test

# Testes específicos
./gradlew test --tests MotoServiceTest

# Com cobertura
./gradlew test jacocoTestReport
```

### Escrever Testes

```java
@ExtendWith(MockitoExtension.class)
@DisplayName("MotoService - Testes Unitários")
class MotoServiceTest {
    
    @Mock
    private MotoRepository repository;
    
    @InjectMocks
    private MotoService service;
    
    @Test
    @DisplayName("Deve criar moto com sucesso")
    void deveCriarMotoComSucesso() {
        // Arrange
        CreateMotoDTO dto = new CreateMotoDTO();
        
        // Act
        MotoDTO result = service.criar(dto);
        
        // Assert
        assertThat(result).isNotNull();
    }
}
```

### Cobertura de Testes

- Mínimo: 80% de cobertura
- Services: 100% de cobertura
- Controllers: 80% de cobertura
- Mappers: 90% de cobertura

## 📚 Documentação

### Atualizar README

Sempre atualize o README.md quando:
- Adicionar nova funcionalidade
- Mudar configuração
- Adicionar dependência
- Alterar processo de deploy

### JavaDoc

```java
/**
 * Service responsável pela gestão de motos.
 * 
 * <p>Implementa operações CRUD e validações de negócio
 * relacionadas às motocicletas da frota Mottu.</p>
 * 
 * @author SentinelTrack Team
 * @version 1.0
 * @since 2025-01-01
 */
@Service
public class MotoService {
    // ...
}
```

### Swagger/OpenAPI

Documente APIs REST:

```java
@Operation(summary = "Buscar moto por placa")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Moto encontrada"),
    @ApiResponse(responseCode = "404", description = "Moto não encontrada")
})
@GetMapping("/motos/buscar/{placa}")
public ResponseEntity<MotoDTO> buscarPorPlaca(@PathVariable String placa) {
    // ...
}
```

## 🐛 Reportar Bugs

### Template de Issue

```markdown
**Descrição do Bug**
Descrição clara do problema

**Para Reproduzir**
1. Vá para '...'
2. Clique em '...'
3. Veja o erro

**Comportamento Esperado**
O que deveria acontecer

**Screenshots**
Se aplicável

**Ambiente**
- OS: [e.g. macOS]
- Java: [e.g. 17]
- Browser: [e.g. Chrome 120]
```

## 💡 Sugerir Melhorias

### Template de Feature Request

```markdown
**Problema a Resolver**
Qual problema esta feature resolve?

**Solução Proposta**
Descrição da solução

**Alternativas Consideradas**
Outras abordagens pensadas

**Contexto Adicional**
Qualquer informação relevante
```

## 🏆 Reconhecimento

Contribuidores serão adicionados ao README.md na seção de créditos.

## 📞 Contato

- **Email:** equipe.sentineltrack@fiap.com.br
- **Discord:** SentinelTrack Team
- **Issues:** [GitHub Issues](https://github.com/FIXMYCAR-CUPINCHA/Java/issues)

## 📄 Licença

Ao contribuir, você concorda que suas contribuições serão licenciadas sob a MIT License.

---

**Obrigado por contribuir com o SentinelTrack! 🏍️**
