package classes;

public class Category implements Cell {
    private int ID;
    private String name;

    public Category(int ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getID() {
        return ID;
    }
}
