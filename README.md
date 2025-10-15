AV1 AV2 AV3
1ª ch
2ª ch
Curso: Disciplina: Código/Turma:
Professor/a: Data:
Aluno/a: Matrícula:
INSTRUÇÕES PARA O DESENVOLVIMENTO DO TRABALHO
• Trabalho é em Equipe
• O Trabalho vale 30% da nota da disciplina.
Processador de Consultas (Histórias de Usuário)
Este documento descreve o trabalho de implementação de um Processador de Consultas, reestruturado no formato
de Histórias de Usuário. O objetivo é alinhar as regras de negócio, critérios de aceitação e métricas de avaliação para
a disciplina de Banco de Dados.
Para este desenvolvimento, sugere-se o uso de um quadro Kanban para facilitar e organizar o desenvolvimento em
equipe (time), onde cada história de usuário pode ser distribuída para um membro do time. Sugere-se também o uso
de uma ferramenta de CI para melhorar a segurança do projeto.
Os merges do processo de desenvolvimento são de responsabilidade do time e devem ser levados em consideração
na hora de construção com interdependências. Assim sendo, como cada história de usuário pode ter
interdependências, é necessário observar estas interdependências quando a equipe for priorizar e distribuir as
histórias de usuário.
O professor irá atuar como PO desta aplicação e estará disponível para tirar dúvidas das histórias de usuário,
critérios e regras de negócio.
HU1 – Entrada e Validação da Consulta
Como usuário do sistema (aluno/desenvolvedor), quero digitar uma consulta SQL na interface gráfica para que o
sistema valide sintaxe, tabelas e atributos existentes.
Critérios de Aceitação:
• Interface	gráfica	com	campo	de	entrada	da	consulta.
• Parser	deve	validar	comandos	SQL	básicos selecionados	para	o	trabalho (SELECT,	FROM,	WHERE,	JOIN,	
ON).
• Operadores	válidos para	o	trabalho (=,	>,	<,	<=,	>=,	<>,	AND,	(	)).
• Verificação	de	existência	de	tabelas	e	atributos.
Regras de Negócio:
• Apenas	tabelas/atributos listados	no	modelo	podem	ser	usados (Imagem	01).
• Consultas	devem	suportar	múltiplos	JOINs (0,	1,…,N).
• Deve	ignorar	a	diferença	entre	palavras	maiúsculas	e	minúsculas.
• Deve	ignorar	repetições	de	espaços	em	branco.
HU2 – Conversão para Álgebra Relacional
Como aluno, quero que minha consulta SQL seja convertida em uma expressão de álgebra relacional, para
compreender a representação teórica.
Critérios de Aceitação:
• Exibir	a	consulta	equivalente	em	álgebra	relacional na	interface	gráfica.
• A	conversão	deve	preservar	operadores	e	condições.
Regras de Negócio:
• Representação	deve	incluir	seleção	(σ),	projeção	(π)	e	junções	(⋈).
HU3 – Construção do Grafo de Operadores
Como aluno, quero que o sistema construa um grafo de operadores, para visualizar a estratégia de execução da
consulta.
Critérios de Aceitação:
• O	grafo	deve	ser	gerado	em	memória	e	exibido	na	interface.
• Cada	nó	deve	representar	operadores.
• Arestas	devem	representar	fluxo	de	resultados	intermediários.
• As	folhas	devem	representar	as	tabelas.
• A	raiz	deve	representar	a	última	projeção.
• O	grafo	representa	a	estratégia	de	execução.
Regras de Negócio:
• O	grafo	deve	respeitar	dependências	lógicas	da	consulta.
HU4 – Otimização da Consulta
Como aluno, quero que a álgebra relacional seja otimizada conforme heurísticas, para reduzir o custo de execução.
Critérios de Aceitação:
• Aplicar	heurísticas:	
o seleções	que	reduzem	tuplas	primeiro;	
o projeções	que	reduzem	atributos na	sequência;	
o seleções	e	junções	mais	restritivas	primeiro;	
o evitar	produto	cartesiano.
• Exibir	o	grafo	otimizado.
Regras de Negócio:
• A	árvore	deve	ser	reordenada (ou	construída) para	eficiência,	aplicando	heurísticas.
HU5 – Plano de Execução
Como aluno, quero visualizar a ordem de execução da consulta, para compreender como o banco executaria passo a
passo.
Critérios de Aceitação:
• Exibir	ordem	de	execução	(plano de	execução	ordenado).
• Listar	operações	na	ordem	correta.
Regras de Negócio:
• Execução	deve	seguir	ordem	definida	pelo	grafo	otimizado.
Critérios de Avaliação
Critério Peso
Interface gráfica funcional – Deve existir e permitir digitar a cláusula SQL e ver o resultado do grafo de
operadores.
1,0
Codificação e Execução do Parsing e validação correta – Deve ser possível identificar que o parsing ocorreu
corretamente e verificar a codificação. Será observado o método de validação (expressão regular ou outro
método)
2,0
Codificação e Execução da Conversão para álgebra relacional – Será observado o resultado da conversão
para álgebra relacional e a forma como foi codificado.
1,5
Codificação e Execução da exibição do Grafo de operadores otimizado 1,0
Ordem de execução apresentada 1,5
Codificação e Aplicação da heurística de redução de tuplas 1,0
Codificação e Aplicação da heurística de redução de atributos 1,0
Codificação e Uso da Junção. 1,0
Total 10,0
Anexos e Protótipos
Imagem 01 – Modelo de dados de referência para os metadados
Nomes das Tabelas e Campos para os metadados – Para conhecer os campos e tabelas para usar no
parse
• Categoria (idCategoria, Descricao )
• Produto (idProduto, Nome, Descricao, Preco, QuantEstoque, Categoria_idCategoria)
• TipoCliente ( idTipoCliente, Descricao)
• Cliente (idCliente, Nome, Email, Nascimento, Senha, TipoCliente_idTipoCliente, DataRegistro)
• TipoEndereco (idTipoEndereco , Descricao)
• Endereco (idEndereco, EnderecoPadrao, Logradouro, Numero, Complemento, Bairro, Cidade, UF,
CEP, TipoEndereco_idTipoEndereco, Cliente_idCliente )
• Telefone (Numero, Cliente_idCliente)
• Status (idStatus, Descricao)
• Pedido (idPedido, Status_idStatus, DataPedido, ValorTotalPedido, Cliente_idCliente)
• Pedido_has_Produto(idPedidoProduto, Pedido_idPedido, Produto_idProduto, Quantidade,
PrecoUnitario)
Heurísticas Básicas a serem usadas
a. Aplicar primeiro as operações que reduzem o tamanho dos resultados intermediários
i. operações de seleção - reduzem o número de tuplas
ii. operações de projeção - reduzem o número de atributos
b. Aplicar primeiro as operações de seleção e de junção mais restritivas
i. reordenar os nós folha da árvore de consulta
ii. evitar a operação de produto cartesiano
iii. ajustar o restante da árvore de forma apropriada
Funcionamento:
• A string com a consulta SQL é entrada na interface gráfica;
• A string é parseada e o comando SQL é validado além de validar se as tabelas existem e se
os campos informados no select existem nas tabelas;
• O comando SQL é convertido para álgebra relacional;
• Mostrar na Interface a conversão do SQL para álgebra relacional;
• A álgebra relacional é otimizada conforme as heurísticas solicitadas (ver item 5);
• O grafo de operadores é construído em memória;
• O grafo de operadores deve ser mostrado na Interface gráfica;
• O resultado da consulta mostrando cada operação e a ordem que será executada, é exibido
na interface gráfica (plano de execução).
