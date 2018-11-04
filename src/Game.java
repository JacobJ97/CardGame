import java.lang.reflect.Array;
import java.util.*;

public class Game {

    /** IDS that will have information of cards to hold */
    private static Map<String, String> playerIDS = new HashMap<>();
    private static Map<String, Boolean> playerState = new HashMap<>();
    private static Map<Player, String> playerBetSet = new HashMap<>();

    private static String[] cardRanksString = {"High Card", "Pair", "Two Pair", "Three of a Kind", "Straight", "Flush",
    "Full House", "Four of a Kind", "Straight Flush", "Royal Flush"};
    private static String[] ranksInt = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

    /** Card move info */
    private static final int INITIAL_DRAW_TO_PLAYERS = 2;
    private static final int FLOP = 3;
    private static final int TURN = 1;
    private static final int RIVER = 1;
    private static final int[] turnInfo = {INITIAL_DRAW_TO_PLAYERS, FLOP, TURN, RIVER};
    private static String suitFlush;
    private static CommunityCards tableCards;
    private static Logic logic;

    //TODO: Refactor code

    public static void main(String []args) {
        displayMenu();
        inputsAtStart();
        int[] settings = initSettings();
        Random r = new Random();
        int randPosition = r.nextInt(settings[1]);
        playGame(settings, randPosition);
        System.out.println("Done!");
    }

    private static void playGame(int[] settings, int randPosition) {
        Player[] players = new Player[settings[1]];
        boolean playing = true;

        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(playerIDS.get(String.valueOf(i)), settings[0]);
        }

