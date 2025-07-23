import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Node 
{
	private String CID;
	private String IP;
	private int port = 0;
	private String region = null;
	private String nodeStr;
	private byte[] key;
	private HashMap<String,String> ipInfo = null;
	public Node(int port,String IP) {
		try {	
			this.IP = IP;
			this.port = port;
			this.key = Constants.key;
			this.nodeStr = Integer.toString(port)+Constants.CIDSeparator+IP;
			this.CID = EncryptionDecryption.encrypt(nodeStr,key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Node(String CID)
	{
		try {
			this.CID = CID;
			this.key = Constants.key;
			this.nodeStr = EncryptionDecryption.decrypt(CID,key);
			String[] nodeArr = nodeStr.split(Constants.CIDSeparator);
			this.IP = nodeArr[1];
			this.port = Integer.parseInt(nodeArr[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public String toString()
	{
		return nodeStr;
	}
	public boolean equals(Node compNode)
	{
		return (compNode.getCID().equals(getCID()));
	}
	public String getIP()
	{
		return IP;
	}
	public String getCID()
	{
		return CID;
	}
	public void setPort(int newPort)
	{
		port = newPort;
	}
	public int getPort()
	{
		return port;
	}
	public String getRegion()
	{
		if (region == null)
		{
			ipInfo = Constants.grabIPInfo(IP);
			region = ipInfo.get("region");
			return region;
		}
		else
			return region;
	}
	public boolean isMe()
	{
		return (Constants.accInfo[1].equals(CID));
	}
}
