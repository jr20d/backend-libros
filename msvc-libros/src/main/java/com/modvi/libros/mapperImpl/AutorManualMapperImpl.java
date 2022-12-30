package com.modvi.libros.mapperImpl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.modvi.libros.dto.autores.AutorOpDto;
import com.modvi.libros.entity.Autor;
import com.modvi.libros.mapper.ManualMapper;

@Service(value = "autorManualMapperImpl")
public class AutorManualMapperImpl implements ManualMapper<Autor> {

	@Override
	public <D> Autor insertDtoToEntity(D dto) {
		var autorDto = (AutorOpDto) dto;
		
		var autor = new Autor();
		autor.setNombreAutor(autorDto.getNombreAutor());
		
		return autor;
	}

    @Override
    public <D, R> Optional<R> queryToDto(D dto, R dtoR) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }
}
