package es.mde.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//import org.springframework.stereotype.Repository;

import es.mde.entidades.ClienteConId;

//@Repository
@RepositoryRestResource(path = "clientes", itemResourceRel = "cliente", collectionResourceRel = "clientes")
public interface ClienteDAO extends JpaRepository<ClienteConId, Long> {

}

