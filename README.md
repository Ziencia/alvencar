# Alvencar

Aplicación para gestionar el alquiler y la venta de coches.

## Descripción

La aplicación se encargará de la gestión de ventas y alquileres de una flota de vehículos. Almacenará datos de todos los vehículos, ofertas y transacciones realizadas, así como datos de los clientes y facturas asociadas a estos clientes.

La aplicación será diseñada para acceder a ella desde entorno web en un ordenador.

## Diagrama de clases de diseño

![Diagrama de diseño MVP](https://git.institutomilitar.com/ManuelDeBlas/alvencar/-/wikis/img/20250303_diagrama_clases_alvencar.png)

### Cumplimiento de requisitos

1. **Herencia**: Se realizará sobre `Transaccion` y sus subtipos.
2. **Relación One-To-Many**: Relación entre `Coche` y `Transaccion`.
3. **Método personalizado**: Generación de la factura.
4. **Listado**: Se mostrarán todas las transacciones asociadas a un `Coche`. ([Figura 1](#figura-1)).
5. **CRUD**: Se realizará CRUD en la creación de las transacciones ([Figura 2](#figura-2)).
6. **URLs** del proyecto:
   1. Repositorio proyecto: <https://git.institutomilitar.com/ManuelDeBlas/alvencar>
   2. Librería: <https://github.com/Ziencia/alvencar-libreria>
7. **Despliegue** en Internet:
   1. API: https://alvencar-ac4e236c0ceb.herokuapp.com/api
   2. Web: https://alvencar.netlify.app/

## Interfaz de usuario

### Figura 1

![Figura 1](https://git.institutomilitar.com/ManuelDeBlas/alvencar/-/wikis/img/20250303_interfaz_listado.png)

### Figura 2

![Figura 2](https://git.institutomilitar.com/ManuelDeBlas/alvencar/-/wikis/img/20250304_interfaz_2.png)
