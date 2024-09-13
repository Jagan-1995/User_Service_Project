package dev.jagan.userservice.dtos;

import dev.jagan.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseDto {
    private String name;
    private String email;
//    private User user;
    private ResponseStatus responseStatus;
}
