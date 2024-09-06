package dev.jagan.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseDto {
    private String name;
    private String email;
    private ResponseStatus responseStatus;
}
