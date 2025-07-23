import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

public class RoundedIButt extends IButton {

	private boolean darkMode = true;
	public RoundedIButt(String name, String title) {
		super(name, title);
		setContentAreaFilled(false);
        setFocusPainted(false);
        setMargin(new Insets(2, 8, 2, 8));
        setBackground(Constants.buttonColor);
        setDark(false);
        setForeground(Color.WHITE);
	}

	@Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Antialias for smoother edges
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill rounded rect background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        super.paintComponent(g);
        g2.dispose();
    }

	@Override
	protected void paintBorder(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g.create();
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    g2.setColor(Color.BLACK);
	    g2.setStroke(new BasicStroke(1f));
	    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

	    g2.dispose();
	}

    @Override
    public boolean isOpaque() {
        return false;
    }
}
