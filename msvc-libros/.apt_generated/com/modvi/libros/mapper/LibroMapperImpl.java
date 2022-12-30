package com.modvi.libros.mapper;

import com.modvi.libros.dto.libros.LibroDto;
import com.modvi.libros.entity.Libro;
import java.util.Date;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-29T20:27:31-0600",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 1.4.100.v20220318-0906, environment: Java 17.0.3.1 (Oracle Corporation)"
)
public class LibroMapperImpl implements LibroMapper {

    @Override
    public LibroDto libroEntityToDTO(Libro source) {
        if ( source == null ) {
            return null;
        }

        int cantidad = 0;
        Date fechaPublicacion = null;
        long libroId = 0L;
        String nombreLibro = null;

        cantidad = source.getCantidad();
        fechaPublicacion = source.getFechaPublicacion();
        if ( source.getLibroId() != null ) {
            libroId = source.getLibroId();
        }
        nombreLibro = source.getNombreLibro();

        LibroDto libroDto = new LibroDto( libroId, nombreLibro, fechaPublicacion, cantidad );

        return libroDto;
    }
}
