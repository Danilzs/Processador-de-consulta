package br.edu.processador.model;

import java.util.ArrayList;
import java.util.List;

public class TableMetadata {
    private String name;
    private List<String> columns;

    public TableMetadata(String name) {
        this.name = name;
        this.columns = new ArrayList<>();
    }

    public void addColumn(String column) {
        this.columns.add(column);
    }

    public String getName() {
        return name;
    }

    public List<String> getColumns() {
        return columns;
    }

    public boolean hasColumn(String column) {
        return columns.stream()
                .anyMatch(c -> c.equalsIgnoreCase(column));
    }
}
