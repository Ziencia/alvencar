package es.mde.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// ManyToOne
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


//Padre-Hijos
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Table(name = "TRANSACCIONES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO")
@DiscriminatorValue("TRANSACCION")

public class Transaccion {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Long id;

    private float importe;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VEHICULO")
	private Vehiculo vehiculo;

    public Transaccion() {

    }

    public Transaccion(float importe, Vehiculo vehiculo){
        this.importe = importe;
        this.vehiculo = vehiculo;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }    

    public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

    @Override
    public String toString() {
        return "Vehiculo con matricula " + getVehiculo().getMatricula() + " por " + getImporte();
    }


}
