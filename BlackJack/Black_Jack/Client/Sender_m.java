package Client;

import java.io.*;
import java.net.*;

public class Sender_m extends Thread {
	private PrintWriter writer;	// 송신 데이터

	Sender_m(Socket socket, String playerInfo) throws IOException {	// run
		writer = new PrintWriter(socket.getOutputStream());
		writer.println(playerInfo);	// 사용자 정보 송신
		writer.flush();
	}

	public void sendMessage(String message) {
		if (message.equals(""))
			return;
		writer.println(message);	// 메세지 송신
		writer.flush();
	}
}
