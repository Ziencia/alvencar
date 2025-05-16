<script>
import { useClienteStore } from '@/stores/clienteStore';
import { mapState } from 'pinia';
import { Modal } from 'bootstrap';

export default {
  data() {
    return {
      nuevoCliente: {
        cif: '',
        nombre: '',
        primerApellido: '',
        segundoApellido: '',
        direccion: '',
        telefono: ''
      },
      clienteEditando: null,
      clienteEliminando: null
    };
  },
  computed: {
    ...mapState(useClienteStore, ['clientes', 'cargando', 'error'])
  },
  created() {
    this.clienteStore = useClienteStore();
    this.clienteStore.cargarClientes();
  },
  methods: {
    editarCliente(cliente) {
      this.clienteEditando = cliente;
      this.nuevoCliente = { ...cliente };
      const modalEl = document.getElementById('clienteModal');
      const modal = Modal.getOrCreateInstance(modalEl);
      modal.show();
    },
    async guardarCliente() {
      try {
        if (this.clienteEditando) {
          const clienteActualizado = { ...this.nuevoCliente, _links: this.clienteEditando._links };
          await this.clienteStore.editarCliente(clienteActualizado);
        } else {
          await this.clienteStore.crearCliente({ ...this.nuevoCliente });
        }
        this.nuevoCliente = {
          cif: '',
          nombre: '',
          primerApellido: '',
          segundoApellido: '',
          direccion: '',
          telefono: ''
        };

        const modalEl = document.getElementById('clienteModal');
        const modal = Modal.getInstance(modalEl);
        if (modal) modal.hide();
        this.quitarFocoModal();
      } catch (error) {
        console.error('Error al guardar cliente:', error);
      }
    },
    solicitarEliminacion(cliente) {
      this.clienteEliminando = cliente;
      const modalEl = document.getElementById('confirmarEliminarCliente');
      const modal = Modal.getOrCreateInstance(modalEl);
      modal.show();
    },
    async eliminarCliente(cliente) {
      const modalEl = document.getElementById('confirmarEliminarCliente');
      const modal = Modal.getOrCreateInstance(modalEl);
      modal.hide();
      this.quitarFocoModal();
      await this.clienteStore.eliminarCliente(cliente);

    },
    quitarFocoModal() {
      if (document.activeElement instanceof HTMLElement) {
        document.activeElement.blur();
      }
    },
  }
};
</script>

<template>

  <div class="container mt-4">
    <h3 class="mb-4 text-dark">
      🫂 Listado de Clientes
      <small class="text-muted">(Actualmente hay {{ clientes.length }} clientes)</small>
    </h3>


    <div v-if="cargando" class="alert alert-info">Cargando clientes...</div>
    <div v-if="error" class="alert alert-danger">{{ error }}</div>
    <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#clienteModal">
      ➕ Añadir Cliente
    </button>

    <div class="row g-4">
      <div class="col-md-6 col-lg-4" v-for="cliente in clientes" :key="cliente.id">
        <div class="card shadow-sm h-100">
          <div class="card-body">
            <h5 class="card-title text-primary">{{ cliente.nombre }} {{ cliente.primerApellido }} {{
              cliente.segundoApellido }}
            </h5>
            <p class="card-text mb-1"><strong>CIF:</strong> {{ cliente.cif }}</p>
            <p class="card-text mb-1"><strong>Dirección:</strong> {{ cliente.direccion }}</p>
            <p class="card-text mb-1"><strong>Teléfono:</strong> {{ cliente.telefono }}</p>
          </div>
          <div class="card-footer bg-transparent border-0 d-flex justify-content-end gap-2">
            <button class="btn btn-sm btn-outline-primary" @click="editarCliente(cliente)"
              aria-labelledby="Editar Cliente">
              <i class="bi bi-pencil-fill"></i>
            </button>
            <button class="btn btn-sm btn-outline-danger" @click="solicitarEliminacion(cliente)"
              aria-labelledby="Eliminar Cliente">
              <i class="bi bi-trash-fill"></i>
            </button>
          </div>
        </div>
      </div>

      <div v-if="clientes.length === 0" class="text-center text-muted">
        No hay clientes disponibles.
      </div>
    </div>

    <div class="modal fade" id="clienteModal" tabindex="-1" aria-labelledby="clienteModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow">
          <div class="modal-header bg-primary text-white">
            <h5 class="modal-title" id="clienteModalLabel">Nuevo Cliente</h5>
            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
              aria-label="Cerrar"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="guardarCliente">
              <div class="mb-3">
                <label class="form-label">CIF</label>
                <input v-model="nuevoCliente.cif" type="text" class="form-control" required />
              </div>
              <div class="mb-3">
                <label class="form-label">Nombre</label>
                <input v-model="nuevoCliente.nombre" type="text" class="form-control" required />
              </div>
              <div class="mb-3">
                <label class="form-label">Primer Apellido</label>
                <input v-model="nuevoCliente.primerApellido" type="text" class="form-control" />
              </div>
              <div class="mb-3">
                <label class="form-label">Segundo Apellido</label>
                <input v-model="nuevoCliente.segundoApellido" type="text" class="form-control" />
              </div>
              <div class="mb-3">
                <label class="form-label">Dirección</label>
                <input v-model="nuevoCliente.direccion" type="text" class="form-control" />
              </div>
              <div class="mb-3">
                <label class="form-label">Teléfono</label>
                <input v-model="nuevoCliente.telefono" type="text" class="form-control" />
              </div>
              <div class="text-end">
                <button type="submit" class="btn btn-success">Guardar</button>
                <button type="button" class="btn btn-secondary ms-2" data-bs-dismiss="modal">Cancelar</button>
              </div>
            </form>
          </div>
        </div>
      </div>

    </div>
  </div>

  <div class="modal fade" id="confirmarEliminarCliente" tabindex="-1" aria-labelledby="confirmarEliminarClienteLabel"
    aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="confirmarEliminarClienteLabel">Eliminar cliente</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <p>¿Estás seguro de que desea eliminar al cliente?</p>
          <p class="alert alert-warning text-danger fw-bold">Si elimina al cliente, se borraran las transacciones
            asociadas al mismo.</p>
          <p>Cliente: {{ clienteEliminando?.nombre }} {{ clienteEliminando?.primerApellido }} {{
            clienteEliminando?.segundoApellido }}, con CIF: {{ clienteEliminando?.cif }}</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
          <button type="button" class="btn btn-danger" @click="eliminarCliente(clienteEliminando)">Eliminar</button>
        </div>
      </div>
    </div>
  </div>
</template>