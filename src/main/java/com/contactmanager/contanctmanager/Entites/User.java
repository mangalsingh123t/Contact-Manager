package com.contactmanager.contanctmanager.Entites;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User {

    @Id
    private String uid = UUID.randomUUID().toString();

    public User() {
    }

    @Override
    public String toString() {
        return "User [uid=" + uid + ", name=" + name + ", email=" + email + ", role=" + role + ", enable=" + enable
                + ", password=" + password + ", image=" + image + ", about=" + about + "]";
    }

    private String name;
    private String email;
    private String role;
    private boolean enable;
    private String password;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    private String image;
    private String about;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    private List<Contact> contacts = new ArrayList();

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public User(String uid, String name, String email, String role, boolean enable, String password, String image,
            String about, List<Contact> contacts) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.role = role;
        this.enable = enable;
        this.password = password;
        this.image = image;
        this.about = about;
        this.contacts = contacts;
    }

    

}
