package es.mde.repositorios;

import es.mde.alvencar.TransaccionImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import es.mde.entidades.AlquilerConId;
import es.mde.entidades.TransaccionConId;
import es.mde.entidades.VehiculoConId;
import es.mde.entidades.VehiculoDTO;
import es.mde.entidades.VentaConId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Transactional(readOnly = true)
public class VehiculoDAOImpl implements VehiculoDAOCustom {

	@Autowired
	VehiculoDAO vehiculoDAO;

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<TransaccionConId> getTransaccionVehiculo(Long id) {
		List<TransaccionConId> transacciones = vehiculoDAO.findById(id).get().getTransacciones().stream().toList();
		return transacciones;
	}

	@Override
	public List<VehiculoDTO> findNoAlquilados(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
		List<VehiculoConId> vehiculosAlquiler = vehiculoDAO.findByVendido(false); // Primer filtro, me quedo solo con los no vendidos
		List<VehiculoConId> vehiculosDisponibles = new ArrayList<>();

		for (VehiculoConId vehiculo : vehiculosAlquiler) {
			boolean aptoAlquiler = true;

			for (TransaccionImpl transaccion : vehiculo.getTransacciones()) {

				if (transaccion instanceof VentaConId) {
					break;
				}

				AlquilerConId alquiler = (AlquilerConId) transaccion;

				LocalDateTime inicioAlquiler = alquiler.getFechaHoraEntrega();
				LocalDateTime finAlquiler = alquiler.getFechaHoraDevolucion();

				boolean noPosibleAlquilar;

				// Sin fecha fin (el alquiler esta abierto)
				if (finAlquiler == null) {
					noPosibleAlquilar = fechaFin == null || !fechaFin.isBefore(inicioAlquiler);
				// Con fecha fin (alquiler cerrado)
				} else {
					if (fechaFin == null) {
						noPosibleAlquilar = !fechaInicio.isAfter(finAlquiler);
					} else {
						boolean iniciaAntesFin = !fechaInicio.isAfter(finAlquiler);
						boolean finDespuesInicio = !fechaFin.isBefore(inicioAlquiler);
						noPosibleAlquilar = iniciaAntesFin && finDespuesInicio;
					}
				}

				if (noPosibleAlquilar) {
					aptoAlquiler = false;
					break;
				}
			}

			if (aptoAlquiler) {
				vehiculosDisponibles.add(vehiculo);
			}
		}

		    List<VehiculoDTO> vehiculosDTO = vehiculosDisponibles.stream()
            .map(VehiculoDTO::new)
            .collect(Collectors.toList());

		return vehiculosDTO;
	}
}
