package com.museando.utilerias;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class Utileria {
	public static String guardarArchivo(MultipartFile multiPart, String ruta) {
		// Obtenemos el nombre original del archivo.
		String nombreOriginal = multiPart.getOriginalFilename();
		try {
			// Formamos el nombre del archivo para guardarlo en el disco duro.
			File imageFile = new File(ruta + nombreOriginal);
			System.out.println("Archivo: " + imageFile.getAbsolutePath());
			// Guardamos fisicamente el archivo en HD.
			multiPart.transferTo(imageFile);
			return nombreOriginal;
		} catch (IOException e) {
			System.out.println("Error " + e.getMessage());
			return null;
		}
	}

	public static String capitalizarCadaPalabra(String texto) {
	    if (texto == null || texto.isEmpty()) return texto;
	    String[] palabras = texto.toLowerCase().split("\\s+");
	    StringBuilder resultado = new StringBuilder();
	    for (String palabra : palabras) {
	        if (!palabra.isEmpty()) {
	            resultado.append(Character.toUpperCase(palabra.charAt(0)))
	                     .append(palabra.substring(1)).append(" ");
	        }
	    }
	    return resultado.toString().trim();
	}

}
