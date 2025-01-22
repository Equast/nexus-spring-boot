package com.example.demo.FileCompression.Entity;

import java.util.ArrayList;
import java.util.List;

public class PDFFileList {

    private List<PDFFile> pdfFiles;


    public PDFFileList() {
        this.pdfFiles = new ArrayList<>();
    }


    public void addPDFFile(PDFFile pdfFile) {
        this.pdfFiles.add(pdfFile);
    }


    public List<PDFFile> getPdfFiles() {
        return pdfFiles;
    }


    public void setPdfFiles(List<PDFFile> pdfFiles) {
        this.pdfFiles = pdfFiles;
    }

    @Override
    public String toString() {
        return "PDFFileList{" +
                "pdfFiles=" + pdfFiles +
                '}';
    }
}