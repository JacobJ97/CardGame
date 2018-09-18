import java.util.*;
import java.util.stream.Collectors;

public class Head2Head {

    private Map<Player, Object[]> allPlayerInfo;
    private int topHand;
    private int winningHighCard;
    private Player winningPlayer;
    private Map<Player, Integer> topHandMap = new HashMap<>();
    private ArrayList<Player> playersWon = new ArrayList<>();
    private ArrayList<Integer> winningHandNums = new ArrayList<>();
    private Map<Player, ArrayList<Integer>> topHandMapCards = new HashMap<>();
    private Map<Player, ArrayList<Integer>> top5CardsMap = new HashMap<>();
    private Map<Player, Integer> handRankComp = new HashMap<>();
    private Map<Player, Integer> secondaryRankComp = new HashMap<>();
    private Map<Player, Integer> teritaryRankComp = new HashMap<>();

    Head2Head(Map<Player, Object[]> allPlayerInfo) {
        this.allPlayerInfo = allPlayerInfo;
    }

    Object[] determineWinner() {
        for (Player player : allPlayerInfo.keySet()) {
            Object[] handInfo = allPlayerInfo.get(player);
            @SuppressWarnings("unchecked")
            ArrayList<Integer> hand = (ArrayList<Integer>)handInfo[1];
            @SuppressWarnings("unchecked")
            ArrayList<Integer> top5 = (ArrayList<Integer>)handInfo[2];
            int rank = (int)handInfo[0];
            handRankSetter(player, rank, hand, top5);
        }
        determineStandings();
        return new Object[] {playersWon, topHand, winningHighCard, winningHandNums};
    }

    private void handRankSetter(Player player, int handRank, ArrayList<Integer> handCards, ArrayList<Integer> top5cards) {
        handRankComp.put(player, handRank);
        player.setPlayerRank(handRank);
        if (topHandMap.size() == 0) {
            mapSetter(player, handRank, handCards, top5cards, false);
        } else {
           if (handRank > topHand) {
               mapSetter(player, handRank, handCards, top5cards, true);
           }
           else if (handRank == topHand) {
                boolean isTie = true;
                if (handRank == 3) {

                }
                if (handRank == 6) {
                    Collections.sort(handCards);
                }
                if (handRank == 5 || handRank == 6 || handRank == 9 || handRank == 10) {
                    Collections.reverse(handCards);
                }
                for (int i = 0; i < handCards.size(); i++) {
                    int a = handCards.get(i);
                    int b = topHandMapCards.get(winningPlayer).get(i);
                    if (a > b) {
                        isTie = false;
                        if (secondaryRankComp.get(player) < a) {
                            secondaryRankComp.put(player, a);
                        }
                        if (secondaryRankComp.get(winningPlayer) < b) {
                            secondaryRankComp.put(winningPlayer, b);
                        }
                        mapSetter(player, handRank, handCards, top5cards, true);
                        break;
                    } else if (a < b) {
                        if (secondaryRankComp.get(player) < a) {
                            secondaryRankComp.put(player, a);
                        }
                        if (secondaryRankComp.get(winningPlayer) < b) {
                            secondaryRankComp.put(winningPlayer, b);
                        }
                        break;
                    }
                    else {continue;}
                }
                if (isTie) {
                    for (int i = 0; i < handCards.size(); i++) {
                        int a = top5cards.get(i);
                        int b = top5CardsMap.get(winningPlayer).get(i);
                        if (a > b) {
                            isTie = false;
                            if (teritaryRankComp.get(player) < a) {
                                teritaryRankComp.put(player, a);
                            }
                            if (teritaryRankComp.get(winningPlayer) < b) {
                                teritaryRankComp.put(winningPlayer, b);
                            }
                            mapSetter(player, handRank, handCards, top5cards, true);
                            winningHighCard = a;
                            break;
                        } else if (b > a) {
                            if (teritaryRankComp.get(player) < a) {
                                teritaryRankComp.put(player, a);
                            }
                            if (teritaryRankComp.get(winningPlayer) < b) {
                                teritaryRankComp.put(winningPlayer, b);
                            }
                            break;
                        }
                        else {continue;}

                    }
                    if (isTie) {
                        playersWon.add(player);
                    }
                }
           }
        }
    }

    private void determineStandings() {
        Map<Player, Integer> sortedHandRank = handRankComp.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        Map<Player, Integer> secondarySortedRank = secondaryRankComp.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        Map<Player, Integer> teritarySortedRank = teritaryRankComp.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        ArrayList<Player> players = new ArrayList<>(sortedHandRank.keySet());
        Map<Integer, List<Player>> mapRankings = new HashMap<>();
        boolean skip = false;
        for (int d = 0; d < players.size() - 1; d++) {
            if (skip) {
                skip = false;
                continue;
            }
            List<Player> playerList = new ArrayList<>();
            if (sortedHandRank.get(players.get(d)).equals(sortedHandRank.get(players.get(d+1)))) {
                Player player1 = players.get(d);
                Player player2 = players.get(d+1);
                if (secondarySortedRank.get(player1).equals(secondarySortedRank.get(player2))) {
                    if (teritarySortedRank.get(player1).equals(teritarySortedRank.get(player2))) {
                        mapRankings.put(d, playerList);
                        mapRankings.get(d).add(player1);
                        mapRankings.get(d).add(player2);
                    }
                    else if (teritarySortedRank.get(player1) > (teritarySortedRank.get(player2))) {
                        mapRankings.put(d, playerList);
                        mapRankings.get(d).add(player1);
                    }
                    else {
                        mapRankings.get(d).add(player2);
                        mapRankings.get(d+1).add(player1);
                        skip = true;
                    }
                }
                else if (secondarySortedRank.get(player1) > (secondarySortedRank.get(player2))) {
                    mapRankings.put(d, playerList);
                    mapRankings.get(d).add(player1);
                }
                else {
                    mapRankings.put(d, playerList);
                    mapRankings.get(d).add(player2);
                    mapRankings.get(d+1).add(player1);
                    skip = true;
                }
            }
            else {
                mapRankings.put(d, playerList);
                mapRankings.get(d).add(players.get(d));
            }
        }
    }

    private void mapSetter(Player player, int handRank, ArrayList<Integer> handCards, ArrayList<Integer> top5cards, boolean eraseOld) {
        topHand = handRank;
        winningPlayer = player;
        if (eraseOld) {
            topHandMap.clear();
            topHandMapCards.clear();
            top5CardsMap.clear();
            playersWon.clear();
            winningHandNums.clear();
        }
        topHandMap.put(player, handRank);
        topHandMapCards.put(player, handCards);
        top5CardsMap.put(player, top5cards);
        playersWon.add(player);
        int[] distinctIntArray;
        if (handRank == 2 || handRank == 3 || handRank == 4 || handRank == 7 || handRank == 8) {
            int[] convertToIntArray = handCards.stream().mapToInt(i -> i).toArray();
            distinctIntArray = Arrays.stream(convertToIntArray).distinct().toArray();
            for (int s = 0; s < distinctIntArray.length; s++) {
                winningHandNums.add(distinctIntArray[s]);
            }
        }
        else if (handRank == 5 || handRank == 9 || handRank == 10) {
            winningHandNums.add(handCards.get(0));
            winningHandNums.add(handCards.get(handCards.size() - 1));
        }
    }
}
