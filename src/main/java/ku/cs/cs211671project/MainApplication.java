package ku.cs.cs211671project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;

import java.io.IOException;public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "KURequest", 950, 680);
        configRoutes();
        FXRouter.goTo("login");
    }

    private void configRoutes() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("login", viewPath + "login.fxml");
        FXRouter.when("dev-profile", viewPath + "dev-profile.fxml");
        FXRouter.when("register", viewPath + "register.fxml");
        FXRouter.when("admin-manage-info-table", viewPath + "admin-manage-info-table.fxml");
        FXRouter.when("admin-manage-faculty-and-major-info", viewPath + "admin-manage-faculty-and-major-info.fxml");
        FXRouter.when("admin-manage-staff-info", viewPath + "admin-manage-staff-info.fxml");
        FXRouter.when("major-staff-info-table", viewPath + "major-staff-info-table.fxml");
        FXRouter.when("advisor-info", viewPath + "advisor-info.fxml");
        FXRouter.when("faculty-info-table", viewPath + "faculty-info-table.fxml");
        FXRouter.when("edit-new-password-staff", viewPath + "edit-new-password-staff.fxml");
        FXRouter.when("add-staff", viewPath + "add-staff.fxml");
        FXRouter.when("edit-staff", viewPath + "edit-staff.fxml");
        FXRouter.when("add-approver", viewPath + "add-approver.fxml");
        FXRouter.when("edit-approver", viewPath + "edit-approver.fxml");
        FXRouter.when("add-student", viewPath + "add-student.fxml");
        FXRouter.when("edit-student", viewPath + "edit-student.fxml");
        FXRouter.when("set-profile", viewPath + "set-profile.fxml");
        FXRouter.when("ban-user", viewPath + "ban-user.fxml");
        FXRouter.when("add-faculty-and-major", viewPath + "add-faculty-and-major.fxml");
        FXRouter.when("edit-faculty-and-major", viewPath + "edit-faculty-and-major.fxml");
        FXRouter.when("approve-request", viewPath + "approve-requests.fxml");
        FXRouter.when("create-request-student", viewPath + "create-requests-student.fxml");
        FXRouter.when("table-request-student", viewPath + "table-requests-student.fxml");
        FXRouter.when("requests-for-registration", viewPath + "requests-for-register.fxml");
        FXRouter.when("request-general-student", viewPath + "request-general-student.fxml");
        FXRouter.when("request-resignation-student",viewPath + "request-resignation-student.fxml");
        FXRouter.when("table-request-advisor", viewPath + "advisor-info.fxml");
        FXRouter.when("student-request-history-advisor", viewPath + "student-request-history-advisor.fxml");
        FXRouter.when("instruction", viewPath + "instruction.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}