import axios from 'axios';
import { defineStore } from 'pinia';
import { getTransacciones, postFactura, deleteTransaccionPorId } from './api-service';

export const useTransaccionStore = defineStore('transaccion', {
  state() {
    return {
      ventas: [],
      alquileres: [],
      cargando: false,
      error: null
    };
  },
  actions: {
    async cargarTransacciones() {
      this.cargando = true;
      this.error = null;
      try {
        const res = await getTransacciones();
        this.ventas = res.data._embedded?.ventas || [];
        this.alquileres = res.data._embedded?.alquileres || [];

        this.ventas = await Promise.all(
          this.ventas.map(async (venta) => {
            const ventaCompleta = { ...venta };
            try {
              const vehiculoRes = await axios.get(venta._links.vehiculo.href);
              ventaCompleta.vehiculo = vehiculoRes.data;
            } catch (e) {
              ventaCompleta.vehiculo = null;
            }
            try {
              const clienteRes = await axios.get(limpiarHref(venta._links.cliente.href));
              //await axios.get(venta._links.cliente.href);
              ventaCompleta.cliente = clienteRes.data;
            } catch (e) {
              ventaCompleta.cliente = null;
            }
            return ventaCompleta;
          }));


        this.alquileres = await Promise.all(
          this.alquileres.map(async (alquiler) => {
            const alquilerCompleto = { ...alquiler };
            try {
              const vehiculoRes = await axios.get(alquiler._links.vehiculo.href);
              alquilerCompleto.vehiculo = vehiculoRes.data;
            } catch (e) {
              alquilerCompleto.vehiculo = null;
            }
            try {
              const clienteRes = await axios.get(limpiarHref(alquiler._links.cliente.href));
              alquilerCompleto.cliente = clienteRes.data;
            } catch (e) {
              alquilerCompleto.cliente = null;
            }
            return alquilerCompleto;
          }));

      } catch (e) {
        this.error = 'Error al cargar transacciones';
      } finally {
        this.cargando = false;
      }
    },
    async cargarVehiculoDeTransaccion(transaccion) {
      try {
        const res = await axios.get(transaccion._links.vehiculo.href);
        return res.data;
      } catch (error) {
        return null;
      }
    },
    async crearFactura(factura) {
      try {
        const res = await postFactura(factura);
      } catch (e) {
        this.error = 'No se pudo crear la factura';
      }
    },
    async eliminarTransaccion(transaccion) {
      try {
        const id = extraerIdDesdeUrl(transaccion._links.self.href);
        if (!id) throw new Error("No se pudo extraer el ID de la transaccion");
        await deleteTransaccionPorId(id);
        this.cargarTransacciones();
      } catch (e) {
        console.error('Error al eliminar cliente:', e);
        this.error = 'No se pudo eliminar el cliente';
      }
    },
  }
});

function extraerIdDesdeUrl(url) {
  return url.split('/').pop();
}

function limpiarHref(href) {
  return href.split('{')[0]; // corta justo antes del `{`
}