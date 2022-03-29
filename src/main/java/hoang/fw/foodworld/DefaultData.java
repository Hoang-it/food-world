package hoang.fw.foodworld;

import hoang.fw.foodworld.dao.RoleRepository;
import hoang.fw.foodworld.entities.Role;
import hoang.fw.foodworld.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultData implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepo;

    @Override
    public void run(String...args) throws Exception {
        Role admin = roleRepo.findByName(Roles.ADMIN.name());
        Role user = roleRepo.findByName(Roles.USER.name());

        if (admin == null) roleRepo.save(new Role(Roles.ADMIN.name()));
        if (user == null) roleRepo.save(new Role(Roles.USER.name()));

    }
}
