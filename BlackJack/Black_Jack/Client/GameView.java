package Client;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.imageio.*;
import javax.swing.*;

import Server.Dealer.Cards;

public class GameView extends JComponent {
	private Image img_back, img_default, img_bj, img_burst, img_win, img_lose;
	private Image[] img_heart = new Image[13];
	private Image[] img_diamond = new Image[13];
	private Image[] img_clover = new Image[13];
	private Image[] img_spade = new Image[13];

	private boolean isStart = false;
	private boolean isHit = false;
	private boolean isResult = false;
	private String name_this = ""; // 현재 접속자의 이름(본인)
	private int player_num = 0; // 딜러 + 접속자 수 (플레이어 수)
	private int bet_this = 0; // 현재 접속자의 배팅액(본인)
	private int p_money = 0; // 잃은 돈 & 얻은 돈
	private Vector<String> nameList = new Vector<String>(); // 접속자의 이름을 저장할 배열
	private Vector<CCard> cardList_dealer = new Vector<CCard>(); // 딜러 카드
	private Vector<CCard> cardList_1 = new Vector<CCard>(); // 각자 카드
	private Vector<CCard> cardList_2 = new Vector<CCard>(); // 각자 카드
	private Vector<CCard> cardList_3 = new Vector<CCard>(); // 각자 카드
	private Vector<CCard> cardList = new Vector<CCard>(); // 받는 카드 전체

	public void initGame() {
		isStart = false;
		isHit = false;
		isResult = false;
		// name_this = "";
		player_num = 0;
		bet_this = 0;
		p_money = 0;
		nameList.removeAllElements();
		cardList_dealer.removeAllElements();
		cardList_1.removeAllElements();
		cardList_2.removeAllElements();
		cardList_3.removeAllElements();
		cardList.removeAllElements();

		repaint();
	}

	public void initComponent() {
		try {
			// 이미지 불러오기
			img_back = ImageIO.read(new File("img/card/back.png"));
			img_default = ImageIO.read(new File("img/card/default.png"));
			img_bj = ImageIO.read(new File("img/blackjack.png"));
			img_burst = ImageIO.read(new File("img/burst.png"));
			img_win = ImageIO.read(new File("img/win.png"));
			img_lose = ImageIO.read(new File("img/lose.png"));
			for (int i = 0; i < 13; i++) {
				img_heart[i] = ImageIO.read(new File("img/card/h"
						+ String.valueOf(i + 1) + ".png"));
				img_diamond[i] = ImageIO.read(new File("img/card/d"
						+ String.valueOf(i + 1) + ".png"));
				img_clover[i] = ImageIO.read(new File("img/card/c"
						+ String.valueOf(i + 1) + ".png"));
				img_spade[i] = ImageIO.read(new File("img/card/s"
						+ String.valueOf(i + 1) + ".png"));
			}
		} catch (IOException e) {
		}
	}

	public void setPlayer(String c) {
		name_this = c;
	}

	public void setBet(int m) {
		bet_this = m;
	}

	public int getMoney() {
		return p_money;
	}

	public void addPlayerName(String c) {
		nameList.add(c);
		player_num = nameList.size() + 1;
		repaint();
	}

	// 카드 리스트에 카드 추가
	public void addCard(String c) {
		// 카드 정보 분석
		StringTokenizer st = new StringTokenizer(c, "/");
		String type = st.nextToken(); // 카드 종류
		String name = st.nextToken(); // 카드 번호
		String value = st.nextToken(); // 카드 값
		CCard c_card = new CCard(type, Integer.parseInt(name),
				Integer.parseInt(value));

		cardList.add(c_card); // 전체 리스트에 추가

		if (cardList.size() == player_num * 2 && isHit == false) {
			isStart = true;
			// 점수계산용 리스트 추가
			// 딜러
			cardList_dealer.add(cardList.get(0));
			cardList_dealer.add(cardList.get(player_num));

			// 개인 카드 저장
			switch (player_num) {
			case 2:
				cardList_1.add(cardList.get(1));
				cardList_1.add(cardList.get(1 + player_num));
				break;
			case 3:
				cardList_1.add(cardList.get(1));
				cardList_1.add(cardList.get(1 + player_num));
				cardList_2.add(cardList.get(2));
				cardList_2.add(cardList.get(2 + player_num));
				break;
			case 4:
				cardList_1.add(cardList.get(1));
				cardList_1.add(cardList.get(1 + player_num));
				cardList_2.add(cardList.get(2));
				cardList_2.add(cardList.get(2 + player_num));
				cardList_3.add(cardList.get(3));
				cardList_3.add(cardList.get(3 + player_num));
				break;
			}

			repaint();
		}
	}

