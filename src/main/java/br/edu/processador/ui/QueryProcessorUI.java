package br.edu.processador.ui;

import br.edu.processador.algebra.RelationalAlgebraConverter;
import br.edu.processador.graph.OperatorGraph;
import br.edu.processador.model.DatabaseSchema;
import br.edu.processador.model.SQLQuery;
import br.edu.processador.optimizer.QueryOptimizer;
import br.edu.processador.parser.SQLParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class QueryProcessorUI extends JFrame {
    private JTextArea queryInput;
    private JTextArea algebraOutput;
    private JTextArea graphOutput;
    private JTextArea optimizationOutput;
    private JTextArea executionPlanOutput;
    private JButton processButton;
    private JButton clearButton;

    private DatabaseSchema schema;
    private SQLParser parser;
    private RelationalAlgebraConverter algebraConverter;
    private QueryOptimizer optimizer;

    public QueryProcessorUI() {
        schema = new DatabaseSchema();
        parser = new SQLParser(schema);
        algebraConverter = new RelationalAlgebraConverter();
        optimizer = new QueryOptimizer();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Processador de Consultas SQL");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top panel - Query input
        JPanel topPanel = createQueryInputPanel();
        add(topPanel, BorderLayout.NORTH);

        // Center panel - Tabbed pane with results
        JTabbedPane tabbedPane = createResultsTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Bottom panel - Action buttons
        JPanel bottomPanel = createButtonPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createQueryInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Entrada da Consulta SQL"));

        queryInput = new JTextArea(5, 80);
        queryInput.setFont(new Font("Monospaced", Font.PLAIN, 14));
        queryInput.setLineWrap(true);
        queryInput.setWrapStyleWord(true);
        queryInput.setText("SELECT Cliente.Nome, Pedido.DataPedido FROM Cliente JOIN Pedido ON Cliente.idCliente = Pedido.Cliente_idCliente WHERE Cliente.idCliente > 100");

        JScrollPane scrollPane = new JScrollPane(queryInput);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JTabbedPane createResultsTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1: Relational Algebra
        algebraOutput = new JTextArea();
        algebraOutput.setEditable(false);
        algebraOutput.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane algebraScroll = new JScrollPane(algebraOutput);
        tabbedPane.addTab("Álgebra Relacional", algebraScroll);

        // Tab 2: Operator Graph
        graphOutput = new JTextArea();
        graphOutput.setEditable(false);
        graphOutput.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane graphScroll = new JScrollPane(graphOutput);
        tabbedPane.addTab("Grafo de Operadores", graphScroll);

        // Tab 3: Optimizations
        optimizationOutput = new JTextArea();
        optimizationOutput.setEditable(false);
        optimizationOutput.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane optimizationScroll = new JScrollPane(optimizationOutput);
        tabbedPane.addTab("Otimizações", optimizationScroll);

        // Tab 4: Execution Plan
        executionPlanOutput = new JTextArea();
        executionPlanOutput.setEditable(false);
        executionPlanOutput.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane executionScroll = new JScrollPane(executionPlanOutput);
        tabbedPane.addTab("Plano de Execução", executionScroll);

        return tabbedPane;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        processButton = new JButton("Processar Consulta");
        processButton.setFont(new Font("Arial", Font.BOLD, 14));
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processQuery();
            }
        });

        clearButton = new JButton("Limpar");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAll();
            }
        });

        panel.add(processButton);
        panel.add(clearButton);

        return panel;
    }

    private void processQuery() {
        String sql = queryInput.getText().trim();

        if (sql.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, insira uma consulta SQL.",
                    "Entrada Vazia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Step 1: Parse and validate SQL
            SQLQuery query = parser.parse(sql);

            // Step 2: Convert to relational algebra
            String algebra = algebraConverter.convertWithFormatting(query);
            algebraOutput.setText(algebra);

            // Step 3: Build operator graph
            OperatorGraph originalGraph = new OperatorGraph();
            originalGraph.buildFromQuery(query);

            // Step 4: Optimize query
            OperatorGraph optimizedGraph = optimizer.optimize(originalGraph, query);

            // Display graphs
            StringBuilder graphDisplay = new StringBuilder();
            graphDisplay.append("=== GRAFO ORIGINAL ===\n\n");
            graphDisplay.append(originalGraph.toTreeString());
            graphDisplay.append("\n\n=== GRAFO OTIMIZADO ===\n\n");
            graphDisplay.append(optimizedGraph.toTreeString());
            graphOutput.setText(graphDisplay.toString());

            // Display optimizations
            optimizationOutput.setText(optimizer.explainOptimizations());

            // Step 5: Generate execution plan
            List<String> executionPlan = optimizedGraph.getExecutionPlan();
            StringBuilder planDisplay = new StringBuilder();
            planDisplay.append("Plano de Execução (Ordem de Operações):\n\n");
            for (String step : executionPlan) {
                planDisplay.append(step).append("\n");
            }
            executionPlanOutput.setText(planDisplay.toString());

            JOptionPane.showMessageDialog(this,
                    "Consulta processada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLParser.ParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao processar consulta:\n" + e.getMessage(),
                    "Erro de Parsing",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro inesperado:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearAll() {
        queryInput.setText("");
        algebraOutput.setText("");
        graphOutput.setText("");
        optimizationOutput.setText("");
        executionPlanOutput.setText("");
    }
}
