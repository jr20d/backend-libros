package com.modvi.libros.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.modvi.libros.dto.libros.LibroDto;
import com.modvi.libros.entity.Libro;

@Mapper
public interface LibroMapper {
	LibroMapper INSTANCE = Mappers.getMapper(LibroMapper.class);
	
	LibroDto libroEntityToDTO(Libro source);
}