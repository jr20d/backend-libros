package com.modvi.libros.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_libro_autor")
@Getter
@Setter
@NoArgsConstructor
public class LibroAutor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "libro_autor_id")
	private Long libroAutorId;
	
	@ManyToOne
	@JoinColumn(name = "libro_id", referencedColumnName = "libro_id")
	private Libro libro;
	
	@ManyToOne
	@JoinColumn(name = "autor_id", referencedColumnName = "autor_id")
	private Autor autor;

    public LibroAutor(Libro libro, Autor autor) {
        super();
        this.libro = libro;
        this.autor = autor;
    }
	
	
}