package javadb_project.demo2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {
    private Integer id;
    private String username;
    private String position;
    @FXML
    private Label label_loggedinas;
    @FXML
    private Button button_logout;
    @FXML
    private Button button_gotoHR;
    @FXML
    private Button button_gotoMoney;
    @FXML
    private Button button_gotoOP;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initialisewithData(Integer id, String username, String position) {
        this.id = id;
        this.username = username;
        this.position = position;

        label_loggedinas.setText("Logged in as : " + username + " with position : " + position);

        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "login.fxml", null, null, null);
            }
        });
        button_gotoHR.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "logged_in_HR.fxml", id, username, position);
            }
        });
        button_gotoMoney.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "logged_in_Financiar.fxml", id, username, position);
                System.out.println("da");
            }
        });

        button_gotoOP.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "logged_in_operations.fxml", id, username, position);
            }
        });


    }


}
