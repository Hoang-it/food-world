package hoang.fw.foodworld.controllers;

import hoang.fw.foodworld.dao.RoleRepository;
import hoang.fw.foodworld.dao.UserRepository;
import hoang.fw.foodworld.dto.UserDTO;
import hoang.fw.foodworld.entities.Role;
import hoang.fw.foodworld.entities.User;
import hoang.fw.foodworld.enums.Provider;
import hoang.fw.foodworld.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(UserDTO user) {
        System.out.println(user.toString());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        User newUser = new User();
        newUser.setPassword(encodedPassword);
        newUser.setUsername(user.getFirstName() + user.getLastName());
        newUser.setEmail(user.getEmail());

        Set<Role> roles = new HashSet<>();
        if (user.isAdmin()) roles.add(roleRepo.findByName(Roles.ADMIN.name()));
        if (user.isUser()) roles.add(roleRepo.findByName(Roles.USER.name()));
        newUser.setRoles(roles);
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEnabled(true);
        newUser.setProvider(Provider.LOCAL);

        userRepo.save(newUser);

        return "register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }


}
