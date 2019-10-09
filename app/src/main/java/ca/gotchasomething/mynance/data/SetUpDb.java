package ca.gotchasomething.mynance.data;
//Db description

public class SetUpDb {
    private String latestDone;
    private Double balanceAmount;
    private long id;

    public SetUpDb(
            String latestDone,
            Double balanceAmount,
            long id) {
        this.latestDone = latestDone;
        this.balanceAmount = balanceAmount;
        this.id = id;
    }

    public String getLatestDone() { return latestDone; }
    public void setLatestDone(String latestDone) { this.latestDone = latestDone; }

    public Double getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(Double balanceAmount) { this.balanceAmount = balanceAmount; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

}
