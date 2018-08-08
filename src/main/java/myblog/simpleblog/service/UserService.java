package myblog.simpleblog.service;

import myblog.simpleblog.model.entity.Role;
import myblog.simpleblog.model.entity.User;
import myblog.simpleblog.model.form.PasswordChangeForm;
import myblog.simpleblog.model.form.RegisterUserForm;
import myblog.simpleblog.repository.RoleRepository;
import myblog.simpleblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User createUser(RegisterUserForm registerUserForm){
        User user = new User();
        user.setFirstname(registerUserForm.getFirstname());
        user.setLastname(registerUserForm.getLastname());
        user.setEmail(registerUserForm.getEmail());
        user.setPassword(registerUserForm.getPassword());
        //szyfrujemy hasło
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        System.out.println("Hasło: "+user.getPassword());
        //przypisanie roli do usera =. ROLE_USER
        Role role = roleRepository.findOneByRoleName("ROLE_USER");
        //dodaje role do zbioru
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(role);
        user.setRoles(roleSet);
//        User savedUser = userRepository.save(user);
        return userRepository.save(user);
    }
    public User changePassword(PasswordChangeForm passwordChangeForm, Long id){
        //metoda Hibernate do modyfikacji usera
        User modifiedUser =userRepository.getOne(id);
        //przepisanie wartości hasła
        modifiedUser.setPassword(bCryptPasswordEncoder.encode(passwordChangeForm.getPassword1()));
        //update
        return userRepository.save(modifiedUser);
    }
    public User getUser(String email){
        return userRepository.findOneByEmail(email);
    }
}
