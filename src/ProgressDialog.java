import javax.swing.*;
import java.awt.*;

public class ProgressDialog {
    private final JDialog dialog;
    private final JProgressBar progressBar;
    private final Thread internalUpdater;
    private volatile boolean running = true;
    private int currentValue = 0;
    private int maxValue;

    public ProgressDialog(Frame owner, String title, int maxValue) {
        this.maxValue = maxValue;
        progressBar = new JProgressBar(0, maxValue);
        progressBar.setStringPainted(true);
        
        dialog = new JDialog(owner, title, false);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());
        dialog.add(progressBar, BorderLayout.CENTER);
        dialog.setSize(300, 80);
        dialog.setLocationRelativeTo(owner);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        internalUpdater = new Thread(() -> {
            while (running) {
                SwingUtilities.invokeLater(() -> progressBar.setValue(currentValue));
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }

    public void show() {
        dialog.setVisible(true);
        internalUpdater.start();
    }

    public void setProgress(int value) {
        this.currentValue = value;
    }

    public void setMax(int newMax) {
        this.maxValue = newMax;
        SwingUtilities.invokeLater(() -> progressBar.setMaximum(newMax));
    }

    public void setText(String text) {
        SwingUtilities.invokeLater(() -> progressBar.setString(text));
    }

    public void finish() {
        running = false;
        internalUpdater.interrupt();
        dialog.dispose();
    }
}
