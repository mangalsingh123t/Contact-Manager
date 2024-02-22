package com.contactmanager.contanctmanager.Entites;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Contact {
    
    @Id
    private String cid = UUID.randomUUID().toString();
    private String name;
    private String surname;

    private String work;

    public Contact(String cid, String name, String surname, String work, String email, String phone, String image,
            String discription, User user) {
        this.cid = cid;
        this.name = name;
        this.surname = surname;
        this.work = work;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.discription = discription;
        this.user = user;
    }

    public Contact() {
        //TODO Auto-generated constructor stub
    }

    private String email;

    private String phone;

    @Override
    public String toString() {
        return "Contact [cid=" + cid + ", name=" + name + ", surname=" + surname + ", work=" + work + ", email=" + email
                + ", phone=" + phone + ", image=" + image + ", discription=" + discription + ", user=" + user + "]";
    }

    private String image;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private String discription;

    @ManyToOne
    // isse yh data serialize nhi hoga ydi ye json me convert hota he to dono user ki cycle bn jayegi
    @JsonIgnore
    private User user;
    
   

}