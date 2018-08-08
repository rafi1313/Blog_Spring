package myblog.simpleblog.controller;

import myblog.simpleblog.model.entity.User;
import myblog.simpleblog.model.form.PasswordChangeForm;
import myblog.simpleblog.model.form.RegisterUserForm;
import myblog.simpleblog.repository.UserRepository;
import myblog.simpleblog.service.UserService;
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
public class UserController {
    UserService userService;
    UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerUserForm", new RegisterUserForm());
        return "registerForm";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Valid RegisterUserForm registerUserForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registerForm";
        }
        //zapis przez klasę service
        userService.createUser(registerUserForm);
        return "redirect:/";
    }

    @GetMapping("/changePassword")
    public String changeUserPassword(Model model, Authentication authentication) {
        PasswordChangeForm passwordChangeForm = new PasswordChangeForm();

        model.addAttribute("passwordChangeForm", passwordChangeForm);

        return "changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute PasswordChangeForm passwordChangeForm, BindingResult bindingResult,Authentication auth) {
//        userService.changeUserPassword(user);
        if (bindingResult.hasErrors()){
            return "changePassword";
        }

        UserDetails loggedUser = (UserDetails) auth.getPrincipal();
        // zalogowano na adres currentEmail
        String currentEmail = loggedUser.getUsername();
//        zwróć użytkownika - obiekt user którego zalogowano
        User currentUser = userService.getUser(currentEmail);
        userService.changePassword(passwordChangeForm,currentUser.getId());
        return "changePassword";
    }
}
