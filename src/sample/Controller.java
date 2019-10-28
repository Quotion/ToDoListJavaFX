package sample;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private TextField fieldForTodo;

    @FXML
    private void onButtonClick(){
        System.out.println(fieldForTodo.getText());
    }
}
