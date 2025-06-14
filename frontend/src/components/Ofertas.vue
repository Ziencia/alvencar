<script>
import { useOfertaStore } from '@/stores/ofertaStore';
import { useVehiculoStore } from '@/stores/vehiculoStore';
import { mapState } from 'pinia';
import { Modal } from 'bootstrap';

export default {
  data() {
    return {
      nuevaOferta: {
        ofertaVenta: '',
        ofertaAlquiler: '',
        vehiculoID: null,
      },
      ofertaEditando: null,
      ofertaEliminando: null
    };
  },
  computed: {
    ...mapState(useOfertaStore, ['ofertas', 'cargando', 'error']),
    ...mapState(useVehiculoStore, ['vehiculos'])
  },
  created() {
    this.cargarDatos();
  },
  methods: {
    async cargarDatos() {
      this.ofertaStore = useOfertaStore();
      if (this.ofertaStore.ofertas.length === 0) {
        this.ofertaStore.cargarOfertas();
      }
      this.vehiculoStore = useVehiculoStore();
      if (this.vehiculoStore.vehiculos.length === 0) {
        this.vehiculoStore.cargarVehiculos();
      }
    
      this.vehiculoStore.cargarVehiculos();
    },
    editarOferta(oferta) {
      this.ofertaEditando = oferta;
      this.nuevaOferta = {
        ofertaVenta: oferta.ofertaVenta,
        ofertaAlquiler: oferta.ofertaAlquiler,
        vehiculoID: oferta.vehiculo.id
      };
      const modalOM = document.getElementById('ofertaModal');
      const modal = Modal.getOrCreateInstance(modalOM);
      modal.show();
    },
    async guardarOferta() {
      try {
        const vehiculoSeleccionado = this.vehiculos.find(v => Number(v.id) === Number(this.nuevaOferta.vehiculoID));
        const vehiculoSeleccionadoURL = vehiculoSeleccionado._links.self.href;

        const datosOferta = {
          ... this.ofertaEditando,
          vehiculo: vehiculoSeleccionadoURL,
          ofertaAlquiler: this.nuevaOferta.ofertaAlquiler,
          ofertaVenta: this.nuevaOferta.ofertaVenta
        }

        if (this.ofertaEditando) {
          const datosOfertaEditada = { ...datosOferta, _links: this.ofertaEditando._links };
          await this.ofertaStore.editarOferta(datosOferta);
          this.ofertaEditando = null;
        } else {
          await this.ofertaStore.crearOferta(datosOferta);
        }
        await this.ofertaStore.cargarOfertas();
        this.nuevaOferta = {
          ofertaVenta: '',
          ofertaAlquiler: '',
          vehiculoID: null
        };

        const modalOM = document.getElementById('ofertaModal');
        const modal = Modal.getInstance(modalOM);
        if (modal) modal.hide();
        this.quitarFocoModal();
      } catch (error) {
        console.error('Error al guardar oferta:', error);
      }
    },
    nuevaOfertaModal() {
      this.nuevaOferta = {
        ofertaVenta: '',
        ofertaAlquiler: '',
        vehiculoID: null
      };
      this.ofertaEditando = null;

      const modalOM = document.getElementById('ofertaModal');
      const modal = Modal.getOrCreateInstance(modalOM);
      modal.show();
    },
    solicitarEliminacion(oferta) {
      this.ofertaEliminando = oferta;
      const modalOM = document.getElementById('confirmarEliminarOferta');
      const modal = Modal.getOrCreateInstance(modalOM);
      modal.show();
    },
    async eliminarOferta(oferta) {
      const modalOM = document.getElementById('confirmarEliminarOferta');
      const modal = Modal.getOrCreateInstance(modalOM);
      modal.hide();
      this.quitarFocoModal();
      await this.ofertaStore.eliminarOferta(oferta);
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
      🔖 Listado de ofertas
      <small v-if="ofertas.length > 0" class="text-muted">
        (Actualmente hay {{ ofertas.length }} {{ ofertas.length === 1 ? 'oferta' : 'ofertas' }})
      </small>
    </h3>

    <div v-if="error" class="alert alert-danger">{{ error }}</div>

    <div class="d-flex mb-3">
      <button class="btn btn-outline-dark btn-lg mb-3 ms-auto" data-bs-toggle="modal" data-bs-target="#ofertaModal"
        @click="nuevaOfertaModal">
        ➕ Añadir oferta
      </button>
    </div>

    <div class="row g-4">
      <div class="col-md-6 col-lg-4" v-for="oferta in ofertas" :key="oferta.id">
        <div class="card shadow-sm h-100">
          <div class="card-body">
            <h5 class="card-title">Vehiculo {{ oferta.vehiculo?.matricula }} </h5>
            <div class="border-top my-3"></div>
            <p class="card-text mb-1"><strong>Oferta venta:</strong> {{ oferta.ofertaVenta }} €</p>
            <p class="card-text mb-1"><strong>Oferta alquiler:</strong> {{ oferta.ofertaAlquiler }} €</p>
          </div>
          <div class="card-footer bg-transparent border-0 d-flex justify-content-end gap-2">
            <button class="btn btn-sm btn-outline-primary" @click="editarOferta(oferta)"
              aria-labelledby="Editar Oferta">
              <i class="bi bi-pencil-fill"></i>
            </button>
            <button class="btn btn-sm btn-outline-danger" @click="solicitarEliminacion(oferta)"
              aria-labelledby="Eliminar oferta">
              <i class="bi bi-trash-fill"></i>
            </button>
          </div>
        </div>
      </div>

      <div v-if="ofertas.length === 0" class="text-center text-muted">
        No hay ofertas disponibles.
      </div>
    </div>

    <div class="modal fade" id="ofertaModal" tabindex="-1" aria-labelledby="ofertaModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow">
          <div class="modal-header bg-primary text-white">
            <h5 class="modal-title" id="ofertaModalLaber">Oferta de vehículo</h5>
            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
              aria-label="Cerrar"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="guardarOferta">
              <div class="mb-3">
                <label class="form-label fw-semibold fs-6">Vehículo</label>
                <select v-model.number="nuevaOferta.vehiculoID" class="form-select form-select-m" required>
                  <option v-for="v in vehiculos" :key="v.id" :value="v.id">
                    {{ v.matricula }}, {{ v.marca }} {{ v.modelo }}
                  </option>
                </select>
              </div>
              <div class="mb-3">
                <label class="form-label">Oferta de venta:</label>
                <input v-model="nuevaOferta.ofertaVenta" type="number" class="form-control" required />
              </div>
              <div class="mb-3">
                <label class="form-label">Oferta de alquiler:</label>
                <input v-model="nuevaOferta.ofertaAlquiler" type="number" class="form-control" required />
              </div>
              <div class="text-end">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="submit" class="btn btn-success ms-2">Guardar</button>
              </div>
            </form>
          </div>
        </div>
      </div>

    </div>
  </div>

  <div class="modal fade" id="confirmarEliminarOferta" tabindex="-1" aria-labelledby="confirmarEliminarOfertaLabel"
    aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="confirmarEliminarOfertaLabel">Eliminar oferta</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <p>¿Estás seguro de que desea eliminar la oferta?</p>
          <p class="alert alert-warning text-danger fw-bold">Si elimina la oferta, no se podrá recuperar</p>
          <p> Vehiculo: {{ ofertaEliminando?.vehiculo?.matricula }}, oferta para alquiler {{
            ofertaEliminando?.ofertaAlquiler }}
            y venta {{ ofertaEliminando?.ofertaVenta }}</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
          <button type="button" class="btn btn-danger" @click="eliminarOferta(ofertaEliminando)">Eliminar</button>
        </div>
      </div>
    </div>
  </div>
</template>