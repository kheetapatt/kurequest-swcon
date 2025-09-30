package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;

import ku.cs.models.ApproverList;
import ku.cs.models.Staff;
import ku.cs.models.StaffList;
import ku.cs.services.ApproverListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StaffListFileDatasource;

import java.io.IOException;

public class AddApproverController{

    @FXML private Label statusLabel;
    @FXML private Label nameErrorMessage;
    @FXML private Label positionErrorMessage;
    @FXML private TextField nameTextField;
    @FXML private ChoiceBox<String> positionChoice;

    private Datasource<ApproverList> approverListDatasource;
    private ApproverList approverList;
    private String faculty;
    private String major;

    private String[] facultyApproverPositions = {"คณบดี","รักษาการแทนคณบดี","รองคณบดีฝ่ายวิจัยและวิเทศสัมพันธ์","รองคณบดีฝ่ายบริการวิชาการ",
            "รองคณบดีฝ่ายบริหาร","รองคณบดีฝ่ายพัฒนาองค์กร","รองคณบดีฝ่ายบริหารยุทธศาสตร์และนวัตกรรม","รองคณบดีฝ่ายภาพลักษณ์องค์กร",
            "รองคณบดีฝ่ายเทคโนโลยีดิจิทัล","รองคณบดีฝ่ายพัฒนานิสิต","รองคณบดีฝ่ายวิชาการ"};

    private String[] majorApproverPositions = {"หัวหน้าภาควิชา","รองหัวหน้าภาควิชา","รักษาการแทนหัวหน้าภาควิชา"};

    private Staff staff;
    private String currentRole;

    @FXML
    public void initialize() {
        approverListDatasource = new ApproverListFileDatasource("data", "approver-list.csv");
        Datasource<StaffList> staffDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        approverList = approverListDatasource.readData();
        StaffList staffList = staffDatasource.readData();

        String staffUsername = (String) FXRouter.getData();
        staff = staffList.findStaffByUsername(staffUsername);

        currentRole = staff.getRole();

        statusLabel.setText("");
        nameErrorMessage.setText("");
        positionErrorMessage.setText("");
        if (currentRole.equals("faculty staff")) {
            positionChoice.getItems().addAll(facultyApproverPositions);
            faculty = staff.getFaculty();
            major = "";
        } else if (currentRole.equals("major staff")){
            positionChoice.getItems().addAll(majorApproverPositions);
            faculty = staff.getFaculty();
            major = staff.getMajor();
        }
    }

    @FXML
    public void onBackButtonClick() {
        try {
            if (currentRole.equals("faculty staff")) {
                FXRouter.goTo("faculty-info-table", staff.getUsername() + "," + "approver");
            } else if (currentRole.equals("major staff")) {
                FXRouter.goTo("major-staff-info-table", staff.getUsername() + "," + "approver");
            }
        } catch (IOException e) {
            statusLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
            statusLabel.setStyle("-fx-text-fill: red");
        }
    }

    @FXML
    public void onAddButtonClick() {
        String name = nameTextField.getText().trim();

        if (name.equals("")) {
            nameErrorMessage.setText("กรุณากรอกชื่อผู้อนุมัติ");
            return;
        }
        try {
            String position = positionChoice.getValue();
            if (position == null) {
                positionErrorMessage.setText("กรุณาเลือกตำแหน่ง");
                return;
            }

            approverList.addApprover(name, position, faculty, major);
            approverListDatasource.writeData(approverList);
            nameTextField.setText("");
            positionChoice.setValue(null);
            statusLabel.setText("เพิ่มผู้อนุมัติสำเร็จ");
            nameErrorMessage.setText("");
            positionErrorMessage.setText("");
        } catch (NullPointerException e) {
            positionErrorMessage.setText("กรุณาเลือกตำแหน่ง");
        }
    }

    @FXML
    public void onCancelButtonClick() {
        nameTextField.setText("");
        positionChoice.setValue(null);
        statusLabel.setText("");
        nameErrorMessage.setText("");
        positionErrorMessage.setText("");
    }
}
