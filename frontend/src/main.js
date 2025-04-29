import { createApp } from 'vue'

import App from '@/App.vue'

import Home from '@/components/Home.vue'

import { createRouter, createWebHashHistory } from "vue-router";

// Import our custom CSS
import '@/scss/styles.scss'

// Import all of Bootstrap's JS
import * as bootstrap from 'bootstrap'

import PrimeVue from 'primevue/config'
import Aura from '@primeuix/themes/aura';


const NotFound = () => import('@/components/NotFound.vue');
const Clientes = () => import('@/components/Clientes.vue');
const Vehiculos = () => import('@/components/Vehiculos.vue');
const Transacciones = () => import('@/components/Transacciones.vue');

const routes = [
    { path: '/', redirect: '/home' },
    { path: '/home', component: Home, name: 'home'},
    { path: '/clientes', component: Clientes, name: 'clientes'},
    { path: '/vehiculos', component: Vehiculos, name: 'vehiculos'},
    { path: '/transacciones', component: Transacciones, name: 'transacciones'},
    { path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFound },
  ];

  const router = createRouter({
    history: createWebHashHistory(),
    routes,
  });

const app = createApp(App)
  
app.use(router) 
app.use(PrimeVue, {
    theme: {
        preset: Aura
    }
});
app.mount('#app')
  
