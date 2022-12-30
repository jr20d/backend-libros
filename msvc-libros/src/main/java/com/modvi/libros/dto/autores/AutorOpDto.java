package com.modvi.libros.dto.autores;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutorOpDto {
	@NotNull(message = "No ingresó el nombre del autor")
	@NotBlank(message = "Asegurese de ingresar el nombre del autor")
	@Length(min = 3, max = 150, message = "El nombre del autor debe contener entre 3 a 150 caracteres")
	private String nombreAutor;
}