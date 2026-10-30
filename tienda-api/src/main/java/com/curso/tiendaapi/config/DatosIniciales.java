package com.curso.tiendaapi.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.curso.tiendaapi.modelo.Producto;
import com.curso.tiendaapi.repositorio.ProductoRepository;

// CommandLineRunner: el método run() se ejecuta una vez, al arrancar el servidor
@Component
public class DatosIniciales implements CommandLineRunner {

    private final ProductoRepository repositorio;

    // Inyección por constructor: Spring entrega el repositorio ya creado
    public DatosIniciales(ProductoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void run(String... args) {
        if (repositorio.count() == 0) {
            repositorio.saveAll(List.of(
                new Producto("Manzana", 20.5, 3, "/imagenes/manzana.svg"),
                new Producto("Piña", 15.36, 5, "/imagenes/pina.svg"),
                new Producto("Pera", 5.45, 2, "/imagenes/pera.svg"),
                new Producto("Papaya", 6.15, 0, "/imagenes/papaya.svg")
            ));
            System.out.println(">> Se cargaron los productos iniciales");
        }

        // Al añadir stockMinimo a una base H2 que ya existe, JPA crea la columna
        // pero sus filas antiguas pueden quedar en null. Se conserva la información
        // y se completa solamente el nuevo valor predeterminado.
        repositorio.findAll().forEach(producto -> {
            if (producto.getStockMinimo() == null) {
                producto.setStockMinimo(2);
                repositorio.save(producto);
            }
        });
    }
}

