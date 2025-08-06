package com.museando.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.museando.model.Ubicacion;
import com.museando.repository.UbicacionesRepository;

@Service
public class ServiceUbicacionesInt implements IntUbicaciones {
    @Autowired
    private UbicacionesRepository repoUbicaciones;

    @Override
    public List<Ubicacion> obtenerUbicaciones() {
        return repoUbicaciones.findAll();
    }

    @Override
    public void agregar(Ubicacion ubicacion) {
        repoUbicaciones.save(ubicacion);
    }

    @Override
    public Ubicacion buscarPorId(Integer IdUbicacion) {
        Ubicacion ubicacion = null;
        Optional<Ubicacion> optional = repoUbicaciones.findById(IdUbicacion);
        if (optional.isPresent()) {
            ubicacion = optional.get();
        }
        return ubicacion;
    }

    @Override
    public void eliminar(Integer IdUbicacion) {
        repoUbicaciones.deleteById(IdUbicacion);
    }

    @Override
    public int totalUbicaciones() {
        return obtenerUbicaciones().size();
    }

    @Override
    public Page<Ubicacion> buscarTodas(Pageable page) {
        return repoUbicaciones.findAll(page);
    }

    @Override
    public boolean tieneMuseosAsociados(int idUbicacion) {
        Ubicacion ubicacion = buscarPorId(idUbicacion);
        if (ubicacion != null && ubicacion.getMuseos() != null) {
            return !ubicacion.getMuseos().isEmpty();
        }
        return false;
    }

} 