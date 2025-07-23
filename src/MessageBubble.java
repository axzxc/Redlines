import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MessageBubble extends JPanel {

	private String CID,username,message,timestamp;
	private int padding = 5;
	private int width;
	public MessageBubble(JPanel display, ArrayList<MessageBubble> messageList,String message,String CID,String username,String timestamp, boolean fromMe) {
		this.message = message;
		this.CID = CID;
		this.username = username;
		this.timestamp = timestamp;
		for (MessageBubble bubble : messageList)
		{
			if (bubble.equals(this))
				return;
		}
		this.width = Constants.commentsWidth - 40;
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		JLabel userLabel = new JLabel(username);
		userLabel.setForeground(Color.WHITE);
		Dimension ulDim = userLabel.getPreferredSize();
		ulDim.width = width; // clamp width
		Constants.setFixedSize(userLabel, ulDim);
		//userLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
		add(userLabel);
		JTextArea messageArea = new JTextArea(message);
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		messageArea.setEditable(false);
		messageArea.setOpaque(false);
		messageArea.setForeground(Color.WHITE);
		Dimension messageAreaSize = messageArea.getPreferredSize();
		messageArea.setSize(width, Short.MAX_VALUE);
		messageAreaSize = messageArea.getPreferredSize(); 
		messageArea.setSize(messageAreaSize);
		add(messageArea);
		setToolTipText("<html>"+CID+"<br>"+timestamp+"</html>");
		if (fromMe)
		{
			setBackground(Color.GRAY);
			userLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		else
		{
			userLabel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			setBackground(Color.GREEN);
			setAlignmentX(Component.RIGHT_ALIGNMENT);
		}
		setOpaque(true);
		messageList.add(this);
		Dimension thisDim = new Dimension(width,((padding*2)+messageArea.getHeight()+ulDim.height));
		Constants.setFixedSize(this,thisDim);
		this.setSize(thisDim);
		//this.setBorder(BorderFactory.createLineBorder(Color.RED));
		setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
		display.add(this);
		display.add(Box.createVerticalStrut(5));
		display.validate();
		display.revalidate();
		display.repaint();
		
	}
	public String getCID() {
		return CID;
	}
	public String getUsername() {
		return username;
	}
	public String getMessage() {
		return message;
	}
	public String getTimestamp()
	{
		return timestamp;
	}
	public boolean equals(MessageBubble bubble)
	{
		return bubble.getTimestamp().equals(timestamp) && bubble.getCID().equals(CID) && bubble.getMessage().equals(message);
	}
}
