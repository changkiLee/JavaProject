package TCP;

import java.io.*;
import java.net.*;

public class Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("TCP/IP ���");
		ServerSocket sSocket = null;
		Socket socket = null;
		PrintWriter pw_send = null;
		
		try{
			// ���� ��Ʈ ����
			sSocket = new ServerSocket(6784);
			System.out.println("Server Ready...");
		} catch(IOException ee){
			System.err.println("�ش� ��Ʈ�� ���� �ֽ��ϴ�.");
			System.exit(-1);
		}
		
		try{
			// ���� ���
			socket = sSocket.accept();
			
			// ������ ���� �� ���
			System.out.println("������ ���� : " + socket.toString());						
			BufferedReader br_receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String receivedData = br_receive.readLine();
			System.out.println("���ŵ� ����(Server) : " + receivedData);
			
			// ������ ��ȯ �� ����
			String sendData = receivedData.toUpperCase();
			System.out.println("��ȯ�� ����(Server) : " + sendData);
			
			// ������ �۽�
			pw_send = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			pw_send.println(sendData);
			pw_send.flush();
			
			// Close
			pw_send.close();
			br_receive.close();
			socket.close();
		} catch(IOException ee){}

	}

}
