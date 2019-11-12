package ca.gotchasomething.mynance.data;
//Db description

public class TransactionsDb {
    private String transType; //"in" or "out" or "transfer" or "ccPayment"
    private String transIsCC;
    private String transBdgtCat;
    private long transBdgtId;
    private Double transAmt;
    private Double transAmtInA;
    private Double transAmtInOwing;
    private Double transAmtInB;
    private Double transAmtOutA;
    private Double transAmtOutOwing;
    private Double transAmtOutB;
    private long transToAcctId;
    private String transToAcctName;
    private String transToDebtSav; //"D" or "S"
    private long transFromAcctId;
    private String transFromAcctName;
    private String transFromDebtSav; //"D" or "S"
    private String transBdgtPriority; //out only
    private String transBdgtWeekly; //out only
    private String transCCToPay;
    private String transCCPaid;
    private String transCreatedOn;
    private long id;

    public TransactionsDb(
            String transType,
            String transIsCC,
            String transBdgtCat,
            long transBdgtId,
            Double transAmt,
            Double transAmtInA,
            Double transAmtInOwing,
            Double transAmtInB,
            Double transAmtOutA,
            Double transAmtOutOwing,
            Double transAmtOutB,
            long transToAcctId,
            String transToAcctName,
            String transToDebtSav,
            long transFromAcctId,
            String transFromAcctName,
            String transFromDebtSav,
            String transBdgtPriority,
            String transBdgtWeekly,
            String transCCToPay,
            String transCCPaid,
            String transCreatedOn,
            long id) {
        this.transType = transType;
        this.transIsCC = transIsCC;
        this.transBdgtCat = transBdgtCat;
        this.transBdgtId = transBdgtId;
        this.transAmt = transAmt;
        this.transAmtInA = transAmtInA;
        this.transAmtInOwing = transAmtInOwing;
        this.transAmtInB = transAmtInB;
        this.transAmtOutA = transAmtOutA;
        this.transAmtOutOwing = transAmtOutOwing;
        this.transAmtOutB = transAmtOutB;
        this.transToAcctId = transToAcctId;
        this.transToAcctName = transToAcctName;
        this.transToDebtSav = transToDebtSav;
        this.transFromAcctId = transFromAcctId;
        this.transFromAcctName = transFromAcctName;
        this.transFromDebtSav = transFromDebtSav;
        this.transBdgtPriority = transBdgtPriority;
        this.transBdgtWeekly = transBdgtWeekly;
        this.transCCToPay = transCCToPay;
        this.transCCPaid = transCCPaid;
        this.transCreatedOn = transCreatedOn;
        this.id = id;
    }

    public String getTransType() {
        return transType;
    }
    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransIsCC() {
        return transIsCC;
    }
    public void setTransIsCC(String transIsCC) {
        this.transIsCC = transIsCC;
    }

    public String getTransBdgtCat() {
        return transBdgtCat;
    }
    public void setTransBdgtCat(String transBdgtCat) {
        this.transBdgtCat = transBdgtCat;
    }

    public long getTransBdgtId() {
        return transBdgtId;
    }
    public void setTransBdgtId(long transBdgtId) {
        this.transBdgtId = transBdgtId;
    }

    public Double getTransAmt() {
        return transAmt;
    }
    public void setTransAmt(Double transAmt) {
        this.transAmt = transAmt;
    }

    public Double getTransAmtInA() {
        return transAmtInA;
    }
    public void setTransAmtInA(Double transAmtInA) {
        this.transAmtInA = transAmtInA;
    }

    public Double getTransAmtInOwing() {
        return transAmtInOwing;
    }
    public void setTransAmtInOwing(Double transAmtInOwing) { this.transAmtInOwing = transAmtInOwing; }

    public Double getTransAmtInB() {
        return transAmtInB;
    }
    public void setTransAmtInB(Double transAmtInB) {
        this.transAmtInB = transAmtInB;
    }

    public Double getTransAmtOutA() {
        return transAmtOutA;
    }
    public void setTransAmtOutA(Double transAmtOutA) {
        this.transAmtOutA = transAmtOutA;
    }

    public Double getTransAmtOutOwing() {
        return transAmtOutOwing;
    }
    public void setTransAmtOutOwing(Double transAmtOutOwing) { this.transAmtOutOwing = transAmtOutOwing; }

    public Double getTransAmtOutB() {
        return transAmtOutB;
    }
    public void setTransAmtOutB(Double transAmtOutB) {
        this.transAmtOutB = transAmtOutB;
    }

    public long getTransToAcctId() {
        return transToAcctId;
    }
    public void setTransToAcctId(long transToAcctId) {
        this.transToAcctId = transToAcctId;
    }

    public String getTransToAcctName() {
        return transToAcctName;
    }
    public void setTransToAcctName(String transToAcctName) { this.transToAcctName = transToAcctName; }

    public String getTransToDebtSav() {
        return transToDebtSav;
    }
    public void setTransToDebtSav(String transToDebtSav) {
        this.transToDebtSav = transToDebtSav;
    }

    public long getTransFromAcctId() {
        return transFromAcctId;
    }
    public void setTransFromAcctId(long transFromAcctId) {
        this.transFromAcctId = transFromAcctId;
    }

    public String getTransFromAcctName() {
        return transFromAcctName;
    }
    public void setTransFromAcctName(String transFromAcctName) { this.transFromAcctName = transFromAcctName; }

    public String getTransFromDebtSav() {
        return transFromDebtSav;
    }
    public void setTransFromDebtSav(String transFromDebtSav) { this.transFromDebtSav = transFromDebtSav; }

    public String getTransBdgtPriority() {
        return transBdgtPriority;
    }
    public void setTransBdgtPriority(String transBdgtPriority) { this.transBdgtPriority = transBdgtPriority; }

    public String getTransBdgtWeekly() {
        return transBdgtWeekly;
    }
    public void setTransBdgtWeekly(String transBdgtWeekly) { this.transBdgtWeekly = transBdgtWeekly; }

    public String getTransCCToPay() {
        return transCCToPay;
    }
    public void setTransCCToPay(String transCCToPay) {
        this.transCCToPay = transCCToPay;
    }

    public String getTransCCPaid() {
        return transCCPaid;
    }
    public void setTransCCPaid(String transCCPaid) {
        this.transCCPaid = transCCPaid;
    }

    public String getTransCreatedOn() {
        return transCreatedOn;
    }
    public void setTransCreatedOn(String transCreatedOn) {
        this.transCreatedOn = transCreatedOn;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getTransBdgtCat();
    }
}
