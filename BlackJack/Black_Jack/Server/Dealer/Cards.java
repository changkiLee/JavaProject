package Server.Dealer;

import java.util.Random;

//한 종류의 카드를 나타내는 클래스
public class Cards {
	private Random rd = new Random();
	private Card c[] = new Card[13];
	private int availCount = 13;

	@SuppressWarnings("unused")
	private Cards() {
	}

	Cards(String type) {
		for (int i = 1; i < 14; i++) {
			c[i - 1] = new Card(type, String.valueOf(i), i > 10 ? 10 : i);
		}
	}

	public Card getRandomCard() {
		Card result = c[rd.nextInt(13)]; // 한종류의 카드에서 랜덤한 한장 뽑기

		// 뽑은 카드가 사용된 카드가 아니면 리턴
		if (!result.getIsUsed()) {
			result.setIsUsed(true); // 사용됨
			availCount--; // 가용 카드 리턴시 가용 카드수 -1
			return result;
		}

		return null; // 사용할수 있는 카드가 없음
	}

	public int getAvailCount() {
		return availCount;
	}
}
