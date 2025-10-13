package com.example.uade.tpo.FelsaniMotors.repository;

import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    @Query("SELECT p FROM Publicacion p WHERE p.usuario.idUsuario = :idUsuario")
    List<Publicacion> findByIdUsuario(@Param("idUsuario") Long idUsuario);
    
    List<Publicacion> findByPrecioBetween(float precioMin, float precioMax);
    
    List<Publicacion> findByEstado(char estado);
    
    Page<Publicacion> findAllByOrderByFechaPublicacionDesc(Pageable pageable);
    
    @Query("SELECT p FROM Publicacion p WHERE " +
           "(:busqueda IS NULL OR :busqueda = '' OR " +
           "  LOWER(p.titulo) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "  LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "  LOWER(p.ubicacion) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "  LOWER(p.auto.marca) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "  LOWER(p.auto.modelo) LIKE LOWER(CONCAT('%', :busqueda, '%'))) AND " +
           "(:marcas IS NULL OR p.auto.marca IN :marcas) AND " +
           "(:modelos IS NULL OR p.auto.modelo IN :modelos) AND " +
           "(:anios IS NULL OR p.auto.anio IN :anios) AND " +
           "(:estados IS NULL OR p.auto.estado IN :estados) AND " +
           "(:combustibles IS NULL OR p.auto.combustible IN :combustibles) AND " +
           "(:tipoCategorias IS NULL OR p.auto.tipoCategoria IN :tipoCategorias) AND " +
           "(:tipoCajas IS NULL OR p.auto.tipoCaja IN :tipoCajas) AND " +
           "(:motores IS NULL OR p.auto.motor IN :motores) AND " +
           "(:kmMin IS NULL OR p.auto.kilometraje >= :kmMin) AND " +
           "(:kmMax IS NULL OR p.auto.kilometraje <= :kmMax) " +
           "ORDER BY p.fechaPublicacion DESC")
    Page<Publicacion> filtrar(
        @Param("busqueda") String busqueda,
        @Param("marcas") List<String> marcas,
        @Param("modelos") List<String> modelos,
        @Param("anios") List<String> anios,
        @Param("estados") List<String> estados,
        @Param("combustibles") List<String> combustibles,
        @Param("tipoCategorias") List<String> tipoCategorias,
        @Param("tipoCajas") List<String> tipoCajas,
        @Param("motores") List<String> motores,
        @Param("kmMin") Float kmMin,
        @Param("kmMax") Float kmMax,
        Pageable pageable
    );
    
    @Query("SELECT p.auto.marca FROM Publicacion p WHERE p.auto.marca IS NOT NULL GROUP BY p.auto.marca ORDER BY p.auto.marca")
    List<String> findAllMarcas();
    
    @Query("SELECT p.auto.modelo FROM Publicacion p WHERE p.auto.modelo IS NOT NULL GROUP BY p.auto.modelo ORDER BY p.auto.modelo")
    List<String> findAllModelos();
    
    @Query("SELECT p.auto.anio FROM Publicacion p WHERE p.auto.anio IS NOT NULL GROUP BY p.auto.anio ORDER BY p.auto.anio DESC")
    List<Integer> findAllAnios();
    
    @Query("SELECT p.auto.estado FROM Publicacion p WHERE p.auto.estado IS NOT NULL GROUP BY p.auto.estado ORDER BY p.auto.estado")
    List<String> findAllEstadosAuto();
    
    @Query("SELECT p.auto.combustible FROM Publicacion p WHERE p.auto.combustible IS NOT NULL GROUP BY p.auto.combustible ORDER BY p.auto.combustible")
    List<String> findAllCombustibles();
    
    @Query("SELECT p.auto.tipoCategoria FROM Publicacion p WHERE p.auto.tipoCategoria IS NOT NULL GROUP BY p.auto.tipoCategoria ORDER BY p.auto.tipoCategoria")
    List<String> findAllTipoCategorias();
    
    @Query("SELECT p.auto.tipoCaja FROM Publicacion p WHERE p.auto.tipoCaja IS NOT NULL GROUP BY p.auto.tipoCaja ORDER BY p.auto.tipoCaja")
    List<String> findAllTipoCajas();
    
    @Query("SELECT p.auto.motor FROM Publicacion p WHERE p.auto.motor IS NOT NULL GROUP BY p.auto.motor ORDER BY p.auto.motor")
    List<String> findAllMotores();
}