package ca.gotchasomething.mynance.data;
//Db description

public class AccountsDb {
    private String acctName;
    private Double acctBal;
    private String isDebt;
    private String isSav;
    private Double acctMax;
    private Double intRate;
    private Double paytsTo;
    private Double annPaytsTo;
    private String endDate;
    private Double debtToPay;
    private long id;
    //private Double savGoal;
    //private Double debtOwing;
    //private Double savingsAmount;
    //private Double savingsPayments;
    //private Double savingsRate;
    //private Double savingsAnnualPayments;
    //private String savDate;
    //private long debtId;
    //private long savId;

    public AccountsDb(
            String acctName,
            Double acctBal,
            String isDebt,
            String isSav,
            Double acctMax,
            Double intRate,
            Double paytsTo,
            Double annPaytsTo,
            String endDate,
            Double debtToPay,
            long id) {
        this.acctName = acctName;
        this.acctBal = acctBal;
        this.isDebt = isDebt;
        this.isSav = isSav;
        this.acctMax = acctMax;
        this.intRate = intRate;
        this.paytsTo = paytsTo;
        this.annPaytsTo = annPaytsTo;
        this.endDate = endDate;
        this.debtToPay = debtToPay;
        this.id = id;
    }

    public String getAcctName() { return acctName; }
    public void setAcctName(String acctName) { this.acctName = acctName; }

    public Double getAcctBal() { return acctBal; }
    public void setAcctBal(Double acctBal) { this.acctBal = acctBal; }

    public String getIsDebt() { return isDebt; }
    public void setIsDebt(String isDebt) { this.isDebt = isDebt; }

    public String getIsSav() { return isSav; }
    public void setIsSav(String isSav) { this.isSav = isSav; }

    public Double getAcctMax() { return acctMax; }
    public void setAcctMax(Double acctMax) { this.acctMax = acctMax; }

    public Double getIntRate() { return intRate; }
    public void setIntRate(Double intRate) { this.intRate = intRate; }

    public Double getPaytsTo() { return paytsTo; }
    public void setPaytsTo(Double paytsTo) { this.paytsTo = paytsTo; }

    public Double getAnnPaytsTo() { return annPaytsTo; }
    public void setAnnPaytsTo(Double annPaytsTo) { this.annPaytsTo = annPaytsTo; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public Double getDebtToPay() { return debtToPay; }
    public void setDebtToPay(Double debtToPay) { this.debtToPay = debtToPay; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
}
