import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtils {

    public static String getActiveMacAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isUp())
                {
	                byte[] macBytes = networkInterface.getHardwareAddress();
	                if (macBytes != null) {
	                    StringBuilder sb = new StringBuilder();
	                    for (int i = 0; i < macBytes.length; i++) {
	                        sb.append(String.format("%02X%s", macBytes[i], (i < macBytes.length - 1) ? "-" : ""));
	                    }
	                    return sb.toString();
	                }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null; // Return null if no active interface is found
    }
}