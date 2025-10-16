package com.example.uade.tpo.FelsaniMotors.service.publicacion;

import com.example.uade.tpo.FelsaniMotors.dto.response.FiltrosOpcionesResponse;
import com.example.uade.tpo.FelsaniMotors.dto.response.PublicacionResponse;
import com.example.uade.tpo.FelsaniMotors.entity.Auto;
import com.example.uade.tpo.FelsaniMotors.entity.Foto;
import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;
import com.example.uade.tpo.FelsaniMotors.entity.Transaccion;
import com.example.uade.tpo.FelsaniMotors.entity.Usuario;
import com.example.uade.tpo.FelsaniMotors.repository.AutoRepository;
import com.example.uade.tpo.FelsaniMotors.repository.FotoRepository;
import com.example.uade.tpo.FelsaniMotors.repository.PublicacionRepository;
import com.example.uade.tpo.FelsaniMotors.repository.TransaccionRepository;
import com.example.uade.tpo.FelsaniMotors.repository.UsuarioRepository;
import com.example.uade.tpo.FelsaniMotors.service.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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
    private AuthenticationService authService;
    
    @Autowired
    private TransaccionRepository transaccionRepository;

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
                                                String ubicacion, float precio, String metodoDePago) {
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
        
        // Convertir a DTO para la respuesta
        return convertToDto(savedPublicacion);
    }

    // --- Seccion PUT --- //
    
    @Override
    public PublicacionResponse updatePublicacion(
            Long idPublicacion, 
            String titulo, 
            String descripcion, 
            String ubicacion, 
            float precio, 
            String metodoDePago, 
            Authentication authentication) {

        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(idPublicacion);
        
        if (publicacionOpt.isEmpty()) {
            throw new RuntimeException("Publicación no encontrada con ID: " + idPublicacion);
        }
        
        Publicacion publicacion = publicacionOpt.get();
        
        // Verificar si el usuario actual tiene permiso para actualizar esta publicacion
        if (!authService.isOwnerOrAdmin(authentication, publicacion.getUsuario().getIdUsuario())) {
            throw new AccessDeniedException("No tienes permiso para actualizar esta publicacion");
        }
        
        // Actualizo parametros
        publicacion.setTitulo(titulo);
        publicacion.setDescripcion(descripcion);
        publicacion.setUbicacion(ubicacion);
        publicacion.setPrecio(precio);
        publicacion.setMetodoDePago(metodoDePago);
        
        Publicacion updatedPublicacion = publicacionRepository.save(publicacion);

        return convertToDto(updatedPublicacion);
    }

    @Override
    public PublicacionResponse updateEstadoPublicacion(Long idPublicacion, char estado, Authentication authentication) {
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(idPublicacion);
        
        if (publicacionOpt.isEmpty()) {
            throw new RuntimeException("Publicación no encontrada con ID: " + idPublicacion);
        }
        
        Publicacion publicacion = publicacionOpt.get();
        
        // Verificar si el usuario actual tiene permiso para actualizar esta publicacion
        if (!authService.isOwnerOrAdmin(authentication, publicacion.getUsuario().getIdUsuario())) {
            throw new AccessDeniedException("No tienes permiso para actualizar el estado de esta publicacion");
        }
        
        publicacion.setEstado(estado);
        
        Publicacion updatedPublicacion = publicacionRepository.save(publicacion);

        return convertToDto(updatedPublicacion);
    }

    // --- Seccion DELETE --- //
    
    @Override
    public boolean deletePublicacion(Long idPublicacion, Authentication authentication) {
        Optional<Publicacion> publicacionOpt = publicacionRepository.findById(idPublicacion);
        
        if (publicacionOpt.isEmpty()) {
            return false;
        }

        Publicacion publicacion = publicacionOpt.get();
        
        // Verificar si el usuario actual tiene permiso para eliminar esta publicacion
        if (!authService.isOwnerOrAdmin(authentication, publicacion.getUsuario().getIdUsuario())) {
            throw new AccessDeniedException("No tienes permiso para eliminar esta publicación");
        }
        
        // 1. Eliminar transacciones relacionadas primero
        List<Transaccion> transacciones = transaccionRepository.findByIdPublicacion(idPublicacion);
        if (!transacciones.isEmpty()) {
            transaccionRepository.deleteAll(transacciones);
        }
        
        // 2. Desasociar el Auto de la Publicacion
        Auto auto = publicacion.getAuto();
        if (auto != null) {
            auto.setPublicacion(null);
            autoRepository.save(auto);
        }
        
        // 3. Eliminar la publicación (fotos y comentarios se eliminan automáticamente por cascade)
        publicacionRepository.delete(publicacion);
        return true;
    }
    
    @Override
    public Page<PublicacionResponse> filtrarPublicaciones(
            String busqueda,
            List<String> marcas,
            List<String> modelos,
            List<String> anios,
            List<String> estados,
            List<String> kilometrajes,
            List<String> combustibles,
            List<String> tipoCategorias,
            List<String> tipoCajas,
            List<String> motores,
            Pageable pageable) {
        
        // Calcular min/max de kilometrajes
        Float kmMin = null;
        Float kmMax = null;
        
        if (kilometrajes != null && !kilometrajes.isEmpty()) {
            for (String rango : kilometrajes) {
                String[] partes = rango.split("-");
                if (partes.length == 2) {
                    try {
                        float min = Float.parseFloat(partes[0]);
                        float max = Float.parseFloat(partes[1]);
                        kmMin = (kmMin == null) ? min : Math.min(kmMin, min);
                        kmMax = (kmMax == null) ? max : Math.max(kmMax, max);
                    } catch (NumberFormatException e) {
                        // Ignorar rangos inválidos
                    }
                }
            }
        }
        
        // Llamar al repositorio con el query
        Page<Publicacion> publicaciones = publicacionRepository.filtrar(
            busqueda, marcas, modelos, anios, estados,
            combustibles, tipoCategorias, tipoCajas, motores,
            kmMin, kmMax, pageable
        );
        
        // Convertir a DTO
        return publicaciones.map(this::convertToDto);
    }
    
    @Override
    public FiltrosOpcionesResponse getOpcionesFiltros() {
        FiltrosOpcionesResponse opciones = new FiltrosOpcionesResponse();
        
        opciones.setMarcas(publicacionRepository.findAllMarcas());
        opciones.setModelos(publicacionRepository.findAllModelos());
        opciones.setAnios(publicacionRepository.findAllAnios());
        opciones.setEstados(publicacionRepository.findAllEstadosAuto());
        opciones.setCombustibles(publicacionRepository.findAllCombustibles());
        opciones.setTipoCategorias(publicacionRepository.findAllTipoCategorias());
        opciones.setTipoCajas(publicacionRepository.findAllTipoCajas());
        opciones.setMotores(publicacionRepository.findAllMotores());
        
        return opciones;
    }
    
    // --- Metodos de conversion --- //
    
    private PublicacionResponse convertToDto(Publicacion publicacion) {

        PublicacionResponse dto = new PublicacionResponse();

        dto.setIdPublicacion(publicacion.getIdPublicacion());
        
        // Establecer IDs en lugar de entidades completas (ver dto/response)
        if (publicacion.getUsuario() != null) {

            dto.setIdUsuario(publicacion.getUsuario().getIdUsuario());
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
            dto.setImagenPrincipal("/api/publicaciones/" + publicacion.getIdPublicacion() + 
                                  "/fotos/" + fotoPrincipal.get().getIdFoto() + "?includeImage=true");
        }
        
        return dto;

    }

}