import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class PieChart extends JPanel {

	private final int[] data;
	private final Color[] colors = {Color.RED,Color.BLUE,Color.GREEN};
	public PieChart(int[] data) {
		this.data = data;
	}
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(width, height) / 2 - 10;

        double total = 0;
        for (int value : data) {
            total += value;
        }

        double startAngle = 0;
        for (int i = 0; i < data.length; i++) {
        	g2d.setColor(colors[i%colors.length]);
        	double angle = (data[i] / total) * 360;
            g2d.fillArc(centerX - radius, centerY - radius, 2 * radius, 2 * radius, (int) startAngle, (int) angle);
            startAngle += angle;
        }
	}
}
