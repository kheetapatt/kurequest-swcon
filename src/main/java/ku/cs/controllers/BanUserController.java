package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BanUserController {
    @FXML private Rectangle imageRectangle;
    @FXML private Label usernameLabel;
    @FXML private Label nameLabel;
    @FXML private Button banButton;
    @FXML private Circle statusCircle;
    @FXML private Label statusLabel;
    @FXML private Label errorLabel;

    private Datasource<UserList> bannedUserListDatasource;
    private UserList bannedUserList;
    private User user;
    @FXML
    public void initialize() {
        Datasource<UserList> userListDatasource = new UserListFileDatasource("data", "user-list.csv");
        bannedUserListDatasource = new UserListFileDatasource("data", "banned-user-list.csv");
        UserList userList = userListDatasource.readData();
        bannedUserList = bannedUserListDatasource.readData();

        String username = (String) FXRouter.getData();
        user = userList.findUserByUsername(username);

        setBanButton(user);
        setStatus(user);
        showInfo(user);
        errorLabel.setText("");
    }

    private void setBanButton(User user) {
        if (bannedUserList.findUserByUsername(user.getUsername()) != null) {
            banButton.setText("เลิกระงับสิทธิ์");
        } else {
            banButton.setText("ระงับสิทธิ์");
        }
    }

    private void setStatus(User user) {
        if (bannedUserList.findUserByUsername(user.getUsername()) != null) {
            statusCircle.setFill(Color.RED);
            statusLabel.setText("ผู้ใช้ถูกระงับสิทธิ์");
        } else {
            statusCircle.setFill(Color.GREEN);
            statusLabel.setText("ผู้ใช้ยังไม่ถูกระงับสิทธิ์");
        }
    }

    private void showInfo(User user) {
        usernameLabel.setText(user.getUsername());
        nameLabel.setText(user.getName());
        InputStream imageStream;
        Image image;
        try {
            imageStream = new FileInputStream("data/user-images/" + user.getImage());
            image = new Image(imageStream);
            imageRectangle.setFill(new ImagePattern(image));
        } catch (FileNotFoundException e) {
            try {
                imageStream = new FileInputStream("data/user-images/" + "default-profile-image.png");
                image = new Image(imageStream);
                imageRectangle.setFill(new ImagePattern(image));
            } catch (FileNotFoundException ex) {
                imageRectangle.setFill(Color.WHITE);
            }
        }
    }

    @FXML
    public void onBanButtonClick() {
        if (bannedUserList.findUserByUsername(user.getUsername()) == null) {
            bannedUserList.addUser(user.getUsername(), user.getPassword(), user.getName(), user.getId()
                    , user.getImage(), user.getFaculty(), user.getMajor(), user.getDate(), user.getRole());
        } else {
            bannedUserList.removeUser(user.getUsername());
        }
        bannedUserListDatasource.writeData(bannedUserList);
        setBanButton(user);
        setStatus(user);
    }

    @FXML
    public void onBackButtonClick() {
        try {
            String role = user.getRole();
            if (role.equals("faculty staff")) {
                FXRouter.goTo("admin-manage-info-table", "faculty staff");
            } else if (role.equals("major staff")) {
                FXRouter.goTo("admin-manage-info-table", "major staff");
            } else if (role.equals("advisor")) {
                FXRouter.goTo("admin-manage-info-table", "advisor");
            } else {
                FXRouter.goTo("admin-manage-info-table", "student");
            }
        } catch (IOException e) {
            errorLabel.setText("ขออภัย ไม่สามารถดำเนินการได้ในขณะนี้");
            errorLabel.setStyle("-fx-text-fill: red");
        }
    }
}
