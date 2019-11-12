package ca.gotchasomething.mynance.data;
//Db description

public class AccountsDb {
    private String acctName;
    private Double acctBal;
    private String acctDebtSav;
    private Double acctMax; //limit for debts, goal for sav
    private Double acctIntRate;
    private Double acctPaytsTo;
    private Double acctAnnPaytsTo;
    private String acctEndDate;
    private Double acctDebtToPay; //debts only
    private long id;

    public AccountsDb(
            String acctName,
            Double acctBal,
            String acctDebtSav,
            Double acctMax,
            Double acctIntRate,
            Double acctPaytsTo,
            Double acctAnnPaytsTo,
            String acctEndDate,
            Double acctDebtToPay,
            long id) {
        this.acctName = acctName;
        this.acctBal = acctBal;
        this.acctDebtSav = acctDebtSav;
        this.acctMax = acctMax;
        this.acctIntRate = acctIntRate;
        this.acctPaytsTo = acctPaytsTo;
        this.acctAnnPaytsTo = acctAnnPaytsTo;
        this.acctEndDate = acctEndDate;
        this.acctDebtToPay = acctDebtToPay;
        this.id = id;
    }

    public String getAcctName() { return acctName; }
    public void setAcctName(String acctName) { this.acctName = acctName; }

    public Double getAcctBal() { return acctBal; }
    public void setAcctBal(Double acctBal) { this.acctBal = acctBal; }

    public String getAcctDebtSav() { return acctDebtSav; }
    public void setAcctDebtSav(String acctDebtSav) { this.acctDebtSav = acctDebtSav; }

    public Double getAcctMax() { return acctMax; }
    public void setAcctMax(Double acctMax) { this.acctMax = acctMax; }

    public Double getAcctIntRate() { return acctIntRate; }
    public void setAcctIntRate(Double acctIntRate) { this.acctIntRate = acctIntRate; }

    public Double getAcctPaytsTo() { return acctPaytsTo; }
    public void setAcctPaytsTo(Double acctPaytsTo) { this.acctPaytsTo = acctPaytsTo; }

    public Double getAcctAnnPaytsTo() { return acctAnnPaytsTo; }
    public void setAcctAnnPaytsTo(Double acctAnnPaytsTo) { this.acctAnnPaytsTo = acctAnnPaytsTo; }

    public String getAcctEndDate() { return acctEndDate; }
    public void setAcctEndDate(String acctEndDate) { this.acctEndDate = acctEndDate; }

    public Double getAcctDebtToPay() { return acctDebtToPay; }
    public void setAcctDebtToPay(Double acctDebtToPay) { this.acctDebtToPay = acctDebtToPay; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
}
