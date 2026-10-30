package com.curso.tiendaapi;

import com.curso.tiendaapi.config.DatosIniciales;
import com.curso.tiendaapi.repositorio.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:tiendatest;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class TiendaApiApplicationTests {
    @Autowired ProductoRepository repositorio;
    @Autowired DatosIniciales datos;

    @Test
    void cargaElCatalogoSinDuplicarloAlReiniciarLaCarga() {
        assertThat(repositorio.findAll()).hasSize(4);
        assertThat(repositorio.findAll()).anySatisfy(producto -> {
            assertThat(producto.getNombre()).isEqualTo("Papaya");
            assertThat(producto.getStock()).isZero();
            assertThat(producto.getStockMinimo()).isEqualTo(2);
        });
        datos.run();
        assertThat(repositorio.count()).isEqualTo(4);
    }
}
