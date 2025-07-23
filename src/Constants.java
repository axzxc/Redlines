import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.cef.CefApp;

import com.google.gson.Gson;

public interface Constants {
	//Listener Arrays
	NetSender[] ns = new NetSender[1];
	NetListener[] nl = new NetListener[1];
	// String Arrays
	String[] accInfo = new String[3];
	String[] myNode = new String[2];
	String[] ipInfo = new String[2];
	String[] helpPages = {"redlines_main","add_p_source_top","add_p_source_file","add_s_source_link","topicAuth","topicClient","showSource_aud","showSource_img","showSource_link","showSource_pdf","showSource_vid","settings","settingsAccount","settingsPreferences","settingsData","settingsConn"};
	String[] topicIntContents = {"sources", "sources/S", "sources/P", "topic.nfo", "related.nfo"};
	String[] relationTypes = {
	        "Other","Background", "Follow-up", "Contrasting View",
	        "Similar Issue", "Same Author", "Expanded Analysis",
	        "Case Study", "Legislation/Policy", "Debunked Claim", "Updates"
    };

	// Strings
	String sysFolder = "sys";
	String arrSeparator = "#S#";
	String fontName = "Monospaced";
	String keyStr = "78d2abffc613f8b354448f0cd1c6c6d8";
	static String masterList = sysFolder+File.separator+".mlst.li";
	String masterKey = "8nE3w+c7Zc5GUJrY+n5kBXtTfgn2/D3lCfHJVJctCBkpSieopHBL2ZwHfNwSa7zQ9mVoZjymJSlfngN6eSSts6c0xRune0cBmYMdJPP/o7Dw/L2szyTVaPOj1H3k0X2J2ivCxaSluwgFID5EGzZcR8HlrOuwWEA3wbl8YG0sgXD6uekTz8XbNSBaaaWrGElqo1h69MK8LKzO1J9uF3tAbxepszCZKA6+eggpTJcT0Vb+7GdIunzB73X9zc1BWKkpcdk+LT5n3y7NF+lp92J2Vw==";
	String NLSeparator = "#NL#";
	String os = System.getProperty("os.name").toUpperCase();
	String separator = "#AX#";
	String topicPath = "topics";
	String annoPath = "annotations";
	String bootInfo = sysFolder+File.separator+".bootinfo";
	String settingsPath = sysFolder+File.separator+".sett";
	String CIDSeparator = "#:#";
	

	//byte[]
	byte[] key = keyStr.getBytes();
	
	//Integer Arrays
	int[] topicDim = {700, 395};
	int[] rlFrameSize = {450,525};
	int[] popupSize = {350,400};
	
	// Integers
	int addButtHeight = topicDim[1] / 12;
	int commentsWidth = topicDim[0] / 4;
	int imgScale = 30;
	int imgSize = 50;
	int switchCardHeight = topicDim[1] / 10;
	int topicHeadHeight = topicDim[1] / 6;
	int topicIDLength = 7;
	int[] PSRPanelDim = {(topicDim[0] - commentsWidth), (topicDim[1] - topicHeadHeight - switchCardHeight - addButtHeight)};
	static int defaultPort = 45333;
	static int timeout = 5000;
	int commentsSaveSec = 10;
	
	//timestamps
	long[] publicTimer = new long[1]; 
	
	//Sets
	static Map<String,Node> nodeList = new HashMap<String,Node>();
	static Map<String, Boolean> toggleOptions = new HashMap<>();
	static Map<String, Integer> integerOptions = new HashMap<>();
	
	// ArrayLists
	static ArrayList<String> masters = new ArrayList<String>();
	static ArrayList<String> topicList = new ArrayList<String>();
	static ArrayList<Topic> topics = new ArrayList<Topic>();
	ArrayList<Node> presentNodes = new ArrayList<Node>();

	// Dimensions
	Dimension topicListButtSize = new Dimension(75, 25);

	// Fonts
	Font descFont = new Font(fontName, Font.PLAIN, 15);
	Font labelFont = new Font(fontName, Font.BOLD, 15);
	Font titleFont = new Font(fontName, Font.BOLD, 30);
	Font valueFont = new Font(fontName, Font.PLAIN, 15);
	
	//Colors
	//Color mainBG = new Color(245, 235, 220, 255);
	Color mainBG = new Color(46, 46, 46);
	Color mainFG = new Color(60, 60, 60);
	Color buttonColor = new Color(100, 40, 40);
	Color subButtonColor = new Color(180, 90, 30);
	Color textColorDM = new Color(230, 230, 230);
	Color textColorLM = new Color(25, 25, 25);
	Color annoBG = new Color(70, 70, 70);
	
	// JFrames
	JFrame settingsFrame = new JFrame("Settings");

	// Layouts
	CardLayout settingsCL = new CardLayout();
	
	// JPanels
	JPanel accountSetPanel = new JPanel();
	JPanel connectionsSetPanel = new JPanel();
	JPanel dataManageSetPanel = new JPanel();
	JPanel preferencesSetPanel = new JPanel();
	JPanel settingsCardPanel = new JPanel(settingsCL);
	JPanel settingsHomePanel = new JPanel();
	static JPanel tlPanel = new JPanel();
	JPanel mainSettingsPanel = new JPanel(new BorderLayout());
	
	//CopyLabels
	CopyLabel IPVal = new CopyLabel("");
	CopyLabel CIDVal = new CopyLabel("");
	
	//InputFields
	InputTextField portField = new InputTextField(myNode[0],text -> {
    	System.out.println("Updating Port to "+text);
    	myNode[0] = text;
    	updateCID(true);
    });
	
	//JLabels
	JLabel totalSpaceUsedLabel = new JLabel();
	
	//booleans
	boolean[] settingsInitalized = {false};
	boolean[] darkMode = {false};
	
	//browser
	CefApp[] browserApp = new CefApp[1];
	
	//debug
	DebugFrame[] df = new DebugFrame[1];
	
