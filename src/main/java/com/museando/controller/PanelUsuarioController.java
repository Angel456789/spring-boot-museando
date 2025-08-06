package com.museando.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.museando.model.Usuario;
import com.museando.model.Voluntariado;
import com.museando.services.ServiceUsuariosInt;
import com.museando.services.VoluntariadosService;

@Controller
@RequestMapping("/mensajes")
public class PanelUsuarioController {

    @Autowired
    private VoluntariadosService voluntariadoService;

    @Autowired
    private ServiceUsuariosInt usuarioService;

    @GetMapping("/bandeja")
    public String verSolicitudes(@AuthenticationPrincipal User user, Model model) {
        if (user == null) return "redirect:/login";

        Usuario usuario = usuarioService.buscarPorUsername(user.getUsername());
        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado");
            return "error";
        }

        List<Voluntariado> solicitudes = voluntariadoService.buscarPorUsuarioId(usuario.getId());
        model.addAttribute("solicitudes", solicitudes);
        return "usuarios/misSolicitudes";
    }



}

    


