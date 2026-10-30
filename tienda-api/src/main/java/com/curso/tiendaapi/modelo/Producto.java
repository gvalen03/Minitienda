package com.curso.tiendaapi.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

// @Entity: JPA convierte esta clase en una tabla de la base de datos
@Entity
@Table(name = "productos")
public class Producto {

    // @Id: llave primaria. IDENTITY: la base de datos asigna 1, 2, 3...
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar 80 caracteres")
    @Column(nullable = false, length = 80)
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private Double precio;

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;

    @PositiveOrZero(message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo = 2;

    // Ruta de la imagen dentro de la carpeta public/ de Angular
    private String imagen;

    // JPA exige un constructor vacío para poder crear los objetos
    public Producto() {
    }

    // Constructor de conveniencia para cargar los datos iniciales
    public Producto(String nombre, Double precio, Integer stock, String imagen) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.imagen = imagen;
    }

    // Getters y setters: Spring los usa para convertir el objeto a JSON y de vuelta
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}

