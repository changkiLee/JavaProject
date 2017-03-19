package Client;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JButton;

import Server.PROTOCOL;

public class Receiver_m extends Thread {
	private Socket socket;
	private TextArea ta;
	private GameView gv = null;
	private boolean isPlay = false;
	private boolean isHOS = false;
	private Label l_money;
	private int money = 0;
	private JButton r_button;

	Receiver_m(Socket socket, TextArea ta, GameView gv, Label m, JButton b) {
		this.socket = socket;
		this.ta = ta;
		this.gv = gv;
		this.l_money = m;
		this.money = 100;
		this.r_button = b;
	}

	public boolean getIsPlay() {
		return isPlay;
	}

	public boolean getIsHOS() {
		return isHOS;
	}
	
	public int getMoney(){
		return money;
	}
	
	public synchronized void run() {
		try {
			// ����
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			while (true) {
				String str = reader.readLine(); // �ѹ���(��ü) ����
				System.out.println(str + " : receiver");
				if (str == null)
					break;
				analysisMessage(str); // �޽��� �м� �� �۽�
				str = null;
			}
		} catch (IOException | InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	public void analysisMessage(String str) throws IOException,
			InterruptedException {
		StringTokenizer st = new StringTokenizer(str, "-");
		String TYPE = st.nextToken(); // TYPE�� �� ���� ��������

		if (PROTOCOL.CHATTING.equals(TYPE)) {
			ta.append(st.nextToken() + "\n"); // ä�� ���� ���
		} else if (PROTOCOL.WAIT.equals(TYPE)) {
			this.wait(10000); // ���
		} else if (PROTOCOL.PROHIBIT.equals(TYPE)) {
			isPlay = true; // �����Ұ�
		} else if (PROTOCOL.PLAYER.equals(TYPE)) {
			gv.addPlayerName(st.nextToken()); // �÷��̾� ����
		} else if (PROTOCOL.GETCARD.equals(TYPE)) {
			isHOS = true; // ��Ʈ ������ ����
			gv.addCard(st.nextToken()); // ī�� �߰�
		} else if (PROTOCOL.HIT.equals(TYPE)) {
			gv.addHitCard(st.nextToken());
		} else if (PROTOCOL.RESULT.equals(TYPE)) {			
			gv.calScore();
			money += gv.getMoney();
			l_money.setText("Money : ��" + String.valueOf(money));
		} else if (PROTOCOL.RESTART.equals(TYPE)) {
			isPlay = false; // ���� ����
			isHOS = false;
			r_button.setEnabled(true);
			gv.initGame();
		}
	}
}
