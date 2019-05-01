package ca.gotchasomething.mynance.data;
//Db description

public class MoneyInDb {
    private String moneyInCat;
    private Double moneyInAmount;
    private Double moneyInA;
    private Double moneyInOwing;
    private Double moneyInB;
    private String moneyInCreatedOn;
    private long incRefKeyMI;
    private long id;

    public MoneyInDb(
            String moneyInCat,
            Double moneyInAmount,
            Double moneyInA,
            Double moneyInOwing,
            Double moneyInB,
            String moneyInCreatedOn,
            long incRefKeyMI,
            long id) {
        this.moneyInCat = moneyInCat;
        this.moneyInAmount = moneyInAmount;
        this.moneyInA = moneyInA;
        this.moneyInOwing = moneyInOwing;
        this.moneyInB = moneyInB;
        this.moneyInCreatedOn = moneyInCreatedOn;
        this.incRefKeyMI = incRefKeyMI;
        this.id = id;
    }

    public String getMoneyInCat() { return moneyInCat; }
    public void setMoneyInCat(String moneyInCat) { this.moneyInCat = moneyInCat; }

    public Double getMoneyInAmount() { return moneyInAmount; }
    public void setMoneyInAmount(Double moneyInAmount) { this.moneyInAmount = moneyInAmount; }

    public Double getMoneyInA() { return moneyInA; }
    public void setMoneyInA(Double moneyInA) { this.moneyInA = moneyInA; }

    public Double getMoneyInOwing() { return moneyInOwing; }
    public void setMoneyInOwing(Double moneyInOwing) { this.moneyInOwing = moneyInOwing; }

    public Double getMoneyInB() { return moneyInB; }
    public void setMoneyInB(Double moneyInB) { this.moneyInB = moneyInB; }

    public String getMoneyInCreatedOn() { return moneyInCreatedOn; }
    public void setMoneyInCreatedOn(String moneyInCreatedOn) { this.moneyInCreatedOn = moneyInCreatedOn; }

    public long getIncRefKeyMI() { return incRefKeyMI; }
    public void setIncRefKeyMI(long incRefKeyMI) { this.incRefKeyMI = incRefKeyMI; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @Override
    public String toString() { return getMoneyInCat(); }
}
