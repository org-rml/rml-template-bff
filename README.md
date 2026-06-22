# rml-template-bff

Template para criação de serviços **BFF (Backend for Frontend)** no padrão `rml`.  
Marcar como **Template Repository** no GitHub para uso em novos projetos.

---

## Visão Geral

| Item | Valor |
|------|-------|
| **Tipo** | BFF (Backend for Frontend) |
| **Padrão** | MVC — Controller → Service → Feign Client |
| **Porta** | `8081` (BFF) / SRV em `8080` |
| **Java** | 21 (Eclipse Temurin) |
| **Framework** | Spring Boot 3.4.5 + Spring Cloud OpenFeign + OkHttp |
| **Segurança** | Sem Spring Security — autenticação via Gateway/infraestrutura |
| **Imagem Docker** | `ghcr.io/org-rml/rml-bff-br.com.{projeto}:latest` |

> O BFF **não tem banco de dados próprio**. Recebe requisições do FED (Angular/React),
> e delega para os SRVs via Feign Client. A autenticação é responsabilidade do Gateway.

---

## Estrutura

```
src/main/java/br/com/rml/BFF_PROJECT/
│
├── Application.java                         ← @SpringBootApplication + @EnableFeignClients
│
├── adapter/
│   └── exception/
│       └── handler/
│           ├── RestExceptionHandler.java    ← @ControllerAdvice, extends ResponseEntityExceptionHandler
│           └── response/
│               └── ApiErroResponse.java     ← payload de erro padronizado
│
├── controller/
│   ├── ProductController.java               ← REST endpoints (sem annotations Swagger)
│   └── SwaggerProductController.java        ← interface com @Operation/@ApiResponse
│
├── service/
│   └── ProductService.java                  ← orquestra chamadas aos Feign clients
│
├── client/
│   ├── ProductClient.java                   ← @FeignClient → chama o SRV correspondente
│   └── dto/
│       └── ProductClientResponseDto.java    ← record: espelha a resposta do SRV
│
├── dto/
│   ├── request/
│   │   └── ProductRequestDto.java           ← record com validações (@NotBlank, @NotNull)
│   └── response/
│       └── ProductResponseDto.java          ← record de saída para o FED
│
├── mapper/
│   └── ProductMapper.java                   ← MapStruct estático (INSTANCE = Mappers.getMapper())
│
└── config/
    ├── ForkJoinConfig.java                  ← pool de threads para CompletableFuture paralelo
    └── SpringDocConfig.java                 ← Swagger/OpenAPI
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
| `ProductClient` | nome do client Feign | `PedidoClient`, `ClienteClient` |
| `ProductController` | nome do controller | `PedidoController` |
| `ProductService` | nome do service | `PedidoService` |
| `SwaggerProductController` | interface Swagger | `SwaggerPedidoController` |

### 3. Criar o config repo
GitHub → **New repository** → **Template: `org-rml/rml-template-srv-config`**  
Nome: `rml-bff-{projeto}-config` → ex: `rml-bff-sovarais-config`

---

## Fluxo de Dados

```
FED (Angular/React)
    │ HTTPS
    ▼
Gateway (autenticação)
    │
    ▼
BFF Controller  (:8081)
    │
    ▼
BFF Service
    │ Feign + OkHttp (REST interno K8s)
    ▼
SRV (rml-srv-*-{projeto})  (:8080)
    │
    ▼
DB (Postgres/outro)
```

---

## Padrões aplicados

### Controller + Interface Swagger
O controller implementa uma interface separada que contém apenas as annotations do OpenAPI,
mantendo o controller limpo:

```java
// Interface — só Swagger
@Tag(name = "ProductController")
public interface SwaggerProductController {
    @Operation(summary = "Lista todos os produtos")
    ResponseEntity<List<ProductResponseDto>> findAll();
}

// Controller — só lógica HTTP
@RestController
public class ProductController implements SwaggerProductController {
    public ResponseEntity<List<ProductResponseDto>> findAll() { ... }
}
```

### MapStruct estático
```java
@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
}

// Uso no service — sem injeção Spring
ProductMapper.INSTANCE.toResponse(clientResponse);
```

### Chamadas paralelas com ForkJoinPool
Para BFFs que consomem múltiplos SRVs em paralelo:
```java
@RequiredArgsConstructor
public class MeuService {
    private final ForkJoinPool forkJoinPool;

    public MeuResponseDto buscarTudo() {
        var a = CompletableFuture.supplyAsync(() -> clienteClient.findAll(), forkJoinPool);
        var b = CompletableFuture.supplyAsync(() -> pedidoClient.findAll(), forkJoinPool);
        CompletableFuture.allOf(a, b).join();
        // ...
    }
}
```

---

## Configuração

### Variáveis de Ambiente

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `SERVER_PORT` | `8081` | Porta do BFF |
| `SERVER_PARALLELISM` | `200` | Threads do ForkJoinPool |
| `SRV_PRODUCT_URL` | `http://localhost:8080` | URL do SRV (K8s: `http://rml-srv-{dominio}-{projeto}`) |

---

## Executando Localmente

```bash
# 1. Sobe o SRV (porta 8080)
cd ../rml-template-srv && mvn spring-boot:run

# 2. Sobe o BFF (porta 8081)
mvn clean spring-boot:run -s ~/.m2/settings-personal.xml
```

Swagger: `http://localhost:8081/swagger-ui.html`

---

## CI/CD

Pipeline `.github/workflows/ci.yml`:

| Job | Trigger | Ação |
|-----|---------|------|
| `build` | push/PR | Maven build |
| `docker` | push | Build + push GHCR |
| `deploy-homolog` | push homolog | Bump `values-version.yaml` branch `homolog` |
| `deploy-production` | push main | Bump `values-version.yaml` branch `main` |

---

## Dependências Principais

| Dependência | Versão |
|-------------|--------|
| Spring Boot | 3.4.5 |
| Spring Cloud OpenFeign | 2024.0.1 |
| feign-okhttp | (gerenciado pelo BOM) |
| micrometer-tracing-bridge-otel | (gerenciado pelo BOM) |
| rml-common-core | 1.0.0-SNAPSHOT |
| MapStruct | 1.5.5 |
| Lombok | 1.18.38 |
