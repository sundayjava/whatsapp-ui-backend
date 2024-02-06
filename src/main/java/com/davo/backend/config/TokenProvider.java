package com.davo.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenProvider {
    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    public String generateToken(Authentication authentication){

        return Jwts.builder()
                .issuer("Sunday David Udoekong")
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() * 86400000))
                .claim("email", authentication.getName())
                .signWith(key)
                .compact();
    }

    public String getEmailFromToken(String jwt){
        jwt = jwt.substring(7);
        Claims claim = Jwts.parser().verifyWith(key)
                .build().parseSignedClaims(jwt).getPayload();

        return String.valueOf(claim.get("email"));
    }
}
