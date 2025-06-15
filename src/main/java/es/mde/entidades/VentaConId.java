package es.mde.entidades;

import java.time.LocalDateTime;

import es.mde.alvencar.Venta;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VENTA")
public class VentaConId  extends TransaccionConId implements Venta{

    private String regimen;
    private LocalDateTime fechaFinGarantia;

    public VentaConId() {
    }

    @Override
    public String getRegimen() {
        return regimen;
    }

    @Override
    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    @Override
    public LocalDateTime getFechaFinGarantia() {
        return fechaFinGarantia;
    }

    @Override
    public void setFechaFinGarantia(LocalDateTime fechaFinGarantia) {
        this.fechaFinGarantia = fechaFinGarantia;
    }
}
