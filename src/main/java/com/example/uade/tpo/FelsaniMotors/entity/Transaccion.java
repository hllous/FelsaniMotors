package com.example.uade.tpo.FelsaniMotors.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "transacciones")
@AllArgsConstructor
@NoArgsConstructor
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaccion;
    
    @ManyToOne
    @JoinColumn(name = "id_publicacion", nullable = false)
    private Publicacion publicacion;
    
    @ManyToOne
    @JoinColumn(name = "id_comprador", nullable = false)
    private Usuario comprador;
    
    @ManyToOne
    @JoinColumn(name = "id_vendedor", nullable = false)
    private Usuario vendedor;
    
    @Column(nullable = false)
    private float monto;
    
    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;
    
    @Column(name = "fecha_transaccion")
    private Date fechaTransaccion;
    
    /*
     * Estados:
     * 
     * PENDIENTE
     * COMPLETADA
     * CANCELADA
     * 
     */

    @Column(nullable = false)
    private String estado; 
    
    @Column
    private String referenciaPago;
    
    @Column
    private String comentarios;
}
