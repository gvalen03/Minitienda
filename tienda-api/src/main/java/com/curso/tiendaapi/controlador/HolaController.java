package com.curso.tiendaapi.controlador;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class HolaController {
    @GetMapping("/api/hola")
    public String saludar() { return "Hola desde Spring Boot"; }
}
