package javadb_project.demo2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ControllerOperations implements Initializable {
    private Integer id;
    private String username;
    private String position;


    private Integer polyclinicId = -1;
    private Integer medicID = -1;
    private Integer procedureDuration = 0;
    private Integer procedureId = -1;
    private LocalDate currentDate = LocalDate.now();

    @FXML
    private ComboBox combobox_selectservice;
    @FXML
    private ComboBox combobox_selecthour;
    @FXML
    private ComboBox combobox_selectmedic;
    @FXML
    private DatePicker datepicker_selectdate;
    @FXML
    private TextField textfield_pacientcnp;
    @FXML
    private Button button_makeappointment;
    @FXML
    private Button button_makereicept;
    @FXML
    private Button button_makeconsultation;
    @FXML
    private Button button_goback;
    @FXML
    private TextField textfield_simtome;
    @FXML
    private TextField textfield_diagnostic;
    @FXML
    private TextField textfield_recomandari;
    @FXML
    private Button button_saveconsultation;

    @FXML
    private TextField textfield_newanalysis_pacientID;
    @FXML
    private TextField textfield_newanalysis_validat;
    @FXML
    private TextField textfield_newanalysis_data;
    @FXML
    private TextField textfield_newanalysis_rezultat;


    @FXML
    private TextField textfield_updateanalysis_pacientID;
    @FXML
    private TextField textfield_updateanalysis_data;
    @FXML
    private TextField textfield_updateanalysis_rezultat;
    @FXML
    private TextField textfield_updateanalysis_validat;
    @FXML
    private Button button_newanalysis;
    @FXML
    private Button button_updateanalysis;

    @FXML
    private TableView<Columns> tableview_showallappointments;
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
    private TableView<Columns> tableview_consultations;
    @FXML
    private TableColumn<Columns, String> col1consultations;
    @FXML
    private TableColumn<Columns, String> col2consultations;
    @FXML
    private TableColumn<Columns, String> col3consultations;
    @FXML
    private TableColumn<Columns, String> col4consultations;
    @FXML
    private TableColumn<Columns, String> col5consultations;
    @FXML
    private TableColumn<Columns, String> col6consultations;


    @FXML
    private TableView<Columns> tableview_reicepts;
    @FXML
    private TableColumn<Columns, String> col1reicepts;
    @FXML
    private TableColumn<Columns, String> col2reicepts;
    @FXML
    private TableColumn<Columns, String> col3reicepts;
    @FXML
    private TableColumn<Columns, String> col4reicepts;

    @FXML
    private TableView<Columns> tableview_usermedicalreport;
    @FXML
    private TableColumn<Columns, String> col1medrap;
    @FXML
    private TableColumn<Columns, String> col2medrap;
    @FXML
    private TableColumn<Columns, String> col3medrap;
    @FXML
    private TableColumn<Columns, String> col4medrap;
    @FXML
    private TableColumn<Columns, String> col5medrap;


    @FXML
    private TableView<Columns> tableview_showmedicalanalysis;
    @FXML
    private TableColumn<Columns, String> col1analysis;
    @FXML
    private TableColumn<Columns, String> col2analysis;
    @FXML
    private TableColumn<Columns, String> col3analysis;
    @FXML
    private TableColumn<Columns, String> col4analysis;
    @FXML
    private TableColumn<Columns, String> col5analysis;
    @FXML
    private TableColumn<Columns, String> col6analysis;


    private static class employeeVacantion {
        public LocalDate startDate;
        public LocalDate stopDate;

        public employeeVacantion(LocalDate start, LocalDate finish) {
            this.startDate = start;
            this.stopDate = finish;
        }
    }

    private static class timeIntervalBusy {
        public String startTime;
        public String stopTime;
        public Integer duration;

        public timeIntervalBusy(String start, String stop) {
            this.startTime = start;
            this.stopTime = stop;
        }

        public timeIntervalBusy(String start, Integer duration) {
            this.startTime = start;
            this.duration = duration;
        }
    }

    private List<employeeVacantion> allEmployeeVacantion = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        combobox_selectservice.setOnAction(actionEvent -> {
            medicID = -1;
            datepicker_selectdate.setValue(LocalDate.now());
            combobox_selecthour.getItems().clear();
            allEmployeeVacantion.clear();
            combobox_selectmedic.getItems().clear();
            getAllMedicFilteredByRequiredCompetencies();
        });

        combobox_selectmedic.setOnAction(actionEvent -> {
            medicID = -1;
            datepicker_selectdate.setValue(LocalDate.now());
            combobox_selecthour.getItems().clear();
            setCalendarDays();
        });

        datepicker_selectdate.setOnAction(actionEvent -> {
            combobox_selecthour.getItems().clear();
            if (!checkIfAvalaibleDate()) {
                datepicker_selectdate.setValue(LocalDate.now());
            }
            getAllAvalaibleHours();
        });

        combobox_selecthour.setOnAction(actionEvent -> {
            getAllAvalaibleHours();
        });


        button_makeappointment.setOnAction(actionEvent -> {
            if (!textfield_pacientcnp.getText().isEmpty() && !combobox_selectmedic.getItems().isEmpty()
                    && !combobox_selectservice.getItems().isEmpty() && !combobox_selecthour.getItems().isEmpty()) {
                if (checkIfAvalaibleDate()) {
                    makeAppointment();
                    DBUtils.changeScene(actionEvent, "logged_in_Operations.fxml", id, username, position);
                }
            } else {
                DBUtils.printError(Error.ERROR1_uncompleted_text_fields);
            }
        });

        button_makeconsultation.setOnAction(actionEvent -> {
            if (!tableview_showallappointments.getSelectionModel().getSelectedItems().isEmpty()) {
                makeConsultation();
                tableview_usermedicalreport.setVisible(true);
                textfield_pacientcnp.setVisible(false);
                combobox_selecthour.setVisible(false);
                combobox_selectmedic.setVisible(false);
                combobox_selectservice.setVisible(false);
                button_makeappointment.setVisible(false);
                button_makeconsultation.setVisible(false);
                tableview_showallappointments.setVisible(false);
                tableview_consultations.setVisible(false);
                datepicker_selectdate.setVisible(false);
                textfield_simtome.setVisible(true);
                textfield_recomandari.setVisible(true);
                textfield_diagnostic.setVisible(true);
                button_saveconsultation.setVisible(true);
                tableview_showmedicalanalysis.setVisible(true);
                showMedicalHistoric();
                showAnalizeMedicale(tableview_showallappointments.getSelectionModel().getSelectedItem().usernameProperty().getValue().toString());
            }
        });

        button_makereicept.setOnAction(actionEvent -> {
            if (!tableview_consultations.getSelectionModel().getSelectedItems().isEmpty()) {
                makeReicept();
                DBUtils.changeScene(actionEvent, "logged_in_Operations.fxml", id, username, position);
            }
        });

        button_goback.setOnAction(actionEvent -> {
            DBUtils.changeScene(actionEvent, "logged_in.fxml", id, username, position);
        });

        button_saveconsultation.setOnAction(actionEvent -> {
            if (!textfield_diagnostic.getText().isEmpty() && !textfield_recomandari.getText().isEmpty() &&
                    !textfield_simtome.getText().isEmpty()) {
                // can save
                saveMedicalReport();
                DBUtils.changeScene(actionEvent, "logged_in_Operations.fxml", id, username, position);
            }
        });

        tableview_showmedicalanalysis.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleKeyPressed();
            }
        });


        button_updateanalysis.setOnAction(actionEvent -> {
            if (!textfield_updateanalysis_validat.getText().isEmpty()) {
                updateMedicalAnalysis(tableview_showmedicalanalysis.getSelectionModel().getSelectedItem().idProperty().getValue().toString());
                DBUtils.changeScene(actionEvent, "logged_in_Operations.fxml", id, username, position);
            }
        });
        button_newanalysis.setOnAction(actionEvent -> {

            if (!textfield_newanalysis_pacientID.getText().isEmpty() &&
                    !textfield_newanalysis_data.getText().isEmpty() && !textfield_newanalysis_rezultat.getText().isEmpty() &&
                    !textfield_newanalysis_validat.getText().isEmpty()) {
                newAnalizeMedicale();
                DBUtils.changeScene(actionEvent, "logged_in_Operations.fxml", id, username, position);
            }
        });

        tableview_showallappointments.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                tableview_showmedicalanalysis.getItems().clear();
                showAnalizeMedicale(tableview_showallappointments.getSelectionModel().getSelectedItem().usernameProperty().getValue().toString());
            }
        });
    }

    public void initializeWitData(Integer id, String username, String position) {
        this.id = id;
        this.username = username;
        this.position = position;
        polyclinicId = DBUtils.searchForPolyclinicID(String.valueOf(id), null, null, null);
        tableview_usermedicalreport.setVisible(false);
        textfield_diagnostic.setVisible(false);
        textfield_recomandari.setVisible(false);
        textfield_simtome.setVisible(false);
        button_saveconsultation.setVisible(false);
        // TODO : for Receptioner - make appointments, ( by medic and specialization, for current or upcoming days) ,
        //  and make reicept based on appointment
        if (position.equals("Receptioner")) {
            tableview_usermedicalreport.setVisible(false);


            textfield_newanalysis_validat.setVisible(false);
            textfield_newanalysis_data.setVisible(false);

            textfield_newanalysis_pacientID.setVisible(false);
            textfield_newanalysis_rezultat.setVisible(false);
            textfield_updateanalysis_data.setVisible(false);

            textfield_updateanalysis_pacientID.setVisible(false);
            textfield_updateanalysis_rezultat.setVisible(false);

            textfield_updateanalysis_validat.setVisible(false);
            button_newanalysis.setVisible(false);
            button_updateanalysis.setVisible(false);
            tableview_showmedicalanalysis.setVisible(false);
            button_makeconsultation.setVisible(false);

            getAllMedicalServices();
            showAllConsultations();
            showAllAppointments();
            getAllReicepts();


        }
        // TODO :  for asistent medical to complete medical raports ( approve them and cannot modify them afterwards)
        else if (position.equals("Asistent")) {
            button_makeappointment.setVisible(false);
            button_makereicept.setVisible(false);
            tableview_reicepts.setVisible(false);
            textfield_pacientcnp.setVisible(false);
            combobox_selecthour.setVisible(false);
            combobox_selectmedic.setVisible(false);
            combobox_selectservice.setVisible(false);
            datepicker_selectdate.setVisible(false);
            tableview_usermedicalreport.setVisible(false);
            tableview_consultations.setVisible(false);
            tableview_reicepts.setVisible(false);
            textfield_newanalysis_data.setEditable(false);
            textfield_newanalysis_data.setText(LocalDate.now().toString());
            button_makeconsultation.setVisible(false);
            textfield_updateanalysis_data.setEditable(false);
            textfield_updateanalysis_pacientID.setEditable(false);
            textfield_updateanalysis_rezultat.setEditable(false);
            showAllAppointments();
        }

        // TODO : for medics : can see appointed pacients to him , to see the entire medical historic , and complete
        //  a raport, with parafa and cannot be modified afterwards, and this can be seen in pacients medical historic
        else if (position.equals("Medic")) {
            button_makeappointment.setVisible(false);
            button_makereicept.setVisible(false);
            tableview_reicepts.setVisible(false);
            textfield_pacientcnp.setVisible(false);
            combobox_selecthour.setVisible(false);
            combobox_selectmedic.setVisible(false);
            combobox_selectservice.setVisible(false);
            datepicker_selectdate.setVisible(false);
            textfield_newanalysis_validat.setVisible(false);
            textfield_newanalysis_data.setVisible(false);

            textfield_newanalysis_pacientID.setVisible(false);
            textfield_newanalysis_rezultat.setVisible(false);
            textfield_updateanalysis_data.setVisible(false);

            textfield_updateanalysis_pacientID.setVisible(false);
            textfield_updateanalysis_rezultat.setVisible(false);

            textfield_updateanalysis_validat.setVisible(false);
            button_newanalysis.setVisible(false);
            button_updateanalysis.setVisible(false);
            tableview_showmedicalanalysis.setVisible(false);

            showAllAppointments();
            showAllConsultations();
        }
    }

    private void getAllMedicalServices() {
        try {
            Connection connection = DBUtils.createConnection();
            String query = " select * from serviciimedicale where Locatie = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(polyclinicId));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                combobox_selectservice.getItems().add(resultSet.getString("NumeServiciu"));
            }
            DBUtils.closeConnection(connection, preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getAllMedicFilteredByRequiredCompetencies() {
        combobox_selectmedic.getItems().clear();
        try {
            Connection connection = DBUtils.createConnection();
            String query = " select Serviciimedicale.id,serviciimedicale.DurataInMinute,serviciimedicale.locatie,serviciimedicale.CompetenteNecesare," +
                    "serviciimedicale.NumeServiciu,serviciimedicale.locatie, detaliimedici.Competente, " +
                    "detaliimedici.MedicID,utilizatori.id, utilizatori.nume,utilizatori.prenume from detaliimedici" +
                    " JOIN serviciimedicale ON detaliimedici.competente= serviciimedicale.CompetenteNecesare" +
                    " JOIN utilizatori on utilizatori.id = detaliimedici.MedicID " +
                    " where serviciimedicale.NumeServiciu =? and serviciimedicale.locatie = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, (String) combobox_selectservice.getValue());
            preparedStatement.setString(2, String.valueOf(polyclinicId));

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //if(resultSet.getString("Competente").contains(combobox_selectservice))
                procedureDuration = resultSet.getInt("serviciimedicale.DurataInMinute");
                procedureId = resultSet.getInt("Serviciimedicale.id");
                combobox_selectmedic.getItems().add(resultSet.getString("detaliimedici.MedicID") + " " + resultSet.getString("utilizatori.nume") + " " + resultSet.getString("utilizatori.prenume"));
            }
            DBUtils.closeConnection(connection, preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCalendarDays() {
        medicID = extractFirstNumber(combobox_selectmedic.getValue().toString());
        if (medicID == -1) {
            DBUtils.printError(Error.ERROR5_nouserfound);
            return;
        }

        try {
            Connection connection = DBUtils.createConnection();
            String query = " select * from concediiangajati where AngajatID =?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(medicID));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String startdayString = resultSet.getString("DataInceput");
                String stopdayString = resultSet.getString("DataSfarsit");
                LocalDate startDay = LocalDate.of(DBUtils.getYearFromDate(startdayString), DBUtils.getMonthFromDate(startdayString), DBUtils.getDayFromDate(startdayString));
                LocalDate stopDay = LocalDate.of(DBUtils.getYearFromDate(stopdayString), DBUtils.getMonthFromDate(stopdayString), DBUtils.getDayFromDate(stopdayString));

                allEmployeeVacantion.add(new employeeVacantion(startDay, stopDay));
            }
            DBUtils.closeConnection(connection, preparedStatement, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfAvalaibleDate() {
        if (datepicker_selectdate.getValue().isBefore(currentDate)) {
            DBUtils.printError(Error.ERROR6_INVALIDDATE);
            return false;
        }
        for (employeeVacantion i : allEmployeeVacantion) {
            if (i.startDate.isBefore(datepicker_selectdate.getValue()) && i.stopDate.isAfter(datepicker_selectdate.getValue())) {
                DBUtils.printError(Error.ERROR6_INVALIDDATE);
                return false;
            }
        }
        return true;
    }

    private void getAllAvalaibleHours() {
// TODO : get the hour interval for the day of the week and select all avalaible posibilities
        LocalDate selectedDate = datepicker_selectdate.getValue();
        DayOfWeek dayOfWeek = selectedDate.getDayOfWeek();
        String dayOfWeekString = dayOfWeek.toString();

        List<timeIntervalBusy> timeIntervalAtWorks = new ArrayList<>();
        List<timeIntervalBusy> timeIntervalBusies = new ArrayList<>();
        try { // get all the time intervals at work in that day
            Connection connection = DBUtils.createConnection();
            String query = " Select * from orarangajati where AngajatId = ? and ZiuaSaptamanii=? and Locatie=? ORDER BY OraInceput ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(medicID));
            preparedStatement.setString(2, dayOfWeekString);
            preparedStatement.setString(3, String.valueOf(polyclinicId));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                timeIntervalAtWorks.add(new timeIntervalBusy(resultSet.getString("OraInceput"), resultSet.getString("OraSfarsit")));
                System.out.println("atwork" + resultSet.getString("OraInceput") + resultSet.getString("OraSfarsit"));
            }
            DBUtils.closeConnection(connection, preparedStatement, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try { // get all the time intervals at work in that day
            Connection connection = DBUtils.createConnection();
            String query = " Select * from programari where MedicId = ? and Data=? and Locatie=? ORDER BY OraInceput ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(medicID));
            preparedStatement.setString(2, datepicker_selectdate.getValue().toString());
            preparedStatement.setString(3, String.valueOf(polyclinicId));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                timeIntervalBusies.add(new timeIntervalBusy(resultSet.getString("OraInceput"), resultSet.getInt("DurataProgramare")));
            }
            DBUtils.closeConnection(connection, preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        for (timeIntervalBusy timeIsAtWork : timeIntervalAtWorks) {
            LocalTime largerStartTime = LocalTime.parse(timeIsAtWork.startTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime largerEndTime = LocalTime.parse(timeIsAtWork.stopTime, DateTimeFormatter.ofPattern("HH:mm:ss"));

            LocalTime currrentEndTime = largerStartTime;
            for (timeIntervalBusy timeIsBusy : timeIntervalBusies) {
                LocalTime smallerStartTime = LocalTime.parse(timeIsBusy.startTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
                LocalTime smallerEndTime = smallerStartTime.plusMinutes(timeIsBusy.duration);
                if (currrentEndTime.isBefore(smallerStartTime) && currrentEndTime.plusMinutes(procedureDuration).isBefore(smallerStartTime)) {
                    // found time interval
                    LocalTime suitableStartTime = currrentEndTime;
                    LocalTime suitableEndTime = currrentEndTime.plusMinutes(procedureDuration);
                    combobox_selecthour.getItems().add(String.valueOf(suitableStartTime));
                }
                currrentEndTime = smallerEndTime;
            }
            if (currrentEndTime.plusMinutes(procedureDuration).isBefore(largerEndTime)) {
                //found again
                LocalTime suitableStartTime = currrentEndTime;

                combobox_selecthour.getItems().add(String.valueOf(suitableStartTime));
            }
        }
    }

    private void makeAppointment() {
        // search if pacient alreadyexists
        Boolean pacientExists = false;
        Integer pacientId = -1;
        try {
            Connection connection = DBUtils.createConnection();
            String query = " SELECT * from utilizatori where CNP=? and TipUtilizator =?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, textfield_pacientcnp.getText().toString());
            preparedStatement.setString(2, "Pacient");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                pacientExists = true;
                pacientId = resultSet.getInt("Id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (pacientExists == false) {
            // add pacient to users database
            try {
                Connection connection = DBUtils.createConnection();
                String query = " INSERT INTO utilizatori (USERNAME, PAROLA,CNP,NUME,PRENUME,TipUtilizator) values ( ?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, "pacient");
                preparedStatement.setString(2, "pacient");
                preparedStatement.setString(3, textfield_pacientcnp.getText().toString());
                preparedStatement.setString(4, "pacient");
                preparedStatement.setString(5, "pacient");
                preparedStatement.setString(6, "Pacient");
                preparedStatement.executeUpdate();
                DBUtils.closeConnection(connection, preparedStatement, null);

            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {// retrieve pacient id
                Connection connection = DBUtils.createConnection();
                String query = " SELECT * from utilizatori where CNP=? and TipUtilizator =?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, textfield_pacientcnp.getText().toString());
                preparedStatement.setString(2, "Pacient");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    //pacientExists = true;
                    pacientId = resultSet.getInt("Id");
                }
                DBUtils.closeConnection(connection, preparedStatement, resultSet);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {// make appointment
            Connection connection = DBUtils.createConnection();
            String query = " INSERT INTO programari (PacientID,MedicID, Data,OraInceput,DurataProgramare," +
                    "ServiciuMedicalID,Locatie) VALUES (?,?,?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(pacientId));
            preparedStatement.setString(2, String.valueOf(medicID));
            preparedStatement.setString(3, datepicker_selectdate.getValue().toString());
            preparedStatement.setString(4, combobox_selecthour.getValue().toString());
            preparedStatement.setString(5, String.valueOf(procedureDuration));
            preparedStatement.setString(6, String.valueOf(procedureId));
            preparedStatement.setString(7, String.valueOf(polyclinicId));
            preparedStatement.executeUpdate();
            DBUtils.closeConnection(connection, preparedStatement, null);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAllAppointments() {
        tableview_showallappointments.getItems().clear();

        col1.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col2.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col3.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        col4.setCellValueFactory(cellData -> cellData.getValue().cnpProperty());
        col5.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        col6.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        col7.setCellValueFactory(cellData -> cellData.getValue().addressProperty());

        //????????????????????????????????????????????????

        try {
            Connection connection = DBUtils.createConnection();
            PreparedStatement preparedStatement = null;
            String query = "select programari.id,programari.pacientId,programari.medicID,programari.data," +
                    "programari.OraInceput,programari.ServiciuMedicalID,programari.locatie,serviciiMedicale.ID," +
                    "serviciimedicale.NumeServiciu from programari" +
                    " JOIN utilizatori on utilizatori.id = programari.pacientID" +
                    " JOIN serviciimedicale on serviciiMedicale.id = programari.ServiciuMedicalID" +
                    "  where programari.locatie = ? ";
            if (this.position.equals("Medic")) {
                query += " and programari.medicID=? ORDER BY programari.DATA ASC,programari.ORAINCEPUT ASC";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, String.valueOf(polyclinicId));
                preparedStatement.setString(2, String.valueOf(this.id));
            } else {
                query += "  ORDER BY programari.DATA ASC,programari.ORAINCEPUT ASC";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, String.valueOf(polyclinicId));
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Columns> itemList = new ArrayList<>();
            while (resultSet.next()) {
                itemList.add(new Columns(resultSet.getString("programari.id"), resultSet.getString("programari.PacientID"),
                        resultSet.getString("programari.MedicID"), resultSet.getString("serviciimedicale.numeServiciu"), resultSet.getString("serviciimedicale.id"),
                        resultSet.getString("programari.Data"), resultSet.getString("programari.OraInceput"),
                        null, null, null, null,
                        null, null, null, null));
            }
            tableview_showallappointments.getItems().addAll(itemList);
            DBUtils.closeConnection(connection, preparedStatement, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAllConsultations() {
        col1consultations.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col2consultations.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col3consultations.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        col4consultations.setCellValueFactory(cellData -> cellData.getValue().cnpProperty());
        col5consultations.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        col6consultations.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        try {
            Connection connection = DBUtils.createConnection();
            PreparedStatement preparedStatement = null;
            String query = " SELECT  consultatii.ID_Consultatie,  consultatii.ProgramareID, consultatii.PacientID,  " +
                    "consultatii.MedicID, consultatii.Data,serviciimedicale.id,serviciimedicale.pret from consultatii " +
                    " JOIN serviciimedicale on serviciimedicale.id = consultatii.serviciuID" +
                    " where consultatii.Locatie = ?";
            if (this.position.equals("Medic")) {
                query += " and consultatii.medicID = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, String.valueOf(polyclinicId));
                preparedStatement.setString(2, String.valueOf(this.id));
            } else {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, String.valueOf(polyclinicId));
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Columns> itemList = new ArrayList<>();
            while (resultSet.next()) {
                itemList.add(new Columns(resultSet.getString("consultatii.ID_Consultatie"),
                        resultSet.getString("consultatii.ProgramareID"), resultSet.getString("consultatii.PacientID"),
                        resultSet.getString("consultatii.MedicID"), resultSet.getString("consultatii.Data"),
                        resultSet.getString("Serviciimedicale.pret"), null, null,
                        null, null, null, null, null, null, null));
            }
            tableview_consultations.getItems().addAll(itemList);
            DBUtils.closeConnection(connection, preparedStatement, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getAllReicepts() {
        col1reicepts.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col2reicepts.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col3reicepts.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        col4reicepts.setCellValueFactory(cellData -> cellData.getValue().cnpProperty());

        try {
            Connection connection = DBUtils.createConnection();
            String query = " Select bonurifiscale.id,bonurifiscale.Consultatieid,bonurifiscale.dataemiterii," +
                    "bonurifiscale.locatie, consultatii.ID_consultatie,consultatii.PacientID , " +
                    "serviciimedicale.id,consultatii.ServiciuID,serviciimedicale.pret from bonurifiscale" +
                    " JOIN consultatii on consultatii.ID_consultatie = bonurifiscale.consultatieid " +
                    " JOIN serviciimedicale on serviciimedicale.id = consultatii.ServiciuID " +
                    " where bonurifiscale.locatie =? ORDER BY bonurifiscale.dataemiterii ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(polyclinicId));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Columns> itemList = new ArrayList<>();
            while (resultSet.next()) {
                itemList.add(new Columns(resultSet.getString("bonurifiscale.id"),
                        resultSet.getString("consultatii.PacientID"), resultSet.getString("bonurifiscale.dataemiterii"),
                        resultSet.getString("serviciimedicale.pret"), null, null, null,
                        null, null, null, null, null, null, null, null));
            }
            tableview_reicepts.getItems().addAll(itemList);
            DBUtils.closeConnection(connection, preparedStatement, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void makeReicept() {
        try {
            Connection connection = DBUtils.createConnection();
            String query = " INSERT INTO bonurifiscale (ConsultatieID,DataEmiterii,Locatie) values (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tableview_consultations.getSelectionModel().getSelectedItem().idProperty().getValue().toString());
            preparedStatement.setString(2, LocalDate.now().toString());
            preparedStatement.setString(3, String.valueOf(polyclinicId));
            preparedStatement.executeUpdate();
            DBUtils.closeConnection(connection, preparedStatement, null);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void makeConsultation() {
        Integer duration = 0;
        try {

            Connection connection = DBUtils.createConnection();
            String query = " Select duratainminute from serviciimedicale where id =? ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tableview_showallappointments.getSelectionModel().getSelectedItem().firstNameProperty().getValue().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                duration = resultSet.getInt("Duratainminute");
            }
            DBUtils.closeConnection(connection, preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DBUtils.createConnection();
            String query = " INSERT INTO consultatii (MedicID,PacientID, Data, Durata, ServiciuID, ProgramareID,Locatie) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tableview_showallappointments.getSelectionModel().getSelectedItem().passwordProperty().getValue().toString());
            preparedStatement.setString(2, tableview_showallappointments.getSelectionModel().getSelectedItem().usernameProperty().getValue().toString());
            preparedStatement.setString(3, tableview_showallappointments.getSelectionModel().getSelectedItem().lastNameProperty().getValue().toString());
            preparedStatement.setString(4, String.valueOf(duration));
            preparedStatement.setString(5, tableview_showallappointments.getSelectionModel().getSelectedItem().firstNameProperty().getValue().toString());
            preparedStatement.setString(6, tableview_showallappointments.getSelectionModel().getSelectedItem().idProperty().getValue().toString());
            preparedStatement.setString(7, String.valueOf(polyclinicId));
            preparedStatement.executeUpdate();
            DBUtils.closeConnection(connection, preparedStatement, null);
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    private void showMedicalHistoric() {
        col1medrap.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col2medrap.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col3medrap.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        col4medrap.setCellValueFactory(cellData -> cellData.getValue().cnpProperty());
        col5medrap.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());

        try {
            Connection connection = DBUtils.createConnection();
            String query = " Select * from rapoartemedicale where PacientID = ? ORDER BY DATA ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tableview_showallappointments.getSelectionModel().getSelectedItem().usernameProperty().getValue().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Columns> itemList = new ArrayList<>();
            while (resultSet.next()) {
                itemList.add(new Columns(resultSet.getString("Data"), resultSet.getString("IstoricSimptome"),
                        resultSet.getString("Diagnostic"), resultSet.getString("Recomandari"),
                        medicCODE(), null, null, null, null, null,
                        null, null, null, null, null));
                System.out.println("yes");
            }
            tableview_usermedicalreport.getItems().addAll(itemList);
            DBUtils.closeConnection(connection, preparedStatement, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String medicCODE() {
        String codParafa = "";

        try {
            Connection connection = DBUtils.createConnection();
            String query = " Select CodParafa from detaliimedici where MedicID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(this.id));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                codParafa = resultSet.getString("CodParafa");
            }
            DBUtils.closeConnection(connection, preparedStatement, resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return codParafa;
    }

    private void saveMedicalReport() {

        try {
            Connection connection = DBUtils.createConnection();
            String query = " Insert INTO rapoarteMedicale (PacientID,MedicID,AsistentID,Data,IstoricSimptome,Diagnostic,Recomandari,Parafat) values (?,?,0,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tableview_showallappointments.getSelectionModel().getSelectedItem().usernameProperty().getValue().toString());
            preparedStatement.setString(2, String.valueOf(this.id));
            preparedStatement.setString(3, LocalDate.now().toString());
            preparedStatement.setString(4, textfield_simtome.getText().toString());
            preparedStatement.setString(5, textfield_diagnostic.getText().toString());
            preparedStatement.setString(6, textfield_recomandari.getText().toString());
            preparedStatement.setString(7, medicCODE());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAnalizeMedicale(String PacientID) {
        col1analysis.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        col2analysis.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        col3analysis.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        col4analysis.setCellValueFactory(cellData -> cellData.getValue().cnpProperty());
        col5analysis.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        col6analysis.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        try {
            Connection connection = DBUtils.createConnection();
            String query = " Select * from rapoarteanalizemedicale where pacientID =? ORDER BY data ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, PacientID);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Columns> itemList = new ArrayList<>();
            while (resultSet.next()) {
                itemList.add(new Columns(resultSet.getString("id"), resultSet.getString("PacientID"),
                        resultSet.getString("AsistentID"), resultSet.getString("data"),
                        resultSet.getString("Rezultat"), resultSet.getString("Validat"), null,
                        null, null, null, null, null, null,
                        null, null));
                // add to table
                tableview_showmedicalanalysis.getItems().addAll(itemList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void newAnalizeMedicale() {

        try {
            Connection connection = DBUtils.createConnection();
            String query = " Insert into rapoarteanalizemedicale (PacientId, AsistentID,Data,Rezultat,Validat) values (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, textfield_newanalysis_pacientID.getText().toString());

            preparedStatement.setString(2, String.valueOf(this.id));
            preparedStatement.setString(3, LocalDate.now().toString());
            preparedStatement.setString(4, textfield_newanalysis_rezultat.getText().toString());
            preparedStatement.setString(5, String.valueOf(0));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleKeyPressed() {
        Columns selectedRow = (Columns) tableview_showmedicalanalysis.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            // textfield_updateanalysis_medicId.setText(selectedRow.usernameProperty().get());
            textfield_updateanalysis_pacientID.setText(selectedRow.usernameProperty().get());
            textfield_updateanalysis_data.setText(selectedRow.cnpProperty().get());
            textfield_updateanalysis_rezultat.setText(selectedRow.firstNameProperty().get());
            textfield_updateanalysis_validat.setText(selectedRow.lastNameProperty().get());
        }
    }

    private void updateMedicalAnalysis(String AnalysisId) {
        try {
            Connection connection = DBUtils.createConnection();
            String query = " Select * from tipuriasistenti where AsistentID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(this.id));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (!resultSet.getString("TipAsistent").equals("Laborator")) {
                    DBUtils.printError(Error.ERROR2_no_right_to_assign_this_usertype);
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Connection connection = DBUtils.createConnection();
            String query = " UPDATE rapoarteanalizemedicale SET validat =?  where id =?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, textfield_updateanalysis_validat.getText().toString());
            preparedStatement.setString(2, AnalysisId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int extractFirstNumber(String inputString) {
        Pattern pattern = Pattern.compile("\\b\\d+\\b");
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        } else {
            return -1;
        }
    }
}