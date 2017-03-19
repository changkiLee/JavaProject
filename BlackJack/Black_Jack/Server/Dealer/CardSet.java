package Server.Dealer;

import java.util.HashMap;
import java.util.Random;


// 카드 한뭉치를 나타내는 클래스
public class CardSet {
	private Random rd = new Random();
	// 하트, 다이아몬드, 클로버, 스페이드가 들어갈 해시맵
	private HashMap<String, Cards> hm = new HashMap<String, Cards>();
	// 초기 사용 가능한 카드 셋트 수(52)
	private int availCount = 52;

	CardSet() {
		hm.put(Card.HEART, new Cards(Card.HEART));
		hm.put(Card.DIAMOND, new Cards(Card.DIAMOND));
		hm.put(Card.CLOVER, new Cards(Card.CLOVER));
		hm.put(Card.SPADE, new Cards(Card.SPADE));
	}

	// 랜덤 카드 뽑기
	public Card getRandomCard() {
		Cards cards = null; // 선택된 종류
		Card card = null; // 선택된 종류에서 선택된 카드

		while (availCount > 0) { // 뽑을 수 있는 카드가 있을때
			switch (rd.nextInt(4)) {
			case 0:
				cards = hm.get(Card.HEART);
				break;
			case 1:
				cards = hm.get(Card.DIAMOND);
				break;
			case 2:
				cards = hm.get(Card.CLOVER);
				break;
			case 3:
				cards = hm.get(Card.SPADE);
				break;
			}

			if (cards.getAvailCount() != 0) { // 선택된 종류의 가용 카드수가 0이 아니어야함
				card = cards.getRandomCard(); // 랜덤 카드 뽑기
				if (card != null) {
					this.availCount--;	// 가용 카드 리턴시 가용 카드수 -1
					return card;		// 뽑힌 카드 리턴
				}
			}
		}
		return null; // 뽑을수 있는 카드가 없음
	}
}