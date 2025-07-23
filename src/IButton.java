import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class IButton extends JButton {

	private final String resPath = "img/";
	private String imgPath,title,name;
	private ImageIcon originalIcon, invertedIcon;
    private int imgScale = Constants.imgScale;
    private boolean darkMode = true;
    public IButton(){}
	public IButton(String name,String title) {
		this.title = title;
		setText(title);
		this.setToolTipText(title);
		this.name = name;
		setImage(name);
	}
	public IButton(String name,String title,boolean hideText) {
		this.title = title;
		if (!hideText)
			setText(title);
		this.setToolTipText(title);
		this.name = name;
		setImage(name);
	}
	public void setImageScale(int newScale)
	{
		imgScale = newScale;
		if (name != null)
			setImage(name);
	}
	public void setToolTip(String s)
	{
		this.setToolTipText(s);
	}
	public void setWide()
	{
		//setHorizontalTextPosition(SwingConstants.RIGHT);
		//setHorizontalAlignment(SwingConstants.RIGHT);
	}
	public void setImage(String imgFilename)
	{
		imgPath = resPath+imgFilename;
		try {
			Image i = ImageIO.read(getClass().getResourceAsStream(imgPath));
			originalIcon = new ImageIcon(i.getScaledInstance(imgScale, imgScale, Image.SCALE_SMOOTH));
			invertedIcon = invertIconColors(originalIcon);
			setIcon((darkMode) ? invertedIcon:originalIcon);
			revalidate();
			repaint();
		} catch (IllegalArgumentException | IOException e) {
			
			e.printStackTrace();
		}
	}
	public String getTitle() {
		return title;
	}
	public void setDark(boolean darkMode) {
		this.darkMode = darkMode;
		if (darkMode) {
            setIcon(invertedIcon);
            setForeground(Color.WHITE);
        } else {
            setIcon(originalIcon);
            setForeground(Color.BLACK);
        }
    }

    private ImageIcon invertIconColors(ImageIcon icon) {
        Image image = icon.getImage();
        BufferedImage buffered = new BufferedImage(
            image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = buffered.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        for (int y = 0; y < buffered.getHeight(); y++) {
            for (int x = 0; x < buffered.getWidth(); x++) {
                int rgba = buffered.getRGB(x, y);
                Color col = new Color(rgba, true);
                Color inv = new Color(
                    255 - col.getRed(),
                    255 - col.getGreen(),
                    255 - col.getBlue(),
                    col.getAlpha()
                );
                //System.out.println("Changing x:"+x+",y:"+y+" from ("+col.getRed()+","+col.getGreen()+","+col.getBlue()+") to ("+inv+")");
                buffered.setRGB(x, y, inv.getRGB());
            }
        }

        return new ImageIcon(buffered);
    }
}
