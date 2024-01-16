package javadb_project.demo2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerAdmin implements Initializable {
    private Integer id;
    private String username;
    private String position;

    @FXML
    private TableView tableview_table_show_users;
    @FXML
    private TableColumn<Columns, String> col0;
    @FXML
    private TableColumn<Columns, String> col1;
    @FXML
    private TableColumn<Columns, String> col2;
    @FXML
    private TableColumn<Columns, String> col3;
    @FXML
    private TableColumn<Columns, String> col4;
    @FXML
    private TableColumn<Columns, String> col5;
    @FXML
    private TableColumn<Columns, String> col6;
    @FXML
    private TableColumn<Columns, String> col7;
    @FXML
    private TableColumn<Columns, String> col8;
    @FXML
    private TableColumn<Columns, String> col9;
    @FXML
    private TableColumn<Columns, String> col10;
    @FXML
    private TableColumn<Columns, String> col11;
    @FXML
    private TableColumn<Columns, String> col12;
    @FXML
    private TableColumn<Columns, String> col13;
    @FXML
    private TableColumn<Columns, String> col14;
    @FXML
    private TextField modify0;
    @FXML
    private TextField modify1;
    @FXML
    private TextField modify2;
    @FXML
    private TextField modify3;
    @FXML
    private TextField modify4;
    @FXML
    private TextField modify5;
    @FXML
    private TextField modify6;
    @FXML
    private TextField modify7;
    @FXML
    private TextField modify8;
    @FXML
    private TextField modify9;
    @FXML
    private TextField modify10;
    @FXML
    private TextField modify11;
    @FXML
    private TextField modify12;
    @FXML
    private TextField modify13;
    @FXML
    private TextField modify14;
    @FXML
    private Label label_loggedin_as;
    @FXML
    private Button button_update_data;
    @FXML
    private Button button_newuser;
    @FXML
    private Button button_logout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "login.fxml", null, null, null);
            }
        });
        button_update_data.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!checkIfEmplyTxtFields()) {
                    if (modify13.getText().equals("Super-Administrator") && position.equals("Administrator"))
                        DBUtils.printError(Error.ERROR2_no_right_to_assign_this_usertype);
                    else {
                        DBUtils.updateUserInfo(modify0.getText(), modify1.getText(), modify2.getText(), modify3.getText(), modify4.getText(),
                                modify5.getText(), modify6.getText(), modify7.getText(), modify8.getText(), modify9.getText(),
                                modify10.getText(), modify11.getText(), modify12.getText(), modify13.getText(), modify14.getText());
                        DBUtils.changeScene(actionEvent, "logged_in_Admin.fxml", id, username, position);
                    }
                }
            }
        });

        button_newuser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!checkIfEmplyTxtFields()) {
                    if (modify13.getText().equals("Super-Administrator") && position.equals("Admnistrator"))
                        DBUtils.printError(Error.ERROR2_no_right_to_assign_this_usertype);
                    else {
                        DBUtils.createNewUser(modify1.getText(), modify2.getText(), modify3.getText(), modify4.getText(),
                                modify5.getText(), modify6.getText(), modify7.getText(), modify8.getText(), modify9.getText(),
                                modify10.getText(), modify11.getText(), modify12.getText(), modify13.getText(), modify14.getText());
                        DBUtils.changeScene(actionEvent, "logged_in_Admin.fxml", id, username, position);
                    }
                }
            }
        });

        tableview_table_show_users.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleKeyPressed();
            }
        });
    }

    public void initializeWithData(Integer id, String username, String position) {
        this.position = position;
        this.id = id;
        this.username = username;

        showAllUsers();
    }

    private boolean checkIfEmplyTxtFields() {
        if (modify1.getText().isEmpty() || modify2.getText().isEmpty() || modify3.getText().isEmpty() ||
                modify4.getText().isEmpty() || modify5.getText().isEmpty() || modify6.getText().isEmpty() ||
                modify7.getText().isEmpty() || modify8.getText().isEmpty() || modify9.getText().isEmpty() ||
                modify10.getText().isEmpty() || modify11.getText().isEmpty() || modify12.getText().isEmpty() ||
                modify13.getText().isEmpty() || modify14.getText().isEmpty()) {
            DBUtils.printError(Error.ERROR1_uncompleted_text_fields);
            return true;
        }
        return false;
    }

    private void showAllUsers() {
        col0.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col1.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col2.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        col3.setCellValueFactory(cellData -> cellData.getValue().cnpProperty());
        col4.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        col5.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        col6.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        col7.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        col8.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        col9.setCellValueFactory(cellData -> cellData.getValue().ibanProperty());
        col10.setCellValueFactory(cellData -> cellData.getValue().contractNumberProperty());
        col11.setCellValueFactory(cellData -> cellData.getValue().dateOfEnrollmentProperty());
        col12.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        col13.setCellValueFactory(cellData -> cellData.getValue().userTypeProperty());
        col14.setCellValueFactory(cellData -> cellData.getValue().polyclinicIdProperty());

        try {
            Connection connection = DBUtils.createConnection();
            String query = "select id,username,parola,cnp,nume,prenume,adresa,telefon,email,ContIBAN,nrContract,dataAngajarii,Functie,TipUtilizator,PoliclinicaID from utilizatori ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Columns> itemList = new ArrayList<>();
            while (resultSet.next()) {
                String col13 = resultSet.getString("TipUtilizator");
                if (this.position.equals("Super Administrator") || (!col13.equals("Super-Administrator") && this.position.equals("Administrator"))) {
                    String col0 = resultSet.getString("id");
                    String col1 = resultSet.getString("username");
                    String col2 = resultSet.getString("parola");
                    String col3 = resultSet.getString("CNP");
                    String col4 = resultSet.getString("Nume");
                    String col5 = resultSet.getString("Prenume");
                    String col6 = resultSet.getString("Adresa");
                    String col7 = resultSet.getString("Telefon");
                    String col8 = resultSet.getString("Email");
                    String col9 = resultSet.getString("ContIBAN");
                    String col10 = resultSet.getString("NrContract");
                    String col11 = resultSet.getString("DataAngajarii");
                    String col12 = resultSet.getString("Functie");
                    String col14 = resultSet.getString("PoliclinicaID");
                    itemList.add(new Columns(col0, col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14));
                }
            }
            tableview_table_show_users.getItems().addAll(itemList);

            DBUtils.closeConnection(connection, preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleKeyPressed() {
        Columns selectedRow = (Columns) tableview_table_show_users.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            modify0.setText(selectedRow.idProperty().get());
            modify1.setText(selectedRow.usernameProperty().get());
            modify2.setText(selectedRow.passwordProperty().get());
            modify3.setText(selectedRow.cnpProperty().get());
            modify4.setText(selectedRow.firstNameProperty().get());
            modify5.setText(selectedRow.lastNameProperty().get());
            modify6.setText(selectedRow.addressProperty().get());
            modify7.setText(selectedRow.phoneNumberProperty().get());
            modify8.setText(selectedRow.emailProperty().get());
            modify9.setText(selectedRow.ibanProperty().get());
            modify10.setText(selectedRow.contractNumberProperty().get());
            modify11.setText(selectedRow.dateOfEnrollmentProperty().get());
            modify12.setText(selectedRow.positionProperty().get());
            modify13.setText(selectedRow.userTypeProperty().get());
            modify14.setText(selectedRow.polyclinicIdProperty().get());
        }
    }
}