import java.util.ArrayList;

public class Player {

    private ArrayList<Card> playerCards;
    private int playerBalance;
    private String name;
    private int totalBetted;
    private int playerStanding;
    private int rank;
    private int secondaryRank;
    private int thirdRank;

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

    ArrayList<Card> getPlayerCards() {
        return playerCards;
    }

    void fold() {
        this.playerCards.clear();
    }

    void setTotalBetted(int bet) {
        totalBetted += bet;
    }

    int getTotalBetted() {
        return totalBetted;
    }

    int getPlayerBalance() {
        return playerBalance;
    }

    void setPlayerBalance(int playerBalance) {
        this.playerBalance = playerBalance;
    }

    void setPlayerRank(int rank) {
        this.rank = rank;
    }

    void setPlayerStanding(int playerStanding) {
        this.playerStanding = playerStanding;
    }

    int getPlayerStanding() {
        return playerStanding;
    }



    public String toString() {
        return (name);
    }
}
