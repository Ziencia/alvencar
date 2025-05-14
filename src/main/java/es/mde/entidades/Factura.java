package es.mde.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "FACTURAS")

public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    private String conceptoFactura;
    private String nombreApellidosDNI;
    private String datosDireccionLocalizacion;
    private String datosVehiculo;
    private float importe;
    private float importeTotal;
    private float impuestos;
    private LocalDateTime fechaFactura;
    private boolean estaPagada;

    public Factura() {
    }

    public Factura(String conceptoFactura, String nombreApellidosDNI, String datosDireccionLocalizacion,
            String datosVehiculo, float importe, float importeTotal, float impuestos, LocalDateTime fechaFactura,
            boolean estaPagada) {
        this.conceptoFactura = conceptoFactura;
        this.nombreApellidosDNI = nombreApellidosDNI;
        this.datosDireccionLocalizacion = datosDireccionLocalizacion;
        this.datosVehiculo = datosVehiculo;
        this.importe = importe;
        this.importeTotal = importeTotal;
        this.impuestos = impuestos;
        this.fechaFactura = fechaFactura;
        this.estaPagada = estaPagada;
    }

    public Long getId() {
        return id;
    }

    public String getConceptoFactura() {
        return conceptoFactura;
    }

    public void setConceptoFactura(String conceptoFactura) {
        this.conceptoFactura = conceptoFactura;
    }

    public String getNombreApellidosDNI() {
        return nombreApellidosDNI;
    }

    public void setNombreApellidosDNI(String nombreApellidosDNI) {
        this.nombreApellidosDNI = nombreApellidosDNI;
    }

    public String getDatosDireccionLocalizacion() {
        return datosDireccionLocalizacion;
    }

    public void setDatosDireccionLocalizacion(String datosDireccionLocalizacion) {
        this.datosDireccionLocalizacion = datosDireccionLocalizacion;
    }

    public String getDatosVehiculo() {
        return datosVehiculo;
    }

    public void setDatosVehiculo(String datosVehiculo) {
        this.datosVehiculo = datosVehiculo;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    public float getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(float importeTotal) {
        this.importeTotal = importeTotal;
    }

    public float getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(float impuestos) {
        this.impuestos = impuestos;
    }

    public LocalDateTime getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(LocalDateTime fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public boolean isEstaPagada() {
        return estaPagada;
    }

    public void setEstaPagada(boolean estaPagada) {
        this.estaPagada = estaPagada;
    }
}
