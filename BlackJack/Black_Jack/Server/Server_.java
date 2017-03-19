package Server;
import java.awt.*;
import java.net.*;

public class Server_ extends Thread {
	private ServerSocket serverSocket = null;
	private TextArea ta;
	private int COUNT = 0;	// 접속 인원수

	public Server_(ServerSocket serverSocket, TextArea ta) {
		this.serverSocket = serverSocket;
		this.ta = ta;
	}

	public void run() {
		try {
			System.out.println("Server Start!\n");
			while (true) {
				Socket socket = serverSocket.accept();		// 클라이언트 접속 대기
				
				COUNT = BC_Client_m.getAcceptNum();
				if(COUNT < 3) {		// 접속 인원 제한
					ta.append("\n" + "Client Connection : " + socket.getInetAddress().getHostAddress() + "\n");	// 접속한 클라이언트의 IP 출력
					Thread thread = new BC_Client_m(socket);	// 다수의 인원 수용
					thread.start();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
