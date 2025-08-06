package com.museando.services;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.museando.model.Ubicacion;

public interface IntUbicaciones {
    public List<Ubicacion> obtenerUbicaciones();
    public void agregar(Ubicacion ubicacion);
    public Ubicacion buscarPorId(Integer IdUbicacion);
    public void eliminar(Integer IdUbicacion);
    public int totalUbicaciones();
    Page<Ubicacion> buscarTodas(Pageable page);
    boolean tieneMuseosAsociados(int idUbicacion);
} 