package javadb_project.demo2;

import java.sql.*;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

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
        stage.setX(0);
        stage.setY(0);
        if (username != null && position != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                if (fxmlFile.equals("logged_in_Super-Admin.fxml")) {
                    stage.setTitle("Super-Admin");
                    LoggedInControllerSuperAdmin loggedInControllerSuperAdmin = loader.getController();
                    loggedInControllerSuperAdmin.setUserInformation(id, username, position);
                } else if (fxmlFile.equals("logged_in_Admin.fxml")) {
                    stage.setTitle("Admin");
                    LoggedInControllerAdmin loggedInControllerAdmin = loader.getController();
                    loggedInControllerAdmin.setUserInformation(id, username, position);
                } else if (fxmlFile.equals("logged_in_Financiar.fxml")) {

                } else if (fxmlFile.equals("logged_in_HR.fxml")) {
                    stage.setTitle("HR");
                    ControllerHR loggedinHR = loader.getController();
                    loggedinHR.initializewithData(id, username, position);
                } else if (fxmlFile.equals("logged_in_Medical.fxml")) {
                } else {
                    LoggedInController loggedInController = loader.getController();
                    loggedInController.setUserInformation(username, position);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stage.setScene(new Scene(root));
        stage.show();
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
            queryStatement = connection.prepareStatement("SELECT id , Parola,functie, TipUtilizator from utilizatori where Username=?");
            queryStatement.setString(1, username);
            result = queryStatement.executeQuery();
            if (result.isBeforeFirst()) {
                if (result.next()) {
                    String retrievePW = result.getString("parola");
                    if (retrievePW.equals(password)) {
                        String accType = result.getString("TipUtilizator");
                        Integer accID = result.getInt("id");
                        String position = result.getString("functie");
                        if (accType.equals("Super-Administrator")) {
                            DBUtils.changeScene(event, "logged_in_Super-Admin.fxml", accID, username, position);
                        } else if (accType.equals("Administrator")) {
                            DBUtils.changeScene(event, "logged_in_Admin.fxml", accID, username, position);
                        } else if (accType.equals("HR")) {
                            DBUtils.changeScene(event, "logged_in_HR.fxml", accID, username, position);
                        } else if (accType.equals("Financiar")) {
                            DBUtils.changeScene(event, "logged_in_Financiar.fxml", accID, username, position);
                        } else if (accType.equals("Medical")) {
                            DBUtils.changeScene(event, "logged_in_Medical.fxml", accID, username, position);
                        } else {
                            DBUtils.changeScene(event, "logged_in_user.fxml", accID, username, position);
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

    private static void closeConnection(Connection connection, PreparedStatement queryStatement, ResultSet result) {
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
}