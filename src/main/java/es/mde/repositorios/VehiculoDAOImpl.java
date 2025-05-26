package es.mde.repositorios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import es.mde.entidades.Transaccion;
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

}

