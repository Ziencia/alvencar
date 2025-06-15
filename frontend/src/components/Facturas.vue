<script>
import { useFacturaStore } from '@/stores/facturaStore';
import { generarfactura } from '@/stores/api-service';
import dayjs from 'dayjs';
import { formatoMoneda } from '@/util/Numero.js';
import FacturaVenta from '@/components/FacturaVenta.vue';
import FacturaAlquiler from '@/components/FacturaAlquiler.vue';

export default {
  data() {
    return {
      facturaSeleccionada: null,
      filtro: 'todas',
      filtroDni: '',
      filtroMatricula: '',
    };
  },
  components: {
    FacturaVenta,
    FacturaAlquiler
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
    },
    facturasFiltradas() {
      return this.facturas.filter(f =>
        (f?.nombreApellidosDNI || '').toLowerCase().includes(this.filtroDni.toLowerCase()) &&
        (f?.datosVehiculo || '').toLowerCase().includes(this.filtroMatricula.toLowerCase())
    );
    },
  },
  methods: {
    formatearFecha(fechaISO) {
      return dayjs(fechaISO).format('DD/MM/YYYY HH:mm');
    },
    formatoMoneda(numero) {
      return formatoMoneda(numero);
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
        this.error('Error al cambiar el estado de la factura:', error);
      }
    },
    async imprimirFactura(factura) {
      try {
        const id = this.extraerIdDesdeUrl(factura._links?.self?.href);
        const response = await generarfactura(id);
        const blob = response.data;

        //https://developer.mozilla.org/en-US/docs/Web/API/URL/createObjectURL_static
        const url = window.URL.createObjectURL(blob);
        const descarga = document.createElement('a');
        descarga.href = url;
        descarga.download = `factura_#${id}.pdf`;
        descarga.click();

      } catch (error) {
        this.error("Error al generar la factura:", error);
      }
    },
    getTipoFactura(tipo) {
      switch (tipo) {
        case 0: return FacturaAlquiler;
        case 1: return FacturaVenta;
        default: return null;
      }
    },
    limpiarFiltros() {
      this.filtroDni = '';
      this.filtroMatricula = '';
    }
  },
  mounted() {
    this.facturaStore.cargarFacturas();      
  }
};

</script>

<template>
  <div class="container mt-4">
    <h3 class="mb-4 text-dark">
      📚 Listado de facturas
      <small class="text-muted">(Actualmente hay {{ facturas.length }} facturas)</small>
    </h3>

    <div v-if="error" class="alert alert-danger">{{ error }}</div>

    <div class="d-flex justify-content-end mb-4">
      <div class="btn-group btn-group-sm" role="group" aria-label="Filtro de facturas">
        <input type="radio" class="btn-check" name="filtroFacturas" id="filtroTodas" autocomplete="off" value="todas"
          v-model="filtro" />
        <label class="btn btn-outline-primary rounded-pill" for="filtroTodas">Todas</label>

        <input type="radio" class="btn-check" name="filtroFacturas" id="filtroPagadas" autocomplete="off"
          value="pagadas" v-model="filtro" />
        <label class="btn btn-outline-success rounded-pill" for="filtroPagadas">Pagadas</label>

        <input type="radio" class="btn-check" name="filtroFacturas" id="filtroNoPagadas" autocomplete="off"
          value="noPagadas" v-model="filtro" />
        <label class="btn btn-outline-danger rounded-pill" for="filtroNoPagadas">No Pagadas</label>
      </div>
    </div>

    <div class="row justify-content-center mb-3">
      <div class="col-auto">
        <div class="input-group">
          <input v-model="filtroDni" type="text" class="form-control form-control" placeholder="Buscar por DNI" />
          <input v-model="filtroMatricula" type="text" class="form-control form-control"
            placeholder="Buscar por matrícula" />
          <button @click="limpiarFiltros" class="btn btn-outline-secondary btn" title="Limpiar filtros">
            <i class="bi bi-x-circle-fill"></i>
          </button>
        </div>
      </div>
    </div>

    <div v-if="facturas.length === 0" class="text-muted"><strong>No hay facturas registradas.</strong></div>


    <div class="row" v-if="facturas.length">
      <div class="col-8 mb-4 mx-auto" v-for="factura in facturasFiltradas" :key="factura.id">
        <component :is="getTipoFactura(factura.tipoFactura)" :factura="factura" :formatoMoneda="formatoMoneda"
          :extraerIdDesdeUrl="extraerIdDesdeUrl" :formatearFecha="formatearFecha">

          <div class="d-flex justify-content-end gap-2 mt-3">
            <button class="btn btn-primary mt-3 btn-sm" @click="marcarComoPagada(factura)">
              {{ factura.estaPagada ? 'Anular pago' : 'Confirmar pago' }}
            </button>
            <button class="btn btn-secondary mt-3 btn-sm" @click="imprimirFactura(factura)">
              🖨️ Imprimir
            </button>
          </div>
        </component>
      </div>
    </div>
  </div>

</template>