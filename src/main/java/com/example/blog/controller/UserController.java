package com.example.blog.controller;

import com.example.blog.model.entity.User;
import com.example.blog.model.form.PasswordChangeForm;
import com.example.blog.model.form.RegisterUserForm;
import com.example.blog.service.AutoMailingService;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.jws.WebParam;
import javax.validation.Valid;

@Controller
public class UserController {


    UserService userService;
    AutoMailingService autoMailingService;

    @Autowired
    public UserController(UserService userService, AutoMailingService autoMailingService) {
        this.userService = userService;
        this.autoMailingService = autoMailingService;
    }

    @GetMapping("/register")
    public String register(Model model){
        RegisterUserForm registerUserForm = new RegisterUserForm();
        model.addAttribute("registerUserForm", registerUserForm);
        return "registerForm";
    }
    @PostMapping("/register")
    public String register(@ModelAttribute @Valid RegisterUserForm registerUserForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return "registerForm";
        }
        // zapis przez klasę z service
        User registeredUser = userService.createUser(registerUserForm);
        return "redirect:/";
    }
    @GetMapping("/changePassword")
    public String changePassword(Model model){
        // utworzenie UserPasswordForm
        PasswordChangeForm passwordChangeForm = new PasswordChangeForm();
        model.addAttribute("passwordChangeForm",passwordChangeForm);
        return "changePassword";
    }
    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute @Valid PasswordChangeForm passwordChangeForm,
                                 BindingResult bindingResult,
                                 Authentication auth){
        if(bindingResult.hasErrors()){
            return "changePassword";
        }
        UserDetails loggedUser = (UserDetails) auth.getPrincipal();
        // zlogowano na adres currentEmail
        String currentEmail = loggedUser.getUsername();
        // zwróć użytkownika - obiekt user którego zalogowano
        User currentUser = userService.getUser(currentEmail);
        System.out.println("aktualne: "+currentUser.getPassword());
        System.out.println("zmienione: "+passwordChangeForm.getPassword1());
        // update
        userService.changePassword(passwordChangeForm, currentUser.getId());
        return "changePassword";
    }


}
