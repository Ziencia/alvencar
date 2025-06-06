<script>
import { useTransaccionStore } from '@/stores/transaccionStore';
import { mapState } from 'pinia';
import { Modal } from 'bootstrap';
import { getFechaHoraActual } from '@/util/Fecha.js';
import { formatoMoneda, formatoNumero } from '@/util/Numero.js';
import dayjs from 'dayjs';
import { getDiferenciaEntreFechas } from '@/util/Fecha.js';

export default {
  data() {
    return {
      facturaSeleccionada: null,
      transaccionEliminando: null,
      facturaModal: null,
      alquilerEditando: {
        kmDespues: null,
        depositoDespues: null,
        fechaHoraDevolucion: getFechaHoraActual()
      },
      alquilerEditandoModal: null,
      getDiferenciaEntreFechas
    };
  },
  computed: {
    ...mapState(useTransaccionStore, ['ventas', 'alquileres', 'cargando', 'error']),
    totalTransacciones() {
      return this.ventas.length + this.alquileres.length;
    }
  },
  mounted() {
    this.transaccionStore = useTransaccionStore();
    this.transaccionStore.cargarTransacciones();

    const modalElement = document.getElementById('facturaModal');
    if (modalElement) {
      this.facturaModal = new Modal(modalElement, { backdrop: 'static' });
    }

    const alquilerModalElement = document.getElementById('alquilerEditandoModal');
    if (alquilerModalElement) {
      this.alquilerEditandoModal = new Modal(alquilerModalElement, { backdrop: 'static' });
    }
  },
  methods: {
    formatearFecha(fechaISO) {
      return dayjs(fechaISO).format('DD/MM/YYYY HH:mm');
    },
    abrirModalFactura(venta) {
      this.facturaSeleccionada = venta;
      this.facturaModal.show();
    },
    formatoNumero(numero) {
      return formatoNumero(numero);
    },
    formatoMoneda(numero) {
      return formatoMoneda(numero);
    },
    async confirmarGenerarFactura() {
      const factura = {
        conceptoFactura: 'Venta de vehículo',
        nombreApellidosDNI: `${this.facturaSeleccionada.cliente.nombre} ${this.facturaSeleccionada.cliente.primerApellido} ${this.facturaSeleccionada.cliente.segundoApellido}, DNI: ${this.facturaSeleccionada.cliente.cif}`,
        datosDireccionLocalizacion: `${this.facturaSeleccionada.cliente.direccion}, Teléfono: ${this.facturaSeleccionada.cliente.telefono}`,
        datosVehiculo: `${this.facturaSeleccionada.vehiculo.marca} ${this.facturaSeleccionada.vehiculo.modelo}, matrícula: ${this.facturaSeleccionada.vehiculo.matricula}`,
        importe: this.facturaSeleccionada.importe,
        impuestos: 0.21,
        importeTotal: this.facturaSeleccionada.importe * 1.21,
        fechaFactura: new Date().toISOString(),
        estaPagada: false
      };
      await this.transaccionStore.crearFactura(factura);
      this.facturaModal.hide();
    },
    solicitarEliminacion(transaccion) {
      this.transaccionEliminando = transaccion;
      const modalEl = document.getElementById('confirmarEliminarTransaccion');
      const modal = Modal.getOrCreateInstance(modalEl);
      modal.show();
    },
    async eliminarTransaccion(transaccion) {
      const modalEl = document.getElementById('confirmarEliminarTransaccion');
      const modal = Modal.getOrCreateInstance(modalEl);
      modal.hide();
      this.quitarFocoModal();
      await this.transaccionStore.eliminarTransaccion(transaccion);
    },
    quitarFocoModal() {
      if (document.activeElement instanceof HTMLElement) {
        document.activeElement.blur();
      }
    },
    alquilerCerrado(alquiler) {
      return alquiler.fechaHoraDevolucion &&
        alquiler.kmDespues &&
        alquiler.depositoDespues;
    },
    editarAlquiler(alquiler) {
      this.alquilerEditando = { ...alquiler };

      if (!this.alquilerEditando.fechaHoraDevolucion) {
        this.alquilerEditando.fechaHoraDevolucion = getFechaHoraActual();
      }

      if (!this.alquilerEditandoModal) {
        const modalEA = document.getElementById('alquilerEditandoModal');
        this.alquilerEditandoModal = Modal.getOrCreateInstance(modalEA);
      }

      this.alquilerEditandoModal.show();
    },
    async actualizarAlquiler() {
      try {
        await this.transaccionStore.editarAlquiler(this.alquilerEditando);
        await this.transaccionStore.cargarTransacciones();
        this.alquilerEditandoModal.hide();

        this.quitarFocoModal();
      } catch (error) {
        this.error = 'Error al actualizar el alquiler';
      }
    },
    cancelarEditandoModal() {
      this.alquilerEditando = {
        kmDespues: null,
        depositoDespues: null,
        fechaHoraDevolucion: getFechaHoraActual()
      };
      document.getElementById('btnCancelarEditandoAlquiler').blur();
    },
    getTotal(alquiler) {
      if (!alquiler.fechaHoraDevolucion) {
        return 'N/D';
      } else {
        const numeroDias = this.getDiferenciaEntreFechas(alquiler);
        let total = numeroDias === 0 ? alquiler.importe : numeroDias * alquiler.importe;

        if (alquiler.kmDespues) {
          const kmIncluidos = numeroDias === 0 ? 150 : numeroDias * 150;
          const kmRecorridos = alquiler.kmDespues - alquiler.kmAntes;
          if (kmRecorridos > kmIncluidos) {
            total += (kmRecorridos - kmIncluidos) * 0.03;
          }
        }

        if (alquiler.depositoDespues === 'Menos') {
          total += 80;
        }
        return total;
      }
    }
  }
};
</script>

