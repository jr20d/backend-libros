package com.modvi.libros.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.modvi.libros.dto.autores.AutorResultDto;
import com.modvi.libros.entity.Autor;

@Mapper
public interface AutorMapper {
	AutorMapper INSTANCE = Mappers.getMapper(AutorMapper.class);
	
	AutorResultDto autorEntityToDto(Autor autor);
}