        while (playing) {
            int smallBlindIndex;
            int bigBlindIndex;
            Pot pot = new Pot(settings[0] / 100, settings[0] / 50);
            int startingCallBet = pot.getBigBlind();
            Deck cards = new Deck();
            randPosition++;
            if (randPosition >= players.length) {
                randPosition = 0;
                smallBlindIndex = 0;
            } else {
                smallBlindIndex = randPosition;
            }
            //small blind
            players[smallBlindIndex].setPlayerBalance(players[smallBlindIndex].getPlayerBalance() - pot.getSmallBlind());
            players[smallBlindIndex].setTotalBetted(pot.getSmallBlind());
            players[smallBlindIndex].setRoundBet(pot.getSmallBlind());
            System.out.println(players[smallBlindIndex].toString() + " has gotten the small blind, putting a total of $" + pot.getSmallBlind() + " in.");
            if ((randPosition + 1) >= players.length) {
                bigBlindIndex = 0;
            } else {
                bigBlindIndex = randPosition + 1;
            }
            System.out.println(bigBlindIndex);
            //large blind
            players[bigBlindIndex].setPlayerBalance(players[bigBlindIndex].getPlayerBalance() - pot.getBigBlind());
            players[bigBlindIndex].setTotalBetted(pot.getBigBlind());
            players[bigBlindIndex].setRoundBet(pot.getBigBlind());
            System.out.println(players[bigBlindIndex].toString() + " has gotten the big blind, putting a total of $" + pot.getBigBlind() + " in.");
            int x = bigBlindIndex + 1;
            for (int i = 0; i < players.length; i++) {
                if (x == players.length) {
                    x = 0;
                }
                ArrayList<Card> dealtCards = cards.dealCards(INITIAL_DRAW_TO_PLAYERS);
                players[i].setPlayerCards(dealtCards);
                playerState.put(playerIDS.get(String.valueOf(i)), true);
            }
            //game loop
            int numberOfMoves = turnInfo.length;
            ArrayList<Player> currentPlayers = new ArrayList<>(Arrays.asList(players));
            tableCards = new CommunityCards();
            for (int turnNumber = 0; turnNumber < numberOfMoves; turnNumber++) {
                if (currentPlayers.size() == 1) {
                    break;
                }
                if (turnNumber >= 1) {
                    ArrayList<Card> cardsOnTable = cards.dealCards(turnInfo[turnNumber]);
                    tableCards.setTableCards(cardsOnTable);
                    pot.setCallTotalOverride(startingCallBet);
                }
                messagesInGame(turnNumber);
                boolean allCall = false;
                playerBetSet.clear();
                for (int f = 0; f < currentPlayers.size(); f++) {
                    currentPlayers.get(f).clearRoundBet();
                }
                while (!allCall) {
                    for (int playerTurn = 0; playerTurn < currentPlayers.size() && !allCall; playerTurn++) {
                        if (players[playerTurn].getPlayerBalance() <= 0) {
                            if (playerTurn == currentPlayers.size() - 1) {
                                allCall = true;
                            }
                            continue;
                        } else {
                            inputsInGame(currentPlayers.get(playerTurn), playerTurn, pot, turnNumber);
                        }
                        if (playerBetSet.get(currentPlayers.get(playerTurn)).equals("fold")) {
                            currentPlayers.remove(currentPlayers.get(playerTurn));
                        }
                        if (currentPlayers.size() == 1) {
                            System.out.println(currentPlayers.get(0) + " has won a total of $" + pot.getPotTotal());
                            currentPlayers.get(0).addWinnings(pot.getPotTotal());
                            allCall = true;
                            break;
                        }
                        for (int d = 0; d < currentPlayers.size() - 1; d++) {
                            if (currentPlayers.get(d).getRoundBet() != currentPlayers.get(d + 1).getRoundBet()) {
                                if (currentPlayers.get(d).getPlayerBalance() == 0) {
                                    continue;
                                } else if (currentPlayers.get(d + 1).getPlayerBalance() == 0) {
                                    continue;
                                } else {
                                    allCall = false;
                                    break;
                                }
                            } else {
                                allCall = true;
                            }
                        }
                    }
                    if (currentPlayers.size() == 1) {
                        break;
                    }
                }
            }
            if (currentPlayers.size() > 1) {
                //calculating the winner
                Map<Player, Object[]> allPlayerInfo = new HashMap<>();
                for (int playerHand = 0; playerHand < currentPlayers.size(); playerHand++) {
                    Object[] handInformation = setUpLogic(players[playerHand]);
                    if (logic.getFlushSuit() != null) {
                        suitFlush = logic.getFlushSuit();
                    }
                    allPlayerInfo.put(players[playerHand], handInformation);
                }
                Head2Head head2head = new Head2Head(allPlayerInfo);
                Object[] winnersInfo = head2head.determineWinner(pot);
                @SuppressWarnings("unchecked")
                ArrayList<Player> winners = (ArrayList<Player>) winnersInfo[0];
                if (winners.size() == 1) {
                    Player winner = winners.get(0);
                    System.out.print("Congrats " + winner + ", you have won a total of $" + winner.getTotalWon() + " with a " + cardRanksString[winner.getPlayerHandRank() - 1]);
                    getWinningMessage(winner.getPlayerHandRank(), winner.getPlayerHighlightCards());
                } else {
                    System.out.print("Congrats to: ");
                    for (int j = 0; j < winners.size(); j++) {
                        Player winner = winners.get(j);
                        System.out.print(winner + ", for winning a total of $" + winner.getTotalWon() + " with a " + cardRanksString[winner.getPlayerHandRank() - 1]);
                        getWinningMessage(winner.getPlayerHandRank(), winner.getPlayerHighlightCards());
                        if (winners.size() - j != 0) {
                            System.out.print(" & ");
                        }
                    }
                }
            }
            for (Player player : players ) {
                if (player.toString().equals("Player")) {
                    if (player.getPlayerBalance() <= 0) {
                        System.out.println("You have ran out of money and as a result have lost.");
                        playing = false;
                        break;
                    }
                }
                else {
                    if (player.getPlayerBalance() <= 0) {
                        System.out.println(player.toString() + "has run out of money, and the game is over.");
                        playing = false;
                        break;
                    }
                }
            }
            if (playing) {
                System.out.println("");
                System.out.println("Do you want to play again? Y or N");
                Scanner s = new Scanner(System.in);
                String playAgain = s.next().toLowerCase();
                while (!playAgain.equals("y") && !playAgain.equals("n")) {
                    System.out.println("Invalid input...play again? Y or N");
                    playAgain = s.next().toLowerCase();
                }
                if (playAgain.equals("y")) {
                    for (Player player : players) {
                        player.setTotalWon(0);
                        player.setPlayerHighlightCards(null);
                        player.setPlayerHighestUniqueCard(0);
                        player.setTotalBetted(-player.getTotalBetted());
                        player.setPlayerStanding(0);
                        player.removePlayerCards();
                        player.setPlayerRank(0);
                    }
                    System.out.println("Another game it is, have fun!");
                } else {
                    System.out.println("You have ended the game with a total of $" + players[0].getPlayerBalance());
                    playing = false;
                }
            }
            else {
                ArrayList<Player> topPlayer = null;
                int topCash = 0;
                for (Player player : players) {
                    int cash = player.getPlayerBalance();
                    if (cash > topCash) {
                        topPlayer.clear();
                        topPlayer.add(player);
                        topCash = cash;
                    }
                    else if (cash == topCash) {
                        topPlayer.add(player);
                    }
                }
                System.out.print("Congrats to: ");

                for (int d = 0; d < topPlayer.size(); d++) {
                    System.out.print(topPlayer.get(d));
                    if (d < topPlayer.size() - 1) {
                        System.out.print(" & ");
                    }
                }
                System.out.println(", you have won the game with a total of $" + topCash);
        }
        }
    }

    private static void getWinningMessage(int rank, ArrayList<Integer> winningCards) {
        if (rank == 1 || rank == 2 || rank == 4 || rank == 8) {
            System.out.println(" of " + ranksInt[(winningCards.get(0) - 1)]);
        }
        else if (rank == 3 || rank == 7) {
            System.out.println(" of " + ranksInt[(winningCards.get(0) - 1)] + " & " + ranksInt[(winningCards.get(1) - 1)]);
        }
        else if (rank == 5 || rank == 9 || rank == 10) {
            System.out.println(" that is between " + ranksInt[(winningCards.get(0) - 1)] + " & " + ranksInt[(winningCards.get(1) - 1)]);
        }
        else {
            System.out.println(" of " + suitFlush);
        }
    }

    private static void messagesInGame(int turnNumber) {
        if (turnNumber == 0) {
            System.out.println("We are currently in pre-flop! What would you like to do?");
        }
        if (turnNumber == 1) {
            System.out.println("We have just witnessed the flop! Any idea on what happens now?");
        }
        if (turnNumber == 2) {
            System.out.println("Oh no! The turn has just happened! Next action?");
        }
        if (turnNumber == 3) {
            System.out.println("The river is flowing! Last chance to do something, make it count!");
        }
    }

    private static void inputsInGame(Player player, int index, Pot pot, int turnNumber) {
        String move;
        Scanner s = new Scanner(System.in);
        int raiseAmount = 0;
        if (player.toString().equals("Player")) {
            System.out.println("The total amount in the pot is: $" + pot.getPotTotal());
            move = s.next().toLowerCase();
            while (move.equals("cash")) {
                int playerBalance = player.getPlayerBalance();
                System.out.println("$" + playerBalance);
                move = s.next().toLowerCase();
            }
            while (move.equals("hand")) {
                Object[] handInformation = setUpLogic(player);
                int rankFormatted = (int)handInformation[0] - 1;
                int rank = (int)handInformation[0];
                String firstIndex = ranksInt[logic.getHighCardNumbers().get(0) - 1];
                System.out.print("Your best hand is: " + cardRanksString[rankFormatted] + " ");
                if (rank == 1) {
                    System.out.println("of " + ranksInt[logic.getHighestCard() - 1]);
                }
                else if (rank == 2 || rank == 4 || rank == 8) {
                    System.out.println("of " + firstIndex);
                }
                else if (rank == 3 || rank == 7) {
                    System.out.println("of " + firstIndex + " & " + ranksInt[logic.getHighCardNumbers().get(2) - 1]);
                }
                else if (rank == 5 || rank == 9 || rank == 10) {
                    System.out.println("of " + firstIndex + " & " + ranksInt[logic.getHighCardNumbers().get(1) - 1]);
                }
                else if (rank == 6) {
                    System.out.println("of " + logic.getFlushSuit());
                }
                move = s.next().toLowerCase();
            }
            while (!move.equals("call") && !move.equals("raise") && !move.equals("fold")) {
                System.out.println("That is not a proper input. You can either check your balance, call, raise or fold.");
                move = s.next().toLowerCase();
            }
        }
        else {
            Object[] handInformation = setUpLogic(player);
            ComputerBrain ai = new ComputerBrain(handInformation, logic);
            ai.determineMove(turnNumber, playerBetSet);
            move = ai.getMove();
            if (move.equals("raise")) {
                raiseAmount = ai.getRaiseValue();
            }
        }
        playerBetSet.put(player, move);
        switch (move) {
            case "call": {
                int call = pot.getCallTotal();
                if (call >= player.getPlayerBalance()) {
                    call = player.getPlayerBalance();
                    pot.setPotTotal(call);
                    player.setRoundBet(call);
                    player.setPlayerBalance(0);
                    player.setTotalBetted(call);
                    System.out.println(player + " has called a total of $" + call);
                } else {
                    if (player.getRoundBet() > 0) {
                        call = pot.getCallTotal() - player.getRoundBet();
                        pot.setPotTotal(call);
                        player.setRoundBet(call);
                        player.setPlayerBalance(player.getPlayerBalance() - call);
                        player.setTotalBetted(call);
                        System.out.println(player + " has called a total of $" + call);
                    }
                    else {
                        pot.setPotTotal(call);
                        player.setRoundBet(call);
                        player.setPlayerBalance(player.getPlayerBalance() - call);
                        player.setTotalBetted(call);
                        System.out.println(player + " has called a total of $" + call);
                    }
                }
                break;
            }
            case "raise": {
                int call = pot.getCallTotal();
                if (player.toString().equals("Player")) {
                    System.out.println("How much do you want to raise it by? Current bet is at $" + call);
                    raiseAmount = s.nextInt();
                }
                if (call + raiseAmount > player.getPlayerBalance()) {
                    raiseAmount = player.getPlayerBalance();
                    pot.setPotTotal(raiseAmount, player.getRoundBet());
                    player.setRoundBet(raiseAmount);
                    player.setTotalBetted(raiseAmount);
                    player.setPlayerBalance(-player.getPlayerBalance());
                } else {
                    pot.setPotTotal(call + raiseAmount);
                    player.setRoundBet(call + raiseAmount);
                    player.setTotalBetted(call + raiseAmount);
                    player.setPlayerBalance(player.getPlayerBalance() - (call + raiseAmount));
                }
                System.out.println(player + " has raised it by $" + raiseAmount + " to bring the bet total to $" + (call + raiseAmount));
                break;
            }
            case "fold":
                System.out.println(player + " has folded.");
                player.fold();
                playerState.put(playerIDS.get(String.valueOf(index)), false);
                break;
        }
    }

    private static Object[] setUpLogic(Player player) {
        ArrayList<Card> cardsAllLogic = new ArrayList<>();
        cardsAllLogic.addAll(player.getPlayerCards());
        cardsAllLogic.addAll(tableCards.getTableCards());
        logic = new Logic(cardsAllLogic, player);
        return logic.determineHand();
    }

    private static int[] initSettings() {
        int playerBalance;
        Scanner s = new Scanner(System.in);
        do {
            System.out.println("Each player starts with?");
            while (!s.hasNextInt()) {
                System.out.println("Invalid input!");
                s.next();
            }
            playerBalance = s.nextInt();
        } while (playerBalance < 0);
        int numOfPlayers;
        do {
            System.out.println("Number of players? (min 2, max 8) [2 is recommended]");
            while (!s.hasNextInt()) {
                System.out.println("Invalid input!");
                s.next();
            }
            numOfPlayers = s.nextInt();
        } while (numOfPlayers < 2 || numOfPlayers > 8);
        for (int i = 0; i < numOfPlayers; i++) {
            if (i == 0) {
                playerIDS.put(String.valueOf(i), "Player");
                playerState.put(playerIDS.get(String.valueOf(i)), true);
            } else {
                playerIDS.put(String.valueOf(i), "Computer_" + i);
                playerState.put(playerIDS.get(String.valueOf(i)), true);
            }
        }
        return new int[]{playerBalance, numOfPlayers};
    }

    private static void inputsAtStart() {
        Scanner s = new Scanner(System.in);
        String response = s.next();
        while (!response.equals("start")) {
            if (response.equals("help")) {
                System.out.println(
                        ">cash - look at balance\n" +
                        ">hand - see your best hand\n" +
                        ">call - make an equal bet\n" +
                        ">raise <value> - raise the bet amount\n" +
                        ">fold - end game\n"
                );
            } else {
                System.out.println("Invalid input. Please enter \"start\" or \"help\" to continue.");
            }
            response = s.next();
        }
    }

    private static void displayMenu() {
        System.out.println("Welcome to Texas Hold 'Em! This iteration of it is built in Java, meant to be played on a " +
                "command line, against the computer. Think you can handle it? Type \"start\" to begin! Or, press \"help\" " +
                "for instructions on playing!");
    }
}
