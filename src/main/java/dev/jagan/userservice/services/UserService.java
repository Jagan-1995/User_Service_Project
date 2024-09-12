package dev.jagan.userservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jagan.userservice.dtos.SendEmailEventDto;
import dev.jagan.userservice.models.Token;
import dev.jagan.userservice.models.User;
import dev.jagan.userservice.repositories.TokenRepository;
import dev.jagan.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserRepository userRepository,
                       TokenRepository tokenRepository,
                       KafkaTemplate kafkaTemplate,
                       ObjectMapper objectMapper){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public User signup(String fullName,
                       String email,
                       String password) throws JsonProcessingException {
        User user = new User();
        user.setName(fullName);
        user.setEmail(email);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));

        userRepository.save(user);
        /*
        I want to publish this event to the message queue - kafka
         */
        SendEmailEventDto emailEventDto = new SendEmailEventDto();
        emailEventDto.setTo(email);
        emailEventDto.setFrom("jackjagan1995@gmail.com");
        emailEventDto.setSubject("Welcome To Scaler Project of Email Service");
        emailEventDto.setBody("Welcome, we are very happy and excited to part of the Email Service Project");


            kafkaTemplate.send(
                    "send_Email",
                    objectMapper.writeValueAsString(emailEventDto)
            );


        // Kafka queue - [{send_Email, {"to" : "", from : "", "body" : ""}}]

        return user;
    }


    public Token login(String email,
                       String password){
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()){
            // throws user not exsits exception
            return null;
        }

        // user exsits
        User user = optionalUser.get();
        // validate his password
        // bCrypt gives a new hash every time even for the same password

        if (!bCryptPasswordEncoder.matches(password, user.getHashedPassword())){
            // password is invalid, throw password invalid exception
            return  null;
        }

        // business logic of how much time you need a token for
        // today + 30days

        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysLater = currentDate.plusDays(30);

        Date expiryAt = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setExpiryAt(expiryAt);
        token.setUser(user);
        // JWT -> A,B,C
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        Token savedToken = tokenRepository.save(token);
        return savedToken;
    }

    public void logout(String token){

        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedEquals(token, false);

        if (optionalToken.isEmpty()){
            // return token doesn't exist exception
            return;
        }
        Token tkn = optionalToken.get();
        tkn.setDeleted(true); // soft Deleted

        tokenRepository.save(tkn);
        return;
    }

    public User validateToken(String token){
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedEqualsAndExpiryAtGreaterThan(token, false, new Date());

        if (optionalToken.isEmpty()){
            // token is Invalid
            return null;
        }

        return optionalToken.get().getUser();
    }


}
