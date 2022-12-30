package com.modvi.libros.dto.libros;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LibroDetalleQueryDto {
    private Long libroId;
    private String nombreLibro;
    private Date fechaPublicacion;
    private int cantidad;
    private Long autorId;
    private String nombreAutor;
}