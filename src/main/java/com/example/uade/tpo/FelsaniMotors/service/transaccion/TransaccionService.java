package com.example.uade.tpo.FelsaniMotors.service.transaccion;

import com.example.uade.tpo.FelsaniMotors.dto.response.TransaccionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface TransaccionService {

    Page<TransaccionResponse> getAllTransacciones(Pageable pageable);
    
    Optional<TransaccionResponse> getTransaccionById(Long idTransaccion);
    
    List<TransaccionResponse> getTransaccionesByIdPublicacion(Long idPublicacion);
    
    List<TransaccionResponse> getTransaccionesByUsuario(Long idUsuario);
    
    TransaccionResponse crearTransaccion(Long idPublicacion, Long idComprador, 
                                         float monto, String metodoPago, 
                                         String referenciaPago, String comentarios);
    
    TransaccionResponse updateTransaccion(Long idTransaccion, String estado, 
                                         String referenciaPago, String comentarios, 
                                         Authentication authentication);
    
    boolean deleteTransaccion(Long idTransaccion, Authentication authentication);
}
