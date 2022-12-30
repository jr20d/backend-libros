package com.modvi.libros.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.modvi.libros.dto.autores.AutorIdNombreDto;
import com.modvi.libros.dto.libros.LibroDetalleQueryDto;
import com.modvi.libros.dto.libros.LibroDto;
import com.modvi.libros.entity.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    
	@Query("SELECT new com.modvi.libros.dto.libros.LibroDto(l.libroId, l.nombreLibro, l.fechaPublicacion, l.cantidad) "
	        + "FROM Libro l WHERE l.nombreLibro LIKE %:busqueda%")
	List<LibroDto> getAllLibros(@Param("busqueda") String busqueda, Pageable pageable);
	
	@Query("SELECT COUNT(l.libroId) FROM Libro l WHERE l.nombreLibro LIKE %:busqueda%")
	int getAllCantidadLibros(@Param("busqueda") String busqueda);
	
	@Query("SELECT COUNT(l.libroId) FROM Libro l WHERE l.nombreLibro = :nombreLibro")
	int existsLibro(@Param("nombreLibro") String nombreLibro);
	
	@Query("SELECT COUNT(l.libroId) FROM Libro l WHERE l.nombreLibro = :nombreLibro AND "
			+ "l.libroId != :libroId")
	int existsLibro(@Param("libroId") long libroId, @Param("nombreLibro") String nombreLibro);
	
	@Query("SELECT a.autorId as autorId, a.nombreAutor as nombreAutor "
	        + "FROM Libro l LEFT JOIN l.librosAutores la LEFT JOIN la.autor a WHERE l.libroId = :libroId")
	List<AutorIdNombreDto> getAutorIdsByLibro(@Param("libroId") long libroId);
	
	@Query("SELECT new com.modvi.libros.dto.libros.LibroDetalleQueryDto(l.libroId, l.nombreLibro, "
            + "l.fechaPublicacion, l.cantidad, a.autorId, a.nombreAutor) "
            + "FROM Libro l LEFT JOIN l.librosAutores la LEFT JOIN la.autor a WHERE l.libroId = :libroId")
    List<LibroDetalleQueryDto> getAutoresByLibro(@Param("libroId") long libroId);
}