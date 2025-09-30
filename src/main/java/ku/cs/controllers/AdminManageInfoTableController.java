package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class AdminManageInfoTableController {
    @FXML private Label adminData;
    @FXML private Label dontHaveData;
    @FXML private TextField giveData;
    @FXML private Button onAdminFacultyStaffButton;
    @FXML private Button onAdminMajorStaffButton;
    @FXML private Button onAdminAdvisorButton;
    @FXML private Button onAdminStudentButton;
    @FXML private Button onSearchDataButton;
    @FXML private TableView<User> infoTableView;
    @FXML private Circle profileAdminCircle;
    private Datasource<UserList> userdatasource;
    private UserList userList;
    private String imageDir;
    private User user;


    private Button lastClickedButton;

    @FXML
    public void initialize() {

        userdatasource = new UserListFileDatasource("data", "user-list.csv");
        userList = userdatasource.readData();
        imageDir = "data/user-images/";

        onAdminFacultyStaffButton.setStyle("-fx-background-color: #8b8d8b; -fx-background-radius: 3; -fx-text-fill: #ffffff;");

        user = userList.getAdmin();
        showInfo(user);
        lastClickedButton = onAdminFacultyStaffButton;

        String passedData = (String) FXRouter.getData();
        if (passedData != null) {
            if (passedData.equals("faculty staff")) {
                onAdminFacultyStaffButtonClick();
            } else if (passedData.equals("major staff")) {
                onAdminMajorStaffButtonClick();
            } else if (passedData.equals("advisor")) {
                onAdminAdvisorButtonClick();
            } else if (passedData.equals("student")) {
                onAdminStudentButtonClick();
            }
        } else {
            onAdminFacultyStaffButtonClick();
        }

        infoTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue observable, User oldValue, User newValue) {
                if (newValue != null) {
                    try {
                        FXRouter.goTo("ban-user", newValue.getUsername());
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
            imageStream = new FileInputStream(imageDir + user.getImage());
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


    private void showInfoTable(String roleFilter) {
        if (roleFilter.equals("SearchData"))
        {
            dontHaveData.setVisible(true);
        }
        else
        {
            dontHaveData.setVisible(false);
        }
        infoTableView.getColumns().clear();

        userList.sort();


        TableColumn<User, String> usernameColumn = new TableColumn<>("ชื่อบัญชีผู้ใช้");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));


        TableColumn<User, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุล");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));


        TableColumn<User, String> dateColumn = new TableColumn<>("วัน/เวลา");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));


        TableColumn<User, Circle> imageColumn = new TableColumn<>("รูปภาพ");
        imageColumn.setCellValueFactory(user ->{
            Circle circle = new Circle();
            try
            {
                circle.setFill(new ImagePattern(new Image(new FileInputStream(imageDir + user.getValue().getImage()))));
            }
            catch (FileNotFoundException e)
            {
                circle.setFill(Color.GRAY);
            }
            if(circle !=  null){
                circle.setRadius(33);
                circle.setStroke(Color.GRAY);
                circle.setStrokeWidth(1.5);
            }
            return new javafx.beans.property.SimpleObjectProperty<>(circle);
        });


        usernameColumn.setPrefWidth(204);
        nameColumn.setPrefWidth(191);
        dateColumn.setPrefWidth(191);
        imageColumn.setPrefWidth(108);
        imageColumn.setStyle("-fx-alignment: CENTER;");


        infoTableView.getColumns().addAll(imageColumn, usernameColumn, nameColumn, dateColumn);
        infoTableView.getItems().clear();



        for (User user : userList.getUsersByRole(roleFilter)) {
            infoTableView.getItems().add(user);
        }

    }




    @FXML
    public void onAdminFacultyStaffButtonClick()
    {
        handleButtonClick(onAdminFacultyStaffButton, "FacultyStaff");
        showInfoTable("faculty staff");

    }

    @FXML
    public void onAdminMajorStaffButtonClick()
    {
        handleButtonClick(onAdminMajorStaffButton, "MajorStaff");
        showInfoTable("major staff");
    }

    @FXML
    public void onAdminAdvisorButtonClick()
    {
        handleButtonClick(onAdminAdvisorButton, "Advisor");
        showInfoTable("advisor");
    }

    @FXML
    public void onAdminStudentButtonClick()
    {
        handleButtonClick(onAdminStudentButton, "Student");
        showInfoTable("student");
    }

    public void onSearchDataButtonClick()
    {
        String searchData = giveData.getText();
        handleButtonClick(onSearchDataButton, "SearchData");
        showInfoTable("SearchData");
        try {
            if (!searchData.isEmpty()) {
                infoTableView.getItems().clear();
                boolean found = false;

                for (User user : userList.getUsers()) {
                    if (user.getUsername().equalsIgnoreCase(searchData) || user.getName().contains(searchData)) {
                        infoTableView.getItems().add(user);
                        found = true;
                        onAdminFacultyStaffButton.setStyle("-fx-background-color: #006c67; -fx-background-radius: 3; -fx-text-fill: #ffffff;");
                        onAdminMajorStaffButton.setStyle("-fx-background-color: #006c67; -fx-background-radius: 3; -fx-text-fill: #ffffff;");
                        onAdminAdvisorButton.setStyle("-fx-background-color: #006c67; -fx-background-radius: 3; -fx-text-fill: #ffffff;");
                        onAdminStudentButton.setStyle("-fx-background-color: #006c67; -fx-background-radius: 3; -fx-text-fill: #ffffff;");
                    }
                }

                if (!found) {
                    dontHaveData.setText("           ไม่พบข้อมูลที่ตรงกัน");
                    onAdminFacultyStaffButton.setStyle("-fx-background-color: #006c67; -fx-background-radius: 3; -fx-text-fill: #ffffff;");
                    onAdminMajorStaffButton.setStyle("-fx-background-color: #006c67; -fx-background-radius: 3; -fx-text-fill: #ffffff;");
                    onAdminAdvisorButton.setStyle("-fx-background-color: #006c67; -fx-background-radius: 3; -fx-text-fill: #ffffff;");
                    onAdminStudentButton.setStyle("-fx-background-color: #006c67; -fx-background-radius: 3; -fx-text-fill: #ffffff;");

                } else {
                    dontHaveData.setText("");
                }
            } else
            {

                onAdminFacultyStaffButtonClick();
                dontHaveData.setVisible(true);
                dontHaveData.setText("กรุณากรอกข้อมูลเพื่อค้นหา");

            }

        } catch (Exception e) {
            dontHaveData.setText(e.getMessage());
        }

    }



    @FXML
    private void handleButtonClick(Button clickedButton,String facultyName) {

        if (clickedButton != onSearchDataButton) {
            if (lastClickedButton != null && !facultyName.equals("SearchData") && !dontHaveData.equals("           ไม่พบข้อมูลที่ตรงกัน")) {
                lastClickedButton.setStyle("-fx-background-color:  #006c67; -fx-background-radius: 3; -fx-text-fill:#ffffff;");
            }
            clickedButton.setStyle("-fx-background-color: #8b8d8b; -fx-background-radius: 3; -fx-text-fill: #ffffff;");
            lastClickedButton = clickedButton;
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