package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.services.*;
import java.io.*;
import java.util.List;

public class StudentRequestHistoryAdvisorController {
    @FXML private Label nameLabel;
    @FXML private Circle imageCircle;
    @FXML private TextField searchTextField;
    @FXML private TableView<Request> requestsTable;
    @FXML private TableColumn<Request, String> dateColumn;
    @FXML private TableColumn<Request, String> typeColumn;
    @FXML private TableColumn<Request, String> statusColumn;

    private Student student;
    private RequestList requestList;
    private Staff staff;

    @FXML
    public void initialize() {
        Datasource<RequestList> requestListDatasource = new RequestListFileDatasource("data", "request-list.csv");
        Datasource<StudentList> studentDatasource = new StudentListFileDatasource("data", "student-list.csv");
        Datasource<StaffList> staffListDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        requestList = requestListDatasource.readData();
        StudentList studentList = studentDatasource.readData();
        StaffList staffList = staffListDatasource.readData();

        String passedData = (String) FXRouter.getData();
        String[] splitedPassedData = passedData.split(",");
        String studentUsername = splitedPassedData[0];
        String staffUsername = splitedPassedData[1];

        staff = staffList.findStaffByUsername(staffUsername);
        student = studentList.findStudentByUsername(studentUsername);
        studentProfile();

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        requestList.sort();

        String id = student.getId();
        List<Request> studentRequestList = requestList.getRequestsById(id);
        showTable(studentRequestList);

        requestsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Request>() {
            @Override
            public void changed(ObservableValue observable, Request oldValue, Request newValue) {
                if (newValue != null) {
                    try {
                        FXRouter.goTo("approve-request", newValue.getId() + "," + newValue.getDate() + "," + student.getUsername() + "," + staff.getUsername());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void showTable(List<Request> studentRequestList) {
        requestsTable.getItems().clear();
        ObservableList<Request> observableRequestList = FXCollections.observableArrayList(studentRequestList);
        requestsTable.setItems(observableRequestList);
    }

    public void studentProfile() {
        if (student != null) {
            nameLabel.setText(student.getName());

            String imagePath;
            imagePath = student.getImage();
            showImage(imagePath);
        }
    }

    private void showImage(String fileName) {
        String imageDir = "data/user-images/";
        InputStream imageStream;
        Image image;
        try {
            imageStream = new FileInputStream(imageDir + fileName);
            image = new Image(imageStream);
            imageCircle.setFill(new ImagePattern(image));
        } catch (FileNotFoundException e) {
            try {
                imageStream = new FileInputStream(imageDir + "default-profile-image.png");
                image = new Image(imageStream);
                imageCircle.setFill(new ImagePattern(image));
            } catch (FileNotFoundException ex) {
                imageCircle.setFill(Color.WHITE);
            }
        }

    }


    @FXML
    public void onSearchButton(){
        String searchText = searchTextField.getText().toLowerCase();
        String studentId = student.getId();

        List<Request> studentRequestList = requestList.getRequestsById(studentId);

        if (searchText != null && !searchText.isEmpty()) {
            List<Request> filteredRequests = requestList.getRequestsByIdWithTypeOrStatus(studentId, searchText);
            showTable(filteredRequests);
        } else {
            showTable(studentRequestList);
        }
    }

    @FXML
    public void onBackButton(){
        try {
            FXRouter.goTo("advisor-info", staff.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

