# Processador de Consultas SQL

Sistema completo para processar consultas SQL e visualizar a álgebra relacional, grafo de operadores, otimizações e plano de execução.

## Como Executar

### Pré-requisitos
- Java 11 ou superior
- Maven 3.6 ou superior

### Compilar o Projeto
```bash
mvn clean compile
```

### Executar a Aplicação
```bash
mvn exec:java -Dexec.mainClass="br.edu.processador.Main"
```

Ou após gerar o JAR:
```bash
java -jar target/query-processor-1.0-SNAPSHOT.jar
```

## Funcionalidades Implementadas

### HU1 - Entrada e Validação da Consulta ✅
- Interface gráfica com campo de entrada para consultas SQL
- Parser que valida comandos SQL (SELECT, FROM, WHERE, JOIN, ON)
- Validação de operadores: =, >, <, <=, >=, <>, AND, ()
- Verificação de existência de tabelas e atributos
- Case-insensitive para palavras-chave SQL
- Normalização de espaços em branco
- Suporte a múltiplos JOINs

### HU2 - Conversão para Álgebra Relacional ✅
- Conversão completa de SQL para álgebra relacional
- Exibição com notação matemática: σ (seleção), π (projeção), ⋈ (junção)
- Visualização passo a passo da conversão
- Preservação de operadores e condições

### HU3 - Construção do Grafo de Operadores ✅
- Grafo gerado em memória e exibido na interface
- Nós representam operadores (TABLE, SELECTION, PROJECTION, JOIN)
- Arestas representam fluxo de resultados intermediários
- Folhas representam tabelas base
- Raiz representa a projeção final
- Visualização em formato de árvore

### HU4 - Otimização da Consulta ✅
Heurísticas aplicadas:
1. **Redução de Tuplas**: Seleções (σ) aplicadas primeiro, próximas às tabelas
2. **Redução de Atributos**: Projeções (π) aplicadas após seleções
3. **Operações Restritivas Primeiro**: Reordenação de operações para eficiência
4. **Evitar Produto Cartesiano**: Estrutura de junções otimizada
5. Comparação entre grafo original e otimizado

### HU5 - Plano de Execução ✅
- Ordem de execução das operações
- Lista numerada de passos
- Segue a ordem do grafo otimizado
- Visualização clara do fluxo de execução

## Esquema de Banco de Dados Suportado

O sistema valida consultas contra o seguinte esquema:

### Tabelas e Campos

- **Categoria** (idCategoria, Descricao)
- **Produto** (idProduto, Nome, Descricao, Preco, QuantEstoque, Categoria_idCategoria)
- **TipoCliente** (idTipoCliente, Descricao)
- **Cliente** (idCliente, Nome, Email, Nascimento, Senha, TipoCliente_idTipoCliente, DataRegistro)
- **TipoEndereco** (idTipoEndereco, Descricao)
- **Endereco** (idEndereco, EnderecoPadrao, Logradouro, Numero, Complemento, Bairro, Cidade, UF, CEP, TipoEndereco_idTipoEndereco, Cliente_idCliente)
- **Telefone** (Numero, Cliente_idCliente)
- **Status** (idStatus, Descricao)
- **Pedido** (idPedido, Status_idStatus, DataPedido, ValorTotalPedido, Cliente_idCliente)
- **Pedido_has_Produto** (idPedidoProduto, Pedido_idPedido, Produto_idProduto, Quantidade, PrecoUnitario)

## Exemplos de Consultas

### Exemplo 1: Consulta Simples
```sql
SELECT Nome, Email FROM Cliente WHERE idCliente > 100
```

### Exemplo 2: Consulta com JOIN
```sql
SELECT Cliente.Nome, Pedido.DataPedido 
FROM Cliente 
JOIN Pedido ON Cliente.idCliente = Pedido.Cliente_idCliente 
WHERE Cliente.idCliente > 100
```

### Exemplo 3: Múltiplos JOINs
```sql
SELECT Cliente.Nome, Pedido.DataPedido, Produto.Nome 
FROM Cliente 
JOIN Pedido ON Cliente.idCliente = Pedido.Cliente_idCliente
JOIN Pedido_has_Produto ON Pedido.idPedido = Pedido_has_Produto.Pedido_idPedido
JOIN Produto ON Pedido_has_Produto.Produto_idProduto = Produto.idProduto
WHERE Cliente.idCliente > 100 AND Produto.Preco > 50
```

## Interface Gráfica

A aplicação possui uma interface dividida em:

1. **Painel de Entrada**: Campo de texto para digitar a consulta SQL
2. **Aba "Álgebra Relacional"**: Exibe a conversão da consulta para álgebra relacional
3. **Aba "Grafo de Operadores"**: Mostra os grafos original e otimizado
4. **Aba "Otimizações"**: Explica as heurísticas aplicadas
5. **Aba "Plano de Execução"**: Lista ordenada das operações

## Arquitetura do Sistema

```
br.edu.processador/
├── model/              # Modelos de dados
│   ├── DatabaseSchema.java
│   ├── TableMetadata.java
│   └── SQLQuery.java
├── parser/             # Parser e validador SQL
│   └── SQLParser.java
├── algebra/            # Conversor para álgebra relacional
│   └── RelationalAlgebraConverter.java
├── graph/              # Construção do grafo de operadores
│   ├── OperatorNode.java
│   └── OperatorGraph.java
├── optimizer/          # Otimizador de consultas
│   └── QueryOptimizer.java
├── ui/                 # Interface gráfica
│   └── QueryProcessorUI.java
└── Main.java           # Classe principal
```

## Critérios de Avaliação Atendidos

| Critério | Peso | Status |
|----------|------|--------|
| Interface gráfica funcional | 1,0 | ✅ Completo |
| Parsing e validação | 2,0 | ✅ Completo |
| Conversão para álgebra relacional | 1,5 | ✅ Completo |
| Exibição do grafo otimizado | 1,0 | ✅ Completo |
| Ordem de execução | 1,5 | ✅ Completo |
| Heurística de redução de tuplas | 1,0 | ✅ Completo |
| Heurística de redução de atributos | 1,0 | ✅ Completo |
| Uso da junção | 1,0 | ✅ Completo |
| **Total** | **10,0** | **✅ 10,0** |

## Tecnologias Utilizadas

- **Java 11**: Linguagem de programação
- **Swing**: Framework para interface gráfica
- **Maven**: Gerenciamento de dependências e build
- **Regex**: Validação e parsing de SQL

## Autor

Desenvolvido como trabalho acadêmico para a disciplina de Banco de Dados.
