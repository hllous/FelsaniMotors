package com.example.uade.tpo.FelsaniMotors.service.transaccion;

import com.example.uade.tpo.FelsaniMotors.dto.response.TransaccionResponse;
import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;
import com.example.uade.tpo.FelsaniMotors.entity.Transaccion;
import com.example.uade.tpo.FelsaniMotors.entity.Usuario;
import com.example.uade.tpo.FelsaniMotors.exceptions.TransaccionInvalidaException;
import com.example.uade.tpo.FelsaniMotors.exceptions.TransaccionNoEncontradaException;
import com.example.uade.tpo.FelsaniMotors.repository.PublicacionRepository;
import com.example.uade.tpo.FelsaniMotors.repository.TransaccionRepository;
import com.example.uade.tpo.FelsaniMotors.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;
    
    @Autowired
    private PublicacionRepository publicacionRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Page<TransaccionResponse> getAllTransacciones(Pageable pageable) {
        Page<Transaccion> transaccionesPage = transaccionRepository.findAll(pageable);
        return transaccionesPage.map(this::convertToTransaccionResponse);
    }

    @Override
    public Optional<TransaccionResponse> getTransaccionById(Long idTransaccion) {
        Optional<Transaccion> transaccion = transaccionRepository.findById(idTransaccion);
        return transaccion.map(this::convertToTransaccionResponse);
    }

    @Override
    public List<TransaccionResponse> getTransaccionesByIdPublicacion(Long idPublicacion) {
        List<Transaccion> transacciones = transaccionRepository.findByIdPublicacion(idPublicacion);
        List<TransaccionResponse> transaccionesResponse = new ArrayList<>();
        
        for (Transaccion transaccion : transacciones) {
            transaccionesResponse.add(convertToTransaccionResponse(transaccion));
        }
        
        return transaccionesResponse;
    }

    @Override
    public List<TransaccionResponse> getTransaccionesByUsuario(Long idUsuario) {
        List<Transaccion> transacciones = transaccionRepository.findByUsuario(idUsuario);
        List<TransaccionResponse> transaccionesResponse = new ArrayList<>();
        
        for (Transaccion transaccion : transacciones) {
            transaccionesResponse.add(convertToTransaccionResponse(transaccion));
        }
        
        return transaccionesResponse;
    }

    @Override
    public TransaccionResponse crearTransaccion(Long idPublicacion, Long idComprador, float monto, String metodoPago, String referenciaPago, String comentarios) {
        
        // Validar la publicacion
        Publicacion publicacion = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new TransaccionInvalidaException("Publicación no encontrada con ID: " + idPublicacion));
        
        // Validar el comprador
        Usuario comprador = usuarioRepository.findById(idComprador)
                .orElseThrow(() -> new TransaccionInvalidaException("Usuario comprador no encontrado con ID: " + idComprador));
        
        // Validar el vendedor (obtenido de la publicacion)
        Usuario vendedor = publicacion.getUsuario();
        
        // Validar que el comprador no sea el vendedor
        if (comprador.getIdUsuario().equals(vendedor.getIdUsuario())) {
            throw new TransaccionInvalidaException("El comprador no puede ser el mismo que el vendedor");
        }
        
        // Validar que la publicacion este activa
        if (publicacion.getEstado() != 'A') {
            throw new TransaccionInvalidaException("La publicación no está activa para realizar transacciones");
        }
        
        // Calcular precio con descuento
        float precioOriginal = publicacion.getPrecio();
        Integer descuentoPorcentaje = publicacion.getDescuentoPorcentaje();
        float precioConDescuento = precioOriginal;
        
        if (descuentoPorcentaje != null && descuentoPorcentaje > 0) {
            precioConDescuento = precioOriginal * (1 - descuentoPorcentaje / 100.0f);
        }
        
        // Validar que el monto sea igual al precio con descuento aplicado
        if (Float.compare(monto, precioConDescuento) != 0) {
            if (descuentoPorcentaje != null && descuentoPorcentaje > 0) {
                throw new TransaccionInvalidaException("El monto de la transacción (" + monto + ") debe ser igual al precio con descuento (" + precioConDescuento + "). Precio original: " + precioOriginal + ", Descuento: " + descuentoPorcentaje + "%");
            } else {
                throw new TransaccionInvalidaException("El monto de la transacción (" + monto + ") debe ser igual al precio de la publicación (" + precioOriginal + ")");
            }
        }
        
        // Crear la transaccion
        Transaccion transaccion = new Transaccion();
        transaccion.setPublicacion(publicacion);
        transaccion.setComprador(comprador);
        transaccion.setVendedor(vendedor);
        transaccion.setMonto(monto);
        transaccion.setMetodoPago(metodoPago);
        transaccion.setFechaTransaccion(new Date());
        transaccion.setEstado("COMPLETADA");
        transaccion.setReferenciaPago(referenciaPago);
        transaccion.setComentarios(comentarios);
        
        // Guardar la transaccion
        Transaccion savedTransaccion = transaccionRepository.save(transaccion);
        
        // Marcar la publicacion como vendida
        publicacion.setEstado('V');
        publicacionRepository.save(publicacion);
        
        // Convertir y devolver la respuesta
        return convertToTransaccionResponse(savedTransaccion);
    }

    @Override
    public TransaccionResponse updateTransaccion(Long idTransaccion, String estado, String referenciaPago, String comentarios, Authentication authentication) {
        
        // Obtener la transaccion
        Transaccion transaccion = transaccionRepository.findById(idTransaccion)
                .orElseThrow(() -> new TransaccionNoEncontradaException("Transaccion no encontrada con ID: " + idTransaccion));
        
        // Validar permiso: solo el comprador o el vendedor pueden actualizar la transaccion
        Usuario usuarioActual = (Usuario) authentication.getPrincipal();
        boolean esComprador = transaccion.getComprador().getIdUsuario().equals(usuarioActual.getIdUsuario());
        boolean esVendedor = transaccion.getVendedor().getIdUsuario().equals(usuarioActual.getIdUsuario());
        
        if (!esComprador && !esVendedor) {
            throw new AccessDeniedException("No tienes permiso para actualizar esta transaccion");
        }
        
        // Actualizar la transaccion
        if (estado != null && !estado.isEmpty()) {

            if (!estado.equals("PENDIENTE") && !estado.equals("COMPLETADA") && !estado.equals("CANCELADA")) {
                throw new TransaccionInvalidaException("Estado invalido: " + estado);
            }
            transaccion.setEstado(estado);
            
            if (estado.equals("COMPLETADA")) {
                Publicacion publicacion = transaccion.getPublicacion();
                publicacion.setEstado('V');
                publicacionRepository.save(publicacion);
            }
        }
        
        if (referenciaPago != null) {
            transaccion.setReferenciaPago(referenciaPago);
        }
        
        if (comentarios != null) {
            transaccion.setComentarios(comentarios);
        }
        
        Transaccion updatedTransaccion = transaccionRepository.save(transaccion);
        return convertToTransaccionResponse(updatedTransaccion);
    }

    @Override
    public boolean deleteTransaccion(Long idTransaccion, Authentication authentication) {

        Transaccion transaccion = transaccionRepository.findById(idTransaccion)
                .orElseThrow(() -> new TransaccionNoEncontradaException("Transacción no encontrada con ID: " + idTransaccion));
        
        // Validar permisos
        Usuario usuarioActual = (Usuario) authentication.getPrincipal();
        boolean esComprador = transaccion.getComprador().getIdUsuario().equals(usuarioActual.getIdUsuario());
        boolean esVendedor = transaccion.getVendedor().getIdUsuario().equals(usuarioActual.getIdUsuario());
        
        if (!esComprador && !esVendedor) {
            throw new AccessDeniedException("No tienes permiso para eliminar esta transacción");
        }
        
        transaccionRepository.delete(transaccion);
        return true;
    }
    
    private TransaccionResponse convertToTransaccionResponse(Transaccion transaccion) {
        TransaccionResponse response = new TransaccionResponse();
        response.setIdTransaccion(transaccion.getIdTransaccion());
        response.setIdPublicacion(transaccion.getPublicacion().getIdPublicacion());
        response.setIdComprador(transaccion.getComprador().getIdUsuario());
        response.setIdVendedor(transaccion.getVendedor().getIdUsuario());
        response.setMonto(transaccion.getMonto());
        response.setMetodoPago(transaccion.getMetodoPago());
        response.setFechaTransaccion(transaccion.getFechaTransaccion());
        response.setEstado(transaccion.getEstado());
        response.setReferenciaPago(transaccion.getReferenciaPago());
        response.setComentarios(transaccion.getComentarios());
        
        // Datos adicionales para UI
        response.setTituloPublicacion(transaccion.getPublicacion().getTitulo());
        response.setNombreComprador(transaccion.getComprador().getNombre());
        response.setNombreVendedor(transaccion.getVendedor().getNombre());
        
        return response;
    }
}
