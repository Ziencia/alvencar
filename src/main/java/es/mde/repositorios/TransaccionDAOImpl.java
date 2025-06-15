package es.mde.repositorios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import es.mde.entidades.TransaccionConId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Transactional(readOnly = true)
public class TransaccionDAOImpl implements TransaccionDAOCustom {

	@Autowired
	VehiculoDAO vehiculoDAO;

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<TransaccionConId> getTransaccionesDeMatricula(String matricula) {

		List<TransaccionConId> transacciones = new ArrayList<TransaccionConId>();
//		clienteDAO.findByCorreoContaining(tipo).forEach(c -> productos.addAll(c.getProductos()));
		vehiculoDAO.findByMatriculaContaining(matricula).forEach(v -> transacciones.addAll(v.getTransacciones()));
		return transacciones;
	}

}

