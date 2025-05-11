import { defineStore } from 'pinia';
import { getFacturas, updateFactura } from './api-service';


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
                const response = await getFacturas();
                this.facturas = response.data._embedded.facturas || [];
            } catch (err) {
                this.error = 'Error al cargar las facturas';
            } finally {
                this.cargando = false;
            }
        },
        async actualizarFactura(factura) {
            try {
                const url = factura._links?.self?.href;
                const res = await updateFactura(url, factura);

                const index = this.facturas.findIndex(f => f._links?.self?.href === url);
                if (index !== -1) {
                    this.facturas[index] = {
                        ...res.data,
                        _links: responde.data_links || factura._links
                    };
                }
            } catch (e) {
                this.error = "No se pudo editar la factura";
            }
        }
    }
});