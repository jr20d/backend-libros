package com.modvi.libros.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modvi.libros.dto.autores.AutorListDto;
import com.modvi.libros.dto.common.PaginadoDto;
import com.modvi.libros.dto.common.RespuestaCambiosBdDto;
import com.modvi.libros.entity.Autor;
import com.modvi.libros.repository.AutorRepository;
import com.modvi.libros.service.CrudBaseService;

@Service("autorService")
public class AutorServiceImpl implements CrudBaseService<Autor> {
	
	private AutorRepository autorRepository;
	
	public AutorServiceImpl(AutorRepository autorRepository) {
		this.autorRepository = autorRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AutorListDto> GetAll(PaginadoDto paginado) {
		return autorRepository.getAllAutores(paginado.getBusqueda(),
				PageRequest.of(paginado.getPagina() - 1, paginado.getCantidad(),
						Sort.by(paginado.getOrden().equalsIgnoreCase("desc") ? 
								Direction.DESC : Direction.ASC, "nombreAutor")));
	}

	@Override
	@Transactional(readOnly = true)
	public int GetAllQuantityRecords(String busqueda) {
		return autorRepository.getAllCantidadAutores(busqueda);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<?> GetById(long id) {
		return autorRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = false)
	public RespuestaCambiosBdDto create(Autor entity) {
	    
	    entity.setNombreAutor(entity.getNombreAutor().trim());
	    
		var respuesta = RespuestaCambiosBdDto.builder();
		
		if (autorRepository.existsAutor(entity.getNombreAutor()) > 0) {
			return respuesta.
					codigoEstado(HttpStatus.CONFLICT)
					.mensaje("Ya existe un autor con el nombre " + entity.getNombreAutor())
					.build();
		}
		
		autorRepository.save(entity);
		
		return respuesta.
				codigoEstado(HttpStatus.CREATED)
				.mensaje("El autor ha sido creado con el nombre " + entity.getNombreAutor())
				.build();
	}

	@Override
	@Transactional(readOnly = false)
	public RespuestaCambiosBdDto update(Autor entity) {
	    
	    entity.setNombreAutor(entity.getNombreAutor().trim());
	    
		var respuesta = RespuestaCambiosBdDto.builder();
		
		if (!autorRepository.existsById(entity.getAutorId())) {
			return respuesta
					.codigoEstado(HttpStatus.NOT_FOUND)
					.mensaje("El autor no existe")
					.build();
		}
		
		if (autorRepository.existsAutor(entity.getAutorId(), entity.getNombreAutor()) > 0) {
			return respuesta.
					codigoEstado(HttpStatus.CONFLICT)
					.mensaje("Ya existe un autor con el nombre " + entity.getNombreAutor())
					.build();
		}
		
		autorRepository.save(entity);
		
		return respuesta.
				codigoEstado(HttpStatus.OK)
				.mensaje("Los datos del autor " + entity.getNombreAutor() + " han sido actualizados")
				.build();
	}

	@Override
	@Transactional(readOnly = false)
	public RespuestaCambiosBdDto delete(Autor entity) {
		var respuesta = RespuestaCambiosBdDto.builder();
		
		if (!autorRepository.existsById(entity.getAutorId())) {
			return respuesta
					.codigoEstado(HttpStatus.NOT_FOUND)
					.mensaje("El autor no existe")
					.build();
		}
		
		autorRepository.delete(autorRepository.findById(entity.getAutorId()).get());
		
		return respuesta.
				codigoEstado(HttpStatus.OK)
				.mensaje("El autor ha sido eliminado")
				.build();
	}
}