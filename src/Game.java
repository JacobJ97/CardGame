import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Arrays;

public class Game {

    /** IDS that will have information of cards to hold */
    private static Map<String, String> playerIDS = new HashMap<>();
    private static Map<String, Boolean> playerState = new HashMap<>();

    /** Card move info */
    private static final int INITIAL_DRAW_TO_PLAYERS = 2;
    private static final int FLOP = 3;
    private static final int TURN = 1;
    private static final int RIVER = 1;
    private static final int[] turnInfo = {INITIAL_DRAW_TO_PLAYERS, FLOP, TURN, RIVER};

    //TODO: Program betting and stuff
    //TODO: Program in the flop/turn/river
    //TODO: Better management of Player class (does bot get own class?)

    public static void main(String []args) {
        displayMenu();
        inputsAtStart();
        int[] settings = initSettings();
        startGame(settings);
        System.out.println("Done!");
    }

    private static void startGame(int[] settings) {
        Deck cards = new Deck();
        Pot pot = new Pot(settings[0] / 100, settings[0] / 50);
        Player[] players = new Player[settings[1]];

        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(playerIDS.get(String.valueOf(i)), settings[0]);
            ArrayList<Card> dealtCards = cards.dealCards(INITIAL_DRAW_TO_PLAYERS);
            players[i].setPlayerCards(dealtCards);
            playerState.put(playerIDS.get(String.valueOf(i)), true);
        }
        //game loop
        ArrayList<Player> currentPlayers = new ArrayList<>(Arrays.asList(players));
        for (int turnNumber = 0; turnNumber < turnInfo.length; turnNumber++) {
            messagesInGame(turnNumber);
            for (int playerTurn = 0; playerTurn < currentPlayers.size(); playerTurn++) {
                inputsInGame(currentPlayers.get(playerTurn), playerTurn, pot);
                int finalPlayerTurn = playerTurn;
                currentPlayers.removeIf((Player player) -> !playerState.get(playerIDS.get(String.valueOf(String.valueOf(finalPlayerTurn)))));
                if (currentPlayers.size() == 1) {
                    System.out.println("You have won!");
                }
            }
        }
    }

    private static void messagesInGame(int turnNumber) {
        if (turnNumber == 0) {
            System.out.println("We are current in pre-flop! What would you like to do?");
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
        System.out.println("The total amount in the pot is: $" + pot.totalPot);
        Scanner s = new Scanner(System.in);
        String move = s.next();
        while (move.equals("cash")) {
            int playerBalance = player.getPlayerBalance();
            System.out.println("$" + playerBalance);
            s.next();
        }
        if (move.equals("call")) {
            int call = pot.getCallTotal();
            pot.setPotTotal(call);
            player.setPlayerBalance(player.getPlayerBalance() - call);
            System.out.println("You have called a total of $" + call);
        }
        else if (move.equals("raise")) {
            int call = pot.getCallTotal();
            System.out.println("How much do you want to raise it by? Current bet is at $" + call);
            int raiseAmount = s.nextInt();
            pot.setPotTotal(call + raiseAmount);
            player.setPlayerBalance(player.getPlayerBalance() - (call + raiseAmount));
        }
        else if (move.equals("fold")) {
            player.fold();
            playerState.put(playerIDS.get(String.valueOf(index)), false);
        }
        else {
            System.out.println("HI JAKE!!! CAN YOU PLEASE PLEASE PLEASE FOCUS ON HUGGING ME AND NOT YOUR DAMN PROGRAMMING!!!!????");
        }
    }

    private static int[] initSettings() {
        Scanner s = new Scanner(System.in);
        System.out.println("Each player starts with?");
        int playerBalance = s.nextInt();
        System.out.println("Number of players? (min & max 2)");
        int numOfPlayers = s.nextInt();
        for (int i = 0; i < numOfPlayers; i++) {
            if (i == 0) {
                playerIDS.put(String.valueOf(i), "Player");
                playerState.put(playerIDS.get(String.valueOf(i)), true);
            }
            playerIDS.put(String.valueOf(i), "Computer_" + i);
            playerState.put(playerIDS.get(String.valueOf(i)), true);
        }
        System.out.println("Number of decks in hand?");
        int numOfDecks = s.nextInt();
        return new int[]{playerBalance, numOfPlayers, numOfDecks};
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
