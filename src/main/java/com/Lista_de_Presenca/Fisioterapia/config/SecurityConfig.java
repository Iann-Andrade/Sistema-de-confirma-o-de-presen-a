package com.Lista_de_Presenca.Fisioterapia.config;


import java.beans.Customizer;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.Lista_de_Presenca.Fisioterapia.repository.UsuarioRepository;
import com.Lista_de_Presenca.Fisioterapia.service.JwtService;

@Configuration
@EnableWebSecurity
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Origens permitidas (URL do seu frontend no Render + localhost para testes)
        configuration.setAllowedOrigins(List.of(
            "https://pelafeu.onrender.com",
            "http://localhost:8080",
            "http://127.0.0.1:5500"
        ));

        // Métodos HTTP aceitos
        configuration.setAllowedMethods(List.of(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "OPTIONS"
        ));

        // Cabeçalhos aceitos nas requisições
        configuration.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type",
            "X-Requested-With"
        ));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        http
            // 1. Aplica a configuração do Bean CorsConfigurationSource acima
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 2. Regras de permissão de rotas
            .authorizeHttpRequests(auth -> auth
                // Permite requisições Preflight (OPTIONS) para resolver o CORS antes do POST/PUT
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Endpoints públicos de login e cadastro
                .requestMatchers("/usuarios/cadastrar", "/usuarios/login").permitAll()

                // Arquivos estáticos e HTMLs
                .requestMatchers("/", "/index.html", "/cronograma.html", "/cadastro.html", "/login.html").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/imgs/**", "/favicon.ico").permitAll()

                // Qualquer outra requisição de dados exigirá Token JWT válido
                .anyRequest().authenticated()
            )

            // 3. Adiciona o filtro do JWT
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtService, usuarioRepository),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}