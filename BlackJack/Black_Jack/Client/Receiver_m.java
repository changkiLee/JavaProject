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
			// 수신
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			while (true) {
				String str = reader.readLine(); // 한문장(전체) 수신
				System.out.println(str + " : receiver");
				if (str == null)
					break;
				analysisMessage(str); // 메시지 분석 및 송신
				str = null;
			}
		} catch (IOException | InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	public void analysisMessage(String str) throws IOException,
			InterruptedException {
		StringTokenizer st = new StringTokenizer(str, "-");
		String TYPE = st.nextToken(); // TYPE에 들어간 것은 프로토콜

		if (PROTOCOL.CHATTING.equals(TYPE)) {
			ta.append(st.nextToken() + "\n"); // 채팅 내용 출력
		} else if (PROTOCOL.WAIT.equals(TYPE)) {
			this.wait(10000); // 대기
		} else if (PROTOCOL.PROHIBIT.equals(TYPE)) {
			isPlay = true; // 참가불가
		} else if (PROTOCOL.PLAYER.equals(TYPE)) {
			gv.addPlayerName(st.nextToken()); // 플레이어 참가
		} else if (PROTOCOL.GETCARD.equals(TYPE)) {
			isHOS = true; // 히트 스테이 가능
			gv.addCard(st.nextToken()); // 카드 추가
		} else if (PROTOCOL.HIT.equals(TYPE)) {
			gv.addHitCard(st.nextToken());
		} else if (PROTOCOL.RESULT.equals(TYPE)) {			
			gv.calScore();
			money += gv.getMoney();
			l_money.setText("Money : ＄" + String.valueOf(money));
		} else if (PROTOCOL.RESTART.equals(TYPE)) {
			isPlay = false; // 참가 가능
			isHOS = false;
			r_button.setEnabled(true);
			gv.initGame();
		}
	}
}
