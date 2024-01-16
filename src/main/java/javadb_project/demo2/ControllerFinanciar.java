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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

class FinancialUserData {
    public Integer userId = 0;
    public String name = "";
    public Double[] salaryPerMonth = new Double[13];
    public Double totalRevenue = 0.00;
    public Double[] medicaUnitRevenuePerMonthFromEmployee = new Double[13];
    public Double[] comision = new Double[13];

    public FinancialUserData(Integer id) {
        this.userId = id;
        for (int i = 0; i <= 12; i++) {
            salaryPerMonth[i] = 0.00;
            medicaUnitRevenuePerMonthFromEmployee[i] = 0.00;
            comision[i] = 0.00;
        }
    }
}

public class ControllerFinanciar implements Initializable {
    private Integer userId;
    private String username;
    private String position;

    @FXML
    private TableView tableview_show_profit_medicalunit;
    @FXML
    private TableColumn<Columns, String> col0_showprofitmedicalunit;
    @FXML
    private TableColumn<Columns, String> col1_showprofitmedicalunit;
    @FXML
    private TableColumn<Columns, String> col2_showprofitmedicalunit;
    @FXML
    private TableColumn<Columns, String> col3_showprofitmedicalunit;
    @FXML
    private TableColumn<Columns, String> col4_showprofitmedicalunit;

    @FXML
    private TableView tableview_showfinancialdetailsforspecificmedic;
    @FXML
    private TableColumn<Columns, String> col1_searchedmedic;
    @FXML
    private TableColumn<Columns, String> col2_searchedmedic;
    @FXML
    private TableColumn<Columns, String> col3_searchedmedic;
    @FXML
    private TableColumn<Columns, String> col4_searchedmedic;
    @FXML
    private TableColumn<Columns, String> col5_searchedmedic;
    @FXML
    private TableColumn<Columns, String> col6_searchedmedic;

    @FXML
    private TableView tableview_showfinancialdetailsforallmedics;
    @FXML
    private TableColumn<Columns, String> col1_allmedics;
    @FXML
    private TableColumn<Columns, String> col2_allmedics;
    @FXML
    private TableColumn<Columns, String> col3_allmedics;
    @FXML
    private TableColumn<Columns, String> col4_allmedics;
    @FXML
    private TableColumn<Columns, String> col5_allmedics;
    @FXML
    private TableColumn<Columns, String> col6_allmedics;
    @FXML
    private TableColumn<Columns, String> col7_allmedics;

    @FXML
    private TableView tableview_showfinancialdetailsemployee;
    @FXML
    private TableColumn<Columns, String> col1_searchedemployee;
    @FXML
    private TableColumn<Columns, String> col2_searchedemployee;
    @FXML
    private TableColumn<Columns, String> col3_searchedemployee;
    @FXML
    private TableColumn<Columns, String> col4_searchedemployee;

    @FXML
    private Button button_searchmedic;
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
    private Label label_medicalunitprofit;


    @FXML
    private Button button_goback;

