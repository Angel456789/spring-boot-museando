package com.museando.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import com.museando.model.Categoria;
import com.museando.model.EstatusMuseo;
import com.museando.model.ImagenMuseo;
import com.museando.model.Museo;
import com.museando.model.Sala;
import com.museando.model.Ubicacion;
import com.museando.model.Voluntariado;
import com.museando.services.IntCategorias;
import com.museando.services.IntImagenesMuseo;
import com.museando.services.IntMuseos;
import com.museando.services.IntSalas;
import com.museando.services.IntUbicaciones;
import com.museando.services.IntVoluntariados;
import com.museando.utilerias.Utileria;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/museos")
public class MuseosController {

	@Autowired
	private IntCategorias serviceCategorias;

	@Autowired
	private IntMuseos serviceMuseos;

	@Autowired
	private IntUbicaciones serviceUbicaciones;

	@Autowired
	private IntImagenesMuseo serviceImagenes;

	@Autowired
	private IntSalas serviceSalas;
	
	@Autowired
	private IntVoluntariados serviceVoluntariados;


	@GetMapping("/index")
	public String redireccionarIndex() {
		return "redirect:/museos/paginado";
	}

	@GetMapping("/nueva")
	public String nueva(Museo museo, Model model) {
		List<Categoria> categorias = serviceCategorias.obtenerCategorias();
		List<Ubicacion> ubicaciones = serviceUbicaciones.obtenerUbicaciones();
		model.addAttribute("categorias", categorias);
		model.addAttribute("ubicaciones", ubicaciones);
		model.addAttribute("estatuses", EstatusMuseo.values());
		model.addAttribute("museo", museo);
		return "museos/formMuseo";
	}

