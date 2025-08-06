package com.museando.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.museando.model.Ubicacion;
import com.museando.services.IntUbicaciones;
import com.museando.utilerias.Utileria;

@Controller
@RequestMapping("/ubicaciones")
public class UbicacionesController {
    @Autowired
    private IntUbicaciones serviceUbicaciones;

    @GetMapping("/index")
    public String redirigirIndex() {
        return "redirect:/ubicaciones/paginado";
    }

    @GetMapping("/paginado")
    public String mostrarIndexPaginado(Model model, Pageable page) {
        Page<Ubicacion> lista = serviceUbicaciones.buscarTodas(page);
        int total = serviceUbicaciones.totalUbicaciones();
        model.addAttribute("total", total);
        model.addAttribute("ubicaciones", lista);
        return "ubicaciones/listUbicaciones";
    }

    /*
    @GetMapping("/eliminar")
    public String eliminar(@RequestParam("id") int idUbicacion) {
        serviceUbicaciones.eliminar(idUbicacion);
        return "redirect:/ubicaciones/index";
    }
    */

    
    @GetMapping("/eliminar")
    public String eliminar(@RequestParam("id") int idUbicacion, RedirectAttributes redirectAttributes) {
        if (serviceUbicaciones.tieneMuseosAsociados(idUbicacion)) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar la ubicación porque está asociada a uno o más museos.");
            return "redirect:/ubicaciones/paginado";
        }
        serviceUbicaciones.eliminar(idUbicacion);
        redirectAttributes.addFlashAttribute("msg", "Ubicación eliminada exitosamente.");
        return "redirect:/ubicaciones/paginado";
    }


    @GetMapping("/testflash")
    public String testFlash(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("msg", "Mensaje de prueba!");
        return "redirect:/ubicaciones/index";
    }



    @GetMapping("/nueva")
    public String nuevaUbicacion(Ubicacion ubicacion) {
        return "ubicaciones/formUbicacion.html";
    }

    @PostMapping("/guardar")
    public String guardarUbicacion(Ubicacion ubicacion, RedirectAttributes redirectAttributes) {
    	
    	// Capitalizar la calle antes de guardar
        ubicacion.setCalle(Utileria.capitalizarCadaPalabra(ubicacion.getCalle()));
        ubicacion.setColonia(Utileria.capitalizarCadaPalabra(ubicacion.getColonia()));
        ubicacion.setMunicipio(Utileria.capitalizarCadaPalabra(ubicacion.getMunicipio()));
        ubicacion.setEstado(Utileria.capitalizarCadaPalabra(ubicacion.getEstado()));
    	
        serviceUbicaciones.agregar(ubicacion);
        redirectAttributes.addFlashAttribute("msg", "Ubicación guardada correctamente.");
        return "redirect:/ubicaciones/paginado";
    }


    @GetMapping("/consultar")
    public String consultarUbicacion(@RequestParam("id") int idUbicacion, Model model) {
        Ubicacion ubicacion = serviceUbicaciones.buscarPorId(idUbicacion);
        model.addAttribute("ubicacion", ubicacion);
        return "ubicaciones/formUbicacion.html";
    }
    
    
} 