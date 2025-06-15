package es.mde.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import es.mde.entidades.VehiculoConId;

@RepositoryRestResource(path = "vehiculos", itemResourceRel = "vehiculo", collectionResourceRel = "vehiculos")
public interface VehiculoDAO extends JpaRepository<VehiculoConId, Long>, VehiculoDAOCustom {
  
    @RestResource(path = "marca")
    List<VehiculoConId> findByMarca(String marca);

    @RestResource(path = "matricula")
    List<VehiculoConId> findByMatriculaContaining(String matricula);

    @RestResource(path = "vendido")
    List<VehiculoConId> findByVendido(boolean vendido);

    @Query("SELECT ve FROM VehiculoConId ve WHERE ve.vendido = false AND ve.condicionAdquisicion = 'Compra'") 
    @RestResource(path = "venta")
    List<VehiculoConId> findCompradosAptosVenta();
}
