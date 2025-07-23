import java.io.*;
import java.net.*;

public class AdvSocket
{
	private String host;
	private int port;
	private PrintWriter out = null;
	private DataOutputStream dataOut = null;
	private DataInputStream  dataIn = null;
	private BufferedReader in = null;
	private Socket socket = null;
	public AdvSocket(String host,int port) throws Exception
	{
		this.socket = new Socket();
		this.port = port;
		this.host = host;
		SocketAddress socketAddress = new InetSocketAddress(host, port);
		socket.connect(socketAddress, Constants.timeout);
		this.out = new PrintWriter(socket.getOutputStream(),true);
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.dataOut = new DataOutputStream(socket.getOutputStream());
		this.dataIn = new DataInputStream(socket.getInputStream());
	}
	public AdvSocket(Socket socket,int port) throws Exception
	{

		InetAddress inet = socket.getInetAddress();
		this.port = port;
		this.host = inet.getHostAddress();
		this.socket = socket;
		this.socket.setSoTimeout(Constants.timeout);
		this.out = new PrintWriter(socket.getOutputStream(),true);
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.dataOut = new DataOutputStream(socket.getOutputStream());
		this.dataIn = new DataInputStream(socket.getInputStream());
	}
	public Socket getSocket()
	{
		return socket;
	}
	public void closeAll()
	{
		try
		{
			out.close();
			in.close();
			socket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public String getHost()
	{
		return host;
	}
	public int getPort()
	{
		return port;
	}
	public PrintWriter getPW()
	{
		return out;
	}
	public BufferedReader getBR()
	{
		return in;
	}
	public DataOutputStream getDOS()
	{
		return dataOut;
	}
	public DataInputStream getDIS()
	{
		return dataIn;
	}
	public String toString()
	{
		return "Host: "+host+"\nPort: "+port+"\n"+"Is Connected: "+socket.isConnected();
	}
}
