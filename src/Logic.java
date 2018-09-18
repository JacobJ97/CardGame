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
    private boolean isStraight;
    private Object[] cardInfo;
    private static final String[] cardString = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private static Map<String, Integer> cardIntValue = new HashMap<>() {{
        for (int i = 0; i < cardString.length; i++) {
            put(cardString[i], i + 1);
        }
    }};
    private int handRank = 0;
    private ArrayList<Integer> highCardNumbers = new ArrayList<>();
    private ArrayList<Integer> highCardNumbersSeperate = new ArrayList<>();
    private boolean twoPair;
    private boolean fullHouse;
    private boolean fourOfAKind;
    private boolean threeOfAKind;
    private String flushSuit;
    private boolean sameSuit;


    public Logic(ArrayList<Card> cardsToCompare, Player player) {
        this.cardsToCompare = cardsToCompare;
        this.player = player;
        cardInfo = determineHandRanking();
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

    void findingPairs(int[] cardsNum) {
        Integer[] cardsInt = Arrays.stream(cardsNum).boxed().toArray(Integer[]::new);
        ArrayList<Integer> singlePair = new ArrayList<>();
        Arrays.sort(cardsInt, Collections.reverseOrder());

        for (int i = 1; i < cardsInt.length; i++) {
            //pair
            if (cardsInt[i].equals(cardsInt[i - 1])) {
                singlePair.addAll(Arrays.asList(cardsInt[i], cardsInt[i - 1]));
                if (i == cardsInt.length - 1) {
                    continue;
                } else {
                    if (cardsInt[i].equals(cardsInt[i + 1])) {
                        if (i == cardsInt.length - 2) {
                            if (!threeOfAKind && !fourOfAKind && !fullHouse) {
                                rankSetter(4);
                                highCardNumbers.addAll(Arrays.asList(cardsInt[i], cardsInt[i - 1], cardsInt[i + 1]));
                                threeOfAKind = true;
                            }
                        } else {
                            //four of a kind
                            if (cardsInt[i].equals(cardsInt[i + 2])) {
                                rankSetter(8);
                                if (isFlush && isStraight) {
                                    continue;
                                } else {
                                    highCardNumbers.addAll(Arrays.asList(cardsInt[i], cardsInt[i - 1], cardsInt[i + 1], cardsInt[i + 2]));
                                    fourOfAKind = true;
                                    break;
                                }
                            }
                        }
                        //full house
                        if (!fullHouse) {
                            for (int j = (i + 2); j < cardsInt.length - 1; j++) {
                                if (cardsInt[j].equals(cardsInt[j + 1])) {
                                    rankSetter(7);
                                    if (isStraight && isFlush) {} else {
                                        highCardNumbers.addAll(Arrays.asList(cardsInt[j], cardsInt[j + 1]));
                                        fullHouse = true;
                                        break;
                                    }

                                }
                            }
                            if (fullHouse) {
                                highCardNumbers.add(0, cardsInt[i - 1]);
                                highCardNumbers.add(1, cardsInt[i]);
                                highCardNumbers.add(2, cardsInt[i + 1]);
                            }
                        }
                        if (!fullHouse && !threeOfAKind) {
                            rankSetter(4);
                            highCardNumbers.addAll(Arrays.asList(cardsInt[i], cardsInt[i - 1], cardsInt[i + 1]));
                            threeOfAKind = true;
                        }
                    }
                }
                //full house
                if (!fullHouse) {
                    for (int j = (i + 1); j < cardsInt.length - 2; j++) {
                        if (cardsInt[j].equals(cardsInt[j + 1])) {
                            if (cardsInt[j].equals(cardsInt[j + 2])) {
                                rankSetter(7);
                                if (isFlush && isStraight) {} else {
                                    highCardNumbers.addAll(Arrays.asList(cardsInt[j], cardsInt[j + 1], cardsInt[j + 2]));
                                    fullHouse = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (fullHouse) {
                        highCardNumbers.add(0, cardsInt[i - 1]);
                        highCardNumbers.add(1, cardsInt[i]);
                    }
                }
                //two pair
                if (!twoPair && !fullHouse) {
                    for (int j = (i + 1); j < cardsInt.length - 1; j++) {
                        if (cardsInt[j].equals(cardsInt[j + 1])) {
                            rankSetter(3);
                            if (!isFlush && !isStraight) {
                                highCardNumbers.addAll(Arrays.asList(cardsInt[j], cardsInt[j + 1]));
                                twoPair = true;
                            }
                            break;
                        }
                    }
                    if (twoPair) {
                        highCardNumbers.add(0, cardsInt[i - 1]);
                        highCardNumbers.add(1, cardsInt[i]);
                    }
                }
            }
        }

        if ((!twoPair && !threeOfAKind && !fourOfAKind && !fullHouse & !isFlush && !isStraight) && singlePair.size() == 2) {
            highCardNumbers.addAll(singlePair);
            rankSetter(2);
        }
    }

    void findStraights(int[] cardsInt, String[] cardsSuit) {
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
        boolean nextNotStraight = true;
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
                nextNotStraight = cardsIntUnique[j] + 2 != cardsIntUnique[j + 2];
            } else if (j >= cardsIntUnique.length - 1) {
                nextNotStraight = true;
            }
            if (k >= 4 && nextNotStraight) {
                isStraight = true;
                storedCardsUniqueInt.add(cardsIntUnique[j+1]);
                int straightLength = storedCardsUniqueInt.size();
                if (straightLength > 5) {
                    for (int x = 0; x < straightLength - 5; x++) {
                        storedCardsUniqueInt.remove(x);
                    }
                }
                highCardNumbers.addAll(storedCardsUniqueInt);
                Object[] keysForMap = sortedCardAndSuit.keySet().toArray();
                int counterMap = sortedCardAndSuit.size();
                for (int r = 0 ; r < counterMap; r++) {
                    if (!storedCardsUniqueInt.contains(keysForMap[r])) {
                        sortedCardAndSuit.remove(keysForMap[r]);
                    }
                }
                if (isFlush) {
                    highCardNumbers.clear();
                    highCardNumbers.addAll(storedCardsUniqueInt);
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
    }

    void findFlush(int[] cardsInt, String[] cardsSuit) {
        Map<String, Integer> suitAndQty = new HashMap<>();
        Map<String, List<Integer>> suitAndCard = new HashMap<>();
        int s = cardsInt.length - 1;
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
            s--;
        }
        isFlush = false;
        for (String key : suitAndQty.keySet()) {
            if (suitAndQty.get(key) >= 5) {
                isFlush = true;
                flushSuit = key;
                rankSetter(6);
                for (int c = 0; c < suitAndQty.get(key); c++) {
                    highCardNumbers.add(suitAndCard.get(key).get(c));
                }
            }
            if (suitAndQty.get(key) == 2 && cardsInt.length == 2) {
                sameSuit = true;
            }
        }
    }

    void findTopCards(int[] cardsIntArray) {
        Arrays.sort(cardsIntArray);
        int counter;
        if (cardsIntArray.length <= 5) {
            counter = 0;
        } else {
            counter = cardsIntArray.length - 5;
        }
        for (var c = cardsIntArray.length - 1; c >= counter; c--) {
            highCardNumbersSeperate.add(cardsIntArray[c]);
        }
        rankSetter(1);
    }

    Object[] determineHand() {
        String[] cardsSuitArray = (String[])cardInfo[0];
        int[] cardsIntArray = (int[])cardInfo[1];
        findFlush(cardsIntArray, cardsSuitArray);
        findStraights(cardsIntArray, cardsSuitArray);
        findingPairs(cardsIntArray);
        findTopCards(cardsIntArray);
        if (handRank == 1) {
            highCardNumbers = highCardNumbersSeperate;
        }
        if (isFlush) {
            return new Object[]{handRank, highCardNumbers, highCardNumbersSeperate};
        }
        else {
            return new Object[]{handRank, highCardNumbers, highCardNumbersSeperate};
        }
    }

    private void rankSetter(int tempRank) {
        if (tempRank > handRank) {
            handRank = tempRank;
        }
    }

    private String getCardHand(int value) {
        return cardString[value - 1];
    }

    String getFlushSuit() {
        return flushSuit;
    }

    boolean isSameSuit() {
        return sameSuit;
    }
}