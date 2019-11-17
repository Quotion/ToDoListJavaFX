package sample;

public class Cell {
    private int ID;
    private String name;

    public Cell(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.ID;
    }
}
