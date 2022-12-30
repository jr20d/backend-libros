package com.modvi.libros.mapper;

import com.modvi.libros.dto.libros.LibroDto;
import com.modvi.libros.entity.Libro;
import java.util.Date;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-07T01:28:57-0600",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.3.1 (Oracle Corporation)"
)
public class LibroMapperImpl implements LibroMapper {

    @Override
    public LibroDto libroEntityToDTO(Libro source) {
        if ( source == null ) {
            return null;
        }

        long libroId = 0L;
        String nombreLibro = null;
        Date fechaPublicacion = null;
        int cantidad = 0;

        if ( source.getLibroId() != null ) {
            libroId = source.getLibroId();
        }
        nombreLibro = source.getNombreLibro();
        fechaPublicacion = source.getFechaPublicacion();
        cantidad = source.getCantidad();

        LibroDto libroDto = new LibroDto( libroId, nombreLibro, fechaPublicacion, cantidad );

        return libroDto;
    }
}
