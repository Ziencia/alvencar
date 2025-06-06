package es.mde.entidades;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ALQUILER")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Alquiler extends Transaccion{
    
  private LocalDateTime fechaHoraDevolucion;
  private float kmAntes;
  private float kmDespues;
  private String depositoAntes;
  private String depositoDespues;

  public Alquiler(){
  }

  public Alquiler(float importe, Vehiculo vehiculo, Cliente cliente, LocalDateTime fechaHoraEntrega, LocalDateTime fechaHoraDevolucion,
        float kmAntes, float kmDespues, String depositoAntes, String depositoDespues) {
    super(vehiculo, cliente, importe, fechaHoraEntrega);
    this.fechaHoraDevolucion = fechaHoraDevolucion;
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

  public String getDepositoAntes() {
    return depositoAntes;
  }

  public void setDepositoAntes(String depositoAntes) {
    this.depositoAntes = depositoAntes;
  }

  public String getDepositoDespues() {
    return depositoDespues;
  }

  public void setDepositoDespues(String depositoDespues) {
    this.depositoDespues = depositoDespues;
  }  
}
