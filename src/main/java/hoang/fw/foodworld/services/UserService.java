package hoang.fw.foodworld.services;

import hoang.fw.foodworld.dao.RoleRepository;
import hoang.fw.foodworld.dao.UserRepository;
import hoang.fw.foodworld.entities.Role;
import hoang.fw.foodworld.enums.Provider;
import hoang.fw.foodworld.entities.User;
import hoang.fw.foodworld.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

    private User createUser(String username){
        // Encode passwork
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(username);

        // Specify role for user
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepo.findByName(Roles.USER.name()));

        // Create new user
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(username);
        newUser.setPassword(encodedPassword);
        newUser.setEnabled(true);
        newUser.setRoles(roles);
        return newUser;
    }

    public void processOAuthPostGoogleLogin(String username) {
        User existUser = repo.getUserByUsername(username);

        if (existUser == null) {
            User newUser = createUser(username);
            newUser.setProvider(Provider.GOOGLE);
            repo.save(newUser);
        }
    }

    public void processOAuthPostFacebookLogin(String username) {
        User existUser = repo.getUserByUsername(username);

        if (existUser == null) {
            User newUser = createUser(username);
            newUser.setProvider(Provider.FACEBOOK);
            repo.save(newUser);
        }

    }

    public void processOAuthPostGithubLogin(String username) {
        User existUser = repo.getUserByUsername(username);

        if (existUser == null) {
            User newUser = createUser(username);
            newUser.setProvider(Provider.GITHUB);
            newUser.setEnabled(true);
            repo.save(newUser);
        }
    }
}
