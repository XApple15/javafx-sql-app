package javadb_project.demo2;

import javafx.beans.property.*;
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

class Columns {
    private StringProperty id;
    private StringProperty username;
    private StringProperty password;
    private StringProperty cnp;
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty address;
    private StringProperty phoneNumber;
    private StringProperty email;
    private StringProperty iban;
    private StringProperty contractNumber;
    private StringProperty dateOfEnrollment;
    private StringProperty position;
    private StringProperty userType;


    private StringProperty timetable_monday;
    private StringProperty timetable_tuesday;
    private StringProperty timetable_wednesday;
    private StringProperty timetable_thursday;
    private StringProperty timetable_friday;
    private StringProperty timetable_saturday;
    private StringProperty timetable_sunday;

    public Columns(String day) {

    }

    public Columns(String id, String username, String password, String cnp, String firstName, String lastName, String address, String phoneNumber, String email, String iban, String contractNumber, String dateOfEnrollment, String position, String userType) {
        this.id = new SimpleStringProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.cnp = new SimpleStringProperty(cnp);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.address = new SimpleStringProperty(address);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
        this.iban = new SimpleStringProperty(iban);
        this.contractNumber = new SimpleStringProperty(contractNumber);
        this.dateOfEnrollment = new SimpleStringProperty(dateOfEnrollment);
        this.position = new SimpleStringProperty(position);
        this.userType = new SimpleStringProperty(userType);
    }

    public StringProperty idProperty() {
        return id;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty cnpProperty() {
        return cnp;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty ibanProperty() {
        return iban;
    }

    public StringProperty contractNumberProperty() {
        return contractNumber;
    }

    public StringProperty dateOfEnrollmentProperty() {
        return dateOfEnrollment;
    }

    public StringProperty positionProperty() {
        return position;
    }

    public StringProperty userTypeProperty() {
        return userType;
    }

}

public class LoggedInControllerSuperAdmin implements Initializable {
    private Integer id;
    private String username;
    private String password;

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
    private Label label_loggedin_as;
    @FXML
    private Button button_update_data;
    @FXML
    private Button button_newuser;
    @FXML
    private Button button_logout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialiseTextFields();
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
                    try {
                        DBUtils.updateUserInfo(modify0.getText(), modify1.getText(), modify2.getText(), modify3.getText(), modify4.getText(),
                                modify5.getText(), modify6.getText(), modify7.getText(), modify8.getText(), modify9.getText(),
                                modify10.getText(), modify11.getText(), modify12.getText(), modify13.getText());
                        DBUtils.changeScene(actionEvent, "logged_in_Super-Admin.fxml", id, username, password);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        button_newuser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!checkIfEmplyTxtFields()) {
                    try {
                        DBUtils.createNewUser(modify1.getText(), modify2.getText(), modify3.getText(), modify4.getText(),
                                modify5.getText(), modify6.getText(), modify7.getText(), modify8.getText(), modify9.getText(),
                                modify10.getText(), modify11.getText(), modify12.getText(), modify13.getText());
                        DBUtils.changeScene(actionEvent, "logged_in_Super-Admin.fxml", id, username, password);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        try {
            showAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableview_table_show_users.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleKeyPressed();
            }
        });
    }

    private void initialiseTextFields() {
        modify1.setText(null);
        modify2.setText(null);
        modify3.setText(null);
        modify4.setText(null);
        modify5.setText(null);
        modify6.setText(null);
        modify7.setText(null);
        modify8.setText(null);
        modify9.setText(null);
        modify10.setText(null);
        modify11.setText(null);
        modify12.setText(null);
        modify13.setText(null);
    }

    private boolean checkIfEmplyTxtFields() {
        if (modify1.getText() == null || modify2.getText() == null || modify3.getText() == null ||
                modify4.getText() == null || modify5.getText() == null || modify6.getText() == null ||
                modify7.getText() == null || modify8.getText() == null || modify9.getText() == null ||
                modify10.getText() == null || modify11.getText() == null || modify12.getText() == null ||
                modify13.getText() == null) {
            DBUtils.printError(Error.ERROR1_uncompleted_text_fields);
            return true;
        }
        return false;
    }

    private void showAllUsers() throws SQLException {

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

        Connection connection = null;
        try {
            connection = DBUtils.createConnection();
            String query = "select id,username,parola,cnp,nume,prenume,adresa,telefon,email,ContIBAN,nrContract,dataAngajarii,Functie,TipUtilizator from utilizatori ";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                List<Columns> itemList = new ArrayList<>();
                while (resultSet.next()) {
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
                    String col13 = resultSet.getString("TipUtilizator");

                    itemList.add(new Columns(col0, col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13));
                }
                tableview_table_show_users.getItems().addAll(itemList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        }
    }

    public void setUserInformation(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        label_loggedin_as.setText("Logged in as " + this.username);
    }
}
