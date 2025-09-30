package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StaffListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class AdminManageStaffInfoController {
    @FXML private Label adminData;
    @FXML private TableView<Staff> staffTableView;
    @FXML private Circle profileAdminCircle;
    private User user;

    public void initialize() {

        Datasource<UserList> userdatasource = new UserListFileDatasource("data","user-list.csv");
        UserList userList = userdatasource.readData();
        Datasource<StaffList> staffdatasource = new StaffListFileDatasource("data", "staff-list.csv");
        StaffList staffList = staffdatasource.readData();

        user = userList.getAdmin();

        showTable(staffList);
        showInfo(user);

        staffTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Staff>() {
            @Override
            public void changed(ObservableValue observable, Staff oldValue, Staff newValue) {
                if (newValue != null) {
                    try {
                        FXRouter.goTo("edit-staff", newValue.getUsername());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
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



    private void showTable(StaffList staffList) {
        staffTableView.getColumns().clear();


        TableColumn<Staff, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุล");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));


        TableColumn<Staff, String> usernameColumn = new TableColumn<>("ชื่อบัญชีผู้ใช้");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));


        TableColumn<Staff, String> passwordColumn = new TableColumn<>("รหัสผ่านเริ่มต้น");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("defaultPassword"));


        TableColumn<Staff, String> facultyColumn = new TableColumn<>("คณะ");
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("faculty"));


        TableColumn<Staff, String> majorColumn = new TableColumn<>("ภาควิชา");
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));


        TableColumn<Staff, String> idColumn = new TableColumn<>("รหัสประจำตัว");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));



        nameColumn.setPrefWidth(120);
        usernameColumn.setPrefWidth(115);
        passwordColumn.setPrefWidth(115);
        facultyColumn.setPrefWidth(115);
        majorColumn.setPrefWidth(115);
        idColumn.setPrefWidth(114);


        staffTableView.getColumns().addAll(nameColumn,usernameColumn,passwordColumn,facultyColumn,majorColumn,idColumn);
        staffTableView.getItems().clear();


        for (Staff staff : staffList.getStaffs()) {
            staffTableView.getItems().add(staff);
        }

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
    public void onAdminAddStaffButton() {
        try {
            FXRouter.goTo("add-staff");
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
