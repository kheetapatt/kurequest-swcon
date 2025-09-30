package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ku.cs.models.FacultyList;
import ku.cs.models.MajorList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.FacultyListFileDatasource;
import ku.cs.services.MajorListFileDatasource;

import java.io.IOException;

public class AddFacultyAndMajorController {
    @FXML private TextField nameTextField;
    @FXML private TextField idTextField;
    @FXML private Label nameError;
    @FXML private Label idError;
    @FXML private Label facultyError;
    @FXML private Label status;
    @FXML private Label addTitle;
    @FXML private ChoiceBox<String> facultyChoice;
    @FXML private ChoiceBox<String> facultyOrMajorChoice;
    @FXML private VBox facultyVbox;

    private Datasource<FacultyList> facultyListDatasource;
    private Datasource<MajorList> majorListDatasource;
    private FacultyList facultyList;
    private MajorList majorList;
    private String[] types = {"คณะ", "ภาควิชา"};
    private String type;

    @FXML
    public void initialize() {
        facultyListDatasource = new FacultyListFileDatasource("data", "faculty-list.csv");
        majorListDatasource = new MajorListFileDatasource("data", "major-list.csv");
        facultyList = facultyListDatasource.readData();
        majorList = majorListDatasource.readData();

        facultyOrMajorChoice.getItems().addAll(types);
        facultyOrMajorChoice.setValue(types[0]);
        type = types[0];
        setFaculty();
        setEmpty();
        status.setText("");

        facultyOrMajorChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldType, String newType) {
                type = newType;
                onTypeSelected();
            }
        });
    }

    private void onTypeSelected() {
        if (type.equals(types[0])) {
            setFaculty();
        } else if (type.equals(types[1])) {
            setMajor();
        }
    }

    private void setFaculty() {
        facultyVbox.setVisible(false);
        addTitle.setText("เพิ่มคณะ");
    }

    private void setMajor() {
        facultyVbox.setVisible(true);
        addTitle.setText("เพิ่มภาควิชา");
        facultyChoice.getItems().addAll(facultyList.getFacultyNames());
    }

    private void setEmpty() {
        nameTextField.setText("");
        idTextField.setText("");
        facultyChoice.setValue(null);
        nameError.setText("");
        idError.setText("");
        facultyError.setText("");
    }

    @FXML
    public void onAddButtonClick() {
        String name = nameTextField.getText().trim();
        String id = idTextField.getText().trim();
        if (name.isEmpty()) {
            nameError.setText("กรุณากรอกชื่อ");
            return;
        }
        if (id.isEmpty()) {
            idError.setText("กรุณากรอกรหัส");
            return;
        }
        if (type.equals(types[0])) {
            facultyList.addFaculty(name, id);
            facultyListDatasource.writeData(facultyList);
        } else if (type.equals(types[1])) {
            String faculty = facultyChoice.getValue();
            if (faculty == null) {
                facultyError.setText("กรุณาเลือกคณะ");
                return;
            }
            majorList.addMajor(name, id, faculty);
            majorListDatasource.writeData(majorList);
        }
        status.setText("เพิ่มสำเร็จ");
        setEmpty();
    }

    @FXML
    public void onCancelButtonClick() {
        setEmpty();
        status.setText("");
    }

    @FXML
    public void onBackButtonClick() {
        try {
            if (type.equals(types[0])) {
                FXRouter.goTo("admin-manage-faculty-and-major-info", "faculty");
            } else if (type.equals(types[1])) {
                FXRouter.goTo("admin-manage-faculty-and-major-info", "major");
            }
        } catch (IOException e) {
            status.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
            status.setStyle("-fx-text-fill: red");
        }
    }
}