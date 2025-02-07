# Alvencar

Aplicación para gestionar el alquiler y la venta de coches.

## Descripción

La aplicación se encargará de la gestión de ventas y alquileres de una flota de vehículos. Almacenará datos de todos los vehículos y transacciones realizadas, así como datos de facturación de los clientes y facturas asociadas a estos datos de facturación.

La aplicación será diseñada para acceder a ella desde entorno web en un ordenador.

## Diagrama de clases de diseño

![Diagrama de diseño MVP](https://git.institutomilitar.com/ManuelDeBlas/alvencar/-/wikis/img/20250207_diagrama_clases_alvencar.png)

### Cumplimiento de requisitos

1. **Herencia**: Se realizará sobre `Transaccion` y sus subtipos.
2. **Relación One-To-Many**: Relación entre `Coche` y `Transaccion`.
3. **Método personalizado**: Generación de la factura.
4. **Listado**: Se mostrarán todos los alquileres asociados a un `Coche`. ([Figura 1](#figura-1)).
5. **CRUD**: Se realizará CRUD tanto en la creación de un alquiler ([Figura 2](#figura-2)), en la introducción de los datos tras la devolución ([Figura 3](#figura-3)) y en la realización de una venta ([Figura 3](#figura-3)).
6. **URLs** del proyecto:
   1. Repositorio proyecto: <https://git.institutomilitar.com/ManuelDeBlas/alvencar>
   2. Librería: <https://github.com/ManuelDeBlas/alvencar>
7. **Despliegue** en Internet:
   1. API: 
   2. Web: 

## Interfaz de usuario

### Figura 1

![Figura 1](https://git.institutomilitar.com/ManuelDeBlas/alvencar/-/wikis/img/20250207_interfaz_listado.jpg)

### Figura 2

![Figura 2](https://git.institutomilitar.com/ManuelDeBlas/alvencar/-/wikis/img/20250702_interfaz_CRUD_alquiler1.jpg)

### Figura 3

![Figura 3](https://git.institutomilitar.com/ManuelDeBlas/alvencar/-/wikis/img/20250702_interfaz_CRUD_alquiler2.jpg)

### Figura 4

![Figura 4](https://git.institutomilitar.com/ManuelDeBlas/alvencar/-/wikis/img/20250702_interfaz_CRUD_venta.jpg)
