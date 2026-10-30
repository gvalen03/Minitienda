import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { App } from './app';

const producto = {
  id: 1,
  nombre: 'Manzana',
  precio: 20.5,
  stock: 3,
  stockMinimo: 2,
  imagen: '/imagenes/manzana.svg'
};
const url = 'http://localhost:8080/api/productos';

describe('App conectada con la API', () => {
  let http: HttpTestingController;
  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [App], providers: [provideHttpClient(), provideHttpClientTesting()] });
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => http.verify());

  it('carga el catálogo desde el servidor y muestra el título', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    expect(fixture.componentInstance.cargando()).toBe(true);
    http.expectOne(url).flush([producto]);
    fixture.detectChanges();
    expect(fixture.componentInstance.productos()).toEqual([producto]);
    expect(fixture.nativeElement.querySelector('h1').textContent).toContain('Tienda de productos');
  });

  it('solo añade al carrito después de que el servidor confirma la compra', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    app.productos.set([producto]);
    app.agregarAlCarrito(1);
    expect(app.carrito()).toEqual([]);
    const peticion = http.expectOne(url + '/1/comprar');
    expect(peticion.request.method).toBe('POST');
    peticion.flush({ ...producto, stock: 2 });
    expect(app.carrito()).toHaveLength(1);
    expect(app.productos()[0].stock).toBe(2);
    expect(app.total()).toBe(20.5);
    fixture.destroy();
  });

  it('repone con el endpoint específico y actualiza el contador sin recargar', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    const pera = { ...producto, id: 3, nombre: 'Pera', stock: 2 };
    const papaya = { ...producto, id: 4, nombre: 'Papaya', stock: 0 };
    app.productos.set([pera, papaya]);

    expect(app.productosConStockBajo()).toBe(2);
    app.reponerStock({ id: 3, cantidad: 5 });

    const peticion = http.expectOne(url + '/3/reponer/5');
    expect(peticion.request.method).toBe('POST');
    peticion.flush({ ...pera, stock: 7 });

    expect(app.productos()[0].stock).toBe(7);
    expect(app.productosConStockBajo()).toBe(1);
    fixture.destroy();
  });

  it('muestra el mensaje que entrega el servidor al rechazar una reposición', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;

    app.reponerStock({ id: 2, cantidad: 30 });
    http.expectOne(url + '/2/reponer/30').flush(
      { mensaje: 'La cantidad debe estar entre 1 y 20' },
      { status: 400, statusText: 'Bad Request' }
    );

    expect(app.mensaje()).toBe('La cantidad debe estar entre 1 y 20');
    expect(app.esError()).toBe(true);
    fixture.destroy();
  });

  it('muestra el error de stock y no cambia el carrito', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    app.agregarAlCarrito(1);
    http.expectOne(url + '/1/comprar').flush({ mensaje: 'Lo sentimos, ya no hay stock' }, { status: 409, statusText: 'Conflict' });
    expect(app.mensaje()).toBe('Lo sentimos, ya no hay stock');
    expect(app.carrito()).toEqual([]);
    fixture.destroy();
  });

  it('permite recuperar la carga tras una desconexión', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    app.cargarProductos();
    http.expectOne(url).flush(null, { status: 503, statusText: 'Unavailable' });
    expect(app.cargando()).toBe(false);
    expect(app.esError()).toBe(true);
    app.cargarProductos();
    http.expectOne(url).flush([producto]);
    expect(app.productos()).toEqual([producto]);
    fixture.destroy();
  });
});
