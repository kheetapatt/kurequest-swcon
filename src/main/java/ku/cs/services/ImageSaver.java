package ku.cs.services;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSaver {
    private String imageDirPath;

    public ImageSaver(String imageDirPath) {
        this.imageDirPath = imageDirPath;
    }

    public void saveImage(File inputFile) throws IOException {
        File imageDirectory = new File(imageDirPath);
        if (!imageDirectory.exists()) {
            imageDirectory.mkdir();
        }

        BufferedImage image = null;
        image = ImageIO.read(inputFile);

        File outputFile = new File(imageDirPath + inputFile.getName());
        ImageIO.write(image, "png", outputFile);
    }

}