    private int currentYear;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_searchmedic.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!checkIfEmplyTxtFields()) {
                    if (!txtfield_position.getText().equals("Medic"))
                        showFinancialDetailsForEmployee(-1, txtfield_firstname.getText(), txtfield_lastname.getText(), txtfield_position.getText());
                    else
                        searchMedicAndShowFinancialDetails(-1, txtfield_firstname.getText(), txtfield_lastname.getText());
                }
            }
        });
        button_goback.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.changeScene(actionEvent, "logged_in.fxml", userId, username, position);
            }
        });
    }

    public void initializewithData(Integer id, String username, String position) {
        this.userId = id;
        this.username = username;
        this.position = position;

        LocalDate localDate = LocalDate.now();
        this.currentYear = 2023;//localDate.getYear();

        if (!position.equals("Financial Analyst")) {
            tableview_showfinancialdetailsforallmedics.setVisible(false);
            tableview_show_profit_medicalunit.setVisible(false);
            if (position.equals("Medic")) {
                tableview_showfinancialdetailsemployee.setVisible(false);
                tableview_showfinancialdetailsforspecificmedic.setLayoutX(50);
                tableview_showfinancialdetailsforspecificmedic.setLayoutY(50);
                searchMedicAndShowFinancialDetails(id, null, null);
            } else {
                tableview_showfinancialdetailsforspecificmedic.setVisible(false);
                tableview_showfinancialdetailsemployee.setLayoutX(50);
                tableview_showfinancialdetailsemployee.setLayoutY(50);

                showFinancialDetailsForEmployee(id, null, null, null);
            }
            button_searchmedic.setVisible(false);
            txtfield_firstname.setVisible(false);
            txtfield_lastname.setVisible(false);
            txtfield_position.setVisible(false);
            label_firstname.setVisible(false);
            label_lastname.setVisible(false);
            label_position.setVisible(false);
            label_medicalunitprofit.setVisible(false);
        } else {
            showProfitPerMedicalUnit();
            showFinancialDetailsForAllMedics();
        }


    }

    private boolean checkIfEmplyTxtFields() {
        if (txtfield_firstname.getText().isEmpty() || txtfield_lastname.getText().isEmpty()) {
            DBUtils.printError(Error.ERROR1_uncompleted_text_fields);
            return true;
        }
        return false;
    }

    public void showProfitPerMedicalUnit() {
        Integer polyclinicId = 0;
        if (this.position.equals("Financial Analyst")) {
            polyclinicId = DBUtils.searchForPolyclinicID(String.valueOf(this.userId), null, null, null);
        }

        List<Columns> itemList = new ArrayList<>();
        col0_showprofitmedicalunit.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col1_showprofitmedicalunit.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col2_showprofitmedicalunit.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        col3_showprofitmedicalunit.setCellValueFactory(cellData -> cellData.getValue().cnpProperty());
        col4_showprofitmedicalunit.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());

        double[] incomePerMonths = new double[13];
        double[] expensesPerMonths = new double[13];
        for (int i = 1; i <= 12; i++) {
            expensesPerMonths[i] = 0;
            incomePerMonths[i] = 0;
        }
        try {
            Connection connection = DBUtils.createConnection();
            String query = " select bonurifiscale.dataEmiterii, bonurifiscale.consultatieID,consultatii.id_consultatie," +
                    " consultatii.serviciuID,serviciimedicale.id,serviciimedicale.pret,detaliimedici.medicID, " +
                    " detaliimedici.ProcentVenit,consultatii.MedicID from bonurifiscale" +
                    " JOIN consultatii on consultatii.id_consultatie = bonurifiscale.consultatieID" +
                    " JOIN serviciimedicale on serviciimedicale.id=consultatii.serviciuID  " +
                    " JOIN detaliimedici on detaliimedici.medicID= consultatii.medicID  ";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getString("bonurifiscale.dataEmiterii");
                if (DBUtils.getYearFromDate(date) == currentYear) {
                    Integer priceOfService = resultSet.getInt("serviciimedicale.pret");
                    Double cutForMedic = resultSet.getDouble("detaliimedici.ProcentVenit");
                    Integer month = DBUtils.getMonthFromDate(date);
                    incomePerMonths[month] = incomePerMonths[month] + priceOfService * (100.00 - cutForMedic) / 100.00;
                }
            }
            DBUtils.closeConnection(connection, preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DBUtils.createConnection();
            String query = "select Suma , Data from cheltuieli";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getString("Data");
                if (DBUtils.getYearFromDate(date) == currentYear) {
                    String sum = resultSet.getString("Suma");
                    Integer month = DBUtils.getMonthFromDate(date);
                    expensesPerMonths[month] = expensesPerMonths[month] + Double.valueOf(sum);
                }
            }
            DBUtils.closeConnection(connection, preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DBUtils.createConnection();
            String query = "select Suma , Data from venituri";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getString("Data");
                if (DBUtils.getYearFromDate(date) == currentYear) {
                    String sum = resultSet.getString("Suma");
                    Integer month = DBUtils.getMonthFromDate(date);
                    expensesPerMonths[month] = expensesPerMonths[month] + Double.valueOf(sum);
                }
            }
            DBUtils.closeConnection(connection, preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 1; i <= 12; i++) {
            itemList.add(new Columns(String.valueOf(polyclinicId), String.valueOf(i), String.valueOf(incomePerMonths[i]), String.valueOf(expensesPerMonths[i]),
                    String.valueOf(incomePerMonths[i] - expensesPerMonths[i]), null, null, null,
                    null, null, null, null, null, null, null));
        }
        tableview_show_profit_medicalunit.getItems().addAll(itemList);
    }

    private void searchMedicAndShowFinancialDetails(Integer givenId, String firstname, String lastname) {
        Integer idSearchedUser;
        if (givenId != -1) {
            idSearchedUser = givenId;
        } else idSearchedUser = DBUtils.searchforID(firstname, lastname, "Medic");
        Integer polyclinicId = DBUtils.searchForPolyclinicID(String.valueOf(idSearchedUser), null, null, null);

        col1_searchedmedic.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col2_searchedmedic.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col3_searchedmedic.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        col4_searchedmedic.setCellValueFactory(cellData -> cellData.getValue().cnpProperty());
        col5_searchedmedic.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        col6_searchedmedic.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        try {
            Connection connection = DBUtils.createConnection();
            String query = "select  bonurifiscale.locatie,venituri.AngajatId,venituri.Suma,venituri.data,detaliimedici.medicID, " +
                    "detaliimedici.ProcentVenit,serviciimedicale.pret ,bonurifiscale.dataemiterii, bonurifiscale.consultatieID, " +
                    "consultatii.id_consultatie, consultatii.ServiciuID,consultatii.MedicID,serviciimedicale.id from bonurifiscale" +
                    " JOIN consultatii on bonurifiscale.consultatieID = consultatii.id_consultatie" +
                    " JOIN serviciimedicale on consultatii.ServiciuID = serviciimedicale.id" +
                    " JOIN detaliimedici on detaliimedici.medicID = ?" +
                    " JOIN venituri on venituri.AngajatId = ?" +
                    " where consultatii.MedicId = ? and bonurifiscale.locatie = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(idSearchedUser));
            preparedStatement.setString(2, String.valueOf(idSearchedUser));
            preparedStatement.setString(3, String.valueOf(idSearchedUser));
            preparedStatement.setString(4, String.valueOf(polyclinicId));

            ResultSet resultSet = preparedStatement.executeQuery();
            FinancialUserData userMedical = new FinancialUserData(0);

            while (resultSet.next()) {
                Integer pretServiciu = resultSet.getInt("serviciimedicale.pret");
                String dataEmitere = resultSet.getString("bonurifiscale.dataemiterii");
                Double procentVenit = resultSet.getDouble("detaliimedici.ProcentVenit");
                if (DBUtils.getYearFromDate(dataEmitere) == currentYear) {
                    Integer lunaEmitere = DBUtils.getMonthFromDate(dataEmitere);
                    userMedical.salaryPerMonth[DBUtils.getMonthFromDate(resultSet.getString("venituri.data"))] = resultSet.getDouble("venituri.suma");
                    userMedical.comision[lunaEmitere] = userMedical.comision[lunaEmitere] + pretServiciu * procentVenit / 100;
                    userMedical.medicaUnitRevenuePerMonthFromEmployee[lunaEmitere] = userMedical.medicaUnitRevenuePerMonthFromEmployee[lunaEmitere] + pretServiciu * (100 - procentVenit) / 100;
                }
            }
            List<Columns> itemList = new ArrayList<>();
            for (int j = 1; j <= 12; j++) {
                itemList.add(new Columns(String.valueOf(j), String.valueOf(userMedical.salaryPerMonth[j]),
                        String.valueOf(userMedical.comision[j]), String.valueOf(userMedical.salaryPerMonth[j] + userMedical.comision[j]),
                        String.valueOf(userMedical.medicaUnitRevenuePerMonthFromEmployee[j]),
                        String.valueOf(userMedical.medicaUnitRevenuePerMonthFromEmployee[j] - userMedical.salaryPerMonth[j] - userMedical.comision[j]),
                        null, null, null, null, null, null, null, null, null));

            }
            tableview_showfinancialdetailsforspecificmedic.getItems().addAll(itemList);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showFinancialDetailsForAllMedics() {
        Integer polyclinicId = DBUtils.searchForPolyclinicID(String.valueOf(this.userId), null, null, null);

        col1_allmedics.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col2_allmedics.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col3_allmedics.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        col4_allmedics.setCellValueFactory(cellData -> cellData.getValue().cnpProperty());
        col5_allmedics.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        col6_allmedics.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        col7_allmedics.setCellValueFactory(cellData -> cellData.getValue().addressProperty());

        try {
            Connection connection = DBUtils.createConnection();
            String query = "select  bonurifiscale.locatie,utilizatori.Nume,utilizatori.Prenume, utilizatori.id, venituri.AngajatId,venituri.Suma," +
                    "venituri.data,detaliimedici.medicID, detaliimedici.ProcentVenit,serviciimedicale.pret , " +
                    "bonurifiscale.dataemiterii, bonurifiscale.ConsultatieId, " +
                    "consultatii.Id_consultatie, consultatii.ServiciuID,consultatii.MedicID,serviciimedicale.id from bonurifiscale" +
                    " JOIN consultatii on bonurifiscale.ConsultatieID = consultatii.Id_consultatie" +
                    " JOIN serviciimedicale on consultatii.ServiciuID = serviciimedicale.id" +
                    " JOIN detaliimedici on detaliimedici.medicID = consultatii.MedicID" +
                    " JOIN venituri on venituri.AngajatId = consultatii.MedicID" +
                    " JOIN utilizatori on utilizatori.id = consultatii.MedicID" +
                    " where bonurifiscale.locatie = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(polyclinicId));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<FinancialUserData> financialUserDataList = new ArrayList<>();
            FinancialUserData current = null;
            while (resultSet.next()) {
                Integer employeeId = resultSet.getInt("detaliimedici.medicID");
                Boolean checkIfEmployeeExists = false;
                for (FinancialUserData i : financialUserDataList) {
                    if (i.userId == employeeId) {
                        checkIfEmployeeExists = true;
                        current = i;
                    }
                }
                if (checkIfEmployeeExists == false) {
                    FinancialUserData newUser = new FinancialUserData(employeeId);
                    Integer pretServiciu = resultSet.getInt("serviciimedicale.pret");
                    String dataEmitere = resultSet.getString("bonurifiscale.dataemiterii");
                    Double procentVenit = resultSet.getDouble("detaliimedici.ProcentVenit");
                    if (DBUtils.getYearFromDate(dataEmitere) == currentYear) {
                        newUser.name = resultSet.getString("utilizatori.nume") + " " + resultSet.getString("utilizatori.prenume");
                        newUser.salaryPerMonth[1] = newUser.salaryPerMonth[1] + resultSet.getDouble("venituri.suma");
                        newUser.comision[1] = newUser.comision[1] + pretServiciu * procentVenit / 100;
                        newUser.medicaUnitRevenuePerMonthFromEmployee[1] = newUser.medicaUnitRevenuePerMonthFromEmployee[1] + pretServiciu * (100 - procentVenit) / 100;
                        financialUserDataList.add(newUser);
                    }
                } else {
                    Integer pretServiciu = resultSet.getInt("serviciimedicale.pret");
                    String dataEmitere = resultSet.getString("bonurifiscale.dataemiterii");
                    Double procentVenit = resultSet.getDouble("detaliimedici.ProcentVenit");
                    if (DBUtils.getYearFromDate(dataEmitere) == currentYear) {
                        // Integer lunaEmitere = DBUtils.getMonthFromDate(dataEmitere);

                        current.salaryPerMonth[DBUtils.getMonthFromDate(resultSet.getString("venituri.data"))] = resultSet.getDouble("venituri.suma");
                        current.comision[1] = current.comision[1] + pretServiciu * procentVenit / 100;
                        current.medicaUnitRevenuePerMonthFromEmployee[1] = current.medicaUnitRevenuePerMonthFromEmployee[1] + pretServiciu * (100 - procentVenit) / 100;
                    }
                }
            }
            List<Columns> itemList = new ArrayList<>();
            for (FinancialUserData i : financialUserDataList
            ) {
                itemList.add(new Columns(String.valueOf(i.userId), i.name, String.valueOf(i.salaryPerMonth[1]),
                        String.valueOf(i.comision[1]), String.valueOf(i.salaryPerMonth[1] + i.comision[1]),
                        String.valueOf(i.medicaUnitRevenuePerMonthFromEmployee[1]),
                        String.valueOf(i.medicaUnitRevenuePerMonthFromEmployee[1] - i.salaryPerMonth[1] - i.comision[1]),
                        null, null, null, null, null, null, null, null));
            }
            tableview_showfinancialdetailsforallmedics.getItems().addAll(itemList);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showFinancialDetailsForEmployee(Integer givenID, String firstname, String lastname, String employeePosition) {
        Integer polyclinicId = DBUtils.searchForPolyclinicID(String.valueOf(this.userId), null, null, null);


        col1_searchedemployee.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col2_searchedemployee.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col3_searchedemployee.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        col4_searchedemployee.setCellValueFactory(cellData -> cellData.getValue().cnpProperty());

        Integer employeeID = 0;
        if (givenID != -1) {
            employeeID = givenID;
        } else employeeID = DBUtils.searchforID(firstname, lastname, employeePosition);
        if (employeeID == 0) {
            DBUtils.printError(Error.ERROR5_nouserfound);
            return;
        }

        try {
            Connection connection = DBUtils.createConnection();
            String query = "select  utilizatori.PoliclinicaId,venituri.AngajatID,venituri.Data, venituri.Suma, utilizatori.id,utilizatori.nume,utilizatori.prenume from venituri" +
                    " JOIN utilizatori on utilizatori.id = ? " +
                    " where venituri.AngajatID = ? and utilizatori.PoliclinicaId=? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(employeeID));
            preparedStatement.setString(2, String.valueOf(employeeID));
            preparedStatement.setString(3, String.valueOf(polyclinicId));

            ResultSet resultSet = preparedStatement.executeQuery();

            FinancialUserData financialUserData = new FinancialUserData(employeeID);

            while (resultSet.next()) {
                Integer month = DBUtils.getMonthFromDate(resultSet.getString("venituri.Data"));
                financialUserData.salaryPerMonth[month] = resultSet.getDouble("venituri.Suma");
                financialUserData.name = resultSet.getString("utilizatori.nume") + " " + resultSet.getString("utilizatori.prenume");
            }
            List<Columns> itemList = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                itemList.add(new Columns(String.valueOf(employeeID), financialUserData.name, String.valueOf(i),
                        String.valueOf(financialUserData.salaryPerMonth[i]), null, null, null,
                        null, null, null, null, null, null, null, null));
            }
            tableview_showfinancialdetailsemployee.getItems().addAll(itemList);
            DBUtils.closeConnection(connection, preparedStatement, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}