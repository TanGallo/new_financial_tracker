package ca.gotchasomething.mynance.data;
//Db description

public class CurrentDb {
    private Double currentA;
    private Double currentOwingA;
    private Double currentB;
    private int lastPageId;
    private String lastDate;
    private long id;

    public CurrentDb(
            Double currentA,
            Double currentOwingA,
            Double currentB,
            int lastPageId,
            String lastDate,
            long id) {
        this.currentA = currentA;
        this.currentOwingA = currentOwingA;
        this.currentB = currentB;
        this.lastPageId = lastPageId;
        this.lastDate = lastDate;
        this.id = id;
    }

    public Double getCurrentA() { return currentA; }
    public void setCurrentA(Double currentA) { this.currentA = currentA; }

    public Double getCurrentOwingA() { return currentOwingA; }
    public void setCurrentOwingA(Double currentOwingA) { this.currentOwingA = currentOwingA; }

    public Double getCurrentB() { return currentB; }
    public void setCurrentB(Double currentB) { this.currentB = currentB; }

    public int getLastPageId() { return lastPageId; }
    public void setLastPageId(int lastPageId) { this.lastPageId = lastPageId; }

    public String getLastDate() { return lastDate; }
    public void setLastDate(String lastDate) { this.lastDate = lastDate; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
}
