package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class DevProfileController {
    @FXML private Circle profileNote;
    @FXML private Circle profileBam;
    @FXML private Circle profileAlfa;
    @FXML private Circle profileTonkla;

    @FXML
    public void initialize() {
        Image image1 = new Image(getClass().getResource("/images/dev_note.png").toString());
        Image image2 = new Image(getClass().getResource("/images/dev_bam.png").toString());
        Image image3 = new Image(getClass().getResource("/images/dev_alfa.png").toString());
        Image image4 = new Image(getClass().getResource("/images/dev_tonkla.png").toString());

        profileNote.setFill(new ImagePattern(image1));
        profileBam.setFill(new ImagePattern(image2));
        profileAlfa.setFill(new ImagePattern(image3));
        profileTonkla.setFill(new ImagePattern(image4));

        profileNote.setStroke(Color.web("#006c67"));
        profileNote.setStrokeWidth(1.5);

        profileBam.setStroke(Color.web("#006c67"));
        profileBam.setStrokeWidth(1.5);

        profileAlfa.setStroke(Color.web("#006c67"));
        profileAlfa.setStrokeWidth(1.5);

        profileTonkla.setStroke(Color.web("#006c67"));
        profileTonkla.setStrokeWidth(1.5);
    }

    @FXML
    public void onBackButtonClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
