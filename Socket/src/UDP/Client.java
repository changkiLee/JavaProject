package UDP;

import java.net.*;
import java.io.*;

public class Client {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("UDP 통신");
		
		// 데이터 입력
		System.out.println("Data 입력(English) : ");
		BufferedReader br_input = new BufferedReader(new InputStreamReader(System.in));
		String sendData = br_input.readLine();
		
		// 소켓 생성
		DatagramSocket dSocket = new DatagramSocket();
		
		// 보내는 주소
		InetAddress iAddress = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
		// 데이터 송신
		DatagramPacket dp_send = new DatagramPacket(sendData.getBytes(), sendData.getBytes().length, iAddress, 6784);
		dSocket.send(dp_send);
		
		System.out.println("전송완료");
		
		// 데이터 수신
		byte[] data = new byte[65508];
		DatagramPacket dp_receive = new DatagramPacket(data, data.length);
		dSocket.receive(dp_receive);
		
		// 데이터 출력
		String receivedData = new String(dp_receive.getData()).trim();
		System.out.println("수신된 내용(Client) : " + receivedData);
		
		// Close
		dSocket.close();
		
	}

}
