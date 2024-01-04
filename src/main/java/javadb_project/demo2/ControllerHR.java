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


public class ControllerHR implements Initializable {
    private int id;
    private String username;
    private String position;


    @FXML
    private TableView tableview_table_show_searched_user_data;
    @FXML
    private TableColumn<Columns, String> col0searched;
    @FXML
    private TableColumn<Columns, String> col1searched;
    @FXML
    private TableColumn<Columns, String> col3searched;
    @FXML
    private TableColumn<Columns, String> col4searched;
    @FXML
    private TableColumn<Columns, String> col5searched;
    @FXML
    private TableColumn<Columns, String> col6searched;
    @FXML
    private TableColumn<Columns, String> col7searched;
    @FXML
    private TableColumn<Columns, String> col8searched;
    @FXML
    private TableColumn<Columns, String> col9searched;
    @FXML
    private TableColumn<Columns, String> col10searched;
    @FXML
    private TableColumn<Columns, String> col11searched;
    @FXML
    private TableColumn<Columns, String> col12searched;
    @FXML
    private TableColumn<Columns, String> col13searched;


    @FXML
    private TableView tableview_table_show_user_data;
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
    private TableView tableview_showusertimetable;
    @FXML
    private TableColumn<Columns, String> col1tmtbl;
    @FXML
    private TableColumn<Columns, String> col2tmtbl;
    @FXML
    private TableColumn<Columns, String> col3tmtbl;
    @FXML
    private TableColumn<Columns, String> col4tmtbl;
    @FXML
    private TableColumn<Columns, String> col5tmtbl;
    @FXML
    private TableColumn<Columns, String> col6tmtbl;
    @FXML
    private TableColumn<Columns, String> col7tmtbl;


    @FXML
    private TableView tableview_showsearchedtimetable;
    @FXML
    private TableColumn<Columns, String> col1tmtblsearched;
    @FXML
    private TableColumn<Columns, String> col2tmtblsearched;
    @FXML
    private TableColumn<Columns, String> col3tmtblsearched;
    @FXML
    private TableColumn<Columns, String> col4tmtblsearched;
    @FXML
    private TableColumn<Columns, String> col5tmtblsearched;
    @FXML
    private TableColumn<Columns, String> col6tmtblsearched;
    @FXML
    private TableColumn<Columns, String> col7tmtblsearched;

    @FXML
    private TableView tableview_showuservacantion;
    @FXML
    private TableColumn<Columns, String> col1vac;
    @FXML
    private TableColumn<Columns, String> col2vac;

