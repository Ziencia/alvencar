package es.mde.entidades;

import es.mde.alvencar.OfertaImpl;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "OFERTAS")
public class OfertaConId extends OfertaImpl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VEHICULO")
	private VehiculoConId vehiculo;

    public OfertaConId() {}
    
    public OfertaConId(float ofertaVenta, float ofertaAlquiler, VehiculoConId vehiculo) {
        super(ofertaVenta,ofertaAlquiler);
        this.vehiculo = vehiculo;
    }

    public Long getId() {
        return id;
    }

    public VehiculoConId getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(VehiculoConId vehiculo) {
        this.vehiculo = vehiculo;
    }
}