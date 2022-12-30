package com.modvi.libros.mapper;

import com.modvi.libros.dto.autores.AutorResultDto;
import com.modvi.libros.entity.Autor;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-10-07T01:28:57-0600",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.3.1 (Oracle Corporation)"
)
public class AutorMapperImpl implements AutorMapper {

    @Override
    public AutorResultDto autorEntityToDto(Autor autor) {
        if ( autor == null ) {
            return null;
        }

        AutorResultDto autorResultDto = new AutorResultDto();

        autorResultDto.setAutorId( autor.getAutorId() );
        autorResultDto.setNombreAutor( autor.getNombreAutor() );

        return autorResultDto;
    }
}
