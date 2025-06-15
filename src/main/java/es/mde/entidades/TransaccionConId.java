package es.mde.entidades;

import es.mde.alvencar.TransaccionImpl;

import java.time.LocalDateTime;

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

public abstract class TransaccionConId extends TransaccionImpl  {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VEHICULO")
	private VehiculoConId vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLIENTE")
	private ClienteConId cliente;

    public TransaccionConId() {
    }

    public TransaccionConId(VehiculoConId vehiculo, ClienteConId cliente, float importe, LocalDateTime fechaHoraEntrega){
        super(importe,fechaHoraEntrega);
        this.vehiculo = vehiculo;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public ClienteConId getCliente() {
        return cliente;
    }

    public void setCliente(ClienteConId cliente) {
        this.cliente = cliente;
    }

    public VehiculoConId getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(VehiculoConId vehiculo) {
		this.vehiculo = vehiculo;
	}
}
