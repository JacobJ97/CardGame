import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Arrays;

public class Game {

    /** IDS that will have information of cards to hold */
    private static Map<String, String> playerIDS = new HashMap<>();
    private static Map<String, Boolean> playerState = new HashMap<>();

    private static String[] cardRanksString = {"High Card", "Pair", "Two Pair", "Three of a Kind", "Straight", "Flush",
    "Full House", "Four of a Kind", "Straight Flush", "Royal Flush"};

    private static String[] ranksInt = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

    /** Card move info */
    private static final int INITIAL_DRAW_TO_PLAYERS = 2;
    private static final int FLOP = 3;
    private static final int TURN = 1;
    private static final int RIVER = 1;
    private static final int ALL_CARDS_DRAWN = INITIAL_DRAW_TO_PLAYERS + FLOP + TURN + RIVER;
    private static final int[] turnInfo = {INITIAL_DRAW_TO_PLAYERS, FLOP, TURN, RIVER};
    private static String suitFlush;

    //TODO: Refactor code

    public static void main(String []args) {
        displayMenu();
        inputsAtStart();
        int[] settings = initSettings();
        playGame(settings);
        System.out.println("Done!");
    }

    private static void playGame(int[] settings) {
        Pot pot = new Pot(settings[0] / 100, settings[0] / 50);
        Player[] players = new Player[settings[1]];
        boolean playing = true;

        while (playing) {
            Deck cards = new Deck();
            for (int i = 0; i < players.length; i++) {
                players[i] = new Player(playerIDS.get(String.valueOf(i)), settings[0]);
                ArrayList<Card> dealtCards = cards.dealCards(INITIAL_DRAW_TO_PLAYERS);
                players[i].setPlayerCards(dealtCards);
                playerState.put(playerIDS.get(String.valueOf(i)), true);
            }
            //game loop
            int numberOfMoves = turnInfo.length;
            ArrayList<Player> currentPlayers = new ArrayList<>(Arrays.asList(players));
            CommunityCards tableCards = new CommunityCards();
            for (int turnNumber = 0; turnNumber < numberOfMoves; turnNumber++) {
                if (turnNumber >= 1) {
                    ArrayList<Card> cardsOnTable = cards.dealCards(turnInfo[turnNumber]);
                    tableCards.setTableCards(cardsOnTable);
                }
                messagesInGame(turnNumber);
                for (int playerTurn = 0; playerTurn < currentPlayers.size(); playerTurn++) {
                    inputsInGame(currentPlayers.get(playerTurn), playerTurn, pot);
                    int finalPlayerTurn = playerTurn;
                    currentPlayers.removeIf((Player player) -> !playerState.get(playerIDS.get(String.valueOf(String.valueOf(finalPlayerTurn)))));
                    if (currentPlayers.size() == 1) {
                        System.out.println("You have won a total of $" + pot.getPotTotal());
                        currentPlayers.get(0).setPlayerBalance(pot.getPotTotal());
                    }
                }
            }
            //calculating the winner
            Map<Player, Object[]> allPlayerInfo = new HashMap<>();
            for (int playerHand = 0; playerHand < currentPlayers.size(); playerHand++) {
                ArrayList<Card> allCards = new ArrayList<>();
                allCards.addAll(players[playerHand].getPlayerCards());
                allCards.addAll(tableCards.getTableCards());
                Logic logic = new Logic(allCards, players[playerHand]);
                Object[] handInformation = logic.determineHand();
                if (logic.getFlushSuit() != null) {
                    suitFlush = logic.getFlushSuit();
                }
                allPlayerInfo.put(players[playerHand], handInformation);
            }
            Head2Head head2head = new Head2Head(allPlayerInfo);
            Object[] winnersInfo = head2head.determineWinner();
            @SuppressWarnings("unchecked")
            ArrayList<Player> winners = (ArrayList<Player>) winnersInfo[0];
            int rank = (int)winnersInfo[1];
            System.out.println(rank);
            @SuppressWarnings("unchecked")
            ArrayList<Integer> fff = (ArrayList<Integer>) winnersInfo[3];
            if (winners.size() == 1) {
                winners.get(0).setPlayerBalance(pot.getPotTotal());
                System.out.print("Congrats " + winners.get(0) + ", you have won a total of $" + pot.getPotTotal() + " with a " + cardRanksString[rank - 1]);
                if (rank == 2 || rank == 4 || rank == 8) {
                    System.out.print(" of " + ranksInt[(fff.get(0) - 1)]);
                }
                else if (rank == 3 || rank == 7) {
                    System.out.print(" of " + ranksInt[(fff.get(0) - 1)] + "& " + ranksInt[(fff.get(1) - 1)]);
                }
                else if (rank == 5 || rank == 9 || rank == 10) {
                    System.out.print(" that is between " + ranksInt[(fff.get(0) - 1)] + "& " + ranksInt[(fff.get(1) - 1)]);
                }
                else {
                    System.out.print(" of " + suitFlush);
                }
            } else {
                System.out.print("Congrats to ");
                for (int j = 0; j < winners.size(); j++) {
                    winners.get(j).setPlayerBalance(pot.getPotTotal() / winners.size());
                    System.out.print(winners.get(j));
                    if (winners.size() - j != 0) {
                        System.out.print("& ");
                    }
                }
                System.out.print(", you have won a total of $" + pot.getPotTotal() / winners.size());
            }
            System.out.println("");
            System.out.println("Do you want to play again? Y or N");
            Scanner s = new Scanner(System.in);
            String playAgain = s.next();
            while (!playAgain.equals("Y") && !playAgain.equals("N")) {
                System.out.println("Invalid input...play again? Y or N");
                playAgain = s.next();
            }
            if (playAgain.equals("Y")) {
                System.out.println("Another game it is, have fun!");
            } else {
                System.out.println("You have ended the game with a total of $" + players[0].getPlayerBalance());
                playing = false;
            }
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

    private static void inputsInGame(Player player, int index, Pot pot) {
        System.out.println("The total amount in the pot is: $" + pot.getPotTotal());
        Scanner s = new Scanner(System.in);
        String move = s.next();
        while (move.equals("cash")) {
            int playerBalance = player.getPlayerBalance();
            System.out.println("$" + playerBalance);
            move = s.next();
        }
        while (!move.equals("call") && !move.equals("raise") && !move.equals("fold")) {
            System.out.println("That is not a proper input. You can either check your balance, call, raise or fold.");
            move = s.next();
        }
        switch (move) {
            case "call": {
                int call = pot.getCallTotal();
                pot.setPotTotal(call);
                player.setPlayerBalance(player.getPlayerBalance() - call);
                System.out.println("You have called a total of $" + call);
                break;
            }
            case "raise": {
                int call = pot.getCallTotal();
                System.out.println("How much do you want to raise it by? Current bet is at $" + call);
                int raiseAmount = s.nextInt();
                pot.setPotTotal(call + raiseAmount);
                player.setPlayerBalance(player.getPlayerBalance() - (call + raiseAmount));
                break;
            }
            case "fold":
                player.fold();
                playerState.put(playerIDS.get(String.valueOf(index)), false);
                break;
            default:
                System.out.println("ponk");
                break;
        }
    }

    private static int[] initSettings() {
        int playerBalance = 0;
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
            System.out.println("Number of players? (min 2, max 8)");
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
            }
            else {
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
                        ">call - make an equal bet\n" +
                        ">raise <value> - raise the bet amount\n" +
                        ">fold - end game\n"
                );
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
