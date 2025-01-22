package com.example.demo.FileCompression.Entity;

import java.util.ArrayList;
import java.util.List;

public class ZIPFileList {

    private List<ZIPFile> ZIPFiles;

    public ZIPFileList() {
        this.ZIPFiles = new ArrayList<>();
    }

    public void addZIPFile(ZIPFile ZIPFile) {
        this.ZIPFiles.add(ZIPFile);
    }

    public List<ZIPFile> getZIPFiles() {
        return ZIPFiles;
    }

    public void setRarFiles(List<ZIPFile> ZIPFiles) {
        this.ZIPFiles = ZIPFiles;
    }

    @Override
    public String toString() {
        return "ZIPFileList{" +
                "zipFiles=" + ZIPFiles +
                '}';
    }
}