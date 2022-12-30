package com.modvi.libros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.modvi.libros.entity.LibroAutor;

@Repository
public interface LibroAutorRepository extends JpaRepository<LibroAutor, Long> {
    
    @Query("SELECT la.libroAutorId FROM LibroAutor la WHERE la.autor.autorId IN(:ids) and la.libro.libroId = :libroId")
    List<Long> getLibroAutorIds(@Param("ids") List<Long> ids, @Param("libroId") long libroId);
}
