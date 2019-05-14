package ca.gotchasomething.mynance.data;
//Db description

public class SetUpDb {
    private int incomeDone;
    private int billsDone;
    private int debtsDone;
    private int savingsDone;
    private int budgetDone;
    private int balanceDone;
    private Double balanceAmount;
    private int tourDone;
    private long id;

    public SetUpDb(
            int incomeDone,
            int billsDone,
            int debtsDone,
            int savingsDone,
            int budgetDone,
            int balanceDone,
            Double balanceAmount,
            int tourDone,
            long id) {
        this.incomeDone = incomeDone;
        this.billsDone = billsDone;
        this.debtsDone = debtsDone;
        this.savingsDone = savingsDone;
        this.budgetDone = budgetDone;
        this.balanceDone = balanceDone;
        this.balanceAmount = balanceAmount;
        this.tourDone = tourDone;
        this.id = id;
    }

    public int getIncomeDone() { return incomeDone; }
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

    public Double getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(Double balanceAmount) { this.balanceAmount = balanceAmount; }

    public int getTourDone() { return tourDone; }
    public void setTourDone(int tourDone) { this.tourDone = tourDone; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

}
