# Contrato da API

## Convenções

| Item | Valor |
|---|---|
| Endereço base | `http://localhost:8080` |
| Formato | JSON, na requisição e na resposta |
| Datas | `yyyy-MM-dd` |
| Erros | Sempre no formato `ErroDTO` |
| Documentação viva | `http://localhost:8080/swagger-ui.html` |

## Rotas

### Piquetes

| Verbo | Caminho | O que faz | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|
| `POST` | `/piquetes` | Cadastra um piquete | `PiqueteRequestDTO` | `PiqueteResponseDTO` | `201` | `400` | sim |
| `GET` | `/piquetes` | Lista os piquetes com a ocupação atual | Nenhuma | lista de `PiqueteResponseDTO` | `200` | Nenhum | sim |
| `GET` | `/piquetes/{id}` | Busca um piquete por ID | Nenhuma | `PiqueteResponseDTO` | `200` | `404` | sim |

### Bois

| Verbo | Caminho | O que faz | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|
| `POST` | `/bois` | Cadastra um boi em um piquete (regras R1 e R2) | `BoiRequestDTO` | `BoiResponseDTO` | `201` | `400`, `404` | sim |
| `GET` | `/bois` | Lista os bois (filtros opcionais `status` e `piqueteId`) | Query params | lista de `BoiResponseDTO` | `200` | Nenhum | sim |
| `GET` | `/bois/{id}` | Busca um boi pelo id | Nenhuma | `BoiResponseDTO` | `200` | `404` | sim |
| `GET` | `/bois/microchip/{codigo}` | Busca um boi pelo microchip | Nenhuma | `BoiResponseDTO` | `200` | `404` | sim |
| `PATCH` | `/bois/{id}/baixa` | Registra baixa do boi (regras R3 e R4) | `BaixaRequestDTO` | `BoiResponseDTO` | `200` | `400`, `404` | sim |
| `GET` | `/bois/{id}/pesagens` | Lista histórico de pesagens | Nenhuma | lista de `PesagemResponseDTO` | `200` | `404` | sim |
| `GET` | `/bois/{id}/movimentacoes` | Lista histórico de movimentações | Nenhuma | lista de `MovimentacaoResponseDTO` | `200` | `404` | sim |

### Pesagens e Movimentações

| Verbo | Caminho | O que faz | Regra | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|
| `POST` | `/pesagens` | Registra a pesagem de um animal | R3 (apenas animal ATIVO) | `201` | `400`, `404` | sim |
| `POST` | `/movimentacoes` | Move um boi para outro piquete | R2 (capacidade do destino) e R3 (animal ATIVO) | `201` | `400`, `404` | sim |

## DTOs

| DTO | Direção | Campos | Validações |
|---|---|---|---|
| `PiqueteRequestDTO` | entrada | `nome`, `capacidadeMaxima` | `nome` (@NotBlank), `capacidadeMaxima` (@NotNull, @Min(1)) |
| `PiqueteResponseDTO` | saída | `id`, `nome`, `capacidadeMaxima`, `ocupacaoAtual` | Nenhuma |
| `BoiRequestDTO` | entrada | `microchip`, `raca`, `dataNascimento`, `piqueteId` | `microchip` (@NotBlank), `raca` (@NotBlank), `dataNascimento` (@NotNull, @PastOrPresent), `piqueteId` (@NotNull) |
| `BoiResponseDTO` | saída | `id`, `microchip`, `raca`, `dataNascimento`, `status`, `piqueteId`, `piqueteNome` | Nenhuma |
| `BaixaRequestDTO` | entrada | `novoStatus`, `motivo` | `novoStatus` (@NotNull), `motivo` (@NotBlank) |
| `PesagemRequestDTO` | entrada | `boiId`, `peso`, `dataPesagem` | `boiId` (@NotNull), `peso` (@NotNull, @Positive), `dataPesagem` (@NotNull, @PastOrPresent) |
| `PesagemResponseDTO` | saída | `id`, `boiId`, `microchip`, `peso`, `dataPesagem` | Nenhuma |
| `MovimentacaoRequestDTO` | entrada | `boiId`, `piqueteDestinoId`, `dataMovimentacao`, `motivo` | `boiId` (@NotNull), `piqueteDestinoId` (@NotNull), `dataMovimentacao` (@NotNull, @PastOrPresent), `motivo` (@NotBlank) |
| `MovimentacaoResponseDTO` | saída | `id`, `boiId`, `microchip`, `piqueteOrigemId`, `piqueteOrigemNome`, `piqueteDestinoId`, `piqueteDestinoNome`, `dataMovimentacao`, `motivo` | Nenhuma |

## Exemplos

### `POST /piquetes`

Requisição:
```json
{
  "nome": "Pasto da Sede",
  "capacidadeMaxima": 50
}
```

Resposta `201 Created`:
```json
{
  "id": 1,
  "nome": "Pasto da Sede",
  "capacidadeMaxima": 50,
  "ocupacaoAtual": 0
}
```

### `POST /bois`

Requisição:
```json
{
  "microchip": "982000123456789",
  "raca": "Nelore",
  "dataNascimento": "2024-03-15",
  "piqueteId": 1
}
```

Resposta `201 Created`:
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

## Erros

Todo erro sai no formato padronizado `ErroDTO`:

### Exemplo 400 (Regra de Negócio Violada)
```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "R2: o piquete Pasto da Sede já está na capacidade máxima (50)",
  "caminho": "/bois",
  "timestamp": "2026-10-26T00:15:00",
  "campos": []
}
```

### Exemplo 404 (Recurso Não Encontrado)
```json
{
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Boi não encontrado: 999",
  "caminho": "/bois/999",
  "timestamp": "2026-10-26T00:15:00",
  "campos": []
}
```
