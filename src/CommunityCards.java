import java.util.ArrayList;

public class CommunityCards {

    private ArrayList<Card> communityCards = new ArrayList<>();

    CommunityCards() {

    }

    void setTableCards(ArrayList<Card> tableCards) {
        communityCards.addAll(tableCards);
        for (Card tableCard : tableCards) {
            System.out.println(tableCard.toString());
        }
    }

    ArrayList<Card> getTableCards() {
        return communityCards;
    }

}
