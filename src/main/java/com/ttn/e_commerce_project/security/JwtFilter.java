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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.nonNull;

@Component
@Slf4j
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
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> inside the jwt filter");
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        try {
            if (nonNull(authHeader) && authHeader.startsWith(BEARER_HEADER)) {
                String jwt = authHeader.substring(7);
                if (jwtService.validateToken(jwt)) {
                    String username = jwtService.getUsername(jwt);
                    List<String> roles = jwtService.getRoles(jwt);
                    var authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                    var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.info(">>>>>>>>>>>>>>>>>>>>>>>> jwt filter executed successfully");
                }
            }
        } catch (Exception e) {
            logger.error("JWT Filter Error");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            return; // stop filter chain here
        }
        filterChain.doFilter(request, response);
    }
}

