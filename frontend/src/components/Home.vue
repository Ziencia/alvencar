<script>
import { useVehiculoStore } from '@/stores/vehiculoStore';
import { useClienteStore } from '@/stores/clienteStore';
import { useTransaccionStore } from '@/stores/transaccionStore';
import { mapState } from 'pinia';

export default {
    computed: {
        ...mapState(useVehiculoStore, ['vehiculos']),
        ...mapState(useClienteStore, ['clientes']),
        ...mapState(useTransaccionStore, ['ventas', 'alquileres']),

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
        }
    },
    mounted() {
        const vehiculosStore = useVehiculoStore();
        const clientesStore = useClienteStore();
        const transaccionesStore = useTransaccionStore();

        vehiculosStore.cargarVehiculos();
        clientesStore.cargarClientes();
        transaccionesStore.cargarTransacciones();
    }
};
</script>

<template>

    <div class="container mt-5">
        <div class="row g-4">
            <!-- Casilla 1: Total clientes -->
            <div class="col-md-6 col-lg-6">
                <div
                    class="card text-white bg-primary h-100 shadow-sm d-flex flex-column justify-content-center align-items-center">
                    <div class="card-body text-center">
                        <i class="bi bi-person-fill display-4 mb-3"></i>
                        <h4>Total de Clientes</h4>
                        <h2> {{ totalClientes }} </h2>
                    </div>
                </div>
            </div>

            <!-- Casilla 2 -->
            <div class="col-md-6 col-lg-6">
                <div
                    class="card text-white bg-primary h-100 shadow-sm d-flex flex-column justify-content-center align-items-center">
                    <div class="card-body text-center">
                        <i class="bi bi-car-front-fill display-4 mb-3"></i>
                        <h4>Total de Vehículos</h4>
                        <h2>{{ totalVehiculos }}</h2>
                    </div>
                </div>
            </div>

            <!-- Casilla 3 -->
            <div class="col-md-6 col-lg-6">
                <div
                    class="card text-white bg-primary h-100 shadow-sm d-flex flex-column justify-content-center align-items-center">
                    <div class="card-body text-center">
                        <i class="bi bi-piggy-bank-fill display-4 mb-3"></i>
                        <h4>Total de Ventas</h4>
                        <h2> {{  totalVentas }}</h2>
                    </div>
                </div>
            </div>

            <!-- Casilla 4 -->
            <div class="col-md-6 col-lg-6">
                <div
                    class="card text-white bg-primary h-100 shadow-sm d-flex flex-column justify-content-center align-items-center">
                    <div class="card-body text-center">
                        <i class="bi bi-wallet-fill display-4 mb-3"></i>
                        <h4>Total de Alquileres</h4>
                        <h2>{{ totalAlquileres }}</h2>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

