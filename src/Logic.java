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
    private boolean isFlush;
    private static final String[] cardString = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private static Map<String, Integer> cardIntValue = new HashMap<>() {{
        for (int i = 0; i < cardString.length; i++) {
            put(cardString[i], i + 1);
        }
    }};
    private static Map<Player, Integer[]> handRankingSystem =  new HashMap<>();
    private int handRank = 0;
    private int highCardRank = 0;


    public Logic(ArrayList<Card> cardsToCompare, Player player) {
        this.cardsToCompare = cardsToCompare;
        this.player = player;
        handRankingSystem.put(player, new Integer[]{handRank, highCardRank});
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

    Map<Player, Integer[]> findingPairs(int[] cardsInt) {
        Arrays.sort(cardsInt);

        for (int i = 1; i < cardsInt.length; i++) {
            //pair
            if (cardsInt[i] == cardsInt[i - 1]) {
                if (i == 6) {
                    rankSetter(2);
                    highCardRank = (cardsInt[i] * 2);
                } else {
                    if (cardsInt[i] == cardsInt[i + 1]) {
                        if (i == cardsInt.length - 2) {
                            rankSetter(4);
                            highCardRank = (cardsInt[i] * 3);
                        } else {
                            //four of a kind
                            if (cardsInt[i] == cardsInt[i + 2]) {
                                rankSetter(8);
                                highCardRank = (cardsInt[i] * 4);
                                continue;
                            }
                        }
                    }
                }
                rankSetter(2);
                //full house
                for (int j = i; j < cardsInt.length - 2; j++) {
                    if (cardsInt[j] == cardsInt[j + 1]) {
                        if (cardsInt[j] == cardsInt[j + 2]) {
                            rankSetter(7);
                            highCardRank =+ cardsInt[j] * 3;
                        }
                    }
                }
                //two pair
                for (int j = i; j < cardsInt.length - 1; j++) {
                    if (cardsInt[j] == cardsInt[j + 1]) {
                        rankSetter(3);
                        highCardRank =+ cardsInt[j] * 2;
                    }
                }
            }
        }
        handRankingSystem.put(player, new Integer[]{handRank, highCardRank});
        return handRankingSystem;
    }

    Map<Player, Integer[]> findStraights(int[] cardsInt, String[] cardsSuit) {
        Map<Integer, String> cardAndSuit = new HashMap<>();
        for (int i = 0; i < cardsToCompare.size(); i++) {
            cardAndSuit.put(cardsInt[i], cardsSuit[i]);
        }
        Map<Integer, String> sortedCardAndSuit = new TreeMap<>(cardAndSuit);
        int[] cardsIntUnique = Arrays.stream(cardsInt).distinct().toArray();
        Arrays.sort(cardsIntUnique);
        int k = 0;
        ArrayList<Integer> storedCardsUniqueInt = new ArrayList<>();


        boolean passed;
        boolean notStraight = true;
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
            if (j < cardsIntUnique.length - 2 && k != 0) {
                notStraight = cardsIntUnique[j] + 2 != cardsIntUnique[j + 2];
            }
            if (k >= 4 && notStraight) {
                storedCardsUniqueInt.add(cardsIntUnique[j+1]);
                System.out.println(storedCardsUniqueInt);
                int straightLength = storedCardsUniqueInt.size();
                if (straightLength > 5) {
                    for (int x = 0; x < straightLength - 5; x++) {
                        storedCardsUniqueInt.remove(x);
                    }
                }
                Object[] keysForMap = sortedCardAndSuit.keySet().toArray();
                int counterMap = sortedCardAndSuit.size();
                for (int r = 0 ; r < counterMap; r++) {
                    if (!storedCardsUniqueInt.contains(keysForMap[r])) {
                        sortedCardAndSuit.remove(keysForMap[r]);
                    }
                }
                for (Integer aStoredCardsUniqueInt : storedCardsUniqueInt) {
                    highCardRank = aStoredCardsUniqueInt;
                }
                if (isFlush) {
                    rankSetter(9);
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
                        rankSetter(10);
                    }
                }
                rankSetter(5);
            }
        }

        handRankingSystem.put(player,new Integer[]{handRank,highCardRank});
        return handRankingSystem;
    }

    Map<Player, Integer[]> findFlush(int[] cardsInt, String[] cardsSuit) {
        Map<String, Integer> suitAndQty = new HashMap<>();
        Map<String, List<Integer>> suitAndCard = new HashMap<>();
        int s = 0;
        for (String x : cardsSuit) {
            if (!suitAndQty.containsKey(x)) {
                suitAndQty.put(x, 1);
                suitAndCard.put(x, new ArrayList<>());
                suitAndCard.get(x).add(cardsInt[s]);
            }
            else {
                suitAndQty.put(x, suitAndQty.get(x) + 1);
                suitAndCard.get(x).add(cardsInt[s]);
                suitAndCard.put(x, suitAndCard.get(x));
            }
            s++;
        }
        isFlush = false;
        for (String key : suitAndQty.keySet()) {
            if (suitAndQty.get(key) >= 5) {
                rankSetter(6);
                for (int c = 0; c < suitAndQty.get(key); c++) {
                    int a = suitAndCard.get(key).get(c);
                    highCardRank += a;
                }
            }
        }
        handRankingSystem.put(player,new Integer[]{handRank, highCardRank});
        return handRankingSystem;
    }

    private void rankSetter(int tempRank) {
        if (tempRank > handRank) {
            handRank = tempRank;
        }
    }

    private String getCardHand(int value) {
        return cardString[value - 1];
    }
}