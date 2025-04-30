package es.mde.entidades;

//@OneToMany. Vehiculos tendra una coleccion de transacciones
import java.util.ArrayList;
import java.util.Collection;

import es.mde.repositorios.VehiculoListener;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



import java.time.LocalDate;

@Entity
@Table(name = "VEHICULOS")
@EntityListeners(VehiculoListener.class)
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
    private Long id;

    private String matricula;
    private String bastidor;
    private String marca;
    private String modelo;
    private String color;
    private LocalDate fechaMatriculacion;
    private String condicionAdquisicion;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = Transaccion.class, mappedBy = "vehiculo")
	private Collection<Transaccion> transacciones = new ArrayList<>();

    // Constructors
    public Vehiculo() {}

    public Vehiculo(String matricula, String bastidor, String marca, String modelo,
                    String color, LocalDate fechaMatriculacion, String condicionAdquisicion) {
        this.matricula = matricula;
        this.bastidor = bastidor;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.fechaMatriculacion = fechaMatriculacion;
        this.condicionAdquisicion = condicionAdquisicion;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getBastidor() {
        return bastidor;
    }

    public void setBastidor(String bastidor) {
        this.bastidor = bastidor;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDate getFechaMatriculacion() {
        return fechaMatriculacion;
    }

    public void setFechaMatriculacion(LocalDate fechaMatriculacion) {
        this.fechaMatriculacion = fechaMatriculacion;
    }

    public String getCondicionAdquisicion() {
        return condicionAdquisicion;
    }

    public void setCondicionAdquisicion(String condicionAdquisicion) {
        this.condicionAdquisicion = condicionAdquisicion;
    }

    public Collection<Transaccion> getTransacciones() {
		return transacciones;
	}

	public void setTransacciones(Collection<Transaccion> transacciones) {
		this.transacciones = transacciones;
	}

	public void addTransaccion(Transaccion transaccion) {
		getTransacciones().add(transaccion);
		transaccion.setVehiculo(this);
	}

}