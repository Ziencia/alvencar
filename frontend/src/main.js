import { createApp } from 'vue'

import App from '@/App.vue'
import Home from '@/components/Home.vue'

import { createPinia } from 'pinia'
import { createRouter, createWebHashHistory } from "vue-router";

// Import our custom CSS
import '@/scss/styles.scss'

// Import all of Bootstrap's JS
import * as bootstrap from 'bootstrap'
import 'bootstrap-icons/font/bootstrap-icons.css';

import PrimeVue from 'primevue/config'
import Aura from '@primeuix/themes/aura';

// Para el uso de los modales de formularios
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap';

const NotFound = () => import('@/components/NotFound.vue');
const Clientes = () => import('@/components/Clientes.vue');
const Vehiculos = () => import('@/components/Vehiculos.vue');
const Transacciones = () => import('@/components/Transacciones.vue');
const Venta = () => import('@/components/Venta.vue');
const Alquiler = () => import('@/components/Alquiler.vue');
const Factura = () => import('@/components/Facturas.vue');
const Oferta = () => import('@/components/Ofertas.vue');


const routes = [
    { path: '/', component: Home, name: 'home'},
    { path: '/clientes', component: Clientes, name: 'clientes'},
    { path: '/vehiculos', component: Vehiculos, name: 'vehiculos'},
    { path: '/transacciones', component: Transacciones, name: 'transacciones'},
    { path: '/venta', component: Venta, name: 'venta'},
    { path: '/alquiler', component: Alquiler, name: 'alquiler'},
    { path: '/facturas', component: Factura, name: 'factura'},
    { path: '/ofertas', component: Oferta, name: 'oferta'},
    { path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFound },
  ];

  const router = createRouter({
    history: createWebHashHistory(),
    routes,
  });

const pinia = createPinia()
const app = createApp(App)
  
app.use(router) 
app.use(PrimeVue, {
    theme: {
        preset: Aura
    }
});
app.use(pinia)
app.mount('#app')
  
