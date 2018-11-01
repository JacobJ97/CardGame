import java.util.*;
import java.util.stream.Collectors;

public class Head2Head {

    private Map<Player, Object[]> allPlayerInfo;
    private int topHand;
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

    Object[] determineWinner(Pot pot) {
        for (Player player : allPlayerInfo.keySet()) {
            Object[] handInfo = allPlayerInfo.get(player);
            @SuppressWarnings("unchecked")
            ArrayList<Integer> hand = (ArrayList<Integer>)handInfo[1];
            @SuppressWarnings("unchecked")
            ArrayList<Integer> top5 = (ArrayList<Integer>)handInfo[2];
            int rank = (int)handInfo[0];
            handRankSetter(player, rank, hand, top5);
        }
        determineStandings(pot);
        return new Object[] {playersWon, topHand};
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
                        secondaryRankComp.put(player, a);
                        mapSetter(player, handRank, handCards, top5cards, true);
                        break;
                    } else if (a < b) {
                        isTie = false;
                        secondaryRankComp.put(winningPlayer, b);
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
                            teritaryRankComp.put(player, a);
                            mapSetter(player, handRank, handCards, top5cards, true);
                            player.setPlayerHighestUniqueCardIndex(i);
                            player.setPlayerHighestUniqueCard(a);
                            break;
                        } else if (a < b) {
                            isTie = false;
                            teritaryRankComp.put(winningPlayer, b);
                            winningPlayer.setPlayerHighestUniqueCardIndex(i);
                            winningPlayer.setPlayerHighestUniqueCard(b);
                            break;
                        } else { continue; }
                    }
                    if (isTie) {
                        playersWon.add(player);
                        player.setPlayerHighlightCards(winningHandNums);
                    }
                }
           }
        }
    }

    private void determineStandings(Pot pot) {
        Map<Player, Integer> sortedHandRank = handRankComp.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        Map<Player, Integer> secondarySortedRank = secondaryRankComp.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        Map<Player, Integer> teritarySortedRank = teritaryRankComp.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        ArrayList<Player> players = new ArrayList<>(sortedHandRank.keySet());
        Map<Integer, List<Player>> mapRankings = new HashMap<>();
        for (int d = 0; d < players.size() - 1; d++) {
            List<Player> playerList = new ArrayList<>();
            if (sortedHandRank.get(players.get(d)).equals(sortedHandRank.get(players.get(d+1)))) {
                if (secondarySortedRank.size() >= 1) {
                    ArrayList<Player> winner = new ArrayList<>(secondarySortedRank.keySet());
                    mapRankings.put(d, playerList);
                    for (Player aWinner : winner) {
                        mapRankings.get(d).add(aWinner);
                    }
                } else if (teritarySortedRank.size() >= 1) {
                    ArrayList<Player> winner = new ArrayList<>(teritarySortedRank.keySet());
                    mapRankings.put(d, playerList);
                    for (Player aWinner : winner) {
                        mapRankings.get(d).add(aWinner);
                    }
                }
                else {
                    mapRankings.put(d, playerList);
                    for (Player player : playersWon) {
                        mapRankings.get(d).add(player);
                    }
                }
            }
            else {
                if (sortedHandRank.get(players.get(d)) > sortedHandRank.get(players.get(d+1))) {
                    mapRankings.put(d, playerList);
                    mapRankings.get(d).add(players.get(d));
                }
                else {
                    mapRankings.put(d, playerList);
                    mapRankings.get(d).add(players.get(d+1));
                }
            }
        }
        for (Integer standing : mapRankings.keySet()) {
            List<Player> player = mapRankings.get(standing);
            for (int d = 0; d < player.size(); d++) {
                player.get(d).setPlayerStanding(standing);
            }
        }

        int totalPot = pot.getPotTotal();
        if (mapRankings.get(0).size() == 1) {
            Player player = mapRankings.get(0).get(0);
            if (player.getPlayerBalance() > 0) {
                player.addWinnings(totalPot);
                player.setTotalWon(totalPot);
                pot.emptyPot(totalPot);
            }
            else {
                int callTotes = 0;
                for (Integer standing : mapRankings.keySet()) {
                    List<Player> individualPlayer = mapRankings.get(standing);
                    for (Player playerLoop : individualPlayer) {
                        if (playerLoop.getTotalBetted() > player.getTotalBetted()) {
                            int variance = playerLoop.getTotalBetted() - player.getTotalBetted();
                            callTotes += player.getTotalBetted();
                            playerLoop.setTotalBetted(-playerLoop.getTotalBetted() + variance);
                        } else {
                            callTotes += playerLoop.getTotalBetted();
                            playerLoop.setTotalBetted(-playerLoop.getTotalBetted());
                            mapRankings.remove(standing, playerLoop);
                        }
                    }
                }
                player.addWinnings(callTotes);
                player.setTotalWon(callTotes);
                pot.emptyPot(callTotes);

                List<Player> s = mapRankings.get(1);
                int numOfPlayers = s.size();
                int potErase = pot.getPotTotal();
                for (int v = 0; v < numOfPlayers; v++) {
                    callTotes = s.get(v).getTotalBetted();
                    int b = pot.getPotTotal() - (pot.getPotTotal() - callTotes);
                    int c = ((pot.getPotTotal() - callTotes) - (pot.getPotTotal() / numOfPlayers));
                    int totalShared = Math.round(b + c);
                    s.get(v).setTotalWon(totalShared);
                    s.get(v).addWinnings(totalShared);
                }
                pot.emptyPot(potErase);
            }
        }
        else {
            List<Player> s = mapRankings.get(0);
            int numOfPlayers = s.size();
            int potErase = pot.getPotTotal();
            for (int v = 0; v < numOfPlayers; v++) {
                int callTotes = s.get(v).getTotalBetted();
                int b = pot.getPotTotal() - (pot.getPotTotal() - callTotes);
                int c = ((pot.getPotTotal() - callTotes) - (pot.getPotTotal() / numOfPlayers));
                int totalShared = Math.round(b + c);
                s.get(v).setTotalWon(totalShared);
                s.get(v).addWinnings(totalShared);
            }
            pot.emptyPot(potErase);
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
        else if (handRank == 1) {
            winningHandNums.add(handCards.get(player.getHighestUniqueCardIndex()));
        }
        player.setPlayerHighlightCards(winningHandNums);
    }
}
