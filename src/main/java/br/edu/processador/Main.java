package br.edu.processador;

import br.edu.processador.ui.QueryProcessorUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and show GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                QueryProcessorUI ui = new QueryProcessorUI();
                ui.setVisible(true);
            }
        });
    }
}
