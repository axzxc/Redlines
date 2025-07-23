import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.util.FontPropertiesManager;

public class Topic extends JFrame {

	private File mainDir;
	private CardLayout contentCL = new CardLayout();
	private JPanel cardContentPanel,curCard;
	private String topicID,title,desc;
	private Topic self;
	private final boolean startTopic;
	private ArrayList<Source> sources = new ArrayList<Source>();
	public ArrayList<String> related = new ArrayList<String>();
	private JPanel descPanel = new JPanel(new BorderLayout());
	private JPanel typePanel;
	private JLabel descTitle = new JLabel("Description:");
	private RoundedIButt authorStatusButt;
	private JTextArea descriptionJTA = new JTextArea(desc);
	private String CID;
	private String authorCID;
	private String authorUID;
	private Node authNode;
	private String authUser;
	private String hash;
	private boolean isAuthor;
	private boolean isFull = false;
	private long curSize;
	private boolean editingTopic = false;
	private boolean infoShown = false;
	private boolean isPublic;
	private boolean published = true;
	private RoundedIButt publishButt;
	private AnnotateBox annoBox;
	private ArrayList<String[]> annotations = new ArrayList<String[]>();
	private Thread browserThread = null;
	private String curRelType = "Other";
	private JComboBox<String> relationDropdown;
	public Topic(File topicNFO,boolean startTopic) throws HeadlessException {
		this.self = this;
		this.startTopic = startTopic;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(topicNFO.getPath()));
        	topicID = br.readLine().trim();
        	authorCID = br.readLine().trim();
			authorUID = br.readLine().trim();
			title = br.readLine().trim();
			authUser = br.readLine().trim();
			isPublic = br.readLine().trim().equals("1");
			desc = br.readLine().trim().replaceAll(Constants.NLSeparator,"\n");
        	String line;
        	br.close();
        	File relatedFile = new File(Constants.topicPath+File.separator+topicID+File.separator+"related.nfo");
        	if (relatedFile.exists())
        	{
	        	br = new BufferedReader(new FileReader(relatedFile));
	        	line = "";
	        	while ((line = br.readLine()) != null)
	        	{
	        		if (!line.contains(Constants.separator))
	        			continue;
	        		related.add(line);
	        	}
	        	br.close();
        	}
        	else
        		relatedFile.createNewFile();
    		ToolTipManager.sharedInstance().setInitialDelay(0);
            init();
        } catch (IOException e) {
            e.printStackTrace();
            dispose();
        }
	}
	public Topic(String topicID,boolean startTopic) throws HeadlessException {
		this.self = this;
		this.startTopic = startTopic;
		this.topicID = topicID;
		mainDir = new File(Constants.topicPath+"/"+topicID);
		try {
			BufferedReader br = new BufferedReader(new FileReader(mainDir.getPath()+"/topic.nfo"));
			topicID = br.readLine().trim();
			authorCID = br.readLine().trim();
			authorUID = br.readLine().trim();
			title = br.readLine().trim();
			authUser = br.readLine().trim();
			isPublic = br.readLine().trim().equals("1");
        	desc = br.readLine().trim().replaceAll(Constants.NLSeparator,"\n");
        	br.close();
        	String line;
        	File relatedFile = new File(Constants.topicPath+File.separator+topicID+File.separator+"related.nfo");
        	if (relatedFile.exists())
        	{
	        	br = new BufferedReader(new FileReader(relatedFile));
	        	line = "";
	        	while ((line = br.readLine()) != null)
	        	{
	        		if (!line.contains(Constants.separator))
	        			continue;
	        		related.add(line);
	        	}
	        	br.close();
        	}
        	else
        		relatedFile.createNewFile();
    		ToolTipManager.sharedInstance().setInitialDelay(0);
    		init();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	private void init()
	{
		setName(topicID);
		setIconImage(ResourceLoader.loadIcon("img/logo.png"));
		//FontPropertiesManager.getInstance().loadOrReadSystemFonts();
		this.CID = Constants.accInfo[1];
    	isAuthor = (authorUID.equals(Constants.accInfo[2]));
    	if (!isAuthor)
    	{
			try {
				authNode = Constants.grabNode(authorCID);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
    	}
		String[] sourceTypes = {"P","S"};
		for (String sType : sourceTypes)
		{
			File sourceDir = new File(Constants.topicPath+File.separator+topicID+File.separator+"sources"+File.separator+sType);
	        File[] files = sourceDir.listFiles();
	        if (files != null)
	        {
	            for (File file : files)
	            {
	            	String name = file.getName();
	            	if (Constants.getExtension(file).equals("lin"))
	            	{
	            		try
	            		{
	            			BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
	            			name = br.readLine().trim();
	            			br.close();
	            		}
	            		catch (IOException e)
	            		{
	            			e.printStackTrace();
	            		}
	            	}
	                if (file.isFile() && !file.getName().substring(file.getName().length() - 5).equals(".desc")) 
	                {
	                	String out = "";
	                	String line;
	                	try
	            		{
	            			BufferedReader br = new BufferedReader(new FileReader(file.getPath()+".desc"));
	            			while ((line = br.readLine()) != null)
	            				out+=line+"\n";
	            			br.close();
	            		}
	            		catch (IOException e)
	            		{
	            			e.printStackTrace();
	            		}
	                	sources.add(new Source(file,name,out)); //make uploading a .desc file as a souce ILLEGAL
	                }
	            }
	        }
		}
		setTitle(title);
		JPanel mainPanel = new JPanel();
		mainPanel.setName("mainPanel");
		mainPanel.setOpaque(true);
		mainPanel.setBackground(Constants.mainBG);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		Constants.setFixedSize(mainPanel,new Dimension(Constants.topicDim[0],Constants.topicDim[1]));
		JPanel headPanel = new JPanel(new BorderLayout());
		headPanel.setName("headPanel");
		Constants.setFixedSize(headPanel,new Dimension(Constants.topicDim[0], Constants.topicHeadHeight));
		JTextField titleLabel = new JTextField(title);
		titleLabel.setOpaque(false);
		titleLabel.setBackground(null);
		titleLabel.setEditable(false);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBorder(null);
		titleLabel.setFont(new Font(Constants.fontName, Font.BOLD, 25));
		headPanel.add(titleLabel,BorderLayout.CENTER);
		headPanel.setOpaque(false);
		mainPanel.add(headPanel);
		JPanel mContentPanel = new JPanel(new BorderLayout());
		mContentPanel.setName("mContentPanel");
		Constants.setFixedSize(mContentPanel,new Dimension(Constants.topicDim[0],(Constants.topicDim[1] - Constants.topicHeadHeight)));
		JPanel sContentPanel = new JPanel(new BorderLayout());
		sContentPanel.setName("sContentPanel");
		sContentPanel.setOpaque(false);
		Constants.setFixedSize(sContentPanel,new Dimension(Constants.topicDim[0] - Constants.commentsWidth,Constants.topicDim[1] - Constants.topicHeadHeight));
		cardContentPanel = new JPanel(contentCL);
		cardContentPanel.setOpaque(false);
		cardContentPanel.setName("cardContentPanel");
		Constants.setFixedSize(cardContentPanel,new Dimension(Constants.topicDim[0] - Constants.commentsWidth,Constants.topicDim[1] - Constants.topicHeadHeight - Constants.switchCardHeight));
		JPanel descHeadPanel = new JPanel(new BorderLayout());
		descHeadPanel.setName("descHeadPanel");
		descHeadPanel.setOpaque(false);
		Constants.setFixedSize(descHeadPanel,new Dimension((int)sContentPanel.getSize().getWidth(),(int)(sContentPanel.getSize().getHeight() - Constants.switchCardHeight)));
		//descHeadPanel.setBackground(Color.WHITE);
		JPanel contentHead = new JPanel(new BorderLayout());
		contentHead.setName("contentHead");
		contentHead.setOpaque(false);
		contentHead.setSize(Constants.topicDim[0] - Constants.commentsWidth,Constants.topicHeadHeight);
		Constants.setFixedSize(contentHead,new Dimension(Constants.topicDim[0] - Constants.commentsWidth,Constants.topicHeadHeight));
		JPanel CHButtons = new JPanel(new FlowLayout());
		CHButtons.setName("CHButtons");
		CHButtons.setOpaque(false);
		RoundedIButt returnHomeButt = new RoundedIButt("return.png","Return");
		returnHomeButt.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				if (infoShown) //change to delete topic button
				{
					try {
						if (!Constants.prompt(self,"Delete '"+title+"'","Please Confirm Deleting'"+title+"'"))
							return;
						String[] relatedArr = related.toArray(new String[related.size()]);
						for (String r : relatedArr)
						{
							String rID = r.split(Constants.separator)[1];
							unlink(rID,true);
						}
						Constants.deleteFolder(Constants.topicPath+File.separator+topicID);
						Constants.deleteFile(Constants.topicPath+File.separator+topicID+".zip");
						Constants.deleteFile(Constants.annoPath+File.separator+topicID+".db");
						File topicIndFile = new File(Constants.topicPath+File.separator+"topic.ind");
						BufferedReader reader = new BufferedReader(new FileReader(topicIndFile));
						String line;
						int lineIndex = 0;
						while ((line = reader.readLine()) != null)
						{
							line = line.trim();
							String[] lineArr = line.split(Constants.separator);
							if (lineArr[0].equals(topicID))
							{
								Constants.removeLine(topicIndFile.getPath(),lineIndex);
								break;
							}
							lineIndex++;
						}
						reader.close();
						Constants.refreshTLPanel();
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
				close();
			}
		});
		CHButtons.add(returnHomeButt);
		JSeparator CHJsep1 = new JSeparator(SwingConstants.VERTICAL);
		CHJsep1.setPreferredSize(new Dimension(1, Constants.topicHeadHeight/2));
		CHButtons.add(CHJsep1);
		RoundedIButt topicInfoButt = new RoundedIButt("info.png","Info");
		topicInfoButt.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				descPanel.removeAll();
				if (!infoShown)
				{
					descPanel.add(loadInfoPanel());
					returnHomeButt.setImage("trash.png");
					returnHomeButt.setToolTip("Delete");
					returnHomeButt.setText("Delete");
				}
				else
				{
					returnHomeButt.setImage("return.png");
					returnHomeButt.setToolTip("Return");
					returnHomeButt.setText("Return");
					descPanel.add(descTitle,BorderLayout.NORTH);
					JScrollPane descScroller = new JScrollPane(descriptionJTA); 
					descPanel.add(descScroller,BorderLayout.CENTER);
				}
				descPanel.revalidate();
				descPanel.repaint();
				infoShown = !infoShown;
			}
		});
		CHButtons.add(topicInfoButt);
		JSeparator CHJsep2 = new JSeparator(SwingConstants.VERTICAL);
		CHJsep2.setPreferredSize(new Dimension(1, Constants.topicHeadHeight/2));
		CHButtons.add(CHJsep2);
		if (isAuthor) //logged in
		{
			RoundedIButt editTopicButt = new RoundedIButt("edit.png","Edit");
			editTopicButt.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					if (infoShown)
						return;
					if (!editingTopic)
					{
						editTopicButt.setImage("save.png");
						editTopicButt.setToolTip("Save");
						editTopicButt.setText("Save");
						//returnHomeButt.setText("Delete");
						descriptionJTA.setEditable(true);
						descriptionJTA.requestFocusInWindow();
						descriptionJTA.selectAll();
					} else {
						editTopicButt.setImage("edit.png");
						editTopicButt.setToolTip("Edit");
						editTopicButt.setText("Edit");
						String formattedNewDesc = descriptionJTA.getText().replaceAll(Constants.NLSeparator,"nl").replaceAll("\n",Constants.NLSeparator);
						Constants.editLine(Constants.topicPath+File.separator+topicID+File.separator+"topic.nfo",7,formattedNewDesc);
						descriptionJTA.setEditable(false);
						Constants.updateTopicInd();
						if (Constants.toggleOptions.get("Auto Zip Topic"))
							Constants.zipTopic(topicID,self,false);
						else
							setPublished(false);
					}
					descPanel.revalidate();
					descPanel.repaint();
					editingTopic = !editingTopic;
				}
			});
			CHButtons.add(editTopicButt);
			JSeparator CHJsep3 = new JSeparator(SwingConstants.VERTICAL);
			CHJsep3.setPreferredSize(new Dimension(1, Constants.topicHeadHeight/2));
			CHButtons.add(CHJsep3);
			publishButt = new RoundedIButt("publish.png", "Published");
			publishButt.addActionListener(e -> {
				Constants.zipTopic(topicID,self,false);
			});
			publishButt.setEnabled(false);
			CHButtons.add(publishButt);
		}
		else
		{
			authorStatusButt = new RoundedIButt("blankDot.png","Author Status: Connecting...");
			authorStatusButt.addActionListener(e ->
			{
				pingHost();
			});
			CHButtons.add(authorStatusButt);
		}
		JSeparator CHJsepHead = new JSeparator(SwingConstants.HORIZONTAL);
		contentHead.add(CHJsepHead,BorderLayout.NORTH);
		contentHead.add(CHButtons,BorderLayout.WEST);
		descHeadPanel.add(contentHead,BorderLayout.NORTH);
		descPanel = new JPanel(new BorderLayout());
		descPanel.setOpaque(false);
		descPanel.setName("descPanel");
		descTitle = new JLabel("Description:");
		descTitle.setFont(new Font(Constants.fontName, Font.BOLD, (int)(Constants.descFont.getSize()*1.0)));
		descPanel.add(descTitle,BorderLayout.NORTH);
		descriptionJTA = new JTextArea(desc.replaceAll(Constants.NLSeparator,"\n"));
		descriptionJTA.setEditable(false);
		descriptionJTA.setBackground(Constants.mainFG);
		descriptionJTA.setWrapStyleWord(true);
		descriptionJTA.setLineWrap(true);
		descriptionJTA.setFont(Constants.descFont);
		JScrollPane descScroller = new JScrollPane(descriptionJTA); 
		Constants.setFixedSize(descScroller,new Dimension((int)descHeadPanel.getSize().getWidth(),(int)(descHeadPanel.getSize().getHeight() - contentHead.getSize().getHeight())));
		descPanel.add(descScroller,BorderLayout.CENTER);
		descHeadPanel.add(descPanel,BorderLayout.CENTER);
		descHeadPanel.setName("descHeadPanel");
		cardContentPanel.add(descHeadPanel,"descHeadPanel");
		JPanel PSPanel = new JPanel();
		PSPanel.setLayout(new BoxLayout(PSPanel, BoxLayout.Y_AXIS));
		if (isAuthor)
		{
			JButton addPSButton = new JButton("Add Source");
			Constants.setFixedSize(addPSButton,new Dimension(Constants.topicDim[0] - Constants.commentsWidth,Constants.addButtHeight));
			addPSButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			addPSButton.setName("addButt");
			addPSButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new AddSource(topicID,self,true);
				}
			});
			PSPanel.add(addPSButton);
		}
		PSPanel.setName("PSPanel");
		cardContentPanel.add(PSPanel,"PSPanel");
		JPanel SSPanel = new JPanel();
		SSPanel.setLayout(new BoxLayout(SSPanel, BoxLayout.Y_AXIS));
		//SSPanel.setMaximumSize(new Dimension(Constants.topicDim[0] - Constants.commentsWidth,Constants.topicDim[0] - Constants.topicHeadHeight-100));
		if (isAuthor)
		{
			JButton addSSButton = new JButton("Add Source");
			Constants.setFixedSize(addSSButton,new Dimension((Constants.topicDim[0] - Constants.commentsWidth),Constants.addButtHeight));
			addSSButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			addSSButton.setName("addButt");
			addSSButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new AddSource(topicID,self,false);
				}
			});
			SSPanel.add(addSSButton);
		}	
		SSPanel.setName("SSPanel");
		cardContentPanel.add(SSPanel,"SSPanel");
		JPanel RTPanel = new JPanel();
		RTPanel.setName("RTPanel");
		RTPanel.setLayout(new BoxLayout(RTPanel, BoxLayout.Y_AXIS));
		//RTPanel.setMaximumSize(new Dimension(Constants.topicDim[0] - Constants.commentsWidth,Constants.topicDim[0] - Constants.topicHeadHeight-100));
		JButton lnkRButton = new JButton("Link Related Button");
		lnkRButton.setName("addButt");
		Constants.setFixedSize(lnkRButton,new Dimension(Constants.topicDim[0] - Constants.commentsWidth,Constants.addButtHeight));
		lnkRButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		lnkRButton.addActionListener(e -> {
			linkSearch();
		});
		RTPanel.add(lnkRButton);
		typePanel = new JPanel(new CardLayout());
		typePanel.setName("typePanel");
		typePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		CardLayout typeCardLayout = (CardLayout) typePanel.getLayout();
		for (String relationType : Constants.relationTypes)
		{
			JPanel tmpTypePanel = new JPanel();
			tmpTypePanel.setLayout(new BoxLayout(tmpTypePanel, BoxLayout.Y_AXIS));
			tmpTypePanel.setName(relationType);
			typePanel.add(tmpTypePanel,relationType);
		}
		relationDropdown = new JComboBox<>(Constants.relationTypes);
		Constants.setFixedSize(relationDropdown,new Dimension(Constants.topicDim[0] - Constants.commentsWidth,Constants.addButtHeight));
		relationDropdown.setName("relationDropdown");
		relationDropdown.addActionListener(e -> {
			String selected = (String)relationDropdown.getSelectedItem();
			if (selected != null)
			{
				curRelType = selected;
				typeCardLayout.show(typePanel,curRelType);
			}
		});
		typeCardLayout.show(typePanel,Constants.relationTypes[0]);
		relationDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
		RTPanel.add(relationDropdown);
		JScrollPane typeJsp = new JScrollPane(typePanel);
		typeJsp.setName("typeJsp");
		RTPanel.add(typeJsp);
		cardContentPanel.add(RTPanel,"RTPanel");
		sContentPanel.add(cardContentPanel,BorderLayout.NORTH);
		JPanel sourcePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		sourcePanel.setName("sourcePanel");
		JButton PSButton = new JButton("Primary");
		PSButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchPanel("PSPanel",true);
			}
		});
		JButton SSButton = new JButton("Secondary");
		SSButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchPanel("SSPanel",true);
			}
		});
		JButton RTButton = new JButton("Related");
		RTButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchPanel("RTPanel",true);
			}
		});
		JButton[] navButts = {PSButton,SSButton,RTButton};
		for (JButton navButt : navButts)
		{
			Constants.setFixedSize(navButt,new Dimension((Constants.topicDim[0] - Constants.commentsWidth)/3,Constants.switchCardHeight));
			navButt.setBackground(Constants.buttonColor);
			sourcePanel.add(navButt);
		}
		//sourcePanel.setDebugGraphicsOptions(DebugGraphics.FLASH_OPTION);
		sContentPanel.add(sourcePanel,BorderLayout.SOUTH);
		mContentPanel.add(sContentPanel,BorderLayout.WEST);
		JPanel commentPanel = new JPanel(new BorderLayout());
		commentPanel.setName("commentPanel");
		commentPanel.setOpaque(false);
		JLabel commentTitle = new JLabel("Annotations");
		commentTitle.setFont(new Font(Constants.fontName,Font.BOLD,(int)(Constants.descFont.getSize()*1.10)));
		commentTitle.setHorizontalAlignment(SwingConstants.CENTER);
		commentPanel.add(commentTitle,BorderLayout.NORTH);
		annoBox = new AnnotateBox(isAuthor,new Dimension(Constants.commentsWidth,(Constants.PSRPanelDim[1] - commentTitle.getHeight())),this,annotations);
		commentPanel.add(annoBox,BorderLayout.CENTER);
		mContentPanel.add(commentPanel,BorderLayout.EAST);
		mContentPanel.setOpaque(false);
		mainPanel.add(mContentPanel);
		//Constants.dumpComponentTree(mainPanel,0);
		setContentPane(mainPanel);
		self.setSize(Constants.topicDim[0],Constants.topicDim[1]);
		self.setResizable(false);
		/*this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newWidth = self.getWidth();
                int newHeight = self.getHeight();
                System.out.println("New width: " + newWidth);
                System.out.println("New height: " + newHeight);
            }
        });*/
		if (hash == null && !startTopic)
		{
			boolean hashFound = false;
			String hlPath = Constants.topicPath+File.separator+".hl";
			try {
				String hlString = Files.readString(Path.of(hlPath));
				String hlDecoded = EncryptionDecryption.decrypt(hlString,Constants.key);
				String[] hashList = hlDecoded.split(Constants.NLSeparator);
				for (String hashPair : hashList)
				{
					String[] hashArr = hashPair.split(Constants.separator);
					if (hashArr[0].equals(topicID))
					{
						hashFound = true;
						hash = hashArr[1];
						break;
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (!hashFound)
				setHash();
		}
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
	}
	public void reShow()
	{
		boolean found  = false;
		Window[] windows = Window.getWindows();
		for (Window window : windows)
		{
            if (window instanceof Topic && window.isVisible())
            {
            	Topic topic = (Topic) window;
                if (topic.getID().equals(topicID))
                {
                	found = true;
                	topic.toFront();
                	return;
                }
            }
        }
		if (!found)
		{
			setVisible(false);
			this.validate();
			this.revalidate();
			this.repaint();
			pack();
			setVisible(true);
			Constants.addUnique(Constants.topics,this);
			if (!isAuthor)
				pingHost();
		}
	}
	private void promptUpdate(String authHash)
	{
		System.out.println("Auth Hash: "+authHash);
		System.out.println("Current Hash: "+hash);
		if (Constants.prompt(self,authUser+" has a Different Version of this Topic","Update to match it?"))
		{
			try 
			{
				Constants.deleteFile(Constants.topicPath+File.separator+topicID+".zip");
				File topicIndFile = new File(Constants.topicPath+File.separator+"topic.ind");
				BufferedReader reader = new BufferedReader(new FileReader(topicIndFile));
				String line;
				int lineIndex = 0;
				while ((line = reader.readLine()) != null)
				{
					line = line.trim();
					String[] lineArr = line.split(Constants.separator);
					if (lineArr[0].equals(topicID))
					{
						Constants.removeLine(topicIndFile.getPath(),lineIndex);
						break;
					}
					lineIndex++;
				}
				reader.close();
				String topicInfo = Constants.ns[0].reqFromAuth(authNode,topicID);
				if (topicInfo != null) //author available
				{
					String[] authResp = topicInfo.split(Constants.separator);
					long filesize = Long.parseLong(authResp[1]);
					ArrayList<Node> availNodes = new ArrayList<Node>();
					for (Map.Entry<String, Node> nodeMap : Constants.nodeList.entrySet())
					{
						Node node = nodeMap.getValue();
						String nodeHash = Constants.ns[0].checkTopicHash(node,topicID);
						if (nodeHash != null && nodeHash.equals(authHash))
							availNodes.add(node);
					}
					Constants.ns[0].startTorrent(availNodes,topicID,filesize);
					self.close();
					Topic t = Constants.getTopic(topicID);
					t.reShow();
				}
				else
				{
					System.out.println("Author Unavailable!!");
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	public boolean pingHost()
	{
		if (isAuthor)
			return true;
		boolean result = false;
		try {
			String topicHashandOpen = Constants.ns[0].checkTopicComments(authNode,topicID);
			if (topicHashandOpen != null)
			{
				String[] topicHashandOpenArr = topicHashandOpen.split(Constants.separator);
				String topicHash = topicHashandOpenArr[0];
				boolean topicOpen = topicHashandOpenArr[1].equals("open");
				if (topicOpen)
				{
					//commentSection.openComments();
					authorStatusButt.setImage("greenDot.png");
					authorStatusButt.setText("Author Status: Connected & Open");
					authorStatusButt.setToolTip("Author Status: Connected & Open");
				}
				else
				{
					//commentSection.closeComments();
					authorStatusButt.setImage("orangeDot.png");
					authorStatusButt.setText("Author Status: Connected & Closed");
					authorStatusButt.setToolTip("Author Status: Connected & Closed");
				}
				if (!topicHash.equals(hash))
					promptUpdate(topicHash);
				result = true;
				annoBox.sendable(true);
			}
			else
			{
				authorStatusButt.setImage("redDot.png");
				authorStatusButt.setText("Author Status: Unreachable");
				authorStatusButt.setToolTip("Author Status: Unreachable");
				annoBox.sendable(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return result;
	}
	public String getID()
	{
		return topicID;
	}
	public void setHash()
	{
		hash = Constants.generateHash(new File(Constants.topicPath+File.separator+topicID+".zip"));
		String hlPath = Constants.topicPath+File.separator+".hl";
		try {
			String hlString = Files.readString(Path.of(hlPath));
			String hlDecoded = EncryptionDecryption.decrypt(hlString,Constants.key);
			BufferedWriter bw = new BufferedWriter(new FileWriter(hlPath));
			bw.write(EncryptionDecryption.encrypt(hlDecoded+Constants.NLSeparator+(topicID+Constants.separator+hash),Constants.key));
			bw.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	public String getHash()
	{
		return hash;
	}
	public String getCID()
	{
		return CID;
	}
	public String getDesc()
	{
		return desc;
	}
	public String getTitle()
	{
		return title;
	}
	public void addSource(Source s)
	{
		sources.add(s);
		if (!startTopic)
			switchPanel(curCard.getName(),false);
	}
	public void switchPanel(String panelName, boolean switchBool)
	{
		curCard = null;
		JPanel newCard = null;
		for (Component comp : cardContentPanel.getComponents()) {
			if (comp.isVisible() == true)
		    	curCard = (JPanel) comp;
			if (comp.getName().equals(panelName))
				newCard = (JPanel) comp;
		}
		if (panelName.equals(curCard.getName()) && switchBool)
		{
			contentCL.show(cardContentPanel,"descHeadPanel");
			return;
		}
		for (Component comp : newCard.getComponents())
		{
			if (comp.getName() == null) {
				//System.out.println(comp);
				newCard.remove(comp);}
		}
		JPanel container = new JPanel();
		container.setOpaque(false);
		container.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JScrollPane jsp = new JScrollPane(container){
		    @Override
		    protected void paintComponent(Graphics g) {
		    	Color bgColor = null;
				switch(panelName)
				{
					case "PSPanel":
						bgColor = new Color(255, 0, 0, 25);
						break;
					case "SSPanel":
						bgColor = new Color(0, 0, 255, 25);
						break;
					case "RTPanel":
						bgColor = new Color(0, 255, 0, 25);
						break;
				}
		        Graphics2D g2 = (Graphics2D) g.create();
		        g2.setColor(bgColor); // semi-transparent color
		        g2.fillRect(0, 0, getWidth(), getHeight());
		        g2.dispose();
		        super.paintComponent(g); // optional: keep super call if unsure
		    }
		};
		jsp.setOpaque(false);
		jsp.getViewport().setOpaque(false);
		if (panelName.equals("PSPanel") || panelName.equals("SSPanel"))
		{
			int[] PSPanelGrid = {3,4};
			int sourcesMade = 0;
			for (Source s : sources)
				if (!((s.isPrimary && panelName.equals("SSPanel")) || (!s.isPrimary && panelName.equals("PSPanel"))))
			        sourcesMade++;
			int initialCapacity = PSPanelGrid[0] * PSPanelGrid[1];
			if (sourcesMade <= initialCapacity)
                container.setLayout(new GridLayout(PSPanelGrid[0], PSPanelGrid[1], 10, 10));
            else
                container.setLayout(new GridLayout(0, PSPanelGrid[1], 10, 10));
			for (Source s : sources)
			{
				if ((s.isPrimary && panelName.equals("SSPanel")) || (!s.isPrimary && panelName.equals("PSPanel")))
					continue;
				SourceButton tmpButt = new SourceButton(s);
        		tmpButt.addActionListener(e -> {
        			showSource(s);
        		});
            	container.add(tmpButt);
            }
			if (sourcesMade <= initialCapacity) {
                int blanksToMake = initialCapacity - sourcesMade;
                if (blanksToMake > 0) {
                    for (int i = 0; i < blanksToMake; i++)
                        container.add(new JLabel(""));
                }
            }
			newCard.add(jsp);
		}
		else //related topics
		{
			for (Component comp : typePanel.getComponents())
			{
				if (comp instanceof JPanel)
				{
					JPanel thisTypePanel = ((JPanel)comp);
					thisTypePanel.removeAll();
					String relatedType = thisTypePanel.getName();
					for (String rTopic : related)
					{
						if (Constants.relationTypes[Integer.parseInt(rTopic.split(Constants.separator)[0])].equals(relatedType))
						{
							thisTypePanel.add(Box.createVerticalStrut(5));
							thisTypePanel.add(new RelatedButton(this,rTopic));
						}
					}
				}
			}
            relationDropdown.setSelectedItem(curRelType);;
			//CardLayout typeCardLayout = (CardLayout) typePanel.getLayout();
			//typeCardLayout.show(typePanel,curRelType);
			typePanel.revalidate();
			typePanel.repaint();
		}
		Constants.setFixedSize(newCard,new Dimension(Constants.PSRPanelDim[0],Constants.PSRPanelDim[1]));
		contentCL.show(cardContentPanel,panelName);
		cardContentPanel.revalidate();
		cardContentPanel.repaint();
		curCard = newCard;
	}
	public String toString()
	{
		return topicID+Constants.separator+title+Constants.separator+((desc.length() > 10) ? desc.substring(0,10):desc)+Constants.separator+authUser;
	}
	public void linkSearch() {
	    JFrame searchFrame = new JFrame("Select a Topic to Relate");
	    searchFrame.setIconImage(ResourceLoader.loadIcon("img/logo.png"));
	    searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    JPanel mainPanel = new JPanel();
	    Constants.setFixedSize(mainPanel,new Dimension(Constants.popupSize[0],Constants.popupSize[1]));
	    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

	    // Search field
	    PlaceholderTextField searchField = new PlaceholderTextField("Search for Related Topics");
		searchField.setFont(Constants.descFont);
		searchField.setBackground(Constants.mainFG);
		searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));
	    searchField.setAlignmentX(Component.CENTER_ALIGNMENT);
	    mainPanel.add(searchField);
	    mainPanel.add(Box.createVerticalStrut(5));
	    JLabel setRelationLabel = new JLabel("Set Topic Relationship Type");
	    setRelationLabel.setFont(Constants.descFont);
	    setRelationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    mainPanel.add(setRelationLabel);
	    JComboBox<String> resultsRelationDropdown = new JComboBox<>(Constants.relationTypes);
	    resultsRelationDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
	    resultsRelationDropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, resultsRelationDropdown.getPreferredSize().height));
	    mainPanel.add(resultsRelationDropdown);
	    mainPanel.add(Box.createVerticalStrut(5));
	    mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
	    mainPanel.add(Box.createVerticalStrut(5));
	    // Results panel with scroll
	    JPanel resultsPanel = new JPanel();
	    resultsPanel.setBorder(null);
	    //resultsPanel.setBackground(Constants.mainFG);
	    resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
	    JScrollPane jsp = new JScrollPane(resultsPanel);
	    jsp.setAlignmentX(Component.CENTER_ALIGNMENT);
	    jsp.setPreferredSize(new Dimension(Constants.popupSize[0], Constants.popupSize[1]));
	    jsp.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	    jsp.setOpaque(false);
	    jsp.setBorder(null);
	    mainPanel.add(jsp);

	    // Function to populate the results panel
	    Runnable populateResults = () -> {
	        resultsPanel.removeAll();
	        String[] searchQ = searchField.getText().split(" ");
	        Topic[] foundTopics = (searchQ.length > 0) ? Constants.search(searchQ, CID) : Constants.topics.toArray(new Topic[Constants.topics.size()]);
	        if (foundTopics != null) {
	            for (Topic t : foundTopics) {
	                if (t.getID().equals(topicID)) continue;
	                boolean found = false;
	                for (String rTopic : related)
	                {
	                	if (rTopic.contains(t.toString()))
	                	{
	                		found = true;
	                		continue;
	                	}
	                }
	                if (found)
	                	continue;
	                JButton resultButt = new JButton(t.getTitle());
	                resultButt.setToolTipText(t.getDesc());
	                resultButt.setAlignmentX(Component.CENTER_ALIGNMENT);
	                resultButt.setMaximumSize(new Dimension(Integer.MAX_VALUE, resultButt.getPreferredSize().height));
	                resultButt.addActionListener(e -> {
	                    link(t.getID(), resultsRelationDropdown.getSelectedIndex(),true);
	                    searchFrame.dispose();
	                });
	                resultsPanel.add(Box.createVerticalStrut(5));
	                resultsPanel.add(resultButt);
	            }
	        }
	        resultsPanel.revalidate();
	        resultsPanel.repaint();
	        //searchFrame.pack();
	    };

	    // Initial population with all topics
	    populateResults.run();

	    // Search trigger on Enter
	    searchField.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyPressed(KeyEvent e) {
	            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	                populateResults.run();
	            }
	        }
	    });

	    searchFrame.add(mainPanel);
	    searchFrame.pack();
	    searchFrame.setResizable(false);
	    searchFrame.setVisible(true);
	    resultsRelationDropdown.requestFocusInWindow();
	}
	public void link(String tID, int typeIndex, boolean fromStart)
	{
		Topic t = Constants.getTopic(tID);
		for (String rTopic : related)
		{
			String[] topicArr = rTopic.split(Constants.separator);
			if (topicArr[1].equals(tID))
			{
				System.out.println("Already Added!");
				return;
			}
		}
		try
		{
            String rLine = Integer.toString(typeIndex)+Constants.separator+t.toString();
            FileWriter writer = new FileWriter(Constants.topicPath+File.separator+topicID+File.separator+"related.nfo",true);
            writer.write(rLine+"\n");
            writer.close();
            related.add(rLine);
            if (fromStart)
            	t.link(topicID,typeIndex,false);
            curRelType = Constants.relationTypes[typeIndex];
            switchPanel("RTPanel",false);
            t.repaint();
            t.revalidate();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
	}
	public void unlink(String rTopicID,boolean fromStart)
	{
		try 
		{
			Topic rTopic = Constants.getTopic(rTopicID);
			int index = 0;
			String line;
			String topicPath = Constants.topicPath+File.separator+topicID+File.separator+"related.nfo";
			BufferedReader br = new BufferedReader(new FileReader(new File(topicPath)));
			while ((line = br.readLine()) != null)
			{
				line = line.trim();
				if (line.contains(rTopicID))
				{
					br.close();
					Constants.removeLine(topicPath,index);
					break;
				}
				index++;
			}
			br.close();
			related.removeIf(item -> item.contains(rTopicID));
			rTopic.setCurRelType(curRelType);
			if (fromStart)
				rTopic.unlink(topicID,false);
			switchPanel("RTPanel",false); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public String getAuthCID()
	{
		return authorCID;
	}
	public String getAuthUID()
	{
		return authorUID;
	}
	private JPanel loadInfoPanel()
	{
		JPanel infoPanel = new JPanel(new GridLayout(1,2));
		JPanel infoDescPanel = new JPanel();
		infoDescPanel.setLayout(new BoxLayout(infoDescPanel, BoxLayout.Y_AXIS));
		JLabel topicInfo = new JLabel("Topic Info");
		topicInfo.setFont(new Font(Constants.fontName, Font.BOLD, (int)(Constants.titleFont.getSize()*0.8)));
		topicInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
		topicInfo.setHorizontalAlignment(SwingConstants.LEFT);
		topicInfo.setHorizontalTextPosition(SwingConstants.LEFT);
		topicInfo.setMaximumSize(new Dimension(Integer.MAX_VALUE, topicInfo.getPreferredSize().height));
		JPanel topicInfoWrapper = new JPanel(new BorderLayout());
		topicInfoWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, topicInfo.getPreferredSize().height));
		topicInfoWrapper.setOpaque(false);
		topicInfoWrapper.add(topicInfo, BorderLayout.WEST);
		infoDescPanel.add(topicInfoWrapper);
		infoDescPanel.add(Box.createVerticalStrut(5));
		int[] nums = {0,0,related.size()};
		for (Source s : sources)
			if (s.isPrimary)
				nums[0]++;
			else
				nums[1]++;
		String[] infoFields = {"Primary Sources","Secondary Sources","Related Topics","Author","Topic Size","Topic ID","Topic Hash"};
		for (int i=0;i<infoFields.length;i++)
		{
			JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			JLabel tmpLabel = new JLabel();
			tmpLabel.setFont(Constants.labelFont);
			JLabel tmpValue = new JLabel();
			tmpValue.setFont(Constants.valueFont);
			switch(infoFields[i])
			{
				case "Primary Sources":
				case "Secondary Sources":
				case "Related Topics":
					tmpLabel.setText("# of "+infoFields[i]+": ");
					tmpValue.setText(Integer.toString(nums[i]));
					break;
				case "Author":
					tmpLabel.setText(infoFields[i]+": ");
					tmpValue.setText(authUser);
					break;
				case "Topic Size":
					tmpLabel.setText(infoFields[i]+": ");
					try {
						tmpValue.setText(Constants.getHRFileSize(Constants.getFolderSize(mainDir.getPath())));
					} catch (IOException e) {
						tmpValue.setText("ERROR");
						e.printStackTrace();
					}
					break;
				case "Topic ID":
					tmpLabel.setText(infoFields[i]+": ");
					tmpValue.setText(topicID);
					break;
				case "Topic Hash":
					tmpLabel.setText(infoFields[i]+": ");
					tmpValue.setText(hash);
					break;
			}
			fieldPanel.add(tmpLabel);
			fieldPanel.add(tmpValue);
			infoDescPanel.add(fieldPanel);
		}
		JScrollPane jsp = new JScrollPane(infoDescPanel);
		infoPanel.add(jsp);
		PieChart infoPie = new PieChart(nums);
		infoPanel.add(infoPie);
		return infoPanel;
	}
	public void close()
	{
		if (!published)
		{
			boolean save = Constants.prompt(self,"Topic Not Pubnlished!","All unpublished data will not be available for your nodes.","Publish","Don't Publish");
			if (save)
				Constants.zipTopic(topicID,self,false);
		}
		for (Source s : sources)
		{
			JFrame ss = s.getFrame();
			if (s.isOpen && ss != null)
			{
				s.isOpen = false;
				ss.dispose();
			}
		}
        for (Topic t : Constants.topics)
        {
        	if (t.getID().equals(topicID))
        	{
        		Constants.topics.remove(t);
        		break;
        	}
        }
		self.dispose();
		Window[] windows = Window.getWindows();
		for (Window window : windows)
		{
            if (window instanceof JFrame && window.isVisible())
            {
            	JFrame win = (JFrame) window;
                if (win.getTitle().equals("RedLines"))
                {
                	win.toFront();
                	break;
                }
            }
        }
	}
	private void showSource(Source s)
	{
		s.isOpen = true;
		boolean found  = false;
		Window[] windows = Window.getWindows();
		for (Window window : windows)
		{
            if (window instanceof JFrame && window.isVisible())
            {
            	JFrame sourceFrame = (JFrame) window;
                if (sourceFrame.getName().equals(s.ID))
                {
                	found = true;
                	sourceFrame.toFront();
                	break;
                }
            }
        }
		if (found)
			return;
		JFrame ss = new JFrame(s.title);
		ss.setIconImage(ResourceLoader.loadIcon("img/logo.png"));
		ss.setName(s.ID);
		s.setFrame(ss);
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel mainShowPanel = new JPanel(new BorderLayout());
		JPanel previewDescPanel = new JPanel(new GridLayout(1,2));
		previewDescPanel.setOpaque(false);
		JPanel previewPanel = new JPanel();
		previewPanel.setOpaque(false);
		ImagePanel IP = new ImagePanel();
		switch(s.type)
		{
			case "img":
				IP.setImage(s.sourceFile);
				Constants.setFixedSize(IP,new Dimension(300, 300));
				previewPanel.add(IP);
				break;
			case "vid":
			case "aud":
				VideoPlayer VP;
				try {
					VP = new VideoPlayer(s,ss);
					if (s.type.equals("aud"))
						Constants.setFixedSize(VP,new Dimension(Constants.popupSize[0], 250));
					previewPanel.add(VP);
					VP.initialize();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				break;
			case "pdf":
				SwingController controller = new SwingController();
				SwingViewBuilder factory = new SwingViewBuilder(controller);
				JPanel pdfPanel = factory.buildViewerPanel();
				ComponentKeyBinding.install(controller, pdfPanel);
				controller.getDocumentViewController().setAnnotationCallback(
					     new org.icepdf.ri.common.MyAnnotationCallback(
					            controller.getDocumentViewController()));
				previewPanel.add(pdfPanel);
				pdfPanel.setPreferredSize(new Dimension(500,600));
				controller.openDocument(s.sourceFile.getPath());
				s.addPDFController(controller);
				break;
			case "link":
			    previewPanel.setPreferredSize(new Dimension(500, 500));
			    JButton toggleButton = new JButton("Show Raw");
				browserThread = new Thread(() -> {
					CefApp cefApp = CefApp.getInstance();
					CefClient client = cefApp.createClient();
				    CefBrowser browser = client.createBrowser(s.name, true, false); 
				    browser.createImmediately();
				    Component browserUI = browser.getUIComponent();
				    JTextArea curlOutput = new JTextArea();
					try {
						curlOutput.setText(Files.readString(Paths.get(s.path)));
					} catch (IOException e) {
						curlOutput.setText("Error Loading "+s.title); 
					}
				    curlOutput.setEditable(false);
				    JScrollPane curlScrollPane = new JScrollPane(curlOutput);
				    JPanel browserPanel = new JPanel(new BorderLayout());
				    browserPanel.add(browserUI, BorderLayout.CENTER);
				    SwingUtilities.invokeLater(() -> {
					    JPanel cards = new JPanel(new CardLayout());
					    cards.add(browserPanel, "browser");
					    cards.add(curlScrollPane, "curl");
					    toggleButton.addActionListener(e -> {
					        CardLayout cl = (CardLayout)(cards.getLayout());
					        if (toggleButton.getText().equals("Show Browser")) {
					            cl.show(cards, "browser");
					            toggleButton.setText("Show Raw");
					        } else {
					            cl.show(cards, "curl");
					            toggleButton.setText("Show Browser");
					        }
					        previewPanel.repaint();
					        previewPanel.revalidate();
					    });
					    previewPanel.setLayout(new BorderLayout());
						previewPanel.add(toggleButton,BorderLayout.SOUTH);
					    previewPanel.add(cards,BorderLayout.CENTER);
					    previewPanel.repaint();
				        previewPanel.revalidate();
				    });
				    ss.addWindowListener(new WindowAdapter() {
			            @Override
			            public void windowClosing(WindowEvent e) {
			                if (browser != null) {
			                    browser.close(true); // Dispose of browser safely
			                }
			                if (browserThread != null && browserThread.isAlive()) {
			                    browserThread.interrupt(); // Stop any long-running logic if you have any
			                }
			            }
			        });
				    try {
				        Thread.sleep(5000); // Wait 3 seconds
				    } catch (InterruptedException ignored) {}
				    
				    SwingUtilities.invokeLater(() -> {
				    	 if (browser != null && browser.isLoading()) {
				             browser.stopLoad(); 
				         }
				    });
				    /*browserUI.addHierarchyListener(e -> {
				        if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && browserUI.isShowing()) {
				            // Wait a little to make sure GLCanvas is fully ready
				            new javax.swing.Timer(300, evt -> {
				                System.out.println("Trying screenshot after showing...");
				                browser.createScreenshot(true).thenAccept(img -> {
				                    if (img != null) {
				                        IP.setImage(img);
				                    } else {
				                        System.out.println("Screenshot failed, image is null.");
				                    }
				                });
				                ((javax.swing.Timer) evt.getSource()).stop();
				            }).start();
				        }
				    });*/
				});
				browserThread.start();
				break;
			default:
				//previewPanel = new JPanel();
				previewPanel.setLayout(new BoxLayout(previewPanel,BoxLayout.Y_AXIS));
				JLabel sName = new JLabel(s.name);
				sName.setFont(Constants.titleFont);
				sName.setToolTipText(s.name);
				sName.setAlignmentX(Component.LEFT_ALIGNMENT); 
				previewPanel.add(sName);
				JLabel sTitle = new JLabel(s.title);
				sTitle.setFont(Constants.descFont);
				sTitle.setAlignmentX(Component.LEFT_ALIGNMENT); 
				previewPanel.add(sTitle);
				JLabel fTime = new JLabel(Constants.formatTimestamp(s.timestamp));
				fTime.setFont(Constants.descFont);
				fTime.setAlignmentX(Component.LEFT_ALIGNMENT); 
				previewPanel.add(fTime);
				JLabel fSize = new JLabel(Constants.getHRFileSize(s.sourceFile.length()));
				fSize.setFont(Constants.descFont);
				fSize.setAlignmentX(Component.LEFT_ALIGNMENT); 
				previewPanel.add(fSize);
				break;
		}
		previewDescPanel.add(previewPanel);
		JPanel sDescPanel = new JPanel(new BorderLayout());
		sDescPanel.setOpaque(false);
		sDescPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		SelectLabel descLabel = new SelectLabel();
		descLabel.setOpaque(true);
		descLabel.setBorder(null);
		descLabel.setFont(Constants.descFont);
		String out = "Unable to read description.";
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(Constants.topicPath+File.separator+topicID+File.separator+"sources"+File.separator+((s.isPrimary) ? "P":"S")+File.separator+s.sourceFile.getName()+".desc"));
        	String line = "";
			out = "";
        	while ((line = br.readLine()) != null)
			{
				line = line.trim();
				out += line+"\n";
			}
        	br.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		descLabel.setText(out);
		//sDescPanel.add(descTitle,BorderLayout.NORTH);
		sDescPanel.add(new JSeparator(SwingConstants.HORIZONTAL),BorderLayout.NORTH);
		JScrollPane dljsp = new JScrollPane(descLabel);
		dljsp.setBorder(null);
		sDescPanel.add(dljsp,BorderLayout.CENTER);
		sDescPanel.add(new JSeparator(SwingConstants.HORIZONTAL),BorderLayout.SOUTH);
		previewDescPanel.add(sDescPanel);
		mainShowPanel.add(previewDescPanel,BorderLayout.CENTER);
		JPanel previewButts = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		String[] butts = {"open","download","edit","delete"};
		for (String butt : butts)
		{
			JButton tmpButt = null;
			switch(butt)
			{
				case "open":
					tmpButt = new IButton("open.png","Open");
					tmpButt.addActionListener(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								Desktop desktop = Desktop.getDesktop();
								switch(s.type)
								{
									case "link":
							            URI uri = new URI(s.name);
							            desktop.browse(uri);
										break;
									default:
										try {
											desktop.open(s.sourceFile);
										}
										catch (NumberFormatException nfE)
										{
											nfE.printStackTrace();
											String os = System.getProperty("os.name").toLowerCase();
											if (os.contains("win")) {
											    Runtime.getRuntime().exec(new String[]{"cmd", "/c", s.sourceFile.getAbsolutePath()});
											} else if (os.contains("mac")) {
											    Runtime.getRuntime().exec(new String[]{"open", s.sourceFile.getAbsolutePath()});
											} else if (os.contains("nix") || os.contains("nux")) {
											    Runtime.getRuntime().exec(new String[]{"xdg-open", s.sourceFile.getAbsolutePath()});
											}
										}
										break;
								}
							} catch (IOException e1) {
								e1.printStackTrace();
							} catch (URISyntaxException e1) {
								e1.printStackTrace();
							} 
						}
					});
					break;
				case "download":
					tmpButt = new IButton("download.png","Download");
					tmpButt.addActionListener(e -> {
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setDialogTitle("Save File As...");
						fileChooser.setSelectedFile(s.sourceFile); // Suggest original name

						int userSelection = fileChooser.showSaveDialog(null);
						if (userSelection == JFileChooser.APPROVE_OPTION) {
						    File destinationFile = fileChooser.getSelectedFile();

						    // Optional: confirm overwrite
						    if (destinationFile.exists()) {
						        int result = JOptionPane.showConfirmDialog(null,
						            "File exists. Overwrite?", "Confirm", JOptionPane.YES_NO_OPTION);
						        if (result != JOptionPane.YES_OPTION) return;
						    }

						    try {
						        Files.copy(s.sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
						        JOptionPane.showMessageDialog(null, "File saved to:\n" + destinationFile.getAbsolutePath());
						    } catch (IOException ex) {
						        ex.printStackTrace();
						        JOptionPane.showMessageDialog(null, "Error saving file:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						    }
						} 
					});
					break;
				case "edit":
					if (isAuthor) 
					{
						tmpButt = new IButton("edit.png","Edit");
						tmpButt.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e) {
								edit(s,sDescPanel);
							}
						});
					}
					else
						continue;
					break;
				case "delete":
					if (isAuthor) //logged in
					{
						tmpButt = new IButton("trash.png","Delete");
						tmpButt.addActionListener(new ActionListener()
						{
							@Override
							public void actionPerformed(ActionEvent e) {
								deletePrompt(s,ss);
							}
						});
					}
					else
						continue;
					break;
			}
			previewButts.add(tmpButt);
		}
		mainShowPanel.add(previewButts,BorderLayout.SOUTH);
		mainPanel.add(mainShowPanel);
		SwingUtilities.invokeLater(() -> {
			ss.add(mainPanel);
			ss.addWindowListener(new java.awt.event.WindowAdapter() {
	            @Override
	            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	            	s.isOpen = false;
	            	s.setFrame(null);
	            	toFront();
	            	requestFocus();
	            }
	        });
			ss.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			ss.pack();
			ss.setResizable(false);
			ss.revalidate();
			ss.repaint();
			ss.setVisible(true);
		});
	}
	private void deletePrompt(Source s, JFrame sFrame)
	{
		JFrame pFrame = new JFrame("Deleting '"+s.name+"'");
		pFrame.setIconImage(ResourceLoader.loadIcon("img/logo.png"));
		JPanel mainPanel = new JPanel(new BorderLayout());
		JLabel text = new JLabel("Delete Source '"+s.name+"'?");
		text.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.add(text,BorderLayout.CENTER);
		JPanel buttPanel = new JPanel(new GridLayout(1,2));
		JButton ok = new JButton("Yes");
		ok.addActionListener(e-> {
			delete(s,sFrame);
			pFrame.dispose();
			switchPanel(curCard.getName(), false);
		});
		buttPanel.add(ok);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(e -> {
			pFrame.dispose();
		});
		buttPanel.add(cancel);
		mainPanel.add(buttPanel,BorderLayout.SOUTH);
		pFrame.add(mainPanel);
		pFrame.pack();
		pFrame.setResizable(false);
		pFrame.setVisible(true);
	}
	private void delete(Source s, JFrame sFrame)
	{
		for (Source source : sources)
		{
			if (source.equals(s))
			{
				sources.remove(source);
				break;
			}
		}
		s.delete();
		sFrame.dispose();
		checkFull();
	}
	private void edit(Source s, JPanel sDescPanel)
	{
		Dimension descPanelSize = sDescPanel.getSize(); 
		int saveHeight = (int)(descPanelSize.height*0.05);
		int editHeight = descPanelSize.height - saveHeight - 20;
		sDescPanel.removeAll();
		JTextArea editDesc = new JTextArea(s.getDesc());
		editDesc.setSize(descPanelSize.width,editHeight);
		Constants.setFixedSize(editDesc,new Dimension(descPanelSize.width,editHeight));
		sDescPanel.add(editDesc,BorderLayout.CENTER);
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				String newDesc = editDesc.getText();
				try (FileWriter writer = new FileWriter(s.path+".desc")) { 
		            writer.write(newDesc); 
		        } catch (IOException ex) {
		            System.err.println("An error occurred while writing to the file: " + ex.getMessage());
		        }
				sDescPanel.removeAll();
				s.setDesc(newDesc);
				SelectLabel descLabel = new SelectLabel();
				descLabel.setOpaque(true);
				descLabel.setBorder(null);
				descLabel.setFont(Constants.descFont);
				descLabel.setText(newDesc);
				sDescPanel.add(new JSeparator(SwingConstants.HORIZONTAL),BorderLayout.NORTH);
				JScrollPane dljsp = new JScrollPane(descLabel);
				dljsp.setBorder(null);
				sDescPanel.add(dljsp,BorderLayout.CENTER);
				sDescPanel.add(new JSeparator(SwingConstants.HORIZONTAL),BorderLayout.SOUTH);
				sDescPanel.revalidate();
				sDescPanel.repaint();
				if (Constants.toggleOptions.get("Auto Zip Topic"))
					Constants.zipTopic(getID(),self,false);
				else
					setPublished(false);
			}
		});
		saveButton.setSize(descPanelSize.width,saveHeight);
		Constants.setFixedSize(saveButton,new Dimension(descPanelSize.width,saveHeight));
		sDescPanel.add(saveButton,BorderLayout.SOUTH);
		sDescPanel.revalidate();
		sDescPanel.repaint(); 
	}
	public boolean checkIsAuthor()
	{
		return isAuthor;
	}
	public ArrayList<String[]> getAnnotations()
	{
		return annotations;
	}
	public boolean getPublic()
	{
		return isPublic;
	}
	public void setPublished(boolean b)
	{
		published = b;
		publishButt.setText((published) ? "Published":"Publish");
		publishButt.setEnabled(!published);
	}
	public boolean getPublished()
	{
		return published;
	}
	public boolean getFull()
	{
		return isFull;
	}
	public boolean checkFull()
	{
		try {
			int maxTSize = Constants.integerOptions.get("maxTSize");
			curSize = Constants.getFolderSize(mainDir.getPath());
			if (maxTSize != 0)
			{
				long maxSize = (long) (maxTSize*Math.pow(1024,3));
				isFull = (curSize >= maxSize);
			}
			else
				isFull = false;
			System.out.println("'"+title+"' is now "+Constants.getHRFileSize(curSize));
			return isFull;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public String getCurRelType()
	{
		return curRelType;
	}
	public void setCurRelType(String s)
	{
		curRelType = s;
	}
	public long getCurSize()
	{
		return curSize;
	}
	public void updateAnnotations()
	{
		annoBox.update();
		if (Constants.toggleOptions.get("'Ding!' on Annotation"))
		{
			AudioInputStream audioStream;
			try {
				String dingPath = "aud"+File.separator+"ding.wav";
				InputStream raw = getClass().getResourceAsStream(dingPath);
				BufferedInputStream buffered = new BufferedInputStream(raw);
				audioStream = AudioSystem.getAudioInputStream(buffered);
			    Clip clip = AudioSystem.getClip();
			    clip.open(audioStream);
			    clip.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
