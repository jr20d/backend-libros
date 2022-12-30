package com.modvi.libros.entity;

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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_autor")
@Getter
@Setter
@NoArgsConstructor
public class Autor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "autor_id", updatable = false)
	private Long autorId;
	
	@Column(name = "nombre_autor", length = 150)
	private String nombreAutor;
	
	@OneToMany(mappedBy = "autor", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE })
	private List<LibroAutor> librosAutores;
	
	public Autor(long autorId) {
		this.autorId = autorId;
	}
	public Autor(long autorId, String nombreAutor) {
        this.autorId = autorId;
        this.nombreAutor = nombreAutor;
    }
}