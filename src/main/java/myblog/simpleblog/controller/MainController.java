package myblog.simpleblog.controller;

import myblog.simpleblog.model.entity.Contact;
import myblog.simpleblog.service.AutoMailingService;
import myblog.simpleblog.service.ContactService;
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
    public String home() {
//        UserDetails principal = (UserDetails) auth.getPrincipal();
//        System.out.println(principal.getUsername());
        return "homePage";
    }
    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }

    @GetMapping("/contact")
    public String contact (Model model){
        // powiązanie obiektu klasy Contact z obiektem contact z szablonu html
        model.addAttribute("contact",new Contact());
        return "contactForm";
    }
    @PostMapping("/contact")
    public String contact(@ModelAttribute @Valid Contact contact,
                          BindingResult bindingResult,
                          Model model){
        String info = "";
        if (bindingResult.hasErrors()){
            info = "występują błędy formularza";
            model.addAttribute("info",info);
            return "contactForm";
        }
        //zapis do DB poprzez ContactService
        contactService.createContact(contact);

        autoMailingService.sendSimpleMessage(
                contact.getEmail(),
                "Potwierdzenie wysłania formularza kontaktowego",
                "Dziękujemy za kontakt. Niezwłocznie się do Ciebie odezwiemy.");
//        System.out.println(contact.getEmail());
        //czyszczenie pól po wysłaniu wiadomości
        contact.setEmail("");
        contact.setMessage("");
        contact.setSubject("");
        info = "poprawnie wysłana wiadomość";
        model.addAttribute("info",info);
        return "contactForm";
    }
}
