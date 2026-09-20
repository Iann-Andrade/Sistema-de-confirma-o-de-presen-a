package com.Lista_de_Presenca.Fisioterapia.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Lista_de_Presenca.Fisioterapia.repository.UsuarioRepository;
import com.Lista_de_Presenca.Fisioterapia.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository; // Adicionado

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(jwtService.getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Long usuarioId = Long.parseLong(claims.getSubject());
            String role = claims.get("role", String.class);

            // Busca o usuário no banco para autenticar corretamente
            var usuario = usuarioRepository.findById(usuarioId).orElse(null);

            if (usuario != null) {
                var authorities = java.util.List.of(
                    new SimpleGrantedAuthority("ROLE_" + role)
                );

                var authentication = new UsernamePasswordAuthenticationToken(
                        usuario, // Coloca o objeto Usuário como principal
                        null,
                        authorities
                );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            // Em caso de erro no token, limpa o contexto para o Spring barrar com 401 padrão
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
