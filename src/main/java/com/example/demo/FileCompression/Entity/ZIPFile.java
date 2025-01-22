package com.example.demo.FileCompression.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@Document(collection = "zip_files")
public class ZIPFile {

    @Id
    private int id;
    private String username;
    private String fileName;

    private String contentType;
    private long size;

    @JsonIgnore
    private byte[] content;
    private String compressionStatus;


    @Override
    public String toString() {
        return "ZIPFile{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", size=" + size +
                ", content=" + Arrays.toString(content) +
                ", compressionStatus='" + compressionStatus + '\'' +
                '}';
    }

    public ZIPFile() {
    }

    public ZIPFile(int id, String username, String fileName, String contentType, long size, byte[] content, String compressionStatus) {
        this.id = id;
        this.username = username;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.content = content;
        this.compressionStatus = compressionStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getCompressionStatus() {
        return compressionStatus;
    }

    public void setCompressionStatus(String compressionStatus) {
        this.compressionStatus = compressionStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}