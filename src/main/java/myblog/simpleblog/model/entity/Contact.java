package myblog.simpleblog.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank (message = "musisz podać temat wiadomości")
    private String subject;
    @NotBlank (message = "musisz podać treść wiadomości")
    private String message;
    @NotBlank (message = "musisz podać adres email")
    @Email (message = "niepoprawny adres email")
    private String email;

    public Contact(String subject, String message, String email) {
        this.subject = subject;
        this.message = message;
        this.email = email;
    }

    public Contact() {}

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
