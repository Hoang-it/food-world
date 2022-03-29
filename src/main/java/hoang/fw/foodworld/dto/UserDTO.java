package hoang.fw.foodworld.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Transient;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private String password;
    private boolean admin;
    private boolean user;
    private String avatar;
    private boolean enabled;

    @Transient
    public String getPhotosImagePath() {
        if (avatar == null || id == null) return null;

        return "/user-photos/" + id + "/" + avatar;
    }
}
