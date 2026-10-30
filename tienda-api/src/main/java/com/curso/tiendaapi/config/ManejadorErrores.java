package com.curso.tiendaapi.config;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

// @RestControllerAdvice: atiende los errores de TODOS los controladores
@RestControllerAdvice
public class ManejadorErrores {

    // Errores lanzados por el servicio: 404 no existe, 409 sin stock
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> manejarEstado(
            ResponseStatusException ex) {
        String mensaje = ex.getReason() != null
            ? ex.getReason()
            : "Error en la petición";
        return ResponseEntity.status(ex.getStatusCode())
            .body(Map.of("mensaje", mensaje));
    }

    // Errores de @Valid: junta los mensajes de todos los campos que fallaron
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidacion(
            MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getDefaultMessage())
            .collect(Collectors.joining(". "));
        return ResponseEntity.badRequest().body(Map.of("mensaje", mensaje));
    }
}
