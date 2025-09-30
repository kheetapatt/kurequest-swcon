package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginController {
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private ImageView logo;
    @FXML private Label tryAgainLabel;

    private Datasource<UserList> userDatasource;
    private Datasource<StaffList> staffDatasource;
    private Datasource<StudentList> studentDatasource;
    private UserList userList;
    private StaffList staffList;
    private StudentList studentList;

    @FXML
    public void initialize() {
        Image imageLogo = new Image(getClass().getResource("/images/ku_logo.png").toString());
        Image imageBackground = new Image(getClass().getResource("/images/background.jpg").toString());

        logo.setImage(imageLogo);

        tryAgainLabel.setText("");

        userDatasource = new UserListFileDatasource("data", "user-list.csv");
        userList = userDatasource.readData();
        staffDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        staffList = staffDatasource.readData();
        studentDatasource = new StudentListFileDatasource("data", "student-list.csv");
        studentList = studentDatasource.readData();
    }

    @FXML
    public void onLogInButtonClick() {
        String username = usernameTextField.getText().trim();
        String password = passwordField.getText().trim();

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

        tryAgainLabel.setText("");

        if (username.isEmpty() || password.isEmpty()) {
            tryAgainLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
            return;
        }

        User user = userList.findUserByUsername(username);

        if (user == null) {
            tryAgainLabel.setText("กรุณาลงทะเบียนก่อนเข้าใช้งาน");
            return;
        }

        try {
            user = userList.login(username, password);
        } catch (Exception e) {
            tryAgainLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
        }

        Datasource<UserList> banUserListDatasource = new UserListFileDatasource("data", "banned-user-list.csv");
        UserList bannedUserList = banUserListDatasource.readData();

        if (user != null) {
            String role = user.getRole();
            if (bannedUserList.findUserByUsername(user.getUsername()) != null) {
                tryAgainLabel.setText("ไม่สามารถเข้าใช้งานได้");
                return;
            }

            try {
                if (role.equals("admin")) {
                    userList.setDate(user.getUsername(), date);
                    userDatasource.writeData(userList);
                    FXRouter.goTo("admin-manage-info-table");
                } else if (role.equals("faculty staff") || role.equals("major staff") || role.equals("advisor")) {
                    Staff staff = staffList.findStaffByUsername(username);
                    staffList.setDate(staff.getUsername(), date);
                    userList.setDate(staff.getUsername(), date);
                    staffDatasource.writeData(staffList);
                    userDatasource.writeData(userList);
                    if (staff != null && staff.isDefaultPassword()) {
                        FXRouter.goTo("edit-new-password-staff", staff.getUsername());
                    } else {
                        if (role.equals("faculty staff")) {
                            FXRouter.goTo("faculty-info-table", staff.getUsername());
                        } else if (role.equals("major staff")) {
                            FXRouter.goTo("major-staff-info-table", staff.getUsername());
                        } else if (role.equals("advisor")) {
                            FXRouter.goTo("advisor-info", staff.getUsername());
                        }
                    }
                } else if (role.equals("student")) {
                    Student student = studentList.findStudentByUsername(username);
                    studentList.setDate(student.getUsername(), date);
                    userList.setDate(student.getUsername(), date);
                    studentDatasource.writeData(studentList);
                    userDatasource.writeData(userList);
                    FXRouter.goTo("create-request-student", user.getUsername());
                }

            } catch (Exception e) {
                tryAgainLabel.setText("กรุณาลองใหม่อีกครั้ง");
                e.printStackTrace();
            }

        } else {
            tryAgainLabel.setText("ชื่อบัญชีผู้ใช้หรือรหัสผ่านไม่ถูกต้อง");
        }
    }

    @FXML
    public void onRegisterButton() {
        try {
            FXRouter.goTo("register");
        } catch (IOException e) {
            e.printStackTrace();
            tryAgainLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
        }
    }

    @FXML
    public void onDevButton() {
        try {
            FXRouter.goTo("dev-profile");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onInstructionButton(){
        try {
            FXRouter.goTo("instruction");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