	//Frames
	JFrame[] rlFrame = new JFrame[1];
	public static void setFixedSize(JComponent component, Dimension size) {
	    component.setPreferredSize(size);
	    component.setMaximumSize(size);
	    component.setMinimumSize(size);
	}
	public static HashMap<String,String> grabIPInfo(String IP)
	{
		//{country=US, loc=39.0834-78.2181, hostname=c-73-216-123-224.hsd1.va.comcast.net, city=Stephens City, org=AS7922 Comcast Cable Communications LLC, timezone=America/New_York, ip=73.216.123.224, postal=22655, readme=https://ipinfo.io/missingauth, region=Virginia}
		HashMap<String,String> ipInfo = new HashMap<String,String>();
		String ipCommand;
		if (os.contains("WIN")) {
            ipCommand = "curl";
        } else if (os.contains("LINUX")) {
            ipCommand = "curl";
        } else if (os.contains("MAC")) {
            ipCommand = "curl";
        } else {
            throw new RuntimeException("Unsupported OS");
        }
		try {
			Process process = Runtime.getRuntime().exec(ipCommand+" ipinfo.io/"+IP);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null)
			{
				line = line.trim();
				if (line.contains(":"))
				{
					String[] lineArr = line.split(":", 2);
					for (int i=0;i<lineArr.length;i++)
					{
						lineArr[i] = lineArr[i].trim();
						lineArr[i] = lineArr[i].replaceAll("\"","");
						lineArr[i] = lineArr[i].replaceAll(",","");
					}
					ipInfo.put(lineArr[0],lineArr[1]);
				}
			}
			//System.out.println(ipInfo);
			return ipInfo;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void setDarkMode(Container parentContainer,boolean isset) {
		darkMode[0] = isset;
		Color fg = darkMode[0] ? textColorDM : textColorLM;
	    applyTextColorRecursive(parentContainer, fg);
	    parentContainer.repaint();
	}

	private static void applyTextColorRecursive(Container container, Color fg) {
	    for (Component comp : container.getComponents()) {
	        comp.setForeground(fg);
	        if (comp instanceof IButton || comp instanceof RoundedIButt)
	        	((IButton)comp).setDark(darkMode[0]); 
	        if (comp instanceof Container) {
	            applyTextColorRecursive((Container) comp, fg); // Recurse into children
	        }
	    }
	    container.setForeground(fg);
	}
	public static boolean prompt(JFrame t,String title,String message)
	{
		JDialog dialog = ((t == null) ? new JDialog(rlFrame[0],title,true):new JDialog(t,title,true));
		final boolean[] result = {false};
		JPanel mainPanel = new JPanel(new BorderLayout());
		JTextField messageField = new JTextField(message);
		messageField.setFont(descFont);
		messageField.setEditable(false);
		mainPanel.add(messageField, BorderLayout.CENTER);
		JButton okButt = new JButton("OK");
	    okButt.addActionListener(e -> {
	        result[0] = true;
	        dialog.dispose();
	    });
	    JButton cancelButt = new JButton("Cancel");
	    cancelButt.addActionListener(e -> dialog.dispose());
	    JPanel buttPanel = new JPanel(new GridLayout(1, 2));
	    buttPanel.add(okButt);
	    buttPanel.add(cancelButt);
	    mainPanel.add(buttPanel, BorderLayout.SOUTH);
	    dialog.getContentPane().add(mainPanel);
	    dialog.pack();
	    dialog.setLocationRelativeTo(null);
	    dialog.setVisible(true); // blocks until dialog is closed
	    return result[0]; 
	}
	public static boolean prompt(JFrame t,String title,String message,String yesText, String noText)
	{
		JDialog dialog = ((t == null) ? new JDialog(rlFrame[0],title,true):new JDialog(t,title,true));
		final boolean[] result = {false};
		JPanel mainPanel = new JPanel(new BorderLayout());
		JTextField messageField = new JTextField(message);
		messageField.setFont(descFont);
		messageField.setEditable(false);
		mainPanel.add(messageField, BorderLayout.CENTER);
		JButton okButt = new JButton(yesText);
	    okButt.addActionListener(e -> {
	        result[0] = true;
	        dialog.dispose();
	    });
	    JButton cancelButt = new JButton(noText);
	    cancelButt.addActionListener(e -> dialog.dispose());
	    JPanel buttPanel = new JPanel(new GridLayout(1, 2));
	    buttPanel.add(okButt);
	    buttPanel.add(cancelButt);
	    mainPanel.add(buttPanel, BorderLayout.SOUTH);
	    dialog.getContentPane().add(mainPanel);
	    dialog.pack();
	    dialog.setLocationRelativeTo(null);
	    dialog.setVisible(true); // blocks until dialog is closed
	    return result[0]; 
	}
	public static boolean deleteFile(String path)
	{
		File fileToDelete = new File(path);
		if (fileToDelete.exists())
		{
			if (fileToDelete.delete()) {
	            return true;
	        } else {
	            System.out.println("Failed to delete '"+path+"'.");
	        }
		}
		else
			System.out.println("'"+path+"' Not Found!");
		return false;
	}
	public static boolean deleteFolder(String path) {
		File folderToDelete = new File(path);
		if (folderToDelete.exists())
		{
	        File[] allContents = folderToDelete.listFiles();
	        if (allContents != null) {
	            for (File file : allContents) {
	            	deleteFolder(file.getPath()); 
	            }
	        }
		}
		else
			System.out.println("'"+path+"' Not Found!");
        return folderToDelete.delete();
    }
	public static Optional<String> getPublicIPAddress() {
        String[] services = {
            "https://checkip.amazonaws.com",
            "https://ipv4.icanhazip.com",
            "https://api.ipify.org"
        };

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        for (String service : services) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(service))
                        .timeout(Duration.ofSeconds(5))
                        .build();
                
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() == 200) {
                    String ip = response.body().trim();
                    // Validate IPv4 format
                    if (ip.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$")) {
                        return Optional.of(ip);
                    }
                }
            } catch (Exception ignored) {}
        }
        return Optional.empty();
    }
	public static Optional<String> getInternalIPAddress() {
        // Try socket connection method first (preferred for default route)
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("8.8.8.8", 53), 1000);
            InetAddress localAddr = socket.getLocalAddress();
            if (!localAddr.isAnyLocalAddress() && !localAddr.isLoopbackAddress() 
                    && localAddr instanceof Inet4Address) {
                return Optional.of(localAddr.getHostAddress());
            }
        } catch (IOException ignored) {}

        // Fallback to network interface enumeration
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp() || iface.isVirtual()) continue;
                
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        return Optional.of(addr.getHostAddress());
                    }
                }
            }
        } catch (SocketException ignored) {}
        return Optional.empty();
    }
	public static String getIP(boolean justIP) throws IOException
	{
		String ipCommand;
		boolean isPublic = toggleOptions.get("Go Public");
		if (os.contains("WIN")) {
            ipCommand = ((isPublic) ? "curl":"ipconfig");
        } else if (os.contains("LINUX")) {
            ipCommand = ((isPublic) ? "curl":"ifconfig");
        } else if (os.contains("MAC")) {
            ipCommand = ((isPublic) ? "curl":"ifconfig");
        } else {
            throw new RuntimeException("Unsupported OS");
        }
		Process process = Runtime.getRuntime().exec(ipCommand+((isPublic) ? " ipinfo.io/json":""));
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		if (isPublic)
		{
			ArrayList<String[]> ipInfo = new ArrayList<String[]>();
			while ((line = reader.readLine()) != null)
			{
				line = line.trim();
				if (line.contains(":"))
				{
					String[] lineArr = line.split(":");
					for (int i=0;i<lineArr.length;i++)
					{
						lineArr[i] = lineArr[i].trim();
						lineArr[i] = lineArr[i].replaceAll("\"","");
						lineArr[i] = lineArr[i].replaceAll(",","");
					}
					ipInfo.add(lineArr);
				}
			}
			//if i need all the info from this json curl ill get it later
			String[][] ipInfoArr = ipInfo.toArray(new String[ipInfo.size()][2]);
			if (!justIP)
			{
				String out = "";
				for (String[] D1 : ipInfoArr)
				{
					for (int i=0;i<D1.length;i++)
						out+=D1[i]+((i!=0) ? "":NLSeparator);
					out+=separator;
				}
				//ystem.out.println(out);
				return out;
			}
			return ipInfoArr[0][1];
		}
		else
		{
			String IP = null;
			while ((line = reader.readLine()) != null)
			{
				line = line.trim();
				if (os.contains("WIN"))
				{
					if (line.contains("IPv4 Address"))
					{
						IP = line.split(":")[1].trim();
						break;
					}
				}
				else if (os.contains("LINUX"))
				{
					if (line.contains("inet"))
					{
						IP = line.split(" ")[1].trim();
						break;
					}
				}
				else if (os.contains("MAC"))
				{
					//macfag lol
					IP="0.0.0.0";
				}
			}
			return IP;
		}
	}
	public static boolean login(String CID,JFrame rlFrame)
	{
		boolean successful = false;
		try
		{
			File credFile = new File(sysFolder+File.separator+".cred");
			if (!credFile.exists())
			{
				JFrame signUpFrame = new JFrame("Create Account");
				signUpFrame.setIconImage(ResourceLoader.loadIcon("img/logo.png"));
				signUpFrame.setResizable(false);
				signUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				String[] fields = {"Username","Password","Verify Password"};
				JPanel mainPanel = new JPanel(new BorderLayout());
				JPanel subPanel = new JPanel(new GridLayout(fields.length,2));
				for (String field : fields)
				{
					JLabel fieldLabel = new JLabel(field);
					fieldLabel.setFont(descFont);
					subPanel.add(fieldLabel);
					if (field.toUpperCase().contains("PASSWORD"))
						subPanel.add(new JPasswordField(10));
					else
						subPanel.add(new JTextField(10));
				}
				mainPanel.add(subPanel,BorderLayout.CENTER);
				JButton saveButton = new JButton("Save");
				saveButton.setFont(descFont);
				saveButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String out = "";
						String password = null;
						for (Component compField : subPanel.getComponents())
						{
							String value = null;
							if (compField instanceof JPasswordField)
							{
								JPasswordField passField = (JPasswordField)compField;
								value = new String(passField.getPassword()).strip().trim();
								if (password != null)
								{
									if (!value.equals(password))
									{
										passField.setText("");
										return;
									}
									else
										continue;
								}
								else
									password = value;
							}
							else if (compField instanceof JTextField)
							{
								value = ((JTextField)compField).getText().strip().trim();
								accInfo[0] = value;
							}
							else if (compField instanceof JLabel)
								continue;
							if (value.equals(""))
								return;
							out += value+separator;
						}
						try {
							out += CID;
							accInfo[1] = CID;
							BufferedWriter writer = new BufferedWriter(new FileWriter(credFile.getPath(), true));
							//System.out.println(out);
							writer.write(EncryptionDecryption.encrypt(out,key));
							writer.close();
						} catch (Exception e1) {
							e1.printStackTrace();
							System.out.println("error saving cred");
						}
						signUpFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						signUpFrame.dispose();
						startRL(rlFrame);
					}
				});
				mainPanel.add(saveButton,BorderLayout.SOUTH);
				signUpFrame.add(mainPanel);
				signUpFrame.pack();
				signUpFrame.setVisible(true);
			}
			else
			{
				BufferedReader br = new BufferedReader(new FileReader(credFile));
				String line;
				line = br.readLine();
				if (line!=null)
					line = EncryptionDecryption.decrypt(line.strip().trim(),key);
				String[] credArr = line.split(separator);
				br.close();
				JFrame signInFrame = new JFrame("Login");
				signInFrame.setIconImage(ResourceLoader.loadIcon("img/logo.png"));
				signInFrame.setResizable(false);
				signInFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JPanel mainPanel = new JPanel(new BorderLayout());
				JPanel subPanel = new JPanel(new GridLayout(2,2));
				JLabel usernameLabel = new JLabel("Username:");
				usernameLabel.setFont(descFont);
				subPanel.add(usernameLabel);
				JTextField userField = new JTextField(10);
				subPanel.add(userField);
				JLabel passwordLabel = new JLabel("Password");
				passwordLabel.setFont(descFont);
				subPanel.add(passwordLabel);
				JPasswordField passField = new JPasswordField(10);
				passField.addKeyListener(new KeyListener() {
					@Override
					public void keyPressed(KeyEvent e) 
					{
						if (e.getKeyCode() == 10)
						{
							if ((userField.getText().trim().strip().equals("") || passField.getPassword().length == 0) || (!userField.getText().trim().strip().equals(credArr[0]) || !(new String(passField.getPassword()).trim().strip()).equals(credArr[1])))
							{
								userField.setText("");
								passField.setText("");
								return;
							}
							accInfo[0] = credArr[0]; //username
							accInfo[1] = CID; //CID
							signInFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
							signInFrame.dispose();
							startRL(rlFrame);
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
				subPanel.add(passField);
				mainPanel.add(subPanel,BorderLayout.CENTER);
				JButton loginButt = new JButton("Login");
				loginButt.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if ((userField.getText().trim().strip().equals("") || passField.getPassword().length == 0) || (!userField.getText().trim().strip().equals(credArr[0]) || !(new String(passField.getPassword()).trim().strip()).equals(credArr[1])))
						{
							userField.setText("");
							passField.setText("");
							return;
						}
						accInfo[0] = credArr[0]; //username
						accInfo[1] = CID; //CID
						signInFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
						signInFrame.dispose();
						startRL(rlFrame);
					}
				});
				mainPanel.add(loginButt,BorderLayout.SOUTH);
				signInFrame.add(mainPanel);
				signInFrame.pack();
				signInFrame.setVisible(true);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return successful;
	}
	public static String distillText(String text)
	{
		text = text.replaceAll(NLSeparator,"\n");
		Set<String> stopWords = new HashSet<>(Arrays.asList("a", "an", "the", "is", "are", "was", "were", "am", "be", "been", "being","and", "or", "but", "if", "then", "else", "in", "on", "at", "to", "for","with", "of", "by", "from", "about", "as", "into", "like", "through","after", "over", "between", "out", "against", "during", "without", "before","under", "around", "among"));
		List<String> resultWords = new ArrayList<>();
		if (text == null) return null;
        // Normalize and split into words
        String[] words = text.toLowerCase().replaceAll("[^a-z0-9\\n ]", "").split(" ");
        //String[] words = text.toLowerCase().replaceAll("[^a-z0-9 ]", "").split("\\s+");
        for (String word : words) {
            if (!stopWords.contains(word) && !word.isEmpty()) {
                resultWords.add(word);
            }
        }
		return String.join(" ", resultWords).replaceAll("\n",NLSeparator);
	}
	private static void startRL(JFrame rlFrameObj)
	{
		rlFrame[0] = rlFrameObj;
		rlFrame[0].setVisible(true);
		rlFrame[0].revalidate();
		rlFrame[0].repaint();
	}
	public static String filterMessage(String dirtyMessage) {
        if (dirtyMessage == null) return "";
        return dirtyMessage.replaceAll("\\r?\\n", NLSeparator);
    }
	public static void openSettings()
	{
		//settingsFrame.removeAll();
		String[] settingOptions = {"Account","Preferences","Data Management","Connections"};
		if (!settingsInitalized[0])
		{
			settingsFrame.setIconImage(ResourceLoader.loadIcon("img/logo.png"));
			JButton settingsTitleButt = new JButton("Settings");
			settingsTitleButt.setAlignmentX(Component.CENTER_ALIGNMENT);
			settingsTitleButt.setFont(titleFont);
			settingsTitleButt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					settingsCL.show(settingsCardPanel,"settingsHomePanel");
					Component visiblePanel = null;

					for (Component comp : settingsCardPanel.getComponents()) {
					    if (comp.isVisible()) {
					        visiblePanel = comp;
					        break;
					    }
					}
					if (visiblePanel != null) {
					    Dimension preferred = visiblePanel.getPreferredSize();
					    settingsCardPanel.setPreferredSize(new Dimension(preferred.width+20,preferred.height+20));						settingsCardPanel.revalidate();
						settingsCardPanel.repaint();
						settingsFrame.setMinimumSize(null);
						settingsFrame.pack();
						//settingsFrame.setSize(preferred.width + widthMargin, preferred.height + heightMargin);
						settingsFrame.revalidate();
						settingsFrame.repaint();
					}
				}
			});
			mainSettingsPanel.add(settingsTitleButt,BorderLayout.NORTH);
			settingsHomePanel.setLayout(new BoxLayout(settingsHomePanel, BoxLayout.Y_AXIS));
			int padding = 10;
			settingsCardPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));
			for (String settingOption : settingOptions)
			{
				JButton SOButt = new JButton(settingOption);
				setFixedSize(SOButt,new Dimension(300,35));
				SOButt.setFont(descFont);
				SOButt.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switchSettingsPanel(settingOption,key);						
					}
				});
				settingsHomePanel.add(SOButt);
			}
			settingsHomePanel.setName("settingsHomePanel");
			settingsCardPanel.add(settingsHomePanel,"settingsHomePanel");
			mainSettingsPanel.add(settingsCardPanel,BorderLayout.CENTER);
			settingsFrame.add(mainSettingsPanel);
			settingsFrame.pack();
			settingsFrame.addWindowListener(new java.awt.event.WindowAdapter() {
	            @Override
	            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	            	saveSettings(settingsFrame);
	            }
	        });
			settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			settingsInitalized[0] = true;
			SwingUtilities.updateComponentTreeUI(settingsFrame);
		}
		else 
		{
			Dimension preferred = settingsHomePanel.getPreferredSize();
			settingsCL.show(settingsCardPanel,"settingsHomePanel");	
		    settingsCardPanel.setPreferredSize(new Dimension(preferred.width+20,preferred.height+20));						settingsCardPanel.revalidate();
			settingsCardPanel.repaint();
			settingsFrame.setMinimumSize(null);
			settingsFrame.pack();
			//settingsFrame.setSize(preferred.width + widthMargin, preferred.height + heightMargin);
			settingsFrame.revalidate();
			settingsFrame.repaint();
		}
		SwingUtilities.updateComponentTreeUI(settingsFrame);
		settingsFrame.setResizable(false);
		settingsFrame.setVisible(true);
	}
	private static void switchSettingsPanel(String settingsOption,byte[] key)
	{
		settingsCL.show(settingsCardPanel,settingsOption);
		Component[] comps = settingsCardPanel.getComponents();
		Component visiblePanel = null;
		boolean cardFound = false;
		for (Component comp : comps)
		{
			if (comp.isVisible() && comp.getName().equals(settingsOption))
			{
				cardFound = true;
				break;
			}
		}
		if (!cardFound)
		{
			JPanel subPanel;
			JScrollPane jsp;
			switch(settingsOption)
			{
				case "Account":
					//setup panel
					//accountSetPanel.setLayout(new BoxLayout(accountSetPanel,BoxLayout.Y_AXIS));
					accountSetPanel.setLayout(new BorderLayout());
					String[] labels = {"Username: ","Password: "};//maybe add age
					subPanel = new JPanel(new GridLayout(labels.length,2,5,5));
					subPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
					for (String label : labels)
					{
						JLabel tmpLabel = new JLabel(label);
						JLabel valLabel = new JLabel("");
						valLabel.setFont(valueFont);
						tmpLabel.setFont(labelFont);
						switch(label)
						{
							case "Username: ":
								valLabel.setText(accInfo[0]);
								break;
							case "Password: ":
								valLabel.setText("********");
								break;
						}
						subPanel.add(tmpLabel);
						subPanel.add(valLabel);
					}
					jsp = new JScrollPane(subPanel);
					accountSetPanel.add(jsp,BorderLayout.CENTER);
					JButton editButton = new JButton("Edit");
					editButton.setFont(labelFont);
					editButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							JButton editButton = (JButton)e.getSource();
							boolean editing = (!editButton.getText().equals("Edit"));
							JScrollPane jsp = (JScrollPane) accountSetPanel.getComponents()[0];
							JPanel subPanel = (JPanel) jsp.getViewport().getView();
							Component[] oldLabels = subPanel.getComponents();
							String newPass = "";
							for (int i=0;i<oldLabels.length;i++)
							{
								if (i>0 && ((JLabel)oldLabels[i-1]).getText().equals("Password: "))
								{
									if (editing)
									{
										JPasswordField oldField = (JPasswordField)oldLabels[i];
										newPass = new String(oldField.getPassword());
										JLabel newLabel = new JLabel("********");
										subPanel.remove(oldField);
										subPanel.add(newLabel, i);
									}
									else
									{	
										JTextField newField =new JPasswordField("");
										subPanel.remove(oldLabels[i]);
										subPanel.add(newField, i);
									}
								}
							}
							if (editing)
								updateAccInfo(newPass);
							editButton.setText((editing) ? "Edit":"Save");
							accountSetPanel.revalidate();
							accountSetPanel.repaint();
						}
					});
					accountSetPanel.add(editButton,BorderLayout.SOUTH);
					accountSetPanel.setName(settingsOption);
					settingsCardPanel.add(accountSetPanel,settingsOption);
					break;
				case "Preferences":
					preferencesSetPanel.setLayout(new BoxLayout(preferencesSetPanel, BoxLayout.Y_AXIS));
					TitledBorder border = BorderFactory.createTitledBorder("Preferences");
					border.setTitleColor(textColorDM);
					border.setTitleFont(descFont);
					preferencesSetPanel.setBorder(border);
					for (String option : toggleOptions.keySet()) {
			            JCheckBox toggle = new JCheckBox(option);
			            toggle.setBackground(mainBG);
			            toggle.setFont(descFont);
			            toggle.setForeground(textColorDM);
			            toggle.setAlignmentX(Component.LEFT_ALIGNMENT);
			            toggle.setSelected(toggleOptions.get(option));
			            toggle.addItemListener(e -> {
			                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
			                toggleOptions.put(option, selected);
			                //String[] toggleOptions = { "Allow Gossip","Go Public","Notify on " };
			                switch (option) {
				                case "Allow Gossip":
				                    toggleGossip(selected);
				                    break;
				                case "Go Public":
				                	togglePublic(selected);
				                	toggle.setEnabled(false);
				                    Timer timer = new Timer(30000, evt -> {
				                    	toggle.setEnabled(true);
				                    });
				                    timer.setRepeats(false);  // Only run once
				                    timer.start();
				                	break;
				                case "'Ding!' on Annotation":
				                	toggleAnnoDing(selected);
				                	break;
				                case "Debug Output":
				                	toggleDebugOutput(selected);
				                	break;
				                default:
				                    System.out.println("Unhandled toggle: " + option);
				            }
			                saveSettings(null);
			            });
			            preferencesSetPanel.add(toggle);
					}
					preferencesSetPanel.setName(settingsOption);
					settingsCardPanel.add(preferencesSetPanel,settingsOption);
					break;
				case "Data Management":
					dataManageSetPanel.setLayout(new BorderLayout());
					try
					{
						JPanel mainPanel = new JPanel();
						mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
						mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
						totalSpaceUsedLabel.setFont(descFont);
						totalSpaceUsedLabel.setText("Total Used Space: "+getHRFileSize(getFolderSize(topicPath)));
						totalSpaceUsedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
						mainPanel.add(totalSpaceUsedLabel);
						JButton exportButt = new JButton("Export");
						exportButt.addActionListener(e -> {
							try {
								JFileChooser fileChooser = new JFileChooser();
								fileChooser.setDialogTitle("Export Topics To...");
								String zipFilePath = accInfo[0]+".tc";
								fileChooser.setSelectedFile(new File(zipFilePath));
								fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Topic Files (*.tc)", "tc"));
								int userSelection = fileChooser.showSaveDialog(null);
								if (userSelection == JFileChooser.APPROVE_OPTION) {
						            File fileToSave = fileChooser.getSelectedFile();
						            // Ensure .tc extension
						            if (!fileToSave.getName().toLowerCase().endsWith(".tc")) {
						                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".tc");
						            }

						            try {
						            	Path[] zipDirs = new Path[] {Paths.get(topicPath)};
						            	ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(fileToSave), StandardCharsets.UTF_8);
										for (Path sourceDir : zipDirs)
										{
											Files.walk(sourceDir).forEach(path -> {
												try {
													String entryName = sourceDir.getFileName().toString() + "/" + sourceDir.relativize(path).toString().replace("\\", "/");
													ZipEntry zipEntry = new ZipEntry(entryName + (Files.isDirectory(path) ? "/" : ""));
								                    zipOut.putNextEntry(zipEntry);
			
								                    if (!Files.isDirectory(path)) { // Only copy file contents, directories are just entries
								                        Files.copy(path, zipOut);
								                    }
								                    zipOut.closeEntry();
								                } catch (IOException ex) {
								                    ex.printStackTrace();
								                }
											});
										}
										JOptionPane.showMessageDialog(null, "File saved to:\n" + fileToSave.getAbsolutePath());
						            } catch (IOException ex) {
						                JOptionPane.showMessageDialog(null, "Error saving file:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						            }
						        }
					    	} catch (Exception e1) {
								e1.printStackTrace();
							}
						});
						exportButt.setFont(descFont);
						exportButt.setAlignmentX(Component.LEFT_ALIGNMENT);
						mainPanel.add(Box.createVerticalStrut(15));
						mainPanel.add(exportButt);
						JButton importButt = new JButton("Import");
						importButt.setFont(descFont);
						importButt.addActionListener(e -> {
							JFileChooser fileChooser = new JFileChooser();
						    fileChooser.setDialogTitle("Import Topics From...");
						    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Topic Files (*.tc)", "tc"));

						    int userSelection = fileChooser.showOpenDialog(null);
						    if (userSelection != JFileChooser.APPROVE_OPTION) return;

						    File fileToOpen = fileChooser.getSelectedFile();
						    if (!fileToOpen.getName().toLowerCase().endsWith(".tc")) {
						        JOptionPane.showMessageDialog(null, "Invalid file type. Please select a .tc file.");
						        return;
						    }

						    try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(fileToOpen), StandardCharsets.UTF_8)) {
						        ZipEntry entry;

						        while ((entry = zipIn.getNextEntry()) != null) {
						            if (entry.isDirectory()) {
						                zipIn.closeEntry();
						                continue;
						            }

						            String entryName = entry.getName().replace("\\", "/");
						            Path topicPath = Paths.get(Constants.topicPath);
						            Path outputDir;

						            String[] parts = entryName.split("/", 2);
						            if (parts.length < 2) {
						                zipIn.closeEntry();
						                continue;
						            }
						            outputDir = topicPath.resolve(parts[1]);

						            // Make sure parent dirs exist
						            Files.createDirectories(outputDir.getParent());

						            // Extract file
						            try (OutputStream out = Files.newOutputStream(outputDir)) {
						                zipIn.transferTo(out);
						            }

						            zipIn.closeEntry();
						        }

						        JOptionPane.showMessageDialog(null, "Import completed successfully!");

						    } catch (IOException ex) {
						        JOptionPane.showMessageDialog(null, "Error importing file:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						        ex.printStackTrace();
						    }
						    refreshTLPanel();
						});
						importButt.setAlignmentX(Component.LEFT_ALIGNMENT);
						mainPanel.add(Box.createVerticalStrut(5));
						mainPanel.add(importButt);
						/*JPanel autoArchivePanel = new JPanel(new GridLayout(1,2));
						JLabel autoArchiveLabel = new JLabel("Auto Archive after _ days: ");
						autoArchiveLabel.setToolTipText("0/blank for it not to auto archive");
						autoArchivePanel.add(autoArchiveLabel);
						JTextField autoArchiveJTF = new JTextField(5);
						autoArchiveJTF.setFont(descFont);
						autoArchivePanel.add(autoArchiveJTF);
						mainPanel.add(autoArchivePanel);*/
						JPanel maxTopicGBPanel = new JPanel(new GridLayout(1,2));
						JLabel maxTopicGBLabel = new JLabel("Max Topic Size (GB): ");
						maxTopicGBLabel.setToolTipText("0/blank for unlimited");
						maxTopicGBLabel.setFont(descFont);
						maxTopicGBPanel.add(maxTopicGBLabel);
						InputTextField maxTopicGBJTF = new InputTextField(integerOptions.get("maxTSize").toString(),text -> {
					    	System.out.println("Updating Max Topic Size to "+text+"GB");
					    	integerOptions.put("maxTSize",Integer.parseInt(text));
					    });
						maxTopicGBPanel.add(maxTopicGBJTF);
						maxTopicGBPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
						mainPanel.add(Box.createVerticalStrut(15));
						mainPanel.add(maxTopicGBPanel);
						JButton clearTopics = new JButton("Clear all Topics");
						clearTopics.setFont(descFont);
						clearTopics.addActionListener(e -> {
							File topics = new File(topicPath);
							File[] files = topics.listFiles();
							if (files != null) {
						        for (File file : files) {
						            if (file.isFile()) {
						                deleteFile(file.getPath());
						            } else if (file.isDirectory()) {
						                deleteFolder(file.getPath()); // Assumes this deletes folder and its contents
						            }
						        }
						        refreshTLPanel();
						    }
						});
						clearTopics.setAlignmentX(Component.LEFT_ALIGNMENT);
						mainPanel.add(Box.createVerticalStrut(10));
						mainPanel.add(clearTopics);
						/*JButton DMSButt = new JButton("Set DMS");
						DMSButt.setFont(descFont);
						DMSButt.setToolTipText("!CAUTION! Set Dead Man's Switch");
						mainPanel.add(DMSButt);*/
						jsp = new JScrollPane(mainPanel); 
						dataManageSetPanel.add(jsp);
						dataManageSetPanel.setName(settingsOption);
						settingsCardPanel.add(dataManageSetPanel,settingsOption);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					break;
				case "Connections":
					connectionsSetPanel.setLayout(new BorderLayout());

					try {
					    subPanel = new JPanel(new GridBagLayout());
					    GridBagConstraints gbc = new GridBagConstraints();
					    gbc.insets = new Insets(5, 5, 5, 5);  // Padding
					    
					    JPanel CIDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
						JLabel CIDLabel = new JLabel("CID: ");
						CIDLabel.setFont(descFont);
					    CIDPanel.add(CIDLabel);
					    CIDVal.setText(accInfo[1]);
					    CIDVal.setFont(descFont);
					    CIDPanel.add(CIDVal);
					    gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
					    subPanel.add(CIDPanel, gbc);
					    
					    JPanel portPanel = new JPanel(new GridLayout(1,2));
					    // Port Field
					    JLabel portLabel = new JLabel("Port: ");
					    portLabel.setOpaque(false);
					    portLabel.setFont(descFont);
					    portPanel.add(portLabel);
					    portField.setText(myNode[0]);
					    portField.setOpaque(false);
					    portField.setFont(descFont);
					    portPanel.add(portField);
					    gbc.gridy++;
					    subPanel.add(portPanel, gbc);

					    // IP Field
					    JPanel IPPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
					    JLabel IPLabel = new JLabel("IP: ");
					    IPLabel.setFont(descFont);
					    IPPanel.add(IPLabel);
					    IPVal.setText(myNode[1]);
					    IPVal.setFont(descFont);
					    IPPanel.add(IPVal);
					    gbc.gridy++;
					    subPanel.add(IPPanel, gbc);

					    // CID Input
					    JPanel subCIDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
					    JLabel cidLabel = new JLabel("Add Node by CID:");
					    cidLabel.setFont(descFont);
					    subCIDPanel.add(cidLabel);

					    JTextField cidField = new JTextField(20);
					    cidField.setFont(descFont);
					    subCIDPanel.add(cidField);

					    // Add Node Button
					    JButton addNodeButton = new JButton("Submit");
					    addNodeButton.setFont(descFont);
					    addNodeButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String newCID = cidField.getText().trim();
								if (!newCID.equals(""))	
									addCID(cidField.getText(),false);
								cidField.setText("");
								
							}
					    });
					    subCIDPanel.add(addNodeButton);
					    gbc.gridy++;
					    subPanel.add(subCIDPanel, gbc);

					    // Import / Export Buttons
					    JPanel ioButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
					    JButton importButton = new JButton("Import Node List");
					    importButton.addActionListener(e -> {
					    	System.out.println("import");
					    	JFileChooser fileChooser = new JFileChooser();
					        fileChooser.setDialogTitle("Import Node List From...");
					        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Node List Files (*.nl)", "nl"));
					        int userSelection = fileChooser.showOpenDialog(null);
					        if (userSelection == JFileChooser.APPROVE_OPTION) {
					            File selectedFile = fileChooser.getSelectedFile();

					            // Optional: verify extension just in case
					            if (selectedFile.getName().toLowerCase().endsWith(".nl")) {
					                try {
										BufferedReader br = new BufferedReader(new FileReader(selectedFile));
										String line = br.readLine();
										br.close();
										if (line == null || line.equals(""))
											return;
										line = line.trim();
										String[] cidArr = EncryptionDecryption.decrypt(line,key).split(separator);
										for (String cid : cidArr)
											addCID(cid,true);
										saveNodeList();
									} catch (Exception e1) {
										e1.printStackTrace();
									}
					            } else {
					                JOptionPane.showMessageDialog(null, "Please select a .nl file.", "Invalid file", JOptionPane.WARNING_MESSAGE);
					            }
					        }
					    });
					    JButton exportButton = new JButton("Export Node List");
					    exportButton.addActionListener(e -> {
					    	System.out.println("export");
					    	try {
					    		String out = String.join(separator, nodeList.keySet());
					    		out = accInfo[1]+separator+out;
								String payload = EncryptionDecryption.encrypt(out,key);
								JFileChooser fileChooser = new JFileChooser();
								fileChooser.setDialogTitle("Export Node List To...");
								fileChooser.setSelectedFile(new File(accInfo[0]+".nl"));
								fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Node List Files (*.nl)", "nl"));
								int userSelection = fileChooser.showSaveDialog(null);
								if (userSelection == JFileChooser.APPROVE_OPTION) {
						            File fileToSave = fileChooser.getSelectedFile();
						            // Ensure .nl extension
						            if (!fileToSave.getName().toLowerCase().endsWith(".nl")) {
						                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".nl");
						            }

						            try (FileWriter writer = new FileWriter(fileToSave)) {
						                writer.write(payload);
						                JOptionPane.showMessageDialog(null, "File saved to:\n" + fileToSave.getAbsolutePath());
						            } catch (IOException ex) {
						                JOptionPane.showMessageDialog(null, "Error saving file:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						            }
						        }
					    	} catch (Exception e1) {
								e1.printStackTrace();
							}
					    	
					    });
					    importButton.setFont(descFont);
					    exportButton.setFont(descFont);
					    ioButtonPanel.add(importButton);
					    ioButtonPanel.add(exportButton);
					    gbc.gridy++;
					    subPanel.add(ioButtonPanel, gbc);

					    // View Node List Button
					    JPanel cvButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
					    JButton viewNodeListButton = new JButton("View Node List");
					    viewNodeListButton.setFont(descFont);
					    viewNodeListButton.addActionListener(e -> {
					        JFrame nlFrame = new JFrame(accInfo[0] + "'s Node List");
					        nlFrame.setIconImage(ResourceLoader.loadIcon("img/logo.png"));
					        JPanel mainPanel = new JPanel(new BorderLayout());

					        JButton refreshNodeListButton = new JButton("Refresh Node List");
					        refreshNodeListButton.setFont(descFont);
					        mainPanel.add(refreshNodeListButton, BorderLayout.NORTH);

					        JPanel nodeListPanel = new JPanel();
					        nodeListPanel.setLayout(new BoxLayout(nodeListPanel, BoxLayout.Y_AXIS));
					        nodeListPanel.add(Box.createVerticalStrut(10));
					        try {
					            for (Map.Entry<String, Node> nodeMap : nodeList.entrySet()) {
					                Node node = nodeMap.getValue();
					                JButton ipLabel = new JButton(node.getIP());
					                ipLabel.setFont(descFont);
					                ipLabel.setToolTipText(EncryptionDecryption.encrypt(node.getCID(), key));
					                ipLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
					                ipLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
					                ipLabel.addActionListener(new ActionListener() {
										@Override
										public void actionPerformed(ActionEvent e) {
											boolean canConnect = ns[0].sendPing(node);
											JOptionPane.showMessageDialog(
												    null,     
												    "Node '"+node.getIP()+"' on Port "+node.getPort()+" Ping "+((canConnect) ? "Successful!":"Failed!"),  
												    "Ping",      
												    JOptionPane.INFORMATION_MESSAGE // icon type (INFO, ERROR, WARNING, etc.)
											);
										}
					                });
					                nodeListPanel.add(ipLabel);
					            }
					        } catch (Exception e2) {
					            e2.printStackTrace();
					        }

					        JScrollPane scrollPane = new JScrollPane(nodeListPanel);
					        mainPanel.add(scrollPane, BorderLayout.CENTER);

					        refreshNodeListButton.addActionListener(ev -> {
					            ns[0].reqNodes();
					            nlFrame.dispose();  // Will reopen via original button
					        });

					        nlFrame.setContentPane(mainPanel);
					        nlFrame.pack();
					        nlFrame.setLocationRelativeTo(null);
					        nlFrame.setVisible(true);
					    });
					    cvButtonPanel.add(viewNodeListButton);
					    JButton deleteButton = new JButton("Clear Node List");
					    deleteButton.setFont(descFont);
					    deleteButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (prompt(settingsFrame,"Clear Node List","Are you sure you want to clear the node list? This is irreversible."))
									clearNodeList();
							}					    
						});
					    cvButtonPanel.add(deleteButton);
					    gbc.gridy++;
					    subPanel.add(cvButtonPanel, gbc);

					    // Add to parent panel
					    connectionsSetPanel.add(subPanel, BorderLayout.NORTH);
					    connectionsSetPanel.setName(settingsOption);
					    settingsCardPanel.add(connectionsSetPanel, settingsOption);

					} catch (Exception e) {
					    e.printStackTrace();
					}
					break;			
			}
			settingsCL.show(settingsCardPanel,settingsOption);
		}
		SwingUtilities.updateComponentTreeUI(settingsFrame);
		for (Component comp : settingsCardPanel.getComponents()) {
		    if (comp.isVisible()) {
		        visiblePanel = comp;
		        break;
		    }
		}
		if (visiblePanel != null) {
		    Dimension preferred = visiblePanel.getPreferredSize();
		    settingsCardPanel.setPreferredSize(new Dimension(preferred.width+20,preferred.height+20));
			settingsCardPanel.revalidate();
			settingsCardPanel.repaint();
			settingsFrame.setMinimumSize(null);
			settingsFrame.pack();
			//settingsFrame.setSize(preferred.width + widthMargin, preferred.height + heightMargin);
			settingsFrame.revalidate();
			settingsFrame.repaint();
		}
	}
	private static void toggleDebugOutput(boolean isSet)
	{
		if (isSet)
			df[0] = new DebugFrame();
		else
			df[0].dispose();
	}
	private static void toggleGossip(boolean isSet)
	{
		System.out.println("Gossip "+(isSet ? "Enabled":"Disabled"));
	}
	private static void togglePublic(boolean isSet)
	{
		long now = System.currentTimeMillis();
		if (now - publicTimer[0] >= 30000)
		{
			updateCID(true);
			System.out.println("CID: "+accInfo[1]);
			System.out.println("IP: "+myNode[1]);
			publicTimer[0] = now;
		}
		else
			System.out.println("Please wait at least 30 seconds.");
		
	}
	private static void toggleAnnoDing(boolean isSet)
	{
		
	}
	public static void shutdown()
	{
		UPnPPortMapper upMapper = nl[0].getMapper();
		if (upMapper != null)
			upMapper.shutdown();
	}
	public static String updateCID(boolean setNets)
	{
		String CID = null;
		String newIP = ipInfo[((toggleOptions.get("Go Public")) ? 1:0)];
		try {
			myNode[1] = newIP;
			CID = EncryptionDecryption.encrypt(String.join(CIDSeparator, myNode),key);
			accInfo[1] = CID;
			IPVal.setText(myNode[1]);
			CIDVal.setText(accInfo[1]);
			portField.setText(myNode[0]);
			connectionsSetPanel.repaint();
			connectionsSetPanel.revalidate();
        	BufferedWriter bw = new BufferedWriter(new FileWriter(new File(bootInfo)));
        	bw.write(EncryptionDecryption.encrypt(accInfo[2],key)+"\n");
        	bw.write(CID);
        	bw.close();
        	if (setNets)
        	{
	        	ns[0].setCID(CID);
	        	nl[0].reinitialize();
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CID;
	}
	private static void addCID(String newCID,boolean delaySave)
	{
		String message = "Failed to add CID!";
		if (addToNodeList(new Node(newCID),delaySave))
		{
			try {
				String IP = EncryptionDecryption.decrypt(newCID,key).split(CIDSeparator)[1];
				message = "Successfully added CID for '"+IP+"'!";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JOptionPane.showMessageDialog(null, message, "Add CID Status", JOptionPane.INFORMATION_MESSAGE);
	}
	public static void addAnnotation(String topicID,String[] annotation)
	{
		Topic thisTopic = getTopic(topicID);
		ArrayList<String[]> annotations = thisTopic.getAnnotations();
		String messageID = "";
		boolean found; 
		do 
		{
			found = false; 
			messageID = randID(4); 
			for (String[] a : annotations)
			{
				if (a[5].equals(messageID))
				{
					found = true;
					break;
				}
			}
		}
		while(found);
		annotation = Arrays.copyOf(annotation, annotation.length + 1); 
		annotation[annotation.length - 1] = messageID;
		String dbPath = annoPath + File.separator + topicID + ".db";
		try 
		{
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
			Statement stmt = conn.createStatement();
		    //stmt.executeUpdate("DELETE FROM annotations");
	        stmt.execute("CREATE TABLE IF NOT EXISTS annotations (id INTEGER PRIMARY KEY, username TEXT, cid TEXT, uid TEXT, message TEXT, timestamp TEXT, messageID TEXT)");
	        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO annotations (username, cid, uid, message, timestamp, messageID) VALUES (?, ?, ?, ?, ?, ?)");
	        for (int i=0; i < annotation.length;i++)
	        	pstmt.setString((i+1),annotation[i]);
	        pstmt.executeUpdate();
	        pstmt.close();
	        stmt.close();
	        conn.close();
	        thisTopic.updateAnnotations();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static long getFolderSize(String folderPath) throws IOException {
        Path folder = Paths.get(folderPath);
        try (Stream<Path> stream = Files.walk(folder)) {
            return stream.filter(Files::isRegularFile) // Only count files
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            System.err.println("Error getting size of " + p + ": " + e.getMessage());
                            return 0; // Or handle the error as needed
                        }
                    })
                    .sum();
        }
    }
	private static boolean updateAccInfo(String newPass)
	{
		String out = accInfo[0]+separator+newPass+separator+accInfo[1];
		File credFile = new File(sysFolder+File.separator+".cred");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(credFile));
			//System.out.println(out);
			bw.write(EncryptionDecryption.encrypt(out,key));
			bw.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public class SettingsWrapper {
		Map<String, Boolean> toggleOptions = new HashMap<>();
	    Map<String, Integer> integerOptions = new HashMap<>();
	}
	public static void loadSettings()
	{
		File sFile = new File(settingsPath);
		Gson gson = new Gson();
        try {
        	if (!sFile.exists())
    		{
    			sFile.createNewFile();
    			//add defaults
    			toggleOptions.put("Allow Gossip", true);
    			toggleOptions.put("Debug Output", false);
    		    toggleOptions.put("Go Public", false);
    		    toggleOptions.put("'Ding!' on Annotation", false);
    		    toggleOptions.put("Auto Zip Topic",false);
    		    integerOptions.put("maxTSize",0);
    			saveSettings(null);
    			return;
    		}
        	FileReader reader = new FileReader(sFile);
           // Map<String, Boolean> loadedMap = gson.fromJson(reader, toggleOptions.getClass());
        	SettingsWrapper wrapper = gson.fromJson(reader, SettingsWrapper.class);
            if (wrapper != null) {
                toggleOptions.clear();
                toggleOptions.putAll(wrapper.toggleOptions);
                integerOptions.clear();
                integerOptions.putAll(wrapper.integerOptions);
            }
        } catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void saveSettings(JFrame settingsFrame)
	{
		System.out.println("Saving Settings");
		SettingsWrapper wrapper = new SettingsWrapper();
		wrapper.toggleOptions = toggleOptions;
		wrapper.integerOptions = integerOptions;
		try(FileWriter writer = new FileWriter(settingsPath)) {
			Gson gson = new Gson();
			gson.toJson(wrapper, writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (settingsFrame != null)
			settingsFrame.dispose();
	}
	public static void saveNodeList()
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(masterList))) {
			String out = "";
			for (Map.Entry<String, Node> nodeMap : nodeList.entrySet())
			{
				Node node = nodeMap.getValue();
				out += node.toString()+separator;
			}
			writer.write(EncryptionDecryption.encrypt(out,key));
            writer.flush(); // Ensures data is written to the file
            writer.close();
        } catch (IOException e) {
            System.out.println("An IO error occurred: " + e.getMessage());
        } catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateHash(String input) {
	    try {
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
	        
	        // Fast hex conversion
	        char[] hexArray = "0123456789abcdef".toCharArray();
	        char[] hexChars = new char[hashBytes.length * 2];
	        for (int j = 0; j < hashBytes.length; j++) {
	            int v = hashBytes[j] & 0xFF;
	            hexChars[j * 2] = hexArray[v >>> 4];
	            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	        }
	        return new String(hexChars);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	public static void refreshTLPanel()
	{
		tlPanel.removeAll();
		topicList.clear();
		File topDir = new File(topicPath);
        if (topDir.exists() && topDir.isDirectory())
        {
            File[] files = topDir.listFiles();
            if (files != null)
                for (File file : files)
                    if (file.isDirectory())
                    	topicList.add(file.getName());
        }
		tlPanel.setLayout(new BoxLayout(tlPanel,BoxLayout.Y_AXIS));
        File topInd = new File(topicPath+File.separator+"topic.ind");
        try {
	        if (topInd.exists())
	        {
	        	if (topInd.length() != 0)
	        	{
		        	BufferedReader br = new BufferedReader(new FileReader(topInd));
		        	String line;
		        	while ((line = br.readLine()) != null) {
		            	String[] topArr = line.split(separator);
		                for (String topicID : topicList)
		                {
		                	if (topicID.equals(topArr[0]))
		                	{
								IButton tmpTopButt = (topArr[5].equals("1")) ? new IButton("globe.png",topArr[3]):new IButton("private.png",topArr[3]);
								tmpTopButt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
								tmpTopButt.setPreferredSize(new Dimension(0, 40));
								tmpTopButt.setBackground(buttonColor);
								tmpTopButt.setFont(descFont.deriveFont(Font.BOLD));
								tmpTopButt.setAlignmentX(Component.CENTER_ALIGNMENT);
								tmpTopButt.addActionListener(e -> {
									getTopic(topicID).reShow();
								});
								tmpTopButt.setWide();
								String descTip;
								int FLDescIndex = topArr[6].indexOf(NLSeparator);
								if (FLDescIndex != -1) 
									descTip = topArr[6].substring(0, FLDescIndex);
								else 
									descTip = topArr[6];
								tmpTopButt.setToolTipText(descTip);
								tlPanel.add(tmpTopButt);
		                	}
		                }
		            }
		        	br.close();
	        	}
	        	else
	        	{
	        		JLabel noTopics = new JLabel("No Topics Yet!");
	        		noTopics.setMaximumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
	        		noTopics.setAlignmentX(Component.CENTER_ALIGNMENT);
	        		noTopics.setHorizontalAlignment(SwingConstants.CENTER);
	        		noTopics.setVerticalAlignment(SwingConstants.CENTER);
	        		noTopics.setFont(descFont);
	        		tlPanel.add(noTopics);	
		       	}
	        }
	        else
	        	updateTopicInd();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tlPanel.revalidate();
        tlPanel.repaint();
		try {
			totalSpaceUsedLabel.setText("Total Used Space: "+getHRFileSize(getFolderSize(topicPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String generateHash(File file)
	{
        try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        FileInputStream fis = new FileInputStream(file);
	        byte[] buffer = new byte[1024];
	        int n;
	        while ((n = fis.read(buffer)) != -1) {
	            digest.update(buffer, 0, n);
	        }
	        fis.close();
	        byte[] hashBytes = digest.digest();
	        StringBuilder sb = new StringBuilder();
	        for (byte b : hashBytes) {
	            sb.append(String.format("%02x", b));
	        }
	        return sb.toString();
        }
        catch (Exception e)
        {
        	e.printStackTrace();
			return null;
        }
    }
	public static String randID(int length)
	{
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder result = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
	}
	public static String getHRFileSize(long bytes) {
        if (bytes < 0) return "Invalid size"; 
        String[] units = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};
        double size = bytes;
        int unitIndex = 0;
        while (size >= 1024 && unitIndex < units.length - 1)
        {
            size /= 1024;
            unitIndex++;
        }
        return String.format("%.2f (%s)", size, units[unitIndex]);
    }
	public static String sanitizeUrl(String url) {
        String regex = "[\\\\/:*?\"<>|]";
        return url.replaceAll(regex, "_");
    }
	public static String getExtension(File file)
	{
		String ext = null;
		ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		return ext;
	}
	private static String[] updateReturnedTopics(String[] oldReturnedTopics, String[] freshList)
	{
		ArrayList<String> newReturnedTopics = new ArrayList<String>();
		if (oldReturnedTopics == null)
			oldReturnedTopics = new String[0];
		for (String oldReturnedTopic : oldReturnedTopics)
			newReturnedTopics.add(oldReturnedTopic);
		for (String freshTopic : freshList)
		{
			boolean found = false;
			for (String oldReturnedTopic : oldReturnedTopics)
			{
				if (freshTopic.split(separator)[0].equals(oldReturnedTopic.split(separator)[0]))
				{
					found = true;
					break;
				}
			}
			for (String curTopic : topicList)
			{
				if (freshTopic.split(separator)[0].equals(curTopic))
				{
					found = true;
					break;
				}
			}
			if (!found)
				newReturnedTopics.add(freshTopic);
		}
		if (newReturnedTopics.size() == 0)
			return null;
		return newReturnedTopics.toArray(new String[newReturnedTopics.size()]);
	}
	public static void zipTopic(String topicID,Component parentComponent,boolean startingTopic)
	{
		TopicZipper.zipTopicWithProgress(topicID,parentComponent,startingTopic);
	}

	public static void extractZip(String zipFilePath) {
	    byte[] buffer = new byte[4096];
	    try {
	        File zipFile = new File(zipFilePath);
	        String destDir = zipFile.getParent() + File.separator + zipFile.getName().replaceFirst("[.][^.]+$", ""); // Remove extension

	        File dir = new File(destDir);
	        if (!dir.exists()) dir.mkdirs();

	        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
	        ZipEntry zipEntry = zis.getNextEntry();
	        while (zipEntry != null) {
	            File newFile = new File(destDir, zipEntry.getName());

	            if (zipEntry.isDirectory()) {
	                newFile.mkdirs();
	            } else {
	                new File(newFile.getParent()).mkdirs(); // Ensure parent directories exist
	                try (FileOutputStream fos = new FileOutputStream(newFile)) {
	                    int len;
	                    while ((len = zis.read(buffer)) > 0) {
	                        fos.write(buffer, 0, len);
	                    }
	                }
	            }
	            zipEntry = zis.getNextEntry();
	        }
	        zis.closeEntry();
	        zis.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	public static void updateTopicInd()
	{
		File topDir = new File(topicPath);
        if (topDir.exists() && topDir.isDirectory())
        {
			try
			{
	        	BufferedWriter writer = new BufferedWriter(new FileWriter(topicPath+File.separator+"topic.ind"));
	            File[] files = topDir.listFiles();
	            if (files != null)
	            {
	                for (File file : files)
	                {
	                	//System.out.println(file.getAbsolutePath());
						String out = "";
						String topicID = null;
	                    if (file.isDirectory())
	                    {
	                    	File topicInfo = new File(file.getPath()+File.separator+"topic.nfo");
	                    	BufferedReader br = new BufferedReader(new FileReader(topicInfo));
	                    	String line; 
	                    	int lineIndex = 0;
	                    	while (((line = br.readLine()) != null))
	                    	{		
	                    		line = line.trim();
	                    		if (topicID == null)
	                    			topicID = line;
	                    		if (lineIndex == 6 || lineIndex == 3)
	                    		{
	                    			//System.out.println("Before Distill: "+line);
	                    			line = distillText(line); //distill title&description
	                    			//System.out.println("After Distill: "+line);
	                    		}
	                    		out += (((out.equals("")) ? "":separator)+line);
	                    		lineIndex++;
	                    	}
	                    	br.close();
	                    }
	                    //if (!out.equals("")) writer.write(out+separator+generateHash(new File(topicPath+File.separator+topicID+".zip"))+"\n");
	                    if (!out.equals(""))
	                    	writer.write(out+"\n");
	                }
	            }
				writer.close();
				refreshTLPanel();
			} catch (IOException e) {
				e.printStackTrace();
			}      	
        }
	}
	public static void dumpComponentTree(Component comp, int depth) {
	    StringBuilder indent = new StringBuilder();
	    for (int i = 0; i < depth; i++) indent.append("  ");

	    String name = (comp.getName() != null) ? comp.getName() : "(unnamed)";
	    String bounds = "[" + comp.getX() + "," + comp.getY() + "," + comp.getWidth() + "," + comp.getHeight() + "]";
	    boolean opaque = (comp instanceof JComponent) && ((JComponent) comp).isOpaque();
	    System.out.println(indent + comp.getClass().getSimpleName() + " " + bounds + " opaque=" + opaque + " - " + name);

	    if (comp instanceof Container) {
	        Component[] children = ((Container) comp).getComponents();
	        for (Component child : children) {
	            dumpComponentTree(child, depth + 1);
	        }
	    }
	}

	public static void editLine(String filePath, int lineNumberToEdit, String newLine) {
        try {
            // 1. Read all lines from the file
            List<String> fileContent = Files.readAllLines(Paths.get(filePath));

            // Adjust line number for 0-based indexing
            if (lineNumberToEdit > 0 && lineNumberToEdit <= fileContent.size()) {
                int indexToEdit = lineNumberToEdit - 1;

                // 2. Modify the desired line
                fileContent.set(indexToEdit, newLine);

                // 3. Write back to the file
                Files.write(Paths.get(filePath), fileContent);

                System.out.println("Line " + lineNumberToEdit + " in " + filePath + " updated successfully.");

            } else {
                System.err.println("Invalid line number: " + lineNumberToEdit + " for file: " + filePath);
            }

        } catch (IOException e) {
            System.err.println("Error processing the file: " + e.getMessage());
        }
    }
	public static void removeLine(String filePath, int lineNumberToRemove) {
        List<String> fileContent = new ArrayList<>();
        BufferedReader reader = null;
        FileWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String currentLine;
            int lineNumber = 0;

            while ((currentLine = reader.readLine()) != null) {
                if (lineNumber != lineNumberToRemove) {
                    fileContent.add(currentLine);
                }
                lineNumber++;
            }

            writer = new FileWriter(filePath);
            for (String line : fileContent) {
                writer.write(line + System.lineSeparator());
            }

            //System.out.println("Line " + lineNumberToRemove + " removed successfully from " + filePath);

        } catch (IOException e) {
            System.err.println("Error processing the file: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing reader/writer: " + e.getMessage());
            }
        }
    }
	public static Node grabNode(String nCID)
	{
		if (!nodeList.containsKey(nCID))
			return new Node(nCID);
		else
			return nodeList.get(nCID);
	}
	public static String[] searchNodes(String[] inquiry,JFrame rlMain)
	{
		ProgressDialog pd = new ProgressDialog(rlMain,"Searching Nodes",nodeList.size());
		pd.show();
		String[] returnedTopics = null;
		presentNodes.clear();
		int pdIndex = 0;
		for (Map.Entry<String, Node> nodeMap : nodeList.entrySet())
		{
			Node node = nodeMap.getValue();
			try {
				if (ns[0].sendPing(node))
				{
					String[] freshList = ns[0].sendInquiry(node,inquiry);
					if (freshList != null)
					{
						returnedTopics = updateReturnedTopics(returnedTopics,freshList);
						presentNodes.add(node);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			pdIndex++;
			final int progress = pdIndex;
			//final String nodeIP = node.getIP();
            SwingUtilities.invokeLater(() -> {
            	//pd.setText("Searching from "+nodeIP);
            	pd.setProgress(progress);
        	});
		}
		pd.finish();
		return returnedTopics;
	}
	public static boolean addToNodeList(Node newNode,boolean delaySave)
	{
		if (String.join(CIDSeparator,myNode).equals(newNode.toString()))
			return false;
		boolean found = false;
		for (Map.Entry<String, Node> nodeMap : nodeList.entrySet())
		{
			Node node = nodeMap.getValue();
			if (node.equals(newNode))
			{
				found = true;
				break;
			}
		}
		if (!found)
		{
			nodeList.put(newNode.getCID(),newNode);
			if (!delaySave)
				saveNodeList();
		}
		return !found;
	}
	public static void clearNodeList()
	{
		nodeList.clear();
		saveNodeList();
	}
	public static Topic[] search(String[] inquiry,String CID)
	{
		Topic[] foundTopics = null;
		ArrayList<Topic> foundTopicsAL = new ArrayList<Topic>();
		//phase 1 inner search
		try 
		{
        	BufferedReader br = new BufferedReader(new FileReader(topicPath+File.separator+"topic.ind"));
        	String topicLine;
        	boolean found = false;
        	String topicID = null;
        	while (((topicLine = br.readLine()) != null))
        	{
	        	String[] topicArr = topicLine.split(separator);
	        	if (topicArr.length<3)
	        		continue;
	        	topicID = null;
	        	for (String wordQuery : inquiry)
				{
	        		for (String wordTopic : topicArr)
	        		{
	        			if (topicID == null)
	        				topicID = wordTopic;
	        			if (wordQuery.toUpperCase().contains(wordTopic.toUpperCase()) || wordTopic.toUpperCase().contains(wordQuery.toUpperCase()))
	        			{
	        				found = true;
	        				addUnique(foundTopicsAL,new Topic(new File(topicPath+File.separator+topicID+File.separator+"topic.nfo"),false));
	        			}
	        		}
				}
        	}
        	br.close();
        	if (found)
        	{
        		//proceed to node search by requesting from each node topic.ind and add to foundTopicsAL
        		//if node topic is found initiate download into ./topics directory. if ID folder name conflicts change.
        		foundTopics = foundTopicsAL.toArray(new Topic[foundTopicsAL.size()]);
        	}
        	else
        	{
        		return null;
        	}
        	
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return foundTopics;
	}
	public static void addUnique(ArrayList<Topic> topicList, Topic topic)
	{
		boolean isUnique = true;
	    for (Topic existingTopic : topicList) {
	        if (existingTopic.getID().equals(topic.getID())) {
	            isUnique = false;
	            break;
	        }
	    }
	    if (isUnique)
	        topicList.add(topic);
	}
	public static Topic getTopic(String topicID)
	{
		boolean found = false;
		Topic t = null;
		for (Topic tmpT : topics)
		{
			if (tmpT.getID().equals(topicID))
			{
				t = tmpT;
				found = true;
				break;
			}
		}
		if (!found)
			t = new Topic(topicID,false);
		return t;
	}
	public static void updateDesc(Source s, String desc)
	{
		try (FileWriter writer = new FileWriter(s.path+".desc")) { 
            writer.write(desc); 
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
	}
	public static String formatTimestamp(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        return formatter.format(date);
    }
}
