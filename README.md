# Processador de Consultas (Histórias de Usuário)

---

## Objetivo
Implementar um Processador de Consultas que:
- Receba uma consulta SQL via interface gráfica.
- Faça parsing e validação (sintaxe e metadados).
- Converta para Álgebra Relacional.
- Construa e otimize o grafo de operadores.
- Exiba o plano de execução ordenado.

O trabalho vale 30% da nota da disciplina e deve ser desenvolvido em equipe.

---

## Orientações de Desenvolvimento
- Utilize um quadro Kanban para organizar as histórias de usuário entre os membros do time.
- Considere interdependências entre histórias ao priorizar e distribuir tarefas.
- Recomenda-se o uso de CI para melhorar a segurança do projeto.
- Merges são responsabilidade do time; leve em conta interdependências ao construir.
- O professor atuará como Product Owner (PO) para dúvidas sobre histórias, critérios e regras.

---

## Histórias de Usuário

### HU1 – Entrada e Validação da Consulta
Como usuário do sistema (aluno/desenvolvedor), quero digitar uma consulta SQL na interface gráfica para que o sistema valide sintaxe, tabelas e atributos existentes.

Critérios de Aceitação:
- [ ] Interface gráfica com campo de entrada da consulta.
- [ ] Parser valida comandos SQL básicos do trabalho: `SELECT`, `FROM`, `WHERE`, `JOIN`, `ON`.
- [ ] Operadores válidos: `=`, `>`, `<`, `<=`, `>=`, `<>`, `AND`, `(` `)`.
- [ ] Verificação de existência de tabelas e atributos.

Regras de Negócio:
- [ ] Apenas tabelas/atributos do modelo podem ser usados (ver Metadados).
- [ ] Consultas devem suportar múltiplos JOINs (0, 1, …, N).
- [ ] Case-insensitive (ignorar diferença entre maiúsculas/minúsculas).
- [ ] Ignorar repetições de espaços em branco.

---

### HU2 – Conversão para Álgebra Relacional
Como aluno, quero que minha consulta SQL seja convertida em uma expressão de álgebra relacional, para compreender a representação teórica.

Critérios de Aceitação:
- [ ] Exibir a consulta equivalente em álgebra relacional na interface.
- [ ] A conversão deve preservar operadores e condições.

Regras de Negócio:
- [ ] A representação deve incluir seleção (σ), projeção (π) e junções (⋈).

---

### HU3 – Construção do Grafo de Operadores
Como aluno, quero que o sistema construa um grafo de operadores, para visualizar a estratégia de execução da consulta.

Critérios de Aceitação:
- [ ] O grafo é gerado em memória e exibido na interface.
- [ ] Cada nó representa um operador.
- [ ] Arestas representam o fluxo de resultados intermediários.
- [ ] Folhas representam as tabelas.
- [ ] A raiz representa a última projeção.
- [ ] O grafo representa a estratégia de execução.

Regras de Negócio:
- [ ] O grafo deve respeitar dependências lógicas da consulta.

---

### HU4 – Otimização da Consulta
Como aluno, quero que a álgebra relacional seja otimizada conforme heurísticas, para reduzir o custo de execução.

Critérios de Aceitação:
- [ ] Aplicar heurísticas:
  - Seleções que reduzem tuplas primeiro.
  - Projeções que reduzem atributos na sequência.
  - Seleções e junções mais restritivas primeiro.
  - Evitar produto cartesiano.
- [ ] Exibir o grafo otimizado.

Regras de Negócio:
- [ ] A árvore deve ser reordenada (ou construída) para eficiência aplicando as heurísticas.

---

### HU5 – Plano de Execução
Como aluno, quero visualizar a ordem de execução da consulta, para compreender como o banco executaria passo a passo.

Critérios de Aceitação:
- [ ] Exibir ordem de execução (plano de execução ordenado).
- [ ] Listar operações na ordem correta.

Regras de Negócio:
- [ ] Execução deve seguir a ordem definida pelo grafo otimizado.

---

## Critérios de Avaliação

| Critério                                                                                  | Peso |
|-------------------------------------------------------------------------------------------|:----:|
| Interface gráfica funcional (entrada SQL e visualização do grafo de operadores)           | 1,0  |
| Codificação e execução do parsing e validação correta (método de validação observado)     | 2,0  |
| Codificação e execução da conversão para álgebra relacional                                | 1,5  |
| Codificação e execução da exibição do grafo de operadores otimizado                       | 1,0  |
| Ordem de execução apresentada                                                             | 1,5  |
| Codificação e aplicação da heurística de redução de tuplas                                | 1,0  |
| Codificação e aplicação da heurística de redução de atributos                             | 1,0  |
| Codificação e uso da junção                                                               | 1,0  |
| Total                                                                                     | 10,0 |

---

## Metadados (Modelo de Dados de Referência)
Imagem 01 – Modelo de dados de referência para os metadados (não incluída).

Tabelas e campos para validação (parsing):

- Categoria (idCategoria, Descricao)
- Produto (idProduto, Nome, Descricao, Preco, QuantEstoque, Categoria_idCategoria)
- TipoCliente (idTipoCliente, Descricao)
- Cliente (idCliente, Nome, Email, Nascimento, Senha, TipoCliente_idTipoCliente, DataRegistro)
- TipoEndereco (idTipoEndereco, Descricao)
- Endereco (idEndereco, EnderecoPadrao, Logradouro, Numero, Complemento, Bairro, Cidade, UF, CEP, TipoEndereco_idTipoEndereco, Cliente_idCliente)
- Telefone (Numero, Cliente_idCliente)
- Status (idStatus, Descricao)
- Pedido (idPedido, Status_idStatus, DataPedido, ValorTotalPedido, Cliente_idCliente)
- Pedido_has_Produto (idPedidoProduto, Pedido_idPedido, Produto_idProduto, Quantidade, PrecoUnitario)

Somente as tabelas e atributos listados acima devem ser aceitos durante a validação da consulta.

---

## Heurísticas de Otimização (Básicas)
a) Aplicar primeiro operações que reduzem resultados intermediários:
- Seleção (reduz número de tuplas).
- Projeção (reduz número de atributos).

b) Aplicar primeiro seleções e junções mais restritivas:
- Reordenar nós folha da árvore de consulta.
- Evitar produto cartesiano.
- Ajustar o restante da árvore de forma apropriada.

---

## Fluxo de Funcionamento
1. A string com a consulta SQL é digitada na interface gráfica.
2. O parser processa a string:
   - Valida sintaxe e comandos suportados.
   - Verifica existência de tabelas e campos referenciados.
3. A consulta é convertida para Álgebra Relacional (σ, π, ⋈).
4. Constrói-se o grafo de operadores correspondente.
5. Aplicam-se heurísticas de otimização sobre a árvore/grafo.
6. Exibe-se:
   - O grafo inicial e o grafo otimizado.
   - O plano de execução (ordem das operações).

---

## Observações
- A implementação deve ser case-insensitive e tolerante a múltiplos espaços em branco.
- Suporte a múltiplos JOINs.
- Recomenda-se testes automatizados e integração contínua (CI).
- A interface deve permitir entrada clara da consulta e visualização dos resultados (álgebra, grafo, plano).
