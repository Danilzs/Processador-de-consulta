# Guia Rápido de Uso - Processador de Consultas SQL

## Como Usar a Aplicação

### 1. Executar a Aplicação

**Windows:**
```bash
run.bat
```

**Linux/Mac:**
```bash
./run.sh
```

**Ou manualmente:**
```bash
mvn clean compile
java -cp target/classes br.edu.processador.Main
```

### 2. Interface da Aplicação

A interface está organizada em 5 seções principais:

```
┌─────────────────────────────────────────────────────────────────┐
│ Processador de Consultas SQL                                    │
├─────────────────────────────────────────────────────────────────┤
│ Entrada da Consulta SQL                                         │
│ ┌───────────────────────────────────────────────────────────┐   │
│ │ SELECT Cliente.Nome FROM Cliente WHERE idCliente > 100   │   │
│ └───────────────────────────────────────────────────────────┘   │
├─────────────────────────────────────────────────────────────────┤
│ [Álgebra Relacional] [Grafo] [Otimizações] [Plano de Execução] │
│ ┌───────────────────────────────────────────────────────────┐   │
│ │                                                           │   │
│ │  Conteúdo da aba selecionada                             │   │
│ │                                                           │   │
│ │                                                           │   │
│ │                                                           │   │
│ └───────────────────────────────────────────────────────────┘   │
├─────────────────────────────────────────────────────────────────┤
│              [Processar Consulta]  [Limpar]                     │
└─────────────────────────────────────────────────────────────────┘
```

### 3. Fluxo de Uso

1. **Digite a consulta SQL** no campo de entrada
2. **Clique em "Processar Consulta"**
3. **Navegue pelas abas** para ver:
   - **Álgebra Relacional**: Conversão matemática da consulta
   - **Grafo de Operadores**: Árvore de operações (original e otimizada)
   - **Otimizações**: Explicação das heurísticas aplicadas
   - **Plano de Execução**: Ordem de execução das operações

### 4. Exemplos de Consultas Válidas

#### Exemplo 1: Consulta Simples
```sql
SELECT Nome, Email FROM Cliente WHERE idCliente > 100
```

**Saída esperada:**
- Álgebra: `π[NOME, EMAIL](σ[IDCLIENTE > 100](CLIENTE))`
- Grafo com 3 níveis: TABLE → SELECTION → PROJECTION

#### Exemplo 2: Consulta com JOIN
```sql
SELECT Cliente.Nome, Pedido.DataPedido 
FROM Cliente 
JOIN Pedido ON Cliente.idCliente = Pedido.Cliente_idCliente 
WHERE Cliente.idCliente > 100
```

**Saída esperada:**
- Álgebra com operador de junção (⋈)
- Grafo otimizado com seleção aplicada antes do JOIN

#### Exemplo 3: Múltiplos JOINs
```sql
SELECT c.Nome, p.Nome 
FROM Cliente c
JOIN Pedido ped ON c.idCliente = ped.Cliente_idCliente
JOIN Pedido_has_Produto php ON ped.idPedido = php.Pedido_idPedido
JOIN Produto p ON php.Produto_idProduto = p.idProduto
WHERE c.idCliente > 100 AND p.Preco > 50
```

**Saída esperada:**
- Múltiplos operadores de junção
- Seleções aplicadas próximo às tabelas base
- Grafo de execução mostrando a ordem otimizada

### 5. Validações e Erros

A aplicação valida:
- ✅ Sintaxe SQL básica
- ✅ Existência de tabelas
- ✅ Existência de colunas
- ✅ Operadores válidos

**Erros comuns:**
- ❌ `Table 'X' does not exist` - Tabela não existe no esquema
- ❌ `Column 'Y' does not exist` - Coluna não existe nas tabelas
- ❌ `Invalid SELECT clause` - Sintaxe SQL incorreta

### 6. Tabelas Disponíveis

O sistema reconhece estas tabelas:
- Categoria
- Produto
- TipoCliente
- Cliente
- TipoEndereco
- Endereco
- Telefone
- Status
- Pedido
- Pedido_has_Produto

Ver `MANUAL.md` para lista completa de colunas.

### 7. Operadores Suportados

**Comparação:** `=`, `>`, `<`, `<=`, `>=`, `<>`  
**Lógicos:** `AND`  
**Agrupamento:** `()`, `()`  
**Junção:** `JOIN ... ON ...`

### 8. Símbolos de Álgebra Relacional

- **π** (Pi) = Projeção (SELECT)
- **σ** (Sigma) = Seleção (WHERE)
- **⋈** (Bowtie) = Junção (JOIN)

### 9. Interpretando o Grafo

```
└── PROJECTION: Nome, Email          ← Raiz (última operação)
    └── SELECTION: idCliente > 100   ← Operação intermediária
        └── TABLE: Cliente           ← Folha (tabela base)
```

**Leitura:** De baixo para cima = ordem de execução  
**Otimização:** Operações movidas para reduzir dados processados

### 10. Testando sem GUI

Se não tiver interface gráfica disponível:
```bash
java -cp target/classes:target/test-classes br.edu.processador.TestQueryProcessor
```

Este comando executa testes automatizados que demonstram todas as funcionalidades.

---

## Dicas

💡 **Use nomes com prefixos de tabela** para evitar ambiguidade:
   - `Cliente.Nome` ao invés de apenas `Nome`

💡 **Consultas case-insensitive**: 
   - `SELECT` = `select` = `SeLeCt`

💡 **Espaços extras são ignorados**:
   - `SELECT    Nome` = `SELECT Nome`

💡 **Sempre valide primeiro**:
   - O parser verifica a consulta antes de processar

---

Para mais detalhes, consulte o arquivo `MANUAL.md`.
