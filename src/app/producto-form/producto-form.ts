import { Component, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Producto } from '../producto';

@Component({
  selector: 'app-producto-form',
  imports: [FormsModule],
  templateUrl: './producto-form.html',
  styleUrl: './producto-form.css'
})
export class ProductoForm {
  // Una señal por cada campo del formulario
  nombre = signal('');
  precio = signal(0);
  stock = signal(0);
  stockMinimo = signal(2);
  imagen = signal('');

  guardar = output<Omit<Producto, 'id'>>();

  enviar() {
    this.guardar.emit({
      nombre: this.nombre(),
      precio: this.precio(),
      stock: this.stock(),
      stockMinimo: this.stockMinimo(),
      imagen: this.imagen()
    });
    // Limpiar el formulario
    this.nombre.set('');
    this.precio.set(0);
    this.stock.set(0);
    this.stockMinimo.set(2);
    this.imagen.set('');
  }
}
