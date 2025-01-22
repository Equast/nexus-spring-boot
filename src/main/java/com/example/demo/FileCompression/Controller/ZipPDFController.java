package com.example.demo.FileCompression.Controller;

import com.example.demo.FileCompression.Entity.ZIPFile;
import com.example.demo.FileCompression.Service.PDFCompressor;
import com.example.demo.FileCompression.Service.PDFFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rarpdf/{username}")
public class ZipPDFController {

    @Autowired
    private PDFCompressor pdfCompressor;

    @Autowired
    private PDFFileListService pdfFileListService;

    int fid=0;

    @PostMapping("/compress")
    public String compressToRar(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        try {
            File tempFile = new File(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());
            file.transferTo(tempFile);


            byte[] compressedContent = pdfCompressor.compressToZip(tempFile);


            ZIPFile ZIPFile = new ZIPFile(
                    fid++,
                    username,
                    file.getOriginalFilename() + ".zip",
                    "application/zip",
                    compressedContent.length,
                    compressedContent,
                    "compressed"
            );

            pdfFileListService.addRARFile(ZIPFile);

            return "File successfully compressed and stored in the list.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error compressing file to RAR: " + e.getMessage();
        }
    }

    @GetMapping("/list")
    public List<ZIPFile> getRarFileList() {
        return pdfFileListService.getAllZIPFiles();

//        List<Map<String, Object>> rarDetails = new ArrayList<>();
//
//        for (ZIPFile ZIPFile : list.getRarFiles()) {
//            Map<String, Object> rarMap = new HashMap<>();
//            rarMap.put("id", ZIPFile.getId());
//            rarMap.put("fileName", ZIPFile.getFileName());
//            rarMap.put("contentType", ZIPFile.getContentType());
//            rarMap.put("size", ZIPFile.getSize());
//            rarMap.put("compressionStatus", ZIPFile.getCompressionStatus());
//            rarMap.put("username", ZIPFile.getUsername());
//            rarDetails.add(rarMap);
//        }
//
//        Map<String, List<Map<String, Object>>> response = new HashMap<>();
//        response.put("zipFiles", rarDetails);
//        return response;
    }

    @GetMapping("/file/{index}")
    public ZIPFile getZIPFile(@PathVariable int index) {
        return pdfFileListService.getZIPFile(index);
    }

    @GetMapping("/viewrar/{index}")
    public ResponseEntity<byte[]> viewRarFile(@PathVariable int index) {
        try {
            ZIPFile ZIPFile = pdfFileListService.getZIPFile(index);

            if (ZIPFile == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + ZIPFile.getFileName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, ZIPFile.getContentType());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(ZIPFile.getContent());
        } catch (IndexOutOfBoundsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/view/{index}")
    public ResponseEntity<byte[]> viewUnzippedPDFFile(@PathVariable int index) {
        try {
            ZIPFile ZIPFile = pdfFileListService.getZIPFile(index);

            if (ZIPFile == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }


            File tempZipFile = File.createTempFile("tempZIP", ".zip");
            try (FileOutputStream fos = new FileOutputStream(tempZipFile)) {
                fos.write(ZIPFile.getContent());
            }


            File outputFolder = new File(System.getProperty("java.io.tmpdir"), "extractedPDF");
            if (!outputFolder.exists()) {
                outputFolder.mkdir();
            }


            try (java.util.zip.ZipInputStream zis = new java.util.zip.ZipInputStream(new FileInputStream(tempZipFile))) {
                java.util.zip.ZipEntry zipEntry;
                while ((zipEntry = zis.getNextEntry()) != null) {
                    if (zipEntry.getName().endsWith(".pdf")) {
                        File extractedPDF = new File(outputFolder, zipEntry.getName());


                        try (FileOutputStream fos = new FileOutputStream(extractedPDF)) {
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, length);
                            }
                        }


                        byte[] pdfContent;
                        try (FileInputStream fis = new FileInputStream(extractedPDF)) {
                            pdfContent = fis.readAllBytes();
                        }


                        HttpHeaders headers = new HttpHeaders();
                        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + extractedPDF.getName() + "\"");
                        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

                        return ResponseEntity.ok()
                                .headers(headers)
                                .body(pdfContent);
                    }
                }
            }


            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
