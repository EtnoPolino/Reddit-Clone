package com.reddit.security;

import com.reddit.exceptions.SpringRedditException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Data
public class JwtProvider {
    /*
    private KeyStore keyStore;
    @PostConstruct
    public void init(){
        try{
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        }catch(KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e){
            throw new SpringRedditException("Exception occured while loading keystore");
        }
    }

    public String generateToken(Authentication authentication){
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey(){
        try{
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        }catch(KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e){
            throw new SpringRedditException("Exception occured while retrieving public key from keystore");
        }
    }
    */

    /*-----------------------------------------------------------------------------------*/
    private final JwtEncoder jwtEncoder; //Implementations of this interface are responsible for encoding a JSON Web Token

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis; // date de fin de validité de notre token


    private String generateTokenWithUserName(String username){
        JwtClaimsSet claims = JwtClaimsSet
                                .builder()
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
