package hoang.fw.foodworld.services;

import hoang.fw.foodworld.dao.RoleRepository;
import hoang.fw.foodworld.dao.UserRepository;
import hoang.fw.foodworld.entities.Role;
import hoang.fw.foodworld.enums.Provider;
import hoang.fw.foodworld.entities.User;
import hoang.fw.foodworld.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository roleRepo;

    public void processOAuthPostGoogleLogin(String username) {
        User existUser = repo.getUserByUsername(username);

        if (existUser == null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(username);
            newUser.setFirstName("Hoang");
            newUser.setLastName("Nguyen");
            newUser.setPassword("Nguyen");
            newUser.setProvider(Provider.GOOGLE);
            newUser.setEnabled(true);

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepo.findByName(Roles.USER.name()));
            newUser.setRoles(roles);

            repo.save(newUser);
        }

    }

    public void processOAuthPostFacebookLogin(String username) {
        User existUser = repo.getUserByUsername(username);

        if (existUser == null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(username);
            newUser.setFirstName("Hoang");
            newUser.setLastName("Nguyen");
            newUser.setPassword("Nguyen");
            newUser.setProvider(Provider.FACEBOOK);
            newUser.setEnabled(true);

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepo.findByName(Roles.USER.name()));
            newUser.setRoles(roles);
            repo.save(newUser);
        }

    }

    public void processOAuthPostGithubLogin(String username) {
        User existUser = repo.getUserByUsername(username);

        if (existUser == null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(username);
            newUser.setFirstName("Hoang");
            newUser.setLastName("Nguyen");
            newUser.setPassword("Nguyen");
            newUser.setProvider(Provider.GITHUB);
            newUser.setEnabled(true);

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepo.findByName(Roles.USER.name()));
            newUser.setRoles(roles);

            repo.save(newUser);
        }
    }
}
