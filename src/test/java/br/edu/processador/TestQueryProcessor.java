package br.edu.processador;

import br.edu.processador.algebra.RelationalAlgebraConverter;
import br.edu.processador.graph.OperatorGraph;
import br.edu.processador.model.DatabaseSchema;
import br.edu.processador.model.SQLQuery;
import br.edu.processador.optimizer.QueryOptimizer;
import br.edu.processador.parser.SQLParser;

public class TestQueryProcessor {
    public static void main(String[] args) {
        System.out.println("=== TESTE DO PROCESSADOR DE CONSULTAS ===\n");
        
        try {
            // Initialize components
            DatabaseSchema schema = new DatabaseSchema();
            SQLParser parser = new SQLParser(schema);
            RelationalAlgebraConverter algebraConverter = new RelationalAlgebraConverter();
            QueryOptimizer optimizer = new QueryOptimizer();
            
            // Test 1: Simple query
            System.out.println("TESTE 1: Consulta Simples");
            System.out.println("-".repeat(80));
            String sql1 = "SELECT Nome, Email FROM Cliente WHERE idCliente > 100";
            System.out.println("SQL: " + sql1);
            testQuery(sql1, parser, algebraConverter, optimizer);
            
            // Test 2: Query with JOIN
            System.out.println("\n\nTESTE 2: Consulta com JOIN");
            System.out.println("-".repeat(80));
            String sql2 = "SELECT Cliente.Nome, Pedido.DataPedido FROM Cliente JOIN Pedido ON Cliente.idCliente = Pedido.Cliente_idCliente WHERE Cliente.idCliente > 100";
            System.out.println("SQL: " + sql2);
            testQuery(sql2, parser, algebraConverter, optimizer);
            
            // Test 3: Multiple JOINs
            System.out.println("\n\nTESTE 3: Múltiplos JOINs");
            System.out.println("-".repeat(80));
            String sql3 = "SELECT Cliente.Nome, Produto.Nome FROM Cliente JOIN Pedido ON Cliente.idCliente = Pedido.Cliente_idCliente JOIN Pedido_has_Produto ON Pedido.idPedido = Pedido_has_Produto.Pedido_idPedido";
            System.out.println("SQL: " + sql3);
            testQuery(sql3, parser, algebraConverter, optimizer);
            
            // Test 4: Error - Invalid table
            System.out.println("\n\nTESTE 4: Validação de Erro (Tabela Inexistente)");
            System.out.println("-".repeat(80));
            String sql4 = "SELECT Nome FROM TabelaInexistente";
            System.out.println("SQL: " + sql4);
            try {
                parser.parse(sql4);
                System.out.println("ERRO: Deveria ter lançado exceção!");
            } catch (SQLParser.ParseException e) {
                System.out.println("✓ Erro esperado capturado: " + e.getMessage());
            }
            
            // Test 5: Error - Invalid column
            System.out.println("\n\nTESTE 5: Validação de Erro (Coluna Inexistente)");
            System.out.println("-".repeat(80));
            String sql5 = "SELECT ColunaInexistente FROM Cliente";
            System.out.println("SQL: " + sql5);
            try {
                parser.parse(sql5);
                System.out.println("ERRO: Deveria ter lançado exceção!");
            } catch (SQLParser.ParseException e) {
                System.out.println("✓ Erro esperado capturado: " + e.getMessage());
            }
            
            System.out.println("\n\n=== TODOS OS TESTES CONCLUÍDOS COM SUCESSO ===");
            
        } catch (Exception e) {
            System.err.println("ERRO: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testQuery(String sql, SQLParser parser, 
                                   RelationalAlgebraConverter algebraConverter,
                                   QueryOptimizer optimizer) throws SQLParser.ParseException {
        // Parse
        SQLQuery query = parser.parse(sql);
        System.out.println("\n✓ Parse bem-sucedido");
        System.out.println("  Tabelas: " + query.getFromTables());
        System.out.println("  Colunas SELECT: " + query.getSelectColumns());
        System.out.println("  WHERE: " + (query.getWhereClause() != null ? query.getWhereClause() : "N/A"));
        System.out.println("  JOINs: " + query.getJoins().size());
        
        // Convert to algebra
        String algebra = algebraConverter.convert(query);
        System.out.println("\n✓ Álgebra Relacional:");
        System.out.println("  " + algebra);
        
        // Build graph
        OperatorGraph originalGraph = new OperatorGraph();
        originalGraph.buildFromQuery(query);
        System.out.println("\n✓ Grafo Original:");
        System.out.println(originalGraph.toTreeString());
        
        // Optimize
        OperatorGraph optimizedGraph = optimizer.optimize(originalGraph, query);
        System.out.println("\n✓ Grafo Otimizado:");
        System.out.println(optimizedGraph.toTreeString());
        
        // Execution plan
        System.out.println("\n✓ Plano de Execução:");
        for (String step : optimizedGraph.getExecutionPlan()) {
            System.out.println("  " + step);
        }
    }
}
