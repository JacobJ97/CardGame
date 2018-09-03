import org.junit.jupiter.api.*;


import java.util.ArrayList;
import java.util.Map;

@DisplayName("Example")
class LogicTest {

    private Logic logic;
    private Player player;
    private String[] cardsSuitArray;
    private int[] cardsIntArray;
    private Map<Player, Integer[]> results;

    @BeforeAll
    static void beforeAll() {

    }

    @BeforeEach
    void beforeEach() {
        Deck cards = new Deck();
        this.player = new Player("bob", 500);
        ArrayList<Card> dealtCards = cards.dealCards(7);
        //System.out.println(dealtCards);
        this.logic = new Logic(dealtCards, player);
        Object[] cardInfo = logic.determineHandRanking();
        cardsSuitArray = (String[]) cardInfo[0];
        cardsIntArray = (int[]) cardInfo[1];
    }

    @AfterEach
    void afterEach() {

    }

    @AfterAll
    static void afterAll() {

    }

    /*@Test
    @DisplayName("Pairs Results")
    @RepeatedTest(100)
    void pairs() {
         results = logic.findingPairs(cardsIntArray);
         int result = results.get(player);
         if (result == 2 || result == 3 || result == 4 || result == 7 || result == 8) {
             System.out.println("Pair GOT: " + result + "\n");
         }
    }*/

    /*@Test
    @DisplayName("Straights Results")
    @RepeatedTest(2000)
    void straights() {
        results = logic.findStraights(cardsIntArray, cardsSuitArray);
        int result = results.get(player)[0];
        if (result == 5 || result == 9 || result == 10) {
            System.out.println("Straight GOT: " + result + "\n");
        }
    }*/

    @Test
    @DisplayName("Flush Results")
    @RepeatedTest(100)
    void flushes() {
        results = logic.findFlush(cardsIntArray, cardsSuitArray);
        Integer[] result = results.get(player);
        if (result[0] == 6) {
            System.out.println("Flush GOT: " + result[0] + "\n");
            System.out.println("Hand Rank: " + result[1] + "\n");
        }
    }
}