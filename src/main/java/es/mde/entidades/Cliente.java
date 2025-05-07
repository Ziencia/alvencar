package es.mde.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CLIENTES")
//@<EntityListeners(ClienteListener.class)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
    private Long id;

    private String cif;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String direccion;
    private String telefono;

    //@OneToMany(cascade = CascadeType.ALL, targetEntity = Factura.class, mappedBy = "cliente")
	//private Collection<Factura> facturas = new ArrayList<>();

    public Cliente(){

    }

    public Cliente(String cif, String nombre, String primerApellido, String segundoApellido, String direccion,
            String telefono) {
        this.cif = cif;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }



 

}