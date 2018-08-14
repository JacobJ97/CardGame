import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private static final int NUM_OF_SUITS = 4;
    private static final int NUM_OF_RANKS = 13;
    private static final int NUM_OF_DECKS = 1;
    private ArrayList<Card> cards;

    /**
     * Traditional
     */

    public Deck() {
        cards = new ArrayList<>();

        for (int i = 0; i < NUM_OF_SUITS; i++) {
            for (int j = 0; j < NUM_OF_RANKS; j++) {
                cards.add(new Card(j, i));
            }
        }
        System.out.println(cards);
        Collections.shuffle(cards);
        System.out.println(cards);
    }

    ArrayList<Card> dealCards(int numDealt) {
        ArrayList<Card> cardsDealt = new ArrayList<>();
        for (int i = 0; i < numDealt; i++) {
            Card pickedUpCard = cards.remove(i);
            cardsDealt.add(pickedUpCard);
        }
        return cardsDealt;
    }
}
