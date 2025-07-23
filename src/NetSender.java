import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
public class NetSender {
	private byte[] key;
	private String CID;
	private ArrayList<AdvSocket> socks = new ArrayList<AdvSocket>();
	public NetSender()
	{
		this.key = Constants.key;
		this.CID = Constants.accInfo[1];
	}
	public void setCID(String CID)
	{
		this.CID = CID;
	}
	private AdvSocket retrieveSocket(Node hostNode)
	{
		String host = hostNode.getIP();
		int port = hostNode.getPort();
		try
		{
			AdvSocket sock = null;
			for (AdvSocket tmpAdvSock : socks)
			{
				Socket tmpSock = tmpAdvSock.getSocket();
				InetAddress tmpInet = tmpSock.getInetAddress();
				if (host == tmpInet.getHostAddress() || host == tmpInet.getHostName())
					sock = tmpAdvSock;
			}
			if (sock == null)// || (sock!=null && !sock.isConnected())
			{
				sock = new AdvSocket(host, port);
				socks.add(sock);
			}
			return sock;
		} catch(Exception e) {
			System.out.println("Unable to retrieve host ("+host+"): "+e.getMessage());
			return null;
		}
	}
	public String reqFromAuth(Node hostNode, String topicID)
	{
		if (hostNode.isMe())
			return null;
		try {
			AdvSocket sock = retrieveSocket(hostNode);
			PrintWriter out = sock.getPW();
			BufferedReader in = sock.getBR();
			out.println(CID+Constants.separator+"reqFromAuth"+Constants.separator+topicID);
			String response = in.readLine();
			return response;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public String checkTopicComments(Node hostNode, String topicID)
	{
		try 
		{
			//System.out.println(host);
			AdvSocket sock = retrieveSocket(hostNode);
			if (sock == null)
				return null;
			PrintWriter out = sock.getPW();
			BufferedReader in = sock.getBR();
			out.println(CID+Constants.separator+"checkTopicComments"+Constants.separator+topicID);
			String response = in.readLine();
			if (response.equals("NULL"))
				return null;
			return response;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public boolean sendCommentMessage(Node hostNode,String topicID,String message)
	{
		try {
			AdvSocket sock = retrieveSocket(hostNode);
			if (sock == null)
				return false;
			PrintWriter out = sock.getPW();
			BufferedReader in = sock.getBR();
			String username = Constants.accInfo[0];
			out.println(CID+Constants.separator+"sendCommentMessage"+Constants.separator+topicID+Constants.separator+username+Constants.separator+message);
			//System.out.println(CID+Constants.separator+"sendCommentMessage"+Constants.separator+topicID+Constants.separator+username+Constants.separator+message);
			String response = in.readLine();
			if (response.equals("NULL"))
				return false;
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	public String checkTopicHash(Node hostNode, String topicID)
	{
		try 
		{
			//System.out.println(host);
			AdvSocket sock = retrieveSocket(hostNode);
			if (sock == null)
				return null;
			PrintWriter out = sock.getPW();
			BufferedReader in = sock.getBR();
			out.println(CID+Constants.separator+"checkTopicHash"+Constants.separator+topicID);
			String response = in.readLine();
			if (response.equals("NULL"))
				return null;
			return response;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public void sendMessage(String message, Node hostNode)
	{
		try
		{
			AdvSocket sock = retrieveSocket(hostNode);
			PrintWriter out = sock.getPW();
			BufferedReader in = sock.getBR();
			out.println(message);
			String response = in.readLine();
			System.out.println("Server response: " + response);
		} catch (SocketTimeoutException e){
			//System.out.println(host+" connection timed out!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean sendPing(Node hostNode)
	{
		try 
		{
			AdvSocket sock = retrieveSocket(hostNode);
			//System.out.println(sock);
			if (sock == null)
				return false;
			PrintWriter out = sock.getPW();
			BufferedReader in = sock.getBR();
			out.println(CID+Constants.separator+"ping");
			String response = in.readLine();
			return (response.equals("Ping OK"));
		}
		catch (Exception e)
		{
			return false;
		}
	}
	public String[] sendInquiry(Node hostNode,String[] inquiry) throws Exception
	{
		AdvSocket sock = retrieveSocket(hostNode);
		PrintWriter out = sock.getPW();
		BufferedReader in = sock.getBR();
		out.println(CID+Constants.separator+"topicInd"+Constants.separator+String.join(Constants.arrSeparator,inquiry));
		String response = in.readLine();
		if (response.equals("NULL"))
			return null;
		else
			return response.split(Constants.arrSeparator);
		//return response.split(Constants.NLSeparator);
	}
	public void reqNodes()
	{
		ArrayList<Node> newNodes = new ArrayList<Node>();
		for (Map.Entry<String, Node> nodeMap : Constants.nodeList.entrySet())
		{
			Node node = nodeMap.getValue();
			try 
			{
				AdvSocket sock = retrieveSocket(node);
				PrintWriter out = sock.getPW();
				BufferedReader in = sock.getBR();
				out.println(CID+Constants.separator+"rn");
				String response = in.readLine();
				String[] requestedNodes = response.split(Constants.separator);
				for (String retrivedNode : requestedNodes)
				{
					//String nodeStr = EncryptionDecryption.decrypt(retrivedNode,Constants.key);
					if (retrivedNode.equals(CID))
						continue;
					System.out.println("New Node: "+retrivedNode);
					if (!Constants.nodeList.containsKey(retrivedNode))
						newNodes.add(new Node(retrivedNode));
				}
			}
			catch(Exception e)
			{
				//System.out.println("Could not retrieve nodes from: "+nodeArr[1]);
				//e.printStackTrace();
			}
		}
		for (Node newNode : newNodes)
			Constants.addToNodeList(newNode,true);
		Constants.saveNodeList();
		//Constants.saveNodeList();
	}
	public boolean startTorrent(ArrayList<Node> availNodes,String topicID, long filesize)
	{
		System.out.println("Available Nodes: "+availNodes.size());
		ExecutorService executor = Executors.newFixedThreadPool(availNodes.size());
	    List<Future<Boolean>> futures = new ArrayList<>();
	    AtomicBoolean success = new AtomicBoolean(true);
	    try {
	        // Phase 1: Parallel Download
	        for (int i = 0; i < availNodes.size(); i++) {
	            final int nodeIndex = i;
	            final Node node = availNodes.get(nodeIndex);
	            
	            futures.add(executor.submit(() -> {
	            	AdvSocket sock = retrieveSocket(node);
	                try {
	                    long chunkSize = filesize / availNodes.size();
	                    long startByte = nodeIndex * chunkSize;
	                    long endByte = (nodeIndex == availNodes.size() - 1) 
	                        ? filesize - 1 
	                        : startByte + chunkSize - 1;
	                    System.out.println("Downloading chunk size:"+chunkSize+"|startByte="+startByte+"|endByte="+endByte+"|nodeIndex="+nodeIndex);
	                    synchronized(sock.getPW()) {
	                        sock.getPW().println(String.join(Constants.separator, 
	                            CID, "torrentTopic", topicID, 
	                            String.valueOf(startByte), 
	                            String.valueOf(endByte)));
	                    }

	                    // Create part directory if needed
	                    File partFile = new File(Constants.topicPath, nodeIndex+Constants.separator+topicID + ".part");
	                    try (FileOutputStream fos = new FileOutputStream(partFile)) {
	                        byte[] buffer = new byte[4096]; // Reduced to 4KB for stability
	                        long bytesRemaining = endByte - startByte + 1;
	                        DataInputStream dis = sock.getDIS();
	                        
	                        while (bytesRemaining > 0) {
	                            int read;
	                            // Synchronized read
	                            synchronized(dis) {
	                                read = dis.read(buffer, 0, 
	                                    (int) Math.min(buffer.length, bytesRemaining));
	                            }
	                            
	                            if (read == -1) break;
	                            fos.write(buffer, 0, read);
	                            bytesRemaining -= read;
	                        }
	                        
	                        // Verify download completeness
	                        if (bytesRemaining > 0) {
	                            throw new IOException("Incomplete transfer: " + bytesRemaining + " bytes missing");
	                        }
	                    }
	                    return true;
	                } catch (Exception e) {
	                    System.err.println("Error downloading from node " + node + ": " + e.getMessage());
	                    success.set(false);
	                    return false;
	                }
	            }));
	        }

	        // Wait for all downloads to complete
	        for (Future<Boolean> future : futures) {
	            future.get(30, TimeUnit.SECONDS); // Timeout per node
	        }

	        // Phase 2: Assemble parts (sequential)
	        if (success.get()) {
	            try (FileOutputStream fos = new FileOutputStream(
	                Constants.topicPath + File.separator + topicID + ".zip", true)) {
	                
	                for (int i = 0; i < availNodes.size(); i++) {
	                    File partFile = new File(
	                        Constants.topicPath + File.separator + i + 
	                        Constants.separator + topicID + ".part");
	                    
	                    if (partFile.exists()) {
	                        Files.copy(partFile.toPath(), fos);
	                        partFile.delete();
	                    }
	                }
	            }
	            
	            Constants.extractZip(Constants.topicPath + File.separator + topicID + ".zip");
	            Constants.updateTopicInd();
	            return true;
	        }
	        return false;
	        
	    } catch (TimeoutException e) {
	        System.err.println("Download timed out");
	        return false;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        executor.shutdown();
	    }
	}
	public String reqTopic(Node hostNode, String topicID)
	{
		AdvSocket sock = retrieveSocket(hostNode);
		PrintWriter out = sock.getPW();
		BufferedReader in = sock.getBR();
		String CID;
		try {
			CID = in.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		DataInputStream DIS = sock.getDIS();
		out.println(CID+Constants.separator+"retTopic"+Constants.separator+topicID);
		try
		{
			File newTopicDir = new File(Constants.topicPath + File.separator + Constants.randID(Constants.topicIDLength));
			boolean dirCreated = newTopicDir.mkdirs(); 
	        if (!dirCreated) {
	            System.err.println("Failed to create directory: " + newTopicDir.getPath());
	            return null;
	        }
	        File newTopicInfo = new File(newTopicDir.getPath() + File.separator + "topic.nfo");
	        FileOutputStream newTopicInfoOut = new FileOutputStream(newTopicInfo);
	        long fileSize = DIS.readLong();
	        byte[] buffer = new byte[4096];
	        int bytesRead;
	        long totalBytesRead = 0;
	        while (totalBytesRead < fileSize && (bytesRead = DIS.read(buffer)) != -1) {
	        	newTopicInfoOut.write(buffer, 0, bytesRead);
	            totalBytesRead += bytesRead;
	        }
	        newTopicInfoOut.close();
	        return CID;
		} catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	public String sendAnnotation(Node hostNode,String topicID,String[] annotation)
	{
		AdvSocket sock = retrieveSocket(hostNode);
		if (sock == null)
			return null;
		PrintWriter out = sock.getPW();
		BufferedReader in = sock.getBR();
		out.println(CID+Constants.separator+"sendAnnotation"+Constants.separator+topicID+Constants.separator+String.join(Constants.arrSeparator,annotation));
		String response;
		try {
			response = in.readLine();
			if (response.equals("NULL"))
				return null;
			else
				return response;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public void closeASock(Node hostNode)
	{
		AdvSocket sock = retrieveSocket(hostNode);
		sock.closeAll();
	}
}
