package com.museando.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.museando.model.Usuario;
import com.museando.model.Voluntariado;
import com.museando.services.IntUsuarios;
import com.museando.services.IntVoluntariados;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    private IntUsuarios serviceUsuarios;
    @Autowired
	private IntVoluntariados serviceVoluntariados;

    // Mostrar listado de usuarios sin paginación
    @GetMapping("/index")
    public String mostrarIndex(Model model) {
        List<Usuario> usuarios = serviceUsuarios.obtenerUsuarios();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("total", serviceUsuarios.totalUsuarios());
        return "usuarios/listUsuarios";
    }

    // Mostrar listado paginado de usuarios
    @GetMapping("/paginado")
    public String mostrarIndexPaginado(Model model, Pageable page) {
        Page<Usuario> lista = serviceUsuarios.buscarTodas(page);
        model.addAttribute("usuarios", lista);
        model.addAttribute("total", serviceUsuarios.totalUsuarios());
        return "usuarios/listUsuarios";
    }

    @GetMapping("/eliminar")
    public String eliminarUsuario(@RequestParam("id") int idUsuario, RedirectAttributes attributes) {
        // Primero obtenemos el usuario a eliminar
        Usuario usuario = serviceUsuarios.buscarPorId(idUsuario); // Asegúrate de tener este método

        if (usuario != null && usuario.getUsername().equalsIgnoreCase("admin")) {
            attributes.addFlashAttribute("error", "No se puede eliminar al usuario administrador.");
            return "redirect:/usuarios/paginado";
        }

        // Validación si tiene postulaciones activas
        List<Voluntariado> voluntariados = serviceVoluntariados.buscarPorUsuarioId(idUsuario);
        if (!voluntariados.isEmpty()) {
            attributes.addFlashAttribute("error", "No se puede eliminar el usuario porque tiene postulaciones activas.");
            return "redirect:/usuarios/paginado";
        }

        // Si pasa todas las validaciones, se elimina
        serviceUsuarios.eliminar(idUsuario);
        attributes.addFlashAttribute("msg", "Usuario eliminado correctamente.");
        return "redirect:/usuarios/paginado";
    }

    
  
    
    @GetMapping("/bloquear")
	public String bloquear(@RequestParam("id") int idUsuario) {
		Usuario usuario = serviceUsuarios.buscarPorId(idUsuario);
		if (usuario != null) {
			usuario.setEstatus(0); // 0 = Bloqueado
			serviceUsuarios.agregar(usuario);
		}
		return "redirect:/usuarios/paginado";
	}

	@GetMapping("/desbloquear")
	public String desbloquear(@RequestParam("id") int idUsuario) {
		Usuario usuario = serviceUsuarios.buscarPorId(idUsuario);
		if (usuario != null) {
			usuario.setEstatus(1); // 1 = Activo
			serviceUsuarios.agregar(usuario);
		}
		return "redirect:/usuarios/paginado";
	}


   
}
