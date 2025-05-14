<script>
import { getClientes, getVehiculos, postVenta } from '@/stores/api-service';

export default {
    data() {
        return {
            clientes: [],
            vehiculos: [],
            venta: {
                clienteId: null,
                vehiculoId: null,
                importe: 0,
                fechaHoraEntrega: null,
                regimen: null,
                fechaFinGarantia: null
            }
        };
    },
    methods: {
        async cargarDatos() {
            const [resClientes, resVehiculos] = await Promise.all([
                getClientes(),
                getVehiculos()
            ]);
            this.clientes = resClientes.data._embedded.clientes.map(c => ({
                ...c,
                id: c._links.self.href.split('/').pop()
            }));

            this.vehiculos = resVehiculos.data._embedded.vehiculos.map(v => ({
                ...v,
                id: v._links.self.href.split('/').pop()
            }));
        },

        async guardarVenta() {
            const clienteURL = this.clientes.find(c => c.id === this.venta.clienteId)._links.self.href;
            const vehiculoURL = this.vehiculos.find(v => v.id === this.venta.vehiculoId)._links.self.href;

            const datos = {
                cliente: clienteURL,
                vehiculo: vehiculoURL,
                importe: this.venta.importe,
                fechaHoraEntrega: this.venta.fechaHoraEntrega,
                regimen: this.venta.regimen,
                fechaFinGarantia: this.venta.fechaFinGarantia
            };
            await postVenta(datos);
            this.$router.push('/transacciones');
        },
        cancelar() {
            this.$router.push('/transacciones');
        }
    },
    created() {
        this.cargarDatos();
    }
};
</script>
<template>
    <div class="container mt-4">
        <h4 class="text-primary mb-3">📑 Nueva Venta</h4>

        <form @submit.prevent="guardarVenta">
            <div class="row mb-1">
                <div class="col-md-6">
                    <label class="form-label fw-semibold fs-6">Cliente</label>
                    <select v-model="venta.clienteId" class="form-select form-select-sm" required>
                        <option v-for="c in clientes" :key="c.id" :value="c.id">
                            {{ c.cif }} - {{ c.nombre }} {{ c.primerApellido }}
                        </option>
                    </select>
                </div>
            </div>
            <div class="row mb-1">
                <div class="col-md-6">
                    <label class="form-label fw-semibold fs-6">Vehículo</label>
                    <select v-model="venta.vehiculoId" class="form-select form-select-sm" required>
                        <option v-for="v in vehiculos" :key="v.id" :value="v.id">
                            {{ v.marca }} {{ v.modelo }} ({{ v.matricula }})
                        </option>
                    </select>
                </div>
            </div>

            <div class="row mb-2">
                <div class="col-md-4">
                    <label class="form-label fw-semibold fs-6">Importe (€)</label>
                    <input type="number" v-model="venta.importe" class="form-control form-control-sm" required />
                </div>

                <div class="col-md-4">
                    <label class="form-label fw-semibold fs-6">Régimen</label>
                    <select v-model="venta.regimen" class="form-select form-select-sm" required>
                        <option disabled value="">Seleccione un régimen</option>
                        <option value="Adquirido">Adquirido</option>
                        <option value="Leading">Leasing</option>
                    </select>
                </div>
            </div>

            <div class="row mb-2">
                <div class="col-md-4">
                    <label class="form-label fw-semibold fs-6">Fecha de entrega</label>
                    <input type="datetime-local" v-model="venta.fechaHoraEntrega" class="form-control form-control-sm"
                        required />
                </div>
                <div class="col-md-4">
                    <label class="form-label fw-semibold fs-6">Fecha fin de garantía</label>
                    <input type="datetime-local" v-model="venta.fechaFinGarantia" class="form-control form-control-sm"
                        required />
                </div>
            </div>

            <div class="d-flex">
                <button type="submit" class="btn btn-success btn-sm">Guardar</button>
                <button type="button" class="btn btn-secondary btn-sm ms-2" @click="cancelar">Cancelar</button>
            </div>
        </form>
    </div>
</template>