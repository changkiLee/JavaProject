package UDP;

import java.net.*;
import java.io.*;

public class Server {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("UDP 통신");
		
		// 소켓 생성 및 포트 지정
		DatagramSocket dSocket = new DatagramSocket(6784);
		
		// 데이터 수신
		byte[] data = new byte[65508];
		DatagramPacket dp_receive = new DatagramPacket(data, data.length);
		dSocket.receive(dp_receive);
		
		// 데이터 출력
		String receivedData = new String(dp_receive.getData()).trim();
		System.out.println("접속자 정보 : " + dp_receive.getAddress().getHostAddress());
		System.out.println("수신된 내용(Server) : " + receivedData);
		
		// 데이터 변환 및 술력
		String sendData = receivedData.toUpperCase();
		System.out.println("변환된 내용(Server) : " + sendData);
		
		// 데이터 송신
		DatagramPacket dp_send = new DatagramPacket(sendData.getBytes(), sendData.getBytes().length, dp_receive.getAddress(), dp_receive.getPort());
		dSocket.send(dp_send);
		
		dSocket.close();
	}

}
