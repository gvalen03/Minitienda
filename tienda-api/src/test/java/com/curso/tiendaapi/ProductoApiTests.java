package com.curso.tiendaapi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "spring.datasource.url=jdbc:h2:mem:apitest;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ProductoApiTests {
    @LocalServerPort int puerto;
    final HttpClient cliente = HttpClient.newHttpClient();

    HttpResponse<String> pedir(String metodo, String ruta, String cuerpo) throws Exception {
        var builder = HttpRequest.newBuilder(URI.create("http://localhost:" + puerto + "/api/productos" + ruta))
            .header("Content-Type", "application/json");
        return cliente.send(builder.method(metodo, cuerpo == null ? HttpRequest.BodyPublishers.noBody()
            : HttpRequest.BodyPublishers.ofString(cuerpo)).build(), HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void verificaCrudCompraValidacionErroresYCors() throws Exception {
        assertThat(pedir("GET", "", null).statusCode()).isEqualTo(200);
        assertThat(pedir("GET", "/1", null).body()).contains("Manzana");
        var inexistente = pedir("GET", "/99", null);
        assertThat(inexistente.statusCode()).isEqualTo(404);
        assertThat(inexistente.body()).contains("No existe el producto con id 99");
        var creado = pedir("POST", "", "{\"nombre\":\"Mango\",\"precio\":8.9,\"stock\":1,\"imagen\":\"\"}");
        assertThat(creado.statusCode()).isEqualTo(201);
        String id = creado.body().replaceAll(".*\\\"id\\\":(\\d+).*", "$1");
        assertThat(pedir("PUT", "/" + id, "{\"nombre\":\"Mango tommy\",\"precio\":9.5,\"stock\":1}").statusCode()).isEqualTo(200);
        var compra = pedir("POST", "/" + id + "/comprar", null);
        assertThat(compra.statusCode()).isEqualTo(200);
        assertThat(compra.body()).contains("\"stock\":0");
        var agotado = pedir("POST", "/" + id + "/comprar", null);
        assertThat(agotado.statusCode()).isEqualTo(409);
        assertThat(agotado.body()).contains("Lo sentimos, ya no hay stock");
        assertThat(pedir("POST", "", "{\"nombre\":\"\",\"precio\":-3,\"stock\":1}").statusCode()).isEqualTo(400);
        assertThat(pedir("DELETE", "/" + id, null).statusCode()).isEqualTo(204);
        assertThat(pedir("GET", "/" + id, null).statusCode()).isEqualTo(404);
        var cors = cliente.send(HttpRequest.newBuilder(URI.create("http://localhost:" + puerto + "/api/productos"))
            .header("Origin", "http://localhost:4200").header("Access-Control-Request-Method", "PUT")
            .method("OPTIONS", HttpRequest.BodyPublishers.noBody()).build(), HttpResponse.BodyHandlers.ofString());
        assertThat(cors.statusCode()).isEqualTo(200);
        assertThat(cors.headers().firstValue("Access-Control-Allow-Origin")).contains("http://localhost:4200");
    }

    @Test
    void reponeConLimitesYConservaElStockCuandoSeRechazaLaOperacion() throws Exception {
        var pera = pedir("POST", "/3/reponer/5", null);
        assertThat(pera.statusCode()).isEqualTo(200);
        assertThat(pera.body()).contains("\"stock\":7");
        assertThat(pera.body()).contains("\"stockMinimo\":2");

        var cantidadInvalida = pedir("POST", "/2/reponer/30", null);
        assertThat(cantidadInvalida.statusCode()).isEqualTo(400);
        assertThat(cantidadInvalida.body()).contains("La cantidad debe estar entre 1 y 20");

        assertThat(pedir("POST", "/2/reponer/20", null).statusCode()).isEqualTo(200);
        assertThat(pedir("POST", "/2/reponer/20", null).statusCode()).isEqualTo(200);
        var superaMaximo = pedir("POST", "/2/reponer/20", null);
        assertThat(superaMaximo.statusCode()).isEqualTo(409);
        assertThat(superaMaximo.body()).contains("El stock no puede superar las 50 unidades");
        assertThat(pedir("GET", "/2", null).body()).contains("\"stock\":45");

        var stockMinimoInvalido = pedir(
            "POST", "", "{\"nombre\":\"Uva\",\"precio\":4.5,\"stock\":5,\"stockMinimo\":-1,\"imagen\":\"\"}"
        );
        assertThat(stockMinimoInvalido.statusCode()).isEqualTo(400);
        assertThat(stockMinimoInvalido.body()).contains("El stock mínimo no puede ser negativo");

        var inexistente = pedir("POST", "/99/reponer/1", null);
        assertThat(inexistente.statusCode()).isEqualTo(404);
    }
}
