package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ku.cs.models.Faculty;
import ku.cs.models.FacultyList;
import ku.cs.models.Major;
import ku.cs.models.MajorList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.FacultyListFileDatasource;
import ku.cs.services.MajorListFileDatasource;

import java.io.IOException;

public class EditFacultyAndMajorController {
    @FXML private Label nameLabel;
    @FXML private Label idLabel;
    @FXML private Label facultyLabel;
    @FXML private Label editTitle;
    @FXML private Label editTitleInPane;
    @FXML private Label nameError;
    @FXML private Label idError;
    @FXML private Label facultyError;
    @FXML private Label statusLabel;
    @FXML private TextField nameTextField;
    @FXML private TextField idTextField;
    @FXML private ChoiceBox<String> facultyChoice;
    @FXML private Pane editPane;
    @FXML private VBox editFacultyVbox;
    @FXML private VBox facultyVbox;

    private Datasource<FacultyList> facultyListDatasource;
    private Datasource<MajorList> majorListDatasource;
    private FacultyList facultyList;
    private MajorList majorList;
    private Major major;
    private Faculty faculty;
    private String type;

    @FXML
    public void initialize() {
        facultyListDatasource = new FacultyListFileDatasource("Data", "faculty-list.csv");
        majorListDatasource = new MajorListFileDatasource("Data", "major-list.csv");
        facultyList = facultyListDatasource.readData();
        majorList = majorListDatasource.readData();

        Object obj = FXRouter.getData();
        if (obj instanceof Faculty) {
            faculty = (Faculty) obj;
            faculty = facultyList.findFacultyById(faculty.getId());
            type = "faculty";
            showFaculty(faculty);
        } else if (obj instanceof Major) {
            major = (Major) obj;
            major = majorList.findMajorById(major.getId());
            type = "major";
            showMajor(major);
            facultyChoice.getItems().addAll(facultyList.getFacultyNames());
        }

        editPane.setVisible(false);
        nameError.setText("");
        idError.setText("");
        facultyError.setText("");
        statusLabel.setText("");
    }

    private void showFaculty(Faculty faculty) {
        nameLabel.setText(faculty.getName());
        idLabel.setText(faculty.getId());
        facultyVbox.setVisible(false);
        editTitle.setText("ข้อมูลคณะ");
    }

    private void showMajor(Major major) {
        nameLabel.setText(major.getName());
        idLabel.setText(major.getId());
        facultyLabel.setText(major.getFaculty());
        editTitle.setText("ข้อมูลภาควิชา");
    }

    @FXML
    public void onEditButtonClick() {
        editPane.setVisible(true);
        if (type.equals("faculty")) {
            nameTextField.setText(faculty.getName());
            idTextField.setText(faculty.getId());
            editFacultyVbox.setVisible(false);
            editTitleInPane.setText("แก้ไขข้อมูลคณะ");
        } else if (type.equals("major")) {
            nameTextField.setText(major.getName());
            idTextField.setText(major.getId());
            facultyChoice.setValue(major.getFaculty());
            editTitleInPane.setText("แก้ไขข้อมูลภาควิชา");
        }
    }

    @FXML
    public void onSubmitButtonClick() {
        String name = nameTextField.getText().trim();
        String id = idTextField.getText().trim();
        if (name.equals("")) {
            nameError.setText("กรุณากรอกชื่อ");
            return;
        }
        if (id.equals("")) {
            idError.setText("กรุณากรอกรหัส");
            return;
        }
        if (type.equals("faculty")) {
            facultyList.editName(faculty.getId(), name);
            facultyList.editId(faculty.getId(), id);
            facultyListDatasource.writeData(facultyList);
            showFaculty(faculty);
        } else if (type.equals("major")) {
            String faculty = facultyChoice.getValue();
            if (faculty == null) {
                facultyError.setText("กรุณาเลือกคณะ");
                return;
            }
            majorList.editName(major.getId(), name);
            majorList.editId(major.getId(), id);
            majorList.editFaculty(major.getId(), faculty);
            majorListDatasource.writeData(majorList);
            showMajor(major);
        }
        editPane.setVisible(false);
        nameError.setText("");
        idError.setText("");
        facultyError.setText("");
    }

    @FXML
    public void onCancelButtonClick() {
        editPane.setVisible(false);
        nameError.setText("");
        idError.setText("");
        facultyError.setText("");
    }

    @FXML
    public void onBackButtonClick() {
        try {
            if (type.equals("faculty")) {
                FXRouter.goTo("admin-manage-faculty-and-major-info", "faculty");
            } else if (type.equals("major")) {
                FXRouter.goTo("admin-manage-faculty-and-major-info", "major");
            }
        } catch (IOException e) {
            statusLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
            statusLabel.setStyle("-fx-text-fill: red");
        }
    }
}
