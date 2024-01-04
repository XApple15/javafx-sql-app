package javadb_project.demo2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerFinanciar implements Initializable {
    private Integer id;
    private String username;
    private String position;

    @FXML
    private TableView tableview_show_profit_medicalunit;
    @FXML
    private TableColumn<Columns, String> col1_showprofitmedicalunit;
    @FXML
    private TableColumn<Columns, String> col2_showprofitmedicalunit;
    @FXML
    private TableColumn<Columns, String> col3_showprofitmedicalunit;
    @FXML
    private TableColumn<Columns, String> col4_showprofitmedicalunit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initializewithData(Integer id, String username, String position) {
        this.id = id;
        this.username = username;
        this.position = position;
       // showProfitPerMedicalUnit();
        searchMedicAndShowFinancialDetails("Popescu", "Ion");
        if (this.position.equals("Financial Analyst")) {
            // in progress TODO: show profit by month for the medical unit

            // TODO: show salaries
            // TODO: show frofit for each employee
        } else {
            // TODO: show salaries for the past months for the employee
            // TODO: show profit for the past months for the employee
        }
    }

    public void showProfitPerMedicalUnit() {
        LocalDate localDate = LocalDate.now();
        Integer currentYear = localDate.getYear();

        List<Columns> itemList = new ArrayList<>();

        col1_showprofitmedicalunit.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col2_showprofitmedicalunit.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col3_showprofitmedicalunit.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col4_showprofitmedicalunit.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        int[] incomePerMonths = new int[13];
        for (int i = 1; i <= 12; i++) incomePerMonths[i] = 0;
        try {
            Connection connection = DBUtils.createConnection();
            String query = "select SumaVenit , Data from veniturimedici";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getString("Data");
                if (DBUtils.getYearFromDate(date) == currentYear) {
                    String sum = resultSet.getString("SumaVenit");
                    Integer month = DBUtils.getMonthFromDate(sum);
                    incomePerMonths[month] = incomePerMonths[month] + Integer.valueOf(sum);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        int[] expensesPerMonths = new int[13];

        for (int i = 1; i <= 12; i++) expensesPerMonths[i] = 0;

        try {
            Connection connection = DBUtils.createConnection();
            String query = "select Suma , Data from cheltuieli";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getString("Data");
                if (DBUtils.getYearFromDate(date) == currentYear) {
                    String sum = resultSet.getString("Suma");
                    Integer month = DBUtils.getMonthFromDate(sum);
                    expensesPerMonths[month] = expensesPerMonths[month] + Integer.valueOf(sum);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 1; i <= 12; i++) {
            itemList.add(new Columns(String.valueOf(i), String.valueOf(incomePerMonths[i]), String.valueOf(expensesPerMonths[i]),
                    String.valueOf(incomePerMonths[i] - expensesPerMonths[i]), null, null, null,
                    null, null, null, null, null, null, null));
        }
        tableview_show_profit_medicalunit.getItems().addAll(itemList);
    }

    private void searchMedicAndShowFinancialDetails(String firstname, String lastname) {
// where tiputilizator == medical

        Integer idSearchedUser = DBUtils.searchforID(firstname, lastname, "Medic");
// trebuie printat salariile in fiecare luna
        // trebuie printat sumele incasate de policlinica datorita lui - salariu- comision din sume
        try {// get all the services with prices
            Connection connection = DBUtils.createConnection();
            String query = "select serviciigenerale.pret , bonurifiscale.dataemiterii, bonurifiscale.ProgramareID, " +
                    "programari.id, programari.ServiciuGeneralID,programari.MedicID,serviciigenerale.id from bonurifiscale";
            query += " JOIN programari on bonurifiscale.ProgramareID = programari.id";
            query += " JOIN serviciigenerale on programari.ServiciuGeneralID = serviciigenerale.id";
            query += " where programari.MedicId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(idSearchedUser));
            ResultSet resultSet = preparedStatement.executeQuery();
/// !!!!!!!!!! COMISION
            while (resultSet.next()) {
                Integer pretServiciu = resultSet.getInt("serviciigenerale.pret");
                String dataEmitere = resultSet.getString("bonurifiscale.dataemiterii");

                System.out.println(pretServiciu + dataEmitere);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}