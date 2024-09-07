package dev.jagan.userservice.repositories;

import dev.jagan.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
   @Override
   Token save(Token token);

   Optional<Token> findByValueAndDeletedEqualsAndExpiryAtGreaterThan(String value, boolean isDeleted, Date expiryGreaterThan);

   Optional<Token> findByValueAndDeletedEquals(String token, boolean deleted);
}

/*
5 august 2024 9 pm hdaafdgdretv
 */