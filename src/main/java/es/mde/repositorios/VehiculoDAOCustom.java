package es.mde.repositorios;

import java.util.List;

import es.mde.entidades.Transaccion;

public interface VehiculoDAOCustom {
    List<Transaccion> getTransaccionVehiculo(Long id);
}
