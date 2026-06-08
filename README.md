# rml-template-bff

Template para criação de serviços **BFF (Backend for Frontend)** no padrão `rml`.  
Marcar como **Template Repository** no GitHub para uso em novos projetos.

---

## Visão Geral

| Item | Valor |
|------|-------|
| **Tipo** | BFF (Backend for Frontend) |
| **Padrão** | MVC — Controller → Service → Feign Client |
| **Porta** | `8080` |
| **Java** | 21 (Eclipse Temurin) |
| **Framework** | Spring Boot 3.4.5 + Spring Cloud OpenFeign |
| **Imagem Docker** | `ghcr.io/org-rml/rml-bff-br.com.{projeto}:latest` |

> O BFF **não tem banco de dados próprio**. Ele recebe requisições do FED (Angular),
> valida o JWT emitido pelo `rml-srv-auth`, e delega para os SRVs via Feign Client.

---

## Estrutura

```
src/main/java/br/com/rml/BFF_PROJECT/
│
├── BffApplication.java          ← @SpringBootApplication + @EnableFeignClients
│
├── controller/
│   └── SampleController.java    ← REST endpoints expostos ao FED
│
├── service/
│   └── SampleService.java       ← orquestra chamadas aos Feign clients
│
├── client/
│   ├── SampleClient.java        ← @FeignClient → chama o SRV correspondente
│   └── dto/
│       └── SampleClientResponseDto.java  ← DTO de resposta do SRV
│
├── dto/
│   ├── request/
│   │   └── SampleRequestDto.java         ← extends BaseLongDTO
│   └── response/
│       └── SampleResponseDto.java        ← extends BaseLongDTO
│
├── mapper/
│   └── SampleMapper.java        ← MapStruct: ClientResponseDto → ResponseDto
│
└── config/
    ├── SecurityConfig.java      ← stateless, valida JWT
    ├── JwtAuthFilter.java       ← OncePerRequestFilter — valida token do srv-auth
    └── SpringDocConfig.java     ← Swagger com Bearer auth
```

---

## Como usar este template

### 1. Criar o repositório
GitHub → **New repository** → **Template: `org-rml/rml-template-bff`**  
Nome: `rml-bff-{projeto}` → ex: `rml-bff-sovarais`

### 2. Substituir os placeholders

| Placeholder | Substituir por | Exemplo |
|-------------|---------------|---------|
| `BFF_PROJECT` | nome do projeto | `sovarais` |
| `rml-bff-BFF_PROJECT` | nome do serviço | `rml-bff-sovarais` |
| `SampleClient` | nome do client Feign | `AuthClient`, `ClientServiceClient` |
| `SampleController` | nome do controller | `ClientController` |
| `SampleService` | nome do service | `ClientService` |

### 3. Criar o config repo
GitHub → **New repository** → **Template: `org-rml/rml-template-srv-config`**  
Nome: `rml-bff-{projeto}-config` → ex: `rml-bff-sovarais-config`

---

## Fluxo de Dados

```
FED (Angular)
    │ HTTPS + JWT
    ▼
BFF Controller
    │
    ▼
BFF Service
    │ Feign (REST interno K8s)
    ▼
SRV (rml-srv-*-{projeto})
    │
    ▼
DB (Postgres)
```

O BFF **valida** o JWT mas **não emite** — quem emite é o `rml-srv-auth`.

---

## Configuração

### Variáveis de Ambiente

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `SERVER_PORT` | `8080` | Porta da aplicação |
| `JWT_SECRET` | `rml-sovarais-auth-secret-key-must-be-at-least-32-chars` | Mesma chave do srv-auth |
| `SRV_BFF_PROJECT_URL` | `http://rml-srv-BFF_PROJECT` | URL do SRV (K8s service name) |

---

## Executando Localmente

```bash
mvn clean spring-boot:run -s ~/.m2/settings-personal.xml
```

Swagger: `http://localhost:8080/swagger-ui.html`

---

## CI/CD

Pipeline `.github/workflows/ci.yml`:

| Job | Trigger | Ação |
|-----|---------|------|
| `build` | push/PR | Maven build |
| `docker` | push | Build + push GHCR |
| `deploy-staging` | push develop | Bump `values-version.yaml` branch `staging` |
| `deploy-sandbox` | após staging + aprovação | Bump branch `sandbox` |
| `deploy-production` | push main + aprovação | Bump branch `production` |

---

## Dependências Principais

| Dependência | Versão |
|-------------|--------|
| Spring Boot | 3.4.5 |
| Spring Cloud OpenFeign | 2024.0.1 |
| rml-common | 1.0.0-SNAPSHOT |
| jjwt | 0.11.5 |
| MapStruct | 1.5.5 |
| Lombok | 1.18.38 |
