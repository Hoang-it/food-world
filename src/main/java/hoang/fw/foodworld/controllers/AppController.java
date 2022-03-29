package hoang.fw.foodworld.controllers;

import hoang.fw.foodworld.FileUploadUtil;
import hoang.fw.foodworld.dao.RoleRepository;
import hoang.fw.foodworld.dao.UserRepository;
import hoang.fw.foodworld.dto.UserDTO;
import hoang.fw.foodworld.entities.Role;
import hoang.fw.foodworld.entities.User;
import hoang.fw.foodworld.enums.Provider;
import hoang.fw.foodworld.enums.Roles;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private ModelMapper mapper;

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
        // Encode passwork
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        // Get roles for user
        Set<Role> roles = new HashSet<>();
        if (user.isAdmin()) roles.add(roleRepo.findByName(Roles.ADMIN.name()));
        if (user.isUser()) roles.add(roleRepo.findByName(Roles.USER.name()));

        // Create new user
        User newUser = mapper.map(user, User.class);                    // mapping dto to entity
        newUser.setPassword(encodedPassword);                           // set password encoded
        newUser.setRoles(roles);                                        // set roles
        newUser.setEnabled(true);                                       // set enable
        newUser.setProvider(Provider.LOCAL);                            // set provider

        // save user
        userRepo.save(newUser);

        return "register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("listUsers", listUsers);
        model.addAttribute("user", auth.getPrincipal());

        return "users";
    }

    @GetMapping("/user")
    public String userPage(@RequestParam("userId") long userId, Model model) {
        UserDTO user = mapper.map(userRepo.getById(userId), UserDTO.class);
        model.addAttribute("user", user);
        return "user_page";
    }

    @PostMapping("/user/upload_avatar")
    public RedirectView saveUser(@RequestParam("image") MultipartFile multipartFile,
                                 @RequestParam("userId") long userId) throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        User user = userRepo.getById(userId);

        user.setAvatar(fileName);
        User savedUser = userRepo.save(user);

        String uploadDir = "user-photos/" + savedUser.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return new RedirectView("/user?userId=" + savedUser.getId(), true);
    }

}
