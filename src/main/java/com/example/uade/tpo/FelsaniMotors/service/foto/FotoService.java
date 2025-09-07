package com.example.uade.tpo.FelsaniMotors.service.foto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.uade.tpo.FelsaniMotors.dto.request.FotoUploadRequest;
import com.example.uade.tpo.FelsaniMotors.dto.response.FotoResponse;
import com.example.uade.tpo.FelsaniMotors.dto.response.ImageResponse;
import com.example.uade.tpo.FelsaniMotors.entity.Foto;
import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;

/*
GET:
getFotosByPublicacion: Devuelve las fotos de la publicación en formato paginado.
getFotoById: busca la foto por ID. Si existe la devuelve, sino lanza excepción.
getFotoResponse: Devuelve la foto en formato base64 para mostrar en el frontend.
getImageResponse: Devuelve la foto en formato ImageResponse (id + base64).
getImagesFromPublicacion: Devuelve todas las fotos de una publicación en formato ImageResponse.

POST:
addFoto: Crea una foto nueva en la publicación, si está marcada como principal desmarca la anterior.
createImage: Crea una imagen independiente (sin asociación a publicacion).

DELETE:
deleteFoto: Borra la foto por ID.

PUT:
setMainFoto: Marca esa foto como la principal de la publicación, desmarca la principal anterior.
updateFotoOrder: Actualiza el orden de la foto en la publicación.
*/


public interface FotoService {
    
    Page<Foto> getFotosByPublicacion(Long idPublicacion, Pageable pageable);
    
    Foto getFotoById(Long idFoto);
    
    FotoResponse getFotoResponse(Long idFoto) throws SQLException;
    
    ImageResponse getImageResponse(Long idFoto) throws SQLException, IOException;
    
    List<ImageResponse> getImagesFromPublicacion(Long idPublicacion) throws SQLException, IOException;
    
    Foto addFoto(Long idPublicacion, FotoUploadRequest request) throws IOException, SQLException;
    
    Foto createImage(Foto foto);
    
    void deleteFoto(Long idFoto);
    
    Foto setMainFoto(Long fotoId);
    
    Foto getMainFoto(Publicacion publicacion);
    
    Foto updateFotoOrder(Long idFoto, Integer orden);
}
