package Server;
import java.awt.*;
import java.net.*;

public class Server_ extends Thread {
	private ServerSocket serverSocket = null;
	private TextArea ta;
	private int COUNT = 0;	// ���� �ο���

	public Server_(ServerSocket serverSocket, TextArea ta) {
		this.serverSocket = serverSocket;
		this.ta = ta;
	}

	public void run() {
		try {
			System.out.println("Server Start!\n");
			while (true) {
				Socket socket = serverSocket.accept();		// Ŭ���̾�Ʈ ���� ���
				
				COUNT = BC_Client_m.getAcceptNum();
				if(COUNT < 3) {		// ���� �ο� ����
					ta.append("\n" + "Client Connection : " + socket.getInetAddress().getHostAddress() + "\n");	// ������ Ŭ���̾�Ʈ�� IP ���
					Thread thread = new BC_Client_m(socket);	// �ټ��� �ο� ����
					thread.start();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
