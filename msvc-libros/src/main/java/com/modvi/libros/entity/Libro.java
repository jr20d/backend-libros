package com.modvi.libros.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_libro")
@Getter
@Setter
@NoArgsConstructor
public class Libro {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "libro_id", updatable = false)
	private Long libroId;
	
	@Column(name = "nombre_libro", length = 150)
	private String nombreLibro;
	
	@Column(name = "fecha_publicacion")
	private Date fechaPublicacion;
	
	@Column(name = "cantidad")
	private int cantidad;
	
	@OneToMany(mappedBy = "libro", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE })
	private List<LibroAutor> librosAutores;
	
	public Libro(long libroId) {
	    this.libroId = libroId;
	}
}