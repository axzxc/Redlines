import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class AnnotateBox extends JPanel {

	private boolean isAuthor;
	private Topic topic;
	private String topicID,dbPath;
	private ArrayList<String[]> annotations;
	private JPanel display = new JPanel();
	private JPanel sendMsgPanel;
	private JScrollPane jp,scrollPane;
	private JTextArea messageInput;
	private JButton submitMessage;
	private Dimension d,disDim;
	private long lastSent = 0;
	public AnnotateBox(boolean isAuthor,Dimension d,Topic topic,ArrayList<String[]> annotations) {
		setLayout(new BorderLayout());
		Constants.setFixedSize(this,d);
		setBackground(new Color(245, 245, 240));
		//setBackground(Constants.annoBG);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		display.setLayout(new BoxLayout(display,BoxLayout.Y_AXIS));
		jp = new JScrollPane(display,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.topic = topic;
		this.d = d;
		this.isAuthor = isAuthor;
		this.annotations = annotations;
		topicID = topic.getID();
		dbPath = Constants.annoPath + File.separator + topicID + ".db";
		//update();
		disDim = new Dimension(Constants.commentsWidth,(int) d.getHeight());
		display.setSize(disDim);
		Constants.setFixedSize(jp,disDim);
		if (!isAuthor)
		{
			messageInput = new JTextArea();
			int fontSize = 12;
			Font miFont = new Font("SansSerif", Font.PLAIN, fontSize);
			messageInput.setFont(miFont);
			messageInput.setForeground(Constants.textColorLM);
			messageInput.setLineWrap(true);
			messageInput.setWrapStyleWord(true);
			scrollPane = new JScrollPane(messageInput);
			scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER); // Hide scrollbar if you want
			FontMetrics fm = messageInput.getFontMetrics(miFont);
			int lineHeight = fm.getHeight();  // height of one line
			int minHeight = lineHeight * 2;
			int maxHeight = lineHeight * 4;
			scrollPane.setPreferredSize(new Dimension(Constants.commentsWidth, minHeight));
			scrollPane.setMinimumSize(new Dimension(Constants.commentsWidth, minHeight));
			scrollPane.setMaximumSize(new Dimension(Constants.commentsWidth, maxHeight));
			int maxChars = 100;
			PlainDocument doc = new PlainDocument();

			doc.setDocumentFilter(new DocumentFilter() {
			    @Override
			    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
			        if (string == null) return;

			        int currentLength = fb.getDocument().getLength();
			        int newLength = string.length();

			        if (currentLength + newLength <= maxChars) {
			            super.insertString(fb, offset, string, attr);
			        } else {
			            int allowed = maxChars - currentLength;
			            if (allowed > 0)
			                super.insertString(fb, offset, string.substring(0, allowed), attr);
			            // else: ignore completely
			        }
			    }

			    @Override
			    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			        if (text == null) return;

			        int currentLength = fb.getDocument().getLength();
			        int newLength = text.length();
			        int finalLength = currentLength - length + newLength;

			        if (finalLength <= maxChars) {
			            super.replace(fb, offset, length, text, attrs);
			        } else {
			            int allowed = maxChars - (currentLength - length);
			            if (allowed > 0)
			                super.replace(fb, offset, length, text.substring(0, allowed), attrs);
			        }
			    }
			});
			messageInput.setDocument(doc);
			messageInput.getDocument().addDocumentListener(new DocumentListener() {
				private void updateSize() {
			        scrollPane.revalidate();
			        scrollPane.repaint();
			        Dimension disDim = jp.getSize();
					Constants.setFixedSize(jp,new Dimension(Constants.commentsWidth,(int)disDim.getHeight() - lineHeight));
					jp.repaint();
					jp.revalidate();
			    }

			    public void insertUpdate(DocumentEvent e) { updateSize(); }
			    public void removeUpdate(DocumentEvent e) { updateSize(); }
			    public void changedUpdate(DocumentEvent e) { updateSize(); }
			});
			messageInput.setToolTipText("Will pause for 30 sec per sent message.");
			submitMessage = new JButton("Send Message");
			submitMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
			Dimension smDim = submitMessage.getPreferredSize();
			smDim.width = Constants.commentsWidth;
			Constants.setFixedSize(submitMessage,smDim);
			InputMap inputMap = messageInput.getInputMap(JComponent.WHEN_FOCUSED);
			ActionMap actionMap = messageInput.getActionMap();
			inputMap.put(KeyStroke.getKeyStroke("ctrl ENTER"), "sendMessage");
			actionMap.put("sendMessage", new AbstractAction() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			    	sendAnnotation();
			    }
			});
			submitMessage.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					sendAnnotation();
				}
			});
			sendMsgPanel = new JPanel(new BorderLayout());
			sendMsgPanel.add(messageInput,BorderLayout.CENTER);
			sendMsgPanel.add(submitMessage,BorderLayout.SOUTH);
			add(sendMsgPanel,BorderLayout.SOUTH);
			//repaint();
			//revalidate();
		}
		update();
	}
	private void openAnnotation(int index)
	{
		String annoTitle = ((topic.getTitle().length()>10) ? topic.getTitle().substring(0,10)+"...":topic.getTitle())+" Annotation #"+(index+1);
		boolean found  = false;
		Window[] windows = Window.getWindows();
		for (Window window : windows)
		{
            if (window instanceof JFrame && window.isVisible())
            {
            	JFrame topic = (JFrame) window;
                if (topic.getTitle().equals(annoTitle))
                {
                	found = true;
                	topic.toFront();
                	break;
                }
            }
        }
		if (found)
			return;
		String[] annotation = annotations.get(index);
		JFrame annoFrame = new JFrame(annoTitle);
		annoFrame.setIconImage(ResourceLoader.loadIcon("img/logo.png"));
		annoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		annoFrame.setLocationRelativeTo(null);
		JPanel mainPanel = new JPanel();
		int padding = 5; 
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		if (isAuthor)
		{
	        JLabel fromLabel = new JLabel("From: " + annotation[0]);
	        fromLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
	        mainPanel.add(fromLabel, BorderLayout.NORTH);
	        // Message body
	        JTextArea messageArea = new JTextArea(annotation[3].replaceAll(Constants.NLSeparator,"\n")){
				@Override
			    public Insets getInsets() {
			        return new Insets(padding, padding, padding, padding); // top, left, bottom, right
			    }
			};
			messageArea.setEditable(false);
	        messageArea.setLineWrap(true);
	        messageArea.setWrapStyleWord(true);
	        messageArea.setRows(10);
		    messageArea.setColumns(30);
	        mainPanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);
	        JPanel bottomPanel = new JPanel(new BorderLayout());
	        JLabel timeLabel = new JLabel("Received: " + annotation[4]);
	        bottomPanel.add(timeLabel, BorderLayout.WEST);
	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	        JButton deleteButton = new JButton("Ignore");
	        deleteButton.addActionListener((ActionEvent e) -> {
	        	annoFrame.dispose();
	        	deleteAnnotation(annotation[5]);
	        });
	        buttonPanel.add(deleteButton);
	        bottomPanel.add(buttonPanel, BorderLayout.EAST);
	        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		}
		else //client vv
		{
			JTextArea messageArea = new JTextArea(annotation[0].replaceAll(Constants.NLSeparator,"\n"))
			{
				@Override
			    public Insets getInsets() {
			        return new Insets(padding, padding, padding, padding); // top, left, bottom, right
			    }
			};
			messageArea.setFont(new Font("Serif", Font.PLAIN, 14));
		    messageArea.setEditable(false);
		    messageArea.setLineWrap(true);
		    messageArea.setWrapStyleWord(true);
		    messageArea.setRows(10);
		    messageArea.setColumns(30);
		    mainPanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);
		    JPanel infoPanel = new JPanel();
		    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		    TitledBorder infoPanelBorder = BorderFactory.createTitledBorder("Sent");
		    infoPanelBorder.setTitleColor(Constants.textColorDM);
		    infoPanel.setBorder(infoPanelBorder);
		    JLabel timeLabel = new JLabel(annotation[1]);
		    timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		    infoPanel.add(timeLabel);
		    infoPanel.add(Box.createVerticalStrut(5));
		    //infoPanel.add(readLabel);
		    mainPanel.add(infoPanel, BorderLayout.NORTH);
		    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		    JButton deleteButton = new JButton("Delete");
		    deleteButton.addActionListener(e -> {
		        annoFrame.dispose();
		        deleteAnnotation(annotation[2]);
		    });
		    buttonPanel.add(deleteButton);
		    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		}
		annoFrame.setContentPane(mainPanel);
		annoFrame.pack();
        annoFrame.setVisible(true);
	}
	private void deleteAnnotation(String messageID)
	{
		String dbPath = Constants.annoPath + File.separator + topicID + ".db";
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath)) {
	        String sql = "DELETE FROM annotations WHERE messageID = ?";
	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, messageID);
	            pstmt.executeUpdate();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		update();
	}
	private void sendAnnotation()
	{
		String text = messageInput.getText();
		if (text.trim().length()<1)
			return;
		String message = Constants.filterMessage(text);
		LocalDateTime now = LocalDateTime.now();
		String timestamp = now.toString();
		long timestampSeconds = now.atZone(ZoneId.systemDefault()).toEpochSecond();
		if (timestampSeconds<(lastSent+30))
			return;
		lastSent = timestampSeconds;
		String messageID = "";
		boolean found; 
		do 
		{
			found = false; 
			messageID = Constants.randID(4); 
			for (String[] annotation : annotations)
			{
				if (annotation[2].equals(messageID))
				{
					found = true;
					break;
				}
			}
		}
		while(found);
		String dbPath = Constants.annoPath + File.separator + topicID + ".db";
		try 
		{
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
			Statement stmt = conn.createStatement();
		    //stmt.executeUpdate("DELETE FROM annotations");
	        stmt.execute("CREATE TABLE IF NOT EXISTS annotations (id INTEGER PRIMARY KEY, message TEXT, timestamp TEXT, messageID TEXT)");
	        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO annotations (message, timestamp, messageID) VALUES (?, ?, ?)");
	        pstmt.setString(1,message);
	        pstmt.setString(2,timestamp);
	        pstmt.setString(3,messageID);
	        pstmt.executeUpdate();
	        pstmt.close();
	        stmt.close();
	        conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		String[] annotation = new String[] {Constants.accInfo[0],Constants.accInfo[1],Constants.accInfo[2],message,timestamp};
		Constants.ns[0].sendAnnotation(Constants.grabNode(topic.getAuthCID()),topicID,annotation);
		messageInput.setText("");
		scrollPane.repaint();
		scrollPane.revalidate();
		update();
	}
	public void update()
	{
		annotations.clear();
		removeAll();
		display.removeAll();
		if (isAuthor)
		{
			try
			{
		    	File db = new File(dbPath);
		    	if (db.exists() & db.length() > 1)
		    	{	
			    	Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
			    	String sql = "SELECT username, cid, uid, message, timestamp, messageID FROM annotations ORDER BY id ASC";
			    	PreparedStatement stmt = conn.prepareStatement(sql);
			    	ResultSet rs = stmt.executeQuery();
			        while (rs.next()) {
			            annotations.add(new String[] {rs.getString("username"),rs.getString("cid"),rs.getString("uid"),rs.getString("message"),rs.getString("timestamp"),rs.getString("messageID")});
			        }
			        conn.close();
		    	}
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			for (int i=0;i<annotations.size();i++)
			{
				final int index = i;
				String[] annotation = annotations.get(index);
				String from = annotation[0];
				String preview = annotation[3].replaceAll(Constants.NLSeparator," ");
				preview = ((preview.length()>10)? preview.substring(0,10):preview);
				JButton tmpButt = new JButton("<html>From: "+from+"<br>"+preview+"</html>");
				tmpButt.setToolTipText(annotation[4]);
				tmpButt.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						openAnnotation(index);
					}
				});
				display.add(tmpButt);
			}
			if (annotations.size() == 0)
			{
				JLabel nLabel = new JLabel("<html><center>None Yet!</center></html>");
				nLabel.setHorizontalAlignment(SwingConstants.CENTER);
				nLabel.setVerticalAlignment(SwingConstants.CENTER);
				nLabel.setBackground(Constants.mainFG);
				nLabel.setForeground(Constants.textColorDM);
				nLabel.setOpaque(true); // Make the background transparent by default
				nLabel.setFont(Constants.descFont); // Set font to default label font
				nLabel.setBorder(BorderFactory.createEmptyBorder()); // Remove the default JTextArea border
				nLabel.setFocusable(false);
				add(nLabel,BorderLayout.CENTER);
				//Constants.setFixedSize(nLabel,disDim);
			}
			else
				add(jp,BorderLayout.CENTER);
		}
		else
		{
			try 
		    {
		    	File db = new File(dbPath);
		    	if (db.exists() & db.length() > 1)
		    	{
			    	Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
			    	String sql = "SELECT message, timestamp, messageID FROM annotations ORDER BY id ASC";
			    	PreparedStatement stmt = conn.prepareStatement(sql);
			    	ResultSet rs = stmt.executeQuery();
			        while (rs.next()) {
			            annotations.add(new String[] {rs.getString("message"),rs.getString("timestamp"),rs.getString("messageID")});
			        }
			        conn.close();
		    	}
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
			for (int i=0;i<annotations.size();i++)
			{
				final int index = i;
				String[] annotation = annotations.get(index);
				String preview = annotation[0].replaceAll(Constants.NLSeparator," ");
				preview = ((preview.length()>10)? preview.substring(0,10):preview);
				JButton tmpButt = new JButton(preview);
				tmpButt.setToolTipText(annotation[1]);
				tmpButt.setMaximumSize(new Dimension(Integer.MAX_VALUE, tmpButt.getPreferredSize().height));
				tmpButt.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						openAnnotation(index);
					}
				});
				display.add(tmpButt);
			}
			Dimension disDim = new Dimension(Constants.commentsWidth,(int) (d.getHeight() - messageInput.getHeight() - submitMessage.getHeight()));
			display.setSize(disDim);
			Constants.setFixedSize(jp,disDim);
			if (annotations.size() == 0)
			{
				JLabel nLabel = new JLabel("<html><center>None Yet!<br>Try sending one.</center></html>");
				nLabel.setHorizontalAlignment(SwingConstants.CENTER);
				nLabel.setVerticalAlignment(SwingConstants.CENTER);
				nLabel.setBackground(Constants.mainFG);
				nLabel.setForeground(Constants.textColorDM);
				nLabel.setOpaque(true); // Make the background transparent by default
				nLabel.setFont(Constants.descFont); // Set font to default label font
				nLabel.setBorder(BorderFactory.createEmptyBorder()); // Remove the default JTextArea border
				nLabel.setFocusable(false);
				add(nLabel,BorderLayout.CENTER);
			}
			else
				add(jp,BorderLayout.CENTER);
			add(sendMsgPanel,BorderLayout.SOUTH);
		}
		SwingUtilities.updateComponentTreeUI(topic);
		repaint();
		revalidate();
	}
	public void sendable(boolean canSend)
	{
		if (canSend) {
	        messageInput.setEditable(true);
	        messageInput.setBackground(Color.WHITE);  // or whatever your normal color is
	        submitMessage.setEnabled(true);
	    } else {
	        messageInput.setEditable(false);
	        messageInput.setBackground(new Color(230, 230, 230));  // light gray
	        submitMessage.setEnabled(false);
	    }
	}
	
}
