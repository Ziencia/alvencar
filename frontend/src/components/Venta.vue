<script>
import { getClientes, getVehiculosNoVendidos, postVenta, updateVehiculo, getVehiculoOfertas } from '@/stores/api-service';

export default {
    data() {
        return {
            clientes: [],
            vehiculos: [],
            venta: {
                clienteId: null,
                vehiculoId: null,
                importe: null,
                fechaHoraEntrega: this.getFechaHoraActual(),
                regimen: null
            }
        };
    },
    computed: {
        fechaFinGarantia() {
            if (!this.venta.fechaHoraEntrega) return null;
            const fecha = new Date(this.venta.fechaHoraEntrega);
            fecha.setFullYear(fecha.getFullYear() + 3);
            return fecha.toISOString().slice(0, 16);
        }
    },
    methods: {
        async cargarDatos() {
            const [resClientes, resVehiculos] = await Promise.all([
                getClientes(),
                getVehiculosNoVendidos()
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
            const vehiculoSeleccionado = this.vehiculos.find(v => v.id === this.venta.vehiculoId);
            const vehiculoURL = vehiculoSeleccionado._links.self.href;

            const datos = {
                cliente: clienteURL,
                vehiculo: vehiculoURL,
                importe: this.venta.importe,
                fechaHoraEntrega: this.venta.fechaHoraEntrega,
                regimen: "Compra",
                fechaFinGarantia: this.fechaFinGarantia
            };
            await postVenta(datos);

            const vehiculoVendido = {
                ...vehiculoSeleccionado,
                vendido: true
            };
            await updateVehiculo(vehiculoURL,vehiculoVendido);
            this.$router.push('/transacciones');
        },
        cancelar() {
            this.$router.push('/transacciones');
        },
        //https://community.esri.com/t5/arcgis-javascript-maps-sdk-questions/javascript-date-now-in-local-time/m-p/494668#M45957
        getFechaHoraActual() {
            const actual = new Date();
            const diferencia = actual.getTimezoneOffset();
            const local = new Date(actual.getTime() - diferencia * 60 * 1000);
            return local.toISOString().slice(0, 16);
        }
    },
    created() {
        this.cargarDatos();
    },
    // Uso de watchers https://vuejs.org/guide/essentials/watchers.html
    watch: {
        async 'venta.vehiculoId'(buscarIdOfertas) {
            if (!buscarIdOfertas) return;
            try {
                const res = await getVehiculoOfertas(buscarIdOfertas);
                const ofertas = res.data._embedded?.ofertas || [];
                if (ofertas.length > 0) {
                    const ultimaOferta = ofertas.reduce((anterior, actual) =>
                        anterior.id > actual.id ? anterior : actual
                    );
                    this.venta.importe = ultimaOferta.ofertaVenta;
                } else {
                    this.venta.importe = 0;
                }
            } catch (error) {
                console.error('Error con las ofertas:', error);
            }
        }
    }
};
</script>
<template>
    <div class="container mt-4">
        <h3 class="mb-4 text-dark">📑 Alta de una nueva venta</h3>

        <div class="row">
            <div class="col-md-5 mx-auto">
                <form @submit.prevent="guardarVenta">

                    <div class="mb-3 col-12">
                        <label class="form-label fw-semibold fs-6">Cliente</label>
                        <select v-model="venta.clienteId" class="form-select form-select-m" required>
                            <option v-for="c in clientes" :key="c.id" :value="c.id">
                                {{ c.cif }} - {{ c.nombre }} {{ c.primerApellido }}
                            </option>
                        </select>
                    </div>

                    <div class="mb-3 col-12">
                        <label class="form-label fw-semibold fs-6">Vehículo</label>
                        <select v-model="venta.vehiculoId" class="form-select form-select-m" required>
                            <option v-for="v in vehiculos" :key="v.id" :value="v.id">
                                {{ v.marca }} {{ v.modelo }} ({{ v.matricula }})
                            </option>
                        </select>
                    </div>

                    <div class="d-flex justify-content-end gap-3 mb-3">
                        <div class="col-6">
                            <label class="form-label fw-semibold fs-6 text-end">Importe (€)</label>
                            <input type="number" v-model="venta.importe" class="form-control form-control-m text-end"
                                required />
                        </div>
                    </div>

                    <div class="d-flex justify-content-end gap-3 mb-3">
                        <div class="col-6">
                            <label class="form-label fw-semibold fs-6">Fecha de entrega</label>
                            <input type="datetime-local" v-model="venta.fechaHoraEntrega" id="fechahora"
                                class="form-control form-control-m" required />
                        </div>
                    </div>

                    <div class="text-end mt-5">
                        <button type="button" class="btn btn-secondary btn-l" @click="cancelar">Cancelar</button>
                        <button type="submit" class="btn btn-success btn-l ms-2">Guardar</button>
                    </div>

                </form>
            </div>
        </div>
    </div>
</template>