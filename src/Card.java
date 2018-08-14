public class Card {

    private static String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private static String[] suits = {"Spades", "Diamonds", "Clubs", "Hearts"};

    private String rank;
    private String suit;

    public Card (int rankIndex, int suitIndex) {
        rank = ranks[rankIndex];
        suit = suits[suitIndex];
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public String toString() {
        return rank + " of " + suit;
    }
}
