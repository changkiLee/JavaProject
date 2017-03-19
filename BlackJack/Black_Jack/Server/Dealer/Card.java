package Server.Dealer;

//ī�� ������ ��Ÿ���� Ŭ����
public class Card {
	final static String HEART = "H";
	final static String DIAMOND = "D";
	final static String CLOVER = "C";
	final static String SPADE = "S";

	private String cardType = "";	// ī�� ����
	private String cardName = ""; // ī�� �̸�
	private int cardValue = 0; // ī�� ����
	private boolean isUsed = false; // �������

	@SuppressWarnings("unused")
	private Card() {
	}

	public Card(String type, String name, int value) {
		this.cardType = type;
		this.cardName = name;
		this.cardValue = value;
		this.isUsed = false;
	}
	
	public String getCardType() {
		return cardType;
	}

	public String getCardName() {
		return cardName;
	}

	public int getCardValue() {
		return cardValue;
	}

	public boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(boolean d) {
		this.isUsed = d;
	}
}