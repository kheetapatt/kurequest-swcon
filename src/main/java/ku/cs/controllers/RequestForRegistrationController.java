package ku.cs.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


import ku.cs.models.RequestList;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class    RequestForRegistrationController {


    ObservableList<String> requestfor;

    @FXML
    private TextField nameBox;
    @FXML
    private TextField idBox;
    @FXML
    private TextField yearNowBox;
    @FXML
    private TextField facultyBox;
    @FXML
    private TextField majorBox;
    @FXML
    private TextField phoneBox;
    @FXML
    private TextField emailBox;
    @FXML
    private ChoiceBox<String> requestForBox;

    @FXML private Button ku1;
    @FXML private Button ku3;

    @FXML private Label semester;
    @FXML private TextField semesterBox;

    @FXML private Label year;
    @FXML private TextField yearBox;

    @FXML private Label changeCredit;
    @FXML private TextField changeCreditBox;
    @FXML private Label changeCreditTo;
    @FXML private TextField changeCreditToBox;
    @FXML private Label changeCreditTo2;

    @FXML private Label from;
    @FXML private TextField fromBox;
    @FXML private Label to;
    @FXML private TextField toBox;

    @FXML private Label reason;
    @FXML private TextField reasonBox;

    @FXML private Label errorText;

    private Student student;
    @FXML
    public void initialize()
    {
        Datasource<StudentList> studentListDatasource = new StudentListFileDatasource("data", "student-list.csv");
        StudentList studentList = studentListDatasource.readData();

        requestfor = FXCollections.observableArrayList("กรุณาเลือก","ลงทะเบียนล่าช้าหรือรักษาสถานภาพนิสิตล่าช้า", "เพิ่มหรือถอนรายวิชาล่าช้า",
                "ลงทะเบียนเกิน 22 หน่วยกิตสำหรับต้นหรือปลาย หรือลงทะเบียนเรียนเกิน 7 หน่วยสำหรับภาคฤดูร้อน","ลงทะเบียนต่ำกว่า 9 หน่วยกิต","ผ่อนผันค่าธรรมเนียมการศึกษา","ย้ายคณะ หรือเปลี่ยนสาขาวิชาเอก");

        String StudentUsername = (String) FXRouter.getData();

        student = studentList.findStudentByUsername(StudentUsername);
        requestForBox.setValue("กรุณาเลือก");
        requestForBox.setItems(requestfor);
        requestForBox.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    }
                });
            requestForBox.setOnAction(e -> {
            String selectedItem = requestForBox.getValue();
            if(selectedItem.equals("ลงทะเบียนล่าช้าหรือรักษาสถานภาพนิสิตล่าช้า")) {
                setVisibility(true, ku1);
                setVisibility(false, ku3, semester, semesterBox, year, yearBox, changeCredit, changeCreditBox, changeCreditTo, changeCreditToBox, changeCreditTo2, from, fromBox, to, toBox);
            } else if (selectedItem.equals("เพิ่มหรือถอนรายวิชาล่าช้า")) {
                setVisibility(true,ku3);
                setVisibility(false, ku1, semester, semesterBox, year, yearBox, changeCredit, changeCreditBox, changeCreditTo, changeCreditToBox, changeCreditTo2, from, fromBox, to, toBox);

            } else if (selectedItem.equals("ลงทะเบียนเกิน 22 หน่วยกิตสำหรับต้นหรือปลาย หรือลงทะเบียนเรียนเกิน 7 หน่วยสำหรับภาคฤดูร้อน")) {
                setVisibility(true,ku3,semester,semesterBox,year,yearBox,changeCredit,changeCreditBox,changeCreditTo,changeCreditToBox,changeCreditTo2);
                setVisibility(false, ku1 ,from, fromBox, to, toBox);

            } else if (selectedItem.equals("ลงทะเบียนต่ำกว่า 9 หน่วยกิต"))
            {
                setVisibility(false,ku1,ku3, semester, semesterBox, year, yearBox, changeCredit, changeCreditBox, changeCreditTo, changeCreditToBox, changeCreditTo2, from, fromBox, to, toBox);

            } else if (selectedItem.equals("ย้ายคณะ หรือเปลี่ยนสาขาวิชาเอก"))
            {
                setVisibility(true,from,fromBox, to, toBox);
                setVisibility(false,ku1,ku3, semester, semesterBox, year, yearBox, changeCredit, changeCreditBox, changeCreditTo, changeCreditToBox, changeCreditTo2);

            }else  if(selectedItem.equals("ผ่อนผันค่าธรรมเนียมการศึกษา"))
            {
                setVisibility(true,semester,semesterBox,year,yearBox);
                setVisibility(false,ku1,ku3,changeCredit, changeCreditBox, changeCreditTo, changeCreditToBox, changeCreditTo2, from, fromBox, to, toBox);;

            }
            });
        setVisibility(false,ku1,ku3, semester, semesterBox, year, yearBox, changeCredit, changeCreditBox, changeCreditTo, changeCreditToBox, changeCreditTo2, from, fromBox, to, toBox,errorText);
        setVisibility(true,reason,reasonBox);
        nameBox.setText(student.getName());
        idBox.setText(student.getId());
        yearNowBox.setText(""+student.getYear());
        facultyBox.setText(student.getFaculty());
        majorBox.setText(student.getMajor());
        emailBox.setText(student.getEmail());

        errorText.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
    }

    @FXML public void onAddKU1()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        File inputKU1 = fileChooser.showOpenDialog(new Stage());
        PDFSaver pdfSaver = new PDFSaver("data/pdf-file-list/");
        if (inputKU1 != null) {
            try {
                pdfSaver.savePDF(inputKU1, idBox.getText(), "KU1");
            }
            catch (Exception e)
            {
                errorText.setText("ไม่สามารถแนบไฟล์ได้");
            }
        }
    }

    @FXML public void onAddKU3() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        File inputKU3 = fileChooser.showOpenDialog(new Stage());

        if (inputKU3 != null) {
            try {
                PDFSaver pdfSaver = new PDFSaver("data/pdf-file-list/");
                pdfSaver.savePDF(inputKU3, idBox.getText(), "KU3");
            }
            catch (Exception e)
            {
                errorText.setText("ไม่สามารถแนบไฟล์ได้");
            }
        }
    }
    @FXML public void onClickSave(){
        try  {
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            String status = "คำร้องส่งต่อให้อาจารย์ที่ปรึกษา";
            String statusFromApprover = "ใบคำร้องใหม่";
            String id = idBox.getText().trim();
            String name = nameBox.getText();
            String faculty = facultyBox.getText().trim();
            String major = majorBox.getText().trim();
            String advisor = student.getAdvisor();
            String phone = phoneBox.getText().trim();
            String academic_year = yearBox.getText().trim();
            String semester = semesterBox.getText().trim();
            String from = fromBox.getText();
            String to = toBox.getText();
            String reason = reasonBox.getText();
            String requestFor= requestForBox.getValue();
            String email = emailBox.getText();
            String year = yearNowBox.getText();
            String type = "คำร้องขอลงทะเบียนเรียน";

            writeNewRequestDate(date, id);

            if(!requestForBox.getValue().equals("กรุณาเลือก")&&!phone.isEmpty()) {
                if (requestFor.equals("ลงทะเบียนล่าช้าหรือรักษาสถานภาพนิสิตล่าช้า")) {
                    String pdfPath = id + "_" + "KU1" + ".pdf";
                    File file = new File("data/pdf-file-list/" + pdfPath);
                    if (file.exists()) {
                        saveRequest(date, status, statusFromApprover, id, name, year, email, phone, faculty, major, advisor, "", "", "", "", reason, requestFor, type);
                        onClickGoBack();
                    } else {
                        setVisibility(true, errorText);
                    }

                } else if (requestFor.equals("เพิ่มหรือถอนรายวิชาล่าช้า")) {
                    String pdfPath = id + "_" + "KU3" + ".pdf";
                    File file = new File("data/pdf-file-list/" + pdfPath);
                    if (file.exists()) {
                        saveRequest(date, status, statusFromApprover, id, name, year, email, phone, faculty, major, advisor, "", "", "", "", reason, requestFor, type);
                        onClickGoBack();

                    } else {
                        setVisibility(true, errorText);
                    }

                } else if (requestFor.equals("ลงทะเบียนเกิน 22 หน่วยกิตสำหรับต้นหรือปลาย หรือลงทะเบียนเรียนเกิน 7 หน่วยสำหรับภาคฤดูร้อน")) {
                    from = changeCreditBox.getText().trim();
                    to = changeCreditToBox.getText().trim();
                    if (!(phone.isEmpty()) && !(academic_year.isEmpty() && !(semester.isEmpty()) && !(from.isEmpty()) && !(to.isEmpty()))) {
                        saveRequest(date, status, statusFromApprover, id, name, year, email, phone, faculty, major, advisor, academic_year, semester, from, to, reason, requestFor, type);
                        onClickGoBack();

                    } else {
                        setVisibility(true, errorText);
                    }
                } else if (requestFor.equals("ลงทะเบียนต่ำกว่า 9 หน่วยกิต")) {
                    if (!(phone.isEmpty())) {
                        saveRequest(date, status, statusFromApprover, id, name, year, email, phone, faculty, major, advisor, "", "", "", "", reason, requestFor, type);
                        onClickGoBack();

                    } else {
                        setVisibility(true, errorText);
                    }
                } else if (requestFor.equals("ผ่อนผันค่าธรรมเนียมการศึกษา")) {
                    if (!(phone.isEmpty()) && !(academic_year.isEmpty()) && !(semester.isEmpty())) {
                        saveRequest(date, status, statusFromApprover, id, name, year, email, phone, faculty, major, advisor, academic_year, semester, "", "", reason, requestFor, type);
                        onClickGoBack();

                    } else {
                        setVisibility(true, errorText);
                    }


                } else if (requestFor.equals("ย้ายคณะ หรือเปลี่ยนสาขาวิชาเอก")) {
                    if (!(phone.isEmpty()) && !(from.isEmpty() && !(to.isEmpty()))) {
                        saveRequest(date, status, statusFromApprover, id, name, year, email, phone, faculty, major, advisor, "", "", from, to, reason, requestFor, type);
                        onClickGoBack();

                    } else {
                        setVisibility(true, errorText);
                    }
                } else
                {
                    setVisibility(true, errorText);
                }
            } else{
                setVisibility(true, errorText);
            }

        } catch(Exception e){
                errorText.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
                setVisibility(true, errorText);
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


    @FXML public void onClickGoBack()
    {
        try {
            FXRouter.goTo("create-request-student",student.getUsername());
        } catch (IOException e) {
            errorText.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
            setVisibility(true, errorText);
        }
    }

    private void setVisibility(boolean visible, Node... nodes) {
        for (Node node : nodes) {
            node.setVisible(visible);
            node.setDisable(!visible);
        }
    }
    private void saveRequest(
                                             String date, String status, String statusFromApprover, String id, String name,String year,String mail,
                                             String phone, String faculty, String major, String advisor, String academic_year,
                                             String semester, String from, String to, String reason, String requestFor, String type)
    {

        Datasource<RequestList> requestsListFileDatasource = new RequestListFileDatasource("data","request-list.csv");
        RequestList requestList = requestsListFileDatasource.readData();
        for (Request request : requestList.getRequests()) {
            System.out.println(request.getType());
        }
        String dateUse = date.replace("/","-").replace(":", "-").replace(" ", "_");
        String outputFileName = "data/pdf-file-list/"+ dateUse+"_"+ id +".pdf";
        PDFMaker p = new PDFMaker();
        p.makePDF(date, name,year, id, phone,mail, faculty, major, advisor, academic_year, semester, from, to, reason, requestFor, type);

        requestList.addRequest(date, status, statusFromApprover, id, phone, faculty, major, advisor,
                academic_year, semester, from, to, reason, requestFor, "", outputFileName, type);
        requestsListFileDatasource.writeData(requestList);
    }

}