	@PostMapping("/guardar")
	public String guardar(@Valid Museo museo, BindingResult result, RedirectAttributes attributes, Model model,
			@RequestParam("archivoImagen") MultipartFile multiPart) {

		// Si hay errores de validación, regresamos al formulario con los datos
		// necesarios
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrió un error: " + error.getDefaultMessage());
			}
			List<Categoria> categorias = serviceCategorias.obtenerCategorias();
			List<Ubicacion> ubicaciones = serviceUbicaciones.obtenerUbicaciones();
			model.addAttribute("categorias", categorias);
			model.addAttribute("ubicaciones", ubicaciones);
			model.addAttribute("estatuses", EstatusMuseo.values()); // <--- aquí también
			return "museos/formMuseo";
		}

		// Si el usuario subió una imagen nueva, la guardamos y asignamos al museo
		if (!multiPart.isEmpty()) {
			String ruta = "c:/museando/img-museos/"; // Ruta en Windows
			String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
			if (nombreImagen != null) {
				museo.setImagen(nombreImagen);
			}
		} else {
			// Si no subió imagen nueva y estamos editando (el id existe), conservamos la
			// imagen previa
			if (museo.getId() != null && museo.getId() > 0) {
				Museo museoActual = serviceMuseos.buscarPorId(museo.getId());
				museo.setImagen(museoActual.getImagen());
			}
		}

		museo.setNombre(Utileria.capitalizarCadaPalabra(museo.getNombre()));
		museo.setDescripcion(Utileria.capitalizarCadaPalabra(museo.getDescripcion()));
		museo.setHorario(Utileria.capitalizarCadaPalabra(museo.getHorario()));

		// Validar si ya existe un museo con el mismo nombre
		Museo museoExistente = serviceMuseos.buscarPorNombre(museo.getNombre());

		if (museoExistente != null && (museo.getId() == null || !museoExistente.getId().equals(museo.getId()))) {
			model.addAttribute("categorias", serviceCategorias.obtenerCategorias());
			model.addAttribute("ubicaciones", serviceUbicaciones.obtenerUbicaciones());
			model.addAttribute("estatuses", EstatusMuseo.values());
			model.addAttribute("museo", museo);
			model.addAttribute("error", "Ya existe un museo con ese nombre.");
			return "museos/formMuseo";
		}

		// Guardamos o actualizamos el museo
		serviceMuseos.agregar(museo);

		// Mensaje para mostrar después de guardar
		attributes.addFlashAttribute("msg", "¡Museo guardado correctamente!");

		// Redirigimos a la lista de museos
		return "redirect:/museos/paginado";
	}

	@GetMapping("/detalle")
	public String detalle(@RequestParam("id") int idMuseo, Model model) {
		Museo museo = serviceMuseos.buscarPorId(idMuseo);
		List<ImagenMuseo> imagenes = serviceImagenes.obtenerPorMuseo(idMuseo);

		List<Sala> salas = serviceSalas.buscarPorMuseo(idMuseo);
		model.addAttribute("salas", salas);

		model.addAttribute("museo", museo);
		model.addAttribute("imagenes", imagenes);
		return "museos/detalleMuseo";
	}

	@GetMapping("/paginado")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Museo> lista = serviceMuseos.buscarTodas(page);
		int total = serviceMuseos.totalMuseos();

		model.addAttribute("total", total);
		model.addAttribute("museos", lista);
		return "museos/listMuseos";
	}

	@GetMapping("/editar")
	public String editarMuseo(@RequestParam("id") int idMuseo, Model model) {
		Museo museo = serviceMuseos.buscarPorId(idMuseo);
		System.out.println("Fecha Fundación: " + museo.getFechaFundacion());
		List<Categoria> categorias = serviceCategorias.obtenerCategorias();
		List<Ubicacion> ubicaciones = serviceUbicaciones.obtenerUbicaciones();
		model.addAttribute("categorias", categorias);
		model.addAttribute("ubicaciones", ubicaciones);
		model.addAttribute("museo", museo);
		model.addAttribute("estatuses", EstatusMuseo.values());
		return "museos/formMuseo";
	}

	@GetMapping("/eliminar")
	public String eliminar(@RequestParam("id") int idMuseo, RedirectAttributes redirectAttrs) {
	    // Verificar si hay voluntariados asociados
	    List<Voluntariado> voluntariados = serviceVoluntariados.obtenerPorMuseo(idMuseo);

	    if (voluntariados != null && !voluntariados.isEmpty()) {
	        redirectAttrs.addFlashAttribute("error", "No se puede eliminar el museo porque tiene solicitudes de voluntariado asociadas.");
	        return "redirect:/museos/paginado";
	    }

	    // Si no hay voluntariados, se puede eliminar
	    serviceMuseos.eliminar(idMuseo);
	    redirectAttrs.addFlashAttribute("msg", "Museo eliminado correctamente con todas sus salas y obras.");
	    return "redirect:/museos/paginado";
	}

	
	@GetMapping("/todos")
	public String mostrarTodos(
	    Model model,
	    @RequestParam(required = false) String nombre,
	    @RequestParam(required = false) Integer categoriaId,
	    @RequestParam(defaultValue = "0") int page) {

	    int size = 9; // Número de elementos por página
	    Pageable pageable = PageRequest.of(page, size);
	    Page<Museo> museos;

	    if ((nombre == null || nombre.isEmpty()) && categoriaId == null) {
	        // Sin filtros: mostrar todos
	        museos = serviceMuseos.buscarTodas(pageable);
	    } else if ((nombre != null && !nombre.isEmpty()) && categoriaId == null) {
	        // Solo filtro por nombre
	        museos = serviceMuseos.buscarPorNombre(nombre, pageable);
	    } else if ((nombre == null || nombre.isEmpty()) && categoriaId != null) {
	        // Solo filtro por categoría
	        museos = serviceMuseos.buscarPorCategoria(categoriaId, pageable);
	    } else {
	        // Filtro por nombre y categoría
	        museos = serviceMuseos.buscarPorNombreYcategoria(nombre, categoriaId, pageable);
	    }

	    int total = (int) museos.getTotalElements();

	    model.addAttribute("total", total);
	    model.addAttribute("museos", museos);
	    model.addAttribute("nombre", nombre);
	    model.addAttribute("categoriaId", categoriaId);

	    // También necesitas enviar la lista de categorías para el dropdown de filtro
	    model.addAttribute("categorias", serviceCategorias.obtenerCategorias());

	    return "listaMuseos";  // Nombre de tu plantilla Thymeleaf
	}
	

	
	@GetMapping("/search")
	public String buscarMuseos(
	        @RequestParam(required = false) String nombre,
	        @RequestParam(required = false) Integer categoriaId,
	        @RequestParam(defaultValue = "0") int page,
	        Model model) {

	    try {
	        System.out.println("Parámetros de búsqueda: nombre=" + nombre + ", categoriaId=" + categoriaId + ", page=" + page);

	        int size = 9;
	        Pageable pageable = PageRequest.of(page, size);

	        if (nombre != null) {
	            nombre = nombre.trim();
	            if (nombre.isEmpty()) {
	                nombre = null;
	            }
	        }

	        if (categoriaId != null && categoriaId <= 0) {
	            categoriaId = null;
	        }

	        Page<Museo> museos;

	        if (nombre == null && categoriaId == null) {
	            museos = serviceMuseos.buscarTodas(pageable);
	        } else if (nombre != null && categoriaId == null) {
	            museos = serviceMuseos.buscarPorNombre(nombre, pageable);
	        } else if (nombre == null && categoriaId != null) {
	            museos = serviceMuseos.buscarPorCategoria(categoriaId, pageable);
	        } else {
	            museos = serviceMuseos.buscarPorNombreYcategoria(nombre, categoriaId, pageable);
	        }

	        model.addAttribute("museos", museos);
	        model.addAttribute("categorias", serviceCategorias.obtenerCategorias());
	        model.addAttribute("nombreBusqueda", nombre);
	        model.addAttribute("categoriaSeleccionada", categoriaId);
	        model.addAttribute("page", page);

	        return "listaMuseos";

	    } catch (Exception ex) {
	        ex.printStackTrace(); // Imprime stacktrace en consola
	        model.addAttribute("error", "Error al buscar museos: " + ex.getMessage());
	        return "error_general"; // Crea esta vista para mostrar errores
	    }
	}


}
