import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLDocument;

import org.cef.CefSettings;

import com.jogamp.opengl.GLProfile;

public class Redlines
{
	private static NetListener netListener;
	private static NetSender netSender;
	private String OS = null;
	//private static String MLFKFile = "masterList.li";
	private static String IP,extIP;
	private static String MAC,CID,UID;
	//private static String keyFile = "rdlns.bin";
	private static BufferedReader fileReader;
	private static JComboBox<String> selectNodes;
	private static JComboBox<String> selectActions;
	private static File topicDir;
	private static File annoDir;
	private static int curHelpPage = 0;
	private static JPanel helpPagePanel;
	public static void main(String[] args)
	{
		try {
    	    UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
    	    ThemeManager.applyDarkTheme();
    	} catch (Exception ex) {
    	    ex.printStackTrace();
    	}
		new VLCInvoker();
		JCEFInvoker jcef = new JCEFInvoker();
		Constants.browserApp[0] = jcef.getCefApp();
		GLProfile.initSingleton();
		System.out.println("RedLines: No Thread Left Behind");
		ToolTipManager.sharedInstance().setInitialDelay(0);
		try {
			MAC = NetworkUtils.getActiveMacAddress();
			IP = Constants.getInternalIPAddress().orElse("NULL");
			if (IP.equals("NULL"))
			{
				System.out.println("Unable to retrieve Internal IP address!");
				return;
			}
			extIP = Constants.getPublicIPAddress().orElse("NULL");
			if (extIP.equals("NULL"))
			{
				System.out.println("Unable to retrieve Public IP address!");
				return;
			}
			File sysFolder = new File(Constants.sysFolder);
			if (!sysFolder.exists())
				sysFolder.mkdir();
			Constants.loadSettings();
			if (Constants.toggleOptions.get("Debug Output"))
				Constants.df[0] = new DebugFrame();
			File bootInfoFile = new File(Constants.bootInfo);
	        if (!bootInfoFile.exists())
	        {
	        	bootInfoFile.createNewFile();
	        	CID = EncryptionDecryption.encrypt(Constants.defaultPort+Constants.CIDSeparator+((Constants.toggleOptions.get("Go Public")) ? extIP:IP),Constants.key);
	        	UID = Constants.generateHash(MAC+Constants.separator+CID);
	        	BufferedWriter bw = new BufferedWriter(new FileWriter(bootInfoFile));
	        	bw.write(EncryptionDecryption.encrypt(UID,Constants.key)+"\n");
	        	bw.write(CID);
	        	bw.close();
	        }
	        else
	        {
	        	BufferedReader br = new BufferedReader(new FileReader(bootInfoFile));
	        	UID = EncryptionDecryption.decrypt(br.readLine().trim(),Constants.key);
	        	CID = br.readLine().trim();
	        	//CID = EncryptionDecryption.encrypt(Constants.defaultPort+Constants.CIDSeparator+((Constants.toggleOptions.get("Go Public")) ? extIP:IP),Constants.key);
	        	br.close();
	        }
	        String[] myNode = EncryptionDecryption.decrypt(CID,Constants.key).split(Constants.CIDSeparator);
	        myNode[1] = ((Constants.toggleOptions.get("Go Public")) ? extIP:IP);
	        CID = EncryptionDecryption.encrypt(myNode[0]+Constants.CIDSeparator+myNode[1],Constants.key);
	        Constants.myNode[0] = myNode[0]; //port
			Constants.myNode[1] = myNode[1]; //IP
			Constants.ipInfo[0] = IP;
			Constants.ipInfo[1] = extIP;
	        Constants.accInfo[2] = UID;
	        Constants.accInfo[1] = CID;
	        netSender = new NetSender();
			Constants.ns[0] = netSender;
			netListener = new NetListener();
			Constants.nl[0] = netListener;
			//gather nodelist
			File mlFile = new File(Constants.masterList);
			if (mlFile.exists() && mlFile.length() > 1)
			{
				fileReader = new BufferedReader(new FileReader(Constants.masterList));
				String mlEnc = fileReader.readLine();
				fileReader.close();
				String[] mlEncArr = EncryptionDecryption.decrypt(mlEnc,Constants.key).split(Constants.separator);
				for (String mNode : mlEncArr)
				{
					if (mNode == null || mNode.equals("null"))
						continue;
					String[] mNodeArr = mNode.split(Constants.CIDSeparator);
					Constants.addToNodeList(new Node(Integer.parseInt(mNodeArr[0]),mNodeArr[1]),true);
				}
			}
			Constants.saveNodeList();
			topicDir = new File(Constants.topicPath);
	        if (!topicDir.exists())
	        {
	            if (!topicDir.mkdir())
	            {
	                System.out.println("Failed to create '"+topicDir+"' directory.");
	                return;
	            }
	            else
	            {
	            	new File(Constants.topicPath+File.separator+"topic.ind").createNewFile();
	            	new File(Constants.topicPath+File.separator+".hl").createNewFile();
	            }
	        }
	        File hlFile = new File(Constants.topicPath+File.separator+".hl");
	        if (!hlFile.exists())
	        	new File(Constants.topicPath+File.separator+".hl").createNewFile();
	        annoDir = new File(Constants.annoPath);
	        if (!annoDir.exists())
	        {
	            if (!annoDir.mkdir())
	            {
	                System.out.println("Failed to create '"+annoDir+"' directory.");
	                return;
	            }
	        }
			startRLMain();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	public static void startRLMain()
	{
		/*Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		Constants.rlFrameSize[0] = (int) screenSize.getWidth()/4;
		Constants.rlFrameSize[1] = (int) screenSize.getHeight()/2;
		System.out.println(Arrays.toString(Constants.rlFrameSize));*/
		JFrame rlFrame = new JFrame("RedLines");
		rlFrame.setIconImage(ResourceLoader.loadIcon("img/logo.png"));
		rlFrame.setSize(Constants.rlFrameSize[0],Constants.rlFrameSize[1]);
		JPanel mainMainPanel = new JPanel();
		mainMainPanel.setOpaque(true);
		mainMainPanel.setBackground(Constants.mainBG);
		mainMainPanel.setLayout(new BoxLayout(mainMainPanel,BoxLayout.Y_AXIS));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(Box.createVerticalStrut(10));
		JPanel headPanel = new JPanel(new BorderLayout());
		headPanel.setOpaque(false);
		Constants.setFixedSize(headPanel,new Dimension(rlFrame.getWidth(),Constants.imgSize));
		IButton donateButt = new IButton("donate.png","Donate",true);
		donateButt.setOpaque(false);
		donateButt.setBorder(null);
		donateButt.setContentAreaFilled(false);
		Constants.setFixedSize(donateButt,new Dimension(Constants.imgSize,Constants.imgSize));
		donateButt.addActionListener(e -> {
				donateButt.setDark(true);
				String title = "Donate to $inkzxc0421";
				boolean found  = false;
				Window[] windows = Window.getWindows();
				for (Window window : windows)
				{
		            if (window instanceof JFrame && window.isVisible())
		            {
		            	JFrame sourceFrame = (JFrame) window;
		                if (sourceFrame.getTitle().equals(title))
		                {
		                	found = true;
		                	sourceFrame.toFront();
		                	break;
		                }
		            }
		        }
				if (found)
					return;
				JFrame donateFrame = new JFrame(title);
				donateFrame.setIconImage(ResourceLoader.loadIcon("img/logo.png"));
				ImagePanel CAPanel = new ImagePanel("cashapp.png");
				CAPanel.setLayout(new BorderLayout());
				Constants.setFixedSize(CAPanel,new Dimension(300,300));
				donateFrame.add(CAPanel);
				donateFrame.pack();
				donateFrame.setResizable(false);
				donateFrame.setVisible(true);
		});
		headPanel.add(donateButt,BorderLayout.WEST);
		JLabel title = new JLabel("Redlines");
		title.setFont(Constants.titleFont);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		headPanel.add(title,BorderLayout.CENTER);
		IButton settingsButt = new IButton("settings.png","Settings",true);
		settingsButt.setOpaque(false);
		settingsButt.setBorder(null);
		settingsButt.setContentAreaFilled(false);
		Constants.setFixedSize(settingsButt,new Dimension(Constants.imgSize,Constants.imgSize));
		settingsButt.addActionListener(e -> {
				Constants.openSettings();
		});
		headPanel.add(settingsButt,BorderLayout.EAST);
		JLabel headLine = new JLabel("No Thread Left Behind");
		headLine.setFont(Constants.titleFont.deriveFont(Font.ITALIC,(Constants.titleFont.getSize2D() - 17f)));
		headLine.setHorizontalAlignment(SwingConstants.CENTER);
		headLine.setAlignmentX(Component.CENTER_ALIGNMENT);
		headPanel.add(headLine,BorderLayout.SOUTH);
		mainPanel.add(headPanel);
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        separator.setPreferredSize(new Dimension((int)(Constants.rlFrameSize[0]*0.75), 1)); // ~75% of 400px frame
        separator.setMaximumSize(new Dimension((int)(Constants.rlFrameSize[0]*0.75), 1)); // lock width
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(separator);
        mainPanel.add(Box.createVerticalStrut(10));
        PlaceholderTextField searchField = new PlaceholderTextField("Search Your Nodes for Topics...");
		searchField.setFont(Constants.descFont);
		searchField.setBackground(Constants.mainFG);
		searchField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) 
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if (Constants.nodeList.size() == 0)
					{
						JOptionPane.showMessageDialog(null, "Please Add a Node in the Settings Connection tab!");
						return;
					}
					new SwingWorker<String[], Void>() {
						@Override
						protected String[] doInBackground() {
							return Constants.searchNodes(searchField.getText().split(" "), rlFrame);
						}

						@Override
						protected void done() {
							try {
								String[] returnedTopics = get();
								if (returnedTopics != null)
									showNodeSearchResults(returnedTopics); // works with null too
								else
									Constants.prompt(null, "Topic Not Found!", "'"+searchField.getText()+"' Not Found Within Your Nodes!");
							} catch (Exception ex) {
								ex.printStackTrace();
								showNodeSearchResults(null);
							}
						}
					}.execute();
					/*String[] returnedTopics = Constants.searchNodes(searchField.getText().split(" "),rlFrame);
					if (returnedTopics != null)
						showNodeSearchResults(returnedTopics);*/
				}
			}
			@Override
			public void keyReleased(KeyEvent e) 
			{
				// TODO Auto-generated method stub
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}
		});
		FontMetrics fm = searchField.getFontMetrics(Constants.descFont);
		Constants.setFixedSize(searchField,new Dimension(rlFrame.getWidth(),fm.getHeight()+6));
		mainPanel.add(searchField);
		JPanel sourcePanel = new JPanel(new GridLayout(1,2));
		Constants.setFixedSize(sourcePanel,new Dimension(rlFrame.getWidth(),fm.getHeight()*4));
		Font startSFont = new Font(Constants.fontName, Font.BOLD, 15);
		JButton pButton = new JButton("<html><center>Start Topic with a Primary Source</center></html>");
		pButton.setBackground(Constants.buttonColor);
		pButton.setFont(startSFont);
		pButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Start Primary Source");
				new AddSource(true);
			}
		});
		sourcePanel.add(pButton);
		JButton sButton = new JButton("<html><center>Start Topic with a Secondary Source</center></html>");
		sButton.setBackground(Constants.buttonColor);
		sButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Start Secondary Source");
				new AddSource(false);
			}
		});
		sButton.setFont(startSFont);
		sourcePanel.add(sButton);
		mainPanel.add(sourcePanel);
		mainPanel.setOpaque(false);
		mainMainPanel.add(mainPanel);
		JPanel topicsPanel = new JPanel();
		topicsPanel.setLayout(new BoxLayout(topicsPanel,BoxLayout.Y_AXIS));
		topicsPanel.setBorder(new EmptyBorder(5,5,5,5));
		JLabel savedTopicsLabel = new JLabel("Saved Topics");
		savedTopicsLabel.setFont(Constants.descFont.deriveFont(Font.BOLD));
		savedTopicsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		savedTopicsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		topicsPanel.add(savedTopicsLabel);
		topicsPanel.add(Box.createVerticalStrut(10));
		JScrollPane tlScroll = new JScrollPane(Constants.tlPanel);
		tlScroll.setOpaque(false);
		tlScroll.getViewport().setOpaque(false);
		Constants.tlPanel.setOpaque(true);
		Constants.tlPanel.setBackground(new Color(0,0,0,25));
		topicsPanel.add(tlScroll);
		topicsPanel.setOpaque(false);
		mainMainPanel.add(topicsPanel);
		JPanel aboutHelpPanel = new JPanel(new BorderLayout());
		aboutHelpPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Constants.imgSize));
		aboutHelpPanel.setPreferredSize(new Dimension(0, Constants.imgSize));
		IButton aboutButton = new IButton("about.png","About",true);
		aboutButton.setOpaque(false);
		aboutButton.setBorder(null);
		aboutButton.setContentAreaFilled(false);
		Constants.setFixedSize(aboutButton,new Dimension(Constants.imgSize,Constants.imgSize));
		aboutButton.addActionListener(e -> {
			showAbout();
		});
		aboutHelpPanel.add(aboutButton,BorderLayout.WEST);
		IButton helpButton = new IButton("help.png","Help",true);
		helpButton.setOpaque(false);
		helpButton.setBorder(null);
		helpButton.setContentAreaFilled(false);
		Constants.setFixedSize(helpButton,new Dimension(Constants.imgSize,Constants.imgSize));
		helpButton.addActionListener(e -> {
			showHelp();
		});
		aboutHelpPanel.add(helpButton,BorderLayout.EAST);
		mainMainPanel.add(aboutHelpPanel);
		rlFrame.setContentPane(mainMainPanel);
		rlFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            	Constants.shutdown();
            }
        });
		rlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rlFrame.setResizable(false);
		if (!Constants.myNode[1].equals((Constants.toggleOptions.get("Go Public")) ? extIP:IP))
			if (Constants.prompt(rlFrame,"IP Adress Change!","Update Node and CID?"))
				Constants.updateCID(true);
		Constants.login(CID,rlFrame); 
		Constants.refreshTLPanel();
		//Constants.dumpComponentTree(rlFrame,0);
		//Constants.setDarkMode(rlFrame,true);
	}
	public static void showNodeSearchResults(String[] returnedTopics)
	{
		JFrame resultsFrame = new JFrame("Results");
		resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		resultsFrame.setIconImage(ResourceLoader.loadIcon("img/logo.png"));
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setPreferredSize(new Dimension(Constants.popupSize[0],Constants.popupSize[1]));
		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new BoxLayout(resultsPanel,BoxLayout.Y_AXIS));
		for (String returnedTopic : returnedTopics)
		{
			String[] topicArr = returnedTopic.split(Constants.separator);
			JButton resultButt = new JButton(topicArr[3]);
			resultButt.setAlignmentX(Component.CENTER_ALIGNMENT);
			resultButt.setFont(Constants.descFont.deriveFont(Constants.descFont.getSize2D() + 2f));
			resultButt.setMaximumSize(new Dimension(Integer.MAX_VALUE, resultButt.getPreferredSize().height));
			resultButt.setToolTipText("By "+topicArr[4]);
			resultButt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectTopic(topicArr[0],topicArr[1],resultsFrame);
				}
			});
			resultsPanel.add(resultButt);
		}
		JScrollPane resultsScroller = new JScrollPane(resultsPanel); 
		mainPanel.add(resultsScroller,BorderLayout.CENTER);
		resultsFrame.add(mainPanel);
		resultsFrame.setResizable(false);
		resultsFrame.pack();
		resultsFrame.setVisible(true);
	}
	public static void selectTopic(String topicID, String topicAuthorCID, JFrame resultsFrame)
	{
		try {
			Node authNode = Constants.grabNode(topicAuthorCID);
			String topicInfo = netSender.reqFromAuth(authNode,topicID);
			if (topicInfo != null) //author available
			{
				String[] authResp = topicInfo.split(Constants.separator);
				String hash = authResp[0];
				long filesize = Long.parseLong(authResp[1]);
				if (!Constants.prompt(null,"Requested Topic Size","The topic requested is "+Constants.getHRFileSize(filesize)+". Download?"))
					return;
				ArrayList<Node> availNodes = new ArrayList<Node>();
				for (Map.Entry<String, Node> nodeMap : Constants.nodeList.entrySet())
				{
					Node node = nodeMap.getValue();
					String nodeHash = netSender.checkTopicHash(node,topicID);
					if (nodeHash != null && nodeHash.equals(hash))
						availNodes.add(node);
				}
				System.out.println("lets start the torrent");
				netSender.startTorrent(availNodes,topicID,filesize);
			}
			else
			{
				ArrayList<String> presentTopicInfos = new ArrayList<String>();
				for (Node node : Constants.presentNodes)
				{
					String topicCliInfo = netSender.reqFromAuth(node,topicID);
					if (topicCliInfo != null)
						presentTopicInfos.add(topicCliInfo+Constants.NLSeparator+node.getCID());
				}
				HashMap<String, Integer> countMap = new HashMap<>();
				String mostFrequent = null;
				int maxCount = 0;
				for (String info : presentTopicInfos) {
		            String[] parts = info.split(Constants.separator); // Use regex-safe split
		            String key = parts[0]; // Extract text before separator
		            countMap.put(key, countMap.getOrDefault(key, 0) + 1);
		            if (countMap.get(key) > maxCount) {
		                maxCount = countMap.get(key);
		                mostFrequent = key;
		            }
		            
		        }
				ArrayList<String> mostFrequentEntries = new ArrayList<>();
		        for (String info : presentTopicInfos) {
		            if (info.startsWith(mostFrequent + Constants.separator)) {
		                mostFrequentEntries.add(info);
		            }
		        }
		        long filesize = Long.parseLong(mostFrequentEntries.get(0).split(Constants.NLSeparator)[0].split(Constants.separator)[1]);
		        ArrayList<Node> availNodes = new ArrayList<Node>();
		        for (String hashNode : mostFrequentEntries)
		        	availNodes.add(Constants.grabNode(hashNode.split(Constants.NLSeparator)[1]));
		        netSender.startTorrent(availNodes,topicID,filesize);
			}
			//
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resultsFrame.dispose();
	}
	public static void showHelp()
	{
		String name = "Redlines Help";
		boolean found = false;
		Window[] windows = Window.getWindows();
		for (Window window : windows)
		{
            if (window instanceof JFrame && window.isVisible())
            {
                if (window.getName().equals(name))
                {
                	found = true;
                	window.toFront();
                	return;
                }
            }
        }
		if (!found)
		{
			JFrame rlHelp = new JFrame(name);
			rlHelp.setIconImage(ResourceLoader.loadIcon("img/logo.png"));
			rlHelp.setName(name);
			JPanel mainPanel = new JPanel(new BorderLayout());
			PlaceholderTextField searchHelpField = new PlaceholderTextField("Search for Help Keyword");
			searchHelpField.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
					{
						String searchQStr = searchHelpField.getText();
						if (searchQStr==null)
							return;
						searchQStr = searchQStr.trim();
						searchHelp(searchQStr);
					}
				}
				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			//mainPanel.add(searchHelpField,BorderLayout.NORTH); //maybe next time
			helpPagePanel = new JPanel(new CardLayout());
			for (String helpPage : Constants.helpPages)
            {
				String helpPageHTML = helpPage+".html";
				JPanel tmpHelpPage = new JPanel(new GridLayout(1,2));
				tmpHelpPage.setName(helpPage);
				Image helpImg = ResourceLoader.loadIcon("img/help/"+helpPage+".png");
				ImagePanel IP = new ImagePanel();
				IP.setImage(helpImg);
				tmpHelpPage.add(IP);
				JTextPane helpPane = new JTextPane();
				helpPane.setContentType("text/html");
				helpPane.setEditable(false);
				helpPane.setOpaque(false);
				HTMLDocument doc = (HTMLDocument)helpPane.getStyledDocument();
				doc.setBase(Redlines.class.getResource("html/help"));
				String helpHtmlPath = "html/help/"+helpPageHTML;
				InputStream htmlIS = Redlines.class.getResourceAsStream(helpHtmlPath);
				if (htmlIS != null) {
					String html = new BufferedReader(new InputStreamReader(htmlIS))
						.lines()
						.collect(Collectors.joining("\n"));
					helpPane.setText(html);
				}
				JScrollPane scrollPane = new JScrollPane(helpPane);
				scrollPane.setBorder(BorderFactory.createEmptyBorder());
				scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
				tmpHelpPage.add(scrollPane);
				helpPagePanel.add(tmpHelpPage,helpPage);
            }
			((CardLayout)helpPagePanel.getLayout()).show(helpPagePanel, Constants.helpPages[curHelpPage]);
			mainPanel.add(helpPagePanel,BorderLayout.CENTER);
			JPanel nextPrevPanel = new JPanel(new GridLayout(1,2));
			JButton prevButton = new JButton("<");
			prevButton.addActionListener(e -> {
				if (curHelpPage != 0)
				{
					curHelpPage--;
					((CardLayout)helpPagePanel.getLayout()).show(helpPagePanel, Constants.helpPages[curHelpPage]);
				}
			});
			prevButton.setFont(Constants.titleFont);
			nextPrevPanel.add(prevButton);
			JButton nextButton = new JButton(">");
			nextButton.addActionListener(e -> {
				if (curHelpPage != Constants.helpPages.length-1)
				{
					curHelpPage++;
					((CardLayout)helpPagePanel.getLayout()).show(helpPagePanel, Constants.helpPages[curHelpPage]);
				}
			});
			nextButton.setFont(Constants.titleFont);
			nextPrevPanel.add(nextButton);
			mainPanel.add(nextPrevPanel,BorderLayout.SOUTH);
			JRootPane rootPane = rlHelp.getRootPane();
	        Action leftArrowAction = new AbstractAction() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	if (curHelpPage != 0)
					{
						curHelpPage--;
						((CardLayout)helpPagePanel.getLayout()).show(helpPagePanel, Constants.helpPages[curHelpPage]);
					}
	            }
	        };
	        Action rightArrowAction = new AbstractAction() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	if (curHelpPage != Constants.helpPages.length-1)
					{
						curHelpPage++;
						((CardLayout)helpPagePanel.getLayout()).show(helpPagePanel, Constants.helpPages[curHelpPage]);
					}
	            }
	        };
	        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	        ActionMap actionMap = rootPane.getActionMap();

	        // For Left Arrow
	        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "leftArrowKey"); // "leftArrowKey" is a unique identifier string
	        actionMap.put("leftArrowKey", leftArrowAction);

	        // For Right Arrow
	        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "rightArrowKey"); // "rightArrowKey" is another unique identifier string
	        actionMap.put("rightArrowKey", rightArrowAction);
			rlHelp.add(mainPanel);
			//rlHelp.pack();
			rlHelp.setSize(900,625);
			rlHelp.setResizable(false);
			rlHelp.setVisible(true);
		}
	}
	private static void searchHelp(String searchQStr) {
	}
	public static void showAbout()
	{
		String name = "About Redlines";
		boolean found = false;
		Window[] windows = Window.getWindows();
		for (Window window : windows)
		{
            if (window instanceof JFrame && window.isVisible())
            {
                if (window.getName().equals(name))
                {
                	found = true;
                	window.toFront();
                	return;
                }
            }
        }
		if (!found)
		{
			JFrame rlAbout = new JFrame(name);
			rlAbout.setIconImage(ResourceLoader.loadIcon("img/logo.png"));
			rlAbout.setName(name);
			JPanel mainPanel = new JPanel(new BorderLayout());
			mainPanel.setBackground(Color.DARK_GRAY);
			JTextPane aboutTextPane = new JTextPane();
	        aboutTextPane.setContentType("text/html");
	        aboutTextPane.setEditable(false);
	        aboutTextPane.setOpaque(false);
	        String aboutHtmlPath = "html/about.html";
	        InputStream htmlIS = Redlines.class.getResourceAsStream(aboutHtmlPath);
	        if (htmlIS != null) {
	            String html = new BufferedReader(new InputStreamReader(htmlIS))
	                .lines()
	                .collect(Collectors.joining("\n"));
	            aboutTextPane.setText(html);
	        }
	        JScrollPane scrollPane = new JScrollPane(aboutTextPane);
	        scrollPane.setBorder(BorderFactory.createEmptyBorder());
	        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
	        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
	        mainPanel.add(scrollPane, BorderLayout.CENTER);
	        rlAbout.add(mainPanel);
			rlAbout.pack();
			rlAbout.setSize(670, 708);
			rlAbout.setResizable(false);
			rlAbout.setVisible(true);
		}
	}
	public static void pingAll()
	{
		for (Map.Entry<String, Node> nodeMap : Constants.nodeList.entrySet())
		{
			Node node = nodeMap.getValue();
			try {
				netSender.sendPing(node);
			} catch (Exception ex) {
				System.out.println(node.getIP()+" connection timed out!");
			}
		}
	}
}
