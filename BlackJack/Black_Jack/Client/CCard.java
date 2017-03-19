package Client;

//카드 한장을 나타내는 클래스
public class CCard {
	private String cardType = "";	// 카드 종류
	private int cardName = 0; // 카드 이름
	private int cardValue = 0; // 카드 점수

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