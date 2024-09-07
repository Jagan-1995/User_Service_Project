package dev.jagan.userservice.dtos;

import dev.jagan.userservice.models.Role;
import dev.jagan.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private String name;
    private String email;
    private List<Role> roles;
    private boolean isEmailVerified;

    public static UserDto from(User user){
        if (user == null) return null;

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setRoles(user.getRoles());
        userDto.setEmailVerified(user.isEmailVerified());

        return userDto;

    }
}
