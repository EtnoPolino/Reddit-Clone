package com.reddit.security;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Data
public class JwtProvider {

    private final JwtEncoder jwtEncoder; //Implementations of this interface are responsible for encoding a JSON Web Token

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis; // date de fin de validité de notre token


    public String generateTokenWithUserName(String username){
        JwtClaimsSet claims = JwtClaimsSet
                                .builder()
                                .issuer("self")
                                .subject(username)
                                .issuedAt(Instant.now())
                                .expiresAt(Instant.now().plusMillis(jwtExpirationInMillis))
                                .claim("scope", "ROLE_USER")
                                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(); //on extrait tous les claims du token (subject, issue, expiration)
    }
    public String generateToken(Authentication authentication){
        User principal = (User) authentication.getPrincipal();
        return generateTokenWithUserName(principal.getUsername());
    }

}
