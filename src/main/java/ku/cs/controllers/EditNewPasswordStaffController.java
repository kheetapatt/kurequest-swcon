package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import ku.cs.models.Staff;
import ku.cs.models.UserList;
import ku.cs.models.StaffList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StaffListFileDatasource;
import ku.cs.services.UserListFileDatasource;

public class EditNewPasswordStaffController {
    @FXML private PasswordField password;
    @FXML private PasswordField confirmPassword;
    @FXML private Label error;

    private Datasource<UserList> userDatasource;
    private Datasource<StaffList> staffDatasource;
    private UserList userList;
    private StaffList staffList;
    private Staff staff;

    @FXML public void initialize() {
        userDatasource = new UserListFileDatasource("data", "user-list.csv");
        userList = userDatasource.readData();
        staffDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        staffList = staffDatasource.readData();

        String username = (String) FXRouter.getData();
        staff = staffList.findStaffByUsername(username);
        System.out.println(username);
        error.setText("");
    }

    @FXML public void onConfirmNewPassword() {
        String newPassword = password.getText();
        String confirmNewPassword = confirmPassword.getText();

        if (newPassword.equals("") || confirmNewPassword.equals("")) {
            error.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
        }

        if (newPassword.equals(confirmNewPassword)) {
            try {
                userList.setPassword(staff.getUsername(), staff.getDefaultPassword(), newPassword);
                userDatasource.writeData(userList);
                staffList.setPassword(staff.getUsername(), staff.getDefaultPassword(), newPassword);
                staffDatasource.writeData(staffList);
                FXRouter.goTo("login");
            } catch (Exception e) {
                error.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
                throw new RuntimeException(e);
            }
        } else {
            error.setText("ยืนยันรหัสผ่านไม่ถูกต้อง");
        }
    }

}
