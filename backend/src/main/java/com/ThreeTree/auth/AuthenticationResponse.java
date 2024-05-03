package com.ThreeTree.auth;

import com.ThreeTree.dto.NewPersonResponse;
import com.ThreeTree.model.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    private String token;
    private Date expirationTime;
    private boolean success;
    private NewPersonResponse user;
}

