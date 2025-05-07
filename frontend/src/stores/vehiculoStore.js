import { defineStore } from 'pinia';
import axios from 'axios';

export const useVehiculoStore = defineStore('vehiculo', {
  state: () => ({
    vehiculos: [],
    cargando: false,
    error: null
  }),
  actions: {
    async cargarVehiculos() {
      this.cargando = true;
      this.error = null;
      try {
        const res = await axios.get('http://localhost:8083/api/vehiculos');
        this.vehiculos = res.data._embedded?.vehiculos || [];
      } catch (e) {
        this.error = 'Error al cargar vehículos';
        console.error(e);
      } finally {
        this.cargando = false;
      }
    },

    async crearVehiculo(vehiculo) {
      try {
        const res = await axios.post('http://localhost:8083/api/vehiculos', vehiculo);
        this.vehiculos.push(res.data);
      } catch (e) {
        console.error("Error al crear vehículo:", e);
        this.error = 'No se pudo crear el vehículo';
      }
    },

    async eliminarVehiculo(vehiculo) {
      try {
        const id = vehiculo._links?.self?.href.split('/').pop();

        if (!id) throw new Error("No se pudo extraer el ID del vehículo");
    
        await axios.delete(`http://localhost:8083/api/vehiculos/${id}`);
        this.vehiculos = this.vehiculos.filter(v => extraerIdDesdeUrl(v._links.self.href) !== id);
      } catch (e) {
        console.error('Error al eliminar vehículo:', e);
        this.error = 'No se pudo eliminar el vehículo';
      }
    },

    async editarVehiculo(vehiculo) {
      try {
        const url = vehiculo._links.self.href;
        const res = await axios.put(url, vehiculo);

        const index = this.vehiculos.findIndex(v => v._links.self.href === url);
        if (index !== -1) {
          this.vehiculos[index] = res.data;
        }
      } catch (e) {
        console.error("Error al editar vehículo:", e);
        this.error = "No se pudo editar el vehículo";
      }
    }
  }
});

function extraerIdDesdeUrl(url) {
  return url.split('/').pop();
}