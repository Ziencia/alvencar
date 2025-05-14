package es.mde.repositorios;

import java.util.List;

import es.mde.entidades.Transaccion;

public interface TransaccionDAOCustom {
	List<Transaccion> getTransaccionesDeMatricula(String matricula);
}
