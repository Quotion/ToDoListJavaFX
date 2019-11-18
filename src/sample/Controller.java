package sample;

// Edit Configurations -> VM options -> "/Users/rise/Documents/javafx-sdk-11.0.2/lib" библиотека для мака

import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private Database conn = new Database();
    private List<VBox> vboxes = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Todo> todos = new ArrayList<>();

    @FXML
    private void onButtonClick() {
        String todo = fieldForTodo.getText();
        String category = choiceBox.getValue().toString();
        Create(category, todo);
    }

    @FXML
    private void onPressEnter(){
        fieldForTodo.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER){
                String todo = fieldForTodo.getText();
                String category = choiceBox.getValue().toString();
                Create(category, todo);
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

        for (Node node : vboxTodo.getChildren()) {
            VBox vbox = vboxes.get(Integer.parseInt(node.getId()) - 1);
            Node nd = vbox.getChildren().get(0);
            if (nd.getId().equals(category)) {
                createTodo(category, todo, vbox);
                return;
            }
        }

        Label label = new Label(category);
        label.setId(category);

        VBox vboxCategory = new VBox(label);
        vboxCategory.setId(Integer.toString(vboxTodo.getChildren().size() + 1));
        vboxCategory.setSpacing(5);
        vboxes.add(vboxCategory);

        CheckBox checkBox = new CheckBox(todo);
        checkBox.setOnAction(actionEvent -> {
            onCheckBox(checkBox);
        });
        checkBox.setId(category);

        vboxCategory.getChildren().addAll(checkBox);

        vboxTodo.getChildren().addAll(vboxCategory);
        vboxTodo.setSpacing(5);

    }

    private void createTodo(String category, String todo, VBox vbox){

        CheckBox checkBox = new CheckBox(todo);

        checkBox.setId(category);
        checkBox.setOnAction(actionEvent -> {
            onCheckBox(checkBox);
        });

        for (Node node : vbox.getChildren()){
            if (node.toString().equals(checkBox.toString())) {
                labelForInfo.setText("Уже существует");
                return;
            }
        }

        vbox.getChildren().addAll(checkBox);
        vbox.setSpacing(6);
    }

    @FXML
    private void onCheckBox(CheckBox checkBox){
        for (VBox vbox : vboxes){
            Node nd = vbox.getChildren().get(0);
            if (nd.getId().equals(checkBox.getId())) {
                vbox.getChildren().removeAll(checkBox);
                if (vbox.getChildren().size() == 1) {
                    vboxes.remove(vbox);
                    vboxTodo.getChildren().removeAll(vbox);
                }
                return;
            }
        }
    }

    @FXML
    private void createCategory(){
        categoryEntry.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                Object ctg = null;
                String category = categoryEntry.getText();
                categoryEntry.setVisible(false);
                if (category.trim().equals("")) {
                    return;
                }
                if (categories.contains(category)){
                    labelForInfo.setText("Уже существует");
                    categoryEntry.clear();
                    return;
                }

                try{
                    conn.execute("INSERT INTO categories (name) VALUES ('" + category + "')");
                    ctg = conn.getCategories("SELECT id FROM categories WHERE name = " + category);
                } catch (Exception error){
                    System.out.println(error);
                }

                categories.add(new Category(Integer.parseInt(ctg.toString()), category));
                choiceBox.getItems().add(choiceBox.getItems().size() - 2, category);
                categoryEntry.clear();
            }
        });
    }


    @Override
    public void initialize(URL locale, ResourceBundle resourceBundle) {
        try {
            categories = conn.getCategories("SELECT * FROM categories");
        } catch (Exception error) {
            System.out.println(error);
        }

        try {
            todos = conn.getTodos("SELECT * FROM todos ORDER BY category_id");
        } catch (Exception error) {
            System.out.println(error);
        }

        for (Category category : categories) {
            choiceBox.getItems().add(category.getName());
            VBox vbox = new VBox(new Label(category.getName()));
            for (Todo todo : todos){
                if (todo.getCategory_id() == category.getID()){
                    CheckBox checkBox = new CheckBox(todo.getName());
                    checkBox.setId(Integer.toString(todo.getCategory_id()));
                    vbox.getChildren().add(checkBox);
                }
            }

            if (vbox.getChildren().size() != 0){
                vbox.setSpacing(5);
                vboxes.add(vbox);
                vboxTodo.getChildren().add(vbox);
            }
        }


        choiceBox.getItems().add(FXCollections.observableArrayList(new Separator(), "Добавить"));
        choiceBox.setValue(categories.get(0).getName());
        choiceBox.setOnAction(actionEvent -> {
            if (choiceBox.getValue().equals("Добавить")){
                categoryEntry.setVisible(true);
                choiceBox.setValue(categories.get(0).getName());
            }
        });
    }
}
