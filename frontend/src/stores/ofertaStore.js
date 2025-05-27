import { defineStore } from 'pinia';
import { getOfertas, getVehiculoOferta, postOferta, deleteOferta, updateOferta } from "@/stores/api-service.js"

export const useOfertaStore = defineStore('oferta', {
  state() {
    return {
      ofertas: [],
      cargando: false,
      error: null
    };
  },
  actions: {
    async cargarOfertas() {
      this.cargando = true;
      this.error = null;
      try {
        const res = await getOfertas();
        const ofertas = res.data._embedded?.ofertas || [];

        // https://stackoverflow.com/questions/51396360/how-to-use-promise-all-using-axios-async-await
        const ofertastasVehiculo = await Promise.all(
          ofertas.map(async (oferta) => {
            const vehiculoRes = await getVehiculoOferta(oferta._links.vehiculo.href);
            return { ...oferta, vehiculo: vehiculoRes.data };
          }));

        this.ofertas = ofertastasVehiculo;

      } catch (e) {
        this.error = 'Error al cargar ofertas';
      } finally {
        this.cargando = false;
      }
    },
    async crearOferta(oferta) {
      try {
        const res = await postOferta(oferta);
        this.oferta.push(res.data);
      } catch (e) {
        this.error = 'No se pudo crear la oferta';
      }
    },
    async eliminarOferta(oferta) {
      try {
        const url = oferta._links.self.href;
        await deleteOferta(url);
        this.ofertas = this.ofertas.filter(o => o._links.self.href !== url);
      } catch (e) {
        this.error = 'No se pudo eliminar la oferta';
      }
    },
    async editarOferta(oferta) {
      try {
        const url = oferta._links.self.href;
        const { _links, ...ofertaSinLinks } = oferta;
        const res = await updateOferta(url, ofertaSinLinks);
        const index = this.ofertas.findIndex(o => o._links.self.href === url);
        if (index !== -1) {
          this.ofertas[index] = res.data;
        }
      } catch (e) {
        this.error = "No se pudo editar la oferta";
      }
    }
  }
});