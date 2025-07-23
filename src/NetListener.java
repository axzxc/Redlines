import java.awt.Window;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;

public class NetListener {
	private ArrayList<AdvSocket> socks = new ArrayList<AdvSocket>();
	private ServerSocket ss = null;
	private String CID;
	private byte[] key;
	private String myIP = null;
	private UPnPPortMapper upMapper;
	private Thread acceptThread = null;
	private boolean running = false;
	public NetListener()
	{
		this.key = Constants.key;
		reinitialize();
	}
	public UPnPPortMapper getMapper()
	{
		return upMapper;
	}
	public void reinitialize()
	{
		this.CID = Constants.accInfo[1];
		myIP = Constants.ipInfo[((Constants.toggleOptions.get("Go Public")) ? 1:0)];
		try {
			if (ss != null)
				ss.close();
			if (acceptThread != null)
			{
				running = false;
				acceptThread.join();
				acceptThread = null;
			}
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (Constants.toggleOptions.get("Go Public"))
		{
			try {
				upMapper = new UPnPPortMapper(3,Constants.timeout,Constants.ipInfo[0],Integer.parseInt(Constants.myNode[0]));//needs to know internal IP first.
				upMapper.addPortMapping(new PortMappingListener() {
			        @Override
			        public void onSuccess() {
			            System.out.println("Port mapping added successfully!");
			        }
	
			        @Override
			        public void onFailure(String reason) {
			            System.err.println("Port mapping failed: " + reason);
			        }
			    });
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			if (upMapper != null)
				upMapper.shutdown();
			upMapper = null;
		}
		boolean portInUse = false;
		boolean portChanged = false;
		int port = Integer.parseInt(Constants.myNode[0]);
		do {
			portInUse = false;
			try {
				ss = new ServerSocket(port);
				running = true;
				if (portChanged)
				{
					Constants.myNode[0] = Integer.toString(port);
					Constants.updateCID(false); //false means dont reinitalize this listener but because its calling the update from within no reinitalization
				}
				start();
			} catch (Exception e) {
				e.printStackTrace();
				portInUse = true;
				//dialog: port in use! retry on random port? can change in settings
				if (Constants.prompt(null, "Port "+Integer.toString(port)+" in use!", "Retry on random port? "))//dialog
				{
					portChanged = true;
					Random random = new Random();
					do 
					{
						port = random.nextInt(50000);
					}
					while(port < 40000);
				}
				else
					System.exit(0);
				System.out.println("Retrying on port: "+port);
			}
		}
		while(portInUse);
	}
	public void start()
	{
		if (acceptThread != null)
		{
			running = false;
			try {
				acceptThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			acceptThread = null;
		}
		acceptThread = new Thread(() -> {
			System.out.println("Starting Listening server on "+myIP+" Port: "+Integer.parseInt(Constants.myNode[0])+" Is Public?: "+Constants.toggleOptions.get("Go Public"));
			while (running)
			{
				Socket clientSocket = null;
				try
				{
					clientSocket = ss.accept();
					AdvSocket cs = new AdvSocket(clientSocket,Integer.parseInt(Constants.myNode[0]));
					if (clientSocket.isConnected() && !myIP.equals(clientSocket.getInetAddress().getHostAddress()))
					{
						socks.add(cs);
						InetAddress cInet = clientSocket.getInetAddress();
						System.out.println(cInet.getHostName()+" connected");
						PrintWriter out = cs.getPW();
						DataOutputStream DOS = cs.getDOS();
						BufferedReader in = cs.getBR();
						String request = in.readLine();
						System.out.println("Request In: "+request);
						if (!request.contains(Constants.separator))
							throw new Exception("Invalid Syntax!");
						String[] reqArr = request.split(Constants.separator);
						String sendersCID = reqArr[0];
						Constants.addToNodeList(new Node(sendersCID),false);
						String topicID;
						File topicZip;
						String hash = null;
						boolean hashFound = false;
						String hlPath = Constants.topicPath+File.separator+".hl";
						byte[] buffer;
						int bytesRead;
						long fileSize;
						switch (reqArr[1])
						{
							case "ping":
								//System.out.println("IM getting pinged!");
								out.println("Ping OK");
								break;
							case "close":
								socks.remove(cs);
								cs.closeAll();
								break;
							case "rn":
								String strOut = "";
								if (Constants.toggleOptions.get("Allow Gossip"))
								{
									for (Map.Entry<String, Node> nodeMap : Constants.nodeList.entrySet())
									{
										String nCID = nodeMap.getKey();
										strOut += nCID+Constants.separator;
									}
								}
								else
									strOut = CID;
								out.println(strOut);
								break;
							case "topicInd":
								//String content = Files.lines(Paths.get()).collect(Collectors.joining("#NL#")); // Join lines with a space
								ArrayList<String> foundTopics = new ArrayList<String>();
								String[] inquiry = reqArr[2].split(Constants.arrSeparator);
								File topicIndFile = new File(Constants.topicPath+File.separator+"topic.ind");
								if (!topicIndFile.exists())
								{
									out.println("NULL");
									break;
								}
								BufferedReader br = new BufferedReader(new FileReader(topicIndFile));
								boolean found = false;
								String line;
					        	while ((line = br.readLine()) != null)
					        	{
					        		line = line.strip();
					        		String[] lineArr = line.split(Constants.separator);
					        		if ((Constants.toggleOptions.get("Go Public") && lineArr[5].equals("0")) || (!Constants.toggleOptions.get("Go Public") && lineArr[5].equals("1")))
					        			continue;
					        		for (String wordQuery : inquiry)
					        		{
					        			if (line.toUpperCase().contains(wordQuery.toUpperCase()))
					        			{
					        				found = true;
					        				foundTopics.add(line);
					        			}
					        		}
					        	}
					        	br.close();
					        	if (!found)
					        		out.println("NULL");
					        	else
					        		out.println(String.join(Constants.arrSeparator,foundTopics));
								break;
							case "retTopic":
								out.println(CID);
								File topicInfo = new File(Constants.topicPath+File.separator+reqArr[2]+File.separator+"topic.nfo");
								FileInputStream fileIn = new FileInputStream(topicInfo);
								DOS.writeLong(topicInfo.length());
								buffer = new byte[4096];
						        bytesRead = 0;
						        while ((bytesRead = fileIn.read(buffer)) != -1) {
						        	DOS.write(buffer, 0, bytesRead);
						        }
						        fileIn.close();
						        //DOS.close();
								break;
							case "reqFromAuth":
								topicID = reqArr[2];
								topicZip = new File(Constants.topicPath+File.separator+topicID+".zip");
								hashFound = false;
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
									if (!hashFound)
										hash = Constants.generateHash(topicZip);
									e1.printStackTrace();
								}
								fileSize = topicZip.length();
								out.println(hash+Constants.separator+String.valueOf(fileSize));
								break;
							case "checkTopicHash":
								topicID = reqArr[2];
								topicZip = new File(Constants.topicPath+File.separator+topicID+".zip");
								hashFound = false;
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
									if (!hashFound)
										hash = Constants.generateHash(topicZip);
									e1.printStackTrace();
								}
								if (hash == null)
									hash = "NULL";
								out.println(hash);
								break;
							case "checkTopicComments":
								topicID = reqArr[2];
								topicZip = new File(Constants.topicPath+File.separator+topicID+".zip");
								hashFound = false;
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
									if (!hashFound)
										hash = Constants.generateHash(topicZip);
									e1.printStackTrace();
								}
								fileSize = topicZip.length();
								File topicDir = new File(Constants.topicPath+File.separator+topicID);
								String title = null;
								if (topicDir.exists())
								{
									try {
										br = new BufferedReader(new FileReader(topicDir.getPath()+File.separator+"topic.nfo"));
										br.readLine();
										br.readLine();
										br.readLine();
										title = br.readLine();
										br.close();
									}catch (Exception e)
									{
										e.printStackTrace();
										out.println("NULL");
										break;
									}
									boolean isOpen = false;
									Window[] windows = Window.getWindows();
									for (Window window : windows)
									{
							            if (window instanceof Topic && window.isVisible())
							            {
							            	Topic win = (Topic) window;
							                if (title != null && win.getName().equals(topicID))
							                {
							                	isOpen = true;
							                	break;
							                }
							            }
									}
									
									out.println(hash+Constants.separator+((isOpen)?"open":"closed"));
								}
								else 
									out.println("NULL");
								break;
							/*case "sendCommentMessage":
								topicID = reqArr[2];
								String username = reqArr[3];
								String message = reqArr[4];
								Topic t = Constants.getTopic(topicID);
								t.getCS().receiveMessage(message,username,sendersCID);
								out.println("good!");
								break;*/
							case "torrentTopic":
								topicID = reqArr[2];
								long startByte = Long.parseLong(reqArr[3]);
								long endByte = Long.parseLong(reqArr[4]);
								long bytesToSend = endByte - startByte + 1; 
								topicZip = new File(Constants.topicPath+File.separator+topicID+".zip");
								FileInputStream topicZipIn = new FileInputStream(topicZip);
								buffer = new byte[4096];
								long skipped = topicZipIn.skip(startByte);
								while (skipped < startByte) {
								    skipped += topicZipIn.skip(startByte - skipped);
								}
						        while (bytesToSend > 0) {
						        	 bytesRead = topicZipIn.read(buffer, 0, (int) Math.min(buffer.length, bytesToSend));
						        	 if (bytesRead == -1) break; // End of file
						        	 DOS.write(buffer, 0, bytesRead);
						        	 bytesToSend -= bytesRead;
						        }
						        topicZipIn.close();
						        //DOS.close();
								break;
							case "sendAnnotation":
								topicID = reqArr[2];
								String[] annotation = reqArr[3].split(Constants.arrSeparator);
								Constants.addAnnotation(topicID,annotation);
								out.println("Good!");
								break;
						}
						//System.out.println("Received request: " + request);
						//out.println("Hello from the server!");
						/*in.close();
						out.close();
						clientSocket.close();*/
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		acceptThread.start();
	}
}
