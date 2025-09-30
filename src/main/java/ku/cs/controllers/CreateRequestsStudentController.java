package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StudentListFileDatasource;

import java.io.*;

public class CreateRequestsStudentController {
    @FXML private Label nameLabel;
    @FXML private Circle imageCircle;
    @FXML private Label errorLabel;
    private Student student;

    @FXML
    public void initialize() {

        Datasource<StudentList> studentListDatasource = new StudentListFileDatasource("data","student-list.csv");
        StudentList studentList = studentListDatasource.readData();

        String username = (String) FXRouter.getData();
        student = studentList.findStudentByUsername(username);
        studentProfile();

        errorLabel.setVisible(false);
    }

    public void studentProfile() {
        if (student != null) {
            nameLabel.setText(student.getName());
            showImage();
        }
    }

    private void showImage() {
        String imageDir = "data/user-images/";
        InputStream imageStream;
        Image image;
        try {
            imageStream = new FileInputStream(imageDir + student.getImage());
            image = new Image(imageStream);
            imageCircle.setFill(new ImagePattern(image));
        } catch (FileNotFoundException e) {
            try {
                imageStream = new FileInputStream(imageDir + "default-profile-image.png");
                image = new Image(imageStream);
                imageCircle.setFill(new ImagePattern(image));
            } catch (FileNotFoundException ex) {
                imageCircle.setFill(Color.WHITE);
            }
        }
    }

    @FXML
    public void onEditStudentButton() {
        try {
            FXRouter.goTo("set-profile", student.getUsername());
        } catch (IOException e) {
            errorLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะ");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    public void onCreateRequestButton() {
        try {
            FXRouter.goTo("create-request-student", student.getUsername());
        } catch (IOException e) {
            errorLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะ");
            errorLabel.setVisible(true);
        }
    }
    @FXML
    public void onStatusRequestButton() {
        try {
            FXRouter.goTo("table-request-student", student.getUsername());
        } catch (IOException e) {
            errorLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะ");
            errorLabel.setVisible(true);
        }
    }
    @FXML
    public void onGeneralRequestButton() {
        if (student.getAdvisor() == null || student.getAdvisor().equals("") || student.getAdvisor().equals("null")) {
            errorLabel.setText("ไม่สามารถสร้างคำร้องได้ เนื่องจากนิสิตไม่มีอาจารย์ที่ปรึกษา กรุณาติดต่อเจ้าหน้าที่ภาควิชา");
            errorLabel.setVisible(true);
        } else {
            try {
                FXRouter.goTo("request-general-student", student.getUsername());
            } catch (IOException e) {
                errorLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะ");
                errorLabel.setVisible(true);
            }
        }

    }
    @FXML
    public void onRequestForRegistrationButton() {
        if (student.getAdvisor() == null || student.getAdvisor().equals("") || student.getAdvisor().isEmpty()) {
            errorLabel.setText("ไม่สามารถสร้างคำร้องได้ เนื่องจากนิสิตไม่มีอาจารย์ที่ปรึกษา กรุณาติดต่อเจ้าหน้าที่ภาควิชา");
            errorLabel.setVisible(true);
        } else {
            try {
                FXRouter.goTo("requests-for-registration", student.getUsername());
            } catch (IOException e) {
                errorLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะ");
                errorLabel.setVisible(true);
            }
        }
    }
    @FXML
    public void onResignationFormButton() {
        if (student.getAdvisor() == null || student.getAdvisor().equals("") || student.getAdvisor().isEmpty()) {
            errorLabel.setText("ไม่สามารถสร้างคำร้องได้ เนื่องจากนิสิตไม่มีอาจารย์ที่ปรึกษา กรุณาติดต่อเจ้าหน้าที่ภาควิชา");
            errorLabel.setVisible(true);
        } else {
            try {
                FXRouter.goTo("request-resignation-student", student.getUsername());
            } catch (IOException e) {
                errorLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะ");
                errorLabel.setVisible(true);
            }
        }
    }

    @FXML
    public void onLogoutButton() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            errorLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะ");
            errorLabel.setVisible(true);
        }
    }
}

