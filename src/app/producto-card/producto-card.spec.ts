import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductoCard } from './producto-card';

describe('ProductoCard', () => {
  let component: ProductoCard;
  let fixture: ComponentFixture<ProductoCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductoCard],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductoCard);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('producto', {
      id: 1,
      nombre: 'Manzana',
      precio: 20.5,
      stock: 3,
      stockMinimo: 2,
      imagen: '/imagenes/manzana.svg'
    });
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
