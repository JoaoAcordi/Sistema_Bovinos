# Autoavaliação

Os 10 critérios que serão avaliados na revisão do código, descritos em `Aula 10-11/AVALIACAO.md`, no repositório da disciplina.

- ✅ atende
- ◐ atende em parte
- ❌ não atende

| Critério | Marca | Evidência |
|---|---|---|
| P1 A API sobe seguindo o README | ✅ | `README.md`, seção "Como executar" (suporta Docker PostgreSQL ou perfil local H2) |
| P2 Cada classe no pacote da sua camada | ✅ | `src/main/java/api/` dividido em model, repository, dto, mapper, service, controller, config |
| P3 Migrations e `@Entity` de todas as tabelas, e a API sobe | ✅ | Migrations `V1` a `V4` em `db/migration/` e entidades em `model/` |
| P4 DTOs de entrada e de saída de todas as entidades | ✅ | Todos os DTOs de entrada e saída com validações Bean Validation em `dto/` |
| P5 Repository, Mapper, Service e Controller de todas, com o comentário no topo, e o mapa da estrutura | ✅ | Todas as classes implementadas com o comentário descritivo no topo e mapa da estrutura no `README.md` |
| P6 API simples: `POST`, `GET` e `GET /{id}`, com `400` e `404` | ✅ | `PiqueteController` e `BoiController` implementam `POST`, `GET` e `GET /{id}` com respostas padronizadas `400` e `404` |
| P7 No mínimo 3 regras de negócio implementadas, com `400` quando violadas | ✅ | 4 regras implementadas: R1, R2, R3 e R4, todas lançando `RegraDeNegocioException` (HTTP 400) |
| P8 As regras atendem ao problema e ao tema | ✅ | Tema Agronegócio: microchip único, limite de lotação em piquetes, restrição para animais inativos e pesagem obrigatória antes do abate/venda |
| P9 Código igual ao desenho | ✅ | `docs/modelo-de-dominio.md` e `docs/contrato-da-api.md` totalmente alinhados com o código |
| P10 README completo e bem escrito | ✅ | `README.md` preenchido sem tags pendentes, com exemplos, tabelas e guia de execução |

**Pontos que o grupo espera no projeto:** 5,0
