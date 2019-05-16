package ca.gotchasomething.mynance.data;
//Db description

public class SetUpDb {
    private String latestDone;
    /*private int incomeDone;
    private int billsDone;
    private int debtsDone;
    private int savingsDone;
    private int budgetDone;
    private int balanceDone;
    private int tourDone;*/
    private Double balanceAmount;
    private long id;

    public SetUpDb(
            String latestDone,
            /*int incomeDone,
            int billsDone,
            int debtsDone,
            int savingsDone,
            int budgetDone,
            int balanceDone,
            int tourDone,*/
            Double balanceAmount,
            long id) {
        this.latestDone = latestDone;
        /*this.incomeDone = incomeDone;
        this.billsDone = billsDone;
        this.debtsDone = debtsDone;
        this.savingsDone = savingsDone;
        this.budgetDone = budgetDone;
        this.balanceDone = balanceDone;
        this.tourDone = tourDone;*/
        this.balanceAmount = balanceAmount;
        this.id = id;
    }

    public String getLatestDone() { return latestDone; }
    public void setLatestDone(String latestDone) { this.latestDone = latestDone; }

    /*public int getIncomeDone() { return incomeDone; }
    public void setIncomeDone(int incomeDone) { this.incomeDone = incomeDone; }

    public int getBillsDone() { return billsDone; }
    public void setBillsDone(int billsDone) { this.billsDone = billsDone; }

    public int getDebtsDone() { return debtsDone; }
    public void setDebtsDone(int debtsDone) { this.debtsDone = debtsDone; }

    public int getSavingsDone() { return savingsDone; }
    public void setSavingsDone(int savingsDone) { this.savingsDone = savingsDone; }

    public int getBudgetDone() { return budgetDone; }
    public void setBudgetDone(int budgetDone) { this.budgetDone = budgetDone; }

    public int getBalanceDone() { return balanceDone; }
    public void setBalanceDone(int balanceDone) { this.balanceDone = balanceDone; }

    public int getTourDone() { return tourDone; }
    public void setTourDone(int tourDone) { this.tourDone = tourDone; }*/

    public Double getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(Double balanceAmount) { this.balanceAmount = balanceAmount; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

}
