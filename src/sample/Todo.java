package sample;

public class Todo extends Cell {
    private int category_id;

    public Todo(int ID, String name, int category_id) {
        super(ID, name);
        this.category_id = category_id;
    }

    public int getCategory_id() {
        return this.category_id;
    }
}
