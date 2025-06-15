package es.mde.rest;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.mde.entidades.TransaccionConId;
import es.mde.entidades.VehiculoDTO;
import es.mde.repositorios.VehiculoDAO;

@RestController
@Configuration
@RequestMapping("/api")
public class VehiculoController {

    private VehiculoDAO vehiculoDAO;

    public VehiculoController(VehiculoDAO vehiculoDAO) {
        this.vehiculoDAO = vehiculoDAO;
    }

    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/format/annotation/DateTimeFormat.ISO.html
    @GetMapping("/vehiculos/alquiler")
    public List<VehiculoDTO> getVehiculosAptosAlquiler(
            @RequestParam("inicio") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(value = "fin", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime fin) {
        return vehiculoDAO.findNoAlquilados(inicio, fin);
    }

    @GetMapping("/vehiculos/id/{id}/transacciones")
    @ResponseBody
    public CollectionModel<PersistentEntityResource> getTransaccionesDeMatricula(@PathVariable Long id,
            PersistentEntityResourceAssembler assembler) {

        System.err.println("Prueba @Path");
        List<TransaccionConId> transacciones = vehiculoDAO.getTransaccionVehiculo(id);

        return assembler.toCollectionModel(transacciones);

    }

}
