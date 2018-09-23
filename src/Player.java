import java.util.ArrayList;

public class Player {

    private ArrayList<Card> playerCards;
    private int playerBalance;
    private String name;
    private int totalBetted;
    private int playerStanding;
    private ArrayList<Integer> goodCardsInt;
    private int totalWon;
    private int highestUniqueCard;
    private int rank;

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

    void setPlayerHighlightCards(ArrayList<Integer> goodCardsInt) {
        this.goodCardsInt = goodCardsInt;
    }

    ArrayList<Integer> getPlayerHighlightCards() {
        return goodCardsInt;
    }

    void setPlayerHighestUniqueCard(int highestUniqueCard) {
        this.highestUniqueCard = highestUniqueCard;
    }

    void setTotalWon(int totalWon) {
        this.totalWon = totalWon;
    }

    int getTotalWon() {
        return totalWon;
    }

    int getPlayerHandRank() {
        return rank;
    }


    int getPlayerStanding() {
        return playerStanding;
    }



    public String toString() {
        return (name);
    }
}
