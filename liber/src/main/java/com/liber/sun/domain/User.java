package com.liber.sun.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by SongJie on 2019/3/8 15:25
 */
@Document(collection = "User")
public class User {
    @Id  // automatically generated
    private String id;
    private String userId;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String phone;
    private String affiliation;
    private String country;
    private String city;
    private String personalKeywords;
    private Date registerTime;

    public User() {
    }

    public User(String id, String userId, String name, String email, String password, String gender, String phone, String affiliation, String country, String city, String personalKeywords, Date registerTime) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.phone = phone;
        this.affiliation = affiliation;
        this.country = country;
        this.city = city;
        this.personalKeywords = personalKeywords;
        this.registerTime = registerTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", affiliation='" + affiliation + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", personalKeywords='" + personalKeywords + '\'' +
                ", registerTime=" + registerTime +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPersonalKeywords() {
        return personalKeywords;
    }

    public void setPersonalKeywords(String personalKeywords) {
        this.personalKeywords = personalKeywords;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }
}
