import { Component, input, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Producto } from '../producto';

@Component({
  selector: 'app-producto-card',
  imports: [FormsModule],
  templateUrl: './producto-card.html',
  styleUrl: './producto-card.css'
})
export class ProductoCard {
  producto = input.required<Producto>();
  comprar = output<number>();
  reponer = output<{ id: number; cantidad: number }>();
  eliminar = output<number>();    // NUEVO
  cantidadReposicion = signal(5);

  alHacerClic() {
    this.comprar.emit(this.producto().id);
  }

  solicitarReposicion() {
    this.reponer.emit({
      id: this.producto().id,
      cantidad: this.cantidadReposicion()
    });
  }
}
