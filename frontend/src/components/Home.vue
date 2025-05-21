<script>
import { useVehiculoStore } from '@/stores/vehiculoStore';
import { useClienteStore } from '@/stores/clienteStore';
import { useTransaccionStore } from '@/stores/transaccionStore';
import { useFacturaStore } from '@/stores/facturaStore';
import { mapState } from 'pinia';

export default {
    computed: {
        ...mapState(useVehiculoStore, ['vehiculos']),
        ...mapState(useClienteStore, ['clientes']),
        ...mapState(useTransaccionStore, ['ventas', 'alquileres']),
        ...mapState(useFacturaStore, ['facturas']),

        totalVehiculos() {
            return this.vehiculos.length;
        },
        totalClientes() {
            return this.clientes.length;
        },
        totalVentas() {
            return this.ventas.length;
        },
        totalAlquileres() {
            return this.alquileres.length;
        },
        totalFacturas() {
            return this.facturas.length;
        }
    },
    mounted() {
        const vehiculosStore = useVehiculoStore();
        const clientesStore = useClienteStore();
        const transaccionesStore = useTransaccionStore();
        const facturaStore = useFacturaStore();

        vehiculosStore.cargarVehiculos();
        clientesStore.cargarClientes();
        transaccionesStore.cargarTransacciones();
        facturaStore.cargarFacturas();
    }
};
</script>

<template>
    <div class="container mt-5 mb-5 bg-navbar-dark">
        <div class="row g-4">
            <!-- Casilla-->
            <div class="col-md-3 col-lg-4">
                <div
                    class="card text-white h-100 shadow-sm d-flex flex-column justify-content-center align-items-center ">
                    <div class="card-body text-center">
                        <i class="bi bi-person-fill display-4 mb-3"></i>
                        <h4>Total de Clientes</h4>
                        <h2> {{ totalClientes }} </h2>
                    </div>
                </div>
            </div>

            <!-- Casilla 2 -->
            <div class="col-md-4 col-lg-4">
                <div
                    class="card text-white h-100 shadow-sm d-flex flex-column justify-content-center align-items-center">
                    <div class="card-body text-center">
                        <i class="bi bi-car-front-fill display-4 mb-3"></i>
                        <h4>Total de Vehículos</h4>
                        <h2>{{ totalVehiculos }}</h2>
                    </div>
                </div>
            </div>

            <!-- Casilla 3 -->
            <div class="col-md-4 col-lg-4">
                <div
                    class="card text-white h-100 shadow-sm d-flex flex-column justify-content-center align-items-center">
                    <div class="card-body text-center">
                        <i class="bi bi-piggy-bank-fill display-4 mb-3"></i>
                        <h4>Total de Ventas</h4>
                        <h2> {{ totalVentas }}</h2>
                    </div>
                </div>
            </div>

            <!-- Casilla 4 -->
            <div class="col-md-4 col-lg-4">
                <div
                    class="card text-white h-100 shadow-sm d-flex flex-column justify-content-center align-items-center">
                    <div class="card-body text-center">
                        <i class="bi bi-wallet-fill display-4 mb-3"></i>
                        <h4>Total de Alquileres</h4>
                        <h2>{{ totalAlquileres }}</h2>
                    </div>
                </div>
            </div>

            <!-- Casilla 5 -->
            <div class="col-md-4 col-lg-4">
                <div
                    class="card text-white h-100 shadow-sm d-flex flex-column justify-content-center align-items-center">
                    <div class="card-body text-center">
                        <i class="bi bi-currency-euro display-4 mb-3"></i>
                        <h4>Total de Facturas</h4>
                        <h2>{{ totalFacturas }}</h2>
                    </div>
                </div>
            </div>

            <!-- Casilla 6 -->
            <div class="col-md-4 col-lg-4">
                <div
                    class="card text-white  h-100 shadow-sm d-flex flex-column justify-content-center align-items-center">
                    <div class="card-body text-center">
                        <i class="bi bi-info-circle display-4 mb-3"></i>
                        <h4>Info:</h4>
                        <p>V 1.0 Sprint Goal 1, 14/05/25</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.card{
    background-color: #7A6263;
}
</style>