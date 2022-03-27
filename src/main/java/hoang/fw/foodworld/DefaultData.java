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

        roleRepo.save(new Role(Roles.ADMIN.name()));
        roleRepo.save(new Role(Roles.USER.name()));
    }
}
