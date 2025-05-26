import { defineStore } from 'pinia';
import { getVehiculos, postVehiculo, deleteVehiculo, updateVehiculo } from './api-service';

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
        const res = await getVehiculos();
        this.vehiculos = res.data._embedded?.vehiculos || [];
      } catch (e) {
        this.error = 'Error al cargar vehículos';
      } finally {
        this.cargando = false;
      }
    },
    async cargarVehiculosNoVendidos() {
      this.cargando = true;
      this.error = null;
      try {
        const res = await getVehiculosNoVendidos();
        this.vehiculos = res.data._embedded?.vehiculos || [];
      } catch (e) {
        this.error = 'Error al cargar vehículos';
      } finally {
        this.cargando = false;
      }
    },
    async crearVehiculo(vehiculo) {
      try {
        const res = await postVehiculo(vehiculo);
        this.vehiculos.push(res.data);
      } catch (e) {
        this.error = 'No se pudo crear el vehículo';
      }
    },
    async eliminarVehiculo(vehiculo) {
      try {
        const url = vehiculo._links.self.href;
        await deleteVehiculo(url);
        this.vehiculos = this.vehiculos.filter(v => v._links.self.href !== url);
      } catch (e) {
        this.error = 'No se pudo eliminar el vehículo';
      }
    },
    async editarVehiculo(vehiculo) {
      try {
        const url = vehiculo._links.self.href;
        const { _links, ...vehiculoSinLinks } = vehiculo;
        const res = await updateVehiculo(url, vehiculoSinLinks);
        const index = this.vehiculos.findIndex(v => v._links.self.href === url);
        if (index !== -1) {
          this.vehiculos[index] = res.data;
        }
      } catch (e) {
        this.error = "No se pudo editar el vehículo";
      }
    }
  }
});