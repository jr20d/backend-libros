package com.modvi.libros.dto.libros;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.modvi.libros.dto.autores.AutorIdDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LibroOpDto {
	@NotNull(message = "Ingresar el título del libro")
	@NotEmpty(message = "No se ingresó el título del libro")
	private String nombreLibro;
	@NotNull(message = "Ingresar la fecha de publicación")
	private Date fechaPublicacion;
	@Min(value = 1, message = "La cantidad ingresada no es válida")
	private int cantidad;
	@Valid
	@NotNull(message = "Ningún autor(es) seleccionado(s)")
	private List<AutorIdDto> autoresIds;
}