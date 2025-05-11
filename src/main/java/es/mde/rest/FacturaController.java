package es.mde.rest;

import java.util.Optional;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import es.mde.entidades.Factura;
import es.mde.repositorios.FacturaDAO;

@RepositoryRestController
public class FacturaController {

    private FacturaDAO facturaDAO;

    public FacturaController(FacturaDAO facturaDAO) {
        this.facturaDAO = facturaDAO;
    }

    @PutMapping("/facturas/{id}")
    public ResponseEntity<Factura> actualizarFactura(@PathVariable Long id, @RequestBody Factura factura) {
        Optional<Factura> facturaExistente = facturaDAO.findById(id);
        if (facturaExistente.isPresent()) {
            Factura facturaActualizar = facturaExistente.get();
            facturaActualizar.setEstaPagada(factura.isEstaPagada());
            Factura facturaGuardada = facturaDAO.save(facturaActualizar);
            return ResponseEntity.ok(facturaGuardada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
