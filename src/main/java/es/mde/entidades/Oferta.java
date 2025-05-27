package es.mde.entidades;

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
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
    private Long id;

    private float ofertaVenta;
    private float ofertaAlquiler;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VEHICULO")
	private Vehiculo vehiculo;

    public Oferta() {}
    
    public Oferta(float ofertaVenta, float ofertaAlquiler, Vehiculo vehiculo) {
        this.ofertaVenta = ofertaVenta;
        this.ofertaAlquiler = ofertaAlquiler;
        this.vehiculo = vehiculo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getOfertaVenta() {
        return ofertaVenta;
    }

    public void setOfertaVenta(float ofertaVenta) {
        this.ofertaVenta = ofertaVenta;
    }

    public float getOfertaAlquiler() {
        return ofertaAlquiler;
    }

    public void setOfertaAlquiler(float ofertaAlquiler) {
        this.ofertaAlquiler = ofertaAlquiler;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
}