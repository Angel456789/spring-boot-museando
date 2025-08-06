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

import com.museando.model.Categoria;
import com.museando.services.IntCategorias;
import com.museando.utilerias.Utileria;

@Controller
@RequestMapping("/categorias")

public class CategoriasController {
	
	@Autowired
	private IntCategorias serviceCategorias;
	
	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Categoria> categorias=serviceCategorias.obtenerCategorias();
		System.out.println(categorias);
	    model.addAttribute("categorias", categorias);
	    model.addAttribute("total", serviceCategorias.totalCategorias());
		return "categorias/listCategorias";
	}

	@GetMapping("/paginado")
	public String mostrarIndexPaginado(Model model, Pageable page) {
	Page<Categoria> lista = serviceCategorias.buscarTodas(page);
	 int total = serviceCategorias.totalCategorias();
	    model.addAttribute("total", total);
	model.addAttribute("categorias", lista);
	return "categorias/listCategorias";
	}

	
	@GetMapping("/eliminar")
	public String eliminar(@RequestParam("id") int idCategoria, RedirectAttributes redirectAttributes) {
	    if (serviceCategorias.tieneMuseosAsociados(idCategoria)) {
	        redirectAttributes.addFlashAttribute("error", "No se puede eliminar la categoría porque está asociada a uno o más museos.");
	        return "redirect:/categorias/paginado";
	    }
	    serviceCategorias.eliminar(idCategoria);
	    redirectAttributes.addFlashAttribute("msg", "Categoría eliminada exitosamente.");
	    return "redirect:/categorias/paginado";
	}


	
	@GetMapping("/nueva")
	public String nuevaCategoria(Categoria categoria) {
		return "categorias/formCategoria.html";
	}
	
	/*
	@PostMapping("/guardar")
	public String guardarCategorias(Categoria categoria, RedirectAttributes redirectAttributes) {
	    // Capitalizar los datos
	    categoria.setNombre(Utileria.capitalizarCadaPalabra(categoria.getNombre()));
	    categoria.setDescripcion(Utileria.capitalizarCadaPalabra(categoria.getDescripcion()));

	    if (categoria.getId() != null) {
	        // Ya existe, es edición: buscamos la categoría original
	        Categoria categoriaExistente = serviceCategorias.buscarPorId(categoria.getId());

	        // Preservamos la lista de museos asociados
	        categoria.setMuseos(categoriaExistente.getMuseos());
	    }

	    serviceCategorias.agregar(categoria); // guarda o actualiza
	    redirectAttributes.addFlashAttribute("msg", "¡Categoría guardada correctamente!");
	    return "redirect:/categorias/paginado";
	}*/
	
	@PostMapping("/guardar")
	public String guardarCategorias(Categoria categoria, RedirectAttributes redirectAttributes) {
	    categoria.setNombre(Utileria.capitalizarCadaPalabra(categoria.getNombre()));
	    categoria.setDescripcion(Utileria.capitalizarCadaPalabra(categoria.getDescripcion()));

	    Categoria catExistente = serviceCategorias.buscarPorNombre(categoria.getNombre());

	    if (catExistente != null && (categoria.getId() == null || !catExistente.getId().equals(categoria.getId()))) {
	        redirectAttributes.addFlashAttribute("error", "Ya existe una categoría con ese nombre.");
	        if (categoria.getId() == null) {
	            return "redirect:/categorias/nueva";
	        } else {
	            return "redirect:/categorias/consultar?id=" + categoria.getId();
	        }
	    }

	    if (categoria.getId() != null) {
	        Categoria categoriaExistente = serviceCategorias.buscarPorId(categoria.getId());
	        categoria.setMuseos(categoriaExistente.getMuseos());
	    }

	    serviceCategorias.agregar(categoria);
	    redirectAttributes.addFlashAttribute("msg", "¡Categoría guardada correctamente!");
	    return "redirect:/categorias/paginado";
	}



	
	@GetMapping("/consultar")
	public String consultarCategoria(@RequestParam("id") int idCategoria, Model model) {
		//System.out.println(idCategoria);
		Categoria categoria =serviceCategorias.buscarPorId(idCategoria);
		System.out.println(categoria);
		model.addAttribute("categoria", categoria);
		return "categorias/formCategoria.html";
	}
}
