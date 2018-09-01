import java.util.ArrayList;

public class Player {

    private ArrayList<Card> playerCards;
    private int playerBalance;
    private String name;

    public Player(String name, int playerBalance) {
        this.name = name;
        this.playerBalance = playerBalance;
    }

    public void setPlayerCards(ArrayList<Card> playerCards) {
        this.playerCards = playerCards;
        if (this.name.equals("Player_ID")) {
            for (Card playerCard : playerCards) {
                System.out.println(playerCard.toString());
            }
        }
    }

    void fold() {
        this.playerCards.clear();
    }

    public int getPlayerBalance() {
        return playerBalance;
    }

    public void setPlayerBalance(int playerBalance) {
        this.playerBalance = playerBalance;
    }

    public String toString() {
        return (name + ": Your cards are \n" + playerCards);
    }
}
