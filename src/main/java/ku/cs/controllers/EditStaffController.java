package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.IOException;

public class EditStaffController {

    @FXML private Label nameLabel;
    @FXML private Label nameErrorLabel;
    @FXML private Label usernameLabel;
    @FXML private Label usernameErrorLabel;
    @FXML private Label idLabel;
    @FXML private Label idErrorLabel;
    @FXML private Label defaultPasswordLabel;
    @FXML private Label defaultPasswordErrorLabel;
    @FXML private Label facultyLabel;
    @FXML private Label majorLabel;
    @FXML private Label facultyErrorLabel;
    @FXML private Label majorErrorLabel;
    @FXML private Label infoTitle;
    @FXML private Label editInfoTitle;
    @FXML private Label errorLabel;
    @FXML private Label statusLabel;
    @FXML private TextField nameTextField;
    @FXML private TextField usernameTextField;
    @FXML private TextField defaultPasswordTextField;
    @FXML private TextField idTextField;
    @FXML private ChoiceBox<String> facultyChoice;
    @FXML private ChoiceBox<String> majorChoice;
    @FXML private Pane editPane;
    @FXML private VBox majorVbox;
    @FXML private VBox idVbox;
    @FXML private VBox editIdVbox;
    @FXML private VBox editMajorVbox;

    private Datasource<StaffList> staffListDatasource;
    private Datasource<UserList> userListDatasource;
    private StaffList staffList;
    private UserList userList;
    private Staff staff;

    private String[] roles = {"faculty staff", "major staff", "advisor"};
    private String role;


