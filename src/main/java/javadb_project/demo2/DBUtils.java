package javadb_project.demo2;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.*;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

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

    public Columns(String Monday, String Tuesday, String Wednesday, String Thursday, String Friday, String Saturday, String Sunday) {
        this.id = new SimpleStringProperty(Monday);
        this.username = new SimpleStringProperty(Tuesday);
        this.password = new SimpleStringProperty(Wednesday);
        this.cnp = new SimpleStringProperty(Thursday);
        this.address = new SimpleStringProperty(Friday);
        this.email = new SimpleStringProperty(Saturday);
        this.contractNumber = new SimpleStringProperty(Sunday);
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

enum Error {
    ERROR1_uncompleted_text_fields,
    ERROR2_no_right_to_assign_this_usertype,
    ERROR3_username_already_exists,
    ERROR4_login_credentials_notmatched

}

public class DBUtils {
    private final static String DBlogin_address = "jdbc:mysql://localhost:3306/policlinici";
    private final static String DB_login_username = "root";
    private final static String DB_login_password = "root";

    // no username or password typed
    public static void printError(Error errors) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (errors) {
            case ERROR1_uncompleted_text_fields -> alert.setContentText("Uncompleted text fields");
            case ERROR2_no_right_to_assign_this_usertype ->
                    alert.setContentText("You don`t have the right to assign this UserType");
            case ERROR3_username_already_exists -> alert.setContentText("This username already exists");
            case ERROR4_login_credentials_notmatched -> alert.setContentText("No matching credentials");
        }
        alert.show();
    }

    public static Connection createConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DBlogin_address, DB_login_username, DB_login_password);
        return connection;
    }

    public static void changeScene(ActionEvent event, String fxmlFile, Integer id, String username, String position) {
        Parent root = null;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//centerStage(stage);
        if (username != null && position != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                if (fxmlFile.equals("logged_in.fxml")) {
                    //   centerStage(stage);
                    stage.setTitle("Logged IN");
                    LoggedInController loggedInController = loader.getController();
                    loggedInController.initialisewithData(id, username, position);
                }
                /*else if (fxmlFile.equals("logged_in_Super-Admin.fxml")) {
                    stage.setTitle("Super-Admin");
                    LoggedInControllerSuperAdmin loggedInControllerSuperAdmin = loader.getController();
                    loggedInControllerSuperAdmin.setUserInformation(id, username, position);
                }*/
                else if (fxmlFile.equals("logged_in_Admin.fxml")) {
                    stage.setX(0);
                    stage.setY(0);
                    stage.setTitle("Admin");
                    LoggedInControllerAdmin loggedInControllerAdmin = loader.getController();
                    loggedInControllerAdmin.initializeWithData(id,username,position);
                } else if (fxmlFile.equals("logged_in_Financiar.fxml")) {
                    stage.setX(0);
                    stage.setY(0);
                    stage.setY(0);
                    ControllerFinanciar loggedinFinance = loader.getController();
                    loggedinFinance.initializewithData(id,username,position);
                } else if (fxmlFile.equals("logged_in_HR.fxml")) {
                    stage.setTitle("HR");
                    stage.setX(0);
                    stage.setY(0);
                    ControllerHR loggedinHR = loader.getController();
                    loggedinHR.initializewithData(id, username, position);
                } else if (fxmlFile.equals("logged_in_Medical.fxml")) {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                //   centerStage(stage);
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stage.setScene(new Scene(root));
        if (fxmlFile.equals("logged_in.fxml") || fxmlFile.equals("login.fxml")) centerStage(stage);
        stage.show();
    }

    private static void centerStage(Stage stage) {
        // Get the primary screen
        Screen screen = Screen.getPrimary();

        // Get the bounds of the screen
        Rectangle2D bounds = screen.getVisualBounds();

        // Calculate the center coordinates
        double centerX = bounds.getMinX() + (bounds.getWidth() - stage.getWidth()) / 2.0;
        double centerY = bounds.getMinY() + (bounds.getHeight() - stage.getHeight()) / 2.0;

        // Set the stage coordinates
        stage.setX(centerX);
        stage.setY(centerY);
    }

    public static void signUpUser(ActionEvent event, String username, String password) throws SQLException {
        Connection connection = null;
        PreparedStatement queryStatement = null;
        ResultSet result = null;
        try {
            connection = DriverManager.getConnection(DBlogin_address, DB_login_username, DB_login_password);
            queryStatement = connection.prepareStatement("select *  from utilizatori where Username=?");
            queryStatement.setString(1, username);
            result = queryStatement.executeQuery();
            if (result.isBeforeFirst()) {
                printError(Error.ERROR3_username_already_exists);
            } else {
                queryStatement = connection.prepareStatement("INSERT INTO utilizatori ( Username, Parola,TipUtilizator) values  (?,?,0)");
                queryStatement.setString(1, username);
                queryStatement.setString(2, password);
                queryStatement.executeUpdate();
                changeScene(event, "logged_in.fxml", null, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, queryStatement, result);
        }
    }

    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement queryStatement = null;
        ResultSet result = null;

        try {
            connection = DriverManager.getConnection(DBlogin_address, DB_login_username, DB_login_password);
            queryStatement = connection.prepareStatement("SELECT id , Parola,functie from utilizatori where Username=?");
            queryStatement.setString(1, username);
            result = queryStatement.executeQuery();
            if (result.isBeforeFirst()) {
                if (result.next()) {
                    String retrievePW = result.getString("parola");
                    if (retrievePW.equals(password)) {
                        Integer accID = result.getInt("id");
                        String position = result.getString("functie");
                        if (position.equals("Super Administrator") || position.equals("Administrator")) {
                            DBUtils.changeScene(event, "logged_in_Admin.fxml", accID, username, position);
                        } else {
                            DBUtils.changeScene(event, "logged_in.fxml", accID, username, position);
                        }
                    } else {
                        printError(Error.ERROR4_login_credentials_notmatched);
                    }
                }
            } else {
                printError(Error.ERROR4_login_credentials_notmatched);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, queryStatement, result);
        }
    }

    public static void updateUserInfo(String id, String username, String password, String cnp, String firstName, String lastName,
                                      String address, String phoneNumber, String email, String iban, String contractNumber,
                                      String dateOfEnrollment, String position, String userType) throws SQLException {
        Connection connection = null;
        try {
            connection = DBUtils.createConnection();

            String query = "UPDATE utilizatori SET username=?, parola=?, CNP=?, Nume=?, Prenume=?, Adresa=?, " +
                    "Telefon=?, Email=?, ContIBAN=?, NrContract=?, DataAngajarii=?, Functie=?, TipUtilizator=? WHERE id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, cnp);
                preparedStatement.setString(4, firstName);
                preparedStatement.setString(5, lastName);
                preparedStatement.setString(6, address);
                preparedStatement.setString(7, phoneNumber);
                preparedStatement.setString(8, email);
                preparedStatement.setString(9, iban);
                preparedStatement.setString(10, contractNumber);
                preparedStatement.setString(11, dateOfEnrollment);
                preparedStatement.setString(12, position);
                preparedStatement.setString(13, userType);
                preparedStatement.setString(14, id);

                preparedStatement.executeUpdate();
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void createNewUser(String username, String password, String cnp, String firstName, String lastName,
                                     String address, String phoneNumber, String email, String iban, String contractNumber,
                                     String dateOfEnrollment, String position, String userType) throws SQLException {
        Connection connection = null;
        try {
            connection = DBUtils.createConnection();

            String query = "INSERT INTO Utilizatori (Username, Parola, CNP, Nume, Prenume, Adresa, Telefon, Email, ContIBAN, NrContract, DataAngajarii, Functie, TipUtilizator)" +
                    "VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, cnp);
                preparedStatement.setString(4, firstName);
                preparedStatement.setString(5, lastName);
                preparedStatement.setString(6, address);
                preparedStatement.setString(7, phoneNumber);
                preparedStatement.setString(8, email);
                preparedStatement.setString(9, iban);
                preparedStatement.setString(10, contractNumber);
                preparedStatement.setString(11, dateOfEnrollment);
                preparedStatement.setString(12, position);
                preparedStatement.setString(13, userType);
                preparedStatement.executeUpdate();
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void closeConnection(Connection connection, PreparedStatement queryStatement, ResultSet result) {
        if (result != null) {
            try {
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (queryStatement != null) {
            try {
                queryStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Integer searchforID(String firstname, String lastname, String position) {
        int fetchedId = 0;
        try {
            Connection connection = DBUtils.createConnection();
            String query = " select id from utilizatori where nume=? and prenume=? and functie=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, firstname);
            preparedStatement.setString(2, lastname);
            preparedStatement.setString(3, position);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                fetchedId = resultSet.getInt("id");
            DBUtils.closeConnection(connection, preparedStatement, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fetchedId;
    }

    public static int getMonthFromDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate date = LocalDate.parse(dateString, formatter);
        return date.getMonthValue();
    }

    public static int getYearFromDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate date = LocalDate.parse(dateString, formatter);
        return date.getYear();
    }
}