	// 히트 카드 추가
	public void addHitCard(String c) {
		isHit = true;

		switch (nameList.indexOf(c)) {
		case 0:
			cardList_1.add(cardList.get(cardList.size() - 1));
			break;
		case 1:
			cardList_2.add(cardList.get(cardList.size() - 1));
			break;
		case 2:
			cardList_3.add(cardList.get(cardList.size() - 1));
			break;
		default:
			cardList_dealer.add(cardList.get(cardList.size() - 1));
		}

		// cardList.remove(cardList.size() - 1);

		repaint();
	}

	// 게임 결과
	public void calScore() {
		isResult = true;

		// 돈은 각자 계산
		// 점수 계산
		int dealer_score = getValue("dealer");
		int p_score = getValue(name_this);

		// dealer burst
		if (dealer_score > 21) {
			dealer_score = 0;
		}
		// player burst
		if (p_score > 21) {
			p_score = 0;
		}

		// dealer blackjack
		if (dealer_score < 0) {
			p_money -= (bet_this * 2); // 무조건 딜러 승리
		}
		// player blackjack
		else if (p_score < 0) {
			p_money += (bet_this * 2); // 딜러가 블랙잭만 아니면 무조건 플레이어 승리
		}

		// win & lose
		if (dealer_score >= 0 && p_score >= 0) {
			if (p_score > dealer_score) {
				p_money += bet_this; // 딴 돈
			} else {
				p_money -= bet_this; // 잃은 돈
			}
		}

		repaint();
	}

	// 본인의 카드 점수
	public int getValue(String c) {
		int sum = 0;
		int ace_num = 0;

		switch (nameList.indexOf(c)) {
		case 0: {
			for (int i = 0; i < cardList_1.size(); i++) {
				sum += cardList_1.get(i).getCardValue();
				if (cardList_1.get(i).getCardName() == 1) { // A인 경우
					ace_num++;
				}
			}
			if (ace_num != 0) {
				for (int i = 0; i < ace_num; i++) {
					sum += 10; // A의 개수만큼 10을 더한다(11로 계산)
					if (sum > 21)
						sum -= 10; // 초과되면 1로 계산
				}
			}
		}
			break;
		case 1: {
			for (int i = 0; i < cardList_2.size(); i++) {
				sum += cardList_2.get(i).getCardValue();
				if (cardList_2.get(i).getCardName() == 1) { // A인 경우
					ace_num++;
				}
			}
			if (ace_num != 0) {
				for (int i = 0; i < ace_num; i++) {
					sum += 10; // A의 개수만큼 10을 더한다(11로 계산)
					if (sum > 21)
						sum -= 10; // 초과되면 1로 계산
				}
			}
		}
			break;
		case 2: {
			for (int i = 0; i < cardList_3.size(); i++) {
				sum += cardList_3.get(i).getCardValue();
				if (cardList_3.get(i).getCardName() == 1) { // A인 경우
					ace_num++;
				}
			}
			if (ace_num != 0) {
				for (int i = 0; i < ace_num; i++) {
					sum += 10; // A의 개수만큼 10을 더한다(11로 계산)
					if (sum > 21)
						sum -= 10; // 초과되면 1로 계산
				}
			}
		}
			break;
		default: {
			for (int i = 0; i < cardList_dealer.size(); i++) {
				sum += cardList_dealer.get(i).getCardValue();
				if (cardList_dealer.get(i).getCardName() == 1) { // A인 경우
					ace_num++;
				}
			}
			if (ace_num != 0) {
				for (int i = 0; i < ace_num; i++) {
					sum += 10; // A의 개수만큼 10을 더한다(11로 계산)
					if (sum > 21)
						sum -= 10; // 초과되면 1로 계산
				}
			}
		}
			break;
		}

		if (ace_num == 1 && sum == 21) {
			return -1; // 블랙잭
		}

		return sum;
	}

