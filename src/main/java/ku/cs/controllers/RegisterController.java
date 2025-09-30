package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
import ku.cs.models.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.StudentListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class RegisterController {
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField nameTextField;
    @FXML private TextField studentIdTextField;
    @FXML private TextField emailTextField;
    @FXML private ChoiceBox<Integer> yearChoiceBox;
    @FXML private Label messageLabel;
    @FXML private ImageView logo;

    private StudentList studentList;
    private UserList userList;
    private StudentListFileDatasource studentListFileDatasource;
    private UserListFileDatasource userListFileDatasource;

    @FXML
    public void initialize() {
        studentListFileDatasource = new StudentListFileDatasource("data", "student-list.csv");
        studentList = studentListFileDatasource.readData();
        userListFileDatasource = new UserListFileDatasource("data", "user-list.csv");
        userList = userListFileDatasource.readData();
        messageLabel.setText("");
        yearChoiceBox.getItems().addAll(1, 2, 3, 4);
    }

    @FXML
    public void onConfirmRegistration() {
        String inputUsername = usernameTextField.getText();
        String inputPassword = passwordField.getText();
        String inputConfirmPassword = confirmPasswordField.getText();
        String inputName = nameTextField.getText();
        String inputStudentId = studentIdTextField.getText();
        String inputEmail = emailTextField.getText();
        Integer inputYear = yearChoiceBox.getValue();

        if (inputUsername.isEmpty() || inputPassword.isEmpty() || inputConfirmPassword.isEmpty() || inputConfirmPassword.isEmpty() || inputStudentId.isEmpty() || inputEmail.isEmpty() || inputYear == null) {
            messageLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
            return;
        }

        if (!inputPassword.equals(inputConfirmPassword)){
            messageLabel.setText("รหัสผ่านไม่ตรงกัน");
        }

        if (userList.findUserByUsername(inputUsername) != null){
            messageLabel.setText("username นี้ถูกใช้งานแล้ว");
        }

        Student existingStudent = studentList.findStudentByID(inputStudentId);

        if (existingStudent != null && existingStudent.getName().equals(inputName) && existingStudent.getEmail().equals(inputEmail) ) {
            existingStudent.setUsername(inputUsername);

            try {
                existingStudent.setPassword(inputPassword);
                existingStudent.setYear(inputYear);
                userList.addUser(inputUsername, inputPassword, inputName, inputStudentId,
                        existingStudent.getFaculty(), existingStudent.getMajor(), "student");
                userListFileDatasource.writeData(userList);
            } catch (Exception e) {
                messageLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
            }

            studentListFileDatasource.writeData(studentList);

            messageLabel.setText("ลงทะเบียนสำเร็จ");
            try {
                FXRouter.goTo("login");
            } catch (IOException e) {
                throw new RuntimeException();
            }

        } else {
            messageLabel.setText("ไม่มีรายชื่อในระบบ กรุณาติดต่อภาควิชา");
        }
    }

    @FXML
    public void onBackButton() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            messageLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
        }
    }

}
