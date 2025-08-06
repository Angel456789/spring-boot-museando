package com.museando.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import com.museando.model.Museo;
import com.museando.model.Perfil;
import com.museando.model.Usuario;
import com.museando.services.IntMuseos;
import com.museando.services.IntUsuarios;
import com.museando.utilerias.Utileria;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

	@Autowired
	private IntMuseos serviceMuseos;

	@Autowired
	private IntUsuarios serviceUsuarios;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/acerca")
	public String acerca() {
		return "acerca";
	}

	@GetMapping("/")
	public String mostrarInicio(Model model) {
		List<Museo> museosDestacados = serviceMuseos.buscarPorEstatusYDestacado();
		model.addAttribute("museosDestacados", museosDestacados);
		return "index";
	}

	
	@GetMapping("/login")
	public String login(@RequestParam(required = false) String error, Model model) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
	        return "redirect:/";  // evita mostrar login si ya está autenticado
	    }

	    if (error != null) {
	        if ("blocked".equals(error)) {
	            model.addAttribute("errorLogin", "Tu cuenta está deshabilitada");
	        } else {
	            model.addAttribute("errorLogin", "Usuario o contraseña incorrectos");
	        }
	    }
	    return "formLogin";
	}


	
	@GetMapping("/signup")
	public String registrarse() {
		return "formRegistro";
	}

	
	
	@PostMapping("/signup")
	public String registroUsuario(Usuario usuario, RedirectAttributes atributo, Model model) {
	    System.out.println(usuario);

	    // Validar si ya existe el username
	    if (serviceUsuarios.existsByUsername(usuario.getUsername())) {
	        model.addAttribute("errorUsername", "¡El nombre de usuario ya está registrado!");
	        return "formRegistro";
	    }

	    // Validar si ya existe el email
	    if (serviceUsuarios.existsByEmail(usuario.getEmail())) {
	        model.addAttribute("errorEmail", "¡El email ya está registrado!");
	        return "formRegistro";
	    }

	    // Capitalizar el nombre del usuario antes de guardar
	    usuario.setNombre(Utileria.capitalizarCadaPalabra(usuario.getNombre()));

	    String textoPlano = usuario.getPassword();
	    String encriptado = passwordEncoder.encode(textoPlano);
	    usuario.setPassword(encriptado);
	    usuario.setFechaRegistro(LocalDate.now());
	    usuario.setEstatus(1);
	    // Creamos el perfil que le asignaremos al usuario nuevo
	    Perfil perfil = new Perfil();
	    perfil.setId(3); // Perfil USUARIO
	    usuario.agregar(perfil);

	    System.out.println(usuario);
	    serviceUsuarios.agregar(usuario);
	    atributo.addFlashAttribute("msg", "Has sido registrado, ahora puedes iniciar sesion.");
	    return "redirect:/login";
	}

	

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/";
	}

	@GetMapping("/bcrypt/{texto}")
	@ResponseBody
	public String encriptar(@PathVariable String texto) {
		return texto + " Encriptado en Bcrypt: " + passwordEncoder.encode(texto);
	}

	@GetMapping("/voluntariado")
	public String mostrarPaginaVoluntariado() {
	    return "voluntariado";
	}

}
