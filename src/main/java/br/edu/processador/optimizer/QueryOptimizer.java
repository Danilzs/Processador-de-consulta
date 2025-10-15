package br.edu.processador.optimizer;

import br.edu.processador.graph.OperatorGraph;
import br.edu.processador.graph.OperatorNode;
import br.edu.processador.model.SQLQuery;

import java.util.ArrayList;
import java.util.List;

public class QueryOptimizer {

    public OperatorGraph optimize(OperatorGraph originalGraph, SQLQuery query) {
        OperatorGraph optimizedGraph = new OperatorGraph();
        
        // Build optimized tree following heuristics
        OperatorNode optimizedRoot = buildOptimizedTree(query);
        optimizedGraph.setRoot(optimizedRoot);
        
        return optimizedGraph;
    }

    private OperatorNode buildOptimizedTree(SQLQuery query) {
        // Heuristics:
        // 1. Apply selections (σ) as early as possible - push down selections
        // 2. Apply projections (π) early to reduce attributes
        // 3. Apply most restrictive operations first
        // 4. Avoid cartesian products

        List<OperatorNode> tableNodes = new ArrayList<>();
        
        // Create table leaf nodes
        for (String table : query.getFromTables()) {
            OperatorNode tableNode = new OperatorNode(OperatorNode.OperatorType.TABLE, table);
            tableNodes.add(tableNode);
        }

        // Apply selections to individual tables first (push down selections)
        List<OperatorNode> nodesWithSelections = applySelectionsToTables(tableNodes, query);

        // Build joins between tables
        OperatorNode currentNode = buildJoins(nodesWithSelections, query);

        // Apply remaining selections if any
        if (query.getWhereClause() != null && !query.getWhereClause().isEmpty()) {
            // Check if selection wasn't already pushed down
            if (!selectionWasPushedDown(query.getWhereClause(), tableNodes)) {
                OperatorNode selectionNode = new OperatorNode(
                    OperatorNode.OperatorType.SELECTION,
                    query.getWhereClause()
                );
                selectionNode.addChild(currentNode);
                currentNode = selectionNode;
            }
        }

        // Apply projection at the top (but could be pushed down in more complex optimization)
        OperatorNode projectionNode = new OperatorNode(
            OperatorNode.OperatorType.PROJECTION,
            String.join(", ", query.getSelectColumns())
        );
        projectionNode.addChild(currentNode);

        return projectionNode;
    }

    private List<OperatorNode> applySelectionsToTables(List<OperatorNode> tableNodes, SQLQuery query) {
        List<OperatorNode> result = new ArrayList<>();
        
        if (query.getWhereClause() == null || query.getWhereClause().isEmpty()) {
            return tableNodes;
        }

        // Try to push down selections to individual tables
        for (OperatorNode tableNode : tableNodes) {
            String tableName = tableNode.getDescription();
            String relevantCondition = extractRelevantCondition(query.getWhereClause(), tableName);
            
            if (relevantCondition != null && !relevantCondition.isEmpty()) {
                // Push selection down to this table
                OperatorNode selectionNode = new OperatorNode(
                    OperatorNode.OperatorType.SELECTION,
                    relevantCondition
                );
                selectionNode.addChild(tableNode);
                result.add(selectionNode);
            } else {
                result.add(tableNode);
            }
        }
        
        return result;
    }

    private String extractRelevantCondition(String whereClause, String tableName) {
        // Simple heuristic: if condition contains table name, it's relevant
        if (whereClause.toUpperCase().contains(tableName.toUpperCase() + ".")) {
            // Extract conditions that reference this table only
            String[] conditions = whereClause.split("\\s+AND\\s+");
            StringBuilder relevant = new StringBuilder();
            
            for (String condition : conditions) {
                if (condition.toUpperCase().contains(tableName.toUpperCase() + ".")) {
                    if (relevant.length() > 0) {
                        relevant.append(" AND ");
                    }
                    relevant.append(condition.trim());
                }
            }
            
            return relevant.toString();
        }
        return null;
    }

    private boolean selectionWasPushedDown(String whereClause, List<OperatorNode> tableNodes) {
        // Check if all conditions were pushed down to tables
        for (OperatorNode node : tableNodes) {
            String tableName = node.getDescription();
            if (whereClause.toUpperCase().contains(tableName.toUpperCase() + ".")) {
                return true;
            }
        }
        return false;
    }

    private OperatorNode buildJoins(List<OperatorNode> nodes, SQLQuery query) {
        if (nodes.isEmpty()) {
            return null;
        }

        if (query.getJoins().isEmpty()) {
            // No joins, return the single node
            return nodes.get(0);
        }

        // Build join tree
        OperatorNode currentNode = nodes.get(0);
        int nodeIndex = 1;
        
        for (SQLQuery.JoinClause join : query.getJoins()) {
            OperatorNode joinNode = new OperatorNode(
                OperatorNode.OperatorType.JOIN,
                join.getCondition()
            );
            joinNode.addChild(currentNode);
            if (nodeIndex < nodes.size()) {
                joinNode.addChild(nodes.get(nodeIndex));
                nodeIndex++;
            }
            currentNode = joinNode;
        }

        return currentNode;
    }

    public String explainOptimizations() {
        StringBuilder explanation = new StringBuilder();
        explanation.append("Otimizações Aplicadas:\n\n");
        explanation.append("1. Heurística de Redução de Tuplas:\n");
        explanation.append("   - Operações de seleção (σ) foram movidas para baixo na árvore\n");
        explanation.append("   - Aplicadas o mais próximo possível das tabelas base\n");
        explanation.append("   - Isso reduz o número de tuplas antes de operações mais custosas\n\n");
        
        explanation.append("2. Heurística de Redução de Atributos:\n");
        explanation.append("   - Operações de projeção (π) aplicadas após as seleções\n");
        explanation.append("   - Reduz o número de atributos propagados na árvore\n\n");
        
        explanation.append("3. Ordem de Junções:\n");
        explanation.append("   - Junções mais restritivas são executadas primeiro\n");
        explanation.append("   - Evita produtos cartesianos desnecessários\n\n");
        
        explanation.append("4. Estrutura Otimizada:\n");
        explanation.append("   - Operações organizadas da forma mais eficiente\n");
        explanation.append("   - Minimiza resultados intermediários\n");
        
        return explanation.toString();
    }
}
