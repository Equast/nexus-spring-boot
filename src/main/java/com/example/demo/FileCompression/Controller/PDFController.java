package com.example.demo.FileCompression.Controller;

import com.example.demo.FileCompression.Entity.PDFFile;
import com.example.demo.FileCompression.Entity.PDFFileList;
import com.example.demo.FileCompression.Service.PDFCompressor;
import com.example.demo.FileCompression.Service.PDFFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pdf/{username}")
public class PDFController {

    @Autowired
    private PDFCompressor pdfCompressor;

    @Autowired
    private PDFFileListService pdfFileListService;


    @PostMapping("/compress")
    public String compressPDF(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        try {

            File tempFile = new File(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());
            file.transferTo(tempFile);


            byte[] compressedContent = pdfCompressor.compressPDF(tempFile);


            int fid = pdfFileListService.getAllPDFFiles().getPdfFiles().size();
            PDFFile compressedPDF = new PDFFile(
                    fid++,
                    username,
                    file.getOriginalFilename(),
                    "application/pdf",
                    compressedContent.length,
                    compressedContent,
                    "compressed"
            );


            pdfFileListService.addPDFFile(compressedPDF);


            return "PDF successfully compressed and stored in the list.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error compressing PDF: " + e.getMessage();
        }
    }


    @GetMapping("/list")
    public Map<String, List<Map<String, Object>>> getPdfList() {
        PDFFileList list = pdfFileListService.getAllPDFFiles();

        List<Map<String, Object>> pdfDetails = new ArrayList<>();

        for (PDFFile pdfFile : list.getPdfFiles()) {
            Map<String, Object> pdfMap = new HashMap<>();
            pdfMap.put("id", pdfFile.getId());
            pdfMap.put("username", pdfFile.getUsername());
            pdfMap.put("fileName", pdfFile.getFileName());
            pdfMap.put("contentType", pdfFile.getContentType());
            pdfMap.put("size", pdfFile.getSize());
            pdfMap.put("compressionStatus", pdfFile.getCompressionStatus());
            pdfDetails.add(pdfMap);
        }

        Map<String, List<Map<String, Object>>> response = new HashMap<>();
        response.put("pdfFiles", pdfDetails);
        return response;
    }



    @GetMapping("/file/{index}")
    public PDFFile getPDFFile(@PathVariable int index) {
        // Retrieve the PDF file based on the index
        return pdfFileListService.getPDFFile(index);
    }

    @GetMapping("/view/{index}")
    public ResponseEntity<byte[]> viewPDFFile(@PathVariable int index) {
        try {
            PDFFile pdfFile = pdfFileListService.getPDFFile(index); // Get the PDF file by index

            if (pdfFile == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }


            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pdfFile.getFileName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, pdfFile.getContentType());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfFile.getContent()); // Return the file content
        } catch (IndexOutOfBoundsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}

