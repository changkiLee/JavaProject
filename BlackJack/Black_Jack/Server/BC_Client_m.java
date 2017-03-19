package Server;

import java.io.*;
import java.net.*;
import java.util.*;

import Server.Dealer.Card;
import Server.Dealer.Dealer;

public class BC_Client_m extends Thread {
	private static List<PrintWriter> list = Collections
			.synchronizedList(new ArrayList<PrintWriter>()); // �۽� ������ ����Ʈ
	private Socket socket;
	private PrintWriter writer; // �۽� ������
	private static Vector<String> nameList = new Vector<String>();
	private static Vector<Card> cardList_dealer = new Vector<Card>();

	// ���� ��� ����
	private static Dealer dl = new Dealer(); // ����(1��)
	private static boolean isPlay = false; // �÷��� ����
	private static int accept_num = 0; // ���� �ο���
	private static int ready_num = 0; // �غ� �ο���
	private static int bet_num = 0; // ���� �ο���
	private static int stay_num = 0; // ������ �ο���
	private static int total_money = 0; // �� ���þ�

	BC_Client_m(Socket socket) {
		this.socket = socket;
		try {
			writer = new PrintWriter(socket.getOutputStream());
			list.add(writer);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void InitGame() // ���� �ʱ�ȭ(����)
	{
		cardList_dealer.removeAllElements();
		dl = new Dealer(); // ������ �� & ī�� �ʱ�ȭ
		isPlay = false; // ���� �÷��� ����
		//accept_num = list.size(); // ���� �ο���
		ready_num = 0; // �غ� �ο���
		bet_num = 0; // ���� �ο���
		stay_num = 0; // ������ �ο���
		total_money = 0; // �� ���þ�
	}

	public static int getAcceptNum() {
		return accept_num;
	}

	public void run() {
		String name = null;
		try {
			// ����
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			name = reader.readLine();
			// ������ ���̵� ���� ����
			boolean sameId = false;
			for (int i = 0; i < nameList.size(); i++) {
				if (nameList.get(i).equals(name)) {
					sameId = true;
				}
			}
			if (!sameId) {
				nameList.add(name);
				// �۽�
				sendAll(PROTOCOL.CHATTING + "-*** [" + name + "] �� ���� !");
				accept_num++;

				if (isPlay) // ���� ���� ���� �Ұ�
					sendAll(PROTOCOL.PROHIBIT + "-");
				// �޽��� �м�
				while (true) {
					// ����
					String str = reader.readLine();
					if (str == null)
						break;
					// �м� �� �۽�
					analysisMessage(name, str);
				}
			}
		} catch (Exception e) {
			System.out.print("server : ");
			System.out.println(e.getMessage());
		} finally {
			// ����Ʈ ����
			list.remove(writer);
			nameList.remove(name);
			// �����ڼ� -1
			accept_num--;
			// ��� ����� ���� �ʱ�ȭ
			if (accept_num == 0)
				InitGame();
			// ���� ���� �����(����)
			if (isPlay) {
				//ready_num--;
				//bet_num--;
				//stay_num--;
				System.exit(0);	// ����
			}
			// �۽�
			sendAll(PROTOCOL.CHATTING + "-*** [" + name + "] �� ���� !");

			try {
				socket.close();
			} catch (Exception ignored) {

			}
		}
	}

	// ����Ʈ�� �ִ� ��� ������ ����
	private void sendAll(String str) {
		for (PrintWriter writer : list) {
			writer.println(str);
			writer.flush();
		}
	}

	// �޽��� �м�(���̵� ����)
	public void analysisMessage(String name, String str) {
		// �޽��� �м��� ���� PROTOCOL �з�
		StringTokenizer st = new StringTokenizer(str, "-");
		String TYPE = st.nextToken(); // str�� ���ۺ� ���� PROTOCOL�� Ÿ���� ����ȴ�.

		if (PROTOCOL.CHATTING.equals(TYPE)) { // ä�� ������
			String chat = st.nextToken();
			sendAll(PROTOCOL.CHATTING + "-" + name + " > " + chat);
		} else if (PROTOCOL.READY.equals(TYPE)) { // �غ�
			sendAll(PROTOCOL.CHATTING + "-***" + name + " �غ� �Ϸ�!");

			// �غ��� ���� ȭ�鿡 ����
			sendAll(PROTOCOL.PLAYER + "-" + name);

			ready_num++; // �غ��� �� ����
			if (accept_num == ready_num) // �غ� �Ϸ�
			{
				sendAll(PROTOCOL.CHATTING + "-***���� �غ� �Ϸ�!");
				isPlay = true; // ���� ����
			}
		} else if (PROTOCOL.WAIT.equals(TYPE)) { // ���
			sendAll(PROTOCOL.WAIT + "-");
		} else if (PROTOCOL.BET.equals(TYPE)) {
			int money = Integer.parseInt(st.nextToken());
			total_money += money;
			sendAll(PROTOCOL.CHATTING + "-***" + name + " ���� : ��"
					+ String.valueOf(money));

			bet_num++;
			if (ready_num == bet_num) {
				sendAll(PROTOCOL.CHATTING + "-***���� ���ÿϷ�!");
				sendAll(PROTOCOL.CHATTING + "-***�� ���þ� : ��"
						+ String.valueOf(total_money));
				sendAll(PROTOCOL.CHATTING + "-***���� ����!");

				// ���� ����(������ ī�带 �й���)
				for (int j = 0; j < 2; j++) {
					// ������
					String dealer = dl.giveCard();
					sendAll(PROTOCOL.GETCARD + "-" + dealer);

					// ī�� ���� �м�
					StringTokenizer dt = new StringTokenizer(dealer, "/");
					String ctype = dt.nextToken(); // ī�� ����
					String cname = dt.nextToken(); // ī�� ��ȣ
					String cvalue = dt.nextToken(); // ī�� ��
					Card c_card = new Card(ctype, cname,
							Integer.parseInt(cvalue));
					cardList_dealer.add(c_card); // ��ü ����Ʈ�� �߰�

					// �ο�����ŭ ī�� ���
					for (int i = 0; i < ready_num; i++) {
						// �� Ŭ���̾�Ʈ�� �̸� ��� �۽�
						sendAll(PROTOCOL.GETCARD + "-" + dl.giveCard());
					}
				}
			}
		} else if (PROTOCOL.HIT.equals(TYPE)) {
			sendAll(PROTOCOL.CHATTING + "-***" + name + " HIT!");

			// ī�� �߰�
			sendAll(PROTOCOL.GETCARD + "-" + dl.giveCard()); // ī�� ���� �߰�
			sendAll(PROTOCOL.HIT + "-" + name); // ����?

		} else if (PROTOCOL.STAY.equals(TYPE)) {
			sendAll(PROTOCOL.CHATTING + "-***" + name + " STAY!");

			stay_num++; // ������ �� ����
			if (ready_num == stay_num) // ������ �Ϸ�
			{
				sendAll(PROTOCOL.CHATTING + "-***���� STAY!");

				// ������ ��Ʈ
				while (true) {
					int sum = 0;
					int ace_num = 0;
					
					for (int i = 0; i < cardList_dealer.size(); i++) {
						sum += cardList_dealer.get(i).getCardValue();
						if (cardList_dealer.get(i).getCardName().equals("1")) { // A�� ���
							ace_num++;
						}
					}
					for (int i = 0; i < ace_num; i++) {
						sum += 10; // A�� ������ŭ 10�� ���Ѵ�(11�� ���)
						if (sum > 21)
							sum -= 10; // �ʰ��Ǹ� 1�� ���
					}
					
					if(sum < 17)	// 16 ���ϸ� ��Ʈ
					{
						String dealer = dl.giveCard();
						sendAll(PROTOCOL.GETCARD + "-" + dealer);	// ���� ��Ʈ
						sendAll(PROTOCOL.HIT + "-" + "dealer"); // ����?
						
						// ī�� ���� �м�
						StringTokenizer dt = new StringTokenizer(dealer, "/");
						String ctype = dt.nextToken(); // ī�� ����
						String cname = dt.nextToken(); // ī�� ��ȣ
						String cvalue = dt.nextToken(); // ī�� ��
						Card c_card = new Card(ctype, cname, Integer.parseInt(cvalue));
						cardList_dealer.add(c_card); // ��ü ����Ʈ�� �߰�
					}
					else
					{
						break;
					}
				}
				
				// ī�� ����
				sendAll(PROTOCOL.CHATTING + "-***���� ī�� ����!");
				sendAll(PROTOCOL.RESULT + "-");
				sendAll(PROTOCOL.WAIT + "-");
				sendAll(PROTOCOL.CHATTING + "-***���� ����� ����!");
				// �����
				sendAll(PROTOCOL.RESTART + "-");
				InitGame();
			}
		}
	}
}