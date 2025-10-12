package com.example.uade.tpo.FelsaniMotors.dto.response;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioResponse {
    private Long idComentario;
    private String texto;
    private Date fecha;
    private UsuarioResponseComentario usuario;
    private PublicacionResponseComentario publicacion;
    private Long idComentarioPadre;
    private List<ComentarioResponse> respuestas;
}
