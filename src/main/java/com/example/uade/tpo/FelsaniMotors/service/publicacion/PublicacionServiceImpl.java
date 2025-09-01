package com.example.uade.tpo.FelsaniMotors.service.publicacion;

import com.example.uade.tpo.FelsaniMotors.dto.response.PublicacionResponse;
import com.example.uade.tpo.FelsaniMotors.entity.Auto;
import com.example.uade.tpo.FelsaniMotors.entity.Categoria;
import com.example.uade.tpo.FelsaniMotors.entity.Foto;
import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;
import com.example.uade.tpo.FelsaniMotors.entity.Usuario;
import com.example.uade.tpo.FelsaniMotors.repository.AutoRepository;
import com.example.uade.tpo.FelsaniMotors.repository.CategoriaRepository;
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
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    // --- Seccion GET --- //
    
    @Override
    public Page<PublicacionResponse> getAllPublicaciones(Pageable pageable) {
        Page<Publicacion> publicaciones = publicacionRepository.findAllByOrderByFechaPublicacionDesc(pageable);
        return publicaciones.map(publicacion -> convertToDto(publicacion));
    }

    @Override
    public Optional<PublicacionResponse> getPublicacionById(Long idPublicacion) {
        Optional<Publicacion> publicacion = publicacionRepository.findById(idPublicacion);
        
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

    // Utilizado en searchPublicaciones
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
    public PublicacionResponse createPublicacion(Long idUsuario, Long idAuto, String titulo, String descripcion, 
                                                String ubicacion, float precio, String metodoDePago,
                                                String urlImagen, Boolean esPrincipal, Integer orden, 
                                                Long idCategoria, String tipoCategoria, String marcaCategoria) {


        Publicacion publicacion = new Publicacion();
        
        // Asigno el usuario
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if (usuario.isPresent()) {
            publicacion.setUsuario(usuario.get());
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }
        
        // Asigno el auto
        Optional<Auto> auto = autoRepository.findById(idAuto);
        if (auto.isPresent()) {

            // Verifico que el auto no este ya en uso en otra publicación activa
            if (auto.get().getPublicacion() != null) {
                throw new RuntimeException("El auto con ID: " + idAuto + " ya está en uso en otra publicación.");
            }
            
            Auto autoEntity = auto.get();
            publicacion.setAuto(autoEntity);
            
            // Manejo de categoria
            Categoria categoriaAsignada = null;
            
            // Caso 1: Si se proporciona un ID de categoria existente
            if (idCategoria != null) {
                Optional<Categoria> categoriaOpt = categoriaRepository.findById(idCategoria);
                if (categoriaOpt.isPresent()) {
                    categoriaAsignada = categoriaOpt.get();
                } else {
                    throw new RuntimeException("Categoría no encontrada con ID: " + idCategoria);
                }
            } 
            // Caso 2: Si se proporcionan datos para crear una nueva categoria
            else if (tipoCategoria != null && marcaCategoria != null) {
                try {
                    // Verificar si ya existe una categoría con ese tipo y marca
                    Categoria categoriaExistente = categoriaRepository.findByTipoCategoria(tipoCategoria);
                    if (categoriaExistente != null && categoriaExistente.getMarca().equals(marcaCategoria)) {
                        categoriaAsignada = categoriaExistente;
                    } else {
                        // Crear nueva categoría
                        Categoria nuevaCategoria = new Categoria();
                        nuevaCategoria.setTipoCategoria(tipoCategoria);
                        nuevaCategoria.setMarca(marcaCategoria);
                        categoriaAsignada = categoriaRepository.save(nuevaCategoria);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error al crear la categoría: " + e.getMessage());
                }
            }
            
            // Asignar la categoria al auto
            if (categoriaAsignada != null) {
                autoEntity.setCategoria(categoriaAsignada);
                autoRepository.save(autoEntity);
            }
        } else {
            throw new RuntimeException("Auto no encontrado con ID: " + idAuto);
        }
        
        // Establezco otros paremetros sin validacion
        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
        publicacion.setUbicacion(ubicacion);
        publicacion.setPrecio(precio);
        publicacion.setMetodoDePago(metodoDePago);
        publicacion.setFechaPublicacion(new Date());
        publicacion.setEstado('A');
        
        // Guardo la publicacion
        Publicacion savedPublicacion = publicacionRepository.save(publicacion);
        
        // Manejar la foto si existe
        if (urlImagen != null && !urlImagen.isEmpty()) {
            Foto foto = new Foto();
            foto.setPublicacion(savedPublicacion);
            
            if (esPrincipal != null) {
                foto.setEsPrincipal(esPrincipal);
            } else {
                foto.setEsPrincipal(true);
            }
            
            if (orden != null) {
                foto.setOrden(orden);
            } else {
                foto.setOrden(0);
            }
            fotoRepository.save(foto);
        }
        
        // Convertir a DTO para la respuesta
        return convertToDto(savedPublicacion);
    }

    // --- Seccion PUT --- //
    
    @Override
    public PublicacionResponse updatePublicacion(Long idPublicacion, String titulo, String descripcion, 
                                                String ubicacion, float precio, String metodoDePago, Long idUsuario) {

        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(idPublicacion);
        
        if (publicacionOpt.isPresent()) {
            Publicacion publicacion = publicacionOpt.get();
            
            // Verifico si el usuario es el creador de la publicacion

            if (!publicacion.getUsuario().getId().equals(idUsuario)) {
                throw new RuntimeException("No tienes permiso para actualizar esta publicacion");
            }
            
            // Actualizo parametros
            publicacion.setTitulo(titulo);
            publicacion.setDescripcion(descripcion);
            publicacion.setUbicacion(ubicacion);
            publicacion.setPrecio(precio);
            publicacion.setMetodoDePago(metodoDePago);
            
            Publicacion updatedPublicacion = publicacionRepository.save(publicacion);

            return convertToDto(updatedPublicacion);

        } else {
            throw new RuntimeException("Publicacion no encontrada con ID: " + idPublicacion);
        }
    }

    @Override
    public PublicacionResponse updateEstadoPublicacion(Long idPublicacion, char estado, Long idUsuario) {

        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(idPublicacion);
        
        if (publicacionOpt.isPresent()) {
            Publicacion publicacion = publicacionOpt.get();
            
            // Verifico si el usuario es el creador de la publicacion

            if (!publicacion.getUsuario().getId().equals(idUsuario)) {
                throw new RuntimeException("No tienes permiso para actualizar el estado de esta publicacion");
            }
            
            publicacion.setEstado(estado);
            
            Publicacion updatedPublicacion = publicacionRepository.save(publicacion);

            return convertToDto(updatedPublicacion);

        } else {
            throw new RuntimeException("Publicacion no encontrada con ID: " + idPublicacion);
        }
    }

    // --- Seccion DELETE --- //
    
    @Override
    public boolean deletePublicacion(Long idPublicacion, Long idUsuario) {

        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(idPublicacion);
        
        if (publicacionOpt.isPresent()) {

            Publicacion publicacion = publicacionOpt.get();
            
            // Verifico si el usuario es el creador de la publicacion
            if (!publicacion.getUsuario().getId().equals(idUsuario)) {
                throw new RuntimeException("No tienes permiso para eliminar esta publicacion");
            }
            
            publicacionRepository.deleteById(idPublicacion);

            return true;
        }else{

            return false;

        }
    }
    
    // --- Metodos de conversion --- //
    
    private PublicacionResponse convertToDto(Publicacion publicacion) {

        PublicacionResponse dto = new PublicacionResponse();

        dto.setIdPublicacion(publicacion.getIdPublicacion());
        
        // Establecer IDs en lugar de entidades completas (ver dto/response)
        if (publicacion.getUsuario() != null) {

            dto.setIdUsuario(publicacion.getUsuario().getId());
            dto.setNombreUsuario(publicacion.getUsuario().getNombre() + " " + publicacion.getUsuario().getApellido());
        }
        
        if (publicacion.getAuto() != null) {

            dto.setIdAuto(publicacion.getAuto().getIdAuto());
            dto.setMarcaAuto(publicacion.getAuto().getMarca());
            dto.setModeloAuto(publicacion.getAuto().getModelo());
        }
        
        // Establezco parametros
        dto.setTitulo(publicacion.getTitulo());
        dto.setDescripcion(publicacion.getDescripcion());
        dto.setUbicacion(publicacion.getUbicacion());
        dto.setPrecio(publicacion.getPrecio());
        dto.setFechaPublicacion(publicacion.getFechaPublicacion());
        dto.setEstado(publicacion.getEstado());
        dto.setMetodoDePago(publicacion.getMetodoDePago());
        
        // Buscar y establecer la informacion de la foto principal
        Optional<Foto> fotoPrincipal = fotoRepository.findByPublicacionAndEsPrincipal(publicacion, true);

        if (fotoPrincipal.isPresent()) {
            dto.setIdFotoPrincipal(fotoPrincipal.get().getIdFoto());
            dto.setImagenPrincipal("/publicaciones/" + publicacion.getIdPublicacion() + "/fotos/" + 
                                                        fotoPrincipal.get().getIdFoto() + "/imagen");
        }
        
        return dto;

    }

}