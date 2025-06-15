package es.mde.rest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.mde.entidades.FacturaConId;
import es.mde.repositorios.FacturaDAO;
import es.mde.service.PdfService;

@RestController
public class PdfController {

    private FacturaDAO facturaDAO;
    private final PdfService pdfService;

    public PdfController(PdfService pdfService, FacturaDAO facturaDAO) {
        this.pdfService = pdfService;
        this.facturaDAO = facturaDAO;
    }

    @GetMapping("/api/generarfactura/{id}")
    public ResponseEntity<ByteArrayResource> generatePdf(@PathVariable Long id) {
        FacturaConId factura = facturaDAO.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay factura"));

        byte[] pdfBytes = pdfService.generarPdf(factura);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "factura.pdf");
        headers.setContentLength(pdfBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
// https://multigenesys.com/blog/advanced-pdf-generation-with-java-spring-boot-and-itext-a-comprehensive-guide