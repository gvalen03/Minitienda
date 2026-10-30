# Mini tienda: Angular + Spring Boot

## Ejecutar Angular

Desde la raíz del proyecto:

```sh
npm install
npm start
```

Abrir http://localhost:4200. Incluye catálogo, tarjetas con ilustraciones SVG locales, carrito, total, control de stock y mensajes de tres segundos. El catálogo se carga desde Spring Boot. Comprar descuenta el stock en H2; crear, reponer cinco unidades y eliminar también se guardan en el servidor. El carrito sigue en memoria y se vacía al recargar, sin devolver el stock, tal como plantea la guía.

## Ejecutar el backend

Requisitos: Java 17 o superior y Maven 3.6.3 o superior. Se usa Spring Boot 4.0.6 y el Maven instalado en el equipo.

En otra terminal:

```sh
cd tienda-api
mvn spring-boot:run
```

Ejecutar siempre desde `tienda-api` para mantener estable la ubicación de la base de datos.

- Saludo de prueba: http://localhost:8080/api/hola
- Consola H2: http://localhost:8080/h2-console
- Driver: `org.h2.Driver`
- JDBC URL: `jdbc:h2:file:./data/tiendadb`
- Usuario: `sa`; contraseña vacía.

En la consola ejecutar `SELECT * FROM PRODUCTOS;` para ver los cuatro productos iniciales. Solo se cargan si la tabla está vacía. Los datos se conservan en `tienda-api/data/`, excluida de Git. La configuración de consola y contraseña vacía corresponde al entorno local de la guía.

## API y segunda parte

Se incorporaron los pasos 1–20 de Angular y 1–24 de las dos partes de Spring Boot. Angular permanece en la raíz y el backend en `tienda-api/`. El paquete Java es `com.curso.tiendaapi`. Las imágenes iniciales son SVG locales; para nuevos productos, indicar una ruta existente como `/imagenes/manzana.svg` o agregar la imagen a `public/imagenes/`.

| Método | Ruta | Resultado |
| --- | --- | --- |
| GET | `/api/productos` | Lista del catálogo |
| GET | `/api/productos/{id}` | Producto o 404 |
| POST | `/api/productos` | Crea, devuelve 201 |
| PUT | `/api/productos/{id}` | Actualiza los datos |
| DELETE | `/api/productos/{id}` | Elimina, devuelve 204 |
| POST | `/api/productos/{id}/comprar` | Descuenta una unidad o devuelve 409 |

Los errores de negocio y validación devuelven `{ "mensaje": "..." }`. Por ejemplo, `/api/productos/99` responde 404 y `No existe el producto con id 99` si no existe ese producto.

CORS permite `http://localhost:4200`; abrir Angular con ese dominio. Reiniciar Spring Boot después de cambios Java. El archivo `tienda-api/pruebas.http` contiene las ocho peticiones de la guía para REST Client; ajustar el id del producto creado antes de actualizarlo o eliminarlo.

Los retos opcionales y la migración del anexo a MySQL no están activados: se mantiene H2 con los datos existentes.

## Verificar

```sh
npm run build
npm test -- --watch=false
cd tienda-api
mvn test
```
