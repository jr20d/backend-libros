package com.modvi.libros.controllers;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.modvi.libros.dto.common.PaginadoDto;
import com.modvi.libros.dto.common.RespuestaDto;
import com.modvi.libros.dto.common.RespuestaListDto;
import com.modvi.libros.dto.common.RespuestaOpDto;
import com.modvi.libros.dto.libros.LibroDetalleDto;
import com.modvi.libros.dto.libros.LibroDto;
import com.modvi.libros.dto.libros.LibroOpDto;
import com.modvi.libros.entity.Libro;
import com.modvi.libros.mapper.LibroMapper;
import com.modvi.libros.mapper.ManualMapper;
import com.modvi.libros.service.CrudBaseService;

@RestController
@RequestMapping(path = "/api/libros")
public class LibrosController {
	private CrudBaseService<Libro> libroService;
	private ManualMapper<Libro> libroManualMapper;
	
	public LibrosController(
		@Qualifier("libroService") CrudBaseService<Libro> libroService,
		@Qualifier("libroManualMapperImpl") ManualMapper<Libro> libroManualMapper
	) {
		this.libroService = libroService;
		this.libroManualMapper = libroManualMapper;
	}
	
	@GetMapping(produces = "application/json")
	public ResponseEntity<?> getAllLibros(
			@RequestParam(name = "pagina", required = true) int pagina, 
			@RequestParam(name = "cantidad", required = true) int cantidad,
			@RequestParam(name = "busqueda", required = true) String busqueda,
			@RequestParam(name = "orden", required = true, defaultValue = "asc") String orden) throws InterruptedException, ExecutionException{
		
		PaginadoDto paginado = PaginadoDto.builder()
				.pagina(pagina)
				.cantidad(cantidad)
				.busqueda(busqueda)
				.orden(orden.equalsIgnoreCase("asc") ? "asc" : "desc")
				.build();
		
		CompletableFuture<List<?>> taskLibros = CompletableFuture.supplyAsync(() -> libroService.GetAll(paginado));
		CompletableFuture<Integer> taskCantidadRegistros = CompletableFuture.supplyAsync(() -> libroService.GetAllQuantityRecords(busqueda));
		CompletableFuture.allOf(taskLibros, taskCantidadRegistros).get();
		
		List<LibroDetalleDto> registros = (List<LibroDetalleDto>) taskLibros.get();
		
		var respuesta = new RespuestaListDto<LibroDetalleDto>();
		respuesta.setMensaje(registros.isEmpty() ? "Registros no encontrados" : "Registros encontrados");
		respuesta.setPagina(pagina);
		respuesta.setRegistrosEncontrados(registros.size());
		respuesta.setTotalRegistros((int) taskCantidadRegistros.get());
		respuesta.setRegistros(registros);
		
		return ResponseEntity.ok(respuesta);
	}
	
	@GetMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<?> GetLibro(@PathVariable(name = "id", required = true) long libroId){
		var libro = libroService.GetById(libroId);
		
		if (libro.isEmpty()) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(RespuestaDto.builder().mensaje("El registro no fué encontrado").build());
		}
		
		return ResponseEntity.ok(libro.get());
	}
	
	@PostMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity<?> createLibro(@RequestBody(required = true) @Valid LibroOpDto libroDto,
			HttpServletRequest request){
		
		Libro libro = libroManualMapper.insertDtoToEntity(libroDto);
		
		var resultado = libroService.create(libro);
		
		if (!resultado.getCodigoEstado().equals(HttpStatus.CREATED)) {
			return ResponseEntity.status(resultado.getCodigoEstado()).body(resultado);
		}
		
		var respuesta = new RespuestaOpDto<LibroDto>(LibroMapper.INSTANCE.libroEntityToDTO(libro));
		respuesta.setCodigoEstado(resultado.getCodigoEstado());
		respuesta.setMensaje(resultado.getMensaje());
		
		return ResponseEntity
				.created(URI.create(request.getRequestURL() + "/" + libro.getLibroId()))
				.body(respuesta);
	}
	
	@PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
	public ResponseEntity<?> updateLibro(@RequestBody(required = true) @Valid LibroOpDto libroDto,
			@PathVariable(name = "id", required = true) long libroId){
		Libro libro = libroManualMapper.insertDtoToEntity(libroDto);
		libro.setLibroId(libroId);
		var resultado = libroService.update(libro);
		
		if (!resultado.getCodigoEstado().equals(HttpStatus.OK)) {
			return ResponseEntity.status(resultado.getCodigoEstado()).body(resultado);
		}
		
		var respuesta = new RespuestaOpDto<LibroDto>(LibroMapper.INSTANCE.libroEntityToDTO(libro));
		respuesta.setCodigoEstado(resultado.getCodigoEstado());
		respuesta.setMensaje(resultado.getMensaje());
		
		return ResponseEntity.ok(respuesta);
	}
	
	@DeleteMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<?> deleteLibro(@PathVariable(name = "id", required = true) long libroId){
		
		Libro libro = new Libro();
		libro.setLibroId(libroId);
		
		var resultado = libroService.delete(libro);
		
		return ResponseEntity.status(resultado.getCodigoEstado()).body(resultado);
	}
}