# SentinelTrack – Backend Java (Spring Boot)

> Sistema backend desenvolvido para gestão de usuários, funcionários, motos e endereços, com autenticação e integração com banco de dados Oracle.

---

## Tecnologias

- **Java 21**  
- **Spring Boot 3.4.x** (Web, Security, JPA, Validation, Thymeleaf, Cache)  
- **Oracle 19c**  
- **Flyway** (migrações de banco)  
- **Maven**  
- **Docker**

---

## Execução

```bash
git clone https://github.com/Challenge-Mottu-2025/DockerJava.git <- Mudar para o nosso repositório
cd DockerJava
mvn clean package
mvn spring-boot:run
```

### Docker
```bash
mvn clean package -DskipTests
docker build -t sentineltrack:latest .
docker run -e DB_USER=... -e DB_PASS=... -p 8080:8080 sentineltrack:latest
```

---

## Login padrão (ambiente DEV)

- **Usuário (CPF):** 99999999999  
- **Senha:** definir localmente (hash BCrypt)

---

## Participantes

| Nome | RM | GitHub |
|------|----|---------|
| Thomaz Oliveira gato | 555323 | [ThomazBartol](https://github.com/ThomazBartol) |
| Vinicius Souza sherek | 556089 | [SouzaEu](https://github.com/SouzaEu) |
| Gabriel Duarte lindo | 556972 | [gabrielduart7](https://github.com/gabrielduart7) |

---