import { defineStore } from 'pinia';
import axios from 'axios';

export const useFacturaStore = defineStore('factura', {
    state: () => ({
        facturas: [],
        cargando: false,
        error: null
    }),
    actions: {
        async cargarFacturas() {
            this.cargando = true;
            this.error = null;
            try {
                const response = await axios.get('http://localhost:8083/api/facturas');
                this.facturas = response.data._embedded.facturas || [];
            } catch (err) {
                this.error = 'Error al cargar las facturas';
            } finally {
                this.cargando = false;
            }
        },
        async actualizarFactura(factura) {
            try {
                const url = factura._links.self.href;
                const res = await axios.put(url, factura);

                const index = this.facturas.findIndex(f => f._links.self.href === url);
                if (index !== -1) {
                    this.facturas[index] = res.data;
                }
            } catch (e) {
                console.error("Error al editar factura:", e);
                this.error = "No se pudo editar la factura";
            }
        }
    }
});

function extraerIdDesdeUrl(url) {
    return url.split('/').pop();
}