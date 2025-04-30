package es.mde.entidades;

import java.time.LocalDate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VENTA")
public class Venta extends Transaccion {

    private String regimen;
    private LocalDate fechaFinGarantia;

    public Venta() {

    }

    public Venta(float importe, Vehiculo vehiculo, String regimen, LocalDate fechaFinGarantia) {
        super(importe, vehiculo);
        this.regimen = regimen;
        this.fechaFinGarantia = fechaFinGarantia;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public LocalDate getFechaFinGarantia() {
        return fechaFinGarantia;
    }

    public void setFechaFinGarantia(LocalDate fechaFinGarantia) {
        this.fechaFinGarantia = fechaFinGarantia;
    }   
}
