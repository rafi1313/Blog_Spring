package com.example.blog.service;

import com.example.blog.model.entity.Role;
import com.example.blog.model.entity.User;
import com.example.blog.model.form.PasswordChangeForm;
import com.example.blog.model.form.RegisterUserForm;
import com.example.blog.repository.RoleRepisitory;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    UserRepository userRepository;
    RoleRepisitory roleRepisitory;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository, RoleRepisitory roleRepisitory) {
        this.userRepository = userRepository;
        this.roleRepisitory = roleRepisitory;
    }
    public User createUser(RegisterUserForm registerUserForm){
        User user = new User();
        user.setFirstname(registerUserForm.getFirstname());
        user.setLastname(registerUserForm.getLastname());
        user.setEmail(registerUserForm.getEmail());
        user.setPassword(registerUserForm.getPassword());
        //szyfrujemy hasło
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        System.out.println("Zaszyfrowane: "+user.getPassword());
        // przypisanie roli do usera -> ROLE_USER
        Role role = roleRepisitory.findOneByRoleName("ROLE_USER");
        // tworze pusty zbiór
        Set<Role> roleSet = new HashSet<Role>();
        // dodaję role zwracaną przez zapytanie do zbioru
        roleSet.add(role);
        user.setRoles(roleSet);
        User savedUser = userRepository.save(user);
        return savedUser;
    }
    public User getUser(String email){
        User user = userRepository.findOneByEmail(email);
        return user;
    }
    public User changePassword(PasswordChangeForm passwordChangeForm, Long id){
        // metoda Hibernae do modyfikacji usera
        User modifiedUser = userRepository.getOne(id);
        // kodowanie hasła
        // przepisanie wartości hasła
        modifiedUser.setPassword(bCryptPasswordEncoder.encode(passwordChangeForm.getPassword1()));
        // update
        return userRepository.save(modifiedUser);
    }
}
