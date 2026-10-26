# BoiChip

API REST para controlar o rebanho de uma fazenda de gado de corte por meio do microchip de cada boi: cadastro dos animais, pesagens, troca de piquete e baixa (venda, abate ou morte). Feita para o gestor da fazenda e para o peão de campo que registra o dia a dia do rebanho.

Projeto Final da disciplina de Backend (Engenharia de Software, SATC). Etapa 1: arquitetura do backend.

## Índice

1. [Integrantes](#integrantes)
2. [O problema](#o-problema)
3. [Tecnologias](#tecnologias)
4. [Arquitetura](#arquitetura)
5. [Entidades](#entidades)
6. [Rotas](#rotas)
7. [Como executar](#como-executar)
8. [Situação do projeto](#situação-do-projeto)
9. [Documentação](#documentação)

## Integrantes

| Nome | GitHub |
|------|--------|
| JOÃO PEDRO TAUFEMBACH ACORDI| [@JoaoAcordi](https://github.com/JoaoAcordi) |
| KAUANY HAHN SARTOR| [@kauanysartor](https://github.com/kauanysartor) |
| RAFAELLA DE SOUZA BENEDET | [@rafinha-benedet](https://github.com/rafinha-benedet) |

## O problema

**Tema sorteado:** Agronegócio

O controle individual dos animais é um dos pilares de uma fazenda profissional ou não. Do ponto de vista financeiro, saber quantos bois a fazenda possui, onde cada um está e quanto pesa permite escolher o melhor momento da venda, dimensionar os gastos com pasto e ração e reduzir perdas. Do ponto de vista sanitário e produtivo, o registro individual facilita o acompanhamento do ganho de peso, o isolamento de animais doentes e o respeito à capacidade de cada pasto, evitando a superlotação, que degrada o solo e compromete a qualidade do rebanho.

Apesar dessa importância, em muitas propriedades o acompanhamento ainda depende de cadernos e planilhas, nos quais os brincos se perdem e as informações ficam incompletas ou desatualizadas. O BoiChip propõe uma solução para esse cenário: a API adota o número do microchip como identidade única de cada animal e centraliza o cadastro, o histórico de pesagens e as movimentações entre piquetes, aplicando automaticamente as regras de manejo da fazenda.


**Quem usa o sistema:**

| Quem | O que faz no sistema |
|------|----------------------|
| Gestor da fazenda | Cadastra piquetes e bois, acompanha o rebanho e dá baixa em animais vendidos, abatidos ou mortos |
| Peão de campo | Registra pesagens e movimenta os bois entre piquetes, lendo o microchip do animal |



**Regras de negócio.**

| # | Regra | Onde está no código | Rota que usa |
|---|-------|---------------------|--------------|
| R1 | Não pode existir dois bois com o mesmo número de microchip | `BoiService` | `POST /bois` |
| R2 | Um piquete não pode receber mais bois ativos do que a sua capacidade máxima | `BoiService` e `MovimentacaoService` | `POST /bois`, `POST /movimentacoes` |
| R3 | Só um boi com status `ATIVO` pode ser pesado, movimentado ou receber baixa | `PesagemService`, `MovimentacaoService` e `BoiService` | `POST /pesagens`, `POST /movimentacoes`, `PATCH /bois/{id}/baixa` |
| R4 | Um boi só pode ser baixado como `VENDIDO` ou `ABATIDO` se tiver ao menos uma pesagem registrada | `BoiService` | `PATCH /bois/{id}/baixa` |

**Fora do escopo:** controle de saúde e vacinação, financeiro (compra e venda com valores), múltiplas fazendas, usuários e login, e a leitura física do microchip por antena (o número é informado pela requisição).

## Tecnologias

| O quê | Qual |
|-------|------|
| Linguagem | Java 17 |
| Framework | Spring Boot 3 (Web, Validation, Data JPA) |
| Banco de dados | PostgreSQL 16, em Docker |
| Migrations | Flyway |
| Documentação da API | springdoc-openapi (Swagger UI) |

## Arquitetura

A API é organizada em camadas. Uma requisição entra pelo Controller, passa pelo Service, que aplica as regras de negócio, e chega ao banco pelo Repository; a resposta faz o caminho de volta. O Mapper converte DTO em entidade e entidade em DTO.

**Mapa da estrutura.** Situação de cada peça: *a fazer*, *montada* (criada e ligada às outras, sem lógica), *em parte* ou *implementada*. Atualize a tabela conforme o grupo avançar.

| Entidade | Migration | Model | Repository | DTOs | Mapper | Service | Controller |
|----------|-----------|-------|------------|------|--------|---------|------------|
| Piquete | `V1__criar_piquetes.sql`, a fazer | `Piquete`, a fazer | `PiqueteRepository`, a fazer | `PiqueteRequestDTO`, `PiqueteResponseDTO`, a fazer | `PiqueteMapper`, a fazer | `PiqueteService`, a fazer | `PiqueteController`, a fazer |
| Boi | `V2__criar_bois.sql`, a fazer | `Boi`, `StatusBoi`, a fazer | `BoiRepository`, a fazer | `BoiRequestDTO`, `BoiResponseDTO`, `BaixaRequestDTO`, a fazer | `BoiMapper`, a fazer | `BoiService` (R1, R2, R3, R4), a fazer | `BoiController`, a fazer |
| Pesagem | `V3__criar_pesagens.sql`, a fazer | `Pesagem`, a fazer | `PesagemRepository`, a fazer | `PesagemRequestDTO`, `PesagemResponseDTO`, a fazer | `PesagemMapper`, a fazer | `PesagemService` (R3), a fazer | `PesagemController`, a fazer |
| Movimentacao | `V4__criar_movimentacoes.sql`, a fazer | `Movimentacao`, a fazer | `MovimentacaoRepository`, a fazer | `MovimentacaoRequestDTO`, `MovimentacaoResponseDTO`, a fazer | `MovimentacaoMapper`, a fazer | `MovimentacaoService` (R2, R3), a fazer | `MovimentacaoController`, a fazer |

## Entidades

| Entidade | O que representa | Como se relaciona |
|----------|------------------|-------------------|
| Piquete | Uma área de pasto da fazenda, com capacidade máxima de animais | Abriga vários bois; é origem e destino de várias movimentações |
| Boi | Um animal do rebanho, identificado pelo número do microchip | Está em um piquete; tem várias pesagens e várias movimentações |
| Pesagem | Um registro de peso de um boi em uma data | Pertence a um boi |
| Movimentacao | A troca de um boi de um piquete para outro | Pertence a um boi; liga um piquete de origem a um de destino |


## Rotas

### As que já funcionam

Nenhuma ainda: o projeto está na fase de desenho. Conforme cada rota passar a responder, mova a linha da tabela de "As planejadas" para cá.

**Exemplo de requisição** (contrato da rota `POST /bois`):

```http
POST /bois
Content-Type: application/json

{
  "microchip": "982000123456789",
  "raca": "Nelore",
  "dataNascimento": "2024-03-15",
  "piqueteId": 1
}
```

Resposta, `201 Created`:

```json
{
  "id": 1,
  "microchip": "982000123456789",
  "raca": "Nelore",
  "dataNascimento": "2024-03-15",
  "status": "ATIVO",
  "piqueteId": 1,
  "piqueteNome": "Pasto da Sede"
}
```

### Erros

Todo erro sai no mesmo formato. Este é o `400` de uma regra de negócio violada (R2, piquete lotado):

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "R2: o piquete Pasto da Sede já está na capacidade máxima (50)",
  "caminho": "/bois",
  "timestamp": "2026-10-19T20:50:00",
  "campos": []
}
```

Um campo inválido também devolve `400`, com a lista `campos` dizendo qual campo corrigir. Um id que não existe devolve `404`.

### As planejadas

Estão no contrato, mas ainda não respondem.

| Verbo | Caminho | O que faz | Regra | Sucesso | Erros |
|-------|---------|-----------|-------|---------|-------|
| POST | `/piquetes` | Cadastra um piquete | Nenhuma | 201 | 400 |
| GET | `/piquetes` | Lista os piquetes com a ocupação atual | Nenhuma | 200 | |
| GET | `/piquetes/{id}` | Busca um piquete | Nenhuma | 200 | 404 |
| POST | `/bois` | Cadastra um boi em um piquete | R1 e R2 | 201 | 400, 404 |
| GET | `/bois` | Lista os bois (filtros `status` e `piqueteId`) | Nenhuma | 200 | |
| GET | `/bois/{id}` | Busca um boi pelo id | Nenhuma | 200 | 404 |
| GET | `/bois/microchip/{codigo}` | Busca um boi pelo número do microchip | Nenhuma | 200 | 404 |
| PATCH | `/bois/{id}/baixa` | Dá baixa (vendido, abatido ou morto) | R3 e R4 | 200 | 400, 404 |
| POST | `/pesagens` | Registra a pesagem de um boi | R3 | 201 | 400, 404 |
| GET | `/bois/{id}/pesagens` | Lista o histórico de pesagens de um boi | Nenhuma | 200 | 404 |
| POST | `/movimentacoes` | Move um boi para outro piquete | R2 e R3 | 201 | 400, 404 |
| GET | `/bois/{id}/movimentacoes` | Lista o histórico de movimentações de um boi | Nenhuma | 200 | 404 |

## Como executar

**Pré-requisitos:** JDK 17, Docker Desktop aberto e Git.

```bash
git clone https://github.com/{{usuario}}/{{repositorio}}.git
cd {{repositorio}}

docker compose up -d          # sobe o PostgreSQL na porta 5437
./mvnw spring-boot:run        # Windows: .\mvnw.cmd spring-boot:run
```

Com a API no ar:

| Endereço | O que é |
|----------|---------|
| http://localhost:8080/swagger-ui.html | Swagger UI: as rotas, com *Try it out* para enviar requisições |
| http://localhost:8080/v3/api-docs | O contrato OpenAPI em JSON |

Para testar as rotas, use o Swagger UI ou o Postman. No Postman, importe todas as rotas de uma vez: *Import → Link → `http://localhost:8080/v3/api-docs`*.

Para parar: `Ctrl+C` na API e `docker compose stop` no banco. Para apagar o banco e rodar as migrations do zero: `docker compose down -v` e suba de novo.

## Situação do projeto

| Parte | Situação |
|-------|----------|
| Desenho | Completo: problema e regras neste README, modelo e contrato em `docs/` |
| Estrutura | {{ex.: as 4 entidades têm migration, entidade, Repository, DTOs, Mapper, Service e Controller}} |
| API e regras | {{ex.: as rotas de piquetes e bois funcionam; R1 e R2 estão implementadas}} |

**Ainda não funciona:** {{as rotas planejadas que ainda não respondem}}

## Documentação

| Documento | O que tem |
|-----------|-----------|
| [Modelo de domínio](docs/modelo-de-dominio.md) | Entidades, relacionamentos, diagrama das tabelas, migrations |
| [Contrato da API](docs/contrato-da-api.md) | Todas as rotas, os DTOs, exemplos de JSON e erros |
| [Passo a passo](docs/passo-a-passo.md) | Como montar o projeto do zero, com o código de cada camada e o roteiro de testes |
| [Autoavaliação](docs/autoavaliacao.md) | Os critérios da revisão do código, marcados pelo grupo |
