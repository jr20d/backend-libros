package com.modvi.libros.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modvi.libros.dto.autores.AutorIdNombreDto;
import com.modvi.libros.dto.common.PaginadoDto;
import com.modvi.libros.dto.common.RespuestaCambiosBdDto;
import com.modvi.libros.dto.libros.LibroDetalleDto;
import com.modvi.libros.dto.libros.LibroDto;
import com.modvi.libros.entity.Autor;
import com.modvi.libros.entity.Libro;
import com.modvi.libros.entity.LibroAutor;
import com.modvi.libros.mapper.ManualMapper;
import com.modvi.libros.repository.AutorRepository;
import com.modvi.libros.repository.LibroAutorRepository;
import com.modvi.libros.repository.LibroRepository;
import com.modvi.libros.service.CrudBaseService;

@Service(value = "libroService")
public class LibroServiceImpl implements CrudBaseService<Libro> {
	
	private LibroRepository libroRepository;
	private AutorRepository autorRepository;
	private LibroAutorRepository libroAutorRepository;
	private ManualMapper<Libro> manualMapper;
	
	public LibroServiceImpl(LibroRepository libroRepository, AutorRepository autorRepository,
	        LibroAutorRepository libroAutorRepository, @Qualifier("libroManualMapperImpl") ManualMapper<Libro> manualMapper) {
		this.libroRepository = libroRepository;
		this.autorRepository = autorRepository;
		this.libroAutorRepository = libroAutorRepository;
		this.manualMapper = manualMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<LibroDto> GetAll(PaginadoDto paginado) {
		return libroRepository.getAllLibros(paginado.getBusqueda(),
				PageRequest.of(paginado.getPagina() - 1, paginado.getCantidad(),
						Sort.by(paginado.getOrden().equalsIgnoreCase("desc") ? 
								Direction.DESC : Direction.ASC, "nombreLibro")));
	}
	
	@Override
	@Transactional(readOnly = true)
	public int GetAllQuantityRecords(String busqueda) {
		return libroRepository.getAllCantidadLibros(busqueda);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<LibroDetalleDto> GetById(long id) {
	    var resultado = libroRepository.getAutoresByLibro(id);
	    
	    if (resultado.isEmpty()) {
	        return Optional.empty();
	    }
	    
	    var registro = new LibroDetalleDto();
	    
	    resultado.forEach(r -> manualMapper.queryToDto(r, registro));
	    
		return Optional.of(registro);
	}

	@Override
	@Transactional(readOnly = false)
	public RespuestaCambiosBdDto create(Libro entity) {
	    
	    entity.setNombreLibro(entity.getNombreLibro().trim());
	    
	    var respuesta = new RespuestaCambiosBdDto();
		
		if (libroRepository.existsLibro(entity.getNombreLibro()) > 0) {
			respuesta.setCodigoEstado(HttpStatus.CONFLICT);
			respuesta.setMensaje("Ya existe un libro con el nombre " + entity.getNombreLibro());
			return respuesta;
		}
		
		var libro = new Libro();
		libro.setFechaPublicacion(entity.getFechaPublicacion());
		libro.setCantidad(entity.getCantidad());
		libro.setNombreLibro(entity.getNombreLibro());
		
		List<AutorIdNombreDto> autoresEncontrados = new ArrayList<>();
		
		validacionAutores(entity, respuesta, autoresEncontrados, false);
		
		if (respuesta.getCodigoEstado() != null) {
		    return respuesta;
		}
		
		libroRepository.save(libro);
		
		entity.getLibrosAutores().forEach(la -> {
		    la.setLibro(libro);
		    libroAutorRepository.save(la);
		});
		
		respuesta.setCodigoEstado(HttpStatus.CREATED);
		respuesta.setMensaje("El libro ha sido creado con el titulo de " + libro.getNombreLibro());
		return respuesta;
	}

	@Override
	@Transactional(readOnly = false)
	public RespuestaCambiosBdDto update(Libro entity) {
	    
	    entity.setNombreLibro(entity.getNombreLibro().trim());
	    
		var respuesta = new RespuestaCambiosBdDto();
		
		if (!libroRepository.existsById(entity.getLibroId())) {
			respuesta.setCodigoEstado(HttpStatus.NOT_FOUND);
			respuesta.setMensaje("El libro no existe");
			return respuesta;
		}
		
		if (libroRepository.existsLibro(entity.getLibroId(), entity.getNombreLibro()) > 0) {
			respuesta.setCodigoEstado(HttpStatus.CONFLICT);
			respuesta.setMensaje("Ya existe otro libro con el nombre " + entity.getNombreLibro());
			return respuesta;
		}
		
		List<AutorIdNombreDto> autoresEncontrados = new ArrayList<>();
        
        validacionAutores(entity, respuesta, autoresEncontrados, true);
        
        if (respuesta.getCodigoEstado() != null) {
            return respuesta;
        }
        
        var libro = new Libro();
        libro.setLibroId(entity.getLibroId());
        libro.setFechaPublicacion(entity.getFechaPublicacion());
        libro.setCantidad(entity.getCantidad());
        libro.setNombreLibro(entity.getNombreLibro());
		
		libroRepository.save(libro);
		
		respuesta.setCodigoEstado(HttpStatus.OK);
		respuesta.setMensaje("Los datos del libro " + libro.getNombreLibro() + " han sido actualizados");
		return respuesta;
	}

	@Override
	@Transactional(readOnly = false)
	public RespuestaCambiosBdDto delete(Libro entity) {
		var respuesta = RespuestaCambiosBdDto.builder();
		
		if (!libroRepository.existsById(entity.getLibroId())) {
			return respuesta
					.codigoEstado(HttpStatus.NOT_FOUND)
					.mensaje("El libro no existe")
					.build();
		}
		
		libroRepository.delete(libroRepository.findById(entity.getLibroId()).get());
		
		return respuesta.
				codigoEstado(HttpStatus.OK)
				.mensaje("El libro ha sido eliminado")
				.build();
	}
	
	private void validacionAutores(Libro entity, RespuestaCambiosBdDto respuesta, List<AutorIdNombreDto> autoresEncontrados, 
	        boolean isUpdate) {
	    var ids = entity.getLibrosAutores()
                .stream()
                .map(a -> a.getAutor().getAutorId()).toList();
	    
	    autoresEncontrados = autorRepository.getAutoresByIds(ids);
	            
        String errores = "";
        
        List<Long> agregarAutoresIds = new ArrayList<>();
        
        for(var la : entity.getLibrosAutores())
        {
            if (!agregarAutoresIds.contains(la.getAutor().getAutorId())) {
                agregarAutoresIds.add(la.getAutor().getAutorId());
            }
            else {
                errores = "Ha ingresado autores duplicados";
                break;
            }
        }
        
        if (!errores.equals("")) {
            respuesta.setCodigoEstado(HttpStatus.CONFLICT);
            respuesta.setMensaje(errores);
        }
        
        if (autoresEncontrados.isEmpty() || autoresEncontrados.size() != entity.getLibrosAutores().size()) {
            respuesta.setCodigoEstado(HttpStatus.CONFLICT);
            respuesta.setMensaje("Ha ingresado autores no válidos");
        }
        if (isUpdate) {
            List<Autor> nuevosAutores = new ArrayList<>();
            List<Autor> eliminarAutores = new ArrayList<>();
            
            List<AutorIdNombreDto> autoresActuales = libroRepository.getAutorIdsByLibro(entity.getLibroId());
            
            autoresEncontrados.forEach(ae -> {
                if (autoresActuales.isEmpty() || autoresActuales.stream().filter(a -> a.getAutorId() == ae.getAutorId()).findAny().isEmpty()) {
                    nuevosAutores.add(new Autor(ae.getAutorId()));
                }
            });
            
            for (var aa : autoresActuales) {
                if (aa.getAutorId() != null && autoresEncontrados.stream().filter(a -> a.getAutorId() == aa.getAutorId()).findAny().isEmpty()) {
                    eliminarAutores.add(new Autor(aa.getAutorId()));
                }
            }
            
            CompletableFuture<List<Long>> taskEliminar = CompletableFuture
                    .supplyAsync(() -> libroAutorRepository.getLibroAutorIds(eliminarAutores.
                            stream().map(a -> a.getAutorId()).toList(), entity.getLibroId()));
            
            try {
                CompletableFuture.allOf(taskEliminar).get();
                libroAutorRepository.deleteAllById(taskEliminar.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
                respuesta.setCodigoEstado(HttpStatus.CONFLICT);
                respuesta.setMensaje("No se pudo realizar la operación");
            } catch (ExecutionException e) {
                e.printStackTrace();
                respuesta.setCodigoEstado(HttpStatus.CONFLICT);
                respuesta.setMensaje("No se pudo realizar la operación");
            }
            
            List<LibroAutor> librosAutores = new ArrayList<>();
            
            nuevosAutores.forEach(a ->{
                librosAutores.add(new LibroAutor(new Libro(entity.getLibroId()), new Autor(a.getAutorId())));
            });
            
            libroAutorRepository.saveAll(librosAutores);
        }
	}
}
