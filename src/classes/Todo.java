package classes;

import classes.Cell;

public class Todo implements Cell {
    private int ID;
    private String name;
    private int category_id;

    public Todo(int ID, String name, int category_id) {
        this.ID = ID;
        this.name = name;
        this.category_id = category_id;
    }

    public int getCategory_id() {
        return category_id;
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
