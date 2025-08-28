package com.example.uade.tpo.FelsaniMotors.service.publicacion;

import com.example.uade.tpo.FelsaniMotors.dto.request.PublicacionCreateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.request.PublicacionUpdateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.response.PublicacionResponse;

import com.example.uade.tpo.FelsaniMotors.entity.Auto;
import com.example.uade.tpo.FelsaniMotors.entity.Foto;
import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;
import com.example.uade.tpo.FelsaniMotors.entity.Usuario;
import com.example.uade.tpo.FelsaniMotors.repository.AutoRepository;
import com.example.uade.tpo.FelsaniMotors.repository.FotoRepository;
import com.example.uade.tpo.FelsaniMotors.repository.PublicacionRepository;
import com.example.uade.tpo.FelsaniMotors.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PublicacionServiceImpl implements PublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;
    
    @Autowired
    private FotoRepository fotoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AutoRepository autoRepository;

    // --- Seccion GET --- //
    
    @Override
    public Page<PublicacionResponse> getAllPublicaciones(Pageable pageable) {
        Page<Publicacion> publicaciones = publicacionRepository.findAllByOrderByFechaPublicacionDesc(pageable);
        return publicaciones.map(publicacion -> convertToDto(publicacion));
    }

    @Override
    public Optional<PublicacionResponse> getPublicacionById(Long id) {
        Optional<Publicacion> publicacion = publicacionRepository.findById(id);
        
        if (publicacion.isPresent()) {
            return Optional.of(convertToDto(publicacion.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<PublicacionResponse> getPublicacionesByIdUsuario(Long idUsuario) {
        List<Publicacion> publicaciones = publicacionRepository.findByIdUsuario(idUsuario);
        List<PublicacionResponse> publicacionesResponse = new ArrayList<>();
        
        for (Publicacion publicacion : publicaciones) {
            publicacionesResponse.add(convertToDto(publicacion));
        }
        
        return publicacionesResponse;
    }

    @Override
    public Page<PublicacionResponse> buscarPublicaciones(String busqueda, Pageable pageable) {
        Page<Publicacion> publicaciones = publicacionRepository.buscarPublicaciones(busqueda, pageable);
        return publicaciones.map(publicacion -> convertToDto(publicacion));
    }

    @Override
    public List<PublicacionResponse> getPublicacionesByRangoPrecio(float precioMin, float precioMax) {
        List<Publicacion> publicaciones = publicacionRepository.findByPrecioBetween(precioMin, precioMax);
        List<PublicacionResponse> publicacionesResponse = new ArrayList<>();
        
        for (Publicacion publicacion : publicaciones) {
            publicacionesResponse.add(convertToDto(publicacion));
        }
        
        return publicacionesResponse;
    }

    @Override
    public List<PublicacionResponse> getPublicacionesByEstado(char estado) {
        List<Publicacion> publicaciones = publicacionRepository.findByEstado(estado);
        List<PublicacionResponse> publicacionesResponse = new ArrayList<>();
        
        for (Publicacion publicacion : publicaciones) {
            publicacionesResponse.add(convertToDto(publicacion));
        }
        
        return publicacionesResponse;
    }

    // --- Seccion POST --- //
    
    @Override
    public PublicacionResponse createPublicacion(PublicacionCreateRequest createRequest) {
        Publicacion publicacion = convertToEntity(createRequest);
        
        // Si es una nueva publicación, asigno la fecha actual
        if (publicacion.getFechaPublicacion() == null) {
            publicacion.setFechaPublicacion(new Date());
        }
        
        // Por defecto, establecemos el estado en 'A' (Activo)
        publicacion.setEstado('A');
        
        // Primero guardamos la publicación
        Publicacion savedPublicacion = publicacionRepository.save(publicacion);
        
        // Si hay información de foto en la solicitud, creamos y guardamos la foto
        if (createRequest.getUrlImagen() != null && !createRequest.getUrlImagen().isEmpty()) {
            Foto foto = new Foto();
            foto.setPublicacion(savedPublicacion);
            foto.setUrlImagen(createRequest.getUrlImagen());
            
            // Si no se especifica, establecemos como principal
            foto.setEsPrincipal(createRequest.getEsPrincipal() != null ? createRequest.getEsPrincipal() : true);
            
            // Si no se especifica orden, establecemos 0
            foto.setOrden(createRequest.getOrden() != null ? createRequest.getOrden() : 0);
            
            fotoRepository.save(foto);
        }
        
        return convertToDto(savedPublicacion);
    }

    // --- Seccion PUT --- //
    
    @Override
    public PublicacionResponse updatePublicacion(Long id, PublicacionUpdateRequest updateRequest) {
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(id);
        
        if (publicacionOpt.isPresent()) {
            Publicacion publicacion = publicacionOpt.get();
            
            // Actualizar los campos desde el DTO
            publicacion.setTitulo(updateRequest.getTitulo());
            publicacion.setDescripcion(updateRequest.getDescripcion());
            publicacion.setUbicacion(updateRequest.getUbicacion());
            publicacion.setPrecio(updateRequest.getPrecio());
            publicacion.setMetodoDePago(updateRequest.getMetodoDePago());
            
            Publicacion updatedPublicacion = publicacionRepository.save(publicacion);
            return convertToDto(updatedPublicacion);
        } else {
            throw new RuntimeException("Publicación no encontrada con ID: " + id);
        }
    }

    @Override
    public PublicacionResponse updateEstadoPublicacion(Long id, char estado) {
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(id);
        
        if (publicacionOpt.isPresent()) {
            Publicacion publicacion = publicacionOpt.get();
            publicacion.setEstado(estado);
            Publicacion updatedPublicacion = publicacionRepository.save(publicacion);
            return convertToDto(updatedPublicacion);
        } else {
            throw new RuntimeException("Publicación no encontrada con ID: " + id);
        }
    }

    // --- Seccion DELETE --- //
    
    @Override
    public boolean deletePublicacion(Long id) {
        if (publicacionRepository.existsById(id)) {
            publicacionRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // --- Métodos de conversión --- //
    
    @Override
    public PublicacionResponse convertToDto(Publicacion publicacion) {
        PublicacionResponse dto = new PublicacionResponse();
        dto.setIdPublicacion(publicacion.getIdPublicacion());
        
        // Establecer IDs en lugar de entidades completas
        if (publicacion.getUsuario() != null) {
            dto.setIdUsuario(publicacion.getUsuario().getId());
            dto.setNombreUsuario(publicacion.getUsuario().getNombre() + " " + publicacion.getUsuario().getApellido());
        }
        
        if (publicacion.getAuto() != null) {
            dto.setIdAuto(publicacion.getAuto().getIdAuto());
            dto.setMarcaAuto(publicacion.getAuto().getMarca());
            dto.setModeloAuto(publicacion.getAuto().getModelo());
        }
        
        // Propiedades básicas
        dto.setTitulo(publicacion.getTitulo());
        dto.setDescripcion(publicacion.getDescripcion());
        dto.setUbicacion(publicacion.getUbicacion());
        dto.setPrecio(publicacion.getPrecio());
        dto.setFechaPublicacion(publicacion.getFechaPublicacion());
        dto.setEstado(publicacion.getEstado());
        dto.setMetodoDePago(publicacion.getMetodoDePago());
        
        // Buscar y establecer la información de la foto principal
        Optional<Foto> fotoPrincipal = fotoRepository.findByPublicacionAndEsPrincipal(publicacion, true);
        if (fotoPrincipal.isPresent()) {
            dto.setIdFotoPrincipal(fotoPrincipal.get().getId());
            dto.setImagenPrincipal(fotoPrincipal.get().getUrlImagen());
        }
        
        return dto;
    }
    
    @Override
    public Publicacion convertToEntity(PublicacionCreateRequest createRequest) {
        Publicacion publicacion = new Publicacion();
        
        // Buscar y asignar entidades a partir de los IDs
        if (createRequest.getIdUsuario() != null) {
            Optional<Usuario> usuario = usuarioRepository.findById(createRequest.getIdUsuario());
            if (usuario.isPresent()) {
                publicacion.setUsuario(usuario.get());
            } else {
                throw new RuntimeException("Usuario no encontrado con ID: " + createRequest.getIdUsuario());
            }
        } else {
            throw new RuntimeException("Se requiere un ID de usuario para crear una publicación");
        }
        
        if (createRequest.getIdAuto() != null) {
            Optional<Auto> auto = autoRepository.findById(createRequest.getIdAuto());
            if (auto.isPresent()) {
                publicacion.setAuto(auto.get());
            } else {
                throw new RuntimeException("Auto no encontrado con ID: " + createRequest.getIdAuto());
            }
        } else {
            throw new RuntimeException("Se requiere un ID de auto para crear una publicación");
        }
        
        // Propiedades básicas
        publicacion.setTitulo(createRequest.getTitulo());
        publicacion.setDescripcion(createRequest.getDescripcion());
        publicacion.setUbicacion(createRequest.getUbicacion());
        publicacion.setPrecio(createRequest.getPrecio());
        publicacion.setMetodoDePago(createRequest.getMetodoDePago());
        
        // La fecha de publicacion y el estado se establecen en el metodo createPublicacion
        
        return publicacion;
    }
}