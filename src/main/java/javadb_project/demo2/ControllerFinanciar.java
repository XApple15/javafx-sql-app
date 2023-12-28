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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void initializewithData(Integer id, String username, String position) {
        this.id = id;
        this.username = username;
        this.position = position;

        if (this.position.equals("Financial Analyst")) {
            // in progress TODO: show profit by month for the medical unit

            // TODO: show salaries
            // TODO: show frofit for each employee
        } else {
            // TODO: show salaries for the past months for the employee
            // TODO: show profit for the past months for the employee
        }
    }

    public void showProfitPerMonth() {
        LocalDate localDate = LocalDate.now();
        Integer currentYear = localDate.getYear();


        int[] incomePerMonths = new int[13];
        for (int i = 1; i <= 12; i++) incomePerMonths[i] = 0;
        try {
            Connection connection = DBUtils.createConnection();
            String query = "select SumaVenit , Data from veniturimedici";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Columns> itemList = new ArrayList<>();


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

        for(int i =1;i<=12;i++) expensesPerMonths[i]=0;

        try {
            Connection connection = DBUtils.createConnection();
            String query = "select Suma , Data from cheltuieli";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Columns> itemList = new ArrayList<>();


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


    }
}