package br.edu.processador.parser;

import br.edu.processador.model.DatabaseSchema;
import br.edu.processador.model.SQLQuery;
import br.edu.processador.model.TableMetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLParser {
    private DatabaseSchema schema;

    public SQLParser(DatabaseSchema schema) {
        this.schema = schema;
    }

    public SQLQuery parse(String sql) throws ParseException {
        if (sql == null || sql.trim().isEmpty()) {
            throw new ParseException("Query cannot be empty");
        }

        // Normalize: convert to uppercase, replace multiple spaces with single space
        String normalized = sql.trim().replaceAll("\\s+", " ").toUpperCase();

        // Validate basic SQL structure
        if (!normalized.startsWith("SELECT")) {
            throw new ParseException("Query must start with SELECT");
        }

        SQLQuery query = new SQLQuery();

        // Parse SELECT clause
        parseSelect(normalized, query);

        // Parse FROM clause
        parseFrom(normalized, query);

        // Parse JOINs
        parseJoins(normalized, query);

        // Parse WHERE clause
        parseWhere(normalized, query);

        // Validate columns
        validateColumns(query);

        return query;
    }

    private void parseSelect(String sql, SQLQuery query) throws ParseException {
        Pattern selectPattern = Pattern.compile("SELECT\\s+(.+?)\\s+FROM", Pattern.CASE_INSENSITIVE);
        Matcher matcher = selectPattern.matcher(sql);

        if (!matcher.find()) {
            throw new ParseException("Invalid SELECT clause");
        }

        String selectPart = matcher.group(1).trim();
        String[] columns = selectPart.split("\\s*,\\s*");

        for (String col : columns) {
            query.getSelectColumns().add(col.trim());
        }

        if (query.getSelectColumns().isEmpty()) {
            throw new ParseException("SELECT clause must have at least one column");
        }
    }

    private void parseFrom(String sql, SQLQuery query) throws ParseException {
        // Extract FROM clause (between FROM and WHERE/JOIN or end)
        Pattern fromPattern = Pattern.compile("FROM\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = fromPattern.matcher(sql);

        if (!matcher.find()) {
            throw new ParseException("Invalid FROM clause");
        }

        String tableName = matcher.group(1);
        if (!schema.hasTable(tableName)) {
            throw new ParseException("Table '" + tableName + "' does not exist");
        }

        query.getFromTables().add(tableName);
    }

    private void parseJoins(String sql, SQLQuery query) throws ParseException {
        // Match all JOIN clauses
        Pattern joinPattern = Pattern.compile("JOIN\\s+(\\w+)\\s+ON\\s+([^\\s]+\\s*[=<>]+\\s*[^\\s]+)", 
                                              Pattern.CASE_INSENSITIVE);
        Matcher matcher = joinPattern.matcher(sql);

        while (matcher.find()) {
            String tableName = matcher.group(1);
            String condition = matcher.group(2);

            if (!schema.hasTable(tableName)) {
                throw new ParseException("Table '" + tableName + "' does not exist");
            }

            query.addJoin(new SQLQuery.JoinClause(tableName, condition));
            query.getFromTables().add(tableName);
        }
    }

    private void parseWhere(String sql, SQLQuery query) {
        // Extract WHERE clause
        Pattern wherePattern = Pattern.compile("WHERE\\s+(.+?)(?:\\s*$)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = wherePattern.matcher(sql);

        if (matcher.find()) {
            String whereClause = matcher.group(1).trim();
            // Validate operators
            if (!whereClause.matches(".*[=<>].*")) {
                return; // No where clause with operators
            }
            query.setWhereClause(whereClause);
        }
    }

    private void validateColumns(SQLQuery query) throws ParseException {
        List<String> allTables = query.getFromTables();

        for (String column : query.getSelectColumns()) {
            if (column.equals("*")) {
                continue; // Wildcard is always valid
            }

            boolean found = false;

            // Check if column has table prefix (e.g., Cliente.Nome)
            if (column.contains(".")) {
                String[] parts = column.split("\\.");
                String tableName = parts[0];
                String columnName = parts[1];

                TableMetadata table = schema.getTable(tableName);
                if (table != null && table.hasColumn(columnName)) {
                    found = true;
                }
            } else {
                // Check in all tables
                for (String tableName : allTables) {
                    TableMetadata table = schema.getTable(tableName);
                    if (table != null && table.hasColumn(column)) {
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                throw new ParseException("Column '" + column + "' does not exist in any of the specified tables");
            }
        }
    }

    public static class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }
    }
}
