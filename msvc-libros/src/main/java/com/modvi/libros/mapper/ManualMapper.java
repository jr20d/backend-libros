package com.modvi.libros.mapper;

import java.util.Optional;

public interface ManualMapper<E> {
	public <D> E insertDtoToEntity(D dto);
	
	public <D, R> Optional<R> queryToDto(D dto, R dtoR);
}