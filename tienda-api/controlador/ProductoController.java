package com.curso.tiendaapi.controlador;
 
import java.util.List;
 
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
 
import com.curso.tiendaapi.modelo.Producto;
import com.curso.tiendaapi.servicio.ProductoService;
 
import jakarta.validation.Valid;
 
@RestController
@RequestMapping("/api/productos") // prefijo común de todas las rutas de esta clase
public class ProductoController {
 
    private final ProductoService servicio;
 
    public ProductoController(ProductoService servicio) {
        this.servicio = servicio;
    }
 
    // GET /api/productos
    @GetMapping
    public List<Producto> listar() {
        return servicio.listar();
    }
 
    // GET /api/productos/3  -> {id} toma el valor 3
    @GetMapping("/{id}")
    public Producto buscar(@PathVariable Long id) {
        return servicio.buscarPorId(id);
    }
 
    // POST /api/productos  con un JSON en el cuerpo
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@Valid @RequestBody Producto producto) {
        return servicio.crear(producto);
    }
 
    // PUT /api/productos/3  con un JSON en el cuerpo
    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id,
                               @Valid @RequestBody Producto producto) {
        return servicio.actualizar(id, producto);
    }
 
    // DELETE /api/productos/3
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
 
    // POST /api/productos/3/comprar
    @PostMapping("/{id}/comprar")
    public Producto comprar(@PathVariable Long id) {
        return servicio.comprar(id);
    }
}
