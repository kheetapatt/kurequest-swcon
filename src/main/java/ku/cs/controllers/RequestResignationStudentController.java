package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import ku.cs.models.RequestList;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
import ku.cs.services.*;

public class RequestResignationStudentController {
    @FXML private TextField nameTextField;
    @FXML private TextField idTextField;
    @FXML private TextField telTextField;
    @FXML private TextField facultyTextField;
    @FXML private TextField majorTextField;
    @FXML private TextField yearTextField;
    @FXML private TextField reasonTextField;
    @FXML private Label errorText;
    private Student student;
    @FXML
    public void initialize() {
        Datasource<StudentList> studentListDatasource = new StudentListFileDatasource("data", "student-list.csv");
        StudentList studentList = studentListDatasource.readData();

        String StudentUsername = (String) FXRouter.getData();
        student = studentList.findStudentByUsername(StudentUsername);
        nameTextField.setText(student.getName());
        idTextField.setText(student.getId());
        yearTextField.setText(""+student.getYear());
        facultyTextField.setText(student.getFaculty());
        majorTextField.setText(student.getMajor());
        errorText.setVisible(false);

        errorText.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
    }



    @FXML public void onConfirmButton() {

        try {
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            String name = nameTextField.getText();
            String year = yearTextField.getText();
            String status = "คำร้องส่งต่อให้อาจารย์ที่ปรึกษา";
            String id = idTextField.getText();
            String phone = telTextField.getText();
            String statusFromApprover = "ใบคำร้องใหม่";
            String faculty = facultyTextField.getText().trim();
            String major = majorTextField.getText().trim();
            String advisor = student.getAdvisor();
            String reason = reasonTextField.getText();

            writeNewRequestDate(date, id);

            if (!(phone.isEmpty())||!(reason.isEmpty())) {
                saveRequest(date, status, statusFromApprover, id,name,year,"",phone, faculty, major, advisor, "", "", "", "", reason,"ขอลาออก","ขอลาออก");
                onCancelButton();
            } else {
                errorText.setVisible(true);
            }
            errorText.setVisible(true);
        } catch (Exception e) {
            errorText.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
            throw new RuntimeException(e);
        }

    }

    private void writeNewRequestDate(String date, String id) {
        RequestStatusDatesFileDatasource requestStatusDatesFileDatasource = new RequestStatusDatesFileDatasource("data", "request-status-dates.csv");
        HashMap<String, String[]> requestStatusDates = requestStatusDatesFileDatasource.readData();

        String[] defaultDates = {"",date,"00/00/00 00:00:00","00/00/00 00:00:00","00/00/00 00:00:00"};
        String pdfFilePath = date.replace("/", "-").replace(":", "-").replace(" ", "_");;
        requestStatusDates.put("data/pdf-file-list/" + pdfFilePath +"_"+ id +".pdf", defaultDates);
        requestStatusDatesFileDatasource.writeData(requestStatusDates);
    }


    @FXML public void onCancelButton() {
        try {
            FXRouter.goTo("create-request-student",student.getUsername());
        } catch (IOException e) {
            errorText.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
        }
    }

    private void saveRequest(
            String date, String status, String statusFromApprover, String id, String name,String year,String mail,
            String phone, String faculty, String major, String advisor, String academic_year,
            String semester, String from, String to, String reason, String requestFor, String type)
    {
        RequestListFileDatasource requestListFileDatasource = new RequestListFileDatasource("data","request-list.csv");
        RequestList requestList = requestListFileDatasource.readData();
        String dateUse = date.replace("/","-").replace(":", "-").replace(" ", "_");
        String outputFileName = "data/pdf-file-list/"+ dateUse+"_"+ id +".pdf";
        PDFMaker p = new PDFMaker();
        p.makePDF(date, name,year, id, phone,mail, faculty, major, advisor, academic_year, semester, from, to, reason, requestFor, type);
        requestList.addRequest(date, status, statusFromApprover, id, phone, faculty, major, advisor,
                academic_year, semester, from, to, reason, requestFor, "", outputFileName, type);
        requestListFileDatasource.writeData(requestList);
    }

}

