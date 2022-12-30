package com.modvi.libros.dto.libros;

import java.util.Date;
import java.util.List;

import com.modvi.libros.dto.autores.AutorResultDto;

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
public class LibroDetalleDto {
	private Long libroId;
	private String nombreLibro;
	private Date fechaPublicacion;
	private int cantidad;
	
	private List<AutorResultDto> autores;
}