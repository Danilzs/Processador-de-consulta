package br.edu.processador.graph;

import br.edu.processador.model.SQLQuery;

import java.util.ArrayList;
import java.util.List;

public class OperatorGraph {
    private OperatorNode root;

    public OperatorGraph() {
    }

    public void buildFromQuery(SQLQuery query) {
        // Build the operator tree from bottom to top
        
        // 1. Create leaf nodes for tables
        List<OperatorNode> tableNodes = new ArrayList<>();
        for (String table : query.getFromTables()) {
            OperatorNode tableNode = new OperatorNode(OperatorNode.OperatorType.TABLE, table);
            tableNodes.add(tableNode);
        }

        // 2. Build join nodes if there are joins
        OperatorNode currentNode;
        if (query.getJoins().isEmpty()) {
            // No joins, just use the first table
            currentNode = tableNodes.get(0);
        } else {
            // Create join nodes
            currentNode = tableNodes.get(0);
            int joinIndex = 0;
            for (SQLQuery.JoinClause join : query.getJoins()) {
                OperatorNode joinNode = new OperatorNode(
                    OperatorNode.OperatorType.JOIN,
                    join.getCondition()
                );
                joinNode.addChild(currentNode);
                joinNode.addChild(tableNodes.get(joinIndex + 1));
                currentNode = joinNode;
                joinIndex++;
            }
        }

        // 3. Add selection node if WHERE clause exists
        if (query.getWhereClause() != null && !query.getWhereClause().isEmpty()) {
            OperatorNode selectionNode = new OperatorNode(
                OperatorNode.OperatorType.SELECTION,
                query.getWhereClause()
            );
            selectionNode.addChild(currentNode);
            currentNode = selectionNode;
        }

        // 4. Add projection node at the top
        OperatorNode projectionNode = new OperatorNode(
            OperatorNode.OperatorType.PROJECTION,
            String.join(", ", query.getSelectColumns())
        );
        projectionNode.addChild(currentNode);

        this.root = projectionNode;
    }

    public OperatorNode getRoot() {
        return root;
    }

    public void setRoot(OperatorNode root) {
        this.root = root;
    }

    public String toTreeString() {
        if (root == null) {
            return "Empty graph";
        }
        return "Grafo de Operadores:\n\n" + root.toTreeString();
    }

    public List<String> getExecutionPlan() {
        List<String> plan = new ArrayList<>();
        if (root != null) {
            collectExecutionOrder(root, plan, 1);
        }
        return plan;
    }

    private int collectExecutionOrder(OperatorNode node, List<String> plan, int step) {
        // Post-order traversal: children first, then parent
        for (OperatorNode child : node.getChildren()) {
            step = collectExecutionOrder(child, plan, step);
        }

        // Add current operation
        if (node.getType() != OperatorNode.OperatorType.TABLE) {
            plan.add(step + ". " + node.toString());
            step++;
        }

        return step;
    }
}
