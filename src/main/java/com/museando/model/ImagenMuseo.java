package com.museando.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name="ImagenesMuseo")
public class ImagenMuseo {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	    private Integer id;
	    private String imagen;

	    @ManyToOne
	    @JoinColumn(name = "idMuseo")
	    private Museo museo;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getImagen() {
			return imagen;
		}

		public void setImagen(String imagen) {
			this.imagen = imagen;
		}

		public Museo getMuseo() {
			return museo;
		}

		public void setMuseo(Museo museo) {
			this.museo = museo;
		}

		@Override
		public String toString() {
			return "ImagenMuseo [id=" + id + ", imagen=" + imagen + ", museo=" + museo + "]";
		}
	    
}
