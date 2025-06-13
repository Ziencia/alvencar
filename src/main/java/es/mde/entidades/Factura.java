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
    private int tipoFactura;
    private LocalDateTime fechaInicioAlquiler;
    private LocalDateTime fechaFinAlquiler;
    private int numeroDiasAlquiler;
    private float importeTotalDias;
    private float importeKmExtra;
    private float importePenalizacionDeposito;


    public Factura() {
    }

    public Factura(String conceptoFactura, String nombreApellidosDNI, String datosDireccionLocalizacion,
            String datosVehiculo, float importe, float importeTotal, float impuestos, LocalDateTime fechaFactura,
            boolean estaPagada, int tipoFactura, LocalDateTime fechaInicioAlquiler, LocalDateTime fechaFinAlquiler,
            int numeroDiasAlquiler, float importeTotalDias, float importeKmExtra, float importePenalizacionDeposito) {
        this.conceptoFactura = conceptoFactura;
        this.nombreApellidosDNI = nombreApellidosDNI;
        this.datosDireccionLocalizacion = datosDireccionLocalizacion;
        this.datosVehiculo = datosVehiculo;
        this.importe = importe;
        this.importeTotal = importeTotal;
        this.impuestos = impuestos;
        this.fechaFactura = fechaFactura;
        this.estaPagada = estaPagada;
        this.tipoFactura = tipoFactura;
        this.fechaInicioAlquiler = fechaInicioAlquiler;
        this.fechaFinAlquiler = fechaFinAlquiler;
        this.numeroDiasAlquiler = numeroDiasAlquiler;
        this.importeTotalDias = importeTotalDias;
        this.importeKmExtra = importeKmExtra;
        this.importePenalizacionDeposito = importePenalizacionDeposito;
    }


    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaInicioAlquiler() {
        return fechaInicioAlquiler;
    }

    public void setFechaInicioAlquiler(LocalDateTime fechaInicioAlquiler) {
        this.fechaInicioAlquiler = fechaInicioAlquiler;
    }

    public LocalDateTime getFechaFinAlquiler() {
        return fechaFinAlquiler;
    }

    public void setFechaFinAlquiler(LocalDateTime fechaFinAlquiler) {
        this.fechaFinAlquiler = fechaFinAlquiler;
    }

    public int getNumeroDiasAlquiler() {
        return numeroDiasAlquiler;
    }

    public void setNumeroDiasAlquiler(int numeroDiasAlquiler) {
        this.numeroDiasAlquiler = numeroDiasAlquiler;
    }

    public float getImporteTotalDias() {
        return importeTotalDias;
    }

    public void setImporteTotalDias(float importeTotalDias) {
        this.importeTotalDias = importeTotalDias;
    }

    public float getImporteKmExtra() {
        return importeKmExtra;
    }

    public void setImporteKmExtra(float importeKmExtra) {
        this.importeKmExtra = importeKmExtra;
    }

    public float getImportePenalizacionDeposito() {
        return importePenalizacionDeposito;
    }

    public void setImportePenalizacionDeposito(float importePenalizacionDeposito) {
        this.importePenalizacionDeposito = importePenalizacionDeposito;
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

    // Tipo 0: venta; Tipo 1: alquiler
    public void setTipoFactura (int tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public int getTipoFactura () {
        return tipoFactura;
    }
}
