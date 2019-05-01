package ca.gotchasomething.mynance.data;
//Db description

public class DebtDb {
    private String debtName;
    private Double debtLimit;
    private Double debtAmount;
    private Double debtRate;
    private Double debtPayments;
    private Double debtFrequency;
    private Double debtAnnualIncome;
    private String debtEnd;
    private Double debtToPay;
    private long expRefKeyD;
    private long incRefKeyD;
    private long id;

    public DebtDb (
            String debtName,
            Double debtLimit,
            Double debtAmount,
            Double debtRate,
            Double debtPayments,
            Double debtFrequency,
            Double debtAnnualIncome,
            String debtEnd,
            Double debtToPay,
            long expRefKeyD,
            long incRefKeyD,
            long id) {
        this.debtName = debtName;
        this.debtLimit = debtLimit;
        this.debtAmount = debtAmount;
        this.debtRate = debtRate;
        this.debtPayments = debtPayments;
        this.debtFrequency = debtFrequency;
        this.debtAnnualIncome = debtAnnualIncome;
        this.debtEnd = debtEnd;
        this.debtToPay = debtToPay;
        this.expRefKeyD = expRefKeyD;
        this.incRefKeyD = incRefKeyD;
        this.id = id;
    }

    public String getDebtName() { return debtName; }
    public void setDebtName(String debtName) { this.debtName = debtName; }

    public Double getDebtLimit() { return debtLimit; }
    public void setDebtLimit(Double debtLimit) { this.debtLimit = debtLimit; }

    public Double getDebtAmount() { return debtAmount; }
    public void setDebtAmount(Double debtAmount) { this.debtAmount = debtAmount; }

    public Double getDebtRate() { return debtRate; }
    public void setDebtRate(Double debtRate) { this.debtRate = debtRate; }

    public Double getDebtPayments() { return debtPayments; }
    public void setDebtPayments(Double debtPayments) { this.debtPayments = debtPayments; }

    public Double getDebtFrequency() { return debtFrequency; }
    public void setDebtFrequency(Double debtFrequency) { this.debtFrequency = debtFrequency; }

    public Double getDebtAnnualIncome() { return debtAnnualIncome; }
    public void setDebtAnnualIncome(Double debtAnnualIncome) { this.debtAnnualIncome = debtAnnualIncome; }

    public String getDebtEnd() { return debtEnd; }
    public void setDebtEnd(String debtEnd) { this.debtEnd = debtEnd; }

    public Double getDebtToPay() { return debtToPay; }
    public void setDebtToPay(Double debtToPay) { this.debtToPay = debtToPay; }

    public long getExpRefKeyD() { return expRefKeyD; }
    public void setExpRefKeyD(long expRefKeyD) { this.expRefKeyD = expRefKeyD; }

    public long getIncRefKeyD() { return incRefKeyD; }
    public void setIncRefKeyD(long incRefKeyD) { this.incRefKeyD = incRefKeyD; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @Override
    public String toString() { return getDebtName(); }
}
