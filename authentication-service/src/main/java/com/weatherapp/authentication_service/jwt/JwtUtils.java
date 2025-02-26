package com.weatherapp.authentication_service.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;


@Component
public class JwtUtils {

    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    @Value("${spring.jwt.jwtCookieName}")
    private String jwtCookie;

    @Value("${spring.jwt.expiration}")
    private long expirationTimeMilliSeconds;
    
    public String extractJwtTokenFromCookie(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if(cookie != null){
            return cookie.getValue();
        }
        return null;
    }

    public ResponseCookie generateJwtCookie(String username){
        String jwtToken = generateJwtToken(username);
        ResponseCookie cookie = ResponseCookie.from(jwtCookie,jwtToken)
                .path("/api")
                .maxAge(24*60*60)
                .httpOnly(false)
                .build();
        return cookie;
    }

    public String generateJwtToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTimeMilliSeconds))
                .signWith(key())
                .compact();
    }

    public ResponseCookie cleanJwtCookie(){
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, "")
                .path("/api")
                .maxAge(0)
                .httpOnly(true)
                .build();
        return cookie;
    }



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

    public boolean validateJwtToken(String token){
        try {
            Jwts.parser().verifyWith((SecretKey) key())
                    .build().parseSignedClaims(token);
            return true;
        }catch (Exception e){
            System.out.println("Invalid JWT Token "+e.getMessage());
        }
        return false;
    }

}
