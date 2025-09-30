package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class AdvisorInfoController {
    @FXML private Label staffData;
    @FXML private Label dontHaveData;
    @FXML private TableView<Object> advisorInfoTable;
    @FXML private Button onListReqButton;
    @FXML private Button onListStudentButton;
    @FXML private Button onSearchData;
    @FXML private ChoiceBox<String> choiceBoxForStaff;
    @FXML private TextField giveData;
    @FXML private Circle profileAdminCircle;
    private String imageDir;
    private Staff staff;
    ObservableList<String> choices;

    private Button lastClickedButton;

    @FXML
    public void initialize()
    {
        choices = FXCollections.observableArrayList("กรุณาเลือก","ชื่อ-นามสกุล","รหัสนิสิต");

        dontHaveData.setVisible(false);
        Datasource<StaffList> staffdatasource = new StaffListFileDatasource("data","staff-list.csv");
        StaffList staffList = staffdatasource.readData();
        imageDir = "data/user-images/";
        lastClickedButton = onListStudentButton;

        String passedData = (String) FXRouter.getData();
        String[] passedDataSplit = passedData.split(",");
        if (passedDataSplit.length == 1) {
            staff = staffList.findStaffByUsername(passedDataSplit[0]);
            onListStudentButtonClick();
        } else if (passedDataSplit.length == 2) {
            staff = staffList.findStaffByUsername(passedDataSplit[0]);
            if (passedDataSplit[1].equals("request")) {
                onListReqButtonClick();
            }
        }

        showInfo(staff);
        setupChoiceBoxListener();

        advisorInfoTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    try {
                        if (newValue instanceof Student) {
                            Student selectedStudent = (Student) newValue;
                            FXRouter.goTo("student-request-history-advisor", selectedStudent.getUsername() + "," +staff.getUsername());
                        } else if (newValue instanceof Request) {
                            Request selectedRequest = (Request) newValue;
                            FXRouter.goTo("approve-request", selectedRequest.getId() + "," + selectedRequest.getDate() + "," + staff.getUsername());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }


    private void showInfo(Staff staff) {
        staffData.setText(staff.getName());
        InputStream imageStream;
        Image image;
        try {
            imageStream = new FileInputStream(imageDir + staff.getImage());
            image = new Image(imageStream);
            profileAdminCircle.setFill(new ImagePattern(image));
        } catch (FileNotFoundException e) {
            try {
                imageStream = new FileInputStream(imageDir + "default-profile-image.png");
                image = new Image(imageStream);
                profileAdminCircle.setFill(new ImagePattern(image));
            } catch (FileNotFoundException ex) {
                profileAdminCircle.setFill(Color.WHITE);
            }
        }
    }


    private void showStudentListTable(StudentList studentList, String advisorName) {
        choiceBoxForStaff.setVisible(true);
        giveData.setVisible(true);
        onSearchData.setVisible(true);
        advisorInfoTable.getColumns().clear();


        TableColumn<Object, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุล");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Object, String> idColumn = new TableColumn<>("รหัสนิสิต");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        TableColumn<Object, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));


        TableColumn<Object, String> majorColumn = new TableColumn<>("ภาควิชา");
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));


        nameColumn.setPrefWidth(173);
        idColumn.setPrefWidth(175);
        emailColumn.setPrefWidth(173);
        majorColumn.setPrefWidth(173);


        advisorInfoTable.getColumns().addAll(nameColumn,idColumn,emailColumn, majorColumn);
        advisorInfoTable.getItems().clear();

        for (Student students : studentList.getStudentsByAdvisor(advisorName)) {
            advisorInfoTable.getItems().addAll(students);
        }



    }

    private void showReqTable(RequestList requestList, String advisorName) {
        choiceBoxForStaff.setVisible(false);
        giveData.setVisible(false);
        onSearchData.setVisible(false);
        dontHaveData.setVisible(false);
        advisorInfoTable.getColumns().clear();

        TableColumn<Object, String> idColumn = new TableColumn<>("รหัสนิสิต");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Object, String> reqColumn = new TableColumn<>("ความประสงค์");
        reqColumn.setCellValueFactory(new PropertyValueFactory<>("requestFor"));

        TableColumn<Object, String> typeColumn = new TableColumn<>("ประเภทคำร้อง");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Object, String> dateColumn = new TableColumn<>("วัน/เวลา");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));


        typeColumn.setPrefWidth(173);
        reqColumn.setPrefWidth(175);
        idColumn.setPrefWidth(173);
        dateColumn.setPrefWidth(173);


        advisorInfoTable.getColumns().addAll(typeColumn, reqColumn, idColumn, dateColumn);
        advisorInfoTable.getItems().clear();

        for (Request request : requestList.getRequestsByAdvisorAndStatus(advisorName, "คำร้องส่งต่อให้อาจารย์ที่ปรึกษา")) {
            advisorInfoTable.getItems().addAll(request);

        }

    }

    @FXML
    public void onListStudentButtonClick()
    {
        Datasource<StudentList> studentListDatasource = new StudentListFileDatasource("data", "student-list.csv");
        StudentList studentList = studentListDatasource.readData();

        handleButtonClick(onListStudentButton);
        showStudentListTable(studentList,staff.getName());
        setupChoiceBoxListener();

    }

    @FXML
    public void onListReqButtonClick()
    {
        Datasource<RequestList> requestsdatasource = new RequestListFileDatasource("data", "request-list.csv");
        RequestList requestList = requestsdatasource.readData();
        requestList.sort();

        handleButtonClick(onListReqButton);
        showReqTable(requestList,staff.getName());
    }


    @FXML
    private void handleButtonClick(Button clickedButton) {
        if(lastClickedButton != null)
        {
            lastClickedButton.setStyle("-fx-background-color:  #006c67; -fx-background-radius: 3; -fx-text-fill:#ffffff;");
        }
        clickedButton.setStyle("-fx-background-color: #8b8d8b; -fx-background-radius: 3; -fx-text-fill: #ffffff;");

        lastClickedButton = clickedButton;

    }


    @FXML
    private void setupChoiceBoxListener() {
        Datasource<StudentList> studentListDatasource = new StudentListFileDatasource("data", "student-list.csv");
        StudentList studentList = studentListDatasource.readData();
        String advisorName = staff.getName();
        choiceBoxForStaff.setValue("กรุณาเลือก");
        choiceBoxForStaff.setItems(choices);
        choiceBoxForStaff.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    giveData.setText("");
                    dontHaveData.setText("");
                    showStudentListTable(studentList,advisorName);
                }
        );
    }


    @FXML
    public void onSearchDataClick() {

        Datasource<StudentList> studentListDatasource = new StudentListFileDatasource("data", "student-list.csv");
        StudentList studentList = studentListDatasource.readData();
        String advisorName = staff.getName();
        String selectedItem = choiceBoxForStaff.getValue();
        String data = giveData.getText();

        try {
            if (!data.isEmpty() && !selectedItem.isEmpty()) {
                if (selectedItem.equals("ชื่อ-นามสกุล"))
                {
                    try {
                        List<Student> foundStudent = studentList.searchStudentUnderAvisorByName(data,advisorName);
                        if (!foundStudent.isEmpty()) {
                            advisorInfoTable.getItems().clear();
                            advisorInfoTable.getItems().addAll(foundStudent);
                            dontHaveData.setVisible(true);
                            dontHaveData.setText("");
                        }
                        else {
                            dontHaveData.setVisible(true);

                            dontHaveData.setText("       ไม่พบข้อมูลนิสิตตามชื่อที่ค้นหา");
                            advisorInfoTable.getItems().clear();
                        }

                    }
                    catch (Exception e)
                    {
                        dontHaveData.setText("       ไม่พบข้อมูลนิสิตตามชื่อที่ค้นหา");

                    }
                }
                else if (selectedItem.equals("รหัสนิสิต"))
                {
                    try {
                        List<Student> foundStudent = studentList.searchStudentUnderAvisorByID(data,advisorName);
                        if (!foundStudent.isEmpty()) {
                            advisorInfoTable.getItems().clear();
                            advisorInfoTable.getItems().addAll(foundStudent);
                            dontHaveData.setText("");
                        }
                        else {
                            dontHaveData.setText("       ไม่พบข้อมูลนิสิตตามรหัสที่ค้นหา");
                            advisorInfoTable.getItems().clear();
                        }
                    }
                    catch (Exception e)
                    {
                        dontHaveData.setText("       ไม่พบข้อมูลนิสิตตามรหัสที่ค้นหา");
                    }
                }
            } else if(data.isEmpty() && !selectedItem.isEmpty()) {
                dontHaveData.setVisible(true);
                dontHaveData.setText("กรุณาใส่ชื่อหรือรหัสนิสิตเพื่อค้นหา");
                onListStudentButtonClick();

            }
        }
        catch (Exception e)
        {
            dontHaveData.setText(e.getMessage());
        }

    }



    @FXML
    public void editDataButtonClick() {
        try {
            FXRouter.goTo("set-profile",staff.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void onLogoutButtonClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}
