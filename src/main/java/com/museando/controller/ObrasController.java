package com.museando.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.museando.model.Obra;
import com.museando.model.Sala;
import com.museando.model.TipoObra;
import com.museando.services.IntObras;
import com.museando.services.IntSalas;
import com.museando.utilerias.Utileria;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/obras")
public class ObrasController {

	@Autowired
	private IntObras serviceObras;

	@Autowired
	private IntSalas serviceSalas;

	@GetMapping("/index")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Obra> lista = serviceObras.buscarTodas(page);
		int total = serviceObras.totalObras();
		model.addAttribute("total", total);
		model.addAttribute("obras", lista);
		return "obras/listObras";
	}

	@GetMapping("/lista")
	public String mostrarListaSinPaginado(Model model) {
		List<Obra> lista = serviceObras.obtenerObras(); // usa el método sin paginación
		model.addAttribute("obras", lista);
		return "hola";
	}

	@GetMapping("/nueva")
	public String nueva(Obra obra, Model model) {
		List<Sala> salas = serviceSalas.obtenerSalas();
		model.addAttribute("salas", salas);
		model.addAttribute("obra", obra);
		model.addAttribute("tipos", TipoObra.values());

		return "obras/formObra";
	}

	@PostMapping("/guardar")
	public String guardar(@Valid Obra obra, BindingResult result, RedirectAttributes attributes, Model model,
			@RequestParam("archivoImagen") MultipartFile multiPart) {

		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Error: " + error.getDefaultMessage());
			}
			List<Sala> salas = serviceSalas.obtenerSalas();
			model.addAttribute("salas", salas);
			model.addAttribute("tipos", TipoObra.values());
			return "obras/formObra";
		}

		if (!multiPart.isEmpty()) {
			String ruta = "c:/museando/img-obras/";
			String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
			if (nombreImagen != null) {
				obra.setImagen(nombreImagen);
			}
		} else {
			// Si es edición, conservar imagen previa
			if (obra.getId() != null && obra.getId() > 0) {
				Obra obraActual = serviceObras.buscarPorId(obra.getId());
				obra.setImagen(obraActual.getImagen());
			}
		}
		
		obra.setTitulo(Utileria.capitalizarCadaPalabra(obra.getTitulo()));
		obra.setAutor(Utileria.capitalizarCadaPalabra(obra.getAutor()));
		obra.setDescripcion(Utileria.capitalizarCadaPalabra(obra.getDescripcion()));


		serviceObras.agregar(obra);
		attributes.addFlashAttribute("msg", "¡Obra guardada correctamente!");
		return "redirect:/obras/index";
	}

	@GetMapping("/detalle")
	public String detalle(@RequestParam("id") int idObra, Model model) {
		Obra obra = serviceObras.buscarPorId(idObra);
		model.addAttribute("obra", obra);
		return "obras/detalleObra";
	}

	@GetMapping("/editar")
	public String editar(@RequestParam("id") int idObra, Model model) {
		Obra obra = serviceObras.buscarPorId(idObra);
		List<Sala> salas = serviceSalas.obtenerSalas();
		model.addAttribute("salas", salas);
		model.addAttribute("obra", obra);
		model.addAttribute("tipos", TipoObra.values());
		return "obras/formObra";
	}

	@GetMapping("/por-sala")
	public String obrasPorSala(@RequestParam Integer idSala, Model model) {
		List<Obra> obras = serviceObras.buscarPorSala(idSala);
		Sala sala = serviceSalas.buscarPorId(idSala);
		model.addAttribute("obras", obras);
		model.addAttribute("sala", sala);
		return "obras/listaPorSala";
	}

	@GetMapping("/eliminar")
	public String eliminar(@RequestParam("id") int idObra) {
		serviceObras.eliminar(idObra);
		return "redirect:/obras/index";
	}

}
