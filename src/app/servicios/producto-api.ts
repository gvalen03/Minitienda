import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto } from '../producto';

// providedIn: 'root' -> existe una sola instancia para toda la aplicación
@Injectable({ providedIn: 'root' })
export class ProductoApi {
  private http = inject(HttpClient);
  private url = 'http://localhost:8080/api/productos';

  listar(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.url);
  }

  // Omit<Producto, 'id'>: un Producto SIN id, porque lo asigna la base de datos
  crear(producto: Omit<Producto, 'id'>): Observable<Producto> {
    return this.http.post<Producto>(this.url, producto);
  }

  actualizar(producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(`${this.url}/${producto.id}`, producto);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  comprar(id: number): Observable<Producto> {
    return this.http.post<Producto>(`${this.url}/${id}/comprar`, {});
  }

  reponer(id: number, cantidad: number): Observable<Producto> {
    return this.http.post<Producto>(`${this.url}/${id}/reponer/${cantidad}`, {});
  }
}
