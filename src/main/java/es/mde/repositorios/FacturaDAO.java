package es.mde.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//import org.springframework.stereotype.Repository;

import es.mde.entidades.FacturaConId;

//@Repository
@RepositoryRestResource(path = "facturas", itemResourceRel = "factura", collectionResourceRel = "facturas")
public interface FacturaDAO extends JpaRepository<FacturaConId, Long> {

}
