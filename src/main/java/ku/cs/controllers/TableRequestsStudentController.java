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

public class TableRequestsStudentController {
    @FXML private Circle imageCircle;
    @FXML private Label nameLabel;
    @FXML private TextField searchTextField;
    @FXML private TableView<Request> requestsTable;
    @FXML private TableColumn<Request, String> dateColumn;
    @FXML private TableColumn<Request, String> typeColumn;
    @FXML private TableColumn<Request, String> statusColumn;
    @FXML private TableColumn<Request, String> processColumn;

    private Student student;
    private RequestList requestList;

    @FXML
    public void initialize() {
        Datasource<RequestList> datasource = new RequestListFileDatasource("data", "request-list.csv");
        Datasource<StudentList> studentDatasource = new StudentListFileDatasource("data", "student-list.csv");
        requestList = datasource.readData();
        StudentList studentList = studentDatasource.readData();

        String username = (String) FXRouter.getData();
        student = studentList.findStudentByUsername(username);
        studentProfile();

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        processColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusFromApprover"));

        System.out.println("total requests: " + requestList.getRequests().size());

        requestList.sort();

        String id = student.getId();
        List<Request> studentRequestList = requestList.getRequestsById(id);
        showTable(studentRequestList);

        requestsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Request>() {
            @Override
            public void changed(ObservableValue observable, Request oldValue, Request newValue) {
                if (newValue != null) {
                    try {
                        FXRouter.goTo("approve-request", newValue.getId() + "," + newValue.getDate() + "," + student.getUsername());
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
            showImage(student.getImage());
        }
    }

    private void showImage(String fileName) {
        InputStream imageStream;
        Image image;
        try {
            imageStream = new FileInputStream("data/user-images/" + fileName);
            image = new Image(imageStream);
            imageCircle.setFill(new ImagePattern(image));
        } catch (FileNotFoundException e) {
            try {
                imageStream = new FileInputStream("data/user-images/" + "default-profile-image.png");
                image = new Image(imageStream);
                imageCircle.setFill(new ImagePattern(image));
            } catch (FileNotFoundException ex) {
                imageCircle.setFill(Color.WHITE);
            }
        }
    }

    @FXML
    public void onEditStudentButton() {
        try {
            FXRouter.goTo("set-profile", student.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    public void onCreateRequestButton() {
        try {
            FXRouter.goTo("create-request-student", student.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onStatusRequestButton() {
        try {
            FXRouter.goTo("table-request-student", student.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void onLogoutButton() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
