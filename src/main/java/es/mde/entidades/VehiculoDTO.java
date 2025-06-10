// Concepto DTO para evitar la recursividad que me estaba causando la busqueda de coches disponibles para alquiler
// https://medium.com/@zubeyrdamar/java-spring-boot-handling-infinite-recursion-a95fe5a53c92 (entre otros)
package es.mde.entidades;

public class VehiculoDTO {
    private Long id;
    private String marca;
    private String modelo;
    private String matricula;

    public VehiculoDTO(es.mde.entidades.Vehiculo vehiculo) {
        this.id = vehiculo.getId();
        this.marca = vehiculo.getMarca();
        this.modelo = vehiculo.getModelo();
        this.matricula = vehiculo.getMatricula();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
