package com.museando.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.museando.model.ImagenMuseo;
import com.museando.repository.ImagenMuseoRepository;

@Service
public class ServiceImagenesInt implements IntImagenesMuseo {

    @Autowired
    private ImagenMuseoRepository repoImagenes;

    @Override
    public List<ImagenMuseo> obtenerTodas() {
        return repoImagenes.findAll();
    }

    @Override
    public List<ImagenMuseo> obtenerPorMuseo(Integer idMuseo) {
        return repoImagenes.findByMuseo_Id(idMuseo);
    }

    @Override
    public void guardar(ImagenMuseo imagen) {
        repoImagenes.save(imagen);
    }

    @Override
    public ImagenMuseo buscarPorId(Integer idImagenMuseo) {
        Optional<ImagenMuseo> optional = repoImagenes.findById(idImagenMuseo);
        return optional.orElse(null);
    }

    @Override
    public void eliminar(Integer idImagenMuseo) {
        repoImagenes.deleteById(idImagenMuseo);
    }

    @Override
    public int totalImagenes() {
        return (int) repoImagenes.count();
    }

    @Override
    public Page<ImagenMuseo> buscarTodas(Pageable page) {
        return repoImagenes.findAll(page);
    }
}