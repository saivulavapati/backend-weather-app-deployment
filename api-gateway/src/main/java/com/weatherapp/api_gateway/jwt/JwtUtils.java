package com.weatherapp.api_gateway.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtils {

    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String getSubjectFromJwtToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey)(key()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String token,String username){
        try {
            String subject = Jwts.parser().verifyWith((SecretKey) key())
                    .build().parseSignedClaims(token).getPayload().getSubject();
            return subject.equals(username);
        }catch (Exception e){
            System.out.println("Invalid JWT Token "+e.getMessage());
        }
        return false;
    }

}