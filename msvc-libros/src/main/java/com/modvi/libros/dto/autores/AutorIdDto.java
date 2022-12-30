package com.modvi.libros.dto.autores;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AutorIdDto {
	@NotNull(message = "Autor(es) no seleccionado(s)")
	@Min(value = 1L, message = "El autor seleccionado no es válido")
	private Long autorId;
}