package com.modvi.libros.dto.libros;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibroDto {
	private long libroId;
	private String nombreLibro;
	private Date fechaPublicacion;
	private int cantidad;
	
    public LibroDto(long libroId, String nombreLibro, Date fechaPublicacion, int cantidad) {
        super();
        this.libroId = libroId;
        this.nombreLibro = nombreLibro;
        this.fechaPublicacion = fechaPublicacion;
        this.cantidad = cantidad;
    }
}