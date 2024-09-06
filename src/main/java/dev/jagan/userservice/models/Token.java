package dev.jagan.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Token extends BaseModel{
    private String value;
    @ManyToOne
    private User user;
    private Date expiryAt;

}

/*
  1          1
 Token ---- User => M : 1
   M         1
 */