package es.mde.repositorios;

import java.util.List;

import es.mde.entidades.TransaccionConId;

public interface TransaccionDAOCustom {
	List<TransaccionConId> getTransaccionesDeMatricula(String matricula);
}
