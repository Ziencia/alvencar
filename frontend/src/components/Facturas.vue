<script>
import { useFacturaStore } from '@/stores/facturaStore';
import dayjs from 'dayjs';

export default {
  data() {
    return {
      facturaSeleccionada: null,
      filtro: 'todas'
    };
  },
  computed: {
    facturaStore() {
      return useFacturaStore();
    },
    facturas() {
      const todas = this.facturaStore.facturas || [];
      if (this.filtro === 'pagadas') return todas.filter(f => f.estaPagada);
      if (this.filtro === 'noPagadas') return todas.filter(f => !f.estaPagada);
      return todas;
    },
    cargando() {
      return this.facturaStore.cargando;
    },
    error() {
      return this.facturaStore.error;
    }
  },
  methods: {
    formatearFecha(fechaISO) {
      return dayjs(fechaISO).format('DD/MM/YYYY HH:mm');
    },
    extraerIdDesdeUrl(url) {
      return url.split('/').pop();
    },
    async marcarComoPagada(factura) {
      const facturaActualizada = { ...factura, estaPagada: !factura.estaPagada };
      try {
        await this.facturaStore.actualizarFactura(facturaActualizada);
        await this.facturaStore.cargarFacturas();
      } catch (error) {
        console.error('Error al cambiar el estado de la factura:', error);
      }
    },
    imprimirFactura(factura) {
      const printWindow = window.open('', '_blank');
      if (!printWindow) return;

      printWindow.document.write(`
        <html>
          <head>
            <title>Factura</title>
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
          </head>
          <body class="p-4">
            <h2>${factura.conceptoFactura}</h2>
            <p><strong>Nº de factura:</strong> ${factura.id}</p>
            <p><strong>Cliente:</strong> ${factura.nombreApellidosDNI}</p>
            <p><strong>Dirección:</strong> ${factura.datosDireccionLocalizacion}</p>
            <p><strong>Vehículo:</strong> ${factura.datosVehiculo}</p>
            <p><strong>Importe:</strong> ${factura.importe} €</p>
            <p><strong>Impuestos:</strong> ${factura.impuestos * 100}%</p>
            <p><strong>Total:</strong> ${factura.importeTotal} €</p>
            <p><strong>Fecha:</strong> ${this.formatearFecha(factura.fechaFactura)}</p>
            <p><strong>Pagada:</strong> ${factura.estaPagada ? 'Sí' : 'No'}</p>
            <script>
              window.onload = function() {
                window.print();
                window.onafterprint = function() { window.close(); };
              };
            <\/script>
          </body>
        </html>
      `);
      printWindow.document.close();
    }
  },
  mounted() {
    this.facturaStore.cargarFacturas();
  }
};

</script>

<template>
  <div class="container mt-4">
    <h2 class="mb-4 text-primary">🧾 Listado de Facturas</h2>

    <!-- Mensajes de carga y error -->
    <div v-if="cargando" class="alert alert-info">Cargando facturas...</div>
    <div v-if="error" class="alert alert-danger">{{ error }}</div>

    <!-- Conteo -->
    <div class="mb-3">
      <strong>Facturas cargadas:</strong> {{ facturas.length }}
    </div>

    <div v-if="cargando" class="alert alert-info">Cargando facturas...</div>
    <div v-if="error" class="alert alert-danger">{{ error }}</div>
    <div class="d-flex justify-content-end mb-4">
      <div class="btn-group mb-4" role="group" aria-label="FiltroFacturas">
        <button type="button" class="btn btn-primary" @click="filtro = 'todas'">Todas</button>
        <button type="button" class="btn btn-success" @click="filtro = 'pagadas'">Pagadas</button>
        <button type="button" class="btn btn-danger" @click="filtro = 'noPagadas'">No pagadas</button>
      </div>
    </div>
    <div v-if="facturas.length === 0" class="text-muted"><strong>No hay facturas registradas.</strong></div>
    <div class="row" v-if="facturas.length">
      <div class="col-12 mb-4" v-for="factura in facturas" :key="factura.id">
        <div class="card shadow-sm w-100" :class="factura.estaPagada ? 'border-success' : 'border-danger'">
          <div class="card-body" :class="factura.estaPagada ? 'bg-white' : 'bg-danger-subtle'">
            <h5 class="card-title" :class="factura.estaPagada ? 'text-success' : 'text-danger'">
              {{ factura.conceptoFactura }}
            </h5>
            <p><strong>Nº de factura:</strong> {{ extraerIdDesdeUrl(factura._links?.self?.href || '') }}</p>
            <p><strong>Cliente:</strong> {{ factura.nombreApellidosDNI }}</p>
            <p><strong>Dirección:</strong> {{ factura.datosDireccionLocalizacion }}</p>
            <p><strong>Vehículo:</strong> {{ factura.datosVehiculo }}</p>
            <p><strong>Importe:</strong> {{ factura.importe }} €</p>
            <p><strong>Impuestos:</strong> {{ factura.impuestos * 100 }}%</p>
            <p><strong>Total:</strong> {{ factura.importeTotal }} €</p>
            <p><strong>Fecha:</strong> {{ formatearFecha(factura.fechaFactura) }}</p>
            <p><strong>Pagada:</strong> {{ factura.estaPagada ? 'Sí' : 'No' }}</p>
            <div class="d-flex justify-content-end gap-2 mt-3">
              <button class="btn btn-primary mt-3" @click="marcarComoPagada(factura)">
                {{ factura.estaPagada ? 'Marcar como No Pagada' : 'Marcar como Pagada' }}
              </button>
              <button class="btn btn-secondary mt-3" @click="imprimirFactura(factura)">
                🖨️ Imprimir
              </button>

            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>