package com.treadingPlatformApplication.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidation extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Fetch the path from the request
        String path = request.getRequestURI();

        // Apply JWT validation only for paths that require it (e.g., "/api/**")
        if (path.startsWith("/api/")) {
            // Fetch the JWT from the Authorization header
            String jwt = request.getHeader(JwtConstant.JWT_HEADER);

            if (jwt == null || !jwt.startsWith("Bearer ")) {
                throw new RuntimeException("JWT Token is missing");
            }

            // Remove the "Bearer " prefix
            jwt = jwt.substring(7);

            try {
                // Validate the JWT token
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.JWT_SECRET_KEY.getBytes());
                Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));
                List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorityList);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // Handle JWT parsing or validation errors
                System.out.println(e.getMessage());
                throw new RuntimeException("Invalid JWT Token");
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}

