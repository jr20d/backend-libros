package com.modvi.libros.dto.common;

import java.util.List;

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
public class RespuestaListDto<T> {
	private String mensaje;
	private int pagina;
	private int totalRegistros;
	private int registrosEncontrados;
	private List<T> Registros;
}