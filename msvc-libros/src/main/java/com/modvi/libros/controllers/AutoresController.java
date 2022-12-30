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

import com.modvi.libros.dto.autores.AutorListDto;
import com.modvi.libros.dto.autores.AutorOpDto;
import com.modvi.libros.dto.autores.AutorResultDto;
import com.modvi.libros.dto.common.PaginadoDto;
import com.modvi.libros.dto.common.RespuestaDto;
import com.modvi.libros.dto.common.RespuestaListDto;
import com.modvi.libros.dto.common.RespuestaOpDto;
import com.modvi.libros.entity.Autor;
import com.modvi.libros.mapper.AutorMapper;
import com.modvi.libros.mapper.ManualMapper;
import com.modvi.libros.service.CrudBaseService;

@RestController
@RequestMapping(path = "/api/autores")
public class AutoresController {
	private CrudBaseService<Autor> autorService;
	private ManualMapper<Autor> autorManualMapper;
	
	public AutoresController(
			@Qualifier("autorService") CrudBaseService<Autor> autorService,
			@Qualifier("autorManualMapperImpl") ManualMapper<Autor> autoManualMapper
	) {
		this.autorService = autorService;
		this.autorManualMapper = autoManualMapper;
	}
	
	@GetMapping(produces = "application/json")
	public ResponseEntity<?> getAllLibros(
			@RequestParam(name = "pagina", required = true) int pagina, 
			@RequestParam(name = "cantidad", required = true) int cantidad,
			@RequestParam(name = "busqueda", required = true) String busqueda,
			@RequestParam(name = "orden", required = true, defaultValue = "asc") String orden) throws InterruptedException, ExecutionException
	{
		PaginadoDto paginado = PaginadoDto.builder()
				.pagina(pagina)
				.cantidad(cantidad)
				.busqueda(busqueda)
				.orden(orden.equalsIgnoreCase("asc") ? "asc" : "desc")
				.build();
		
		CompletableFuture<List<?>> taskAutores= CompletableFuture.supplyAsync(() -> autorService.GetAll(paginado));
		CompletableFuture<Integer> taskCantidadRegistros = CompletableFuture.supplyAsync(() -> autorService.GetAllQuantityRecords(busqueda));
		CompletableFuture.allOf(taskAutores, taskCantidadRegistros).get();
		
		var registros = taskAutores.get();
		
		var respuesta = new RespuestaListDto<AutorListDto>();
		respuesta.setMensaje(registros.isEmpty() ? "Registros no encontrados" : "Registros encontrados");
		respuesta.setPagina(pagina);
		respuesta.setRegistrosEncontrados(registros.size());
		respuesta.setTotalRegistros((int) taskCantidadRegistros.get());
		respuesta.setRegistros((List<AutorListDto>) registros);
		
		return ResponseEntity.ok(respuesta);
	}
	
	@GetMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<?> GetAutor(@PathVariable(name = "id", required = true) long autorId){
		var autor = autorService.GetById(autorId);
		
		if (autor.isEmpty()) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body(RespuestaDto.builder().mensaje("El registro no fué encontrado").build());
		}
		
		return ResponseEntity.ok(AutorMapper.INSTANCE.autorEntityToDto((Autor) autor.get()));
	}
	
	@PostMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity<?> createAutor(@RequestBody(required = true) @Valid AutorOpDto autorDto,
			HttpServletRequest request){
		var autor = autorManualMapper.insertDtoToEntity(autorDto);
		
		var resultado = autorService.create(autor);
		
		if (!resultado.getCodigoEstado().equals(HttpStatus.CREATED)) {
			return ResponseEntity.status(resultado.getCodigoEstado()).body(resultado);
		}
		
		var respuesta = new RespuestaOpDto<AutorResultDto>(AutorMapper.INSTANCE.autorEntityToDto(autor));
		respuesta.setCodigoEstado(resultado.getCodigoEstado());
		respuesta.setMensaje(resultado.getMensaje());
		
		return ResponseEntity
				.created(URI.create(request.getRequestURL() + "/" + autor.getAutorId()))
				.body(respuesta);
	}
	
	@PutMapping(path = "/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> updateAutor(@RequestBody(required = true) @Valid AutorOpDto autorDto,
            @PathVariable(name = "id", required = true) long autorId){
        var autor = autorManualMapper.insertDtoToEntity(autorDto);
        autor.setAutorId(autorId);
        
        var resultado = autorService.update(autor);
        
        if (!resultado.getCodigoEstado().equals(HttpStatus.OK)) {
            return ResponseEntity.status(resultado.getCodigoEstado()).body(resultado);
        }
        
        var respuesta = new RespuestaOpDto<AutorResultDto>(AutorMapper.INSTANCE.autorEntityToDto(autor));
        respuesta.setCodigoEstado(resultado.getCodigoEstado());
        respuesta.setMensaje(resultado.getMensaje());
        
        return ResponseEntity.ok(respuesta);
    }
	
	@DeleteMapping(path = "/{id}", produces = "application/json")
	public ResponseEntity<?> deleteAutor(@PathVariable(name = "id", required = true) long autorId){
	    var autor = new Autor();
	    autor.setAutorId(autorId);
	    
	    var resultado = autorService.delete(autor);
	    
	    return ResponseEntity.status(resultado.getCodigoEstado()).body(resultado);
	}
}