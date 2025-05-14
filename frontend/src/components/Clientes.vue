<script>
import { useClienteStore } from '@/stores/clienteStore';
import { storeToRefs } from 'pinia';
import { onMounted, ref } from 'vue';
import { Modal } from 'bootstrap';

export default {
  setup() {
    const clienteStore = useClienteStore();
    const { clientes, cargando, error } = storeToRefs(clienteStore);

    onMounted(() => {
      clienteStore.cargarClientes();
    });

    const nuevoCliente = ref({
      cif: '',
      nombre: '',
      primerApellido: '',
      segundoApellido: '',
      direccion: '',
      telefono: ''
    });

    const clienteEditando = ref(null);

    const editarCliente = (cliente) => {
      clienteEditando.value = cliente;
      nuevoCliente.value = { ...cliente };
      const modalEl = document.getElementById('clienteModal');
      const modal = Modal.getOrCreateInstance(modalEl);
      modal.show();
    };

    const guardarCliente = async () => {
      try {
        
        if (clienteEditando.value) {
          const clienteActualizado = { ...nuevoCliente.value, _links: clienteEditando.value._links };
          await clienteStore.editarCliente(clienteActualizado);
        } else {
          await clienteStore.crearCliente({ ...nuevoCliente.value });
        }

        nuevoCliente.value = {
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

      } catch (error) {
        console.error('Error al guardar cliente:', error);
      }
    };

    const eliminarCliente = async (id) => {
      if (confirm('¿Estás seguro de que deseas eliminar este cliente?')) {
        await clienteStore.eliminarCliente(id);
      }
    };

    return {
      clientes,
      cargando, 
      error,
      nuevoCliente,
      guardarCliente,
      eliminarCliente,
      editarCliente
    };
  }
};
</script>

<template>

  <div class="container mt-5">
    <h2 class="mb-4 text-primary">👤 Listado de Clientes</h2>

    <!-- Mensajes de carga y error -->
    <div v-if="cargando" class="alert alert-info">Cargando clientes...</div>
    <div v-if="error" class="alert alert-danger">{{ error }}</div>

    <!-- Conteo -->
    <div class="mb-3">
      <strong>Clientes cargados:</strong> {{ clientes.length }}
    </div>

    <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#clienteModal">
      ➕ Añadir Cliente
    </button>

    <div class="row g-4">
      <div class="col-md-6 col-lg-4" v-for="cliente in clientes" :key="cliente.id">
        <div class="card shadow-sm h-100">
          <div class="card-body">
            <h5 class="card-title text-primary">{{ cliente.nombre }} {{ cliente.primerApellido }} {{ cliente.segundoApellido }}
            </h5>
            <p class="card-text mb-1"><strong>CIF:</strong> {{ cliente.cif }}</p>
            <p class="card-text mb-1"><strong>Dirección:</strong> {{ cliente.direccion }}</p>
            <p class="card-text mb-1"><strong>Teléfono:</strong> {{ cliente.telefono }}</p>
          </div>
          <div class="card-footer bg-transparent border-0 d-flex justify-content-end gap-2">
            <button class="btn btn-sm btn-outline-primary" @click="editarCliente(cliente)">
              <i class="bi bi-pencil-fill"></i>
            </button>
            <button class="btn btn-sm btn-outline-danger" @click="eliminarCliente(cliente)">
              <i class="bi bi-trash-fill"></i>
            </button>
          </div>
        </div>
      </div>

      <!-- Sin resultados -->
      <div v-if="clientes.length === 0" class="text-center text-muted">
        No hay clientes disponibles.
      </div>
    </div>

    <div class="modal fade" id="clienteModal" tabindex="-1" aria-labelledby="clienteModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content shadow">
        <div class="modal-header bg-primary text-white">
          <h5 class="modal-title" id="clienteModalLabel">Nuevo Cliente</h5>
          <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
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
</template>