package Server.Dealer;

import java.util.Random;

//�� ������ ī�带 ��Ÿ���� Ŭ����
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
		Card result = c[rd.nextInt(13)]; // �������� ī�忡�� ������ ���� �̱�

		// ���� ī�尡 ���� ī�尡 �ƴϸ� ����
		if (!result.getIsUsed()) {
			result.setIsUsed(true); // ����
			availCount--; // ���� ī�� ���Ͻ� ���� ī��� -1
			return result;
		}

		return null; // ����Ҽ� �ִ� ī�尡 ����
	}

	public int getAvailCount() {
		return availCount;
	}
}
