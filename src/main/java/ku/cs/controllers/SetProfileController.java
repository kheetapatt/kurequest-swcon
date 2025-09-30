package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.*;

public class SetProfileController {
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label idLabel;
    @FXML private Label facultyLabel;
    @FXML private Label majorLabel;
    @FXML private Label emailLabel;
    @FXML private Label advisorLabel;
    @FXML private Label statusLabel;
    @FXML private Label oldPasswordError;
    @FXML private Label newPasswordError;
    @FXML private Label imageError;
    @FXML private Label majorTitle;
    @FXML private Label idTitle;
    @FXML private Label emailTitle;
    @FXML private Label advisorTitle;
    @FXML private TextField oldPasswordTextField;
    @FXML private TextField newPasswordTextField;
    @FXML private Pane editPasswordPane;
    @FXML private Rectangle imageRectangle;
    @FXML private VBox infoVbox;

    private Datasource<StudentList> studentListDatasource;
    private Datasource<UserList> userListDatasource;
    private Datasource<StaffList> staffListDatasource;
    private StudentList studentList;
    private UserList userList;
    private StaffList staffList;
    private User user;
    private String role;


    @FXML
    public void initialize() {
        studentListDatasource = new StudentListFileDatasource("data","student-list.csv");
        userListDatasource = new UserListFileDatasource("data","user-list.csv");
        staffListDatasource = new StaffListFileDatasource("data","staff-list.csv");
        studentList = studentListDatasource.readData();
        userList = userListDatasource.readData();
        staffList = staffListDatasource.readData();

        String username = (String) FXRouter.getData();
        user = userList.findUserByUsername(username);

        role = user.getRole();

        editPasswordPane.setVisible(false);
        oldPasswordError.setText("");
        newPasswordError.setText("");
        imageError.setText("");
        statusLabel.setText("");

        showInfo();
    }

    private void showInfo() {
        if (user != null) {
            if (role.equals("admin")) {
                showAdmin();
            } else if (role.equals("faculty staff")) {
                Staff staff = staffList.findStaffByUsername(user.getUsername());
                showFacultyStaff(staff);
            } else if (role.equals("major staff")) {
                Staff staff = staffList.findStaffByUsername(user.getUsername());
                showMajorStaff(staff);

            } else if (role.equals("advisor")) {
                Staff staff = staffList.findStaffByUsername(user.getUsername());
                showAdvisor(staff);
            } else {
                Student student = studentList.findStudentByUsername(user.getUsername());
                showStudent(student);
            }
        }
    }

    private void showAdmin() {
        infoVbox.setVisible(false);
        showImage(user.getImage());
    }

    private void showFacultyStaff(Staff staff) {
        majorTitle.setVisible(false);
        majorLabel.setVisible(false);
        idTitle.setVisible(false);
        idLabel.setVisible(false);
        emailTitle.setVisible(false);
        emailLabel.setVisible(false);
        advisorTitle.setVisible(false);
        advisorLabel.setVisible(false);

        nameLabel.setText(staff.getName());
        usernameLabel.setText(staff.getUsername());
        facultyLabel.setText(staff.getFaculty());
        showImage(staff.getImage());
    }

    private void showMajorStaff(Staff staff) {
        idTitle.setVisible(false);
        idLabel.setVisible(false);
        emailTitle.setVisible(false);
        emailLabel.setVisible(false);
        advisorTitle.setVisible(false);
        advisorLabel.setVisible(false);

        nameLabel.setText(staff.getName());
        usernameLabel.setText(staff.getUsername());
        facultyLabel.setText(staff.getFaculty());
        majorLabel.setText(staff.getMajor());
        showImage(staff.getImage());
    }

    private void showAdvisor(Staff staff) {
        emailTitle.setVisible(false);
        emailLabel.setVisible(false);
        advisorTitle.setVisible(false);
        advisorLabel.setVisible(false);

        nameLabel.setText(staff.getName());
        usernameLabel.setText(staff.getUsername());
        facultyLabel.setText(staff.getFaculty());
        majorLabel.setText(staff.getMajor());
        idLabel.setText(staff.getId());
        showImage(staff.getImage());
    }

