package ca.gotchasomething.mynance.data;
//Db description

public class BudgetDb {
    private String bdgtCat;
    private Double bdgtPaytAmt; //amt regularly pd towards this category
    private String bdgtExpInc; //"E" or "I" or "N/A"
    private Double bdgtPaytFrq; //frequency of payts towards this category
    private Double bdgtAnnPayt; //annual amt pd towards this category
    private String bdgtPriority; //exp only ("A" or "B" or "N/A"
    private String bdgtWeekly; //exp only ("Y" or "N" or "N/A"
    private long id;

    public BudgetDb(
            String bdgtCat,
            Double bdgtPaytAmt,
            String bdgtExpInc,
            Double bdgtPaytFrq,
            Double bdgtAnnPayt,
            String bdgtPriority,
            String bdgtWeekly,
            long id) {
        this.bdgtCat = bdgtCat;
        this.bdgtPaytAmt = bdgtPaytAmt;
        this.bdgtExpInc = bdgtExpInc;
        this.bdgtPaytFrq = bdgtPaytFrq;
        this.bdgtAnnPayt = bdgtAnnPayt;
        this.bdgtPriority = bdgtPriority;
        this.bdgtWeekly = bdgtWeekly;
        this.id = id;
    }

    public String getBdgtCat() { return bdgtCat; }
    public void setBdgtCat(String bdgtCat) { this.bdgtCat = bdgtCat; }

    public Double getBdgtPaytAmt() { return bdgtPaytAmt; }
    public void setBdgtPaytAmt(Double bdgtPaytAmt) { this.bdgtPaytAmt = bdgtPaytAmt; }

    public String getBdgtExpInc() { return bdgtExpInc; }
    public void setBdgtExpInc(String bdgtExpInc) { this.bdgtExpInc = bdgtExpInc; }

    public Double getBdgtPaytFrq() { return bdgtPaytFrq; }
    public void setBdgtPaytFrq(Double bdgtPaytFrq) { this.bdgtPaytFrq = bdgtPaytFrq; }

    public Double getBdgtAnnPayt() { return bdgtAnnPayt; }
    public void setBdgtAnnPayt(Double bdgtAnnPayt) { this.bdgtAnnPayt = bdgtAnnPayt; }

    public String getBdgtPriority() { return bdgtPriority; }
    public void setBdgtPriority(String bdgtPriority) { this.bdgtPriority = bdgtPriority; }

    public String getBdgtWeekly() { return bdgtWeekly; }
    public void setBdgtWeekly(String bdgtWeekly) { this.bdgtWeekly = bdgtWeekly; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @Override
    public String toString() { return getBdgtCat(); }
}
