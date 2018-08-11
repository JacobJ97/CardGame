import java.util.ArrayList;

public class Player {

    private ArrayList<Card> playerCards;
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public void setPlayerCards(ArrayList<Card> playerCards) {
        this.playerCards = playerCards;
        if (this.name == "Player_ID") {
            for (Card playerCard : playerCards) {
                System.out.println(playerCard.toString());
            }
        }
    }

    public String toString() {
        return (name + ": Your cards are \n" + playerCards);
    }
}
