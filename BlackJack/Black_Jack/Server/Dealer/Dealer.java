package Server.Dealer;

public class Dealer {
	private int money = 0;
	private CardSet card = null;

	public Dealer() {
		money = 1000; // 무한이라 가정
		card = new CardSet();
	}

	public String giveCard() {
		Card rd_card = card.getRandomCard();
		String card_Info = rd_card.getCardType() + "/" + rd_card.getCardName()
				+ "/" + String.valueOf(rd_card.getCardValue() + "/");
		return card_Info;
	}

	public int getDealerMoney() {
		return money;
	}
}
