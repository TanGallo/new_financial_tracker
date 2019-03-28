package ca.gotchasomething.mynance.data;
//Db description

public class SavingsDb {
    private String savingsName;
    private Double savingsAmount;
    private Double savingsGoal;
    private Double savingsPayments;
    private Double savingsFrequency;
    private Double savingsRate;
    private Double savingsAnnualIncome;
    private String savingsDate;
    private long expRefKeyS;
    private long incRefKeyS;
    private long id;

    public SavingsDb(
            String savingsName,
            Double savingsAmount,
            Double savingsGoal,
            Double savingsPayments,
            Double savingsFrequency,
            Double savingsRate,
            Double savingsAnnualIncome,
            String savingsDate,
            long expRefKeyS,
            long incRefKeyS,
            long id) {
        this.savingsName = savingsName;
        this.savingsAmount = savingsAmount;
        this.savingsGoal = savingsGoal;
        this.savingsPayments = savingsPayments;
        this.savingsFrequency = savingsFrequency;
        this.savingsRate = savingsRate;
        this.savingsAnnualIncome = savingsAnnualIncome;
        this.savingsDate = savingsDate;
        this.expRefKeyS = expRefKeyS;
        this.incRefKeyS = incRefKeyS;
        this.id = id;
    }

    public String getSavingsName() { return savingsName; }
    public void setSavingsName(String savingsName) { this.savingsName = savingsName; }

    public Double getSavingsAmount() { return savingsAmount; }
    public void setSavingsAmount(Double savingsAmount) { this.savingsAmount = savingsAmount; }

    public Double getSavingsGoal() { return savingsGoal; }
    public void setSavingsGoal(Double savingsGoal) { this.savingsGoal = savingsGoal; }

    public Double getSavingsPayments() { return savingsPayments; }
    public void setSavingsPayments(Double savingsPayments) { this.savingsPayments = savingsPayments; }

    public Double getSavingsFrequency() { return savingsFrequency; }
    public void setSavingsFrequency(Double savingsFrequency) { this.savingsFrequency = savingsFrequency; }

    public Double getSavingsRate() { return savingsRate; }
    public void setSavingsRate(Double savingsRate) { this.savingsRate = savingsRate; }

    public Double getSavingsAnnualIncome() { return savingsAnnualIncome; }
    public void setSavingsAnnualIncome(Double savingsAnnualIncome) { this.savingsAnnualIncome = savingsAnnualIncome; }

    public String getSavingsDate() { return savingsDate; }
    public void setSavingsDate(String savingsDate) { this.savingsDate = savingsDate; }

    public long getExpRefKeyS() { return expRefKeyS; }
    public void setExpRefKeyS(long expRefKeyS) { this.expRefKeyS = expRefKeyS; }

    public long getIncRefKeyS() { return incRefKeyS; }
    public void setIncRefKeyS(long incRefKeyS) { this.incRefKeyS = incRefKeyS; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @Override
    public String toString() { return getSavingsName(); }
}
