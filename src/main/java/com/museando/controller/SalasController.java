package com.museando.controller;

import java.util.List;

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

import com.museando.model.Museo;
import com.museando.model.Obra;
import com.museando.model.Sala;
import com.museando.model.TipoSala;
import com.museando.services.IntMuseos;
import com.museando.services.IntObras;
import com.museando.services.IntSalas;
import com.museando.utilerias.Utileria;

@Controller
@RequestMapping("/salas")
public class SalasController {

	@Autowired
	private IntSalas serviceSalas;

	@Autowired
	private IntObras serviceObras;

	@Autowired
	private IntMuseos serviceMuseos;

	@GetMapping("/index")
	public String redirigir() {
		return "redirect:/salas/paginado";
	}

	@GetMapping("/paginado")
	public String mostrarSalas(Model model, Pageable page) {
		Page<Sala> lista = serviceSalas.buscarTodas(page);
		model.addAttribute("salas", lista);
		model.addAttribute("total", serviceSalas.totalSalas());
		return "salas/listSalas";
	}

	@GetMapping("/nueva")
	public String nueva(Sala sala, Model model) {
		List<Museo> museos = serviceMuseos.obtenerMuseos();
		model.addAttribute("museos", museos);
		model.addAttribute("tipos", TipoSala.values());
		return "salas/formSala";
	}

	@PostMapping("/guardar")
	public String guardar(Sala sala, RedirectAttributes redirectAttrs) {
		
		sala.setNombre(Utileria.capitalizarCadaPalabra(sala.getNombre()));
		sala.setDescripcion(Utileria.capitalizarCadaPalabra(sala.getDescripcion()));
		
		serviceSalas.agregar(sala);
		redirectAttrs.addFlashAttribute("mensaje", "¡Sala guardada correctamente!");
		return "redirect:/salas/paginado";

	}

	@GetMapping("/editar")
	public String editar(@RequestParam("id") int idSala, Model model) {
		Sala sala = serviceSalas.buscarPorId(idSala);
		List<Museo> museos = serviceMuseos.obtenerMuseos();
		model.addAttribute("museos", museos);
		model.addAttribute("tipos", TipoSala.values());
		model.addAttribute("sala", sala);
		return "salas/formSala";
	}

	@GetMapping("/detalle")
	public String detalle(@RequestParam("id") int idSala, Model model) {
		Sala sala = serviceSalas.buscarPorId(idSala);
		model.addAttribute("sala", sala);

		List<Obra> obras = serviceObras.buscarPorSala(sala.getId());
		model.addAttribute("obras", obras);

		return "salas/detalleSala";
	}

	@GetMapping("/eliminar")
	public String eliminar(@RequestParam("id") int idSala) {
		serviceSalas.eliminar(idSala);
		return "redirect:/salas/paginado";
	}

}
