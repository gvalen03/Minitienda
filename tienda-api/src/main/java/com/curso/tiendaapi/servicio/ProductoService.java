package com.curso.tiendaapi.servicio;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.curso.tiendaapi.modelo.Producto;
import com.curso.tiendaapi.repositorio.ProductoRepository;

// @Service: aquí viven las reglas del negocio
@Service
public class ProductoService {

    private final ProductoRepository repositorio;

    public ProductoService(ProductoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Producto> listar() {
        return repositorio.findAll();
    }

    public Producto buscarPorId(Long id) {
        // findById devuelve un Optional: puede traer el producto o venir vacío
        return repositorio.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No existe el producto con id " + id));
    }

    public Producto crear(Producto producto) {
        producto.setId(null); // el id lo asigna la base de datos, nunca el cliente
        return repositorio.save(producto);
    }

    public Producto actualizar(Long id, Producto datos) {
        Producto existente = buscarPorId(id); // si no existe, lanza 404
        existente.setNombre(datos.getNombre());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());
        existente.setStockMinimo(datos.getStockMinimo());
        existente.setImagen(datos.getImagen());
        return repositorio.save(existente);
    }

    public void eliminar(Long id) {
        Producto existente = buscarPorId(id);
        repositorio.delete(existente);
    }

    // La regla del negocio que antes estaba en Angular
    @Transactional
    public Producto comprar(Long id) {
        Producto producto = buscarPorId(id);
        if (producto.getStock() <= 0) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Lo sentimos, ya no hay stock");
        }
        producto.setStock(producto.getStock() - 1);
        return repositorio.save(producto);
    }

    @Transactional
    public Producto reponer(Long id, Integer cantidad) {
        if (cantidad == null || cantidad < 1 || cantidad > 20) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "La cantidad debe estar entre 1 y 20");
        }

        Producto producto = buscarPorId(id);
        if (producto.getStock() + cantidad > 50) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "El stock no puede superar las 50 unidades");
        }

        producto.setStock(producto.getStock() + cantidad);
        return repositorio.save(producto);
    }
}
