package com.example.demo.FileCompression.Service;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class PDFCompressor {

    // Method to compress the PDF
    public byte[] compressPDF(File inputFile) throws IOException {
        PDDocument document = PDDocument.load(inputFile);
        ByteArrayOutputStream compressedStream = new ByteArrayOutputStream();

        // Optimize images in the PDF
        for (PDPage page : document.getPages()) {
            PDResources resources = page.getResources();
            for (COSName xObjectName : resources.getXObjectNames()) {
                PDXObject xObject = resources.getXObject(xObjectName);
                if (xObject instanceof PDImageXObject) {
                    PDImageXObject image = (PDImageXObject) xObject;

                    // Downscale image resolution
                    BufferedImage bufferedImage = image.getImage();
                    BufferedImage scaledImage = new BufferedImage(
                            bufferedImage.getWidth() / 2,
                            bufferedImage.getHeight() / 2,
                            BufferedImage.TYPE_INT_RGB
                    );

                    Graphics2D g2d = scaledImage.createGraphics();
                    g2d.drawImage(bufferedImage, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), null);
                    g2d.dispose();

                    // Replace the original image with the compressed image
                    PDImageXObject compressedImage = LosslessFactory.createFromImage(document, scaledImage);
                    resources.put(xObjectName, compressedImage);
                }
            }
        }

        // Strip unnecessary elements like metadata
//        document.getDocumentCatalog().getMetadata().setFilter(null);
//        document.setAllSecurityToBeRemoved(true);

        document.save(compressedStream);
        document.close();

        return compressedStream.toByteArray();
    }

    public byte[] compressToZip(File inputFile) throws IOException {
        // Output file (ZIP archive)
        File outputFile = new File(inputFile.getParent(), inputFile.getName() + ".zip");


        try (FileOutputStream fos = new FileOutputStream(outputFile);
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(inputFile)) {


            ZipEntry zipEntry = new ZipEntry(inputFile.getName());
            zos.putNextEntry(zipEntry);


            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, length);
            }


            zos.closeEntry();
        }

        // Read the created ZIP file into a byte array
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             FileInputStream fis = new FileInputStream(outputFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            return byteArrayOutputStream.toByteArray();
        }
    }
}

