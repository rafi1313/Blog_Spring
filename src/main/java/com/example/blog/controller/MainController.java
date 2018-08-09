package com.example.blog.controller;

import com.example.blog.model.entity.Contact;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.AutoMailingService;
import com.example.blog.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class MainController {

    ContactService contactService;
    AutoMailingService autoMailingService;

    @Autowired
    public MainController(ContactService contactService, AutoMailingService autoMailingService) {
        this.contactService = contactService;
        this.autoMailingService = autoMailingService;
    }

    @GetMapping("/")
    public String home(){
//        UserDetails principal = (UserDetails) auth.getPrincipal();
//        System.out.println("Username: "+principal.getUsername());
        return "homePage";
    }
    @GetMapping("/login")
    public String login(){
//        System.out.println("wyswietlenie loginForm");

        return "loginForm";
    }
//
//    @GetMapping("/logout")
//    public String logout()


    @GetMapping("/contact")
    public String contact(Model model){
        // powiązanie obiektu klasy Contact
        // z obiektem contact z szablonu html
        model.addAttribute("contact",new Contact());
        return "contactForm";
    }
    @PostMapping("/contact")
    public String contact(@ModelAttribute @Valid Contact contact,
                          BindingResult bindingResult,
                          Model model){
        String info = "";
        if(bindingResult.hasErrors()){
            info = "występują błędy formularza";
            model.addAttribute("info",info);
            return "contactForm";
        }
        // zapis do DB poprzez ContactService
        contactService.createContact(contact);
        // czyszczenie pól po wysłaniu wiadomości
        // auto-email
        autoMailingService.sendSimpleMessage(
                contact.getEmail(),
                "Potwierdzenie wysłania formularza kontaktowego",
                "Dziękujemy za kontakt. Niezwłocznie się do Ciebie odezwiemy."
                );
        contact.setSubject("");
        contact.setMessage("");
        contact.setEmail("");
        info = "wysłano widomość";
        model.addAttribute("info",info);
        return "contactForm";
    }
}
