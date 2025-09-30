package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class AdminManageFacultyAndMajorInfoController {
    @FXML private Label adminData;
    @FXML private Button onAdminManageFacultyInfoButton;
    @FXML private Button onAdminManageMajorInfoButton;
    @FXML private TableView<Object> FacultyAndMajorTableView;
    @FXML private Circle profileAdminCircle;

    private Button lastClickedButton;
    private User user;


    @FXML
    public void initialize()
    {
        Datasource<UserList> userdatasource = new UserListFileDatasource("data","user-list.csv");
        UserList userList = userdatasource.readData();

        onAdminManageFacultyInfoButton.setStyle("-fx-background-color: #8b8d8b; -fx-background-radius: 3; -fx-text-fill: #ffffff;");
        user = userList.getAdmin();
        showInfo(user);
        lastClickedButton = onAdminManageFacultyInfoButton;

        String passedData = (String) FXRouter.getData();
        if (passedData != null) {
            if (passedData.equals("faculty")) {
                onAdminManageFacultyInfoButtonClick();
            } else if (passedData.equals("major")) {
                onAdminManageMajorInfoButtonClick();
            }
        } else {
            onAdminManageFacultyInfoButtonClick();
        }

    }

    private void showInfo(User user) {
        adminData.setText(user.getName());
        InputStream imageStream;
        Image image;
        try {
            imageStream = new FileInputStream("data/user-images/" + user.getImage());
            image = new Image(imageStream);
            profileAdminCircle.setFill(new ImagePattern(image));
        } catch (FileNotFoundException e) {
            try {
                imageStream = new FileInputStream("data/user-images/" + "default-profile-image.png");
                image = new Image(imageStream);
                profileAdminCircle.setFill(new ImagePattern(image));
            } catch (FileNotFoundException ex) {
                profileAdminCircle.setFill(Color.WHITE);
            }
        }
    }



    private void showFacultyTable(FacultyList facultyList) {
        FacultyAndMajorTableView.getColumns().clear();

        TableColumn<Object, String> nameColumn = new TableColumn<>("ชื่อคณะ");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));


        TableColumn<Object, String> codeColumn = new TableColumn<>("รหัสคณะ");
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("id"));


        FacultyAndMajorTableView.getColumns().addAll(nameColumn, codeColumn);
        FacultyAndMajorTableView.getItems().clear();


        nameColumn.setPrefWidth(347);
        codeColumn.setPrefWidth(347);


        for (Faculty faculty : facultyList.getFaculties()) {
            FacultyAndMajorTableView.getItems().add(faculty);
        }

        FacultyAndMajorTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    try {
                        FXRouter.goTo("edit-faculty-and-major", newValue);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }


    private void showMajorTable(MajorList majorList){
        FacultyAndMajorTableView.getColumns().clear();

        TableColumn<Object, String> nameColumn = new TableColumn<>("ชื่อภาควิชา");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Object, String> codeColumn = new TableColumn<>("รหัสภาควิชา");
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Object, String> departmentColumn = new TableColumn<>("คณะสังกัด");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("faculty"));


        nameColumn.setPrefWidth(232);
        codeColumn.setPrefWidth(231);
        departmentColumn.setPrefWidth(231);


        FacultyAndMajorTableView.getItems().clear();
        FacultyAndMajorTableView.getColumns().addAll(nameColumn, codeColumn, departmentColumn);


        for (Major major : majorList.getMajors()) {
            FacultyAndMajorTableView.getItems().add(major);
        }

        FacultyAndMajorTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    try {
                        FXRouter.goTo("edit-faculty-and-major", newValue);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @FXML
    public void onAdminManageFacultyInfoButtonClick()
    {
        handleButtonClick(onAdminManageFacultyInfoButton);
        Datasource<FacultyList> facultydatasource = new FacultyListFileDatasource("data","faculty-list.csv");
        FacultyList facultyList = facultydatasource.readData();
        showFacultyTable(facultyList);

    }

    @FXML public void onAdminManageMajorInfoButtonClick()
    {
        handleButtonClick(onAdminManageMajorInfoButton);
        Datasource<MajorList> majordatasource = new MajorListFileDatasource("data","major-list.csv");
        MajorList majorList = majordatasource.readData();
        showMajorTable(majorList);
    }

    @FXML
    private void handleButtonClick(Button clickedButton)
    {
        if(lastClickedButton != null)
        {
            lastClickedButton.setStyle("-fx-background-color:  #006c67; -fx-background-radius: 3; -fx-text-fill:#ffffff;");
        }
        clickedButton.setStyle("-fx-background-color: #8b8d8b; -fx-background-radius: 3; -fx-text-fill: #ffffff;");

        lastClickedButton = clickedButton;
    }


    @FXML
    public void onAdminButton() {
        try {
            FXRouter.goTo("admin-manage-info-table");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void onEditFacultyAndMajorInfoButton() {
        try {
            FXRouter.goTo("admin-manage-faculty-and-major-info");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onStaffInfoButton() {
        try {
            FXRouter.goTo("admin-manage-staff-info");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void onAdminAddFacultyButton(){
        try {
            FXRouter.goTo("add-faculty-and-major");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void editDataButtonClick() {
        try {
            FXRouter.goTo("set-profile", user.getUsername());
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




