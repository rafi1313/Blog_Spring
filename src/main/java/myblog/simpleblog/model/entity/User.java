package myblog.simpleblog.model.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

// adnotacja ORM - mapowanie obiektowo-relacyjne
@Entity
public class User {
    // @Id - PK (primary key)
    // @GeneratedValue - autoinkrementacja
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    //@Column(name="passwd") => zmienia nazwę columny w DB
    private String password;
    private String firstname;
    private String lastname;
    //wpisuje aktualną datę i czas
    private Date added = new Date();
    //domyślnie true
    private boolean active;
    @ManyToMany
    @JoinTable(name = "user_role")
    private Set<Role> roles;

    public User(String email, String password, String firstname, String lastname) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.added = null;
        this.active = true;
    }

    public User() {}

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
