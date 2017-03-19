package UDP;

import java.net.*;
import java.io.*;

public class Server {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("UDP ���");
		
		// ���� ���� �� ��Ʈ ����
		DatagramSocket dSocket = new DatagramSocket(6784);
		
		// ������ ����
		byte[] data = new byte[65508];
		DatagramPacket dp_receive = new DatagramPacket(data, data.length);
		dSocket.receive(dp_receive);
		
		// ������ ���
		String receivedData = new String(dp_receive.getData()).trim();
		System.out.println("������ ���� : " + dp_receive.getAddress().getHostAddress());
		System.out.println("���ŵ� ����(Server) : " + receivedData);
		
		// ������ ��ȯ �� ����
		String sendData = receivedData.toUpperCase();
		System.out.println("��ȯ�� ����(Server) : " + sendData);
		
		// ������ �۽�
		DatagramPacket dp_send = new DatagramPacket(sendData.getBytes(), sendData.getBytes().length, dp_receive.getAddress(), dp_receive.getPort());
		dSocket.send(dp_send);
		
		dSocket.close();
	}

}
