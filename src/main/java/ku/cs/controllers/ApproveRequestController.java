package ku.cs.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import ku.cs.models.*;
import ku.cs.services.*;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class  ApproveRequestController {

    @FXML
    private Button downloadButton;
    @FXML
    private Button fileApproveButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Circle colorStatusCircle;

    @FXML private Button approveButton;
    @FXML private Button declineButton;

    @FXML
    private TextField declineTextField;
    @FXML private Label approverLabel;
    @FXML private Label declineReasonLabel;
    @FXML private VBox declineReasonVbox;

    @FXML private Label newRequestStatus;
    @FXML private Label statusFromAdvisor;
    @FXML private Label statusFromMajor;
    @FXML private Label statusFromFaculty;
    @FXML private Label newRequestStatusDate;
    @FXML private Label statusFromAdvisorDate;
    @FXML private Label statusFromMajorDate;
    @FXML private Label statusFromFacultyDate;


    private Request request;
    private Datasource<RequestList> requestsDatasource;
    private RequestList requestList;
    private boolean fileSaved;
    private User user;
    private boolean isStudentRequestHistory;
    private String advisorUsername;

    @FXML
    private ChoiceBox<String> approverChoiceBox;
    @FXML
    private Label errorLabel;
    @FXML private ImageView imageView;
    @FXML private ScrollPane pdfScrollPane;
    @FXML private Button dowloadRequestButton;

    @FXML
    public void initialize() {
        requestsDatasource = new RequestListFileDatasource("data", "request-list.csv");
        requestList = requestsDatasource.readData();
        Datasource<UserList> userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        UserList userList = userListDatasource.readData();

        String passedData = (String) FXRouter.getData();
        String[] splitPassedData = passedData.split(",");
        String requestId = splitPassedData[0];
        String requestDate = splitPassedData[1];
        String username = splitPassedData[2];
        if (splitPassedData.length == 4) {
            advisorUsername = splitPassedData[3];
            isStudentRequestHistory = true;
        }

        user = userList.findUserByUsername(username);
        String role = user.getRole();
        request = requestList.getSpecificRequest(requestId, requestDate);
        colorStatusCircle.setFill(Color.GRAY);
        statusLabel.setText("รอดำเนินการ");

        Datasource<ApproverList> approverListDatasource = new ApproverListFileDatasource("data", "approver-list.csv");
        ApproverList approverList = approverListDatasource.readData();
        String[] approverListChoice = null;

        showPDF(request.getPdfPath());
        setVisibility(false, errorLabel);
        declineReasonVbox.setVisible(false);
        fileSaved = false;
        if (role.equals("faculty staff")) {
            approverListChoice = approverList.getApproversPositionWithNameByFaculty(user.getFaculty());
            setVisibility(true, fileApproveButton, approverChoiceBox, approverLabel,approveButton,declineButton, declineTextField,downloadButton,dowloadRequestButton);
        } else if (role.equals("major staff")) {
            approverListChoice = approverList.getApproversPositionWithNameByMajor(user.getMajor());
            setVisibility(true, fileApproveButton, approverChoiceBox, approverLabel,approveButton,declineButton, declineTextField,downloadButton,dowloadRequestButton);
        }else if (role.equals("advisor")) {
            setVisibility(false, approverChoiceBox, approverLabel);
        } else if(role.equals("student"))
        {
            setStatus();
            setVisibility(false, fileApproveButton, approverChoiceBox, approverLabel,approveButton,declineButton, declineTextField,downloadButton,dowloadRequestButton);

        }else{
            setVisibility(false, fileApproveButton, approverChoiceBox, approverLabel);
        }
        if (approverListChoice != null) {
            approverChoiceBox.getItems().addAll(approverListChoice);
        }
        setDownloadFileButton();
        setStatusDates();
    }

    private void setStatusDates() {
        RequestStatusDatesFileDatasource requestStatusDatesFileDatasource = new RequestStatusDatesFileDatasource("data", "request-status-dates.csv");
        HashMap<String, String[]> requestStatusDates = requestStatusDatesFileDatasource.readData();

        newRequestStatus.setText("สร้างคำร้อง");
        statusFromAdvisor.setText("อนุมัติโดยอาจารย์ที่ปรึกษา");
        statusFromMajor.setText("อนุมัติโดยหัวหน้าภาควิชา");
        statusFromFaculty.setText("อนุมัติโดยคณบดี");
        newRequestStatusDate.setText(requestStatusDates.get(request.getPdfPath())[1]);
        statusFromAdvisorDate.setText(requestStatusDates.get(request.getPdfPath())[2]);
        statusFromMajorDate.setText(requestStatusDates.get(request.getPdfPath())[3]);
        statusFromFacultyDate.setText(requestStatusDates.get(request.getPdfPath())[4]);
        if (request.getStatusFromApprover().equals("ใบคำร้องใหม่")) {
            statusFromAdvisor.setText("");
            statusFromAdvisorDate.setText("");
            statusFromMajor.setText("");
            statusFromFaculty.setText("");
            statusFromMajorDate.setText("");
            statusFromFacultyDate.setText("");
        } else if (request.getStatusFromApprover().equals("อนุมัติโดยอาจารย์ที่ปรึกษา")) {
            statusFromMajor.setText("");
            statusFromFaculty.setText("");
            statusFromMajorDate.setText("");
            statusFromFacultyDate.setText("");
        } else if (request.getStatusFromApprover().equals("อนุมัติโดยหัวหน้าภาควิชา")) {
            statusFromFaculty.setText("");
            statusFromFacultyDate.setText("");
        } else if (request.getStatusFromApprover().equals("ปฏิเสธโดยอาจารย์ที่ปรึกษา")) {
            statusFromAdvisor.setText("ปฏิเสธโดยอาจารย์ที่ปรึกษา");
            statusFromMajor.setText("");
            statusFromFaculty.setText("");
            statusFromMajorDate.setText("");
            statusFromFacultyDate.setText("");
            declineReasonLabel.setText(requestStatusDates.get(request.getPdfPath())[0]);
        } else if (request.getStatusFromApprover().equals("ปฏิเสธโดยหัวหน้าภาควิชา")) {
            statusFromMajor.setText("ปฏิเสธโดยหัวหน้าภาควิชา");
            statusFromFaculty.setText("");
            statusFromFacultyDate.setText("");
            declineReasonLabel.setText(requestStatusDates.get(request.getPdfPath())[0]);
        } else if (request.getStatusFromApprover().equals("ปฏิเสธโดยคณบดี")) {
            statusFromFaculty.setText("ปฏิเสธโดยคณบดี");
            declineReasonLabel.setText(requestStatusDates.get(request.getPdfPath())[0]);
        }
    }

    private void setStatus() {
        if (request.getStatus().equals("คำร้องดำเนินการครบถ้วน")) {
            statusLabel.setText("ดำเนินการครบถ้วน");
            colorStatusCircle.setFill(Color.GREEN);
        } else if (request.getStatus().equals("คำร้องถูกปฏิเสธ")) {
            statusLabel.setText("ถูกปฏิเสธ");
            colorStatusCircle.setFill(Color.RED);
            declineReasonVbox.setVisible(true);
        } else {
            statusLabel.setText("กำลังดำเนินการ");
            colorStatusCircle.setFill(Color.GRAY);
        }
    }

    private void setDownloadFileButton() {
        String requestFor = request.getRequestFor();

        if (requestFor.equals("ใบแทนปริญญาบัตรชำรุด")) {
            setVisibility(true, downloadButton);
            downloadButton.setText("ใบปริญญาบัตรที่ชำรุดและสำเนาบัตรประชาชน");
        } else if (requestFor.equals("ใบแทนปริญญาบัตรสูญหาย")) {
            setVisibility(true, downloadButton);
            downloadButton.setText("ใบแจ้งความและสำเนาบัตรประชาชน");
        }else if (requestFor.equals("เปลี่ยนชื่อตัว")) {
            setVisibility(true, downloadButton);
            downloadButton.setText("ใบเปลี่ยนชื่อ");
        } else if (requestFor.equals("เปลี่ยนชื่อสกุล")) {
            setVisibility(true, downloadButton);
            downloadButton.setText("ใบเปลี่ยนนามสกุล");
        } else if (requestFor.equals("ลงทะเบียนล่าช้าหรือรักษาสถานภาพนิสิตล่าช้า")) {
            setVisibility(true, downloadButton);
            downloadButton.setText("KU1");
        } else if (requestFor.equals("เพิ่มหรือถอนรายวิชาล่าช้า")) {
            setVisibility(true, downloadButton);
            downloadButton.setText("KU3");
        } else if (requestFor.equals("ลงทะเบียนเกิน 22 หน่วยกิตสำหรับต้นหรือปลาย หรือลงทะเบียนเรียนเกิน 7 หน่วยสำหรับภาคฤดูร้อน")) {
            setVisibility(true, downloadButton);
            downloadButton.setText("KU3");
        } else {
            downloadButton.setVisible(false);
        }
    }


    @FXML
    public void onDownloadFile() {
        String requestFor = request.getRequestFor();
        String pdfPath = null;

        if (requestFor.equals("ใบแทนปริญญาบัตรชำรุด")) {
            pdfPath = "data/pdf-file-list/" + request.getId() + "_" + "DamageCerti" + ".pdf";
        } else if (requestFor.equals("ใบแทนปริญญาบัตรสูญหาย")) {
            pdfPath = "data/pdf-file-list/" + request.getId() + "_" + "lose" + ".pdf";
        }else if (requestFor.equals("เปลี่ยนชื่อตัว")) {
            pdfPath = "data/pdf-file-list/" + request.getId() + "_" + "changeName" + ".pdf";
        } else if (requestFor.equals("เปลี่ยนชื่อสกุล")) {
            pdfPath = "data/pdf-file-list/" + request.getId() + "_" + "changeLastname" + ".pdf";
        } else if (requestFor.equals("ลงทะเบียนล่าช้าหรือรักษาสถานภาพนิสิตล่าช้า")) {
            pdfPath = "data/pdf-file-list/" + request.getId() + "_" + "KU1" + ".pdf";
        } else if (requestFor.equals("เพิ่มหรือถอนรายวิชาล่าช้า")) {
            pdfPath = "data/pdf-file-list/" + request.getId() + "_" + "KU3" + ".pdf";
        } else if (requestFor.equals("ลงทะเบียนเกิน 22 หน่วยกิตสำหรับต้นหรือปลาย หรือลงทะเบียนเรียนเกิน 7 หน่วยสำหรับภาคฤดูร้อน")) {
            pdfPath = "data/pdf-file-list/" + request.getId() + "_" + "KU3" + ".pdf";
        } else {
            downloadButton.setVisible(false);
        }
        if (!(pdfPath == null)) {
            File file = new File(pdfPath);
            try {
                if (file.exists()) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(file);
                    }
                }
            } catch (IOException e) {
                errorLabel.setVisible(true);
                errorLabel.setText("ไม่สามารถเปิดไฟล์ได้");
            }
        }
    }

    @FXML
    public void onFileApprove() {
        FileChooser fileChooser = new FileChooser();
        File approveFile = fileChooser.showOpenDialog(new Stage());
        PDFSaver pdfSaver = new PDFSaver("data/pdf-file-list/");
        try {
            if (approveFile != null) {
                pdfSaver.savePDFRequest(request.getPdfPath(), approveFile);
                fileSaved = true;
            }
        } catch (Exception e) {
            errorLabel.setVisible(true);
            errorLabel.setText("ไม่สามารถบันทึกไฟล์ได้");
        }
    }

    @FXML
    public void onClickApprove() {

        try {
            if (user.getRole().equals("advisor")) {
                if (fileSaved) {
                    advisorApprover();
                } else {
                    setVisibility(true, errorLabel);
                }
            } else if (user.getRole().equals("major staff")) {
                if (fileSaved) {
                    majorStaffApprover();
                } else {
                    setVisibility(true, errorLabel);
                }
            } else if (user.getRole().equals("faculty staff")) {
                if (fileSaved) {
                    facultyStaffApprover();
                } else {
                    setVisibility(true, errorLabel);
                }
            }
            requestsDatasource.writeData(requestList);
            onBackButton();

        } catch (Exception e) {
            setVisibility(true, errorLabel);
        }
    }

    private void advisorApprover() {
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        requestList.setDate(request.getId(), request.getDate(), date);

        RequestStatusDatesFileDatasource requestStatusDatesFileDatasource = new RequestStatusDatesFileDatasource("data", "request-status-dates.csv");
        HashMap<String, String[]> requestStatusDates = requestStatusDatesFileDatasource.readData();
        requestStatusDates.get(request.getPdfPath())[2] = date;
        requestStatusDatesFileDatasource.writeData(requestStatusDates);

        requestList.approveRequest(request.getId(), request.getDate(), "คำร้องส่งต่อให้หัวหน้าภาควิชา", "อนุมัติโดยอาจารย์ที่ปรึกษา", user.getName());
        colorStatusCircle.setFill(Color.GREEN);
        statusLabel.setText("อนุมัติแล้ว");
    }

    private void majorStaffApprover() {
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        requestList.setDate(request.getId(), request.getDate(), date);

        RequestStatusDatesFileDatasource requestStatusDatesFileDatasource = new RequestStatusDatesFileDatasource("data", "request-status-dates.csv");
        HashMap<String, String[]> requestStatusDates = requestStatusDatesFileDatasource.readData();
        requestStatusDates.get(request.getPdfPath())[3] = date;
        requestStatusDatesFileDatasource.writeData(requestStatusDates);

        String approver = approverChoiceBox.getValue();
        if (request.getType().equals("คำร้องขอลงทะเบียนเรียน") || request.getType().equals("ขอลาออก")) {
            requestList.approveRequest(request.getId(), request.getDate(), "คำร้องส่งต่อให้คณบดี", "อนุมัติโดยหัวหน้าภาควิชา", approver);
        } else if (request.getType().equals("คำร้องทั่วไป")) {
            request.setStatus("คำร้องดำเนินการครบถ้วน");
            request.setStatusFromApprover("อนุมัติโดยหัวหน้าภาควิชา");
            requestList.approveRequest(request.getId(), request.getDate(), "คำร้องดำเนินการครบถ้วน", "อนุมัติโดยหัวหน้าภาควิชา", approver);
        } else {
            setVisibility(true, errorLabel);
        }
        colorStatusCircle.setFill(Color.GREEN);
        statusLabel.setText("อนุมัติแล้ว");
    }

    private void facultyStaffApprover() {
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        requestList.setDate(request.getId(), request.getDate(), date);

        RequestStatusDatesFileDatasource requestStatusDatesFileDatasource = new RequestStatusDatesFileDatasource("data", "request-status-dates.csv");
        HashMap<String, String[]> requestStatusDates = requestStatusDatesFileDatasource.readData();
        requestStatusDates.get(request.getPdfPath())[4] = date;
        requestStatusDatesFileDatasource.writeData(requestStatusDates);

        String approver = approverChoiceBox.getValue();
        requestList.approveRequest(request.getId(), request.getDate(), "คำร้องดำเนินการครบถ้วน","อนุมัติโดยคณบดี" , approver);
        colorStatusCircle.setFill(Color.GREEN);
        statusLabel.setText("อนุมัติแล้ว");

    }

    @FXML
    public void onClickDecline() {
        try {
            if (user.getRole().equals("advisor")) {
                advisorDecline();
            } else if (user.getRole().equals("major staff")) {
                majorStaffDecline();
            } else if (user.getRole().equals("faculty staff")) {
                facultyStaffDecline();
            }
            requestsDatasource.writeData(requestList);
            onBackButton();

        } catch (Exception e) {
            errorLabel.setVisible(true);
            errorLabel.setText("ไม่สามารถดำเนินการได้");
        }
    }

    private void advisorDecline() {
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        requestList.setDate(request.getId(), request.getDate(), date);

        RequestStatusDatesFileDatasource requestStatusDatesFileDatasource = new RequestStatusDatesFileDatasource("data", "request-status-dates.csv");
        HashMap<String, String[]> requestStatusDates = requestStatusDatesFileDatasource.readData();
        requestStatusDates.get(request.getPdfPath())[2] = date;

        String decline = declineTextField.getText();
        requestList.approveRequest(request.getId(), request.getDate(), "คำร้องถูกปฏิเสธ", "ปฏิเสธโดยอาจารย์ที่ปรึกษา", user.getName());
        requestStatusDates.get(request.getPdfPath())[0] = decline;
        requestStatusDatesFileDatasource.writeData(requestStatusDates);

        colorStatusCircle.setFill(Color.RED);
        statusLabel.setText("ปฏิเสธแล้ว");
    }

    private void majorStaffDecline() {
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        requestList.setDate(request.getId(), request.getDate(), date);

        RequestStatusDatesFileDatasource requestStatusDatesFileDatasource = new RequestStatusDatesFileDatasource("data", "request-status-dates.csv");
        HashMap<String, String[]> requestStatusDates = requestStatusDatesFileDatasource.readData();
        requestStatusDates.get(request.getPdfPath())[3] = date;

        String approver = approverChoiceBox.getValue();
        String decline = declineTextField.getText();
        requestList.approveRequest(request.getId(), request.getDate(), "คำร้องถูกปฏิเสธ", "ปฏิเสธโดยหัวหน้าภาควิชา", approver);
        requestStatusDates.get(request.getPdfPath())[0] = decline;
        requestStatusDatesFileDatasource.writeData(requestStatusDates);

        colorStatusCircle.setFill(Color.RED);
        statusLabel.setText("ปฏิเสธแล้ว");
    }

    private void facultyStaffDecline() {
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        requestList.setDate(request.getId(), request.getDate(), date);

        RequestStatusDatesFileDatasource requestStatusDatesFileDatasource = new RequestStatusDatesFileDatasource("data", "request-status-dates.csv");
        HashMap<String, String[]> requestStatusDates = requestStatusDatesFileDatasource.readData();
        requestStatusDates.get(request.getPdfPath())[4] = date;

        String approver = approverChoiceBox.getValue();
        String decline = declineTextField.getText();
        requestList.approveRequest(request.getId(), request.getDate(), "คำร้องถูกปฏิเสธ", "ปฏิเสธโดยคณบดี", approver);
        requestStatusDates.get(request.getPdfPath())[0] = decline;
        requestStatusDatesFileDatasource.writeData(requestStatusDates);

        colorStatusCircle.setFill(Color.RED);
        statusLabel.setText("ปฏิเสธแล้ว");
    }

    @FXML
    public void onBackButton() {
        try {
            if (user.getRole().equals("advisor")) {
                FXRouter.goTo("advisor-info", user.getUsername() + "," + "request");
            } else if (user.getRole().equals("major staff")) {
                FXRouter.goTo("major-staff-info-table", user.getUsername());
            } else if (user.getRole().equals("faculty staff")) {
                FXRouter.goTo("faculty-info-table", user.getUsername());
            } else if (user.getRole().equals("student")) {
                if (isStudentRequestHistory) {
                    FXRouter.goTo("student-request-history-advisor", user.getUsername() + "," + advisorUsername);
                } else {
                    FXRouter.goTo("table-request-student", user.getUsername());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setVisibility(boolean visible, Node... nodes) {
        for (Node node : nodes) {
            node.setVisible(visible);
            node.setDisable(!visible);
        }
    }


    public void showPDF(String filePath) {
        try {
            File pdfFile = new File(filePath);
            if (!pdfFile.exists()) {
                errorLabel.setVisible(true);
                errorLabel.setText("ไม่พบไฟล์");
                return;
            }
            PDDocument document = Loader.loadPDF(pdfFile);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 170);
            WritableImage image = SwingFXUtils.toFXImage(bim, null);

            imageView.setImage(image);
            imageView.setVisible(true);
            pdfScrollPane.setContent(imageView);

            document.close();
        } catch (IOException e)
        {
            errorLabel.setVisible(true);
            errorLabel.setText("ไม่พบไฟล์");
        }
    }

    public void onDownloadRequestButtonClick() {
        String pdfPath = request.getPdfPath();
        File file = new File(pdfPath);
        try {
            if (file.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }
            }
        } catch (IOException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("ไม่สามารถเปิดไฟล์ได้");
        }
    }
}

