package ca.gotchasomething.mynance.data;
//Db description

public class MoneyOutDb {
    private String moneyOutCat; //1
    private String moneyOutPriority; //2
    private String moneyOutWeekly; //3
    private Double moneyOutAmount; //4
    private String moneyOutCreatedOn; //5
    private String moneyOutCC; //6
    private int moneyOutToPay; //7
    private int moneyOutPaid; //8
    private long expRefKeyMO; //9
    private long id; //0

    public MoneyOutDb(
            String moneyOutCat,
            String moneyOutPriority,
            String moneyOutWeekly,
            Double moneyOutAmount,
            String moneyOutCreatedOn,
            String moneyOutCC,
            int moneyOutToPay,
            int moneyOutPaid,
            long expRefKeyMO,
            long id) {
        this.moneyOutCat = moneyOutCat;
        this.moneyOutPriority = moneyOutPriority;
        this.moneyOutWeekly = moneyOutWeekly;
        this.moneyOutAmount = moneyOutAmount;
        this.moneyOutCreatedOn = moneyOutCreatedOn;
        this.moneyOutCC = moneyOutCC;
        this.moneyOutToPay = moneyOutToPay;
        this.moneyOutPaid = moneyOutPaid;
        this.expRefKeyMO = expRefKeyMO;
        this.id = id;
    }

    public String getMoneyOutCat() { return moneyOutCat; }
    public void setMoneyOutCat(String moneyOutCat) { this.moneyOutCat = moneyOutCat; }

    public String getMoneyOutPriority() { return moneyOutPriority; }
    public void setMoneyOutPriority(String moneyOutPriority) { this.moneyOutPriority = moneyOutPriority; }

    public String getMoneyOutWeekly() { return moneyOutWeekly; }
    public void setMoneyOutWeekly(String moneyOutWeekly) { this.moneyOutWeekly = moneyOutWeekly; }

    public Double getMoneyOutAmount() { return moneyOutAmount; }
    public void setMoneyOutAmount(Double moneyOutAmount) { this.moneyOutAmount = moneyOutAmount; }

    public String getMoneyOutCreatedOn() { return moneyOutCreatedOn; }
    public void setMoneyOutCreatedOn(String moneyOutCreatedOn) { this.moneyOutCreatedOn = moneyOutCreatedOn; }

    public String getMoneyOutCC() { return moneyOutCC; }
    public void setMoneyOutCC(String moneyOutCC) { this.moneyOutCC = moneyOutCC; }

    public int getMoneyOutToPay() { return moneyOutToPay; }
    public void setMoneyOutToPay(int moneyOutToPay) { this.moneyOutToPay = moneyOutToPay; }

    public int getMoneyOutPaid() { return moneyOutPaid; }
    public void setMoneyOutPaid(int moneyOutPaid) { this.moneyOutPaid = moneyOutPaid; }

    public long getExpRefKeyMO() { return expRefKeyMO; }
    public void setExpRefKeyMO(long expRefKeyMO) { this.expRefKeyMO = expRefKeyMO; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @Override
    public String toString() { return getMoneyOutCat(); }
}
