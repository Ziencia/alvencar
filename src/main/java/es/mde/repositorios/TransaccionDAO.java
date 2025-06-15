package es.mde.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.mde.entidades.TransaccionConId;
import java.util.List;


//@Repository
@RepositoryRestResource(path = "transacciones", itemResourceRel = "transaccion", collectionResourceRel = "transacciones")
public interface TransaccionDAO extends JpaRepository<TransaccionConId, Long>, TransaccionDAOCustom {

    List<TransaccionConId> findByImporte(float importe);
}

