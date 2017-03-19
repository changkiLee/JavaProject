package Client;

import java.io.*;
import java.net.*;

public class Sender_m extends Thread {
	private PrintWriter writer;	// �۽� ������

	Sender_m(Socket socket, String playerInfo) throws IOException {	// run
		writer = new PrintWriter(socket.getOutputStream());
		writer.println(playerInfo);	// ����� ���� �۽�
		writer.flush();
	}

	public void sendMessage(String message) {
		if (message.equals(""))
			return;
		writer.println(message);	// �޼��� �۽�
		writer.flush();
	}
}
