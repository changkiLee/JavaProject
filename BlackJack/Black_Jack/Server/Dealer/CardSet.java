package Server.Dealer;

import java.util.HashMap;
import java.util.Random;


// ī�� �ѹ�ġ�� ��Ÿ���� Ŭ����
public class CardSet {
	private Random rd = new Random();
	// ��Ʈ, ���̾Ƹ��, Ŭ�ι�, �����̵尡 �� �ؽø�
	private HashMap<String, Cards> hm = new HashMap<String, Cards>();
	// �ʱ� ��� ������ ī�� ��Ʈ ��(52)
	private int availCount = 52;

	CardSet() {
		hm.put(Card.HEART, new Cards(Card.HEART));
		hm.put(Card.DIAMOND, new Cards(Card.DIAMOND));
		hm.put(Card.CLOVER, new Cards(Card.CLOVER));
		hm.put(Card.SPADE, new Cards(Card.SPADE));
	}

	// ���� ī�� �̱�
	public Card getRandomCard() {
		Cards cards = null; // ���õ� ����
		Card card = null; // ���õ� �������� ���õ� ī��

		while (availCount > 0) { // ���� �� �ִ� ī�尡 ������
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

			if (cards.getAvailCount() != 0) { // ���õ� ������ ���� ī����� 0�� �ƴϾ����
				card = cards.getRandomCard(); // ���� ī�� �̱�
				if (card != null) {
					this.availCount--;	// ���� ī�� ���Ͻ� ���� ī��� -1
					return card;		// ���� ī�� ����
				}
			}
		}
		return null; // ������ �ִ� ī�尡 ����
	}
}