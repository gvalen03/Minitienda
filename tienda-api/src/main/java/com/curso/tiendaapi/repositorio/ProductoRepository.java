package com.curso.tiendaapi.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.curso.tiendaapi.modelo.Producto;

// JpaRepository<Producto, Long>: repositorio de Productos cuya llave es Long.
// Con solo esta línea ya existen findAll(), findById(), save(), delete(), count()...
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}

