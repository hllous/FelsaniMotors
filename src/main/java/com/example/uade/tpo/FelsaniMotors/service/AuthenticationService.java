package com.example.uade.tpo.FelsaniMotors.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.uade.tpo.FelsaniMotors.controllers.auth.AuthenticationRequest;
import com.example.uade.tpo.FelsaniMotors.controllers.auth.AuthenticationResponse;
import com.example.uade.tpo.FelsaniMotors.controllers.auth.RegisterRequest;
import com.example.uade.tpo.FelsaniMotors.controllers.config.JwtService;
import com.example.uade.tpo.FelsaniMotors.entity.Usuario;
import com.example.uade.tpo.FelsaniMotors.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UsuarioRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                var user = Usuario.usuarioBuilder()
                                .nombre(request.getFirstname())
                                .apellido(request.getLastname())
                                .email(request.getEmail())
                                .contrasena(passwordEncoder.encode(request.getPassword()))
                                .rol(request.getRole())
                                .telefono(request.getTelefono())
                                .activo(true)
                                .build();

                repository.save(user);
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                var user = repository.findByEmail(request.getEmail())
                                .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }
}
