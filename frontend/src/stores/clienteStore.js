import axios from 'axios';
import { defineStore } from 'pinia';
import { getClientes, postCliente, deleteCliente, updateCliente } from "@/stores/api-service.js"

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
        const res = await getClientes();
        this.clientes = res.data._embedded?.clientes || [];
      } catch (e) {
        this.error = 'Error al cargar clientes';
      } finally {
        this.cargando = false;
      }
    },
    async crearCliente(cliente) {
      try {
        const res = await postCliente(cliente);
        this.clientes.push(res.data);
      } catch (e) {
        this.error = 'No se pudo crear el cliente';
      }
    },
    async eliminarCliente(cliente) {
      try {
        const url = cliente._links.self.href;
        await deleteCliente(url);
        this.clientes = this.clientes.filter(c => c._links.self.href !== url);
      } catch (e) {
        this.error = 'No se pudo eliminar el cliente';
      }
    },
    async editarCliente(cliente) {
      try {
        const url = cliente._links.self.href;
        const { _links, ...clienteSinLinks } = cliente; 
        const res = await updateCliente(url, clienteSinLinks);
        const index = this.clientes.findIndex(c => c._links.self.href === url);
        if (index !== -1) {
          this.clientes[index] = res.data;
        }
      } catch (e) {
        this.error = "No se pudo editar el cliente";
      }
    }
  }
});

function extraerIdDesdeUrl(url) {
  return url.split('/').pop();
}