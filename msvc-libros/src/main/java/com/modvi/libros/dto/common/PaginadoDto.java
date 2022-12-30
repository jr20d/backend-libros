package com.modvi.libros.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginadoDto {
	private int pagina;
	private int cantidad;
	private String busqueda;
	private String orden;
}