    private void showStudent(Student student) {
        nameLabel.setText(student.getName());
        usernameLabel.setText(student.getUsername());
        facultyLabel.setText(student.getFaculty());
        majorLabel.setText(student.getMajor());
        idLabel.setText(student.getId());
        emailLabel.setText(student.getEmail());
        advisorLabel.setText(student.getAdvisor());
        showImage(student.getImage());
    }

    private void showImage(String fileName) {
        InputStream imageStream;
        Image image;
        try {
            imageStream = new FileInputStream("data/user-images/" + fileName);
            image = new Image(imageStream);
            imageRectangle.setFill(new ImagePattern(image));
        } catch (FileNotFoundException e) {
            try {
                imageStream = new FileInputStream("data/user-images/" + "default-profile-image.png");
                image = new Image(imageStream);
                imageRectangle.setFill(new ImagePattern(image));
            } catch (FileNotFoundException ex) {
                imageRectangle.setFill(Color.WHITE);
            }
        }
    }


    @FXML
    public void onEditImageButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        File inputFile = fileChooser.showOpenDialog(new Stage());
        if (inputFile != null) {
            ImageSaver imageSaver = new ImageSaver("data/user-images/");
            try {
                imageSaver.saveImage(inputFile);

                if (role.equals("student")) {
                    studentList.setImage(user.getUsername(), inputFile.getName());
                    studentListDatasource.writeData(studentList);
                } else if (role.equals("faculty staff") || role.equals("major staff") || role.equals("advisor")) {
                    staffList.setImage(user.getUsername(), inputFile.getName());
                    staffListDatasource.writeData(staffList);
                }
                userList.setImage(user.getUsername(), inputFile.getName());
                userListDatasource.writeData(userList);

            } catch (Exception e) {
                imageError.setText("ไม่สามารถเปลี่ยนภาพ Profile ได้");
            }
            showInfo();
        }
    }

    @FXML
    public void onEditPasswordButtonClick() {
        editPasswordPane.setVisible(true);
    }

    @FXML
    public void onSubmitPasswordButtonClick() {
        String oldPassword = oldPasswordTextField.getText().trim();
        String newPassword = newPasswordTextField.getText().trim();
        if (oldPassword.equals("")) {
            oldPasswordError.setText("กรุณากรอกรหัสผ่านเดิม");
            return;
        }
        if (newPassword.equals("")) {
            newPasswordError.setText("กรุณากรอกรหัสผ่านใหม่");
            return;
        }
        try {
            if (role.equals("student")) {
                if (studentList.setPassword(user.getUsername(), oldPassword, newPassword)) {
                    studentListDatasource.writeData(studentList);
                } else {
                    oldPasswordError.setText("รหัสผ่านเดิมไม่ถูกต้อง");
                    return;
                }
            } else if (role.equals("faculty staff") || role.equals("major staff") || role.equals("advisor")) {
                if (staffList.setPassword(user.getUsername(), oldPassword, newPassword)) {
                    staffListDatasource.writeData(staffList);
                } else {
                    oldPasswordError.setText("รหัสผ่านเดิมไม่ถูกต้อง");
                    return;
                }
            }
        } catch (Exception e) {
            statusLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
        }

        try {
            userList.setPassword(user.getUsername(), oldPassword, newPassword);
        } catch (Exception e) {
            statusLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
        }
        userListDatasource.writeData(userList);
        onCancelPasswordButtonClick();
    }

    @FXML
    public void onCancelPasswordButtonClick() {
        oldPasswordTextField.setText("");
        newPasswordTextField.setText("");
        editPasswordPane.setVisible(false);
    }

    @FXML
    public void onBackButtonClick() {
        try {
            if (role.equals("admin")) {
                FXRouter.goTo("admin-manage-info-table");
            } else if (role.equals("faculty staff")) {
                FXRouter.goTo("faculty-info-table", user.getUsername());
            } else if (role.equals("major staff")) {
                FXRouter.goTo("major-staff-info-table", user.getUsername());
            } else if (role.equals("advisor")) {
                FXRouter.goTo("advisor-info", user.getUsername());
            } else {
                FXRouter.goTo("create-request-student", user.getUsername());
            }
        } catch (IOException e) {
            statusLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
        }
    }
}