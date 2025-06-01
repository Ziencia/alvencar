import axios from 'axios';
import { defineStore } from 'pinia';
import { getOfertas, postOferta, deleteOferta, updateOferta, updateOfertaVehiculo} from "@/stores/api-service.js"

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
        this.ofertas = res.data._embedded?.ofertas || [];

        // https://stackoverflow.com/questions/51396360/how-to-use-promise-all-using-axios-async-await
        this.ofertas = await Promise.all(
          this.ofertas.map(async (oferta) => {
            const ofertaCompleta = { ...oferta };
            try {
              const vehiculoRes = await axios.get(oferta._links.vehiculo.href);
              const vehiculo = vehiculoRes.data;
              const vehiculoID = vehiculo._links.self.href;
              vehiculo.id = vehiculoID.split('/').pop();
              ofertaCompleta.vehiculo = vehiculo;
            } catch (e) {
              ofertaCompleta.vehiculo = null;
            }
            return ofertaCompleta;
          }));
      } catch (e) {
        this.error = 'Error al cargar ofertas';
      } finally {
        this.cargando = false;
      }
    },
    async crearOferta(oferta) {
      try {
        const res = await postOferta(oferta);
        this.ofertas.push(res.data);
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
    //metodo sugerido en https://reflectoring.io/relations-with-spring-data-rest/
    async editarOferta(oferta) {
      try {
        const url = oferta._links.self.href;
        const vehiculHref = oferta.vehiculo;
        const { _links, ...ofertaSinLinks } = oferta;
        await updateOferta(url, ofertaSinLinks);
        await updateOfertaVehiculo(url,vehiculHref);
      } catch (e) {
        this.error = "No se pudo editar la oferta";
      }
    }
  }
});