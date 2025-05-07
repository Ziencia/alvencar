<script>
import { onMounted, ref } from 'vue';
import { useVehiculoStore } from '@/stores/vehiculoStore';
import { storeToRefs } from 'pinia';
import { Modal } from 'bootstrap';

export default {
  setup() {
    const store = useVehiculoStore();
    const { vehiculos, cargando, error } = storeToRefs(store);

    const formulario = ref({
      matricula: '',
      bastidor: '',
      marca: '',
      modelo: '',
      color: '',
      fechaMatriculacion: '',
      condicionAdquisicion: ''
    });

    const vehiculoEditando = ref(null);

    const editarVehiculo = (vehiculo) => {
      vehiculoEditando.value = vehiculo;
      formulario.value = { ...vehiculo };
      const modalEl = document.getElementById('vehiculoModal');
      const modal = Modal.getOrCreateInstance(modalEl);
      modal.show();
    };

    const guardarVehiculo = async () => {
      try {
        console.log(" Enviando vehículo:", formulario.value);

        if (vehiculoEditando.value) {
          const vehiculoActualizado = { ...formulario.value, _links: vehiculoEditando.value._links };
          await store.editarVehiculo(vehiculoActualizado);
        } else {
          await store.crearVehiculo({ ...formulario.value });
        }

        formulario.value = {
          matricula: '',
          bastidor: '',
          marca: '',
          modelo: '',
          color: '',
          fechaMatriculacion: '',
          condicionAdquisicion: ''
        };

        const modalEl = document.getElementById('vehiculoModal');
        const modal = Modal.getInstance(modalEl);
        if (modal) modal.hide();

      } catch (error) {
        console.error('Error al guardar vehículo:', error);
      }
    };

    const eliminarVehiculo = async (id) => {
      if (confirm('¿Estás seguro de que deseas eliminar este vehículo?')) {
        await store.eliminarVehiculo(id);
      }
    };

    onMounted(() => {
      store.cargarVehiculos();
    });

    const formatFecha = (fecha) => {
      return fecha ? new Date(fecha).toLocaleDateString('es-ES') : '';
    };

    return {
      vehiculos,
      cargando,
      error,
      formatFecha,
      formulario,
      guardarVehiculo,
      eliminarVehiculo,
      editarVehiculo
    };
  }
};
</script>


<template>
  <div class="container mt-5">
    <h2 class="mb-4 text-primary">🚗 Listado de Vehículos</h2>

    <!-- Mensajes de carga y error -->
    <div v-if="cargando" class="alert alert-info">Cargando vehículos...</div>
    <div v-if="error" class="alert alert-danger">{{ error }}</div>

    <!-- Conteo -->
    <div class="mb-3">
      <strong>Vehículos cargados:</strong> {{ vehiculos.length }}
    </div>

    <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#vehiculoModal">
      ➕ Añadir Vehículo
    </button>
    <div class="container mt-4">

      <div class="row">
        <div class="col-12 mb-4" v-for="vehiculo in vehiculos" :key="vehiculo.id">
          <div class="card shadow-sm">
            <div class="card-body">
              <h5 class="card-title"> Vehículo: {{ vehiculo.matricula }}</h5>
              <h6 class="card-subtitle mb-2 text-muted">{{ vehiculo.marca }} - {{ vehiculo.modelo }}</h6>
              <ul class="list-group list-group-flush mb-3">
                <li class="list-group-item"><strong>Bastidor:</strong> {{ vehiculo.bastidor }}</li>
                <li class="list-group-item"><strong>Color:</strong> {{ vehiculo.color }}</li>
                <li class="list-group-item"><strong>Fecha Matriculación:</strong> {{
                  formatFecha(vehiculo.fechaMatriculacion) }}</li>
                <li class="list-group-item"><strong>Condición Adquisición:</strong> {{ vehiculo.condicionAdquisicion }}
                </li>
              </ul>
              <div class="d-flex justify-content-end gap-2">
                <button class="btn btn-sm btn-success" @click="">Transacciones</button>
                <button class="btn btn-sm btn-primary" @click="editarVehiculo(vehiculo)">Editar</button>
                <button class="btn btn-sm btn-danger" @click="eliminarVehiculo(vehiculo)">Eliminar</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Sin resultados -->
    <div v-if="vehiculos.length === 0" class="text-center text-muted">
      No hay vehiculos disponibles.
    </div>
  </div>

  <!-- Modal para crear/editar vehículo -->
  <div class="modal fade" id="vehiculoModal" tabindex="-1" aria-labelledby="vehiculoModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="vehiculoModalLabel">Formulario Vehículo</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="guardarVehiculo">
            <div class="row g-3">
              <div class="col-md-6">
                <label class="form-label">Matrícula</label>
                <input v-model="formulario.matricula" class="form-control" required />
              </div>
              <div class="col-md-6">
                <label class="form-label">Bastidor</label>
                <input v-model="formulario.bastidor" class="form-control" required />
              </div>
              <div class="col-md-6">
                <label class="form-label">Marca</label>
                <input v-model="formulario.marca" class="form-control" required />
              </div>
              <div class="col-md-6">
                <label class="form-label">Modelo</label>
                <input v-model="formulario.modelo" class="form-control" required />
              </div>
              <div class="col-md-6">
                <label class="form-label">Color</label>
                <input v-model="formulario.color" class="form-control" required />
              </div>
              <div class="col-md-6">
                <label class="form-label">Fecha de Matriculación</label>
                <input v-model="formulario.fechaMatriculacion" type="date" class="form-control" required />
              </div>
              <div class="col-md-12">
                <label class="form-label">Condición de Adquisición</label>
                <select v-model="formulario.condicionAdquisicion" class="form-select" required>
                  <option value="">Seleccione</option>
                  <option>Compra</option>
                  <option>Leasing</option>
                </select>
              </div>
            </div>
            <div class="mt-4 text-end">
              <button type="submit" class="btn btn-success">Guardar</button>
              <button type="button" class="btn btn-secondary ms-2" data-bs-dismiss="modal">Cancelar</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>
