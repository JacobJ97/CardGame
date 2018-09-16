import java.util.ArrayList;

public class ComputerBrain {

    private Object[] handInformation;

    ComputerBrain(Object[] handInformation) {
        this.handInformation = handInformation;
    }

    void determineMove(int turnNumber) {
        int rank = (int)handInformation[0];
        @SuppressWarnings("unchecked")
        ArrayList<Integer> hands = (ArrayList<Integer>)handInformation[1];
        @SuppressWarnings("unchecked")
        ArrayList<Integer> topCards = (ArrayList<Integer>)handInformation[2];
        getRankStrength(rank, turnNumber, hands, topCards);
    }

    private void getRankStrength(int handRank, int handTurn, ArrayList<Integer> hands, ArrayList<Integer> topCards) {
        if (handTurn == 0) {
            if (handRank == 2) {
                int pairOf = hands.get(0);
                if (pairOf > 10) {
                    randomMoveDecider(1, 30, 64, 0);
                } else {
                    randomMoveDecider(1, 25, 69, 0);
                }
            }
            else if (handRank == 1) {
                boolean goodCard;
                int highHand1 = hands.get(0);
                int highHand2 = hands.get(1);
                if (highHand1 >= 10 && highHand1 <= 13) {
                    goodCard = true;
                    if (highHand2 >= 10 && highHand2 <= 13) {
                        randomMoveDecider(1, 20, 79,0);
                    }
                }
                else if (highHand1 > 9) {
                    goodCard = false;
                }
            }
        }
        else if (handTurn == 1) {

        }
        else if (handTurn == 2) {

        }
        else if (handTurn == 3) {

        }
    }

    private int randomMoveDecider(int allIn, int raise, int call, int fold) {
        int move = 5;
        return move;
    }

}
