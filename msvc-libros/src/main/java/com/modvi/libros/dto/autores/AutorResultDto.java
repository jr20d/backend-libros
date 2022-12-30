package com.modvi.libros.dto.autores;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AutorResultDto extends AutorIdDto {
	private String nombreAutor;
	
	public AutorResultDto(long autorId, String nombreAutor) {
	    super(autorId);
	    this.nombreAutor = nombreAutor;
	}
}