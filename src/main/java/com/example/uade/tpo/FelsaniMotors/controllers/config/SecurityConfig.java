package com.example.uade.tpo.FelsaniMotors.controllers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.uade.tpo.FelsaniMotors.entity.Role;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(req -> req
                                
                                // Endpoints de autenticacion
                                // Permitir acceso a recursos estáticos
                                .requestMatchers("/", "/index.html", "/foto-ejemplo.html", "/*.html", "/css/**", "/js/**", "/images/**").permitAll()
                                .requestMatchers("/public/**").permitAll()
                                
                                // Endpoints de autenticacion
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                                .requestMatchers("/error/**").permitAll()
                                
                                // Publicaciones - lectura publica, escritura autenticada
                                .requestMatchers(HttpMethod.GET, "/api/publicaciones", "/api/publicaciones/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/publicaciones").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/api/publicaciones/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/publicaciones/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                
                                // Autos - lectura publica, escritura autenticada
                                .requestMatchers(HttpMethod.GET, "/api/autos", "/api/autos/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/autos").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                
                                // Usuarios - solo administradores pueden ver todos los usuarios
                                .requestMatchers(HttpMethod.GET, "/api/usuarios").hasAuthority(Role.ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll() // Registro público
                                .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                
                                // Comentarios - lectura publica, escritura autenticada
                                .requestMatchers(HttpMethod.GET, "/api/publicaciones/*/comentarios", "/api/publicaciones/*/comentarios/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/publicaciones/*/comentarios", "/api/publicaciones/*/comentarios/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/api/publicaciones/*/comentarios/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/publicaciones/*/comentarios/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                
                                // Fotos - lectura publica, escritura autenticada
                                .requestMatchers(HttpMethod.GET, "/api/publicaciones/*/fotos", "/api/publicaciones/*/fotos/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/publicaciones/*/fotos").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/api/publicaciones/*/fotos/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/publicaciones/*/fotos/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                                
                                // Cualquier otro endpoint requiere autenticación
                                .anyRequest().authenticated())
                        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                        .authenticationProvider(authenticationProvider)
                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
