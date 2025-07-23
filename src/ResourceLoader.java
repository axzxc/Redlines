import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ResourceLoader {
	
    public static Image loadIcon(String path) {
        try {
            return ImageIO.read(ResourceLoader.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
