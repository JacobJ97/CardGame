import org.junit.jupiter.api.*;


import java.util.ArrayList;
import java.util.Map;

@DisplayName("Example")
class LogicTest {

    private Logic logic;

    @BeforeAll
    static void beforeAll() {

    }

    @BeforeEach
    void beforeEach() {
        Deck cards = new Deck();
        Player player = new Player("bob", 500);
        ArrayList<Card> dealtCards = cards.dealCards(7);
        //System.out.println(dealtCards);
        this.logic = new Logic(dealtCards, player);
    }

    @AfterEach
    void afterEach() {

    }

    @AfterAll
    static void afterAll() {

    }

    @Test
    @DisplayName("Test")
    @RepeatedTest(1000)
    void logic_test() {
        Object[] cardInfo = logic.determineHand();
        if ((int)cardInfo[0] == 8) {
            for (int counter = 0; counter < cardInfo.length; counter++) {
                System.out.println(cardInfo[counter]);
            }
        }
    }
}