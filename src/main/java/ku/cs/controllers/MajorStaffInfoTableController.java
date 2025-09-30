package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.*;
import ku.cs.services.*;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MajorStaffInfoTableController {
    @FXML private Label staffData;
    @FXML private Label dontHaveData;
    @FXML private Button onMajorReqButton;
    @FXML private Button onMajorApproverReqButton;
    @FXML private Button onMajorManageNisitButton;
    @FXML private Button onSearchData;
    @FXML private TableView<Object> majorTableView;
    @FXML private ChoiceBox<String> choiceBoxForStaff;
    @FXML private TextField giveData;
    @FXML private Circle profileStaffSecCircle;
    @FXML private Button addApproverButton;
    @FXML private Button addNisitButton;

    private StudentList studentList;
    private Staff staff;

    ObservableList<String> choices;
    private Button lastClickedButton;

    @FXML
    public void initialize()
    {
        choices = FXCollections.observableArrayList("กรุณาเลือก","ชื่อ-นามสกุล","รหัสนิสิต");
        dontHaveData.setVisible(false);
        lastClickedButton = onMajorReqButton;
        Datasource<StaffList> staffdatasource = new StaffListFileDatasource("data","staff-list.csv");
        StaffList staffList = staffdatasource.readData();

        String passedData = (String) FXRouter.getData();
        String[] passedDataSplit = passedData.split(",");
        if (passedDataSplit.length == 1) {
            staff = staffList.findStaffByUsername(passedDataSplit[0]);
            onMajorReqButtonClick();
        } else if (passedDataSplit.length == 2) {
            staff = staffList.findStaffByUsername(passedDataSplit[0]);
            if (passedDataSplit[1].equals("approver")) {
                onMajorApproverReqButtonClick();
            } else if (passedDataSplit[1].equals("student")) {
                onMajorManageNisitButtonClick();
            }
        }

        showInfo(staff);
        setupChoiceBoxListener();

        majorTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    try {
                        if (newValue instanceof Approver) {
                            Approver selectedApprover = (Approver) newValue;
                            FXRouter.goTo("edit-approver", selectedApprover.getName() + "," +staff.getUsername());
                        } else if (newValue instanceof Request) {
                            Request selectedRequest = (Request) newValue;
                            FXRouter.goTo("approve-request", selectedRequest.getId() + "," + selectedRequest.getDate() + "," + staff.getUsername());
                        } else if (newValue instanceof Student) {
                            Student selectedStudent = (Student) newValue;
                            FXRouter.goTo("edit-student", selectedStudent.getId() + "," + staff.getUsername());
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
            imageStream = new FileInputStream("data/user-images/" + staff.getImage());
            image = new Image(imageStream);
            profileStaffSecCircle.setFill(new ImagePattern(image));
        } catch (FileNotFoundException e) {
            try {
                imageStream = new FileInputStream("data/user-images/" + "default-profile-image.png");
                image = new Image(imageStream);
                profileStaffSecCircle.setFill(new ImagePattern(image));
            } catch (FileNotFoundException ex) {
                profileStaffSecCircle.setFill(Color.WHITE);
            }
        }
    }


    void showRequestTable(RequestList requestList, String majorStaff) {
        addNisitButton.setVisible(false);
        addApproverButton.setVisible(false);
        choiceBoxForStaff.setVisible(false);
        giveData.setVisible(false);
        onSearchData.setVisible(false);
        dontHaveData.setVisible(false);
        majorTableView.getColumns().clear();


        TableColumn<Object, String> idColumn = new TableColumn<>("รหัสนิสิต");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        TableColumn<Object, String> typeColumn = new TableColumn<>("ประเภทคำร้อง");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Object, String> reqColumn = new TableColumn<>("ความประสงค์");
        reqColumn.setCellValueFactory(new PropertyValueFactory<>("requestFor"));


        TableColumn<Object, String> dateColumn = new TableColumn<>("วัน/เวลา");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        idColumn.setPrefWidth(173);
        typeColumn.setPrefWidth(174);
        reqColumn.setPrefWidth(174);
        dateColumn.setPrefWidth(173);

        majorTableView.getColumns().addAll(idColumn,typeColumn,reqColumn,dateColumn) ;
        majorTableView.getItems().clear();

        for (Request request : requestList.findRequestByMajorAndStatus(majorStaff,"คำร้องส่งต่อให้หัวหน้าภาควิชา")) {
            majorTableView.getItems().add(request);
        }
    }

    private void showStudentTable(StudentList studentList,String majorStaff) {
        addNisitButton.setVisible(true);
        addApproverButton.setVisible(false);
        choiceBoxForStaff.setVisible(true);
        giveData.setVisible(true);
        onSearchData.setVisible(true);
        majorTableView.getColumns().clear();


        TableColumn<Object, String> idColumn = new TableColumn<>("รหัสนิสิต");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        TableColumn<Object, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุล");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));


        TableColumn<Object, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));


        TableColumn<Object, String> majorColumn = new TableColumn<>("ภาควิชา");
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));


        idColumn.setPrefWidth(173);
        nameColumn.setPrefWidth(174);
        emailColumn.setPrefWidth(174);
        majorColumn.setPrefWidth(173);



        majorTableView.getColumns().addAll(idColumn,nameColumn, emailColumn, majorColumn);
        majorTableView.getItems().clear();



        for (Student students : studentList.getStudentsByMajor(majorStaff)) {
            majorTableView.getItems().add(students);
        }


    }




    private void showApproverTable(ApproverList approverList,String majorStaff) {
        addNisitButton.setVisible(false);
        addApproverButton.setVisible(true);
        choiceBoxForStaff.setVisible(false);
        giveData.setVisible(false);
        onSearchData.setVisible(false);
        dontHaveData.setVisible(false);
        majorTableView.getColumns().clear();


        TableColumn<Object, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุล");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));


        TableColumn<Object, String> positonColumn = new TableColumn<>("ตำแหน่ง");
        positonColumn.setCellValueFactory(obj -> {
            Approver approver = (Approver) obj.getValue();
            return new javafx.beans.property.SimpleObjectProperty<>(approver.getPositionWithFacultyOrMajor());
        });

        nameColumn.setPrefWidth(347);
        positonColumn.setPrefWidth(347);

        majorTableView.getColumns().addAll(nameColumn,positonColumn);
        majorTableView.getItems().clear();


        for (Approver approvers : approverList.getApproversByMajor(majorStaff)) {
            majorTableView.getItems().add(approvers);
        }


    }

    @FXML
    public void onMajorReqButtonClick()
    {
        Datasource<RequestList> requestsdatasource = new RequestListFileDatasource("data", "request-list.csv");
        RequestList requestList = requestsdatasource.readData();
        requestList.sort();

        String majorStaff = staff.getMajor();

        handleButtonClick(onMajorReqButton);
        showRequestTable(requestList,majorStaff);

    }

    public void onMajorApproverReqButtonClick()
    {
        Datasource<ApproverList> approverdatasource = new ApproverListFileDatasource("data", "approver-list.csv");
        ApproverList approverList = approverdatasource.readData();

        String majorStaff = staff.getMajor();

        handleButtonClick(onMajorApproverReqButton);
        showApproverTable(approverList,majorStaff);
    }


    public void onMajorManageNisitButtonClick()
    {
        Datasource<StudentList> studentdatasource = new StudentListFileDatasource("data", "student-list.csv");
        studentList = studentdatasource.readData();

        String majorStaff = staff.getMajor();

        handleButtonClick(onMajorManageNisitButton);
        showStudentTable(studentList,majorStaff);
        setupChoiceBoxListener();
    }

    @FXML
    private void setupChoiceBoxListener() {
        String majorStaff = staff.getMajor();
        choiceBoxForStaff.setValue("กรุณาเลือก");
        choiceBoxForStaff.setItems(choices);
        choiceBoxForStaff.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    giveData.setText("");
                    dontHaveData.setText("");
                    showStudentTable(studentList,majorStaff);
                }
        );
    }


    @FXML
    public void onSearchDataClick() {

            String majorStaff = staff.getMajor();
            String selectedItem = choiceBoxForStaff.getValue();
            String data = giveData.getText();

        try {
            if (!data.isEmpty() && !selectedItem.isEmpty()) {
                if (selectedItem.equals("ชื่อ-นามสกุล")) {
                    try {
                        List<Student> foundStudent = studentList.searchStudenMajorByName(data, majorStaff);
                        if (!foundStudent.isEmpty()) {
                            majorTableView.getItems().clear();
                            majorTableView.getItems().addAll(foundStudent);
                            dontHaveData.setVisible(true);
                            dontHaveData.setText("");
                        } else {
                            dontHaveData.setVisible(true);
                            dontHaveData.setText("       ไม่พบข้อมูลนิสิตตามชื่อที่ค้นหา");
                            majorTableView.getItems().clear();
                        }

                    } catch (Exception e) {
                        dontHaveData.setText("       ไม่พบข้อมูลนิสิตตามชื่อที่ค้นหา");

                    }
                } else if (selectedItem.equals("รหัสนิสิต")) {
                    try {
                        List<Student> foundStudent = studentList.searchStudentMajorByID(data, majorStaff);
                        if (!foundStudent.isEmpty()) {
                            majorTableView.getItems().clear();
                            majorTableView.getItems().addAll(foundStudent);
                            dontHaveData.setText("");
                        } else {
                            dontHaveData.setText("       ไม่พบข้อมูลนิสิตตามรหัสที่ค้นหา");
                            majorTableView.getItems().clear();
                        }
                    } catch (Exception e) {
                        dontHaveData.setText("       ไม่พบข้อมูลนิสิตตามรหัสที่ค้นหา");
                    }
                }
            } else if (data.isEmpty() && !selectedItem.isEmpty()) {
                dontHaveData.setVisible(true);
                dontHaveData.setText("กรุณาใส่ชื่อหรือรหัสนิสิตเพื่อค้นหา");
                onMajorManageNisitButtonClick();

            }
        } catch (Exception e) {
            dontHaveData.setText(e.getMessage());
        }


    }

    @FXML
    private void handleButtonClick(Button clickedButton) {
        if(lastClickedButton != null)
        {
            lastClickedButton.setStyle("-fx-background-color: #006c67; -fx-background-radius: 3; -fx-text-fill:#ffffff;");
        }
        clickedButton.setStyle("-fx-background-color: #8b8d8b; -fx-background-radius: 3; -fx-text-fill: #ffffff;");

        lastClickedButton = clickedButton;

    }

    @FXML
    private void addNisitButtonClick(){
        try {
            FXRouter.goTo("add-student",staff.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void addApproverButtonClick() {
        try {
            FXRouter.goTo("add-approver",staff.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
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






