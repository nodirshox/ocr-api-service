package com.microservice.ocr.task;

import com.lowagie.text.List;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class TaskService {
    public Task createTask(Task task) throws IOException, TesseractException {
        // download file
        String fileName = "example.png";
        File file = new File(fileName);

        // Load file into PDFBox class
        PDDocument document = PDDocument.load(file);
        PDFTextStripper stripper = new PDFTextStripper();
        String strippedText = stripper.getText(document);

        // Check text exists into the file
        if (strippedText.trim().isEmpty()){
            strippedText = extractTextFromScannedDocument(document);
        }

        return new Task(strippedText);
    }

    private String extractTextFromScannedDocument(PDDocument document)
            throws IOException, TesseractException {

        // Extract images from file
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();

        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("/usr/share/tessdata/");
        tesseract.setLanguage("ita"); // choose your language

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

            // Create a temp image file
            File temp = File.createTempFile("tempfile_" + page, ".png");
            ImageIO.write(bim, "png", temp);

            String result = tesseract.doOCR(temp);
            out.append(result);

            // Delete temp file
            temp.delete();
        }

        return out.toString();
    }
}
