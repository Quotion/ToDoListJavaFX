package sample;

import javafx.fxml.FXML;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.text.LabelView;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private TextField fieldForTodo;

    @FXML
    private VBox vbox;

    @FXML
    private void onButtonClick(){
        String todo = fieldForTodo.getText();
        Create(todo);
    }

    @FXML
    private void Create(String todo){
        CheckBox checkBox = new CheckBox(todo);
        checkBox.setId(Integer.toString(vbox.getChildren().size() + 1));
        checkBox.setLineSpacing(1);
        checkBox.setOnAction(actionEvent -> {
            onCheckBox(checkBox);
        });
        vbox.getChildren().addAll(checkBox);
        vbox.setSpacing(5);
        System.out.println(vbox.getChildren());
    }

    @FXML
    private void onCheckBox(CheckBox checkBox){
        System.out.println(checkBox);
    }
}
