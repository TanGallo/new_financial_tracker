package ca.gotchasomething.mynance.data;
//Db description

public class CurrentDb {
    private Double currentAccountBalance;
    private Double currentAvailableBalance;
    private int currentPageId;
    private long id;

    public CurrentDb(
            Double currentAccountBalance,
            Double currentAvailableBalance,
            int currentPageId,
            long id) {
        this.currentAccountBalance = currentAccountBalance;
        this.currentAvailableBalance = currentAvailableBalance;
        this.currentPageId = currentPageId;
        this.id = id;
    }

    public Double getCurrentAccountBalance() { return currentAccountBalance; }
    public void setCurrentAccountBalance(Double currentAccountBalance) { this.currentAccountBalance = currentAccountBalance; }

    public Double getCurrentAvailableBalance() { return currentAvailableBalance; }
    public void setCurrentAvailableBalance(Double currentAvailableBalance) { this.currentAvailableBalance = currentAvailableBalance; }

    public int getCurrentPageId() { return currentPageId; }
    public void setCurrentPageId(int currentPageId) { this.currentPageId = currentPageId; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    //@Override
    //public String toString() { return getMoneyInCat(); }
}
