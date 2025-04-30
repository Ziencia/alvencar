package es.mde.rest;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import es.mde.entidades.Transaccion;
import es.mde.repositorios.VehiculoDAO;

@RepositoryRestController
@Configuration
public class VehiculoController {
      
        private VehiculoDAO vehiculoDAO;
    
        public VehiculoController(VehiculoDAO vehiculoDAO) {
            this.vehiculoDAO = vehiculoDAO;
        }
    
        @GetMapping("/vehiculos/{id}/transacciones")
        @ResponseBody
        public CollectionModel<PersistentEntityResource> getTransaccionesDeMatricula(@PathVariable Long id,
        PersistentEntityResourceAssembler assembler) {

		System.err.println("Prueba @Path");
		List<Transaccion> transacciones = vehiculoDAO.getTransaccionVehiculo(id);

		return assembler.toCollectionModel(transacciones);
}}
    

