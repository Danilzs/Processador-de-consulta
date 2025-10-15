package br.edu.processador.model;

import java.util.ArrayList;
import java.util.List;

public class SQLQuery {
    private List<String> selectColumns;
    private List<String> fromTables;
    private String whereClause;
    private List<JoinClause> joins;

    public SQLQuery() {
        this.selectColumns = new ArrayList<>();
        this.fromTables = new ArrayList<>();
        this.joins = new ArrayList<>();
    }

    public List<String> getSelectColumns() {
        return selectColumns;
    }

    public void setSelectColumns(List<String> selectColumns) {
        this.selectColumns = selectColumns;
    }

    public List<String> getFromTables() {
        return fromTables;
    }

    public void setFromTables(List<String> fromTables) {
        this.fromTables = fromTables;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public List<JoinClause> getJoins() {
        return joins;
    }

    public void addJoin(JoinClause join) {
        this.joins.add(join);
    }

    public static class JoinClause {
        private String table;
        private String condition;

        public JoinClause(String table, String condition) {
            this.table = table;
            this.condition = condition;
        }

        public String getTable() {
            return table;
        }

        public String getCondition() {
            return condition;
        }
    }
}
