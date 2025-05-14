package es.mde.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//import org.springframework.stereotype.Repository;

import es.mde.entidades.Venta;

//@Repository
@RepositoryRestResource(path = "ventas", itemResourceRel = "venta", collectionResourceRel = "ventas")
public interface VentaDAO extends JpaRepository<Venta, Long> {

}

