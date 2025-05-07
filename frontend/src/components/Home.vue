<script>
import { useVehiculoStore } from '@/stores/vehiculoStore';
import { useClienteStore } from '@/stores/clienteStore';
import { storeToRefs } from 'pinia';
import { computed, onMounted } from 'vue';

export default {
    setup() {
        const vehiculosStore = useVehiculoStore();
        const clientesStore = useClienteStore();

        const { vehiculos } = storeToRefs(vehiculosStore);
        const { clientes } = storeToRefs(clientesStore);

        onMounted(() => {
            vehiculosStore.cargarVehiculos();
            clientesStore.cargarClientes();
        });

        const totalVehiculos = computed(() => vehiculos.value.length);
        const totalClientes = computed(() => clientes.value.length);

        return {
            totalVehiculos, totalClientes
        };
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
                        <i class="bi bi-car-front-fill display-4 mb-3"></i>
                        <h4>Total de Transacciones</h4>
                        <h2>??</h2>
                    </div>
                </div>
            </div>

            <!-- Casilla 4 -->
            <div class="col-md-6 col-lg-6">
                <div
                    class="card text-white bg-primary h-100 shadow-sm d-flex flex-column justify-content-center align-items-center">
                    <div class="card-body text-center">
                        <i class="bi bi-car-front-fill display-4 mb-3"></i>
                        <h4>Total de XX</h4>
                        <h2>??</h2>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

