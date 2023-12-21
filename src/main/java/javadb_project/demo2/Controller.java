package javadb_project.demo2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button button_login;
    @FXML
    private TextField field_username;
    @FXML
    private TextField field_password;
    @FXML
    private Button button_sign_up;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (field_password.getText().isEmpty() == true || field_username.getText().isEmpty() == true)
                    DBUtils.printError(Error.ERROR1_uncompleted_text_fields);
                else {
                    DBUtils.logInUser(actionEvent, field_username.getText(), field_password.getText());
                }
            }
        });
        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "sign_up.fxml", null, null, null);
            }
        });
    }
}
