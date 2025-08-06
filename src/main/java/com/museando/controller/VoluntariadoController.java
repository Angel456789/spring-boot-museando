package com.museando.controller;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.museando.model.EstadoVoluntariado;
import com.museando.model.Museo;
import com.museando.model.Usuario;
import com.museando.model.Voluntariado;
import com.museando.services.IntMuseos;
import com.museando.services.IntUsuarios;
import com.museando.services.IntVoluntariados;
import com.museando.utilerias.Utileria;

import jakarta.validation.Valid;

@Controller
public class VoluntariadoController {

	@Autowired
	private IntVoluntariados serviceVoluntariados;

	@Autowired
	private IntMuseos serviceMuseos; // servicio para obtener museos
	
	@Autowired
	private IntUsuarios serviceUsuarios;
	
	  @Autowired
	    private JavaMailSender mailSender;


	@GetMapping("/voluntariado/nueva")
	public String mostrarFormulario(
	        @RequestParam(value = "museoId", required = false) Integer museoId, 
	        Model model) {

	    System.out.println("Mensaje recibido: " + model.getAttribute("msg"));
	    System.out.println("Voluntariado recibido: " + model.getAttribute("voluntariado"));

	    // Si el modelo NO contiene un voluntariado, agregamos uno nuevo vacío
	    if (!model.containsAttribute("voluntariado")) {
	        Voluntariado voluntariado = new Voluntariado();

	        if (museoId != null) {
	            Museo museo = serviceMuseos.buscarPorId(museoId);
	            voluntariado.setMuseo(museo);
	            model.addAttribute("museo", museo);
	        }

	        model.addAttribute("voluntariado", voluntariado);
	    }

	    return "voluntariado/formVoluntariado";
	}



	@PostMapping("/voluntariado/guardar")
	public String guardarVoluntariado(
	    @Valid Voluntariado voluntariado, 
	    BindingResult result, 
	    @RequestParam("archivoAdjunto") MultipartFile archivoAdjunto,
	    Model model, 
	    RedirectAttributes attributes, 
	    @RequestParam(value = "idMuseo", required = false) Integer idMuseo) {

	    // Validar si el archivo fue enviado
	    if (archivoAdjunto == null || archivoAdjunto.isEmpty()) {
	        model.addAttribute("error", "Debes subir un archivo antes de enviar tu solicitud.");

	        // Cargar el museo para mantener el idMuseo visible en el form
	        if (idMuseo != null) {
	            Museo museo = serviceMuseos.buscarPorId(idMuseo);
	            voluntariado.setMuseo(museo);
	            model.addAttribute("museo", museo);
	        }

	        // Volver a poner el voluntariado con los datos ingresados
	        model.addAttribute("voluntariado", voluntariado);

	        // Devolver la vista directamente, sin redirect para mantener los datos
	        return "voluntariado/formVoluntariado";
	    }

	    try {
	        // Si no viene el museo dentro del objeto, asignar a partir del idMuseo
	        if (voluntariado.getMuseo() == null || voluntariado.getMuseo().getId() == null) {
	            if (idMuseo != null) {
	                Museo museo = serviceMuseos.buscarPorId(idMuseo);
	                voluntariado.setMuseo(museo);
	            }
	        }

	        // Guardar archivo y asignar nombre
	        String ruta = "c:/museando/voluntariado-archivos/";
	        String nombreArchivo = Utileria.guardarArchivo(archivoAdjunto, ruta);
	        voluntariado.setArchivo(nombreArchivo);

	        // Obtener usuario autenticado
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String username = auth.getName();
	        Usuario usuarioActual = serviceUsuarios.buscarPorUsername(username);
	        voluntariado.setUsuario(usuarioActual);

	        // Guardar voluntariado
	        serviceVoluntariados.guardar(voluntariado);

	        // Mensaje éxito y redirect para limpiar el form y evitar resubmits
	        attributes.addFlashAttribute("msg", "¡Solicitud enviada correctamente!");
	        return "redirect:/voluntariado/nueva?museoId=" + voluntariado.getMuseo().getId();

	    } catch (Exception e) {
	        // Manejo de errores
	        String mensajeError = (e.getCause() != null && e.getCause().getCause() != null) 
	            ? e.getCause().getCause().getMessage() 
	            : e.getMessage();

	        if (mensajeError != null && mensajeError.contains("Duplicate entry")) {
	            model.addAttribute("error", "Ya tienes una solicitud pendiente o aceptada para este museo.");
	        } else {
	            model.addAttribute("error", "Error: " + mensajeError);
	        }

	        // Cargar museo para el form
	        if (idMuseo != null) {
	            Museo museo = serviceMuseos.buscarPorId(idMuseo);
	            voluntariado.setMuseo(museo);
	            model.addAttribute("museo", museo);
	        }

	        model.addAttribute("voluntariado", voluntariado);
	        return "voluntariado/formVoluntariado";  // Sin redirect para mantener datos y mostrar error
	    }
	}



	
	@GetMapping("/voluntariado/lista")
	public String listarVoluntariados(
	    @RequestParam(name = "estado", required = false) String estado,
	    Model model) {

	    List<Voluntariado> lista;

	    if (estado == null || estado.isEmpty()) {
	        // Mostrar todos
	        lista = serviceVoluntariados.obtenerTodos();
	    } else {
	        // Convertir a mayúsculas para que coincida con el Enum (si usas enum)
	        String estadoMayuscula = estado.toUpperCase();

	        try {
	            EstadoVoluntariado estadoEnum = EstadoVoluntariado.valueOf(estadoMayuscula);
	            lista = serviceVoluntariados.buscarPorEstado(estadoEnum);
	        } catch (IllegalArgumentException e) {
	            // Estado inválido, mostrar todos o manejar error
	            lista = serviceVoluntariados.obtenerTodos();
	            model.addAttribute("error", "Estado de filtro inválido: " + estado);
	        }
	    }

	    model.addAttribute("voluntariados", lista);
	    model.addAttribute("filtroEstado", estado); // para que en la vista puedas marcar el filtro activo
	    return "voluntariado/listVoluntariados";
	}

	

