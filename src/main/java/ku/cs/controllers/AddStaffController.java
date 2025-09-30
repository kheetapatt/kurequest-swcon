package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ku.cs.models.FacultyList;
import ku.cs.models.MajorList;
import ku.cs.models.StaffList;
import ku.cs.models.UserList;
import ku.cs.services.*;

import java.io.IOException;

public class AddStaffController {
    @FXML private TextField nameTextField;
    @FXML private TextField defaultPasswordTextField;
    @FXML private TextField usernameTextField;
    @FXML private TextField idTextField;
    @FXML private ChoiceBox<String> facultyChoice;
    @FXML private ChoiceBox<String> majorChoice;
    @FXML private ChoiceBox<String> roleChoice;
    @FXML private Label nameErrorLabel;
    @FXML private Label defaultPasswordErrorLabel;
    @FXML private Label usernameErrorLabel;
    @FXML private Label idErrorLabel;
    @FXML private Label facultyErrorLabel;
    @FXML private Label majorErrorLabel;
    @FXML private Label statusLabel;
    @FXML private Label addTitle;
    @FXML private VBox majorVbox;
    @FXML private VBox idVbox;

    private Datasource<StaffList> staffListDatasource;
    private Datasource<UserList> userListDatasource;
    private StaffList staffList;
    private UserList userList;
    private String[] roles = {"เจ้าหน้าที่คณะ","เจ้าหน้าที่ภาควิชา","อาจารย์ที่ปรึกษา"};
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

        facultyChoice.getItems().addAll(facultyList.getFacultyNames());
        roleChoice.getItems().addAll(roles);
        roleChoice.setValue(roles[0]);
        role = roles[0];
        facultyStaffSelectionDefault();
        setEmpty();
        statusLabel.setText("");

        roleChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldRole, String newRole) {
                role = newRole;
                onRoleSelected();
            }
        });

        facultyChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldfaculty, String newFaculty) {
                majorChoice.getItems().clear();
                majorChoice.getItems().addAll(majorList.getMajorNamesByFaculty(newFaculty));
            }
        });

    }

    public void setEmpty() {
        nameErrorLabel.setText("");
        defaultPasswordErrorLabel.setText("");
        usernameErrorLabel.setText("");
        idErrorLabel.setText("");
        facultyErrorLabel.setText("");
        majorErrorLabel.setText("");
        nameTextField.setText("");
        defaultPasswordTextField.setText("");
        usernameTextField.setText("");
        idTextField.setText("");
        facultyChoice.setValue(null);
        majorChoice.setValue(null);
    }


    public void onRoleSelected() {
        role = roleChoice.getValue();
        if (role.equals(roles[0])) {
            facultyStaffSelectionDefault();
        } else if (role.equals(roles[1])) {
            majorStaffSelectionDefault();
        } else if (role.equals(roles[2])){
            advisorSelectionDefault();
        }
    }

    public void facultyStaffSelectionDefault() {
        majorVbox.setVisible(false);
        idVbox.setVisible(false);
        addTitle.setText("เพิ่มเจ้าหน้าที่คณะ");
        statusLabel.setText("");
    }

    public void majorStaffSelectionDefault() {
        majorVbox.setVisible(true);
        idVbox.setVisible(false);
        addTitle.setText("เพิ่มเจ้าหน้าที่ภาควิชา");
        statusLabel.setText("");
    }

    public void advisorSelectionDefault() {
        majorVbox.setVisible(true);
        idVbox.setVisible(true);
        addTitle.setText("เพิ่มอาจารย์ที่ปรึกษา");
        statusLabel.setText("");
    }

    @FXML
    public void onAddButtonClick() {
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

            if (role.equals(roles[0])) {
                try {
                    staffList.addFacultyStaff(username, defaultPassword, name, faculty);
                    userList.addUser(username, defaultPassword, name, "", faculty, "", "faculty staff");
                } catch (Exception e) {
                    statusLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
                    statusLabel.setStyle("-fx-text-fill: red");
                    return;
                }
            } else if (role.equals(roles[1])) {
                try {
                    major = majorChoice.getValue();
                    if (major == null) {
                        majorErrorLabel.setText("กรุณาเลือกภาควิชา");
                        return;
                    }
                    try {
                        staffList.addMajorStaff(username, defaultPassword, name, faculty, major);
                        userList.addUser(username, defaultPassword, name, "", faculty, major, "major staff");
                    } catch (Exception e) {
                        statusLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
                        statusLabel.setStyle("-fx-text-fill: red");
                        return;
                    }
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
                    try {
                        staffList.addAdvisor(username, defaultPassword, name, faculty, major, id);
                        userList.addUser(username, defaultPassword, name, id, faculty, major, "advisor");
                    } catch (Exception e) {
                        statusLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
                        statusLabel.setStyle("-fx-text-fill: red");
                        return;
                    }
                } catch (NullPointerException e) {
                    majorErrorLabel.setText("กรุณาเลือกภาควิชา");
                    return;
                }
            }
        } catch (NullPointerException e) {
            facultyErrorLabel.setText("กรุณาเลือกคณะ");
            return;
        }
        staffListDatasource.writeData(staffList);
        userListDatasource.writeData(userList);
        statusLabel.setText("เพิ่มสำเร็จ");
        setEmpty();
    }

    @FXML
    public void onCancelButtonClick() {
        setEmpty();
        statusLabel.setText("");
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
 