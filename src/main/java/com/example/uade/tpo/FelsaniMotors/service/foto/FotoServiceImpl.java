package com.example.uade.tpo.FelsaniMotors.service.foto;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.uade.tpo.FelsaniMotors.dto.request.FotoUploadRequest;
import com.example.uade.tpo.FelsaniMotors.dto.response.FotoResponse;
import com.example.uade.tpo.FelsaniMotors.dto.response.ImageResponse;
import com.example.uade.tpo.FelsaniMotors.entity.Foto;
import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;
import com.example.uade.tpo.FelsaniMotors.repository.FotoRepository;
import com.example.uade.tpo.FelsaniMotors.repository.PublicacionRepository;

import jakarta.persistence.EntityNotFoundException;

/*
Metodos CRUD:

GET:
getFotosByPublicacion: Devuelve las fotos de la publicación en formato paginado.
getFotoById: Devuelve la foto por ID, si no existe lanza excepcion.
getFotoResponse: Devuelve la foto en formato base64 para enviar al frontend.
getMainFoto: Devuelve la foto principal de la publicación.

POST:
addFoto: Crea una foto nueva en la publicación, si la marcás como principal desmarca la anterior.

DELETE:
deleteFoto: Borra la foto por ID.

PUT:
setMainFoto: Marca esa foto como principal y desmarca la anterior.
updateFotoOrder: Actualiza el orden de la foto.
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
    public FotoResponse getFotoResponse(Long idFoto) throws SQLException {
        Foto foto = this.getFotoById(idFoto);
        
        String base64Image = Base64.getEncoder()
                .encodeToString(foto.getImage().getBytes(1, (int) foto.getImage().length()));
        
        return FotoResponse.builder()
                .idFoto(foto.getIdFoto())
                .base64Image(base64Image)
                .esPrincipal(foto.getEsPrincipal())
                .orden(foto.getOrden())
                .build();
    }

    @Override
    public Foto addFoto(Long idPublicacion, FotoUploadRequest request) 
            throws IOException, SQLException {
        
        Publicacion publicacion = null;
        
        // Si tenemos idPublicacion, buscamos la publicación y la asociamos
        if (idPublicacion != null) {
            publicacion = publicacionRepository.findById(idPublicacion)
                    .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada con ID: " + idPublicacion));
            
            // Si esta foto va a ser la principal, desmarcamos la anterior
            if (request.getEsPrincipal() != null && request.getEsPrincipal()) {
                fotoRepository.findByPublicacionAndEsPrincipal(publicacion, true)
                    .ifPresent(fotoPrincipal -> {
                        fotoPrincipal.setEsPrincipal(false);
                        fotoRepository.save(fotoPrincipal);
                    });
            }
        }
        
        byte[] bytes = request.getFile().getBytes();
        Blob blob = new SerialBlob(bytes);
        
        Foto foto = Foto.builder()
                .publicacion(publicacion)
                .image(blob)
                .esPrincipal(request.getEsPrincipal() != null ? request.getEsPrincipal() : false)
                .orden(request.getOrden() != null ? request.getOrden() : 0)
                .build();
        
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
        
        // Desmarcar la foto principal actual
        fotoRepository.findByPublicacionAndEsPrincipal(publicacion, true)
            .ifPresent(fotoPrincipal -> {
                fotoPrincipal.setEsPrincipal(false);
                fotoRepository.save(fotoPrincipal);
            });
        
        // Marcar la nueva foto como principal
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
    
    @Override
    public Foto createImage(Foto foto) {
        return fotoRepository.save(foto);
    }
    
    @Override
    public ImageResponse getImageResponse(Long idFoto) throws SQLException, IOException {
        Foto foto = this.getFotoById(idFoto);
        String encodedString = Base64.getEncoder()
                .encodeToString(foto.getImage().getBytes(1, (int) foto.getImage().length()));
        return ImageResponse.builder()
                .id(idFoto)
                .file(encodedString)
                .build();
    }
    
    @Override
    public List<ImageResponse> getImagesFromPublicacion(Long idPublicacion) throws SQLException, IOException {
        Page<Foto> fotos = this.getFotosByPublicacion(idPublicacion, PageRequest.of(0, Integer.MAX_VALUE));
        
        List<ImageResponse> response = new ArrayList<>();
        for (Foto foto : fotos.getContent()) {
            try {
                String encodedString = Base64.getEncoder()
                        .encodeToString(foto.getImage().getBytes(1, (int) foto.getImage().length()));
                response.add(ImageResponse.builder().id(foto.getIdFoto()).file(encodedString).build());
            } catch (Exception e) {
                // Si hay algún problema con una foto, continuamos con las demás
                continue;
            }
        }
        
        return response;
    }
}