    @FXML
    private TableView tableview_showsearchedvacantion;
    @FXML
    private TableColumn<Columns, String> col1vacsearched;
    @FXML
    private TableColumn<Columns, String> col2vacsearched;

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
    private Label label_searchuser;
    @FXML
    private Button button_search;
    @FXML
    private Button button_goback;

    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void initializewithData(Integer id, String username, String position) {
        this.id = id;
        this.username = username;
        this.position = position;
        searchUser("ghost", null, null, null);
        showTimeTable("ghost", null, null, null);
        showVacantion("ghost", null, null, null);
        if (position.equals("HR Specialist")) {

        } else {
            txtfield_firstname.setVisible(false);
            txtfield_lastname.setVisible(false);
            txtfield_position.setVisible(false);
            label_firstname.setVisible(false);
            label_lastname.setVisible(false);
            label_position.setVisible(false);
            button_search.setVisible(false);
            label_searchuser.setVisible(false);
            tableview_showsearchedtimetable.setVisible(false);
            tableview_table_show_searched_user_data.setVisible(false);
            tableview_showsearchedvacantion.setVisible(false);
        }

        button_search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                searchUser(position, txtfield_firstname.getText(), txtfield_lastname.getText(), txtfield_position.getText());
                showTimeTable(position, txtfield_firstname.getText(), txtfield_lastname.getText(), txtfield_position.getText());
                showVacantion(position, txtfield_firstname.getText(), txtfield_lastname.getText(), txtfield_position.getText());
            }
        });
        button_goback.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "logged_in.fxml", id, username, position);
            }
        });
    }

    private void searchUser(String callerPosition, String firstName, String lastName, String position) {
        col0.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col1.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
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

        col0searched.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col1searched.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col3searched.setCellValueFactory(cellData -> cellData.getValue().cnpProperty());
        col4searched.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        col5searched.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        col6searched.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        col7searched.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        col8searched.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        col9searched.setCellValueFactory(cellData -> cellData.getValue().ibanProperty());
        col10searched.setCellValueFactory(cellData -> cellData.getValue().contractNumberProperty());
        col11searched.setCellValueFactory(cellData -> cellData.getValue().dateOfEnrollmentProperty());
        col12searched.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        col13searched.setCellValueFactory(cellData -> cellData.getValue().userTypeProperty());


        if (callerPosition.equals("HR Specialist")) {
            tableview_table_show_searched_user_data.getItems().clear();
            tableview_showsearchedtimetable.getItems().clear();
        }
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

                itemList.add(new Columns(col0, col1, null, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13));
            }
            if (callerPosition.equals("HR Specialist"))
                tableview_table_show_searched_user_data.getItems().addAll(itemList);
            else tableview_table_show_user_data.getItems().addAll(itemList);
            DBUtils.closeConnection(connection, preparedStatement, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        } /*finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    }

    private void showTimeTable(String callerPosition, String firstName, String lastName, String position) {
        Integer fetchedId = DBUtils.searchforID(firstName, lastName, position);
        Integer usedId;

        col1tmtbl.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col2tmtbl.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col3tmtbl.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        col4tmtbl.setCellValueFactory(cellData -> cellData.getValue().cnpProperty());
        col5tmtbl.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        col6tmtbl.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        col7tmtbl.setCellValueFactory(cellData -> cellData.getValue().contractNumberProperty());

        col1tmtblsearched.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col2tmtblsearched.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col3tmtblsearched.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        col4tmtblsearched.setCellValueFactory(cellData -> cellData.getValue().cnpProperty());
        col5tmtblsearched.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        col6tmtblsearched.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        col7tmtblsearched.setCellValueFactory(cellData -> cellData.getValue().contractNumberProperty());


        if (fetchedId != 0) usedId = fetchedId; // get the id to search from the person
        else usedId = this.id;
        String startTime;
        String prevStartTime = "null";
        String[] textForEachDay = new String[8];
        for (int i = 1; i <= 7; i++) textForEachDay[i] = "";

        try { // print the timetable
            Connection connection = DBUtils.createConnection();
            String query = " select ZiuaSaptamanii,OraInceput,OraSfarsit,Locatie from orarangajati where AngajatID=? ORDER BY  OraInceput ASC ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, usedId.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Columns> itemList = new ArrayList<>();

            while (resultSet.next()) {
                String dayOfWeek = resultSet.getString("ZiuaSaptamanii");
                startTime = resultSet.getString("OraInceput");
                if (prevStartTime.equals("null")) prevStartTime = startTime;
                if (!startTime.equals(prevStartTime)) {
                    {
                        itemList.add(new Columns(textForEachDay[1], textForEachDay[2], textForEachDay[3], textForEachDay[4], textForEachDay[5], textForEachDay[6], textForEachDay[7]));
                        for (int i = 1; i <= 7; i++) textForEachDay[i] = "";
                        prevStartTime = startTime;
                    }
                }
                String toPrint = resultSet.getString("OraInceput") + resultSet.getString("OraSfarsit")
                        + resultSet.getString("Locatie");
                switch (dayOfWeek) {
                    case "Monday" -> textForEachDay[1] = toPrint;
                    case "Tuesday" -> textForEachDay[2] = toPrint;
                    case "Wednesday" -> textForEachDay[3] = toPrint;
                    case "Thursday" -> textForEachDay[4] = toPrint;
                    case "Friday" -> textForEachDay[5] = toPrint;
                    case "Saturday" -> textForEachDay[6] = toPrint;
                    case "Sunday" -> textForEachDay[7] = toPrint;
                }
            }
            itemList.add(new Columns(textForEachDay[1], textForEachDay[2], textForEachDay[3], textForEachDay[4], textForEachDay[5], textForEachDay[6], textForEachDay[7]));
            if (callerPosition.equals("HR Specialist"))
                tableview_showsearchedtimetable.getItems().addAll(itemList);
            else tableview_showusertimetable.getItems().addAll(itemList);
            DBUtils.closeConnection(connection, preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showVacantion(String callerPosition, String firstName, String lastName, String position) {
        col1vac.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col2vac.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        col1vacsearched.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col2vacsearched.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        if (callerPosition.equals("HR Specialist")) {
            tableview_table_show_searched_user_data.getItems().clear();
            tableview_showsearchedtimetable.getItems().clear();
            tableview_showsearchedvacantion.getItems().clear();
        }
        Connection connection = null;
        try {
            connection = DBUtils.createConnection();
            PreparedStatement preparedStatement;
            String query = "select DataInceput,DataSfarsit from concediiangajati ";
            query += "where AngajatID=?";
            preparedStatement = connection.prepareStatement(query);
            if (callerPosition.equals("HR Specialist")) {
                preparedStatement.setString(1, DBUtils.searchforID(firstName, lastName, position).toString());
            } else {
                preparedStatement.setString(1, String.valueOf(this.id));
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Columns> itemList = new ArrayList<>();

            while (resultSet.next()) {
                String col0 = resultSet.getString("DataInceput");
                String col1 = resultSet.getString("DataSfarsit");
                itemList.add(new Columns(col0, col1, null, null, null, null, null, null, null, null, null, null, null, null));
            }
            if (callerPosition.equals("HR Specialist"))
                tableview_showsearchedvacantion.getItems().addAll(itemList);
            else tableview_showuservacantion.getItems().addAll(itemList);
            DBUtils.closeConnection(connection, preparedStatement, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        } /*finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    }
}