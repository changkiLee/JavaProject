package TCP;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	
	private static Scanner scan;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("TCP/IP 통신");
		
		// 데이터 입력
		scan = new Scanner(System.in);
		System.out.println("Data 입력(English) : ");
		String sendData = scan.nextLine();
				
		InetAddress iAddress = null;
		Socket socket = null;
		PrintWriter pw_send = null;
		
		try{
			// IP, 소켓, 송신 설정
			iAddress = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
			socket = new Socket(iAddress, 6784);
						
			// 데이터 전송
			pw_send = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			pw_send.println(sendData);
			pw_send.flush();
			
			// 데이터 수신 및 출력
			BufferedReader br_receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String receivedData = br_receive.readLine();
			System.out.println("수신된 내용(Client) : " + receivedData);		
			
			// Close
			pw_send.close();
			br_receive.close();
			socket.close();			
		} catch(IOException ee){
			System.err.println("접속 오류 : " + ee.toString());
			System.exit(-1);
		}
	}

}