	public Image selectImage(CCard cc) {
		Image img_ccard = null;
		// 카드 이미지 검색
		if ((cc.getCardType()).equals("H")) {
			img_ccard = img_heart[cc.getCardName() - 1];
		} else if ((cc.getCardType()).equals("D")) {
			img_ccard = img_diamond[cc.getCardName() - 1];
		} else if ((cc.getCardType()).equals("C")) {
			img_ccard = img_clover[cc.getCardName() - 1];
		} else if ((cc.getCardType()).equals("S")) {
			img_ccard = img_spade[cc.getCardName() - 1];
		}
		return img_ccard;
	}

	// 기본 이미지
	protected void paintComponent(Graphics g) {
		// 기본 카드 이미지
		g.drawImage(img_default, 20, 50, 75, 107, this);
		g.drawImage(img_default, 180, 50, 75, 107, this);
		g.drawImage(img_default, 340, 50, 75, 107, this);
		g.drawImage(img_default, 500, 50, 75, 107, this);

		// 플레이어 아이디 출력
		g.drawString("<Dealer>", 35, 180);
		for (int i = 0; i < nameList.size(); i++) {
			g.drawString("<" + nameList.get(i) + ">", 202 + 162 * i, 180);
		}

		// 그리기
		if (isStart)
			gameStart(g);
		if (isHit)
			gameHit(g);
		if (isResult)
			gameResult(g);
	}

	public void gameStart(Graphics g) {
		// 그리기
		// 딜러 카드 1장 돌리기
		g.drawImage(img_back, 20, 50, 75, 107, this);
		// 플레이어 카드 1장째 돌리기(본인만 보인다)
		for (int i = 1; i < player_num; i++) {
			if (name_this.equals(nameList.get(i - 1))) { // 본인
				switch (nameList.indexOf(nameList.get(i - 1))) {
				case 0:
					g.drawImage(selectImage(cardList_1.get(0)), 20 + i * 160,
							50, 75, 107, this);
					break;
				case 1:
					g.drawImage(selectImage(cardList_2.get(0)), 20 + i * 160,
							50, 75, 107, this);
					break;
				case 2:
					g.drawImage(selectImage(cardList_3.get(0)), 20 + i * 160,
							50, 75, 107, this);
					break;
				default:
					break;
				}
			} else { // 나머지
				g.drawImage(img_back, 20 + i * 160, 50, 75, 107, this); // 타인 카드
			}
		}

		// 모두 카드 2장째 돌리기(모두 보인다)
		g.drawImage(selectImage(cardList_dealer.get(1)), 40, 50, 75, 107, this);
		switch (player_num) {
		case 2:
			g.drawImage(selectImage(cardList_1.get(1)), 40 + 1 * 160, 50, 75,
					107, this);
			break;
		case 3:
			g.drawImage(selectImage(cardList_1.get(1)), 40 + 1 * 160, 50, 75,
					107, this);
			g.drawImage(selectImage(cardList_2.get(1)), 40 + 2 * 160, 50, 75,
					107, this);
			break;
		case 4:
			g.drawImage(selectImage(cardList_1.get(1)), 40 + 1 * 160, 50, 75,
					107, this);
			g.drawImage(selectImage(cardList_2.get(1)), 40 + 2 * 160, 50, 75,
					107, this);
			g.drawImage(selectImage(cardList_3.get(1)), 40 + 3 * 160, 50, 75,
					107, this);
			break;
		}
	}

