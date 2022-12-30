package com.modvi.libros.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaOpDto<T> extends RespuestaCambiosBdDto {
	private T registro;
}