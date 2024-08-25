package com.treadingPlatformApplication.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtProvider {

    private static final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.JWT_SECRET_KEY.getBytes());

    public static String generateToken(Authentication authentication){

        Collection<? extends GrantedAuthority>authorities = authentication.getAuthorities();
        String role = populateAutherities(authorities);

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+86400000))
                .claim("email",authentication.getName())
                .claim("authorities",role)
                .signWith(key)
                .compact();
    }
    public static String getEmailFromToken(String token){
        try{
            if(token!=null && token.startsWith("Bearer ")){
                token = token.substring(7);
            }
            Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
            String email = String.valueOf(claims.get("email"));

            if(email!=null){
                return email;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    private static String populateAutherities(Collection<? extends GrantedAuthority> authorities) {

        Set<String> auths = new HashSet<>();

        for(GrantedAuthority auth:authorities){
               auths.add(auth.getAuthority());
        }
        return String.join(",",auths);
    }
}
