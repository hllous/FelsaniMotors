package com.example.uade.tpo.FelsaniMotors.service.catalogo;

import com.example.uade.tpo.FelsaniMotors.entity.EstadoAuto;
import com.example.uade.tpo.FelsaniMotors.entity.MarcaAuto;
import com.example.uade.tpo.FelsaniMotors.entity.TipoCaja;
import com.example.uade.tpo.FelsaniMotors.entity.TipoCombustible;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogoServiceImpl implements CatalogoService {

    @Override
    public List<String> getMarcas() {

        List<String> marcas = new ArrayList<>();
        for (MarcaAuto marca : MarcaAuto.values()) {
            marcas.add(marca.name());
        }
        return marcas;
    }

    @Override
    public List<String> getEstados() {

        List<String> estados = new ArrayList<>();
        for (EstadoAuto estado : EstadoAuto.values()) {
            estados.add(estado.name());
        }
        return estados;
    }

    @Override
    public List<String> getCombustibles() {

        List<String> combustibles = new ArrayList<>();
        for (TipoCombustible combustible : TipoCombustible.values()) {
            combustibles.add(combustible.name());
        }
        return combustibles;
    }

    @Override
    public List<String> getTiposCaja() {
        
        List<String> tiposCaja = new ArrayList<>();
        for (TipoCaja tipoCaja : TipoCaja.values()) {
            tiposCaja.add(tipoCaja.name());
        }
        return tiposCaja;
    }
}
