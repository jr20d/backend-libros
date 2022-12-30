package com.modvi.libros.service;

import java.util.List;
import java.util.Optional;

import com.modvi.libros.dto.common.PaginadoDto;
import com.modvi.libros.dto.common.RespuestaCambiosBdDto;

public interface CrudBaseService<E> {
	List<?> GetAll(PaginadoDto paginado);
	int GetAllQuantityRecords(String busqueda);
	Optional<?> GetById(long id);
	RespuestaCambiosBdDto create(E entity);
	RespuestaCambiosBdDto update(E entity);
	RespuestaCambiosBdDto delete(E entity);
}