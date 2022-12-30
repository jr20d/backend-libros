package com.modvi.libros.mapperImpl;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.modvi.libros.dto.autores.AutorResultDto;
import com.modvi.libros.dto.libros.LibroDetalleDto;
import com.modvi.libros.dto.libros.LibroDetalleQueryDto;
import com.modvi.libros.dto.libros.LibroOpDto;
import com.modvi.libros.entity.Autor;
import com.modvi.libros.entity.Libro;
import com.modvi.libros.entity.LibroAutor;
import com.modvi.libros.mapper.ManualMapper;

@Service(value = "libroManualMapperImpl")
public class LibroManualMapperImpl implements ManualMapper<Libro> {

	@Override
	public <D> Libro insertDtoToEntity(D dto) {
		var libroDto = (LibroOpDto) dto;
		
		var autoresLibros = new ArrayList<LibroAutor>();
		
		libroDto.getAutoresIds().forEach(autor ->{
			var libroAutor = new LibroAutor();
			libroAutor.setAutor(new Autor(autor.getAutorId()));
			
			autoresLibros.add(libroAutor);
		});
		
		var libro = new Libro();
		libro.setNombreLibro(libroDto.getNombreLibro());
		libro.setFechaPublicacion(libroDto.getFechaPublicacion());
		libro.setCantidad(libroDto.getCantidad());
		libro.setLibrosAutores(autoresLibros);
		
		return libro;
    }

    @Override
    public <D, R> Optional<R> queryToDto(D dto, R dtoR) {
        var queryResult = (LibroDetalleQueryDto) dto;
        var registro = (LibroDetalleDto) dtoR;
        
        if (queryResult.getLibroId() == null) {
            return Optional.empty();
        }
        
        if (registro.getLibroId() == null) {
            registro.setLibroId(queryResult.getLibroId());
            registro.setNombreLibro(queryResult.getNombreLibro());
            registro.setFechaPublicacion(queryResult.getFechaPublicacion());
            registro.setCantidad(queryResult.getCantidad());
            registro.setAutores(new ArrayList<>());
        }
        
        if (queryResult.getAutorId() != null) {
            var autores = registro.getAutores();
            var autor = new AutorResultDto();
            autor.setAutorId(queryResult.getAutorId());
            autor.setNombreAutor(queryResult.getNombreAutor());
            autores.add(autor);
        }
        return (Optional<R>) Optional.of(registro);
    }
}