	public void gameHit(Graphics g) {
		// 히트 카드 그리기
		switch (player_num) {
		case 2:
			for (int i = 2; i < cardList_dealer.size(); i++)
				g.drawImage(selectImage(cardList_dealer.get(i)), 20 + 20 * i,
						50, 75, 107, this);
			for (int i = 2; i < cardList_1.size(); i++)
				g.drawImage(selectImage(cardList_1.get(i)), 20 + 20 * i + 1
						* 160, 50, 75, 107, this);
			break;
		case 3:
			for (int i = 2; i < cardList_dealer.size(); i++)
				g.drawImage(selectImage(cardList_dealer.get(i)), 20 + 20 * i,
						50, 75, 107, this);
			for (int i = 2; i < cardList_1.size(); i++)
				g.drawImage(selectImage(cardList_1.get(i)), 20 + 20 * i + 1
						* 160, 50, 75, 107, this);
			for (int i = 2; i < cardList_2.size(); i++)
				g.drawImage(selectImage(cardList_2.get(i)), 20 + 20 * i + 2
						* 160, 50, 75, 107, this);
			break;
		case 4:
			for (int i = 2; i < cardList_dealer.size(); i++)
				g.drawImage(selectImage(cardList_dealer.get(i)), 20 + 20 * i,
						50, 75, 107, this);
			for (int i = 2; i < cardList_1.size(); i++)
				g.drawImage(selectImage(cardList_1.get(i)), 20 + 20 * i + 1
						* 160, 50, 75, 107, this);
			for (int i = 2; i < cardList_2.size(); i++)
				g.drawImage(selectImage(cardList_2.get(i)), 20 + 20 * i + 2
						* 160, 50, 75, 107, this);
			for (int i = 2; i < cardList_3.size(); i++)
				g.drawImage(selectImage(cardList_3.get(i)), 20 + 20 * i + 3
						* 160, 50, 75, 107, this);
			break;
		}
	}

