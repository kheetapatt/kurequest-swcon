package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import ku.cs.models.Approver;
import ku.cs.models.ApproverList;
import ku.cs.models.Staff;
import ku.cs.models.StaffList;
import ku.cs.services.ApproverListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StaffListFileDatasource;

import java.io.IOException;

public class EditApproverController {

    @FXML private Pane editPane;
    @FXML private Label nameLabel;
    @FXML private Label positionLabel;
    @FXML private ChoiceBox<String> positionChoice;
    @FXML private Label nameErrorLabel;
    @FXML private Label positionErrorLabel;
    @FXML private Label statusLabel;
    @FXML private TextField nameTextField;

    private Datasource<ApproverList> approverListDatasource;
    private ApproverList approverList;
    private Approver approver;
    private Staff staff;

    private String[] facultyApproverPositions = {"คณบดี","รักษาการแทนคณบดี","รองคณบดีฝ่ายวิจัยและวิเทศสัมพันธ์","รองคณบดีฝ่ายบริการวิชาการ",
            "รองคณบดีฝ่ายบริหาร","รองคณบดีฝ่ายพัฒนาองค์กร","รองคณบดีฝ่ายบริหารยุทธศาสตร์และนวัตกรรม","รองคณบดีฝ่ายภาพลักษณ์องค์กร",
            "รองคณบดีฝ่ายเทคโนโลยีดิจิทัล","รองคณบดีฝ่ายพัฒนานิสิต","รองคณบดีฝ่ายวิชาการ"};

    private String[] majorApproverPositions = {"หัวหน้าภาควิชา","รองหัวหน้าภาควิชา","รักษาการแทนหัวหน้าภาควิชา"};

    @FXML
    public void initialize() {
        approverListDatasource = new ApproverListFileDatasource("data", "approver-list.csv");
        approverList = approverListDatasource.readData();
        Datasource<StaffList> staffListDatasource = new StaffListFileDatasource("data", "staff-list.csv");
        StaffList staffList = staffListDatasource.readData();

        String passedData = (String) FXRouter.getData();
        String[] data = passedData.split(",");
        String approverName = data[0];
        String staffUsername = data[1];

        staff = staffList.findStaffByUsername(staffUsername);
        approver = approverList.findApproverByName(approverName);
        showApprover(approver);

        editPane.setVisible(false);
        nameErrorLabel.setText("");
        positionErrorLabel.setText("");
        statusLabel.setText("");
        if (staff.getRole().equals("faculty staff")) {
            positionChoice.getItems().addAll(facultyApproverPositions);
        } else if (staff.getRole().equals("major staff")) {
            positionChoice.getItems().addAll(majorApproverPositions);
        }
    }


    public void showApprover(Approver approver) {
        nameLabel.setText(approver.getName());
        positionLabel.setText(approver.getPosition());
    }

    @FXML
    public void onEditButtonClick() {
        editPane.setVisible(true);
        nameTextField.setText(approver.getName());
        positionChoice.setValue(approver.getPosition());
    }

    @FXML
    public void onSubmitButtonClick() {
        String name = nameTextField.getText().trim();

        if (name.equals("")) {
            nameErrorLabel.setText("กรุณากรอกชื่อผู้อนุมัติ");
            return;
        }
        try {
            String position = positionChoice.getValue();
            if (position == null) {
                positionErrorLabel.setText("กรุณาเลือกตำแหน่ง");
                return;
            }

            approverList.editName(approver.getName(), name);
            approverList.editPosition(approver.getName(), position);
            approverListDatasource.writeData(approverList);
            showApprover(approver);
            nameErrorLabel.setText("");
            positionErrorLabel.setText("");
            editPane.setVisible(false);
        } catch (NullPointerException e) {
            positionErrorLabel.setText("กรุณาเลือกตำแหน่ง");
        }
    }

    @FXML
    public void onCancelButtonClick() {
        nameErrorLabel.setText("");
        positionErrorLabel.setText("");
        editPane.setVisible(false);
    }

    @FXML
    public void onBackButtonClick() {
        try {
            if (staff.getRole().equals("faculty staff")) {
                FXRouter.goTo("faculty-info-table", staff.getUsername() + "," + "approver");
            } else if (staff.getRole().equals("major staff")){
                FXRouter.goTo("major-staff-info-table", staff.getUsername() + "," + "approver");
            }
        } catch (IOException e) {
            statusLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
            statusLabel.setStyle("-fx-text-fill: red");
        }
    }
}
