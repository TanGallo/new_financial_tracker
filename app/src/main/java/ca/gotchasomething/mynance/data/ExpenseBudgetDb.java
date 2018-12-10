package ca.gotchasomething.mynance.data;
//Db description

public class ExpenseBudgetDb {
    private String expenseName; //1
    private Double expenseAmount; //2
    private Double expenseFrequency; //3
    private String expensePriority; //4
    private String expenseWeekly; //5
    private Double expenseAnnualAmount; //6
    private Double expenseAAnnualAmount; //7
    private Double expenseBAnnualAmount; //8
    private long id; //0

    public ExpenseBudgetDb(
            String expenseName,
            Double expenseAmount,
            Double expenseFrequency,
            String expensePriority,
            String expenseWeekly,
            Double expenseAnnualAmount,
            Double expenseAAnnualAmount,
            Double expenseBAnnualAmount,
            long id) {
        this.expenseName = expenseName;
        this.expenseAmount = expenseAmount;
        this.expenseFrequency = expenseFrequency;
        this.expensePriority = expensePriority;
        this.expenseWeekly = expenseWeekly;
        this.expenseAnnualAmount = expenseAnnualAmount;
        this.expenseAAnnualAmount = expenseAAnnualAmount;
        this.expenseBAnnualAmount = expenseBAnnualAmount;
        this.id = id;
    }

    public String getExpenseName() { return expenseName; }
    public void setExpenseName(String expenseName) { this.expenseName = expenseName; }

    public Double getExpenseAmount() { return expenseAmount; }
    public void setExpenseAmount(Double expenseAmount) { this.expenseAmount = expenseAmount; }

    public Double getExpenseFrequency() { return expenseFrequency; }
    public void setExpenseFrequency(Double expenseFrequency) { this.expenseFrequency = expenseFrequency; }

    public String getExpensePriority() { return expensePriority; }
    public void setExpensePriority(String expensePriority) { this.expensePriority = expensePriority; }

    public String getExpenseWeekly() { return expenseWeekly; }
    public void setExpenseWeekly(String expenseWeekly) { this.expenseWeekly = expenseWeekly; }

    public Double getExpenseAnnualAmount() { return expenseAnnualAmount; }
    public void setExpenseAnnualAmount(Double expenseAnnualAmount) { this.expenseAnnualAmount = expenseAnnualAmount; }

    public Double getExpenseAAnnualAmount() { return expenseAAnnualAmount; }
    public void setExpenseAAnnualAmount(Double expenseAAnnualAmount) { this.expenseAAnnualAmount = expenseAAnnualAmount; }

    public Double getExpenseBAnnualAmount() { return expenseBAnnualAmount; }
    public void setExpenseBAnnualAmount(Double expenseBAnnualAmount) { this.expenseBAnnualAmount = expenseBAnnualAmount; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @Override
    public String toString() { return getExpenseName(); }
}
