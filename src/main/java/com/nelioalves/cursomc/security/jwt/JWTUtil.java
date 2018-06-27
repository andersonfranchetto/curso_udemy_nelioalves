package com.nelioalves.cursomc.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }

    public boolean tokenValid(String token){
        Claims claims = getClaims(token);
        if(claims != null){
            if(claims.getSubject() != null
                    && claims.getExpiration() != null
                    && new Date(System.currentTimeMillis()).before(claims.getExpiration())){
                return true;
            }
        }
        return false;
    }

    public String getUsername(String token){
        if(getClaims(token) != null){
            return getClaims(token).getSubject();
        }
        return null;
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
        } catch (Exception e){
            return null;
        }
    }
}
