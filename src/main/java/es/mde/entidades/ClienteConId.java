package es.mde.entidades;

import es.mde.alvencar.ClienteImpl;

import java.util.ArrayList;
import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "CLIENTES")
public class ClienteConId extends ClienteImpl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, targetEntity = TransaccionConId.class, mappedBy = "cliente")
	private Collection<TransaccionConId> transacciones = new ArrayList<>();

    public ClienteConId(){
    }

    public ClienteConId(String cif, String nombre, String primerApellido, String segundoApellido, String direccion,
            String telefono, Collection<TransaccionConId> transacciones) {
        super(cif, nombre, primerApellido, segundoApellido, direccion, telefono);
        this.transacciones = transacciones;
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
		transaccion.setCliente(this);
	} 
}