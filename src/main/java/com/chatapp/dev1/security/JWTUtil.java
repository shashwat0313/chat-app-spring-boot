package com.chatapp.dev1.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JWTUtil {

    private final String JWT_SECRET = "secret123sbkjdvguyfgu67fgi76fk7utfkuyfkmbskergs8gbrgwo3478gfbleriog43784g5456465432165848679845643584tferliugfg4tfe4r5t";
    private final int JWT_EXPIRATION_MS = 86400000; // 24 hours

    public String generateToken(String username, List<String> roles) {
    log.info("gen token username: " + username);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public List<String> getRolesFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        //noinspection unchecked
        return claims.get("roles", List.class);
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);

//          if no exception thrown, the token is okay so return true
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
