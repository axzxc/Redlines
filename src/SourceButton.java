import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

public class SourceButton extends IButton {
    private double curvature = 0.3; // 0.0 = sharp, 1.0 = fully rounded
    private Source source;
    private String imgPath;
    private int titleLength = 20;
    private int imgScale = 64;
    private Color BG;
    private static final int FIXED_SIZE = 100;

    public SourceButton(Source s) {
    	source = s;
    	BG = ((!s.isPrimary) ? new Color(100,40,40):new Color(40,40,100));
    	setBackground(BG);
    	setImageScale(imgScale);
    	setHorizontalTextPosition(SwingConstants.CENTER);
    	setVerticalTextPosition(SwingConstants.BOTTOM);
    	String buttName = (source.name.length() > titleLength) ? source.name.substring(0,titleLength)+"...":source.name;
    	setText(buttName);
    	String iconName = "file";
    	switch(s.type)
    	{
	    	case "link":
				iconName = "www";
				break;
	    	case "vid":
				iconName = "vid";
				break;
	    	case "aud":
				iconName = "audio";
				break;
    	}
    	imgPath = iconName+".png"; 		
    	try {
    		Image i = null;
    		if (s.type.equals("img"))
    			i=ImageIO.read(source.sourceFile);
    		else
    			setImage(imgPath);
			if (i!=null)
				setIcon(new ImageIcon(i.getScaledInstance(imgScale, imgScale, Image.SCALE_SMOOTH)));
		} catch (IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	setToolTipText(source.name);
        //setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setFont(Constants.descFont);
        setMargin(new Insets(8, 8, 8, 8));
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(FIXED_SIZE, FIXED_SIZE);
    }
    public void setCurvature(double c) {
        curvature = Math.max(0.0, Math.min(1.0, c));
        repaint();
    }

    public double getCurvature() {
        return curvature;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int arcW = (int) (width * curvature);
        int arcH = (int) (height * curvature);

        // Clip to rounded shape BEFORE calling super
        Shape clip = new RoundRectangle2D.Float(0, 0, width, height, arcW, arcH);
        g2.setClip(clip);

        // Let UI delegate paint the default "content area filled" look
        super.paintComponent(g2);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int arcW = (int) (width * curvature);
        int arcH = (int) (height * curvature);

        g2.setColor(Color.BLACK); // customize border color here
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, width - 1, height - 1, arcW, arcH);

        g2.dispose();
    }
}
