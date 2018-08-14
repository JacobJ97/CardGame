public class Pot {

    private int smallBlind;
    private int bigBlind;
    public int totalPot;
    public int callTotal;

    Pot(int smallBlind, int bigBlind) {
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        setPotTotal(smallBlind + bigBlind);
        setCallTotal(bigBlind);
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public void setSmallBlind(int smallBlind) {
        this.smallBlind = smallBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    public void setPotTotal(int total) {
        setCallTotal(total);
        totalPot += total;
    }

    private void setCallTotal(int bet) {
        if (bet > callTotal) {
            callTotal = bet;
        }
    }

    public int getPotTotal() {
        return totalPot;
    }

    public int getCallTotal() {
        return callTotal;
    }
}
