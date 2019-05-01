package ca.gotchasomething.mynance.data;
//Db description

public class CurrentDb {
    private Double currentAccount;
    private Double currentB;
    private Double currentA;
    private Double currentOwingA;
    private int currentPageId;
    private long id;

    public CurrentDb(
            Double currentAccount,
            Double currentB,
            Double currentA,
            Double currentOwingA,
            int currentPageId,
            long id) {
        this.currentAccount = currentAccount;
        this.currentB = currentB;
        this.currentA = currentA;
        this.currentOwingA = currentOwingA;
        this.currentPageId = currentPageId;
        this.id = id;
    }

    public Double getCurrentAccount() { return currentAccount; }
    public void setCurrentAccount(Double currentAccountBalance) { this.currentAccount = currentAccount; }

    public Double getCurrentB() { return currentB; }
    public void setCurrentB(Double currentB) { this.currentB = currentB; }

    public Double getCurrentA() { return currentA; }
    public void setCurrentA(Double currentA) { this.currentA = currentA; }

    public Double getCurrentOwingA() { return currentOwingA; }
    public void setCurrentOwingA(Double currentOwingA) { this.currentOwingA = currentOwingA; }

    public int getCurrentPageId() { return currentPageId; }
    public void setCurrentPageId(int currentPageId) { this.currentPageId = currentPageId; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
}
