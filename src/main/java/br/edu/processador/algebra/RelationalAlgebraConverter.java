package br.edu.processador.algebra;

import br.edu.processador.model.SQLQuery;

import java.util.List;
import java.util.stream.Collectors;

public class RelationalAlgebraConverter {

    public String convert(SQLQuery query) {
        StringBuilder algebra = new StringBuilder();

        // Build from bottom to top
        List<String> tables = query.getFromTables();
        
        // Start with base tables or joins
        String baseExpression = buildBaseExpression(query, tables);
        
        // Apply selections (WHERE clause)
        if (query.getWhereClause() != null && !query.getWhereClause().isEmpty()) {
            baseExpression = "σ[" + query.getWhereClause() + "](" + baseExpression + ")";
        }
        
        // Apply projection (SELECT clause)
        String selectList = String.join(", ", query.getSelectColumns());
        algebra.append("π[").append(selectList).append("](").append(baseExpression).append(")");

        return algebra.toString();
    }

    private String buildBaseExpression(SQLQuery query, List<String> tables) {
        if (tables.isEmpty()) {
            return "";
        }

        if (query.getJoins().isEmpty()) {
            // No joins, just the base table
            return tables.get(0);
        }

        // Build join expression
        StringBuilder joinExpr = new StringBuilder(tables.get(0));
        
        for (SQLQuery.JoinClause join : query.getJoins()) {
            joinExpr.append(" ⋈[").append(join.getCondition()).append("] ").append(join.getTable());
        }

        return joinExpr.toString();
    }

    public String convertWithFormatting(SQLQuery query) {
        StringBuilder result = new StringBuilder();
        result.append("Expressão em Álgebra Relacional:\n\n");

        List<String> tables = query.getFromTables();
        
        // Show step by step
        result.append("1. Tabelas base:\n");
        for (String table : tables) {
            result.append("   - ").append(table).append("\n");
        }
        result.append("\n");

        // Show joins
        if (!query.getJoins().isEmpty()) {
            result.append("2. Junções (⋈):\n");
            for (SQLQuery.JoinClause join : query.getJoins()) {
                result.append("   - ").append(join.getTable())
                      .append(" ON ").append(join.getCondition()).append("\n");
            }
            result.append("\n");
        }

        // Show selections
        if (query.getWhereClause() != null && !query.getWhereClause().isEmpty()) {
            result.append((query.getJoins().isEmpty() ? "2" : "3"))
                  .append(". Seleção (σ):\n");
            result.append("   - σ[").append(query.getWhereClause()).append("]\n\n");
        }

        // Show projection
        int step = 2;
        if (!query.getJoins().isEmpty()) step++;
        if (query.getWhereClause() != null && !query.getWhereClause().isEmpty()) step++;
        
        result.append(step).append(". Projeção (π):\n");
        result.append("   - π[").append(String.join(", ", query.getSelectColumns())).append("]\n\n");

        result.append("Expressão completa:\n");
        result.append(convert(query));

        return result.toString();
    }
}
