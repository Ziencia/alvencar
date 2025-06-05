<script>
import { getClientes, getVehiculosNoVendidos, postAlquiler, getVehiculoOfertas } from '@/stores/api-service';
import { getFechaHoraActual } from '@/util/Fecha.js';

export default {
    data() {
        return {
            clientes: [],
            vehiculos: [],
            alquiler: {
                clienteId: null,
                vehiculoId: null,
                importe: null,
                fechaHoraEntrega: getFechaHoraActual(),
                fechaHoraDevolucion: null,
                regimen: null,
                kmAntes: null,
                kmDespues: null,
                depositoAntes: null,
                depositoDespues: null
            }
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

        async guardarAlquiler() {
            const clienteURL = this.clientes.find(c => c.id === this.alquiler.clienteId)._links.self.href;
            const vehiculoSeleccionado = this.vehiculos.find(v => v.id === this.alquiler.vehiculoId);
            const vehiculoURL = vehiculoSeleccionado._links.self.href;

            const datos = {
                cliente: clienteURL,
                vehiculo: vehiculoURL,
                importe: this.alquiler.importe,
                fechaHoraEntrega: this.alquiler.fechaHoraEntrega,
                fechaHoraDevolucion: this.alquiler.fechaHoraDevolucion ?? null,
                kmAntes: this.alquiler.kmAntes,
                kmDespues: this.alquiler.kmDespues ?? null,
                depositoAntes: this.alquiler.depositoAntes,
                depositoDespues: this.alquiler.depositoDespues ?? null
                //https://www.luisllamas.es/javascript-operador-coalescencia-nula/
            };

            await postAlquiler(datos);
            this.$router.push('transacciones');
        },

        cancelar() {
            this.$router.push('/transacciones');
        },
    },
    created() {
        this.cargarDatos();
    },
    watch: {
        async 'alquiler.vehiculoId'(buscarIdOfertas) {
            if (!buscarIdOfertas) return;
            try {
                const res = await getVehiculoOfertas(buscarIdOfertas);
                const ofertas = res.data._embedded?.ofertas || [];
                if (ofertas.length > 0) {
                    const ultimaOferta = ofertas.reduce((anterior, actual) =>
                        anterior.id > actual.id ? anterior : actual
                    );
                    this.alquiler.importe = ultimaOferta.ofertaAlquiler;
                } else {
                    this.alquiler.importe = 0;
                }
            } catch (error) {
                this.error('Error con las ofertas:', error);
            }
        }
    }
}
</script>

<template>
    <div class="container mt-4">
        <h3 class="mb-4 text-dark"> 🗂 Alta de un nuevo alquiler</h3>

        <div class="row">
            <div class="col-md-5 mx-auto">
                <form @submit.prevent="guardarAlquiler">
                    <div class="mb-3 col-12">
                        <label class="form-label fw-semibold fs-6">Cliente</label>
                        <select v-model="alquiler.clienteId" class="form-select form-select-m" required>
                            <option v-for="c in clientes" :key="c.id" :value="c.id">
                                {{ c.cif }} - {{ c.nombre }} {{ c.primerApellido }}
                            </option>
                        </select>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-semibold fs-6">Fecha de entrega</label>
                            <input type="datetime-local" v-model="alquiler.fechaHoraEntrega" id="fechahora"
                                class="form-control form-control-m" required />
                        </div>

                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-semibold fs-6">Fecha de devolución</label>
                            <input type="datetime-local" v-model="alquiler.fechaHoraDevolucion" id="fechahora"
                                class="form-control form-control-m"/>
                        </div>
                    </div>

                    <div class="mb-3 col-12">
                        <label class="form-label fw-semibold fs-6">Vehículo</label>
                        <select v-model="alquiler.vehiculoId" class="form-select form-select-m" required>
                            <option v-for="v in vehiculos" :key="v.id" :value="v.id">
                                {{ v.marca }} {{ v.modelo }} ({{ v.matricula }})
                            </option>
                        </select>
                    </div>

                    <div class="d-flex justify-content-end gap-3 mb-3">
                        <div class="col-6">
                            <label class="form-label fw-semibold fs-6 text-end">Importe (€)</label>
                            <input type="number" v-model="alquiler.importe" class="form-control form-control-m text-end"
                                required />
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-semibold fs-6 text-end">Kilómetros iniciales</label>
                            <input type="number" v-model="alquiler.kmAntes" class="form-control form-control-m text-end"
                                required />
                        </div>

                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-semibold fs-6 text-end">Kilometros finales</label>
                            <input type="number" v-model="alquiler.kmDespues"
                                class="form-control form-control-m text-end" />
                        </div>
                    </div>


                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-semibold fs-6 text-end">Depósito inicial</label>
                            <select v-model="alquiler.depositoAntes" class="form-select text-end" required>
                                <option value="" disabled selected>Nivel del deposito</option>
                                <option value="Lleno">Lleno</option>
                                <option value="Medio">Medio</option>
                                <option value="Vacío">Vacío</option>
                            </select>
                        </div>

                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-semibold fs-6 text-end">Depósito final</label>
                            <select v-model="alquiler.depositoDespues" class="form-select text-end">
                                <option value="" disabled selected>Nivel del deposito</option>
                                <option value="Lleno">Igual</option>
                                <option value="Medio">Menos</option>
                            </select>
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