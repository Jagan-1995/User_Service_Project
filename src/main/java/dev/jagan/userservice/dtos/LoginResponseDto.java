package dev.jagan.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LoginResponseDto {
    private String tokenValue;
    private Date expiryAt;
    private String email;

}
