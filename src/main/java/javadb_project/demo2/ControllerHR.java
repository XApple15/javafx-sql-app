package javadb_project.demo2;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import java.util.Objects;
import java.util.ResourceBundle;


public class ControllerHR implements Initializable {
    private int id;
    private String username;
    private String position;


    @FXML
    private TableView tableview_table_show_users;
    @FXML
    private TableColumn<Columns, String> col0;
    @FXML
    private TableColumn<Columns, String> col1;
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
    private TextField txtfield_firstname;
    @FXML
    private TextField txtfield_lastname;
    @FXML
    private TextField txtfield_position;
    @FXML
    private Label label_firstname;
    @FXML
    private Label label_lastname;
    @FXML
    private Label label_position;
    @FXML
    private Button button_search;

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initializewithData(Integer id, String username, String position) {
        this.id = id;
        this.username = username;
        this.position = position;

        if (position.equals("HR Specialist")) {

        } else {
            txtfield_firstname.setVisible(false);
            txtfield_lastname.setVisible(false);
            txtfield_position.setVisible(false);
            label_firstname.setVisible(false);
            label_lastname.setVisible(false);
            label_position.setVisible(false);
            button_search.setVisible(false);
            searchUser(position, null, null, null);
        }
        button_search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchUser(position, txtfield_firstname.getText(), txtfield_lastname.getText(), txtfield_position.getText());
            }
        });
    }

    private void searchUser(String callerPosition, String firstName, String lastName, String position) {
        col0.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col1.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        // col2.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
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
            PreparedStatement preparedStatement;
            String query = "select id,username,cnp,nume,prenume,adresa,telefon,email,ContIBAN,nrContract,dataAngajarii,Functie,TipUtilizator from utilizatori ";
            if (callerPosition.equals("HR Specialist")) {
                query += "where nume = ? and prenume=? and  functie =? ";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, position);

            } else {
                query += "where id=?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, String.valueOf(this.id));
            }
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Columns> itemList = new ArrayList<>();
            while (resultSet.next()) {
                String col0 = resultSet.getString("id");
                String col1 = resultSet.getString("username");
                String col2 = "null";
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


}
