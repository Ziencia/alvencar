<script>
export default {
  name: 'FacturaVenta',
  props: {
    factura: {
      type: Object,
      required: true
    },
    formatoMoneda: {
      type: Function,
      required: true
    },
    extraerIdDesdeUrl: {
      type: Function,
      required: true
    },
    formatearFecha: {
      type: Function,
      required: true
    }
  }
};
</script>

<template>
  <div class="list-group list-group-flush border rounded mb-2"
    :class="factura.estaPagada ? 'border-success' : 'border-danger'">
    <div class="list-group-item d-flex flex-column flex-md-row justify-content-between align-items-start"
      :class="factura.estaPagada ? '' : 'bg-danger-subtle'">
      <div class="flex-grow-1">
        <h6 class="mb-1" :class="factura.estaPagada ? 'text-success' : 'text-danger'">
          {{ factura.conceptoFactura }}: {{ factura.datosVehiculo }}
        </h6>
        <small class="text-muted">
          Nº {{ extraerIdDesdeUrl(factura._links?.self?.href || '') }} |
          Cliente: {{ factura.nombreApellidosDNI }} |
          Fecha: {{ formatearFecha(factura.fechaFactura) }}
        </small>

        <div class="small mt-1 text-muted">
          Dirección: {{ factura.datosDireccionLocalizacion }}
        </div>
        <div class="small mt-1 text-muted">
          Importe de venta: {{ formatoMoneda(factura.importe) }}
        </div>
      </div>

      <div class="text-end mt-2 mt-md-0">
        <div><strong>{{ formatoMoneda(factura.importeTotal) }}</strong></div>
        <div class="mt-1">
          <slot></slot>
        </div>
      </div>
    </div>
  </div>
</template>
