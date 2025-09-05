package com.example.uade.tpo.FelsaniMotors.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.uade.tpo.FelsaniMotors.controllers.auth.AuthenticationRequest;
import com.example.uade.tpo.FelsaniMotors.controllers.auth.AuthenticationResponse;
import com.example.uade.tpo.FelsaniMotors.controllers.auth.RegisterRequest;
import com.example.uade.tpo.FelsaniMotors.controllers.config.JwtService;
import com.example.uade.tpo.FelsaniMotors.entity.Role;
import com.example.uade.tpo.FelsaniMotors.entity.Usuario;
import com.example.uade.tpo.FelsaniMotors.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UsuarioRepository usuarioRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                var user = Usuario.builder()
                                .nombre(request.getFirstname())
                                .apellido(request.getLastname())
                                .email(request.getEmail())
                                .contrasena(passwordEncoder.encode(request.getPassword()))
                                .rol(request.getRole())
                                .activo(true)
                                .build();

                usuarioRepository.save(user);
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

                var user = usuarioRepository.findByEmail(request.getEmail())
                                .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }

        public boolean isOwnerOrAdmin(Authentication authentication, Long resourceOwnerId) {
                try {
                        Usuario currentUser = getCurrentUser(authentication);
                        
                        // Verificar si es administrador o propietario
                        boolean isAdmin = currentUser.getRol() == Role.ADMIN;
                        boolean isOwner = currentUser.getIdUsuario().equals(resourceOwnerId);
                        
                        if (isOwner || isAdmin) {
                                return true;
                        } else {
                                return false;
                        }
                        
                } catch (Exception e) {
                        return false;
                }
        }
        
        private Usuario getCurrentUser(Authentication authentication) {
                if (authentication == null) {
                        throw new IllegalArgumentException("No hay autenticación disponible");
                }
                
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                return usuarioRepository.findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        }
        
}
