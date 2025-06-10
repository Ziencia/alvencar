package es.mde.repositorios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import es.mde.entidades.Alquiler;
import es.mde.entidades.Transaccion;
import es.mde.entidades.Vehiculo;
import es.mde.entidades.VehiculoDTO;
import es.mde.entidades.Venta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Transactional(readOnly = true)
public class VehiculoDAOImpl implements VehiculoDAOCustom {

	@Autowired
	VehiculoDAO vehiculoDAO;

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<Transaccion> getTransaccionVehiculo(Long id) {
		List<Transaccion> transacciones = vehiculoDAO.findById(id).get().getTransacciones().stream().toList();
		return transacciones;
	}

	@Override
	public List<VehiculoDTO> findNoAlquilados(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
		List<Vehiculo> vehiculosAlquiler = vehiculoDAO.findByVendido(false); // Primer filtro, me quedo solo con los no vendidos
		List<Vehiculo> vehiculosDisponibles = new ArrayList<>();

		for (Vehiculo vehiculo : vehiculosAlquiler) {
			boolean aptoAlquiler = true;

			for (Transaccion transaccion : vehiculo.getTransacciones()) {

				if (transaccion instanceof Venta) {
					break;
				}

				Alquiler alquiler = (Alquiler) transaccion;

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
