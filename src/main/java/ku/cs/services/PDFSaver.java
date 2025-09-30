package ku.cs.services;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.File;
import java.io.IOException;


public class PDFSaver {
    private String pdfDirPath;

    public PDFSaver(String pdfDirPath) {
        this.pdfDirPath = pdfDirPath;
    }

    public void savePDF(File inputFile, String id, String type) throws IOException {
        File pdfDir = new File(pdfDirPath);
        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        PDDocument document =Loader.loadPDF(inputFile);
        String outputFileName = pdfDirPath + id + "_" + type + ".pdf";
        document.save(outputFileName);
        document.close();
    }


    public void savePDFRequest(String date, String id, File inputFile) throws IOException {
        File pdfDir = new File(pdfDirPath);
        if (!pdfDir.exists()) {
            pdfDir.mkdir();
        }

        PDDocument document = Loader.loadPDF(inputFile);
        String dateUse = date.replace("/","-").replace(":", "-").replace(" ", "_");
        String outputFileName = pdfDirPath+ dateUse+"_"+ id + ".pdf";
        document.save(outputFileName);
        document.close();
    }

    public void savePDFRequest(String filepath, File inputFile) throws IOException {
        PDDocument document = Loader.loadPDF(inputFile);
        document.save(filepath);
        document.close();
    }
}
