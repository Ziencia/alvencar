package es.mde.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.mde.entidades.OfertaConId;

@RepositoryRestResource(path = "ofertas", itemResourceRel = "oferta", collectionResourceRel = "ofertas")
public interface OfertaDAO extends JpaRepository<OfertaConId, Long> {

}
