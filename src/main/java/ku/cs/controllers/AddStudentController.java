package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.models.Staff;
import ku.cs.models.StaffList;
import ku.cs.models.StudentList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StaffListFileDatasource;
import ku.cs.services.StudentListFileDatasource;

import java.io.IOException;

public class AddStudentController {
    @FXML private TextField idTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField advisorTextField;
    @FXML private Label idErrorLabel;
    @FXML private Label nameErrorLabel;
    @FXML private Label emailErrorLabel;
    @FXML private Label statusLabel;

    private Datasource<StudentList> studentListDatasource;
    private StudentList studentList;

    private Staff staff;

    @FXML
    private void initialize() {
        studentListDatasource = new StudentListFileDatasource("data", "student-list.csv");
        studentList = studentListDatasource.readData();
        Datasource<StaffList> staffListDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        StaffList staffList = staffListDatasource.readData();

        String staffUsername = (String) FXRouter.getData();
        staff = staffList.findStaffByUsername(staffUsername);

        setEmpty();
        statusLabel.setText("");
    }

    private void setEmpty() {
        idErrorLabel.setText("");
        nameErrorLabel.setText("");
        emailErrorLabel.setText("");
        nameTextField.setText("");
        idTextField.setText("");
        emailTextField.setText("");
    }

    @FXML
    public void onAddButtonClick() {
        String name = nameTextField.getText().trim();
        String email = emailTextField.getText().trim();
        String id = idTextField.getText().trim();
        String advisor = advisorTextField.getText().trim();
        if (id.equals("")) {
            idErrorLabel.setText("กรุณากรอกรหัสนิสิต");
            return;
        }
        if (name.equals("")) {
            nameErrorLabel.setText("กรุณากรอกชื่อนิสิต");
            return;
        }
        if (email.equals("")) {
            emailErrorLabel.setText("กรุณากรอก Email นิสิต");
            return;
        }
        if (studentList.findStudentByID(id) != null) {
            idErrorLabel.setText("มีรหัสนิสิตนี้ในระบบแล้ว");
            return;
        }

        try {
            studentList.addStudent(name, id, staff.getFaculty(), staff.getMajor(), email, advisor);
            studentListDatasource.writeData(studentList);
        } catch (Exception e) {
            statusLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
            statusLabel.setStyle("-fx-text-fill: red");
            return;
        }
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
            FXRouter.goTo("major-staff-info-table", staff.getUsername() + "," + "student");
        } catch (IOException e) {
            statusLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
            statusLabel.setStyle("-fx-text-fill: red");
        }
    }
}