	public void gameResult(Graphics g) {
		switch (player_num) {
		case 2:
			for (int i = 0; i < cardList_dealer.size(); i++)
				g.drawImage(selectImage(cardList_dealer.get(i)), 20 + 20 * i,
						50, 75, 107, this);
			for (int i = 0; i < cardList_1.size(); i++)
				g.drawImage(selectImage(cardList_1.get(i)), 20 + 20 * i + 1
						* 160, 50, 75, 107, this);
			break;
		case 3:
			for (int i = 0; i < cardList_dealer.size(); i++)
				g.drawImage(selectImage(cardList_dealer.get(i)), 20 + 20 * i,
						50, 75, 107, this);
			for (int i = 0; i < cardList_1.size(); i++)
				g.drawImage(selectImage(cardList_1.get(i)), 20 + 20 * i + 1
						* 160, 50, 75, 107, this);
			for (int i = 0; i < cardList_2.size(); i++)
				g.drawImage(selectImage(cardList_2.get(i)), 20 + 20 * i + 2
						* 160, 50, 75, 107, this);
			break;
		case 4:
			for (int i = 0; i < cardList_dealer.size(); i++)
				g.drawImage(selectImage(cardList_dealer.get(i)), 20 + 20 * i,
						50, 75, 107, this);
			for (int i = 0; i < cardList_1.size(); i++)
				g.drawImage(selectImage(cardList_1.get(i)), 20 + 20 * i + 1
						* 160, 50, 75, 107, this);
			for (int i = 0; i < cardList_2.size(); i++)
				g.drawImage(selectImage(cardList_2.get(i)), 20 + 20 * i + 2
						* 160, 50, 75, 107, this);
			for (int i = 0; i < cardList_3.size(); i++)
				g.drawImage(selectImage(cardList_3.get(i)), 20 + 20 * i + 3
						* 160, 50, 75, 107, this);
			break;
		}

		int dealer_score = getValue("dealer");
		int p1_score = 0;
		int p2_score = 0;
		int p3_score = 0;

		// dealer burst
		if (dealer_score > 21) {
			dealer_score = 0;
			g.drawImage(img_burst, 20, 80, 100, 69, this);
		}
		// dealer blackjack
		if (dealer_score < 0) {
			g.drawImage(img_bj, 20, 80, 80, 80, this);
		}

		switch (player_num) {
		case 2:
			p1_score = getValue(nameList.get(0));
			// burst
			if (p1_score > 21) {
				p1_score = 0;
				g.drawImage(img_burst, 20 + 160, 80, 100, 69, this);
			}
			// dealer blackjack
			if (dealer_score < 0) {
				g.drawImage(img_lose, 20 + 160, 80, 100, 73, this);
			}
			// blackjack
			else if (p1_score < 0) {
				g.drawImage(img_bj, 20 + 160, 80, 80, 80, this);
			}
			// win & lose
			if (dealer_score >= 0 && p1_score >= 0 && p1_score != 0) {
				if (p1_score > dealer_score) {
					g.drawImage(img_win, 20 + 160, 80, 100, 66, this);
				} else {
					g.drawImage(img_lose, 20 + 160, 80, 100, 73, this);
				}
			}
			break;
		case 3:
			p1_score = getValue(nameList.get(0));
			p2_score = getValue(nameList.get(1));

			// burst
			if (p1_score > 21) {
				p1_score = 0;
				g.drawImage(img_burst, 20 + 160, 80, 100, 69, this);
			}
			if (p2_score > 21) {
				p2_score = 0;
				g.drawImage(img_burst, 20 + 320, 80, 100, 69, this);
			}
			// dealer blackjack
			if (dealer_score < 0) {
				g.drawImage(img_lose, 20 + 160, 80, 100, 73, this);
			}
			// blackjack
			else if (p1_score < 0) {
				g.drawImage(img_bj, 20 + 160, 80, 80, 80, this);
			}
			// dealer blackjack
			if (dealer_score < 0) {
				g.drawImage(img_lose, 20 + 320, 80, 100, 73, this);
			}
			// blackjack
			else if (p2_score < 0) {
				g.drawImage(img_bj, 20 + 320, 80, 80, 80, this);
			}
			// win & lose
			if (dealer_score >= 0 && p1_score >= 0 && p1_score != 0) {
				if (p1_score > dealer_score) {
					g.drawImage(img_win, 20 + 160, 80, 100, 66, this);
				} else {
					g.drawImage(img_lose, 20 + 160, 80, 100, 73, this);
				}
			}
			if (dealer_score >= 0 && p2_score >= 0 && p2_score != 0) {
				if (p2_score > dealer_score) {
					g.drawImage(img_win, 20 + 320, 80, 100, 66, this);
				} else {
					g.drawImage(img_lose, 20 + 320, 80, 100, 73, this);
				}
			}
			break;
		case 4:
			p1_score = getValue(nameList.get(0));
			p2_score = getValue(nameList.get(1));
			p3_score = getValue(nameList.get(2));

			// burst
			if (p1_score > 21) {
				p1_score = 0;
				g.drawImage(img_burst, 20 + 160, 80, 100, 69, this);
			}
			if (p2_score > 21) {
				p2_score = 0;
				g.drawImage(img_burst, 20 + 320, 80, 100, 69, this);
			}
			if (p3_score > 21) {
				p3_score = 0;
				g.drawImage(img_burst, 20 + 480, 80, 100, 69, this);
			}
			// dealer blackjack
			if (dealer_score < 0) {
				g.drawImage(img_lose, 20 + 160, 80, 100, 73, this);
			}
			// blackjack
			else if (p1_score < 0) {
				g.drawImage(img_bj, 20 + 160, 80, 80, 80, this);
			}
			// dealer blackjack
			if (dealer_score < 0) {
				g.drawImage(img_lose, 20 + 320, 80, 100, 73, this);
			}
			// blackjack
			else if (p2_score < 0) {
				g.drawImage(img_bj, 20 + 320, 80, 80, 80, this);
			}
			// dealer blackjack
			if (dealer_score < 0) {
				g.drawImage(img_lose, 20 + 480, 80, 100, 73, this);
			}
			// blackjack
			else if (p3_score < 0) {
				g.drawImage(img_bj, 20 + 480, 80, 80, 80, this);
			}
			// win & lose
			if (dealer_score >= 0 && p1_score >= 0 && p1_score != 0) {
				if (p1_score > dealer_score) {
					g.drawImage(img_win, 20 + 160, 80, 100, 66, this);
				} else {
					g.drawImage(img_lose, 20 + 160, 80, 100, 73, this);
				}
			}
			if (dealer_score >= 0 && p2_score >= 0 && p2_score != 0) {
				if (p2_score > dealer_score) {
					g.drawImage(img_win, 20 + 320, 80, 100, 66, this);
				} else {
					g.drawImage(img_lose, 20 + 320, 80, 100, 73, this);
				}
			}
			if (dealer_score >= 0 && p3_score >= 0 && p3_score != 0) {
				if (p3_score > dealer_score) {
					g.drawImage(img_win, 20 + 480, 80, 100, 66, this);
				} else {
					g.drawImage(img_lose, 20 + 480, 80, 100, 73, this);
				}
			}

			break;
		}
	}
}