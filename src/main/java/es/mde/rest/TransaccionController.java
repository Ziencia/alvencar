package es.mde.rest;

import java.util.List;

import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.mde.entidades.TransaccionConId;
import es.mde.repositorios.TransaccionDAO;

@RepositoryRestController
public class TransaccionController {

    private TransaccionDAO transaccionDAO;

    public TransaccionController(TransaccionDAO transaccionDAO) {
        this.transaccionDAO = transaccionDAO;
    }

    @GetMapping("/transacciones/search/por-matricula")
    @ResponseBody
    public CollectionModel<PersistentEntityResource> getTransaccionesVehiculo(
            @RequestParam("matricula") String matricula,
            PersistentEntityResourceAssembler assembler) {

        if (matricula == null || matricula.isBlank()) {
            throw new IllegalArgumentException("La matrícula no puede ser vacía");
        }

        List<TransaccionConId> transacciones = transaccionDAO.getTransaccionesDeMatricula(matricula);

        return assembler.toCollectionModel(transacciones);
    }

    @DeleteMapping("/transacciones/eliminar/{id}")
    public ResponseEntity<?> borrarTransaccion(@PathVariable Long id) {
         return transaccionDAO.findById(id).map(transaccion -> {
            transaccion.setCliente(null);
            transaccion.setVehiculo(null);
            transaccionDAO.save(transaccion);
            transaccionDAO.delete(transaccion);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
