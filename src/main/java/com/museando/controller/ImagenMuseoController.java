package com.museando.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.museando.model.ImagenMuseo;
import com.museando.model.Museo;
import com.museando.services.IntImagenesMuseo;
import com.museando.services.IntMuseos;
import com.museando.utilerias.Utileria;

@Controller
@RequestMapping("/galeria")
public class ImagenMuseoController {

	@Autowired
	private IntImagenesMuseo serviceImagenes;

	@Autowired
	private IntMuseos serviceMuseos;

	// Mostrar galería de un museo
	@GetMapping("/museo/{idMuseo}")
	public String mostrarGaleria(@PathVariable Integer idMuseo, Model model) {
		Museo museo = serviceMuseos.buscarPorId(idMuseo);
		List<ImagenMuseo> imagenes = serviceImagenes.obtenerPorMuseo(idMuseo);

		System.out.println("Museo buscado: " + museo.getNombre());
		System.out.println("Número de imágenes encontradas: " + (imagenes != null ? imagenes.size() : "null"));

		model.addAttribute("museo", museo);
		model.addAttribute("imagenes", imagenes);

		return "museos/galeriaMuseo"; // Vista para mostrar la galería
	}

	// Guardar una imagen nueva
	@PostMapping("/guardar")
	public String guardarImagenes(@RequestParam Integer idMuseo,
			@RequestParam("archivoImagenes") MultipartFile[] archivos, RedirectAttributes attributes) {

		String ruta = "c:/museando/img-galeria/";
		Museo museo = serviceMuseos.buscarPorId(idMuseo);
		int contador = 0;

		for (MultipartFile multiPart : archivos) {
			if (!multiPart.isEmpty()) {
				String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
				if (nombreImagen != null) {
					ImagenMuseo imagen = new ImagenMuseo();
					imagen.setMuseo(museo);
					imagen.setImagen(nombreImagen);
					serviceImagenes.guardar(imagen);
					contador++;
				}
			}
		}

		if (contador > 0) {
			attributes.addFlashAttribute("msg", contador + " imagen(es) guardada(s) correctamente.");
		} else {
			attributes.addFlashAttribute("msg", "No se pudieron guardar las imágenes.");
		}

		return "redirect:/galeria/museo/" + idMuseo;
	}

	// Eliminar una imagen
	@GetMapping("/eliminar/{idImagen}/{idMuseo}")
	public String eliminarImagen(@PathVariable Integer idImagen, @PathVariable Integer idMuseo,
			RedirectAttributes attributes) {
		serviceImagenes.eliminar(idImagen);
		attributes.addFlashAttribute("msg", "Imagen eliminada correctamente.");
		return "redirect:/galeria/museo/" + idMuseo;
	}

}
