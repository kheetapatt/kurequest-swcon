package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
import ku.cs.models.RequestList;
import ku.cs.services.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class RequestGeneralStudentController {
    @FXML private TextField nameTextField;
    @FXML private TextField idTextField;
    @FXML private TextField telTextField;
    @FXML private TextField facultyTextField;
    @FXML private TextField majorTextField;
    @FXML private TextField yearTextField;
    @FXML private ChoiceBox<String> topicChoice;
    @FXML private TextField nameThaiChangeFromTextField;
    @FXML private TextField nameThaiChangeToTextField;
    @FXML private TextField nameEngChangeFromTextField;
    @FXML private TextField nameEngChangeToTextField;
    @FXML private TextField reasonTextField;
    @FXML private ChoiceBox<String> lossOrDamageChoice;
    @FXML private Button fileAttachment1Button;
    @FXML private Label errorText;

    ObservableList<String> topiclist;
    ObservableList<String> lossordamagelist;

    private Student student;

    private String type1;


    @FXML
    public void initialize() {
        Datasource<StudentList> studentListDatasource = new StudentListFileDatasource("data", "student-list.csv");
        StudentList studentList = studentListDatasource.readData();

        topiclist = FXCollections.observableArrayList("กรุณาเลือก","ใบแทนปริญญาบัตร","เปลี่ยนชื่อตัว","เปลี่ยนชื่อสกุล","อื่นๆ");
        lossordamagelist = FXCollections.observableArrayList("กรุณาเลือก","สูญหาย","ชำรุด");

        String StudentUsername = (String) FXRouter.getData();
        student = studentList.findStudentByUsername(StudentUsername);
        topicChoice.setItems(topiclist);
        lossOrDamageChoice.setItems(lossordamagelist);

        lossOrDamageChoice.setValue("กรุณาเลือก");
        topicChoice.setValue("กรุณาเลือก");
        topicChoice.setOnAction(e->TopicChoice());
        lossOrDamageChoice.setOnAction(e->LossOrDamageChoice());
        setVisibility(false,lossOrDamageChoice,fileAttachment1Button,nameEngChangeFromTextField,nameEngChangeToTextField,nameThaiChangeFromTextField,nameThaiChangeToTextField,reasonTextField,errorText);

        nameTextField.setText(student.getName());
        idTextField.setText(student.getId());
        facultyTextField.setText(student.getFaculty());
        majorTextField.setText(student.getMajor());
        yearTextField.setText(String.valueOf(student.getYear()));

        errorText.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
        yearTextField.setText(""+student.getYear());
        facultyTextField.setText(student.getFaculty());
        majorTextField.setText(student.getMajor());
    }

    private void LossOrDamageChoice()
    {
     String choice = lossOrDamageChoice.getValue();
        if(choice.equals("สูญหาย"))
        {
            setVisibility(true,fileAttachment1Button);
            setVisibility(false,nameEngChangeToTextField,nameEngChangeFromTextField,nameThaiChangeToTextField,nameThaiChangeFromTextField,nameThaiChangeToTextField,reasonTextField);
            fileAttachment1Button.setText("แนบใบแจ้งความและสำเนาบัตรประชาชน (รวมในไฟล์เดียวกัน)");

           type1 = "lose";
        } else if(choice.equals("ชำรุด"))
        {
            setVisibility(true,fileAttachment1Button);
            setVisibility(false,nameEngChangeToTextField,nameEngChangeFromTextField,nameThaiChangeToTextField,nameThaiChangeFromTextField,nameThaiChangeToTextField,reasonTextField);
            fileAttachment1Button.setText("แนบปริญญาบัตรที่ชำรุดและสำเนาบัตรประชาชน (รวมในไฟล์เดียวกัน)");
            type1 = "DamageCerti";
        }

    }

    private void TopicChoice() {
        String topic = topicChoice.getValue();
        if (topic.equals("ใบแทนปริญญาบัตร")) {
            setVisibility(true, lossOrDamageChoice);
            setVisibility(false, fileAttachment1Button, nameEngChangeFromTextField, nameEngChangeToTextField, nameThaiChangeFromTextField, nameThaiChangeToTextField, reasonTextField);

            LossOrDamageChoice();
        } else if (topic.equals("เปลี่ยนชื่อตัว")) {
            setVisibility(true, fileAttachment1Button, nameEngChangeFromTextField, nameEngChangeToTextField, nameThaiChangeFromTextField, nameThaiChangeToTextField);
            setVisibility(false, lossOrDamageChoice, reasonTextField);
            fileAttachment1Button.setText("แนบใบเปลี่ยนชื่อ");
            type1 = "changeName";
        } else if (topic.equals("เปลี่ยนชื่อสกุล")) {
            setVisibility(true, fileAttachment1Button, nameEngChangeFromTextField, nameEngChangeToTextField, nameThaiChangeFromTextField, nameThaiChangeToTextField, reasonTextField);
            setVisibility(false, lossOrDamageChoice, reasonTextField);
            fileAttachment1Button.setText("แนบใบเปลี่ยนสกุล");
            type1 = "changeLastname";
        }else if(topic.equals("อื่นๆ"))
        {
            setVisibility(true,reasonTextField);
            setVisibility(false,fileAttachment1Button,nameEngChangeToTextField,nameEngChangeFromTextField,nameThaiChangeToTextField,nameThaiChangeFromTextField,nameThaiChangeToTextField,lossOrDamageChoice);

        }
    }


    @FXML public void  attachFile1() {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
            File attachfile1 = fileChooser.showOpenDialog(new Stage());
            PDFSaver pdfSaver = new PDFSaver("data/pdf-file-list/");
            if (attachfile1 != null) {
                try {
                    pdfSaver.savePDF(attachfile1,idTextField.getText(),type1);
                    System.out.println("Save successful");
                }
                catch (Exception e)
                {
                    System.err.println("Error save File");
                }
            }
        }




    @FXML public void onConfirmButton() {

        try  {
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            String status = "คำร้องส่งต่อให้อาจารย์ที่ปรึกษา";
            String name = nameTextField.getText();
            String id = idTextField.getText().trim();
            String statusFromApprover = "ใบคำร้องใหม่";
            String faculty = facultyTextField.getText().trim();
            String major = majorTextField.getText().trim();
            String advisor = student.getAdvisor();
            String phone = telTextField.getText().trim();
            String from = (nameEngChangeFromTextField.getText() + " " + nameThaiChangeFromTextField.getText());
            String to = (nameEngChangeToTextField.getText() + " " + nameThaiChangeToTextField.getText());
            String reason = reasonTextField.getText().trim();
            String requestFor = topicChoice.getValue().trim();
            String type = "คำร้องทั่วไป";
            String year = yearTextField.getText().trim();

            writeNewRequestDate(date, id);

            if(!topicChoice.getValue().equals("กรุณาเลือก")) {
                if (topicChoice.getValue().equals("ใบแทนปริญญาบัตร") && !phone.isEmpty()) {
                    if (lossOrDamageChoice.getValue().equals("ชำรุด")) {
                        String pdfPath = "data/pdf-file-list/" +id + "_" + type1 + ".pdf";
                        File file = new File(pdfPath);
                        if (file.exists()) {
                            saveRequest(date, status, statusFromApprover, id, name, year, "", phone, faculty, major, advisor, "", "", from, to, "", "ใบแทนปริญญาบัตรชำรุด", type);
                            onCancelButton();

                        } else {
                            errorText.setVisible(true);
                        }
                    } else if (lossOrDamageChoice.getValue().equals("สูญหาย"))
                    {
                        String pdfPath = "data/pdf-file-list/" +id + "_" + type1 + ".pdf";
                        File file = new File(pdfPath);
                        if (file.exists()) {
                            saveRequest(date, status, statusFromApprover, id, name, year, "", phone, faculty, major, advisor, "", "", from, to, "", "ใบแทนปริญญาบัตรสูญหาย", type);
                            onCancelButton();

                        } else {
                            errorText.setVisible(true);
                        }
                    } else {
                        errorText.setVisible(true);
                    }
                } else if (requestFor.equals("เปลี่ยนชื่อตัว")) {
                    String pdfPath = "data/pdf-file-list/" +id + "_" + type1 + ".pdf";
                    File file = new File(pdfPath);
                    if (!phone.isEmpty() && !from.isEmpty() && !to.isEmpty()&&file.exists()) {
                        saveRequest(date, status, statusFromApprover, id, name, year, "", phone, faculty, major, advisor, "", "", from, to, "", requestFor, type);
                        onCancelButton();

                    } else {
                        errorText.setVisible(true);
                    }
                } else if (requestFor.equals("เปลี่ยนชื่อสกุล")) {
                    String pdfPath = "data/pdf-file-list/" +id + "_" + type1 + ".pdf";
                    File file = new File(pdfPath);
                    if (!phone.isEmpty() && !from.isEmpty() && !to.isEmpty()&&file.exists()) {
                        saveRequest(date, status, statusFromApprover, id, name, year, "", phone, faculty, major, advisor, "", "", from, to, "", requestFor, type);
                        onCancelButton();

                    } else {
                        errorText.setVisible(true);
                    }
                } else if (requestFor.equals("อื่นๆ")) {
                    if (!phone.isEmpty() && !reason.isEmpty()) {
                        saveRequest(date, status, statusFromApprover, id, name, year, "", phone, faculty, major, advisor, "", "", "", "", reason, requestFor, type);
                        onCancelButton();
                    } else {
                        errorText.setVisible(true);
                    }
                }else{ errorText.setVisible(true);
                }
            }else{ errorText.setVisible(true);}

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

    @FXML public void onCancelButton() {
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
    {        String dateUse = date.replace("/","-").replace(":", "-").replace(" ", "_");
        String outputFileName = "data/pdf-file-list/"+ dateUse+"_"+ id + ".pdf";
        RequestListFileDatasource requestListFileDatasource = new RequestListFileDatasource("data","request-list.csv");
        RequestList requestList = requestListFileDatasource.readData();
        PDFMaker p = new PDFMaker();
        p.makePDF(date, name,year, id, phone,mail, faculty, major, advisor, academic_year, semester, from, to, reason, requestFor, type);
        requestList.addRequest(date, status, statusFromApprover, id, phone, faculty, major, advisor,
                academic_year, semester, from, to, reason, requestFor, "",outputFileName, type);
        requestListFileDatasource.writeData(requestList);
    }

}
