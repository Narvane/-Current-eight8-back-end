package com.myportfolio.eightg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class ApplicationUser {

    @Id
    private String id;

    private String email;

    private String name;

    private String imageUrl;

    private AuthProvider provider;

    private String providerId;

    private Boolean emailVerified = false;

    @JsonIgnore
    private String password = null;

}
