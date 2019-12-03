package sample;

// Edit Configurations -> VM options -> "/Users/rise/Documents/javafx-sdk-11.0.2/lib" библиотека для мака

import classes.Category;
import classes.Todo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Time;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private TextField fieldForTodo;

    @FXML
    private VBox vboxTodo;

    @FXML
    private ChoiceBox choiceBox;

    @FXML
    private TextField categoryEntry;

    @FXML
    private Label labelForInfo;

    private Database conn = new Database(); //объект калсса базы данных
    private List<VBox> vboxes = new ArrayList<>(); //список "вбоксов" нужен для проверки на совпадения, также работы с "тудушками" и категориям
    private List<Category> categories = new ArrayList<>(); //список категорий
    private List<Todo> todos = new ArrayList<>(); //список дел


    @FXML
    //Если происходит нажатие кнопки "добавить", то вызывается функция,
    //в которой происходит добавление "дела" в базу данных, списки и т.д.
    private void onButtonClick() {
        String todo = fieldForTodo.getText();
        String category = choiceBox.getValue().toString();
        Create(category, todo);
        fieldForTodo.clear();
    }


    @FXML
    //Тоже самое, что и onButtonClick, только на Enter
    private void onPressEnter(){
        fieldForTodo.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER){
                String todo = fieldForTodo.getText();
                String category = choiceBox.getValue().toString();
                Create(category, todo);
                fieldForTodo.clear();
            }
        });
    }


    @FXML
    //Если удаляется категория, то удаляются и все "дела", которые к ней относятся,
    //но в БД не реалезованно (мной) каскадное удаление всех дел, принадлежащих этой категории
    private void onDeleteCategory(){
        //берем значении текстового поля
        String ctg = choiceBox.getValue().toString();
        int id = 0;

        choiceBox.getItems().remove(ctg); //удаляем в choicebox
        choiceBox.setValue(choiceBox.getItems().get(0).toString());
        for (Category category : categories){
            if (category.getName().equals(ctg)){
                id = category.getID();
                categories.remove(category);
                break;
            }
        }

        for (VBox vbox : vboxes){
            if (vbox.getChildren().get(0).toString().contains(ctg)){
                todos.removeIf(todo -> vbox.getId().equals(Integer.toString(todo.getCategory_id())));
                vboxTodo.getChildren().remove(vbox);
                vboxes.remove(vbox);
                break;
            }
        }

        conn.execute("DELETE FROM todos WHERE category_id = " + id);
        conn.execute("DELETE FROM categories WHERE name = \"" + ctg + "\"");
    }


    @FXML
    //создается категория, при нажатии на enter
    //если человек ничего не ввел или такая категория уже существует, то ничего не далаем
    private void createCategory(){
        categoryEntry.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                String category = categoryEntry.getText();

                categoryEntry.setVisible(false);
                categoryEntry.clear();
                if (category.trim().equals("")) {
                    labelForInfo.setText("Вы ничего не ввели");
                    return;
                }

                for (Category categoryObject : categories) {
                    if (categoryObject.getName().equals(category)) {
                        labelForInfo.setText("Уже существует");
                        return;
                    }
                }

                try{
                    conn.execute("INSERT INTO categories (name) VALUES ('" + category + "')");
                    int ctg = Integer.parseInt(conn.getInfo("SELECT id FROM categories WHERE name = \"" + category + "\""));
                    categories.add(new Category(ctg, category));
                    choiceBox.getItems().add(choiceBox.getItems().size() - 2, category);
                } catch (Exception error){
                    labelForInfo.setText("Что-то пошло не так");
                    error.printStackTrace();
                    return;
                }
            }
        });
    }


    private void Create(String category, String todo) {
        labelForInfo.setText("");
        categoryEntry.setVisible(false);

        if (todo.trim().equals("")) {
            labelForInfo.setText("Вы ничего не ввели");
            return;
        }

        for (Todo td : todos){
            if (td.getName().equals(todo)){
                labelForInfo.setText("Уже существует");
                return;
            }
        }

        int category_id = Integer.parseInt(conn.getInfo("SELECT id FROM categories WHERE name = \"" + category + "\""));

        for (VBox vbox : vboxes){
            if(vbox.getChildren().get(0).toString().contains(category)){
                CheckBox checkBox = new CheckBox(todo);
                checkBox.setOnAction(actionEvent -> onCheckBox(checkBox));

                checkBox.setId(Integer.toString(category_id));
                checkBox.setPadding(new Insets(0, 5, 0, 0));
                vbox.getChildren().addAll(checkBox);

                conn.execute("INSERT INTO todos (name, category_id) VALUES (\"" + todo + "\", " + category_id + ")");
                int id = Integer.parseInt(conn.getInfo("SELECT id FROM todos WHERE name = \"" + todo + "\""));

                todos.add(new Todo(id, todo, category_id));


                return;
            }
        }

        VBox vbox = new VBox(new Label(category));
        vbox.setId(Integer.toString(category_id));
        vbox.setSpacing(5);
        vboxes.add(vbox);

        CheckBox checkBox = new CheckBox(todo);
        checkBox.setOnAction(actionEvent -> onCheckBox(checkBox));

        checkBox.setId(Integer.toString(category_id));
        checkBox.setLineSpacing(5);
        vbox.getChildren().addAll(checkBox);
        vboxTodo.getChildren().addAll(vbox);

        conn.execute("INSERT INTO todos (name, category_id) VALUES (\"" + todo + "\", " + category_id+ ")");
        int id = Integer.parseInt(conn.getInfo("SELECT id FROM todos WHERE name = \"" + todo + "\""));

        todos.add(new Todo(id, todo, category_id));
    }


    private void onCheckBox(CheckBox checkBox){
        for (VBox vbox : vboxes){

            if (vbox.getChildren().toString().contains(checkBox.getText())){
                vbox.getChildren().remove(checkBox);
                conn.execute("DELETE FROM todos WHERE name = \"" + checkBox.getText() + "\"");

                if (vbox.getChildren().size() == 1){
                    vboxes.remove(vbox);
                    vboxTodo.getChildren().remove(vbox);
                }
            }
        }
    }


    @Override
    public void initialize(URL locale, ResourceBundle resourceBundle) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //берём все категории
        try {
            categories = conn.getCategories("SELECT * FROM categories");
        } catch (Exception error) {
            error.printStackTrace();
        }

        //берем все списки дел
        try {
            todos = conn.getTodos("SELECT * FROM todos ORDER BY category_id");
        } catch (Exception error) {
            error.printStackTrace();
        }

        //проходимся по всем категориям
        for (Category category : categories) {
            choiceBox.getItems().addAll(category.getName());
            VBox vbox = new VBox(new Label(category.getName()));

            for (Todo todo : todos){

                if (todo.getCategory_id() == category.getID()){
                    CheckBox checkBox = new CheckBox(todo.getName());

                    checkBox.setId(Integer.toString(todo.getCategory_id()));
                    checkBox.setOnAction(actionEvent -> onCheckBox(checkBox));
                    checkBox.setPadding(new Insets(0, 5, 0, 0)); //не работает, надо удалить

                    vbox.setId(Integer.toString(todo.getCategory_id()));
                    vbox.getChildren().add(checkBox);
                }
            }

            if (vbox.getChildren().size() != 1){
                vbox.setSpacing(5);
                vboxes.add(vbox);
                vboxTodo.getChildren().add(vbox);
                vboxTodo.setSpacing(5);
            }
        }

        choiceBox.getItems().addAll(FXCollections.observableArrayList(new Separator(), "Добавить"));
        choiceBox.setValue(choiceBox.getItems().get(0));

        choiceBox.setOnAction(actionEvent -> {
            if (choiceBox.getValue().equals("Добавить")){
                categoryEntry.setVisible(true);
                choiceBox.setValue(choiceBox.getItems().get(0));
            }
        });
    }
}
