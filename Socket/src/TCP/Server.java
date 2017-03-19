package TCP;

import java.io.*;
import java.net.*;

public class Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("TCP/IP 통신");
		ServerSocket sSocket = null;
		Socket socket = null;
		PrintWriter pw_send = null;
		
		try{
			// 소켓 포트 지정
			sSocket = new ServerSocket(6784);
			System.out.println("Server Ready...");
		} catch(IOException ee){
			System.err.println("해당 포트가 열려 있습니다.");
			System.exit(-1);
		}
		
		try{
			// 수신 대기
			socket = sSocket.accept();
			
			// 데이터 수신 및 출력
			System.out.println("접속자 정보 : " + socket.toString());						
			BufferedReader br_receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String receivedData = br_receive.readLine();
			System.out.println("수신된 내용(Server) : " + receivedData);
			
			// 데이터 변환 및 술력
			String sendData = receivedData.toUpperCase();
			System.out.println("변환된 내용(Server) : " + sendData);
			
			// 데이터 송신
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
