package websocket;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class initializeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		String app_key = "38162d61dd6d2dca";
		String app_secert = "7ba41f9a8ac98f2c16ee904bc7ea624c";
		String timestamp = getCurrentTimestamp();		
		String sign = "";
		try {
			sign = MD5Encoding(timestamp + "#" + app_secert);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String destUri = "ws://192.168.30.70:9000/websocket/record/" + app_key + "/" + timestamp + "/" + sign;
		
		System.out.println("----------");
		System.out.println("---------- websocket.initializeServlet successfully ----------");
		System.out.println("----------");

		WebSocketClient client = new WebSocketClient();
		SimpleEchoSocket socket = new SimpleEchoSocket();

		try {
			client.start();

			URI echoUri = new URI(destUri);
			ClientUpgradeRequest request = new ClientUpgradeRequest();
			client.connect(socket, echoUri, request);
			System.out.printf("Connecting to : %s%n", echoUri);

			// wait for closed socket connection.
			//socket.awaitClose(5, TimeUnit.SECONDS);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				//client.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/** MD5加密
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public String MD5Encoding(String str) throws NoSuchAlgorithmException {
		String rval = "";
		
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		try {
			md5.update((str).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte b[] = md5.digest();

		int i;
		StringBuffer buf = new StringBuffer("");

		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0) {
				i += 256;
			}
			if (i < 16) {
				buf.append("0");
			}
			buf.append(Integer.toHexString(i));
		}

		rval = buf.toString();		
		return rval;
	}
	
	/** 取得目前的 timestamp
	 * @return
	 */
	public String getCurrentTimestamp() {
		Date date= new Date();		 
		long time = date.getTime();
		return Long.toString(time);
	}

}
