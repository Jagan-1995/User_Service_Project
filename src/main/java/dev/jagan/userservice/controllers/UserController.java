package dev.jagan.userservice.controllers;


import dev.jagan.userservice.dtos.*;
import dev.jagan.userservice.models.Token;
import dev.jagan.userservice.models.User;
import dev.jagan.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users") // localhost:8080/users/...
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    /*
    Take the requests from the users
    1. signup
    2. login
    3. logout
    4. validate token
     */

    @PostMapping("/signup")
    public SignupResponseDto signup(@RequestBody SignupRequestDto signupRequestDto){
        String name = signupRequestDto.getName();
        String email = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();

        // call the user service to register the user and return the user back to the client
        // return the user response dto back to the client

        SignupResponseDto signupResponseDto = new SignupResponseDto();
        try{
            User user = userService.signup(name, email, password);
            signupResponseDto.setEmail(user.getEmail());
            signupResponseDto.setName(user.getName());
            signupResponseDto.setResponseStatus(ResponseStatus.SUCCESS);
        }
        catch (Exception ex){
            signupResponseDto.setResponseStatus(ResponseStatus.FAILURE);
        }


        return signupResponseDto;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto){
        Token token = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setEmail(token.getUser().getEmail());
        loginResponseDto.setTokenValue(token.getValue());
        loginResponseDto.setExpiryAt(token.getExpiryAt());

        return loginResponseDto;

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto){
        userService.logout(logoutRequestDto.getToken());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
