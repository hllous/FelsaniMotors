package com.example.uade.tpo.FelsaniMotors.service.comentario;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.uade.tpo.FelsaniMotors.entity.Comentario;
import com.example.uade.tpo.FelsaniMotors.exceptions.ComentarioInvalidoException;
import com.example.uade.tpo.FelsaniMotors.exceptions.ComentarioNoEncontradoException;
import com.example.uade.tpo.FelsaniMotors.repository.ComentarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;

    @Override
    public Comentario crear(Comentario comentario) {
        if (comentario.getIdComentario() != null) {
            throw new ComentarioInvalidoException("Para crear no envíes idComentario.");
        }
        if (comentario.getTexto() == null || comentario.getTexto().isBlank()) {
            throw new ComentarioInvalidoException("El texto del comentario es obligatorio.");
        }
        if (comentario.getFecha() == null) {
            comentario.setFecha(new Date());
        }
        return comentarioRepository.save(comentario);
    }

    @Override
    public Comentario responder(Long idComentarioPadre, Comentario respuesta) {
        Comentario padre = comentarioRepository.findById(idComentarioPadre)
                .orElseThrow(() -> new ComentarioNoEncontradoException(idComentarioPadre));

        if (respuesta.getIdComentario() != null) {
            throw new ComentarioInvalidoException("Para responder no envíes idComentario.");
        }
        if (respuesta.getTexto() == null || respuesta.getTexto().isBlank()) {
            throw new ComentarioInvalidoException("El texto de la respuesta es obligatorio.");
        }
        respuesta.setComentarioPadre(padre);
        if (respuesta.getFecha() == null) {
            respuesta.setFecha(new Date());
        }
        return comentarioRepository.save(respuesta);
    }

    @Override
    @Transactional(readOnly = true)
    public Comentario obtenerPorId(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new ComentarioNoEncontradoException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comentario> listarRaiz() {
        return comentarioRepository.findByComentarioPadreIsNullOrderByFechaAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comentario> listarRespuestas(Long idComentarioPadre) {
        if (!comentarioRepository.existsById(idComentarioPadre)) {
            throw new ComentarioNoEncontradoException(idComentarioPadre);
        }
        return comentarioRepository.findByComentarioPadreIdComentarioOrderByFechaAsc(idComentarioPadre);
    }

    @Override
    public Comentario actualizarTexto(Long idComentario, String nuevoTexto) {
        if (nuevoTexto == null || nuevoTexto.isBlank()) {
            throw new ComentarioInvalidoException("El nuevo texto no puede estar vacío.");
        }
        Comentario existente = obtenerPorId(idComentario);
        existente.setTexto(nuevoTexto);
        return comentarioRepository.save(existente);
    }

    @Override
    public void eliminar(Long idComentario) {
        Comentario existente = obtenerPorId(idComentario);

        List<Comentario> respuestas = existente.getRespuestas();
        if (respuestas != null && !respuestas.isEmpty()) {
            respuestas.forEach(hijo -> eliminar(hijo.getIdComentario()));
        }

        comentarioRepository.delete(existente);
    }
}
