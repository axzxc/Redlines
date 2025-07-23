import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * A custom Swing component that extends JTextArea but is styled to look and feel
 * like a JLabel, while allowing its text content to be selected and copied.
 *
 * This class achieves the JLabel-like appearance by:
 * 1. Making the JTextArea non-editable.
 * 2. Removing its default border.
 * 3. Setting its background to be transparent or match its parent's background.
 * 4. Setting its foreground color to match the default JLabel foreground.
 * 5. Optionally preventing it from gaining focus to avoid a focus border.
 */
public class SelectLabel extends JTextArea {

    /**
     * Constructs a new CopyableLabel with the specified text.
     *
     * @param text The text to be displayed by the label.
     */
    public SelectLabel(String text) {
        super(text); // Call JTextArea's constructor with the initial text
        initLabelLookAndFeel();
    }

    /**
     * Constructs a new CopyableLabel with no initial text.
     */
    public SelectLabel() {
        super();
        initLabelLookAndFeel();
    }

    /**
     * Configures the JTextArea to visually resemble a JLabel.
     */
    private void initLabelLookAndFeel() {
        setEditable(false); // Make the text non-editable
        setEnabled(true);
        setLineWrap(true);   // Enable text wrapping for long content
        setWrapStyleWord(true); // Wrap at word boundaries
        setOpaque(false); // Make the background transparent by default
        setBackground(UIManager.getColor("Panel.background"));
        setForeground(UIManager.getColor("Label.foreground")); // Set text color to default label foreground
        setFont(UIManager.getFont("Label.font")); // Set font to default label font
        setBorder(BorderFactory.createEmptyBorder()); // Remove the default JTextArea border
        setFocusable(true); // Prevent it from gaining focus, which avoids a focus border on click
        setMargin(new Insets(8, 8, 8, 8));
    }

    /**
     * Overrides the setBorder method to ensure that an empty border is always
     * used, maintaining the JLabel-like appearance.
     * Any attempt to set a different border will be ignored.
     * @param border The border to set (will be ignored).
     */
    @Override
    public void setBorder(Border border) {
        // Always enforce an empty border to maintain JLabel-like appearance
        super.setBorder(BorderFactory.createEmptyBorder());
    }

}