	// Método para eliminar voluntariado por id
    @GetMapping("/voluntariado/eliminar")
    public String eliminarVoluntariado(@RequestParam("id") Integer id, RedirectAttributes attributes) {
        try {
            Voluntariado v = serviceVoluntariados.buscarPorId(id);
            if (v != null) {
                // Eliminar archivo físico si existe
                if (v.getArchivo() != null) {
                	File archivo = new File("c:/museando/voluntariado-archivos/" + v.getArchivo());
                    if (archivo.exists()) {
                        archivo.delete();
                    }
                }
                // Eliminar registro
                serviceVoluntariados.eliminar(id);
                attributes.addFlashAttribute("msg", "Voluntariado eliminado correctamente.");
            }
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al eliminar voluntariado: " + e.getMessage());
        }
        return "redirect:/voluntariado/lista";
    }
    
    @GetMapping("/voluntariado/descargar")
    public ResponseEntity<Resource> descargarArchivo(@RequestParam("id") Integer id) {
        try {
            Voluntariado v = serviceVoluntariados.buscarPorId(id);
            if (v == null || v.getArchivo() == null) {
                return ResponseEntity.notFound().build();
            }

            Path rutaArchivo = Paths.get("c:/museando/voluntariado-archivos/").resolve(v.getArchivo()).normalize();
            Resource recurso = new UrlResource(rutaArchivo.toUri());

            if (!recurso.exists()) {
                return ResponseEntity.notFound().build();
            }

            String nombreArchivo = recurso.getFilename();

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
                .body(recurso);

        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/voluntariado/enviarCorreo")
    public String enviarCorreo(
            @RequestParam("para") String para,
            @RequestParam("asunto") String asunto,
            @RequestParam("mensaje") String mensaje,
            RedirectAttributes attributes) {

        try {
        	
        	asunto = Utileria.capitalizarCadaPalabra(asunto);
        	mensaje = Utileria.capitalizarCadaPalabra(mensaje);

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(para);
            email.setSubject(asunto);
            email.setText(mensaje);
            email.setFrom("violeehdz.rdz@gmail.com"); // tu correo remitente

            mailSender.send(email);

            attributes.addFlashAttribute("msg", "Correo enviado correctamente a " + para);
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al enviar correo: " + e.getMessage());
        }
        return "redirect:/voluntariado/lista";  // vuelve a la lista de voluntarios
    }


    @GetMapping("/voluntariado/formGmail")
    public String mostrarFormularioCorreo(@RequestParam("idUsuario") Integer idUsuario, Model model, RedirectAttributes attributes) {
        Usuario usuario = serviceUsuarios.buscarPorId(idUsuario);
        if (usuario == null) {
            attributes.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/voluntariado/lista";
        }
        

        model.addAttribute("correoUsuario", usuario.getEmail());
        model.addAttribute("username", usuario.getUsername()); // o getUsername()

        return "voluntariado/formGmail"; // Aquí coincide con el nombre del archivo .html
    }


    @GetMapping("/voluntariado/cambiarEstado")
    public String cambiarEstado(@RequestParam("id") Integer id,
                                @RequestParam("nuevoEstado") String nuevoEstado,
                                RedirectAttributes attributes) {
        Voluntariado voluntariado = serviceVoluntariados.buscarPorId(id);
        if (voluntariado == null) {
            attributes.addFlashAttribute("mensaje", "Voluntariado no encontrado");
            attributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/voluntariado/lista";
        }

        // Validar que sólo se pueda cambiar estado si está PENDIENTE
        if (!voluntariado.getEstado().equals(EstadoVoluntariado.PENDIENTE)) {
            attributes.addFlashAttribute("mensaje", "No se puede cambiar el estado porque la solicitud ya está procesada (" 
                                      + voluntariado.getEstado() + ")");
            attributes.addFlashAttribute("alertClass", "alert-warning");
            return "redirect:/voluntariado/lista";
        }

        // Intentar cambiar estado
        try {
            voluntariado.setEstado(EstadoVoluntariado.valueOf(nuevoEstado));
            serviceVoluntariados.guardar(voluntariado);
            attributes.addFlashAttribute("mensaje", "Estado actualizado a " + nuevoEstado);
            attributes.addFlashAttribute("alertClass", "alert-success");
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("mensaje", "Estado inválido");
            attributes.addFlashAttribute("alertClass", "alert-danger");
        }

        return "redirect:/voluntariado/lista";
    }




}
