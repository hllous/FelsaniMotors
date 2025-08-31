package com.example.uade.tpo.FelsaniMotors.service.comentario;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.uade.tpo.FelsaniMotors.entity.Comentario;
import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;
import com.example.uade.tpo.FelsaniMotors.exceptions.ComentarioInvalidoException;
import com.example.uade.tpo.FelsaniMotors.exceptions.ComentarioNoEncontradoException;
import com.example.uade.tpo.FelsaniMotors.repository.ComentarioRepository;
import com.example.uade.tpo.FelsaniMotors.repository.PublicacionRepository;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;
    @Autowired
    private PublicacionRepository publicacionRepository;

    @Override
    public Comentario crearComentario(Long idPublicacion, Comentario comentario) {
        if (comentario.getTexto() == null || comentario.getTexto().isBlank()) {
            throw new ComentarioInvalidoException("El texto del comentario es obligatorio.");
        }
        if (comentario.getFecha() == null) {
            comentario.setFecha(new Date());
        }
        Optional<Publicacion> publicacion = publicacionRepository.findById(idPublicacion);
        if (publicacion.isEmpty()) {
            throw new ComentarioInvalidoException("La publicación con ID " + idPublicacion + " no existe.");
        }
        comentario.setPublicacion(publicacion.get());
        return comentarioRepository.save(comentario);
    }

    @Override
    public Comentario crearRespuesta(Long idPublicacion, Long idComentarioPadre, Comentario respuesta) {

        Optional<Publicacion> publicacion = publicacionRepository.findById(idPublicacion);
        if (publicacion.isEmpty()) {
            throw new ComentarioInvalidoException("La publicación con ID " + idPublicacion + " no existe.");
        }

        Optional<Comentario> padre = comentarioRepository.findById(idComentarioPadre);
        if (padre.isEmpty()) {
            throw new ComentarioNoEncontradoException(idComentarioPadre);
        }

        if (respuesta.getTexto() == null || respuesta.getTexto().isBlank()) {
            throw new ComentarioInvalidoException("El texto de la respuesta es obligatorio.");
        }

        respuesta.setPublicacion(publicacion.get());
        respuesta.setPadre(padre.get());

        if (respuesta.getFecha() == null) {
            respuesta.setFecha(new Date());
        }
        return comentarioRepository.save(respuesta);
    }

    @Override
    public Comentario buscarPorId(Long idComentario) {
        Optional<Comentario> comentario = comentarioRepository.findById(idComentario);
        if (comentario.isEmpty()) {
            throw new ComentarioNoEncontradoException(idComentario);
        }
        return comentario.get();
    }

    @Override
    public List<Comentario> listarComentariosPrincipales(Long idPublicacion) {
        return comentarioRepository.findComentariosPrincipalesByIdPublicacion(idPublicacion);
    }

    @Override
    public List<Comentario> listarRespuestas(Long idComentarioPadre) {
        return comentarioRepository.findRespuestasByPadreId(idComentarioPadre);
    }

    @Override
    public List<Comentario> listarComentariosOrdenados(Long idPublicacion) {
        return comentarioRepository.findAllComentariosByPublicacionOrdenados(idPublicacion);
    }

    @Override
    public Comentario actualizarTexto(Long idComentario, String nuevoTexto) {
        if (nuevoTexto == null || nuevoTexto.isBlank()) {
            throw new ComentarioInvalidoException("El nuevo texto no puede estar vacío.");
        }
        Comentario comentarioAModificar = buscarPorId(idComentario);
        comentarioAModificar.setTexto(nuevoTexto);
        return comentarioRepository.save(comentarioAModificar);
    }

    @Override
    public void eliminarComentario(Long idComentario) {
        Comentario comentarioAEliminar = buscarPorId(idComentario);

        List<Comentario> respuestas = comentarioAEliminar.getRespuestas();
        if (respuestas != null && !respuestas.isEmpty()) {
            respuestas.forEach(hijo -> eliminarComentario(hijo.getIdComentario()));
        }
        comentarioRepository.delete(comentarioAEliminar);
    }
}
