import java.util.ArrayList;

public class CommunityCards {

    private ArrayList<Card> communityCards = new ArrayList<>();

    CommunityCards() {

    }

    void setTableCards(ArrayList<Card> tableCards) {
        communityCards.addAll(tableCards);
        System.out.println(communityCards.toString());
    }

    ArrayList<Card> getTableCards() {
        return communityCards;
    }

}
