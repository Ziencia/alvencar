package es.mde.repositorios;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.mde.entidades.Vehiculo;

@Component
public class VehiculoListener {
	private Logger log = LoggerFactory.getLogger(VehiculoListener.class);
	private static VehiculoDAO vehiculoDAO;

	@Autowired
	public void init(VehiculoDAO vehiculoDAO) {
		VehiculoListener.vehiculoDAO = vehiculoDAO;
	}

	/**
	 * Crea un Listener que se ejecuta antes de guardar un vehiculo
	 * 
	 * @param vehiculo Vehiculo que se ha guardado
	 */
	@PrePersist
	public void preGuardar(Vehiculo vehiculo) throws Exception {
		boolean condicion = false;
		if (vehiculoDAO.count() != 0) {
			List<Vehiculo> vehiculos = vehiculoDAO.findAll().stream().collect(Collectors.toList());
			//System.err.println("Leyendo lista de vehiculo: Primer vehiculo" + vehiculos.get(0).getMatricula());
		}
		if (condicion) {
			throw new Exception("Se cumple mi condición para no crearse el cliente");
		} else {
			//System.err.println("Se va a guardar un vehiculo: " + vehiculo.getMatricula());
		}
	}

	@PostRemove
	public void postBorrar(Vehiculo vehiculo) {
		//System.err.println("Se ha borrado el vehiculo: " + vehiculo.getMatricula());
	}

	@PostUpdate
	public void postActualizar(Vehiculo vehiculo) {
		//System.err.println("Se ha actualizado el vehiculo: " + vehiculo.getMatricula());
	}

//	@PostLoad
//	public void postGuardar(Cliente cliente) {
//		log.warn("has guardado un cliente: " + cliente.getNombre());
//	}
}

