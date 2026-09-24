package com.Lista_de_Presenca.Fisioterapia.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Lista_de_Presenca.Fisioterapia.repository.UsuarioRepository;
import com.Lista_de_Presenca.Fisioterapia.service.JwtService;

@Configuration
public class SecurityConfig {

    private final UsuarioRepository usuarioRepository;

    public SecurityConfig(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 1. Libera endpoints de autenticação
                .requestMatchers("/usuarios/cadastrar", "/usuarios/login", "/favicon.ico").permitAll()
                
                // 2. Libera os arquivos visuais e recursos estáticos (HTML, CSS, JS)
                .requestMatchers("/", "/index.html", "/cronograma.html", "/cadastro.html", "/login.html", "/imgs/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                
                // 3. QUALQUER ROTA DE DADOS / API exige o Token JWT
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtService, usuarioRepository),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}