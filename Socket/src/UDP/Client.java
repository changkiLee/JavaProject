package UDP;

import java.net.*;
import java.io.*;

public class Client {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("UDP ���");
		
		// ������ �Է�
		System.out.println("Data �Է�(English) : ");
		BufferedReader br_input = new BufferedReader(new InputStreamReader(System.in));
		String sendData = br_input.readLine();
		
		// ���� ����
		DatagramSocket dSocket = new DatagramSocket();
		
		// ������ �ּ�
		InetAddress iAddress = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
		// ������ �۽�
		DatagramPacket dp_send = new DatagramPacket(sendData.getBytes(), sendData.getBytes().length, iAddress, 6784);
		dSocket.send(dp_send);
		
		System.out.println("���ۿϷ�");
		
		// ������ ����
		byte[] data = new byte[65508];
		DatagramPacket dp_receive = new DatagramPacket(data, data.length);
		dSocket.receive(dp_receive);
		
		// ������ ���
		String receivedData = new String(dp_receive.getData()).trim();
		System.out.println("���ŵ� ����(Client) : " + receivedData);
		
		// Close
		dSocket.close();
		
	}

}
