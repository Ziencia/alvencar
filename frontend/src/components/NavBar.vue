<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark py-3">
    <div class="container-fluid">
      <router-link class="navbar-brand fs-4 d-flex align-items-center" to="/home">
        <i class="bi bi-house-door-fill me-2"></i> INICIO
      </router-link>

      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav ms-auto mb-2 mb-lg-0 fs-5">
          <li class="nav-item">
            <router-link class="nav-link d-flex align-items-center" to="/clientes">
              <i class="bi bi-people-fill me-2"></i> Clientes
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link d-flex align-items-center" to="/vehiculos">
              <i class="bi bi-truck-front-fill me-2"></i> Vehículos
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link d-flex align-items-center" to="/transacciones">
              <i class="bi bi-cash-stack me-2"></i> Transacciones
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>
