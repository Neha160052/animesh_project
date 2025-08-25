package com.ttn.e_commerce_project.security;

import com.ttn.e_commerce_project.service.impl.JwtService;
import com.ttn.e_commerce_project.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
public class JwtFilter extends OncePerRequestFilter {

    JwtService jwtService;

    public static final String BEARER_HEADER = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (nonNull(authHeader) && authHeader.startsWith(BEARER_HEADER)) {
            String jwt = authHeader.substring(7);
            if (jwtService.validateToken(jwt)) {
                String username = jwtService.getUsername(jwt);
                String role = jwtService.getRoles(jwt);
                var authorities = singletonList(new SimpleGrantedAuthority("ROLE_" + role));
                var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }
}

