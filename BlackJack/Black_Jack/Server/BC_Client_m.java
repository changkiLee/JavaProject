package Server;

import java.io.*;
import java.net.*;
import java.util.*;

import Server.Dealer.Card;
import Server.Dealer.Dealer;

public class BC_Client_m extends Thread {
	private static List<PrintWriter> list = Collections
			.synchronizedList(new ArrayList<PrintWriter>()); // 송신 데이터 리스트
	private Socket socket;
	private PrintWriter writer; // 송신 데이터
	private static Vector<String> nameList = new Vector<String>();
	private static Vector<Card> cardList_dealer = new Vector<Card>();

	// 게임 요소 변수
	private static Dealer dl = new Dealer(); // 딜러(1명)
	private static boolean isPlay = false; // 플레이 상태
	private static int accept_num = 0; // 접속 인원수
	private static int ready_num = 0; // 준비 인원수
	private static int bet_num = 0; // 배팅 인원수
	private static int stay_num = 0; // 스테이 인원수
	private static int total_money = 0; // 총 배팅액

	BC_Client_m(Socket socket) {
		this.socket = socket;
		try {
			writer = new PrintWriter(socket.getOutputStream());
			list.add(writer);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void InitGame() // 게임 초기화(리셋)
	{
		cardList_dealer.removeAllElements();
		dl = new Dealer(); // 딜러의 돈 & 카드 초기화
		isPlay = false; // 현재 플래이 상태
		//accept_num = list.size(); // 접속 인원수
		ready_num = 0; // 준비 인원수
		bet_num = 0; // 배팅 인원수
		stay_num = 0; // 스테이 인원수
		total_money = 0; // 총 배팅액
	}

	public static int getAcceptNum() {
		return accept_num;
	}

	public void run() {
		String name = null;
		try {
			// 수신
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			name = reader.readLine();
			// 동일한 아이디 접속 방지
			boolean sameId = false;
			for (int i = 0; i < nameList.size(); i++) {
				if (nameList.get(i).equals(name)) {
					sameId = true;
				}
			}
			if (!sameId) {
				nameList.add(name);
				// 송신
				sendAll(PROTOCOL.CHATTING + "-*** [" + name + "] 님 입장 !");
				accept_num++;

				if (isPlay) // 게임 도중 참가 불가
					sendAll(PROTOCOL.PROHIBIT + "-");
				// 메시지 분석
				while (true) {
					// 수신
					String str = reader.readLine();
					if (str == null)
						break;
					// 분석 후 송신
					analysisMessage(name, str);
				}
			}
		} catch (Exception e) {
			System.out.print("server : ");
			System.out.println(e.getMessage());
		} finally {
			// 리스트 삭제
			list.remove(writer);
			nameList.remove(name);
			// 접속자수 -1
			accept_num--;
			// 모두 퇴장시 게임 초기화
			if (accept_num == 0)
				InitGame();
			// 게임 도중 퇴장시(종료)
			if (isPlay) {
				//ready_num--;
				//bet_num--;
				//stay_num--;
				System.exit(0);	// 종료
			}
			// 송신
			sendAll(PROTOCOL.CHATTING + "-*** [" + name + "] 님 퇴장 !");

			try {
				socket.close();
			} catch (Exception ignored) {

			}
		}
	}

	// 리스트에 있는 모든 데이터 전송
	private void sendAll(String str) {
		for (PrintWriter writer : list) {
			writer.println(str);
			writer.flush();
		}
	}

	// 메시지 분석(아이디에 따름)
	public void analysisMessage(String name, String str) {
		// 메시지 분석을 통해 PROTOCOL 분류
		StringTokenizer st = new StringTokenizer(str, "-");
		String TYPE = st.nextToken(); // str의 시작부 적힌 PROTOCOL의 타입이 저장된다.

		if (PROTOCOL.CHATTING.equals(TYPE)) { // 채팅 데이터
			String chat = st.nextToken();
			sendAll(PROTOCOL.CHATTING + "-" + name + " > " + chat);
		} else if (PROTOCOL.READY.equals(TYPE)) { // 준비
			sendAll(PROTOCOL.CHATTING + "-***" + name + " 준비 완료!");

			// 준비자 정보 화면에 띄우기
			sendAll(PROTOCOL.PLAYER + "-" + name);

			ready_num++; // 준비자 수 증가
			if (accept_num == ready_num) // 준비 완료
			{
				sendAll(PROTOCOL.CHATTING + "-***전원 준비 완료!");
				isPlay = true; // 참가 제한
			}
		} else if (PROTOCOL.WAIT.equals(TYPE)) { // 대기
			sendAll(PROTOCOL.WAIT + "-");
		} else if (PROTOCOL.BET.equals(TYPE)) {
			int money = Integer.parseInt(st.nextToken());
			total_money += money;
			sendAll(PROTOCOL.CHATTING + "-***" + name + " 배팅 : ＄"
					+ String.valueOf(money));

			bet_num++;
			if (ready_num == bet_num) {
				sendAll(PROTOCOL.CHATTING + "-***전원 배팅완료!");
				sendAll(PROTOCOL.CHATTING + "-***총 배팅액 : ＄"
						+ String.valueOf(total_money));
				sendAll(PROTOCOL.CHATTING + "-***게임 시작!");

				// 게임 세팅(딜러가 카드를 분배함)
				for (int j = 0; j < 2; j++) {
					// 딜러용
					String dealer = dl.giveCard();
					sendAll(PROTOCOL.GETCARD + "-" + dealer);

					// 카드 정보 분석
					StringTokenizer dt = new StringTokenizer(dealer, "/");
					String ctype = dt.nextToken(); // 카드 종류
					String cname = dt.nextToken(); // 카드 번호
					String cvalue = dt.nextToken(); // 카드 값
					Card c_card = new Card(ctype, cname,
							Integer.parseInt(cvalue));
					cardList_dealer.add(c_card); // 전체 리스트에 추가

					// 인원수만큼 카드 배부
					for (int i = 0; i < ready_num; i++) {
						// 각 클라이언트의 이름 대로 송신
						sendAll(PROTOCOL.GETCARD + "-" + dl.giveCard());
					}
				}
			}
		} else if (PROTOCOL.HIT.equals(TYPE)) {
			sendAll(PROTOCOL.CHATTING + "-***" + name + " HIT!");

			// 카드 추가
			sendAll(PROTOCOL.GETCARD + "-" + dl.giveCard()); // 카드 한장 추가
			sendAll(PROTOCOL.HIT + "-" + name); // 누가?

		} else if (PROTOCOL.STAY.equals(TYPE)) {
			sendAll(PROTOCOL.CHATTING + "-***" + name + " STAY!");

			stay_num++; // 스테이 수 증가
			if (ready_num == stay_num) // 스테이 완료
			{
				sendAll(PROTOCOL.CHATTING + "-***전원 STAY!");

				// 딜러의 히트
				while (true) {
					int sum = 0;
					int ace_num = 0;
					
					for (int i = 0; i < cardList_dealer.size(); i++) {
						sum += cardList_dealer.get(i).getCardValue();
						if (cardList_dealer.get(i).getCardName().equals("1")) { // A인 경우
							ace_num++;
						}
					}
					for (int i = 0; i < ace_num; i++) {
						sum += 10; // A의 개수만큼 10을 더한다(11로 계산)
						if (sum > 21)
							sum -= 10; // 초과되면 1로 계산
					}
					
					if(sum < 17)	// 16 이하면 히트
					{
						String dealer = dl.giveCard();
						sendAll(PROTOCOL.GETCARD + "-" + dealer);	// 딜러 히트
						sendAll(PROTOCOL.HIT + "-" + "dealer"); // 누가?
						
						// 카드 정보 분석
						StringTokenizer dt = new StringTokenizer(dealer, "/");
						String ctype = dt.nextToken(); // 카드 종류
						String cname = dt.nextToken(); // 카드 번호
						String cvalue = dt.nextToken(); // 카드 값
						Card c_card = new Card(ctype, cname, Integer.parseInt(cvalue));
						cardList_dealer.add(c_card); // 전체 리스트에 추가
					}
					else
					{
						break;
					}
				}
				
				// 카드 공개
				sendAll(PROTOCOL.CHATTING + "-***전원 카드 공개!");
				sendAll(PROTOCOL.RESULT + "-");
				sendAll(PROTOCOL.WAIT + "-");
				sendAll(PROTOCOL.CHATTING + "-***게임 재시작 가능!");
				// 재시작
				sendAll(PROTOCOL.RESTART + "-");
				InitGame();
			}
		}
	}
}