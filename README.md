# Alvencar

Aplicación para gestionar el alquiler y venta de coches.

## Descripción

La aplicación se encargará de la gestión de ventas y alquileres de una flota de vehículos. Almacenará datos de todos los vehículos y transacciones realizadas, así como datos de facturación de los clientes y facturas asociadas a estos datos de facturación.

La aplicación será diseñada para acceder a ella desde entorno web en un ordenador.

## Diagrama de clases de diseño

![Diagrama de diseño MVP](https://git.institutomilitar.com/ManuelDeBlas/alvencar/-/wikis/img/20250119_diagrama_clases_alvencar.drawio.png)

**Cumplimiento de requisitos**
1. **Herencia**: Se realizará sobre `Transaccion` y sus subtipos.
2. **Relación One-To-Many**: Relación entre `Transaccion` y `Factura`.
3. **Método personalizado**: Cálculo de los impuestos asociados al precio final.
4. **Listado**: Se mostrarán todas las facturas asociadas a unos datos de facturación concretos ([Figura 1](#figura-1)).
5. **CRUD**: En la misma pantalla anterior se usarán los controles adecuados para hacer el CRUD. Se usará el formulario de la ([Figura 2](#figura-2)).
6. **URLs** del proyecto:
   1. Repositorio proyecto: https://git.institutomilitar.com/ManuelDeBlas/alvencar
   2. Librería: https://github.com/ManuelDeBlas/alvencar
7. **Despliegue** en Internet:
   1. API: 
   2. Web: 

## Interfaz de usuario

### Figura 1: 

![Figura 1](https://git.institutomilitar.com/ManuelDeBlas/alvencar/-/wikis/img/20250119_interfaz_figura1.jpg)

### Figura 2:

![Figura 2](https://git.institutomilitar.com/ManuelDeBlas/alvencar/-/wikis/img/20250119_interfaz_figura2.jpg)

