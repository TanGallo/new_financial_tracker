package ca.gotchasomething.mynance.data;
//Db description

public class CurrentDb {
    private Double currentAccountBalance;
    private Double currentAvailableBalance;
    private Double neededForA;
    private int currentPageId;
    private long id;

    public CurrentDb(
            Double currentAccountBalance,
            Double currentAvailableBalance,
            Double neededForA,
            int currentPageId,
            long id) {
        this.currentAccountBalance = currentAccountBalance;
        this.currentAvailableBalance = currentAvailableBalance;
        this.neededForA = neededForA;
        this.currentPageId = currentPageId;
        this.id = id;
    }

    public Double getCurrentAccountBalance() { return currentAccountBalance; }
    public void setCurrentAccountBalance(Double currentAccountBalance) { this.currentAccountBalance = currentAccountBalance; }

    public Double getCurrentAvailableBalance() { return currentAvailableBalance; }
    public void setCurrentAvailableBalance(Double currentAvailableBalance) { this.currentAvailableBalance = currentAvailableBalance; }

    public Double getNeededForA() { return neededForA; }
    public void setNeededForA(Double neededForA) { this.neededForA = neededForA; }

    public int getCurrentPageId() { return currentPageId; }
    public void setCurrentPageId(int currentPageId) { this.currentPageId = currentPageId; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
}
