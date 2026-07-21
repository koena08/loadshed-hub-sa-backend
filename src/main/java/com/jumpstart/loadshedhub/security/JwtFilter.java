package com.jumpstart.loadshedhub.security;

import com.jumpstart.loadshedhub.repository.UserRepository;
<<<<<<< HEAD
import jakarta.servlet.*;
import jakarta.servlet.http.*;
=======
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
>>>>>>> bce1c30216c5b82041fddfb22f56fd1f90b24ccf
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
<<<<<<< HEAD
import java.io.IOException;

@Component @RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwt; private final UserRepository users;
    @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String h=req.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) try { users.findByEmail(jwt.email(h.substring(7))).ifPresent(u -> SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(u,null,u.getAuthorities()))); } catch (RuntimeException ignored) { }
        chain.doFilter(req,res);
=======

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository users;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                users.findByEmail(jwtUtil.extractEmail(header.substring(7))).ifPresent(user ->
                        SecurityContextHolder.getContext().setAuthentication(
                                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())));
            } catch (RuntimeException ignored) { }
        }
        chain.doFilter(request, response);
>>>>>>> bce1c30216c5b82041fddfb22f56fd1f90b24ccf
    }
}
