package es.mde.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import es.mde.entidades.Vehiculo;


//@Repository
@RepositoryRestResource(path = "vehiculos", itemResourceRel = "vehiculo", collectionResourceRel = "vehiculos")
public interface VehiculoDAO extends JpaRepository<Vehiculo, Long>, VehiculoDAOCustom {
  
    @RestResource(path = "marca")
    List<Vehiculo> findByMarca(String marca);

    @RestResource(path = "matricula")
    List<Vehiculo> findByMatriculaContaining(String matricula);

}
