package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "contactoscliente")
public class ContactoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contacto")
    private Integer idContacto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente") // No nullable por defecto, FK
    private Cliente cliente;

    @Column(name = "nombre_contacto", nullable = false, length = 255)
    private String nombreContacto;

    @Column(name = "cargo_contacto", length = 100)
    private String cargoContacto;

    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;

    @Column(name = "correo_contacto", length = 100)
    private String correoContacto;

    // Constructores
    public ContactoCliente() {
    }

    public ContactoCliente(Cliente cliente, String nombreContacto, String cargoContacto, String telefonoContacto, String correoContacto) {
        this.cliente = cliente;
        this.nombreContacto = nombreContacto;
        this.cargoContacto = cargoContacto;
        this.telefonoContacto = telefonoContacto;
        this.correoContacto = correoContacto;
    }

    // Getters y Setters
    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getCargoContacto() {
        return cargoContacto;
    }

    public void setCargoContacto(String cargoContacto) {
        this.cargoContacto = cargoContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getCorreoContacto() {
        return correoContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactoCliente that = (ContactoCliente) o;
        // Usa idContacto si está disponible, sino una combinación de campos de negocio
        if (idContacto != null) {
            return idContacto.equals(that.idContacto);
        }
        // Si idContacto es nulo (entidad nueva), compara por campos de negocio
        // Asegúrate de que 'cliente' y su ID se manejen correctamente si es parte de la unicidad antes de guardar.
        // Para este ejemplo, si el ID es nulo, solo son iguales si son la misma instancia o todos los campos coinciden.
        // Pero es mejor basarse en ID una vez asignado.
        return Objects.equals(nombreContacto, that.nombreContacto) &&
                Objects.equals(correoContacto, that.correoContacto) &&
                (cliente != null && that.cliente != null && cliente.getIdCliente() != null ? cliente.getIdCliente().equals(that.cliente.getIdCliente()) : cliente == that.cliente) ;
    }

    @Override
    public int hashCode() {
        // Usa idContacto si está disponible
        if (idContacto != null) {
            return Objects.hash(idContacto);
        }
        // Si idContacto es nulo, usa una combinación de campos de negocio
        return Objects.hash(cliente != null ? cliente.getIdCliente() : null, nombreContacto, correoContacto);
    }

    // toString
    @Override
    public String toString() {
        return "ContactoCliente{" +
                "idContacto=" + idContacto +
                ", idCliente=" + (cliente != null ? cliente.getIdCliente() : "null") +
                ", nombreContacto='" + nombreContacto + '\'' +
                ", cargoContacto='" + cargoContacto + '\'' +
                ", telefonoContacto='" + telefonoContacto + '\'' +
                ", correoContacto='" + correoContacto + '\'' +
                '}';
    }
}