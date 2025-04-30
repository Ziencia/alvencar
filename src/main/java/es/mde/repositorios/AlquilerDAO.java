package es.mde.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//import org.springframework.stereotype.Repository;

import es.mde.entidades.Alquiler;

//@Repository
@RepositoryRestResource(path = "alquileres", itemResourceRel = "alquiler", collectionResourceRel = "alquileres")
public interface AlquilerDAO extends JpaRepository<Alquiler, Long> {

}

