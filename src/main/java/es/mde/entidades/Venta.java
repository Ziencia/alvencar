package es.mde.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VENTA")
public class Venta extends Transaccion {

    private String regimen;
    private LocalDateTime fechaFinGarantia;

    public Venta() {
    }

    public Venta(Vehiculo vehiculo, Cliente cliente, float importe, LocalDateTime fechaHoraEntrega, String regimen,
            LocalDateTime fechaFinGarantia) {
        super(vehiculo,cliente,importe,fechaHoraEntrega);
        this.regimen = regimen;
        this.fechaFinGarantia = fechaFinGarantia;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public LocalDateTime getFechaFinGarantia() {
        return fechaFinGarantia;
    }

    public void setFechaFinGarantia(LocalDateTime fechaFinGarantia) {
        this.fechaFinGarantia = fechaFinGarantia;
    }
}
