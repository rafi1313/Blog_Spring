package com.example.blog.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "musisz podać temat wiedomości")
    private String subject;
    @NotBlank(message = "musisz podać treść wiadomości")
    private String message;
    //@Pattern(regexp = "[\\p{L}0-9]+@[a-z0-9]+\\.[a-z]{2,3}", message = "niepoprawny format adresu email")
    @NotBlank(message = "musisz podać adres email")
    @Email
    private String email;

    public Contact(String subject, String message, String email) {
        this.subject = subject;
        this.message = message;
        this.email = email;
    }
    public Contact() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
