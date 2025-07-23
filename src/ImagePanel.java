import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private Image image;
	private final String resPath = "img/";
	public ImagePanel() {}
	public ImagePanel(BufferedImage i)
	{
		image = i;
	}
	public ImagePanel(File f) {
		try {
            image = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public ImagePanel(String filename)
	{
		String imgPath = resPath+filename;
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imgPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Cannot display "+imgPath);
		}
	}
	public void setImage(Image i)
	{
		image = i;
		this.repaint();
		this.revalidate();
	}
	public void setImage(BufferedImage i)
	{
		image = i;
		this.repaint();
		this.revalidate();
	}
	public void setImage(File f)
	{
		try {
            image = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
		this.repaint();
		this.revalidate();
	}
	 @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this); 
        }
    }

}
