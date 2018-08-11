import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    /** IDS that will have information of cards to hold */
    private static String[] playerIDS = {"Computer_ID", "Player_ID"};
    private static Player[] players = new Player[playerIDS.length];

    /** Card move info */
    private static final int INITIAL_DRAW_TO_PLAYERS = 2;
    private static final int FLOP = 3;
    private static final int TURN = 1;
    private static final int RIVER = 1;

    //TODO: Program betting and stuff
    //TODO: Program in the flop/turn/river
    //TODO: Better management of Player class (does bot get own class?)

    public static void main(String []args) {
        displayMenu();
        inputsAtStart();
        startGame();
        System.out.println("Done!");
    }

    private static void startGame() {
        Deck cards = new Deck();
        System.out.println(playerIDS.length);
        for (int i = 0; i < playerIDS.length; i++) {
            players[i] = new Player(playerIDS[i]);
            ArrayList<Card> dealtCards = cards.dealCards(INITIAL_DRAW_TO_PLAYERS);
            players[i].setPlayerCards(dealtCards);
        }

    }

    private static void inputsAtStart() {
        Scanner s = new Scanner(System.in);
        String response = s.next();
        while (!response.equals("start")) {
            response = s.next();
        }
    }

    private static void displayMenu() {
        System.out.println("Welcome to Texas Hold 'Em! This iteration of it is built in Java, meant to be played on a " +
                "command line, against the computer. Think you can handle it? Type \"start\" to begin!");
    }

}
