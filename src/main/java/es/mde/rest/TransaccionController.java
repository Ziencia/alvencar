package es.mde.rest;

import java.util.List;
    
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.mde.entidades.Transaccion;
import es.mde.repositorios.TransaccionDAO;

@RepositoryRestController
public class TransaccionController {
      
        private TransaccionDAO transaccionDAO;
    
        public TransaccionController(TransaccionDAO transaccionDAO) {
            this.transaccionDAO = transaccionDAO;
        }
    
        @GetMapping("/transacciones/search/por-matricula")
        @ResponseBody
        public CollectionModel<PersistentEntityResource> getTransaccionesVehiculo(@RequestParam("matricula") String matricula,
                PersistentEntityResourceAssembler assembler) {

            if (matricula == null || matricula.isBlank()) {
                throw new IllegalArgumentException("La matrícula no puede ser vacía");
            }

            List<Transaccion> transacciones = transaccionDAO.getTransaccionesDeMatricula(matricula);
    
            return assembler.toCollectionModel(transacciones);
        }
}
    

