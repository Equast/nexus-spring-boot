package com.example.demo.FileCompression.Service;

import com.example.demo.FileCompression.Entity.PDFFile;
import com.example.demo.FileCompression.Entity.PDFFileList;
import com.example.demo.FileCompression.Entity.ZIPFile;
import com.example.demo.FileCompression.Entity.ZIPFileList;
import com.example.demo.FileCompression.Repository.PDFFileRepository;
import com.example.demo.FileCompression.Repository.ZIPFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PDFFileListService {

    PDFFileList pdfFileList = new PDFFileList(); // In-memory list of PDFFile objects
    ZIPFileList ZIPFileList = new ZIPFileList(); // In-memory list of RARFile objects
    @Autowired
    private PDFFileRepository pdfFileRepository;

    @Autowired
    private ZIPFileRepository zipFileRepository;

    public void addPDFFile(PDFFile pdfFile) {
        pdfFileList.addPDFFile(pdfFile);
        pdfFileRepository.save(pdfFile);
    }


    public PDFFileList getAllPDFFiles() {
        return pdfFileList;
    }

    public PDFFile getPDFFile(int index) {

        if (index >= 0 && index < pdfFileList.getPdfFiles().size()) {
            return pdfFileList.getPdfFiles().get(index);
        }
        return null;
    }


    public void addRARFile(ZIPFile ZIPFile) {
//        ZIPFileList.addZIPFile(ZIPFile);
        zipFileRepository.save(ZIPFile);
    }


    public List<ZIPFile> getAllZIPFiles() {
        return zipFileRepository.findAll();
    }

    public ZIPFile getZIPFile(int index) {
        if (index >= 0 && index < ZIPFileList.getZIPFiles().size()) {
            return zipFileRepository.findById(index).get();
        }
        return null;
    }
}
