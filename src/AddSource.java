import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddSource extends JFrame {
	private String topicID,url,title,desc;
	private long fileSize;
	private boolean isPrimary,isTopic;
	private boolean isFile = true;
	private JPanel mainPanel,FLPanel;
	private JPanel FLCon = new JPanel(new BorderLayout());
	private CardLayout cl = new CardLayout();
	private CardLayout FLCL = new CardLayout();
	private File fileSource = null;
	private static JTextField linkTF = null;
	private JTextField titleField;
	private JTextArea descArea,sourceDesc;
	private Topic topicFrame;
	private int width = 500;
	private int height = 250;
	private String CID;
	private final AddSource self = this;
	public AddSource(String topicID, Topic topicFrame,boolean isPrimary) throws HeadlessException {
		this.topicFrame = topicFrame;
		if (topicFrame.getFull())
			return;
		this.isTopic = false;
		this.topicID = topicID;
		this.isPrimary = isPrimary;
		this.CID = topicFrame.getCID();
		this.setTitle("Add "+((isPrimary)?"Primary":"Secondary")+" Source");
		/*this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // Trigger the action here
            	//topicFrame.setTitle("Extended Frame Closed");
            	//topicFrame.reloadPanel();
            }
        });*/
		init();
	}
	public AddSource(boolean isPrimary) throws HeadlessException {
		this.isTopic = true;
		this.isPrimary = isPrimary;
		this.setTitle("Add "+((isPrimary)?"Primary":"Secondary")+" Source");
		this.CID = Constants.accInfo[1];
		init();
	}
	public void init()
	{
		this.setIconImage(ResourceLoader.loadIcon("img/logo.png"));
		this.setSize(width,height);
		this.setResizable(false);
		JPanel parentPanel = new JPanel(new BorderLayout());
		parentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel = new JPanel(cl);
		if (isTopic)
		{
			JPanel descPanel = new JPanel(new BorderLayout());
			JPanel titlePanel = new JPanel(new BorderLayout());
			JLabel topicTitle = new JLabel("Topic Title");
			topicTitle.setHorizontalAlignment(SwingConstants.CENTER);
			topicTitle.setFont(Constants.descFont);
			titlePanel.add(topicTitle,BorderLayout.NORTH);
			titleField = new JTextField();
			titleField.setDocument(new javax.swing.text.PlainDocument() {
			    @Override
			    public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
			        if (str == null) return;

			        int available = 40 - getLength();
			        if (available <= 0) return;

			        if (str.length() > available) {
			            str = str.substring(0, available);
			        }

			        super.insertString(offs, str, a);
			    }
			});

			titlePanel.add(titleField,BorderLayout.CENTER);
			descPanel.add(titlePanel,BorderLayout.NORTH);
			JPanel topicDescPanel = new JPanel(new BorderLayout());
			JLabel topicDesc = new JLabel("Topic Description");
			topicDesc.setHorizontalAlignment(SwingConstants.CENTER);
			topicDesc.setFont(Constants.descFont);
			topicDescPanel.add(topicDesc,BorderLayout.NORTH);
			descArea = new JTextArea(10,20);
			descArea.setWrapStyleWord(true);
			descArea.setLineWrap(true);
			descArea.setEditable(true);
			JScrollPane jsp = new JScrollPane(descArea);
			jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			topicDescPanel.add(jsp,BorderLayout.CENTER);
			descPanel.add(topicDescPanel);
			descPanel.setName("descPanel");
			mainPanel.add(descPanel,"descPanel");
		}
		JPanel addSourcePanel = new JPanel(new BorderLayout());
		JPanel sPanel = new JPanel(new BorderLayout());
		ButtonGroup rGroup = new ButtonGroup(); 
		JPanel rPanel = new JPanel(new GridLayout(1,2));
		JRadioButton rbFile = new JRadioButton("Upload File",true);
		rbFile.setOpaque(false);
		rbFile.addActionListener(e -> {
			FLCL.show(FLPanel,"file");
			isFile = true;
		});
		rbFile.setFont(Constants.descFont);
		rGroup.add(rbFile);
		rPanel.add(rbFile);
		JRadioButton rbLink = new JRadioButton("Source Link",false);
		rbLink.setOpaque(false);
		rbLink.addActionListener(e -> {
			FLCL.show(FLPanel,"link");
			isFile = false;
		});
		rbLink.setFont(Constants.descFont);
		rGroup.add(rbLink);
		rPanel.add(rbLink);
		sPanel.add(rPanel,BorderLayout.NORTH);
		FLPanel = new JPanel(FLCL);
		JPanel FPanel = new JPanel(new GridLayout(1,2,10,0));
		JLabel filenameLabel = new JLabel("None Selected");
		JFileChooser sourceChooser = new JFileChooser();
        JButton addFile = new JButton("Add File");
        addFile.addActionListener(e -> {
	        int result = sourceChooser.showOpenDialog(this);
	        if (result == JFileChooser.APPROVE_OPTION) {
	            fileSource = sourceChooser.getSelectedFile();
	            String filename = fileSource.getName();
	            filenameLabel.setText(filename);
	            int maxGB = Constants.integerOptions.get("maxTSize");
	            if (maxGB != 0)
	            {
		            long maxSizeBytes = Constants.integerOptions.get("maxTSize") * 1024L * 1024 * 1024;
		            if (topicFrame != null && ((fileSource.length()+topicFrame.getCurSize()) > maxSizeBytes))
		            {
		            	fileSource = null;
		            	JOptionPane.showMessageDialog(self,"'" + filename + "' is too large to add.", "'"+filename+"' too Large!", JOptionPane.INFORMATION_MESSAGE);
		            }
	            }
	        }
        });
        FPanel.add(addFile);
        FPanel.add(filenameLabel);
        FLPanel.add(FPanel,"file");
        JPanel LPanel = new JPanel(new GridLayout(1,1,10,0));
        linkTF = new JTextField();
        LPanel.add(linkTF);
        FLPanel.add(LPanel,"link");
        sPanel.add(FLPanel,BorderLayout.CENTER);
        addSourcePanel.add(sPanel,BorderLayout.NORTH);
		JPanel sourceDescPanel = new JPanel();
		sourceDescPanel.setLayout(new BoxLayout(sourceDescPanel, BoxLayout.Y_AXIS));
		JLabel SDLabel = new JLabel("Source Description:");
		SDLabel.setFont(Constants.descFont);
		sourceDescPanel.add(SDLabel);
		sourceDesc = new JTextArea();
		sourceDesc.setWrapStyleWord(true);
		JScrollPane jsp = new JScrollPane(sourceDesc);
		sourceDescPanel.add(jsp);
		addSourcePanel.add(sourceDescPanel,BorderLayout.CENTER);
		addSourcePanel.setName("addSourcePanel");
		mainPanel.add(addSourcePanel,"addSourcePanel");
		JPanel conPanel = new JPanel(new BorderLayout());
		conPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		conPanel.setName("conPanel");
		mainPanel.add(conPanel,"conPanel");
		parentPanel.add(mainPanel,BorderLayout.CENTER);
		JPanel navPanel = new JPanel(new GridLayout(1,2));
		JButton prevButton = new JButton("Previous");
		prevButton.addActionListener(e -> {
			JPanel curCard = null;
			for (Component comp : mainPanel.getComponents()) {
			    if (comp.isVisible() == true) {
			        curCard = (JPanel) comp;
			        break;
			    }
			}
			switch(curCard.getName())
			{
				case "descPanel":
					break;
				case "addSourcePanel":
					if (isTopic)
						cl.previous(mainPanel);
					break;
				default:
					cl.previous(mainPanel);
					break;
			}
		});
		navPanel.add(prevButton);
		JButton nextButton = new JButton("Next");
		nextButton.addActionListener(e -> {
			JPanel curCard = null;
			for (Component comp : mainPanel.getComponents()) {
				if (comp.isVisible() == true) {
			    	curCard = (JPanel)comp;
			    	break;
			    }
			}
			switch(curCard.getName())
			{
				case "descPanel":
					if (!titleField.getText().equals("") && !descArea.getText().equals(""))
						cl.next(mainPanel);
					break;
				case "addSourcePanel":
					if (((isFile&&fileSource!=null) || (!isFile&&isValidURL(linkTF.getText()))) && (!sourceDesc.getText().equals("")))
					{
						loadConPanel(conPanel);
						cl.next(mainPanel);
					}
					break;
				default:
					saveSource();
					break;
			}
		});
		navPanel.add(nextButton);
		parentPanel.add(navPanel,BorderLayout.SOUTH);
		add(parentPanel);
		//Constants.dumpComponentTree(this,0);
		this.setVisible(true);
		
	}
	public void loadConPanel(JPanel cp)
	{
		cp.removeAll();
		if (isTopic) {
			JPanel topicInfoPanel = new JPanel(new BorderLayout());
			title = titleField.getText();
			JLabel conTitle = new JLabel(title);
			conTitle.setFont(Constants.descFont.deriveFont(Font.BOLD,(Constants.descFont.getSize2D() + 4f)));
			topicInfoPanel.add(conTitle,BorderLayout.NORTH);
			desc = descArea.getText().replaceAll("\r\n?|\n", Constants.NLSeparator);
			String plDesc = desc.replaceAll(Constants.NLSeparator," ");
			JLabel conDesc = new JLabel((plDesc.length()>50) ? plDesc.substring(0,50):plDesc);
			conDesc.setFont(Constants.descFont.deriveFont(Font.BOLD | Font.ITALIC));
			conDesc.setToolTipText(desc);
			topicInfoPanel.add(conDesc,BorderLayout.CENTER);
			cp.add(topicInfoPanel,BorderLayout.NORTH);
		}
		FLCon.removeAll();
		if (isFile)
		{
			JPanel conFile = new JPanel(new GridLayout(2,1)); 
			JLabel filename = new JLabel(fileSource.getName());
			filename.setFont(Constants.descFont);
			filename.setHorizontalAlignment(SwingConstants.CENTER);
			filename.setAlignmentX(Component.CENTER_ALIGNMENT); 
			conFile.add(filename);
			fileSize = fileSource.length();
			JLabel fileSizeLabel = new JLabel("Size: "+Constants.getHRFileSize(fileSize));
			fileSizeLabel.setFont(Constants.descFont.deriveFont(Constants.descFont.getSize2D() - 2f));
			fileSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
			filename.setAlignmentX(Component.CENTER_ALIGNMENT); 
			conFile.add(fileSizeLabel);
			FLCon.add(conFile,BorderLayout.CENTER);
		}
		else
		{
			JPanel conLink = new JPanel(new GridLayout(2,1));
			url = linkTF.getText();
			JLabel statusLabel = new JLabel();
			statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
			statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
			statusLabel.setFont(Constants.descFont.deriveFont(Constants.descFont.getSize2D() + 2f));
			try {
		        URL urlObj = new URL(url);
		        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
		        connection.setRequestMethod("GET");
		        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
		        	statusLabel.setText("Status: OK");
		    } catch (IOException e) {
		    	statusLabel.setText("Status: Failed");
		    }
			conLink.add(statusLabel);
			JButton linkButt = new JButton("Open '"+url+"'");
			linkButt.setToolTipText(url);
			linkButt.addActionListener(e -> {
				try {
		            if (Desktop.isDesktopSupported()) {
		                Desktop desktop = Desktop.getDesktop();
		                if (desktop.isSupported(Desktop.Action.BROWSE))
		                    desktop.browse(new URI(url));
		            }
		            System.out.println("Desktop or browse action not supported on this system.");
		        } catch (Exception ex) {
		            System.err.println("Error opening URL: " + ex.getMessage());
		        }
			});
			conLink.add(linkButt);
			FLCon.add(conLink,BorderLayout.CENTER);
		}
		cp.add(FLCon,BorderLayout.CENTER);
		cp.repaint();
	}
	public void saveSource()
	{//create an index file that pairs the random topic ID to the topic searchables
		Source newSource = null;
		try
		{	
			File topicIDDir = null;
			if (isTopic)
			{
				boolean found = true;
				do
				{
					topicID = Constants.generateHash(CID+Constants.randID(Constants.topicIDLength));
					topicIDDir = new File(Constants.topicPath+"/"+topicID);
					found = topicIDDir.exists();
				}
				while (found);
				topicIDDir.mkdir();
				for (String path : Constants.topicIntContents)
				{
					File tmpPath = new File(topicIDDir+"/"+path);
					if (path.contains("."))
						tmpPath.createNewFile();
					else
						tmpPath.mkdir();
				}
				BufferedWriter nfoWriter = new BufferedWriter(new FileWriter(topicIDDir.getAbsolutePath()+"/topic.nfo", true));
				nfoWriter.write(topicID+"\n"+CID+"\n"+Constants.accInfo[2]+"\n"+title+"\n"+Constants.accInfo[0]+"\n"+(Constants.toggleOptions.get("Go Public") ? "1":"0")+"\n"+desc);
				nfoWriter.close();
			}
			else
			{
				topicIDDir = new File(Constants.topicPath+"/"+topicID);
			}
			String sourcePath = topicIDDir.getPath()+File.separator+"sources"+File.separator+((isPrimary) ? "P":"S");
			String sourceName = "";
			if (isFile)
			{
				try {
					sourceName = fileSource.getName().replaceAll(" ","_");
				    Path sourcePathObj = fileSource.toPath();
				    Path targetPathObj = Paths.get(sourcePath + File.separator + sourceName);
				    Files.copy(sourcePathObj, targetPathObj, StandardCopyOption.REPLACE_EXISTING);
				    newSource = new Source(targetPathObj.toFile(), sourceName, desc);
				} catch (IOException e) {
				    e.printStackTrace();
				    System.out.println("Error copying file: " + e.getMessage());
				}
			}
			else
			{
				String linkName;
				File lnkFile;
				boolean found = true;
				do
				{
					linkName = Constants.sanitizeUrl(url)+Constants.randID(5)+".lin";
					lnkFile = new File(sourcePath+File.separator+linkName);
					found = lnkFile.exists();
				}
				while (found);
				sourceName = linkName;
				lnkFile.createNewFile();
				try 
				{
				    // Execute the curl command
					Process process = Runtime.getRuntime().exec(new String[]{"curl", url});
					// Capture the output of the curl command
					InputStream inputStream = process.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					// Write the output to a file
					FileWriter fileWriter = new FileWriter(lnkFile);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					String line;
					bufferedWriter.write(url+"\n");
					while ((line = reader.readLine()) != null) {
					    bufferedWriter.write(line);
					    bufferedWriter.newLine();
					}
					// Close the streams
					bufferedWriter.close();
					reader.close();
					// Wait for the process to complete
					int exitCode = process.waitFor();
					if (exitCode == 0)
					    System.out.println("Curl output successfully written to " + lnkFile.getName());
					else
					    System.err.println("Curl command failed with exit code " + exitCode);
				} catch (IOException | InterruptedException e) {
				    e.printStackTrace();
				}
				newSource = new Source(lnkFile,url,desc);
			}
			FileWriter writer = new FileWriter(sourcePath+File.separator+sourceName+".desc",false);
			writer.write(sourceDesc.getText());
            writer.close();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		this.dispose();
		//topicFrame.pack();
		Topic t = (isTopic) ? (new Topic(topicID,true)):topicFrame;
		Constants.updateTopicInd();
		if (Constants.toggleOptions.get("Auto Zip Topic") || isTopic)
			Constants.zipTopic(t.getID(),t,isTopic);
		else
			t.setPublished(false);
		t.checkFull();
		if (!isTopic)
			t.addSource(newSource);
	}
	public static boolean isValidURL(String url) {
		boolean valid = true;
	    if (url == null || url.isEmpty()) {
	        valid = false;
	    }
	    else
	    {
		    String regex = "^(https?)://[\\w.-]+(?:\\.[\\w.-]+)+(?::\\d+)?(/[\\w\\-./?%&=()#]*)?$";
		    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		    valid = pattern.matcher(url).matches();
	    }
	    if (!valid)
	    {
	    	JOptionPane.showMessageDialog(null, "Invalid URL!");
	    	linkTF.setText("");
	    }
	    return valid;
	}

	public boolean checkPrimary()
	{
		return isPrimary;
	}
}
