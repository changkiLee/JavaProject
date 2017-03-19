package Client;

//ī�� ������ ��Ÿ���� Ŭ����
public class CCard {
	private String cardType = "";	// ī�� ����
	private int cardName = 0; // ī�� �̸�
	private int cardValue = 0; // ī�� ����

	@SuppressWarnings("unused")
	private CCard() {
	}

	public CCard(String type, int name, int value) {
		this.cardType = type;
		this.cardName = name;
		this.cardValue = value;
	}
	
	public String getCardType() {
		return cardType;
	}

	public int getCardName() {
		return cardName;
	}

	public int getCardValue() {
		return cardValue;
	}
}