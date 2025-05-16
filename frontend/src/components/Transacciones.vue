<script>
import { useTransaccionStore } from '@/stores/transaccionStore';
import { mapState } from 'pinia';
import { Modal } from 'bootstrap';
import dayjs from 'dayjs';

export default {
  data() {
    return {
      facturaSeleccionada: null,
      transaccionEliminando: null,
      facturaModal: null,
      transaccionStore: useTransaccionStore()
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
  },
  methods: {
    formatearFecha(fechaISO) {
      return dayjs(fechaISO).format('DD/MM/YYYY HH:mm');
    },
    abrirModalFactura(venta) {
      this.facturaSeleccionada = venta;
      this.facturaModal.show();
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
  }
};
</script>

<template>
  <div class="container mt-4">
    <h2 class="mb-4 text-primary">📚 Listado de Transacciones</h2>

    <!-- Mensajes de carga y error -->
    <div v-if="cargando" class="alert alert-info">Cargando clientes...</div>
    <div v-if="error" class="alert alert-danger">{{ error }}</div>

    <!-- Conteo -->
    <div class="mb-3">
      <strong>Transacciones cargadas:</strong> {{ totalTransacciones }}
    </div>

    <div class="mb-3 d-flex gap-2">
      <button @click="$router.push({ name: 'venta' })" class="btn btn-primary">
        📑 Añadir Venta
      </button>
      <button class="btn btn-primary">
        🏪 Añadir Alquiler
      </button>
    </div>

    <!-- Tabs Bootstrap -->
    <ul class="nav nav-tabs mb-4" id="transaccionTabs" role="tablist">
      <li class="nav-item" role="presentation">
        <button class="nav-link active" id="ventas-tab" data-bs-toggle="tab" data-bs-target="#ventas" type="button"
          role="tab">Ventas</button>
      </li>
      <li class="nav-item" role="presentation">
        <button class="nav-link" id="alquileres-tab" data-bs-toggle="tab" data-bs-target="#alquileres" type="button"
          role="tab">Alquileres</button>
      </li>
    </ul>

    <div class="tab-content" id="transaccionTabsContent">

      <!-- VENTAS -->
      <div class="tab-pane fade show active" id="ventas" role="tabpanel">
        <div v-if="ventas.length === 0" class="text-muted">No hay ventas registradas.</div>
        <div class="row" v-else>
          <div class="col-8 mb-4 mx-auto" v-for="(venta, i) in ventas" :key="i">
            <div class="card h-100 shadow-sm">
              <div class="card-body">
                <h5 class="card-title text-primary">Venta por importe de {{ venta.importe }} €.</h5>
                <p>Realizada el: {{ formatearFecha(venta.fechaHoraEntrega) }}</p>
                <p>Régimen: {{ venta.regimen }}</p>
                <p>Fin de Garantía: {{ formatearFecha(venta.fechaFinGarantia) }}</p>

                <hr />

                <p><strong>Datos del vehículo asociado:</strong></p>
                <p> {{ venta.vehiculo?.marca }} {{ venta.vehiculo?.modelo }}, con matrícula <strong>{{
                  venta.vehiculo?.matricula }}</strong> </p>

                <hr />
                <p><strong>Datos del cliente asociado:</strong></p>
                <p><strong>{{ venta.cliente?.cif }}</strong>, {{ venta.cliente?.nombre }} {{
                  venta.cliente?.primerApellido }} {{ venta.cliente?.segundoApellido }}</p>

                <button class="btn btn-outline-primary mt-3" @click="abrirModalFactura(venta)">
                  Generar Factura
                </button>
                <div class="d-flex justify-content-end gap-2">
                  <button class="btn btn-sm btn-danger" @click="solicitarEliminacion(venta)">Eliminar</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- ALQUILERES -->
      <div class="tab-pane fade" id="alquileres" role="tabpanel">
        <div v-if="alquileres.length === 0" class="text-muted">No hay alquileres registrados.</div>
        <div class="row" v-else>
          <div class="col-12 mb-4" v-for="(alquiler, i) in alquileres" :key="i">
            <div class="card h-100 shadow-sm">
              <div class="card-body">

                <h5 class="card-title text-success">🏪 Alquiler: {{ alquiler.importe }} €</h5>

                <p><strong>Fecha Devolución:</strong> {{ alquiler.fechaHoraDevolucion }}</p>
                <p><strong>Devuelto:</strong> {{ alquiler.fechaHoraDevuelto }}</p>
                <p><strong>Kilómetros:</strong> {{ alquiler.kmDespues - alquiler.kmAntes }} km</p>
                <p><strong>Depósito Inicial:</strong> {{ alquiler.depositoAntes }} €</p>
                <p><strong>Depósito Final:</strong> {{ alquiler.depositoDespues }} €</p>

                <hr />

                <p><strong>🚗 Vehículo:</strong></p>
                <ul>
                  <li><strong>Matrícula:</strong> {{ alquiler.vehiculo?.matricula }}</li>
                  <li><strong>Marca:</strong> {{ alquiler.vehiculo?.marca }}</li>
                  <li><strong>Modelo:</strong> {{ alquiler.vehiculo?.modelo }}</li>
                </ul>
                <hr />
                <p><strong>👤 Cliente:</strong></p>
                <ul>
                  <li><strong>CIF:</strong> {{ alquiler.cliente?.cif }}</li>
                  <li><strong>NOMBRE:</strong> {{ alquiler.cliente?.nombre }}</li>
                </ul>
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
            <br>{{ facturaSeleccionada?.cliente?.direccion }}, Telefono: {{ facturaSeleccionada?.cliente?.telefono }}
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
          <p>Transacción de venta por valor de {{ transaccionEliminando?.importe }} para {{
            transaccionEliminando?.cliente?.cif }} y {{ transaccionEliminando?.vehiculo?.matricula }}</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
          <button type="button" class="btn btn-danger"
            @click="eliminarTransaccion(transaccionEliminando)">Eliminar</button>
        </div>
      </div>
    </div>
  </div>
</template>
