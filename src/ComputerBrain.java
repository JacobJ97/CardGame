import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class ComputerBrain {

    private Object[] handInformation;
    private Logic logic;
    private String move;
    private int raiseValue;
    private int straightNum;
    private int suitNum;

    ComputerBrain(Object[] handInformation, Logic logic) {
        this.handInformation = handInformation;
        this.logic = logic;
    }

    void determineMove(int turnNumber, Map<Player, String> playerMoves) {
        int rank = (int)handInformation[0];
        @SuppressWarnings("unchecked")
        ArrayList<Integer> hands = (ArrayList<Integer>)handInformation[1];
        @SuppressWarnings("unchecked")
        ArrayList<Integer> topCards = (ArrayList<Integer>)handInformation[2];
        if ((boolean)handInformation[3]) {
            int straightLength = (int)handInformation[4];
            int suitLength = (int)handInformation[5];
            if (straightLength >= 3) {
                straightNum = straightLength;
            }
            if (suitLength >= 3) {
                suitNum = suitLength;
            }
        }
        getRankStrength(rank, turnNumber, hands, topCards, playerMoves);
    }

    int getRaiseValue() {
        return raiseValue;
    }

    String getMove() {
        return move;
    }

    private void getRankStrength(int handRank, int handTurn, ArrayList<Integer> hands, ArrayList<Integer> topCards, Map<Player, String> playerMoves) {
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        for (Player player : playerMoves.keySet()) {
            String move = playerMoves.get(player);
            if (move.equals("raise")) {
                a =+ 1;
                b =+ 14;
                c =+ 50;
                d =+ 35;
            }
            else if (move.equals("call")) {
                a =+ 1;
                b =+ 24;
                c =+ 55;
                d =+ 25;
            }
            else if (move.equals("fold")) {
                a =+ 1;
                b =+ 34;
                c =+ 55;
                d =+ 10;
            }
        }

        a = (a / playerMoves.size()) - ((1 + 5) / 3);
        //
        //
        b = (b / playerMoves.size()) - ((14 + 25 + 35) / 3);
        c = (c / playerMoves.size()) - ((35 + 50 + 50) / 3);
        d = (d / playerMoves.size()) - ((50 + 15 + 10) / 3);


        if (handTurn == 0) {
            if (handRank == 2) {
                int pairOf = hands.get(0);
                if (pairOf > 10) {
                    randomMoveDecider(a + 1, b + 30, c + 64, 0);
                } else {
                    randomMoveDecider(a + 1, b + 25,  c + 69, 0);
                }
            }
            else if (handRank == 1) {
                boolean sameSuit = logic.isSameSuit();
                int highHand1 = hands.get(0);
                int highHand2 = hands.get(1);

                if (highHand1 >= 10 && highHand1 <= 13) {
                    if (highHand2 == (highHand2 - 1) || highHand2 == (highHand2 + 1)) {
                        if (sameSuit) {
                            randomMoveDecider(a + 1, b + 20, c + 79, 0);
                        }
                        else {
                            randomMoveDecider(a + 1, b + 10, c + 89, d);
                        }
                    }
                    else {
                        if (highHand2 >= 10 && highHand2 <= 13) {
                            if (sameSuit) {
                                randomMoveDecider(a + 1, b + 10, c + 89, 0);
                            }
                            else {
                                randomMoveDecider(a + 1, b + 5, c + 94,0);
                            }
                        }
                        else {
                            if (sameSuit) {
                                randomMoveDecider(a + 1, b + 5,c + 90, d + 4);
                            }
                            else {
                                randomMoveDecider(a + 1, b + 1, c + 86, d + 12);
                            }
                        }
                    }
                }
                else {
                    if (highHand2 == (highHand1 - 1) || highHand2 == (highHand1 + 1)) {
                        if (sameSuit) {
                            randomMoveDecider(a + 1, b + 10, c + 89, 0);
                        }
                        else {
                            randomMoveDecider(a + 1, b + 5, c + 94, 0);
                        }
                    }
                    else {
                        if (highHand2 >= 10 && highHand2 <= 13) {
                            if (sameSuit) {
                                randomMoveDecider(a + 1, b + 5, c + 94, 0);
                            }
                            else {
                                randomMoveDecider(a + 1, b + 2, c + 90, d + 7);
                            }
                        }
                        else {
                            if (sameSuit) {
                                randomMoveDecider(a + 1, b + 3, c + 90, d + 6);
                            }
                            else {
                                randomMoveDecider(a + 1, b + 1, c + 70, d + 28);
                            }
                        }
                    }
                }
            }
        }
        else if (handTurn == 1) {
            if (handRank >= 9) {
                randomMoveDecider(a + 5, b + 55, c + 40, 0);
            }

            if (handRank >= 8) {
                randomMoveDecider(a + 3, b + 40, c + 57, 0);
            }

            if (handRank >= 7) {
                randomMoveDecider(a + 2, b + 38, c + 60, 0);
            }

            if (handRank >= 6) {
                randomMoveDecider(a + 1, b + 36, c + 63, 0);
            }

            if (handRank >= 5) {
                randomMoveDecider(a + 1, b + 35, c + 64, d + 1);
            }

            if (handRank >= 4) {
                if (suitNum == 3) {
                    randomMoveDecider(a + 1, b + 25, c + 73, d + 2);
                } else if (suitNum == 4) {
                    randomMoveDecider(a + 1, b + 30, c + 68, d + 1);
                }
                else if (straightNum == 3) {
                    randomMoveDecider(a + 1, b + 23, c + 72, d + 4);
                }
                else if (straightNum == 4) {
                    randomMoveDecider(a + 1, b + 27, c + 70, d + 2);
                } else {
                    randomMoveDecider(a + 1, b + 20, c + 74, d + 5);
                }
            }

            if (handRank >= 3) {
                if (suitNum == 3) {
                    randomMoveDecider(a + 1, b + 20, c + 74, d + 5);
                } else if (suitNum == 4) {
                    randomMoveDecider(a + 1, b + 24, c + 72, d + 3);
                }
                else if (straightNum == 3) {
                    randomMoveDecider(a + 1, b + 17, c + 75, d + 7);
                }
                else if (straightNum == 4) {
                    randomMoveDecider(a + 1, b + 20, c + 74, d + 5);
                } else {
                    randomMoveDecider(a + 1, b + 15, c + 74, d + 10);
                }
            }

            if (handRank >= 2) {
                if (suitNum == 3) {
                    randomMoveDecider(a + 1, b + 20, c + 69, d + 10);
                } else if (suitNum == 4) {
                    randomMoveDecider(a + 1, b + 22, c + 69, d + 8);
                }
                else if (straightNum == 3) {
                    randomMoveDecider(a + 1, b + 17, c + 69, d + 12);
                }
                else if (straightNum == 4) {
                    randomMoveDecider(a + 1, b + 20, c + 69, d + 10);
                } else {
                    randomMoveDecider(a + 1, b + 15, c + 69, d + 14);
                }
            }

            if (handRank == 1) {
                if (suitNum == 3) {
                    randomMoveDecider(a + 1, b + 18, c + 56, d + 25);
                } else if (suitNum == 4) {
                    randomMoveDecider(a + 1, b + 20, c + 58, d + 21);
                }
                else if (straightNum == 3) {
                    randomMoveDecider(a + 1, b + 15, c + 55, d + 29);
                }
                else if (straightNum == 4) {
                    randomMoveDecider(a + 1, b + 18, c + 56, d + 25);
                } else {
                    randomMoveDecider(a + 1, b + 15, c + 50, d + 34);
                }
            }
        }
        else if (handTurn == 2) {
            if (handRank >= 9) {
                randomMoveDecider(a + 10, b + 60, c + 30, 0);
            }

            if (handRank >= 8) {
                randomMoveDecider(a + 5, b + 55, c + 40, 0);
            }

            if (handRank >= 7) {
                randomMoveDecider(a + 5, b + 55, c + 40, 0);
            }

            if (handRank >= 6) {
                randomMoveDecider(a + 5, b + 55, c + 40, 0);
            }

            if (handRank >= 5) {
                randomMoveDecider(a + 5, b + 25, c + 69, d + 1);
            }

            if (handRank >= 4) {
                if (suitNum == 3) {
                    randomMoveDecider(a + 5, b + 20, c + 74, d + 1);
                } else if (suitNum == 4) {
                    randomMoveDecider(a + 5, b + 23, c + 71, d + 1);
                }
                else if (straightNum == 3) {
                    randomMoveDecider(a + 5, b + 18, c + 75, d + 2);
                }
                else if (straightNum == 4) {
                    randomMoveDecider(a + 5, b + 20, c + 74, d + 1);
                } else {
                    randomMoveDecider(a + 5, b + 15, c + 77, d + 3);
                }
            }

            if (handRank >= 3) {
                if (suitNum == 3) {
                    randomMoveDecider(a + 5, b + 15, c + 78, d + 2);
                } else if (suitNum == 4) {
                    randomMoveDecider(a + 5, b + 18, c + 76, d + 1);
                }
                else if (straightNum == 3) {
                    randomMoveDecider(a + 5, b + 13, c + 79, d + 3);
                }
                else if (straightNum == 4) {
                    randomMoveDecider(a + 5, b + 15, c + 78, d + 2);
                } else {
                    randomMoveDecider(a + 5, b + 10, c + 80, d + 5);
                }
            }

            if (handRank >= 2) {
                if (suitNum == 3) {
                    randomMoveDecider(a + 1, b + 15, c + 74, d + 10);
                } else if (suitNum == 4) {
                    randomMoveDecider(a + 1, b + 15, c + 77, d + 7);
                }
                else if (straightNum == 3) {
                    randomMoveDecider(a + 1, b + 15, c + 72, d + 12);
                }
                else if (straightNum == 4) {
                    randomMoveDecider(a + 1, b + 15, c + 74, d + 10);
                } else {
                    randomMoveDecider(a + 1, b + 10, c + 74, d + 15);
                }
            }

            if (handRank == 1) {
                if (suitNum == 3) {
                    randomMoveDecider(a + 1, b + 10, c + 59, d + 30);
                } else if (suitNum == 4) {
                    randomMoveDecider(a + 1, b + 10, c + 69, d + 20);
                }
                else if (straightNum == 3) {
                    randomMoveDecider(a + 1, b + 10, c + 54, d + 35);
                }
                else if (straightNum == 4) {
                    randomMoveDecider(a + 1, b + 10, c + 59, d + 30);
                } else {
                    randomMoveDecider(a + 1, b + 10, c + 50, d + 39);
                }
            }
        }
        else if (handTurn == 3) {
            if (handRank >= 9) {
                randomMoveDecider(a + 20, b + 50, c + 30, 0);
            }

            if (handRank >= 8) {
                randomMoveDecider(a + 10, b + 50, c + 40, 0);
            }

            if (handRank >= 7) {
                randomMoveDecider(a + 10, b + 50, c + 40, 0);
            }

            if (handRank >= 6) {
                randomMoveDecider(a + 10, b + 50, c + 40, 0);
            }

            if (handRank >= 5) {
                randomMoveDecider(a + 5, b + 44, c + 50, d + 1);
            }

            if (handRank >= 4) {
                if (suitNum == 3) {
                    randomMoveDecider(a + 5, b + 40, c + 50, d + 5);
                } else if (suitNum == 4) {
                    randomMoveDecider(a + 5, b + 44, c + 50, d + 1);
                }
                else if (straightNum == 3) {
                    randomMoveDecider(a + 5, b + 40, c + 50, d + 5);
                }
                else if (straightNum == 4) {
                    randomMoveDecider(a + 5, b + 44, c + 50, d + 1);
                } else {
                    randomMoveDecider(a + 5, b + 40, c + 50, d + 5);
                }
            }

            if (handRank >= 3) {
                if (suitNum == 3) {
                    randomMoveDecider(a + 5, b + 35, c + 50, d + 10);
                } else if (suitNum == 4) {
                    randomMoveDecider(a + 5, b + 43, c + 50, d + 2);
                }
                else if (straightNum == 3) {
                    randomMoveDecider(a + 5, b + 40, c + 50, d + 10);
                }
                else if (straightNum == 4) {
                    randomMoveDecider(a + 5, b + 43, c + 50, d + 2);
                } else {
                    randomMoveDecider(a + 5, b + 40, c + 50, d + 10);
                }
            }

            if (handRank >= 2) {
                if (suitNum == 3) {
                    randomMoveDecider(a + 1, b + 30, c + 54, d + 15);
                } else if (suitNum == 4) {
                    randomMoveDecider(a + 1, b + 40, c + 56, d + 3);
                }
                else if (straightNum == 3) {
                    randomMoveDecider(a + 1, b + 30, c + 54, d + 15);
                }
                else if (straightNum == 4) {
                    randomMoveDecider(a + 1, b + 40, c + 56, d + 3);
                } else {
                    randomMoveDecider(a + 1, b + 30, c + 54, d + 15);
                }
            }

            if (handRank == 1) {
                if (suitNum == 3) {
                    randomMoveDecider(a + 1, b + 37, c + 37, d + 35);
                } else if (suitNum == 4) {
                    randomMoveDecider(a + 1, b + 44, c + 40, d + 15);
                }
                else if (straightNum == 3) {
                    randomMoveDecider(a + 1, b + 37, c + 37, d + 35);
                }
                else if (straightNum == 4) {
                    randomMoveDecider(a + 1, b + 44, c + 40, d + 15);
                } else {
                    randomMoveDecider(a + 1, b + 37, c + 37, d + 35);
                }
            }
        }

        if (move.equals("raise")) {
            determineRaiseValue(handRank);
        }
    }

    private void randomMoveDecider(int allIn, int raise, int call, int fold) {
        Random r = new Random();
        int s = r.nextInt(allIn + raise + call + fold) + 1;

        /*
        allin - 5, raise 30, call 50, fold
        all in: 1 - 5
        raise: 6 - 35
        call: 36 - 85
        fold: 86 - 100
        generated number is 46
         */
        //if 5 is equal to or greater than 46
        if (s <= allIn) {
            move = "allIn";
        }
        //if 5+30 (35) is equal to or greater than 46
        else if (s <= (raise + allIn)) {
            move = "raise";
        }
        //if 5+30+50 (85) is equal to or greater than 46
        //call will be called (lol)
        else if (s <= (call + allIn + raise)) {
            move = "call";
        }
        else if (s <= (fold + allIn + raise + call)) {
            move = "fold";
        }
        else {
            move = "fold";
        }
    }

    private void determineRaiseValue(int rank) {
        Random r = new Random();
        if (rank > 9) {
            raiseValue = r.nextInt(400) + 1;
        }
        else if (rank > 5) {
            raiseValue = r.nextInt(250) + 1;
        }
        else if (rank > 2) {
            raiseValue = r.nextInt(200) + 1;
        }
        else {
            raiseValue = r.nextInt(150) + 1;
        }
    }
}
