import java.util.ArrayList;

public class Player {

    private ArrayList<Card> playerCards;
    private int playerBalance;
    private String name;

    Player(String name, int playerBalance) {
        this.name = name;
        this.playerBalance = playerBalance;
    }

    void setPlayerCards(ArrayList<Card> playerCards) {
        this.playerCards = playerCards;
        System.out.println(name + " has the cards: ");
        for (Card playerCard : playerCards) {
            System.out.println(playerCard);
        }
    }

    public ArrayList<Card> getPlayerCards() {
        return playerCards;
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
        return (name);
    }
}
