import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ShowSource extends JFrame {

	private final Source source;
	public ShowSource(Source source) throws HeadlessException {
		this.source = source;
		JPanel mainPanel = new JPanel();
		JPanel containerPanel = new JPanel();
		try
		{
			switch (source.type)
			{
				case "img":
					BufferedImage image = ImageIO.read(source.sourceFile);
					containerPanel.add(new JLabel(new ImageIcon(image)));
					break;//make 
				case "vid":
					break;
				case "aud":
					break;
				case "link":
					break;
				case "file":
					break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		mainPanel.add(containerPanel);
		this.add(mainPanel);
		this.validate();
		this.pack();
		this.setVisible(true);
	}

}
