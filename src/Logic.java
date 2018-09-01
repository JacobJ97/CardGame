import java.util.*;

public class Logic {

    /**
     * Rankings
     * ========
     * 1 - Royal Flush (10, J, Q, K, A of the same suit)
     * 2 - Straight Flush (straight with the same suit)
     * 3 - Four of a kind (Hand with four of the same number)
     * 4 - Full House (3 of a kind, and a pair)
     * 5 - Flush (5 numbers that have same suit)
     * 6 - Straight (5 numbers in a row [2, 3, 4, 5, 6])
     * 7 - Three of a Kind (hand with three of the same number)
     * 8 - Two Pair (two pairs)
     * 9 - One Pair (hand with two of the same number)
     * 10 - High Card (whatever hand is highest)
     */

    private ArrayList<Card> cardsToCompare;
    private Player player;
    private static final String[] cardString = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private static Map<String, Integer> cardIntValue = new HashMap<>() {{
        for (int i = 0; i < cardString.length; i++) {
            put(cardString[i], i + 1);
        }
    }};
    private int handRank = 0;


    public Logic(ArrayList<Card> cardsToCompare, Player player) {
        this.cardsToCompare = cardsToCompare;
        this.player = player;
    }

    private int getCardIntValue(String rank) {
        return cardIntValue.get(rank);
    }

    Object[] determineHandRanking() {
        int i = 0;
        String[] cardSuitArray = new String[cardsToCompare.size()];
        int[] cardIntArray = new int[cardsToCompare.size()];
        for (Card card : cardsToCompare) {
            cardSuitArray[i] = card.getSuit();
            cardIntArray[i] = getCardIntValue(card.getRank());
            i++;
        }
        return new Object[] {cardSuitArray, cardIntArray};
    }

    Map<Player, Integer> findingPairs(int[] cardsInt) {
        Arrays.sort(cardsInt);

        Map<Player, Integer> handRankingSystem = new HashMap<>();

        for (int i = 1; i < cardsInt.length; i++) {
            int tempRank;
            //pair
            if (cardsInt[i] == cardsInt[i - 1]) {
                if (i == 6) {
                    tempRank = 1;
                    if (tempRank > handRank) {
                        handRank = tempRank;
                    }
                } else {
                    if (cardsInt[i] == cardsInt[i + 1]) {
                        if (i == cardsInt.length - 2) {
                            tempRank = 3;
                            if (tempRank > handRank) {
                                handRank = tempRank;
                            }
                        } else {
                            //four of a kind
                            if (cardsInt[i] == cardsInt[i + 2]) {
                                tempRank = 7;
                                if (tempRank > handRank) {
                                    handRank = tempRank;
                                }
                                continue;
                            }
                        }
                    }
                }
                tempRank = 1;
                if (tempRank > handRank) {
                    handRank = tempRank;
                }
                //three pair
                for (int j = i; j < cardsInt.length - 2; j++) {
                    if (cardsInt[j] == cardsInt[j + 1]) {
                        if (cardsInt[j] == cardsInt[j + 2]) {
                            tempRank = 6;
                            if (tempRank > handRank) {
                                handRank = tempRank;
                            }
                        }
                    }
                }
                //two pair
                for (int j = i; j < cardsInt.length - 1; j++) {
                    if (cardsInt[j] == cardsInt[j + 1]) {
                        tempRank = 2;
                        if (tempRank > handRank) {
                            handRank = tempRank;
                        }
                    }
                }
            }
        }
        handRankingSystem.put(player, handRank);
        return handRankingSystem;
    }

    Map<Player, Integer> findStraights(int[] cardsInt, String[] cardsSuit) {
        Map<Integer, String> cardAndSuit = new HashMap<>();
        for (int i = 0; i < cardsToCompare.size(); i++) {
            cardAndSuit.put(cardsInt[i], cardsSuit[i]);
        }
        Map<Integer, String> sortedCardAndSuit = new TreeMap<>(cardAndSuit);
        int[] cardsIntUnique = Arrays.stream(cardsInt).distinct().toArray();
        Arrays.sort(cardsIntUnique);
        int k = 0;
        ArrayList<Integer> storedCardsUniqueInt = new ArrayList<>();
        Map<Player, Integer> handRankingSystem = new HashMap<>();

        int tempRank;
        boolean passed;
        for (int j = 0; j < cardsIntUnique.length - 1; j++) {
            if ((cardsIntUnique[j] + 1) == cardsIntUnique[j+1]) {
                storedCardsUniqueInt.add(cardsIntUnique[j]);
                k++;
                passed = true;
            } else {
                storedCardsUniqueInt.clear();
                passed = false;
            }
            if (!passed) {
                k = 0;
            }
            else if (k == 4) {
                storedCardsUniqueInt.add(cardsIntUnique[j+1]);
                Object[] keysForMap = sortedCardAndSuit.keySet().toArray();
                int counterMap = sortedCardAndSuit.size();
                for (int r = 0 ; r < counterMap; r++) {
                    if (!storedCardsUniqueInt.contains(keysForMap[r])) {
                        sortedCardAndSuit.remove(keysForMap[r]);
                    }
                }
                String[] suitsArray = new String[5];
                for (int l = 0; l < storedCardsUniqueInt.size(); l++) {
                    int card = storedCardsUniqueInt.get(l);
                    String suit = sortedCardAndSuit.get(card);
                    suitsArray[l] = suit;
                }
                String[] suitsArrayUnique = new HashSet<>(Arrays.asList(suitsArray)).toArray(new String[0]);
                if (suitsArrayUnique.length == 1) {
                    tempRank = 9;
                    if (tempRank > handRank) {
                        handRank = tempRank;
                    }
                    int x = 0;
                    for (int m = 0; m < storedCardsUniqueInt.size(); m++) {
                        String cardFaceValue = getCardHand(storedCardsUniqueInt.get(m));
                        if (cardFaceValue.equals(getCardHand(9 + m))) {
                            x++;
                        } else {
                            break;
                        }
                    }
                    if (x == 5) {
                        tempRank = 10;
                        if (tempRank > handRank) {
                            handRank = tempRank;
                        }
                    }
                }
                tempRank = 4;
                if (tempRank > handRank) {
                    handRank = tempRank;
                }
            }
        }

        handRankingSystem.put(player,handRank);
        return handRankingSystem;
    }


    private String getCardHand(int value) {
        return cardString[value - 1];
    }

}
