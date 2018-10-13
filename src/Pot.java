public class Pot {

    private int smallBlind;
    private int bigBlind;
    private int totalPot;
    private int callTotal;

    Pot(int smallBlind, int bigBlind) {
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        setPotTotal(smallBlind);
        setPotTotal(bigBlind);
    }

    int getSmallBlind() {
        return smallBlind;
    }

    int getBigBlind() {
        return bigBlind;
    }

    void emptyPot(int takeAmount) {
        this.totalPot -= takeAmount;
    }

    void setSmallBlind(int smallBlind) {
        this.smallBlind = smallBlind;
    }

    void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    void setPotTotal(int total) {
        setCallTotal(total);
        totalPot += total;
    }

    private void setCallTotal(int bet) {
        if (bet > callTotal) {
            callTotal = bet;
        }
    }

    void setCallTotalOverride(int bigBlind) {
        callTotal = bigBlind;
    }

    int getPotTotal() {
        return totalPot;
    }

    int getCallTotal() {
        return callTotal;
    }
}
