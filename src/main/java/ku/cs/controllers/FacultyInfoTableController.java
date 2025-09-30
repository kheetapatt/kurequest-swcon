package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
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


public class FacultyInfoTableController {
    @FXML private Label staffData;
    @FXML private TableView<Object> staffTableView;
    @FXML private Button onApprover;
    @FXML private Button onListReqeustsButton;
    @FXML private Button onApproverButton;
    @FXML private Circle profileCircle;
    private RequestList requestList;
    private String imageDir;
    private Staff staff;

    private Button lastClickedButton;

    @FXML
    public void initialize()
    {
        Datasource<RequestList> requestsdatasource = new RequestListFileDatasource("data", "request-list.csv");
        requestList = requestsdatasource.readData();
        requestList.sort();
        Datasource<StaffList> staffdatasource = new StaffListFileDatasource("data","staff-list.csv");
        StaffList staffList = staffdatasource.readData();
        imageDir = "data/user-images/";
        lastClickedButton = onListReqeustsButton;

        String passedData = (String) FXRouter.getData();
        String[] passedDataSplit = passedData.split(",");
        if (passedDataSplit.length == 1) {
            staff = staffList.findStaffByUsername(passedDataSplit[0]);
            onListReqeustsButtonClick();
        } else if (passedDataSplit.length == 2) {
            staff = staffList.findStaffByUsername(passedDataSplit[0]);
            if (passedDataSplit[1].equals("approver")) {
                onApproverButtonClick();
            }
        }

        showInfo(staff);

        staffTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
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
            profileCircle.setFill(new ImagePattern(image));
        } catch (FileNotFoundException e) {
            try {
                imageStream = new FileInputStream(imageDir + "default-profile-image.png");
                image = new Image(imageStream);
                profileCircle.setFill(new ImagePattern(image));
            } catch (FileNotFoundException ex) {
                profileCircle.setFill(Color.WHITE);
            }
        }
    }


    private void showReqListTable(RequestList requestList, String facultyName) {
        onApprover.setVisible(false);
        staffTableView.getColumns().clear();


        TableColumn<Object, String> majorColumn = new TableColumn<>("ภาควิชา");
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));

        TableColumn<Object, String> typeColumn = new TableColumn<>("ประเภทคำร้อง");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));


        TableColumn<Object, String> reqColoumn = new TableColumn<>("ความประสงค์");
        reqColoumn.setCellValueFactory(new PropertyValueFactory<>("requestFor"));

        TableColumn<Object, String> dateColumn = new TableColumn<>("วัน/เวลา");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));


        majorColumn.setPrefWidth(173);
        typeColumn.setPrefWidth(175);
        reqColoumn.setPrefWidth(173);
        dateColumn.setPrefWidth(173);



        staffTableView.getColumns().addAll(majorColumn, typeColumn, reqColoumn, dateColumn);
        staffTableView.getItems().clear();



        for (Request request : requestList.findRequestByFacultyAndStatus(facultyName,"คำร้องส่งต่อให้คณบดี")) {
            staffTableView.getItems().addAll(request);
        }

    }

    private void showApproverTable(ApproverList approverList, String facultyName) {
        onApprover.setVisible(true);
        staffTableView.getColumns().clear();

        TableColumn<Object, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุล");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Object, String> positonColumn = new TableColumn<>("ตำแหน่ง");
        positonColumn.setCellValueFactory(obj -> {
            Approver approver = (Approver) obj.getValue();
            return new javafx.beans.property.SimpleObjectProperty<>(approver.getPositionWithFacultyOrMajor());
        });

        nameColumn.setPrefWidth(347);
        positonColumn.setPrefWidth(347);

        staffTableView.getColumns().addAll(nameColumn, positonColumn);
        staffTableView.getItems().clear();

        for (Approver approver : approverList.getApproversByFaculty(facultyName)) {
            staffTableView.getItems().add(approver);
        }

    }


    @FXML
    public void onListReqeustsButtonClick()
    {
        String facultyName = (staff.getFaculty());
        handleButtonClick(onListReqeustsButton);
        showReqListTable(requestList, facultyName);
    }

    @FXML
    public void onApproverButtonClick()
    {
        String facultyName = (staff.getFaculty());
        handleButtonClick(onApproverButton);
        Datasource<ApproverList> approverdatasource = new ApproverListFileDatasource("data", "approver-list.csv");
        ApproverList approverList = approverdatasource.readData();
        showApproverTable(approverList, facultyName);
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
    private void onApproveButtonClick()
    {
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
