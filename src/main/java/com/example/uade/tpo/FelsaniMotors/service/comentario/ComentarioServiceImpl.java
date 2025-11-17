package com.example.uade.tpo.FelsaniMotors.service.comentario;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade.tpo.FelsaniMotors.dto.response.ComentarioResponse;
import com.example.uade.tpo.FelsaniMotors.dto.response.PublicacionResponseComentario;
import com.example.uade.tpo.FelsaniMotors.dto.response.UsuarioResponseComentario;
import com.example.uade.tpo.FelsaniMotors.entity.Comentario;
import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;
import com.example.uade.tpo.FelsaniMotors.entity.Role;
import com.example.uade.tpo.FelsaniMotors.entity.Usuario;
import com.example.uade.tpo.FelsaniMotors.exceptions.AccesoNoAutorizadoException;
import com.example.uade.tpo.FelsaniMotors.exceptions.ComentarioInvalidoException;
import com.example.uade.tpo.FelsaniMotors.exceptions.ComentarioNoEncontradoException;
import com.example.uade.tpo.FelsaniMotors.repository.ComentarioRepository;
import com.example.uade.tpo.FelsaniMotors.repository.PublicacionRepository;
import com.example.uade.tpo.FelsaniMotors.repository.UsuarioRepository;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;
    @Autowired
    private PublicacionRepository publicacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public ComentarioResponse crearComentario(Long idPublicacion, Long idUsuario, String texto) {

        if (texto == null || texto.isBlank()) {
            throw new ComentarioInvalidoException("El texto del comentario es obligatorio.");
        }
        
        Optional<Publicacion> publicacion = publicacionRepository.findById(idPublicacion);
        if (publicacion.isEmpty()) {
            throw new ComentarioInvalidoException("La publicación con ID " + idPublicacion + " no existe.");
        }
        
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if (usuario.isEmpty()) {
            throw new ComentarioInvalidoException("El usuario con ID " + idUsuario + " no existe.");
        }
        
        Comentario comentario = new Comentario();
        comentario.setTexto(texto);
        comentario.setFecha(new Date());
        comentario.setPublicacion(publicacion.get());
        comentario.setUsuario(usuario.get());
        
        Comentario guardado = comentarioRepository.save(comentario);
        return convertToDto(guardado);
    }

    @Override
    public ComentarioResponse crearRespuesta(Long idPublicacion, Long idComentarioPadre, Long idUsuario, String texto) {

        Optional<Publicacion> publicacion = publicacionRepository.findById(idPublicacion);
        if (publicacion.isEmpty()) {
            throw new ComentarioInvalidoException("La publicación con ID " + idPublicacion + " no existe.");
        }

        Optional<Comentario> padre = comentarioRepository.findById(idComentarioPadre);
        if (padre.isEmpty()) {
            throw new ComentarioNoEncontradoException(idComentarioPadre);
        }

        if (texto == null || texto.isBlank()) {
            throw new ComentarioInvalidoException("El texto de la respuesta es obligatorio.");
        }

        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
        if (usuario.isEmpty()) {
            throw new ComentarioInvalidoException("El usuario con ID " + idUsuario + " no existe.");
        }

        Comentario respuesta = new Comentario();
        respuesta.setTexto(texto);
        respuesta.setFecha(new Date());
        respuesta.setPublicacion(publicacion.get());
        respuesta.setPadre(padre.get());
        respuesta.setUsuario(usuario.get());

        if (respuesta.getFecha() == null) {
            respuesta.setFecha(new Date());
        }
        
        Comentario guardado = comentarioRepository.save(respuesta);
        return convertToDto(guardado);
    }

    @Override
    public ComentarioResponse buscarPorId(Long idComentario) {
        Optional<Comentario> comentario = comentarioRepository.findById(idComentario);
        if (comentario.isEmpty()) {
            throw new ComentarioNoEncontradoException(idComentario);
        }
        return convertToDto(comentario.get());
    }

    @Override
    public List<ComentarioResponse> listarComentariosPrincipales(Long idPublicacion) {
        List<Comentario> comentarios = comentarioRepository.findComentariosPrincipalesByIdPublicacion(idPublicacion);
        return comentarios.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComentarioResponse> listarRespuestas(Long idComentarioPadre) {
        List<Comentario> respuestas = comentarioRepository.findRespuestasByPadreId(idComentarioPadre);
        return respuestas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComentarioResponse> listarComentariosOrdenados(Long idPublicacion) {
        List<Comentario> comentarios = comentarioRepository.findAllComentariosByPublicacionOrdenados(idPublicacion);
        return comentarios.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ComentarioResponse actualizarTexto(Long idComentario, String nuevoTexto) {
        if (nuevoTexto == null || nuevoTexto.isBlank()) {
            throw new ComentarioInvalidoException("El nuevo texto no puede estar vacío.");
        }
        
        Optional<Comentario> comentarioOpt = comentarioRepository.findById(idComentario);
        if (comentarioOpt.isEmpty()) {
            throw new ComentarioNoEncontradoException(idComentario);
        }
        
        Comentario comentarioAModificar = comentarioOpt.get();
        comentarioAModificar.setTexto(nuevoTexto);
        
        Comentario actualizado = comentarioRepository.save(comentarioAModificar);
        return convertToDto(actualizado);
    }

    @Override
    public void eliminarComentario(Long idComentario, Long idUsuarioActual) {
        Optional<Comentario> comentarioOpt = comentarioRepository.findById(idComentario);
        if (comentarioOpt.isEmpty()) {
            throw new ComentarioNoEncontradoException(idComentario);
        }
        
        Comentario comentarioAEliminar = comentarioOpt.get();
        
        // Obtener el usuario actual
        Optional<Usuario> usuarioActualOpt = usuarioRepository.findById(idUsuarioActual);
        if (usuarioActualOpt.isEmpty()) {
            throw new ComentarioInvalidoException("El usuario no existe.");
        }
        
        Usuario usuarioActual = usuarioActualOpt.get();
        
        // Verificar si es ADMIN o es el creador del comentario
        boolean esAdmin = usuarioActual.getRol() == Role.ADMIN;
        boolean esCreador = comentarioAEliminar.getUsuario().getIdUsuario().equals(idUsuarioActual);
        
        if (!esAdmin && !esCreador) {
            throw new AccesoNoAutorizadoException(
                "No tienes permisos para eliminar este comentario. Solo el creador o un administrador pueden eliminarlo."
            );
        }

        // Eliminar respuestas recursivamente (se eliminan en cascada por la configuración de la entidad)
        comentarioRepository.delete(comentarioAEliminar);
    }

    @Override
    public List<ComentarioResponse> listarTodosLosComentarios() {
        List<Comentario> comentarios = comentarioRepository.findAllComentariosOrderByFechaDesc();
        return comentarios.stream()
                .map(this::convertToDtoSinRespuestas)
                .collect(Collectors.toList());
    }
    
    // Método privado para convertir Comentario a ComentarioResponse
    private ComentarioResponse convertToDto(Comentario comentario) {
        if (comentario == null) return null;
        
        ComentarioResponse response = new ComentarioResponse();
        response.setIdComentario(comentario.getIdComentario());
        response.setTexto(comentario.getTexto());
        response.setFecha(comentario.getFecha());
        
        // Convertir usuario
        if (comentario.getUsuario() != null) {
            UsuarioResponseComentario usuarioDto = new UsuarioResponseComentario();
            usuarioDto.setIdUsuario(comentario.getUsuario().getIdUsuario());
            usuarioDto.setEmail(comentario.getUsuario().getEmail());
            usuarioDto.setNombre(comentario.getUsuario().getNombre());
            usuarioDto.setApellido(comentario.getUsuario().getApellido());
            usuarioDto.setTelefono(comentario.getUsuario().getTelefono());
            usuarioDto.setRol(comentario.getUsuario().getRol().name());
            response.setUsuario(usuarioDto);
        }
        
        // Convertir publicación
        if (comentario.getPublicacion() != null) {
            PublicacionResponseComentario publicacionDto = new PublicacionResponseComentario();
            publicacionDto.setIdPublicacion(comentario.getPublicacion().getIdPublicacion());
            publicacionDto.setTitulo(comentario.getPublicacion().getTitulo());
            publicacionDto.setDescripcion(comentario.getPublicacion().getDescripcion());
            publicacionDto.setUbicacion(comentario.getPublicacion().getUbicacion());
            publicacionDto.setPrecio(comentario.getPublicacion().getPrecio());
            publicacionDto.setFechaPublicacion(comentario.getPublicacion().getFechaPublicacion());
            publicacionDto.setEstado(comentario.getPublicacion().getEstado());
            response.setPublicacion(publicacionDto);
        }
        
        // Agregar ID del comentario padre si existe
        if (comentario.getPadre() != null) {
            response.setIdComentarioPadre(comentario.getPadre().getIdComentario());
        }
        
        // Convertir respuestas (si existen)
        if (comentario.getRespuestas() != null && !comentario.getRespuestas().isEmpty()) {
            List<ComentarioResponse> respuestasDto = comentario.getRespuestas().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            response.setRespuestas(respuestasDto);
        }
        
        return response;
    }

    // Método privado para convertir Comentario a ComentarioResponse sin respuestas anidadas
    private ComentarioResponse convertToDtoSinRespuestas(Comentario comentario) {
        if (comentario == null) return null;
        
        ComentarioResponse response = new ComentarioResponse();
        response.setIdComentario(comentario.getIdComentario());
        response.setTexto(comentario.getTexto());
        response.setFecha(comentario.getFecha());
        
        // Convertir usuario
        if (comentario.getUsuario() != null) {
            UsuarioResponseComentario usuarioDto = new UsuarioResponseComentario();
            usuarioDto.setIdUsuario(comentario.getUsuario().getIdUsuario());
            usuarioDto.setEmail(comentario.getUsuario().getEmail());
            usuarioDto.setNombre(comentario.getUsuario().getNombre());
            usuarioDto.setApellido(comentario.getUsuario().getApellido());
            usuarioDto.setTelefono(comentario.getUsuario().getTelefono());
            usuarioDto.setRol(comentario.getUsuario().getRol().name());
            response.setUsuario(usuarioDto);
        }
        
        // Convertir publicación
        if (comentario.getPublicacion() != null) {
            PublicacionResponseComentario publicacionDto = new PublicacionResponseComentario();
            publicacionDto.setIdPublicacion(comentario.getPublicacion().getIdPublicacion());
            publicacionDto.setTitulo(comentario.getPublicacion().getTitulo());
            publicacionDto.setDescripcion(comentario.getPublicacion().getDescripcion());
            publicacionDto.setUbicacion(comentario.getPublicacion().getUbicacion());
            publicacionDto.setPrecio(comentario.getPublicacion().getPrecio());
            publicacionDto.setFechaPublicacion(comentario.getPublicacion().getFechaPublicacion());
            publicacionDto.setEstado(comentario.getPublicacion().getEstado());
            response.setPublicacion(publicacionDto);
        }
        
        // Agregar ID del comentario padre si existe
        if (comentario.getPadre() != null) {
            response.setIdComentarioPadre(comentario.getPadre().getIdComentario());
        }
        
        // NO incluir respuestas anidadas para el endpoint de admin
        response.setRespuestas(null);
        
        return response;
    }
}
