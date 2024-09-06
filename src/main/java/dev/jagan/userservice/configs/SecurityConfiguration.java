package dev.jagan.userservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests((requests) -> {
                   try{
                       requests
                               .anyRequest().permitAll()
                               .and().cors().disable()
                               .csrf().disable();
                   }catch (Exception ex){
                       throw new RuntimeException();
                   }
                });
        return http.build();
    }
}
