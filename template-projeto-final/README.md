# BoiChip

API REST para controle do rebanho de uma fazenda de gado de corte por meio do microchip de cada boi: cadastro dos animais, pesagens, troca de piquete e baixa (venda, abate ou morte). Feita para o gestor da fazenda e para o peão de campo que registra o dia a dia do rebanho.

Projeto Final da disciplina de Backend (Engenharia de Software, SATC).

## Índice

- [Integrantes](#integrantes)
- [O problema](#o-problema)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Entidades](#entidades)
- [Rotas](#rotas)
- [Como executar](#como-executar)
- [Situação do projeto](#situação-do-projeto)
- [Documentação](#documentação)

## Integrantes

| Nome | GitHub |
|---|---|
| JOÃO PEDRO TAUFEMBACH ACORDI | [@JoaoAcordi](https://github.com/JoaoAcordi) |
| KAUANY HAHN SARTOR | [@kauanysartor](https://github.com/kauanysartor) |
| RAFAELLA DE SOUZA BENEDET | [@rafinha-benedet](https://github.com/rafinha-benedet) |

## O problema

**Tema sorteado:** Agronegócio

O controle individual dos animais é um dos pilares de uma fazenda profissional. Do ponto de vista produtivo e financeiro, saber quantos bois a fazenda possui, onde cada um está e quanto pesa permite planejar o melhor momento da venda, dimensionar pastagem e ração e diminuir perdas.

O BoiChip adota o microchip como identidade única de cada animal, centralizando o cadastro, histórico de pesagens e movimentação entre piquetes, garantindo automaticamente as regras de manejo.

**Quem usa o sistema:**

| Quem | O que faz no sistema |
|---|---|
| Gestor da fazenda | Cadastra piquetes e bois, monitora o rebanho e dá baixa em animais vendidos, abatidos ou mortos |
| Peão de campo | Registra pesagens e transfere bois entre piquetes a partir do microchip |

**Regras de negócio:**

| # | Regra | Onde está no código | Rota que usa |
|---|---|---|---|
| R1 | Não pode existir dois bois com o mesmo número de microchip | `BoiService` | `POST /bois` |
| R2 | Um piquete não pode receber mais bois ativos do que a sua capacidade máxima | `BoiService` e `MovimentacaoService` | `POST /bois`, `POST /movimentacoes` |
| R3 | Só um boi com status `ATIVO` pode ser pesado, movimentado ou receber baixa | `PesagemService`, `MovimentacaoService` e `BoiService` | `POST /pesagens`, `POST /movimentacoes`, `PATCH /bois/{id}/baixa` |
| R4 | Um boi só pode ser baixado como `VENDIDO` ou `ABATIDO` se tiver ao menos uma pesagem registrada | `BoiService` | `PATCH /bois/{id}/baixa` |

**Fora do escopo:** controle de vacinação/sanidade, financeiro com valores em reais, múltiplas fazendas, autenticação/login e leitor físico por hardware.

## Tecnologias

| O quê | Qual |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.5 (Web, Validation, Data JPA) |
| Banco de dados | PostgreSQL 16 (Docker) / H2 (perfil local) |
| Migrations | Flyway |
| Documentação da API | springdoc-openapi (Swagger UI) |

## Arquitetura

A API é organizada em camadas: Controller -> Service -> Repository / Mapper.

| Entidade | Migration | Model | Repository | DTOs | Mapper | Service | Controller |
|---|---|---|---|---|---|---|---|
| Piquete | `V1__criar_piquetes.sql`, pronta | `Piquete`, pronta | `PiqueteRepository`, pronto | `PiqueteRequestDTO`, `PiqueteResponseDTO`, prontos | `PiqueteMapper`, implementado | `PiqueteService`, implementado | `PiqueteController`, implementado |
| Boi | `V2__criar_bois.sql`, pronta | `Boi`, `StatusBoi`, pronta | `BoiRepository`, pronto | `BoiRequestDTO`, `BoiResponseDTO`, `BaixaRequestDTO`, prontos | `BoiMapper`, implementado | `BoiService` (R1, R2, R3, R4), implementado | `BoiController`, implementado |
| Pesagem | `V3__criar_pesagens.sql`, pronta | `Pesagem`, pronta | `PesagemRepository`, pronto | `PesagemRequestDTO`, `PesagemResponseDTO`, prontos | `PesagemMapper`, implementado | `PesagemService` (R3), implementado | `PesagemController`, implementado |
| Movimentacao | `V4__criar_movimentacoes.sql`, pronta | `Movimentacao`, pronta | `MovimentacaoRepository`, pronto | `MovimentacaoRequestDTO`, `MovimentacaoResponseDTO`, prontos | `MovimentacaoMapper`, implementado | `MovimentacaoService` (R2, R3), implementado | `MovimentacaoController`, implementado |

## Entidades

| Entidade | O que representa | Como se relaciona |
|---|---|---|
| Piquete | Área de pasto com capacidade máxima | Abriga vários bois; origem e destino de movimentações |
| Boi | Animal do rebanho identificado por microchip | Alocado em um piquete; possui pesagens e movimentações |
| Pesagem | Registro de peso de um animal em data específica | Pertence a um boi |
| Movimentacao | Transferência de um animal de piquete | Pertence a um boi e conecta piquete origem a destino |

## Rotas

| Verbo | Caminho | O que faz | Regra | Sucesso | Erros |
|-------|---------|-----------|-------|---------|-------|
| `POST` | `/piquetes` | Cadastra um piquete | Nenhuma | 201 | 400 |
| `GET` | `/piquetes` | Lista os piquetes com a ocupação atual | Nenhuma | 200 | Nenhum |
| `GET` | `/piquetes/{id}` | Busca um piquete pelo id | Nenhuma | 200 | 404 |
| `POST` | `/bois` | Cadastra um boi em um piquete | R1 e R2 | 201 | 400, 404 |
| `GET` | `/bois` | Lista os bois (filtros `status` e `piqueteId`) | Nenhuma | 200 | Nenhum |
| `GET` | `/bois/{id}` | Busca um boi pelo id | Nenhuma | 200 | 404 |
| `GET` | `/bois/microchip/{codigo}` | Busca um boi pelo microchip | Nenhuma | 200 | 404 |
| `PATCH` | `/bois/{id}/baixa` | Registra baixa do boi (venda, abate, morte) | R3 e R4 | 200 | 400, 404 |
| `POST` | `/pesagens` | Registra pesagem de um boi | R3 | 201 | 400, 404 |
| `GET` | `/bois/{id}/pesagens` | Histórico de pesagens de um boi | Nenhuma | 200 | 404 |
| `POST` | `/movimentacoes` | Move um boi para outro piquete | R2 e R3 | 201 | 400, 404 |
| `GET` | `/bois/{id}/movimentacoes` | Histórico de movimentações de um boi | Nenhuma | 200 | 404 |

## Como executar

### Com Docker e PostgreSQL:
```bash
docker compose up -d          # Sobe o banco PostgreSQL na porta 5437
.\mvnw.cmd spring-boot:run    # Executa a aplicação Spring Boot
```

### Sem Docker (usando perfil local H2 em memória):
```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

### Endereços:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Situação do projeto

| Parte | Situação |
|---|---|
| Desenho | Completo: modelo em `docs/modelo-de-dominio.md` e contrato em `docs/contrato-da-api.md` |
| Estrutura | 4 entidades completas (Migration, Model, Repository, DTOs, Mapper, Service, Controller) |
| API e regras | 100% implementada e testada (R1, R2, R3 e R4 ativas com testes automatizados) |

## Documentação

- [Modelo de domínio](docs/modelo-de-dominio.md)
- [Contrato da API](docs/contrato-da-api.md)
- [Passo a passo e testes](docs/passo-a-passo.md)
- [Autoavaliação](docs/autoavaliacao.md)