    @FXML
    public void initialize() {
        Datasource<FacultyList> facultyListDatasource = new FacultyListFileDatasource("data", "faculty-list.csv");
        Datasource<MajorList> majorListDatasource = new MajorListFileDatasource("data", "major-list.csv");
        staffListDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        FacultyList facultyList = facultyListDatasource.readData();
        MajorList majorList = majorListDatasource.readData();
        staffList = staffListDatasource.readData();
        userList = userListDatasource.readData();

        String staffUsername = (String) FXRouter.getData();
        staff = staffList.findStaffByUsername(staffUsername);
        role = staff.getRole();

        setEmpty();
        facultyChoice.getItems().addAll(facultyList.getFacultyNames());
        editPane.setVisible(false);
        if (role.equals(roles[0])) {
            facultyStaffDefault();
        } else if (role.equals(roles[1])) {
            majorStaffDefault();
        } else if (role.equals(roles[2])) {
            advisorDefault();
        }
        showStaff(staff);

        facultyChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldfaculty, String newFaculty) {
                majorChoice.getItems().clear();
                majorChoice.getItems().addAll(majorList.getMajorNamesByFaculty(newFaculty));
            }
        });
    }

    public void showStaff(Staff staff) {
        usernameLabel.setText(staff.getUsername());
        defaultPasswordLabel.setText(staff.getDefaultPassword());
        nameLabel.setText(staff.getName());
        facultyLabel.setText(staff.getFaculty());
        majorLabel.setText(staff.getMajor());
        idLabel.setText(staff.getId());
    }

    public void setEmpty() {
        nameErrorLabel.setText("");
        defaultPasswordErrorLabel.setText("");
        usernameErrorLabel.setText("");
        idErrorLabel.setText("");
        facultyErrorLabel.setText("");
        majorErrorLabel.setText("");
        idErrorLabel.setText("");
        errorLabel.setVisible(false);
        statusLabel.setText("");
    }

    public void facultyStaffDefault() {
        infoTitle.setText("ข้อมูลเจ้าหน้าที่คณะ");
        editInfoTitle.setText("แก้ไขข้อมูลเจ้าหน้าที่คณะ");
        majorVbox.setVisible(false);
        idVbox.setVisible(false);
        editMajorVbox.setVisible(false);
        editIdVbox.setVisible(false);

    }

    public void majorStaffDefault() {
        infoTitle.setText("ข้อมูลเจ้าหน้าที่ภาควิชา");
        editInfoTitle.setText("แก้ไขข้อมูลเจ้าหน้าที่ภาควิชา");
        majorVbox.setVisible(true);
        idVbox.setVisible(false);
        editMajorVbox.setVisible(true);
        editIdVbox.setVisible(false);
    }

    public void advisorDefault() {
        infoTitle.setText("ข้อมูลอาจารย์ที่ปรึกษา");
        editInfoTitle.setText("แก้ไขข้อมูลอาจารย์ที่ปรึกษา");
        majorVbox.setVisible(true);
        idVbox.setVisible(true);
        editMajorVbox.setVisible(true);
        editIdVbox.setVisible(true);
    }

    @FXML
    public void onEditButtonClick() {
        editPane.setVisible(true);
        nameTextField.setText(staff.getName());
        usernameTextField.setText(staff.getUsername());
        defaultPasswordTextField.setText(staff.getDefaultPassword());
        idTextField.setText(staff.getId());
        facultyChoice.setValue(staff.getFaculty());
        majorChoice.setValue(staff.getMajor());
    }

    @FXML
    public void onSubmitButtonClick() {
        String name = nameTextField.getText().trim();
        String defaultPassword = defaultPasswordTextField.getText().trim();
        String username = usernameTextField.getText().trim();
        String faculty;
        String major;
        String id;
        if (name.equals("")) {
            nameErrorLabel.setText("กรุณากรอกชื่อ-นามสกุล");
            return;
        }
        if (defaultPassword.equals("")) {
            defaultPasswordErrorLabel.setText("กรุณากรอกรหัสผ่านเริ่มต้น");
            return;
        }
        if (username.equals("")) {
            usernameErrorLabel.setText("กรุณากรอกชื่อบัญชีผู้ใช้");
            return;
        }
        try {
            faculty = facultyChoice.getValue();
            if (faculty == null) {
                facultyErrorLabel.setText("กรุณาเลือกคณะ");
                return;
            }
        } catch (NullPointerException e) {
            facultyErrorLabel.setText("กรุณาเลือกคณะ");
            return;
        }
        try {
            if (role.equals(roles[0])) {
                staffList.editFacultyStaff(staff.getUsername(), username, defaultPassword, name, faculty);
                userList.editAllInfo(staff.getUsername(), username, defaultPassword, name, "", faculty, "");
            } else if (role.equals(roles[1])) {
                try {
                    major = majorChoice.getValue();
                    if (major == null) {
                        majorErrorLabel.setText("กรุณาเลือกภาควิชา");
                        return;
                    }
                    staffList.editMajorStaff(staff.getUsername(), username, defaultPassword, name, faculty, major);
                    userList.editAllInfo(staff.getUsername(), username, defaultPassword, name, "", faculty, major);
                } catch (NullPointerException e) {
                    majorErrorLabel.setText("กรุณาเลือกภาควิชา");
                    return;
                }
            } else if (role.equals(roles[2])){
                id = idTextField.getText().trim();
                if (id.equals("")) {
                    idErrorLabel.setText("กรุณากรอกรหัสประจำตัว");
                    return;
                }
                try {
                    major = majorChoice.getValue();
                    if (major == null) {
                        majorErrorLabel.setText("กรุณาเลือกภาควิชา");
                        return;
                    }
                    staffList.editAdvisor(staff.getUsername(), username, defaultPassword, name, faculty, major, id);
                    userList.editAllInfo(staff.getUsername(), username, defaultPassword, name, id, faculty, major);
                } catch (NullPointerException e) {
                    majorErrorLabel.setText("กรุณาเลือกภาควิชา");
                    return;
                }
            }
        } catch (Exception e) {
            errorLabel.setVisible(true);
            return;
        }

        staffListDatasource.writeData(staffList);
        userListDatasource.writeData(userList);
        showStaff(staff);
        editPane.setVisible(false);
        setEmpty();
    }

    @FXML
    public void onCancelButtonClick() {
        editPane.setVisible(false);
        setEmpty();
    }

    @FXML
    public void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-manage-staff-info");
        } catch (IOException e) {
            statusLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
            statusLabel.setStyle("-fx-text-fill: red");
        }
    }
}
