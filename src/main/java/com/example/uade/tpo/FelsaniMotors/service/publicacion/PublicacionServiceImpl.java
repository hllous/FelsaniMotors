package com.example.uade.tpo.FelsaniMotors.service.publicacion;

import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;
import com.example.uade.tpo.FelsaniMotors.entity.dto.PublicacionDTO;
import com.example.uade.tpo.FelsaniMotors.repository.PublicacionRepository;
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

    // --- Seccion GET --- //
    
    @Override
    public Page<PublicacionDTO> getAllPublicaciones(Pageable pageable) {
        Page<Publicacion> publicaciones = publicacionRepository.findAllByOrderByFechaPublicacionDesc(pageable);
        return publicaciones.map(publicacion -> convertToDto(publicacion));
    }

    @Override
    public Optional<PublicacionDTO> getPublicacionById(Long id) {
        Optional<Publicacion> publicacion = publicacionRepository.findById(id);
        
        if (publicacion.isPresent()) {
            return Optional.of(convertToDto(publicacion.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<PublicacionDTO> getPublicacionesByUsuario(Long idUsuario) {
        List<Publicacion> publicaciones = publicacionRepository.findByIdUsuario(idUsuario);
        List<PublicacionDTO> publicacionesDTO = new ArrayList<>();
        
        for (Publicacion publicacion : publicaciones) {
            publicacionesDTO.add(convertToDto(publicacion));
        }
        
        return publicacionesDTO;
    }

    @Override
    public List<PublicacionDTO> getPublicacionesByAuto(Long idAuto) {
        List<Publicacion> publicaciones = publicacionRepository.findByIdAuto(idAuto);
        List<PublicacionDTO> publicacionesDTO = new ArrayList<>();
        
        for (Publicacion publicacion : publicaciones) {
            publicacionesDTO.add(convertToDto(publicacion));
        }
        
        return publicacionesDTO;
    }

    @Override
    public Page<PublicacionDTO> buscarPublicaciones(String busqueda, Pageable pageable) {
        Page<Publicacion> publicaciones = publicacionRepository.buscarPublicaciones(busqueda, pageable);
        return publicaciones.map(publicacion -> convertToDto(publicacion));
    }

    @Override
    public List<PublicacionDTO> getPublicacionesByRangoPrecio(float precioMin, float precioMax) {
        List<Publicacion> publicaciones = publicacionRepository.findByPrecioBetween(precioMin, precioMax);
        List<PublicacionDTO> publicacionesDTO = new ArrayList<>();
        
        for (Publicacion publicacion : publicaciones) {
            publicacionesDTO.add(convertToDto(publicacion));
        }
        
        return publicacionesDTO;
    }

    @Override
    public List<PublicacionDTO> getPublicacionesByEstado(char estado) {
        List<Publicacion> publicaciones = publicacionRepository.findByEstado(estado);
        List<PublicacionDTO> publicacionesDTO = new ArrayList<>();
        
        for (Publicacion publicacion : publicaciones) {
            publicacionesDTO.add(convertToDto(publicacion));
        }
        
        return publicacionesDTO;
    }

    // --- Seccion POST --- //
    
    @Override
    public PublicacionDTO createPublicacion(PublicacionDTO publicacionDTO) {
        Publicacion publicacion = convertToEntity(publicacionDTO);
        
        // Si es una nueva publicación, asigno la fecha actual
        if (publicacion.getFechaPublicacion() == null) {
            publicacion.setFechaPublicacion(new Date());
        }
        
        Publicacion savedPublicacion = publicacionRepository.save(publicacion);
        return convertToDto(savedPublicacion);
    }

    // --- Seccion PUT --- //
    
    @Override
    public PublicacionDTO updatePublicacion(Long id, PublicacionDTO publicacionDTO) {
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(id);
        
        if (publicacionOpt.isPresent()) {
            Publicacion publicacion = publicacionOpt.get();
            
            // Actualizar los campos desde el DTO
            publicacion.setTitulo(publicacionDTO.getTitulo());
            publicacion.setDescripcion(publicacionDTO.getDescripcion());
            publicacion.setUbicacion(publicacionDTO.getUbicacion());
            publicacion.setPrecio(publicacionDTO.getPrecio());
            publicacion.setEstado(publicacionDTO.getEstado());
            publicacion.setMetodoDePago(publicacionDTO.getMetodoDePago());
            
            Publicacion updatedPublicacion = publicacionRepository.save(publicacion);
            return convertToDto(updatedPublicacion);
        } else {
            throw new RuntimeException("Publicación no encontrada con ID: " + id);
        }
    }

    @Override
    public PublicacionDTO updateEstadoPublicacion(Long id, char estado) {
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
    public PublicacionDTO convertToDto(Publicacion publicacion) {
        PublicacionDTO dto = new PublicacionDTO();
        dto.setId(publicacion.getIdPublicacion());
        dto.setIdUsuario(publicacion.getIdUsuario());
        dto.setIdAuto(publicacion.getIdAuto());
        dto.setTitulo(publicacion.getTitulo());
        dto.setDescripcion(publicacion.getDescripcion());
        dto.setUbicacion(publicacion.getUbicacion());
        dto.setPrecio(publicacion.getPrecio());
        dto.setFechaPublicacion(publicacion.getFechaPublicacion());
        dto.setEstado(publicacion.getEstado());
        dto.setMetodoDePago(publicacion.getMetodoDePago());
        
        // Aquí se podrían añadir los datos adicionales como nombre de usuario, marca y modelo de auto
        // que se obtendrían de otros servicios
        
        return dto;
    }
    
    @Override
    public Publicacion convertToEntity(PublicacionDTO dto) {
        Publicacion publicacion = new Publicacion();
        
        // No establecemos el ID para nuevas entidades
        if (dto.getId() != null) {
            publicacion.setIdPublicacion(dto.getId());
        }
        
        publicacion.setIdUsuario(dto.getIdUsuario());
        publicacion.setIdAuto(dto.getIdAuto());
        publicacion.setTitulo(dto.getTitulo());
        publicacion.setDescripcion(dto.getDescripcion());
        publicacion.setUbicacion(dto.getUbicacion());
        publicacion.setPrecio(dto.getPrecio());
        publicacion.setEstado(dto.getEstado());
        publicacion.setMetodoDePago(dto.getMetodoDePago());
        
        // Para nuevas entidades, establecemos la fecha actual
        if (dto.getFechaPublicacion() == null) {
            publicacion.setFechaPublicacion(new Date());
        } else {
            publicacion.setFechaPublicacion(dto.getFechaPublicacion());
        }
        
        return publicacion;
    }
}