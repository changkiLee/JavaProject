package TCP;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	
	private static Scanner scan;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("TCP/IP ���");
		
		// ������ �Է�
		scan = new Scanner(System.in);
		System.out.println("Data �Է�(English) : ");
		String sendData = scan.nextLine();
				
		InetAddress iAddress = null;
		Socket socket = null;
		PrintWriter pw_send = null;
		
		try{
			// IP, ����, �۽� ����
			iAddress = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
			socket = new Socket(iAddress, 6784);
						
			// ������ ����
			pw_send = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			pw_send.println(sendData);
			pw_send.flush();
			
			// ������ ���� �� ���
			BufferedReader br_receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String receivedData = br_receive.readLine();
			System.out.println("���ŵ� ����(Client) : " + receivedData);		
			
			// Close
			pw_send.close();
			br_receive.close();
			socket.close();			
		} catch(IOException ee){
			System.err.println("���� ���� : " + ee.toString());
			System.exit(-1);
		}
	}

}
