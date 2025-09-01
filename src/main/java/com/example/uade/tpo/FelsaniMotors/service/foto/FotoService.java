package com.example.uade.tpo.FelsaniMotors.service.foto;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.uade.tpo.FelsaniMotors.dto.response.FotoResponse;
import com.example.uade.tpo.FelsaniMotors.entity.Foto;
import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;

/*
GET:
getFotosByPublicacion: Devuelve las fotos de la publicación en formato paginado.
getFotos: Devuelve las fotos de la publi usando findByIdPublicacion
getFotosById: busca la foto por ID usando getFotoByID. Si existe devuelve 200 OK con la entidad Foto, sino un 404.
getFotoImage: Devuelve los bytes de la imagen, si no existe devuelve un 404.

POST:
uploadFoto: Permite subir una nueva foto a una publicación. Lo guarda siempre como binario en la bd.
addFoto: Crea una foto nueva en la publicación, si la marcás como principal desmarca la anterior.

DELETE:
deleteFoto: Borra la foto por ID.

PUT:
setMainFoto: Marca esa foto como la principal de la publicación, desmarca la principal anterior (si existe).
updateFotoOrder: Actualiza el orden de la foto en la publicación y devuelve la foto actualizada.


 */


public interface FotoService {
    
    Page<Foto> getFotosByPublicacion(Long idPublicacion, Pageable pageable);
    
    
    Foto getFotoById(Long idFoto);
    
    
    byte[] getFotoData(Long idFoto);
    
    // Nuevo método para obtener respuesta completa con datos de la foto
    FotoResponse getFotoResponse(Long idFoto);
    
    
    Foto addFoto(Long idPublicacion, MultipartFile archivo, Boolean esPrincipal, Integer orden) throws IOException;
    
    
    void deleteFoto(Long idFoto);
    
    
    Foto setMainFoto(Long fotoId);
    
    
    Foto getMainFoto(Publicacion publicacion);
    
    
    Foto updateFotoOrder(Long idFoto, Integer orden);
}