<template>
  <div class="container mt-4">
    <h3 class="mb-4 text-dark">
      📚 Listado de Transacciones
      <small class="text-muted">(Actualmente hay {{ totalTransacciones }} transacciones)</small>
    </h3>

    <!-- Mensajes de carga y error -->
    <div v-if="cargando" class="alert alert-info">Cargando clientes...</div>
    <div v-if="error" class="alert alert-danger">{{ error }}</div>

    <div class="d-flex mb-3">
      <button @click="$router.push({ name: 'venta' })" class="btn btn-outline-dark btn-lg mb-3 ms-auto">
        ➕ Añadir Venta
      </button>
      <button @click="$router.push({ name: 'alquiler' })" class="btn btn-outline-dark btn-lg mb-3 ms-3">
        ➕ Añadir Alquiler
      </button>
    </div>

    <!-- Tabs Bootstrap -->
    <ul class="nav nav-tabs mb-4" id="transaccionTabs" role="tablist">
      <li class="nav-item" role="presentation">
        <button class="nav-link active text-dark fs-5" id="alquileres-tab" data-bs-toggle="tab"
          data-bs-target="#alquileres" type="button" role="tab">ALQUILER</button>
      </li>
      <li class="nav-item" role="presentation">
        <button class="nav-link text-dark fs-5" id="ventas-tab" data-bs-toggle="tab" data-bs-target="#ventas"
          type="button" role="tab">VENTA</button>
      </li>
    </ul>

    <div class="tab-content" id="transaccionTabsContent">

      <!-- ALQUILERES -->
      <div class="tab-pane fade show active" id="alquileres" role="tabpanel">
        <div v-if="alquileres.length === 0" class="text-muted">No hay alquileres registrados.</div>
        <div class="row" v-else>
          <div class="col-8 mb-4 mx-auto" v-for="(alquiler, i) in alquileres" :key="i">
            <div class="card h-100 shadow-sm">
              <div class="card-body">
                <h5 class="card-title">Alquiler del vehículo {{ alquiler.vehiculo?.matricula }} ({{
                  alquiler.vehiculo?.marca }}, {{ alquiler.vehiculo?.modelo }})</h5>
                <div class="border-top my-3"></div>
                <div class="row mb-3">
                  <div class="col-3 fw-bold">Realizada el:</div>
                  <div class="col-3">{{ formatearFecha(alquiler.fechaHoraEntrega) }}</div>
                  <div class="col-3 fw-bold">Con devolución el:</div>
                  <div class="col-3">{{ alquiler.fechaHoraDevolucion ? formatearFecha(alquiler.fechaHoraDevolucion) : ''
                    }}</div>
                </div>

                <p><strong>Datos del cliente asociado: </strong>
                  {{ alquiler.cliente?.nombre }} {{
                    alquiler.cliente?.primerApellido }} {{ alquiler.cliente?.segundoApellido }} ({{ alquiler.cliente?.cif
                  }})</p>

                <div class="row mb-3">
                  <div class="col-3 fw-bold">Km. iniciales:</div>
                  <div class="col-3">{{ formatoNumero(alquiler.kmAntes) }}</div>
                  <div class="col-3 fw-bold">Km. finales:</div>
                  <div class="col-3" v-if="alquiler.kmDespues">{{ formatoNumero(alquiler.kmDespues) }}</div>
                </div>

                <div class="row mb-3">
                  <div class="col-3 fw-bold">Depósito inicial:</div>
                  <div class="col-3">{{ alquiler.depositoAntes }}</div>
                  <div class="col-3 fw-bold">Depósito final:</div>
                  <div class="col-3">{{ alquiler.depositoDespues }}</div>
                </div>

                <div class="row mb-3">
                  <div class="col-3 fw-bold">Precio/dia:</div>
                  <div class="col-3">{{ formatoMoneda(alquiler.importe) }}</div>
                  <div class="col-3 fw-bold">Total estimado:</div>
                  <div class="col-3">{{ formatoMoneda(this.getTotal(alquiler)) }}</div>
                </div>

                <div class="card-footer bg-transparent border-0 d-flex justify-content-end gap-2">
                  <button class="btn btn-sm btn-outline-primary" @click="abrirModalFactura(alquiler)"
                    :disabled="!alquilerCerrado(alquiler)" aria-labelledby="Generar factura">
                    <i class="bi bi-envelope-paper-fill"></i>
                  </button>
                  <button class="btn btn-sm btn-outline-primary" @click="editarAlquiler(alquiler)"
                    aria-labelledby="Cerrar alquiler">
                    <i class="bi bi-pencil-fill"></i>
                  </button>
                  <div class="d-flex justify-content-end gap-2">
                    <button class="btn btn-sm btn-outline-danger" @click="solicitarEliminacion(alquiler)"
                      aria-labelledby="Eliminar vehículo">
                      <i class="bi bi-trash-fill"></i>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>


      <!-- VENTAS -->
      <div class="tab-pane fade" id="ventas" role="tabpanel">
        <div v-if="ventas.length === 0" class="text-muted">No hay ventas registradas.</div>
        <div class="row" v-else>
          <div class="col-8 mb-4 mx-auto" v-for="(venta, i) in ventas" :key="i">
            <div class="card h-100 shadow-sm">
              <div class="card-body">
                <h5 class="card-title">Venta del vehículo {{ venta.vehiculo?.matricula }} ({{ venta.vehiculo?.marca }},
                  {{ venta.vehiculo?.modelo }})</h5>
                <div class="border-top my-3"></div>
                <p><strong>Realizada el:</strong> {{ formatearFecha(venta.fechaHoraEntrega) }}</p>
                <p><strong>Fin de Garantía:</strong> {{ formatearFecha(venta.fechaFinGarantia) }}</p>
                <p><strong>Por importe de:</strong>
                  {{ venta.importe }}€ </p>
                <p><strong>Datos del cliente asociado: </strong>
                  {{ venta.cliente?.nombre }} {{
                    venta.cliente?.primerApellido }} {{ venta.cliente?.segundoApellido }} ({{ venta.cliente?.cif }})</p>

                <div class="card-footer bg-transparent border-0 d-flex justify-content-end gap-2">

                  <button class="btn btn-sm btn-outline-primary" @click="abrirModalFactura(venta)"
                    aria-labelledby="Generar factura">
                    <i class="bi bi-envelope-paper-fill"></i>
                  </button>
                  <div class="d-flex justify-content-end gap-2">
                    <button class="btn btn-sm btn-outline-danger" @click="solicitarEliminacion(venta)"
                      aria-labelledby="Eliminar vehículo">
                      <i class="bi bi-trash-fill"></i>
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-show="facturaSeleccionada" class="modal fade" id="facturaModal" tabindex="-1"
        aria-labelledby="facturaModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="facturaModalLabel">¿Quiere generar la factura?</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
              <p><strong>FACTURA DE VENTA</strong></p>
              <p><strong>Cliente:</strong><br />
                {{ facturaSeleccionada?.cliente?.nombre }} {{ facturaSeleccionada?.cliente?.primerApellido }} {{
                  facturaSeleccionada?.cliente?.segundoApellido }}, DNI: {{ facturaSeleccionada?.cliente?.cif }}
                <br>{{ facturaSeleccionada?.cliente?.direccion }}, Telefono: {{ facturaSeleccionada?.cliente?.telefono
                }}
              </p>

              <p><strong>Vehículo:</strong> <br />
                {{ facturaSeleccionada?.vehiculo?.marca }} {{ facturaSeleccionada?.vehiculo?.modelo }}, matricula: {{
                  facturaSeleccionada?.vehiculo?.matricula }}</p>
              <p><strong>Importe de venta:</strong> {{ facturaSeleccionada?.importe }} €</p>
              <p><strong>Calculo de impuestos:</strong> 21% </p>
              <p><strong>Importe total:</strong> {{ facturaSeleccionada?.importe * 1.21 }} € </p>
              <p><strong>Fecha:</strong> {{ formatearFecha(facturaSeleccionada?.fechaHoraEntrega) }}</p>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
              <button type="button" class="btn btn-primary" @click="confirmarGenerarFactura">Confirmar</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="modal fade" id="confirmarEliminarTransaccion" tabindex="-1"
    aria-labelledby="confirmarEliminarTransaccionLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="confirmarEliminarTransaccionLabel">Eliminar transacción</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <p>¿Estás seguro de que desea eliminar la transacción?</p>
          <p class="alert alert-warning text-danger fw-bold">La eliminación sera irreversible.</p>
          <p>Transacción del vehiculo {{ transaccionEliminando?.vehiculo?.matricula }} ({{
            transaccionEliminando?.vehiculo?.marca }} {{ transaccionEliminando?.vehiculo?.modelo }}) </p>
          <p> del cliente {{ transaccionEliminando?.cliente?.cif }} ({{ transaccionEliminando?.cliente?.nombre }} {{
            transaccionEliminando?.cliente?.primerApellido }} {{ transaccionEliminando?.cliente?.segundoApellido }})</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
          <button type="button" class="btn btn-danger"
            @click="eliminarTransaccion(transaccionEliminando)">Eliminar</button>
        </div>
      </div>
    </div>
  </div>

  <div class="modal fade" id="alquilerEditandoModal" tabindex="-1" aria-labelledby="alquilerEditandoModal"
    aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content shadow">
        <div class="modal-header bg-primary text-white">
          <h5 class="modal-title" id="alquilerEditandoModal">Editar alquiler</h5>
          <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="actualizarAlquiler">
            <div class="mb-3">
              <label for="fechaHoraDevolucion" class="form-label">Fecha de devolución</label>
              <input type="datetime-local" v-model="alquilerEditando.fechaHoraDevolucion" class="form-control"
                id="fechaHoraDevolucion" />
            </div>
            <div class="mb-3">
              <label for="kmDespues" class="form-label">Kilómetros finales</label>
              <input type="number" v-model="alquilerEditando.kmDespues" class="form-control" id="kmDespues" />
            </div>
            <div class="mb-3">
              <label for="depositoDespues" class="form-label">Depósito final</label>
              <select v-model="alquilerEditando.depositoDespues" class="form-select" id="depositoDespues">
                <option value="" disabled>Seleccione</option>
                <option value="Igual">Igual</option>
                <option value="Menos">Menos</option>
              </select>
            </div>
            <div class="text-end">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" @click="cancelarEditandoModal"
                id="btnCancelarEditandoAlquiler">Cancelar</button>
              <button type="submit" class="btn btn-primary ms-2">Guardar</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>

</template>
