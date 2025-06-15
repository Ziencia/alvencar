package es.mde.repositorios;

import java.time.LocalDateTime;
import java.util.List;

import es.mde.entidades.TransaccionConId;
import es.mde.entidades.VehiculoDTO;

public interface VehiculoDAOCustom {
    List<TransaccionConId> getTransaccionVehiculo(Long id);
    List<VehiculoDTO> findNoAlquilados(LocalDateTime inicioAlquiler, LocalDateTime finAlquiler);
}
