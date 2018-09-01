import org.junit.jupiter.api.*;


import java.util.ArrayList;
import java.util.Map;

@DisplayName("Example")
class LogicTest {

    private Logic logic;
    private Object[] cardInfo;
    private ArrayList<Card> dealtCards;
    private Player player;
    private String[] cardsSuitArray;
    private int[] cardsIntArray;

    @BeforeAll
    static void beforeAll() {

    }

    @BeforeEach
    void beforeEach() {
        Deck cards = new Deck();
        this.player = new Player("bob", 500);
        this.dealtCards = cards.dealCards(7);
        //System.out.println(dealtCards);
        this.logic = new Logic(dealtCards, player);
        cardInfo = logic.determineHandRanking();
        cardsSuitArray = (String[])cardInfo[0];
        cardsIntArray = (int[])cardInfo[1];
    }

    @AfterEach
    void afterEach() {

    }

    @AfterAll
    static void afterAll() {

    }

    @Test
    @DisplayName("Pairs Results")
    @RepeatedTest(100)
    void pairs() {
         Map<Player, Integer> results = logic.findingPairs(cardsIntArray);
         int result = results.get(player);
         if (result == 1 || result == 2 || result == 3 || result == 6 || result == 7) {
             System.out.println("Pair GOT: " + result + "\n");
         }
    }

    @Test
    @DisplayName("Straights Results")
    @RepeatedTest(100)
    void straights() {
        Map<Player, Integer> results = logic.findStraights(cardsIntArray, cardsSuitArray);
        int result = results.get(player);
        if (result == 4 || result == 9 || result == 10) {
            System.out.println("Straight GOT: " + result + "\n");
        }
    }
}