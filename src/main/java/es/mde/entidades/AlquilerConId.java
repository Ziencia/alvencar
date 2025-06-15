package es.mde.entidades;

import es.mde.alvencar.Alquiler;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ALQUILER")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class AlquilerConId extends TransaccionConId implements Alquiler{
    
  private LocalDateTime fechaHoraDevolucion;
  float kmAntes;
  float kmDespues;
  String depositoAntes;
  String depositoDespues;

  public AlquilerConId(){
  }

  @Override
  public LocalDateTime getFechaHoraDevolucion() {
    return this.fechaHoraDevolucion;
  }
  @Override
  public void setFechaHoraDevolucion(LocalDateTime fechaHoraDevolucion) {
    this.fechaHoraDevolucion = fechaHoraDevolucion;
  }

  @Override
  public float getKmAntes() {
    return this.kmAntes;
  }

  @Override
  public void setKmAntes(float kmAntes) {
    this.kmAntes = kmAntes;
  }

  @Override
  public float getKmDespues() {
    return this.kmDespues;
  }

  @Override
  public void setKmDespues(float kmDespues) {
    this.kmDespues = kmDespues;
  }

  @Override
  public String getDepositoAntes() {
    return this.depositoAntes;
  }

  @Override
  public void setDepositoAntes(String depositoAntes) {
    this.depositoAntes = depositoAntes;
  }

  @Override
  public String getDepositoDespues() {
    return this.depositoDespues;
  }

  @Override
  public void setDepositoDespues(String depositoDespues) {
    this.depositoDespues = depositoDespues;
  }

}
