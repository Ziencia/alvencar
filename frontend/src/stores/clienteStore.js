import axios from 'axios';
import { defineStore } from 'pinia';

export const useClienteStore = defineStore('cliente', {
  state: () => ({
    clientes: [],
    cargando: false,
    error: null
  }),
  actions: {
    async cargarClientes() {
      this.cargando = true;
      this.error = null;
      try {
        const res = await axios.get('http://localhost:8083/api/clientes');
        this.clientes = res.data._embedded?.clientes || [];
      } catch (e) {
        this.error = 'Error al cargar clientes';
        console.error(e);
      } finally {
        this.cargando = false;
      }
    },
    async crearCliente(cliente) {
      try {
        const res = await axios.post('http://localhost:8083/api/clientes', cliente);
        this.clientes.push(res.data);
      } catch (e) {
        console.error("Error al crear cliente:", e);
        this.error = 'No se pudo crear el cliente';
      }
    },
    async eliminarCliente(cliente) {
      try {
        const id = extraerIdDesdeUrl(cliente._links.self.href);

        if (!id) throw new Error("No se pudo extraer el ID del cliente");

        await axios.delete(`http://localhost:8083/api/clientes/${id}`);
        this.clientes = this.clientes.filter(c => extraerIdDesdeUrl(c._links.self.href) !== id);
      } catch (e) {
        console.error('Error al eliminar cliente:', e);
        this.error = 'No se pudo eliminar el cliente';
      }
    },
    async editarCliente(cliente) {
      try {
        const url = cliente._links.self.href;
        const res = await axios.put(url, cliente);

        const index = this.clientes.findIndex(c => c._links.self.href === url);
        if (index !== -1) {
          this.clientes[index] = res.data;
        }
      } catch (e) {
        console.error("Error al editar cliente:", e);
        this.error = "No se pudo editar el cliente";
      }
    }
  }
});

function extraerIdDesdeUrl(url) {
  return url.split('/').pop();
}