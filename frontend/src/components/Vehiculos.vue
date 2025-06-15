<script>
import { useVehiculoStore } from '@/stores/vehiculoStore';
import { mapState } from 'pinia';
import { Modal } from 'bootstrap';

export default {
  data() {
    return {
      formulario: {
        matricula: '',
        bastidor: '',
        marca: '',
        modelo: '',
        color: '',
        fechaMatriculacion: '',
        condicionAdquisicion: '',
        vendido: null
      },
      vehiculoEditando: null,
      vehiculoEliminando: null
    };
  },
  computed: {
    ...mapState(useVehiculoStore, ['vehiculos', 'cargando', 'error'])
  },
  created() {
    this.vehiculoStore = useVehiculoStore();
    if (this.vehiculoStore.vehiculos.length === 0) {
      this.vehiculoStore.cargarVehiculos();
    }
  },
  methods: {
    async guardarVehiculo() {
      try {
        if (this.vehiculoEditando) {
          const vehiculoActualizado = {
            ...this.formulario,
            _links: this.vehiculoEditando._links
          };
          await this.vehiculoStore.editarVehiculo(vehiculoActualizado);
        } else {
          await this.vehiculoStore.crearVehiculo({ ...this.formulario });
        }

        this.formulario = {
          matricula: '',
          bastidor: '',
          marca: '',
          modelo: '',
          color: '',
          fechaMatriculacion: '',
          condicionAdquisicion: '',
          vendido: null
        };

        const modalEl = document.getElementById('vehiculoModal');
        const modal = Modal.getInstance(modalEl);
        if (modal) modal.hide();

        this.vehiculoEditando = null;

      } catch (e) {
        console.error("Error al guardar vehículo:", e);
      }
    },
    editarVehiculo(vehiculo) {
      this.vehiculoEditando = vehiculo;
      this.formulario = {
        matricula: vehiculo.matricula || '',
        bastidor: vehiculo.bastidor || '',
        marca: vehiculo.marca || '',
        modelo: vehiculo.modelo || '',
        color: vehiculo.color || '',
        fechaMatriculacion: vehiculo.fechaMatriculacion || '',
        condicionAdquisicion: vehiculo.condicionAdquisicion || '',
        vendido: vehiculo.vendido === true ? true : false
      };
      const modalEl = document.getElementById('vehiculoModal');
      const modal = Modal.getOrCreateInstance(modalEl);
      modal.show();
    },
    solicitarEliminacion(vehiculo) {
      this.vehiculoEliminando = vehiculo;
      const modalEl = document.getElementById('confirmarEliminarVehiculo');
      const modal = Modal.getOrCreateInstance(modalEl);
      modal.show();
    },
    async eliminarVehiculo(vehiculo) {
      const modalEl = document.getElementById('confirmarEliminarVehiculo');
      const modal = Modal.getOrCreateInstance(modalEl);
      modal.hide();
      this.quitarFocoModal();
      await this.vehiculoStore.eliminarVehiculo(vehiculo);
    },
    formatFecha(fecha) {
      return fecha ? new Date(fecha).toLocaleDateString('es-ES') : '';
    },
    quitarFocoModal() {
      if (document.activeElement instanceof HTMLElement) {
        document.activeElement.blur();
      }
    },
    verTransacciones(matricula){
      this.$router.push({
        name: 'transacciones',
        query: {
          matricula: matricula
        }
      });
    }    
  },
};
</script>

<template>
  <div class="container mt-4">
    <h3 class="mb-4 text-dark">
      🚗 Listado de vehículos
      <small class="text-muted">(Actualmente hay {{ vehiculos.length }} vehículos)</small>
    </h3>

    <div v-if="error" class="alert alert-danger">{{ error }}</div>

    <div class="d-flex mb-3">
      <button class="btn btn-outline-dark btn-lg mb-3 ms-auto" data-bs-toggle="modal" data-bs-target="#vehiculoModal">
        ➕ Añadir vehículo
      </button>
    </div>

    <div class="row g-4">
      <div class="col-md-6 col-lg-4" v-for="vehiculo in vehiculos" :key="vehiculo.id">
        <div class="card shadow-sm h-100">
          <div class="card-body">
            <h5 class="card-title"> {{ vehiculo.matricula }}</h5>
            <h6 class="card-subtitle mb-2 text-muted">{{ vehiculo.marca }} - {{ vehiculo.modelo }}</h6>
            <div class="border-top my-3"></div>
            <p class="card-text mb-1"><strong>Bastidor:</strong> {{ vehiculo.bastidor }}</p>
            <p class="card-text mb-1"><strong>Color:</strong> {{ vehiculo.color }}</p>
            <p class="card-text mb-1"><strong>Fecha Matriculación:</strong> {{
              formatFecha(vehiculo.fechaMatriculacion) }}</p>
            <p class="card-text mb-1"><strong>Condición Adquisición:</strong> {{ vehiculo.condicionAdquisicion }}</p>
            <p class="card-text mb-1"><strong>Estado del vehículo: </strong>
              <span :class="vehiculo.vendido ? 'text-success' : 'text-primary'">
                {{ vehiculo.vendido ? 'No disponible' : 'Disponible' }}
              </span>
            </p>
          </div>
          <div class="card-footer bg-transparent border-0 d-flex justify-content-end gap-2">
            <button class="btn btn-sm btn-outline-success" @click="verTransacciones(vehiculo?.matricula)" aria-labelledby="Buscar transacciones">
              <i class="bi bi-search"></i>
            </button>
            <button class="btn btn-sm btn-outline-primary" @click="editarVehiculo(vehiculo)"
              aria-labelledby="Editar vehículo">
              <i class="bi bi-pencil-fill"></i>
            </button>
            <button class="btn btn-sm btn-outline-danger" @click="solicitarEliminacion(vehiculo)"
              aria-labelledby="Eliminar vehículo">
              <i class="bi bi-trash-fill"></i>
            </button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="vehiculos.length === 0" class="text-center text-muted">
      No hay vehiculos disponibles.
    </div>
  </div>

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
              <div class="col-md-12">
                <label class="form-label">Estado del vehículo</label>
                <select v-model="formulario.vendido" class="form-select" required>
                  <option :value="null">Seleccione</option>
                  <option :value="true">No disponible</option>
                  <option :value="false">Disponible</option>
                </select>
              </div>
            </div>
            <div class="mt-4 text-end">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
              <button type="submit" class="btn btn-success ms-2">Guardar</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>

  <div class="modal fade" id="confirmarEliminarVehiculo" tabindex="-1" aria-labelledby="confirmarEliminarVehiculoLabel"
    aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="confirmarEliminarVehiculoLabel">Eliminar vehículo</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <p>¿Estás seguro de que desea eliminar el vehículo?</p>
          <p class="alert alert-warning text-danger fw-bold">Si elimina el vehículo, se borraran las transacciones
            asociadas al mismo.</p>
          <p>Vehiculo: {{ vehiculoEliminando?.matricula }}, {{ vehiculoEliminando?.marca }}-{{
            vehiculoEliminando?.modelo }}</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
          <button type="button" class="btn btn-danger" @click="eliminarVehiculo(vehiculoEliminando)">Eliminar</button>
        </div>
      </div>
    </div>
  </div>
</template>
