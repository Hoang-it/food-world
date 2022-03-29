package hoang.fw.foodworld;

import hoang.fw.foodworld.dto.UserDTO;
import hoang.fw.foodworld.entities.User;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FoodWorldApplicationTests {
    @Autowired
    private ModelMapper mapper;

    @Test
    void contextLoads() {
        UserDTO userDto = new UserDTO();
        userDto.setEmail("Hoang");
        User user = mapper.map(userDto, User.class);
        System.out.println(user.getEmail());
    }

}
