package Server.Dealer;

//카드 한장을 나타내는 클래스
public class Card {
	final static String HEART = "H";
	final static String DIAMOND = "D";
	final static String CLOVER = "C";
	final static String SPADE = "S";

	private String cardType = "";	// 카드 종류
	private String cardName = ""; // 카드 이름
	private int cardValue = 0; // 카드 점수
	private boolean isUsed = false; // 사용유무

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