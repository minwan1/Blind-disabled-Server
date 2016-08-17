import java.net.ServerSocket;
import java.net.Socket;

public class GUIServer
{
	public static void main(String[] args)
	{


		Socket socket = null;
		ServerSocket serverSocket = null;

		GUIServerThread serverThread = null;
		GUIServerConn serverConn = new GUIServerConn();

		try
		{
			serverSocket = new ServerSocket(6672);

			System.out.println("##################################");
			System.out.println("#        Server 준비 완료        #");
			System.out.println("##################################");

			while (true)
			{
				socket = serverSocket.accept();
				System.out.println(socket.getInetAddress() + " 연결");

				serverThread = new GUIServerThread(socket, serverConn);
				serverThread.start();
			}

		} catch (Exception e)		{
			e.printStackTrace();
		}
	}
}