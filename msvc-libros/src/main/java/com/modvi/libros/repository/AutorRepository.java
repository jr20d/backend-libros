package com.modvi.libros.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.modvi.libros.dto.autores.AutorIdNombreDto;
import com.modvi.libros.dto.autores.AutorListDto;
import com.modvi.libros.entity.Autor;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
	
	@Query("SELECT a.autorId as autorId, a.nombreAutor as nombreAutor, COUNT(la.libroAutorId) as cantidadLibros FROM Autor a "
			+ "LEFT JOIN a.librosAutores la "
			+ "WHERE a.nombreAutor LIKE %:busqueda% GROUP BY a.autorId")
	List<AutorListDto> getAllAutores(@Param("busqueda") String busqueda, Pageable pageable);
	
	@Query("SELECT COUNT(a.autorId) FROM Autor a WHERE a.nombreAutor LIKE %:busqueda%")
	int getAllCantidadAutores(@Param("busqueda") String busqueda);
	
	@Query("SELECT COUNT(a.autorId) FROM Autor a WHERE a.nombreAutor = :nombreAutor")
	int existsAutor(@Param("nombreAutor") String nombreAutor);
	
	@Query("SELECT COUNT(a.autorId) FROM Autor a WHERE a.nombreAutor = :nombreAutor AND "
			+ "a.autorId != :autorId")
	int existsAutor(@Param("autorId") long autorId, @Param("nombreAutor") String nombreAutor);
	
	@Query("SELECT a.autorId as autorId, a.nombreAutor as nombreAutor FROM Autor a WHERE a.autorId IN(:ids)")
	List<AutorIdNombreDto> getAutoresByIds(@Param("ids") List<Long> ids);
}