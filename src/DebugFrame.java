import javax.swing.*;
import java.awt.*;
import java.io.*;

public class DebugFrame extends JFrame {

    private JTextArea consoleTextArea;

    public DebugFrame() {
        super("Console Output");
        setIconImage(ResourceLoader.loadIcon("img/logo.png"));
        // Create JTextArea and wrap in scroll pane
        consoleTextArea = new JTextArea(20, 50);
        consoleTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(consoleTextArea);

        // Layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // Redirect System.out
        redirectSystemOut();

        // Basic JFrame settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void redirectSystemOut() {
        OutputStream out = new OutputStream() {
            private final int MAX_LINES = 1000;

            @Override
            public void write(int b) {
                appendText(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) {
                appendText(new String(b, off, len));
            }

            private void appendText(String text) {
                SwingUtilities.invokeLater(() -> {
                    consoleTextArea.append(text);
                    trimLines();
                    consoleTextArea.setCaretPosition(consoleTextArea.getDocument().getLength());
                });
            }

            private void trimLines() {
                String[] lines = consoleTextArea.getText().split("\n");
                if (lines.length > MAX_LINES) {
                    int trimStart = lines.length - MAX_LINES;
                    StringBuilder trimmed = new StringBuilder();
                    for (int i = trimStart; i < lines.length; i++) {
                        trimmed.append(lines[i]).append("\n");
                    }
                    consoleTextArea.setText(trimmed.toString());
                }
            }
        };

        PrintStream ps = new PrintStream(out, true);
        System.setOut(ps);
        System.setErr(ps);
    }
}
