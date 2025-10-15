package br.edu.processador.graph;

import java.util.ArrayList;
import java.util.List;

public class OperatorNode {
    public enum OperatorType {
        TABLE,          // Leaf node - represents a table
        SELECTION,      // σ - filters rows
        PROJECTION,     // π - selects columns
        JOIN            // ⋈ - joins tables
    }

    private OperatorType type;
    private String description;
    private List<OperatorNode> children;
    private int cost;  // Estimated cost for optimization

    public OperatorNode(OperatorType type, String description) {
        this.type = type;
        this.description = description;
        this.children = new ArrayList<>();
        this.cost = 0;
    }

    public void addChild(OperatorNode child) {
        this.children.add(child);
    }

    public OperatorType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public List<OperatorNode> getChildren() {
        return children;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return type + ": " + description;
    }

    public String toTreeString() {
        return toTreeString("", true);
    }

    private String toTreeString(String prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(isTail ? "└── " : "├── ").append(this.toString()).append("\n");
        
        for (int i = 0; i < children.size(); i++) {
            boolean last = (i == children.size() - 1);
            sb.append(children.get(i).toTreeString(prefix + (isTail ? "    " : "│   "), last));
        }
        
        return sb.toString();
    }
}
