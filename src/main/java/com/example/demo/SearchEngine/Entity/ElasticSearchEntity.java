package com.example.demo.SearchEngine.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import java.util.Date;
import java.util.List;

@Document(indexName = "doctors") // Specify the index name as "doctors"
public class ElasticSearchEntity {

    @Id
    private String id; // Elasticsearch _id field

    private String name; // Inferred as Text by default

    private List<String> specialization; // Inferred as Text by default

    private List<String> qualification; // Inferred as Text by default

    private int experience; // Inferred as Integer by default

    private List<String> clinics; // Inferred as Text by default

    private ContactInfo contactInfo; // Inferred as Object by default

    private Date joiningDate; // Inferred as Date by default

    private String profileImage; // Inferred as Text by default

    private String about; // Inferred as Text by default

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSpecialization() {
        return specialization;
    }

    public void setSpecialization(List<String> specialization) {
        this.specialization = specialization;
    }

    public List<String> getQualification() {
        return qualification;
    }

    public void setQualification(List<String> qualification) {
        this.qualification = qualification;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<String> getClinics() {
        return clinics;
    }

    public void setClinics(List<String> clinics) {
        this.clinics = clinics;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    // Inner class for ContactInfo
    public static class ContactInfo {

        private String email; // Inferred as Text by default

        private String phone; // Inferred as Text by default

        // Getters and Setters

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
