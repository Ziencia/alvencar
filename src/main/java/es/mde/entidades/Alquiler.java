package es.mde.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ALQUILER")
public class Alquiler extends Transaccion{
    
  private LocalDateTime fechaHoraDevolucion;
  private LocalDateTime fechaHoraDevuelto;
  private float kmAntes;
  private float kmDespues;
  private float depositoAntes;
  private float depositoDespues;

  public Alquiler(){
  }

  public Alquiler(float importe, Vehiculo vehiculo, LocalDateTime fechaHoraDevolucion, LocalDateTime fechaHoraDevuelto,
        float kmAntes, float kmDespues, float depositoAntes, float depositoDespues) {
    super(importe, vehiculo);
    this.fechaHoraDevolucion = fechaHoraDevolucion;
    this.fechaHoraDevuelto = fechaHoraDevuelto;
    this.kmAntes = kmAntes;
    this.kmDespues = kmDespues;
    this.depositoAntes = depositoAntes;
    this.depositoDespues = depositoDespues;
  }

  public LocalDateTime getFechaHoraDevolucion() {
    return fechaHoraDevolucion;
  }

  public void setFechaHoraDevolucion(LocalDateTime fechaHoraDevolucion) {
    this.fechaHoraDevolucion = fechaHoraDevolucion;
  }

  public LocalDateTime getFechaHoraDevuelto() {
    return fechaHoraDevuelto;
  }

  public void setFechaHoraDevuelto(LocalDateTime fechaHoraDevuelto) {
    this.fechaHoraDevuelto = fechaHoraDevuelto;
  }

  public float getKmAntes() {
    return kmAntes;
  }

  public void setKmAntes(float kmAntes) {
    this.kmAntes = kmAntes;
  }

  public float getKmDespues() {
    return kmDespues;
  }

  public void setKmDespues(float kmDespues) {
    this.kmDespues = kmDespues;
  }

  public float getDepositoAntes() {
    return depositoAntes;
  }

  public void setDepositoAntes(float depositoAntes) {
    this.depositoAntes = depositoAntes;
  }

  public float getDepositoDespues() {
    return depositoDespues;
  }

  public void setDepositoDespues(float depositoDespues) {
    this.depositoDespues = depositoDespues;
  }
  
}
