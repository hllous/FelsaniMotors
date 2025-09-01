package com.example.uade.tpo.FelsaniMotors.service.foto;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.uade.tpo.FelsaniMotors.dto.response.FotoResponse;
import com.example.uade.tpo.FelsaniMotors.entity.Foto;
import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;
import com.example.uade.tpo.FelsaniMotors.repository.FotoRepository;
import com.example.uade.tpo.FelsaniMotors.repository.PublicacionRepository;

import jakarta.persistence.EntityNotFoundException;

/*
Metodos CRUD:

GET:
getFotosByPublicacion: Devuelve las fotos de la publicación en formato paginado.
getFotoById: Devuelve la foto por ID, si no existe devuelve un 404.
getFotoData: Devuelve los bytes de la imagen, si no existe devuelve un 404.
getMainFoto: Devuelve la foto principal de la publicación.

POST:
addFoto: Crea una foto nueva en la publicación, si la marcás como principal desmarca la anterior.

DELETE:
deleteFoto: Borra la foto por ID, si sale bien devuelve 204 No Content.

PUT:
setMainFoto: Marca esa foto como principal y desmarca la anterior, si no existe devuelve un 404, si sale bien devuelve 200 OK.
updateFotoOrder: Actualiza el orden de la foto y devuelve la foto actualizada, si no existe devuelve un 404, si sale bien devuelve 200 OK.
*/

@Service
public class FotoServiceImpl implements FotoService {

    @Autowired
    private FotoRepository fotoRepository;
    
    @Autowired
    private PublicacionRepository publicacionRepository;

    @Override
    public Page<Foto> getFotosByPublicacion(Long idPublicacion, Pageable pageable) {
        return fotoRepository.findByIdPublicacion(idPublicacion, pageable);
    }

    @Override
    public Foto getFotoById(Long idFoto) {
        return fotoRepository.findById(idFoto)
            .orElseThrow(() -> new EntityNotFoundException("Foto no encontrada con ID: " + idFoto));
    }
    
    @Override
    public byte[] getFotoData(Long idFoto) {
        Foto foto = this.getFotoById(idFoto);
        return foto.getDatos();
    }
    
    @Override
    public FotoResponse getFotoResponse(Long idFoto) {
        Foto foto = this.getFotoById(idFoto);
        return new FotoResponse(foto.getDatos(), MediaType.IMAGE_JPEG);
    }

    @Override
    public Foto addFoto(Long idPublicacion, MultipartFile archivo, Boolean esPrincipal, Integer orden) throws IOException {
        
        Publicacion publicacion = publicacionRepository.findById(idPublicacion).get();
        
        Foto foto = new Foto();
        foto.setPublicacion(publicacion);
        
        
        foto.setDatos(archivo.getBytes());
        
        
        if (esPrincipal != null && esPrincipal) {
            
            fotoRepository.findByPublicacionAndEsPrincipal(publicacion, true)
                .ifPresent(fotoPrincipal -> {
                    fotoPrincipal.setEsPrincipal(false);
                    fotoRepository.save(fotoPrincipal);
                });
            
            foto.setEsPrincipal(true);
        } else {
            foto.setEsPrincipal(false);
        }
        
        
        if (orden != null) {
            foto.setOrden(orden);
        }
        
        return fotoRepository.save(foto);
    }

    @Override
    public void deleteFoto(Long idFoto) {
        fotoRepository.deleteById(idFoto);
    }
    
    @Override
    public Foto setMainFoto(Long fotoId) {
        Foto foto = this.getFotoById(fotoId);
        Publicacion publicacion = foto.getPublicacion();
        
        
        fotoRepository.findByPublicacionAndEsPrincipal(publicacion, true)
            .ifPresent(fotoPrincipal -> {
                fotoPrincipal.setEsPrincipal(false);
                fotoRepository.save(fotoPrincipal);
            });
        
        
        foto.setEsPrincipal(true);
        return fotoRepository.save(foto);
    }
    
    @Override
    public Foto getMainFoto(Publicacion publicacion) {
        return fotoRepository.findByPublicacionAndEsPrincipal(publicacion, true)
            .orElse(null);
    }
    
    @Override
    public Foto updateFotoOrder(Long idFoto, Integer orden) {
        Foto foto = this.getFotoById(idFoto);
        foto.setOrden(orden);
        return fotoRepository.save(foto);
    }
}
