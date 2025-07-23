import javax.swing.*;
import java.awt.datatransfer.*;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.*;

public class CopyLabel extends JLabel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CopyLabel(String text) {
        super(text);
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // optional: show hand cursor
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                StringSelection selection = new StringSelection(getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, null);

                // Optional: give some feedback
                setToolTipText("Copied to clipboard!");
            }
        });
    }
}
