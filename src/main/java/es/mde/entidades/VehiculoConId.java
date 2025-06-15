package es.mde.entidades;

import es.mde.alvencar.VehiculoImpl;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "VEHICULOS")
public class VehiculoConId extends VehiculoImpl{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = TransaccionConId.class, mappedBy = "vehiculo")
	private Collection<TransaccionConId> transacciones = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, targetEntity = OfertaConId.class, mappedBy = "vehiculo")
    private Collection<OfertaConId> ofertas = new ArrayList<>();

    public VehiculoConId(){

    }

    public VehiculoConId(String matricula, String bastidor, String marca, String modelo,
                    String color, LocalDate fechaMatriculacion, String condicionAdquisicion, boolean vendido) {
        super(matricula, bastidor, marca, modelo, color, fechaMatriculacion, condicionAdquisicion, vendido);
    }

    public Long getId() {
        return id;
    }

    public Collection<TransaccionConId> getTransacciones() {
		return transacciones;
	}

	public void setTransacciones(Collection<TransaccionConId> transacciones) {
		this.transacciones = transacciones;
	}

	public void addTransaccion(TransaccionConId transaccion) {
		getTransacciones().add(transaccion);
		transaccion.setVehiculo(this);
	}

    public Collection<OfertaConId> getOfertas() {
		return ofertas;
	}

	public void setOfertas(Collection<OfertaConId> ofertas) {
		this.ofertas = ofertas;
	}

	public void addOferta(OfertaConId oferta) {
		getOfertas().add(oferta);
		oferta.setVehiculo(this);
	}
}