import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class InputTextField extends JTextField {
    private boolean hasTyped = false;
    private final InputSubmitHandler submitHandler;

    public InputTextField(String placeholder, InputSubmitHandler handler) {
        super(placeholder);
        this.submitHandler = handler;
        setBackground(Constants.annoBG);
        setForeground(Constants.textColorDM);
        setFont(Constants.descFont);
        ((AbstractDocument) this.getDocument()).setDocumentFilter(new DigitOnlyFilter());
 
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	if (!hasTyped)
        		{
	        		SwingUtilities.invokeLater(() -> {
	        	        setText("");
	        	        setForeground(Color.CYAN);
	        	        hasTyped = true;
	        	    });
        		}
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    setForeground(Constants.textColorDM);
                    hasTyped = false;
                    submitHandler.submit(getText());
                }
            }
        });
    }

    private static class DigitOnlyFilter extends DocumentFilter {
    	private static final int MAX_LENGTH = 6;

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("\\d+")) {
                int newLength = fb.getDocument().getLength() + string.length();
                if (newLength <= MAX_LENGTH) {
                    super.insertString(fb, offset, string, attr);
                }
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("\\d*")) {
                int currentLength = fb.getDocument().getLength();
                int newLength = currentLength - length + (string != null ? string.length() : 0);
                if (newLength <= MAX_LENGTH) {
                    super.replace(fb, offset, length, string, attr);
                }
            }
        }
    }
}
