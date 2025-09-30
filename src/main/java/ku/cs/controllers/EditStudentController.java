package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.IOException;

public class EditStudentController {
    @FXML private Pane editPane;
    @FXML private TextField nameTextField;
    @FXML private TextField idTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField advisorTextField;
    @FXML private Label nameLabel;
    @FXML private Label idLabel;
    @FXML private Label emailLabel;
    @FXML private Label advisorLabel;
    @FXML private Label nameErrorLabel;
    @FXML private Label idErrorLabel;
    @FXML private Label emailErrorLabel;
    @FXML private Label errorLabel;
    @FXML private Label statusLabel;

    private Datasource<StudentList> studentFileDatasource;
    private Datasource<UserList> userFileDatasource;
    private StudentList studentList;
    private UserList userList;
    private Student student;
    private Staff staff;

    @FXML
    public void initialize() {
        studentFileDatasource = new StudentListFileDatasource("data", "student-list.csv");
        userFileDatasource = new UserListFileDatasource("data", "user-list.csv");
        Datasource<StaffList> staffListDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        studentList = studentFileDatasource.readData();
        userList = userFileDatasource.readData();
        StaffList staffList = staffListDatasource.readData();

        String passedData = (String) FXRouter.getData();
        String[] data = passedData.split(",");
        String studentId = data[0];
        String staffUsername = data[1];
        staff = staffList.findStaffByUsername(staffUsername);
        student = studentList.findStudentByID(studentId);
        showStudent(student);

        setEmpty();
    }

    public void showStudent(Student student) {
        nameLabel.setText(student.getName());
        idLabel.setText(student.getId());
        emailLabel.setText(student.getEmail());
        advisorLabel.setText(student.getAdvisor());
    }

    private void setEmpty() {
        editPane.setVisible(false);
        nameErrorLabel.setText("");
        idErrorLabel.setText("");
        emailErrorLabel.setText("");
        errorLabel.setVisible(false);
        statusLabel.setText("");
    }

    @FXML
    public void onEditButtonClick() {
        editPane.setVisible(true);
        nameTextField.setText(student.getName());
        idTextField.setText(student.getId());
        emailTextField.setText(student.getEmail());
        advisorTextField.setText(student.getAdvisor());
    }

    @FXML
    public void onSubmitButtonClick() {
        String name = nameTextField.getText().trim();
        String email = emailTextField.getText().trim();
        String id = idTextField.getText().trim();
        String advisor = advisorTextField.getText().trim();
        if (name.equals("")) {
            nameErrorLabel.setText("กรุณากรอกชื่อนิสิต");
            return;
        }
        if (email.equals("")) {
            emailErrorLabel.setText("กรุณากรอก Email นิสิต");
            return;
        }
        if (id.equals("")) {
            idErrorLabel.setText("กรุณากรอกรหัสนิสิต");
            return;
        }
        if (!id.equals(student.getId()) && studentList.findStudentByID(id) != null) {
            idErrorLabel.setText("มีรหัสนิสิตนี้ในระบบแล้ว");
            return;
        }
        studentList.editStudentInfo(student.getId(), id, name, email, advisor);
        userList.setId(student.getUsername(), id);
        userList.setName(student.getUsername(), name);
        userFileDatasource.writeData(userList);
        studentFileDatasource.writeData(studentList);
        showStudent(student);
        setEmpty();
    }

    @FXML
    public void onCancelButtonClick() {
        setEmpty();
    }

    @FXML
    public void onBackButtonClick() {
        try {
            FXRouter.goTo("major-staff-info-table", staff.getUsername() + "," + "student");
        } catch (IOException e) {
            statusLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
            statusLabel.setStyle("-fx-text-fill: red");
        }
    }
}
