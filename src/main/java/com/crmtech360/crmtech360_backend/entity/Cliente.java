package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String tipoDocumento;

    @Column(name = "numero_documento", nullable = false, unique = true, length = 20)
    private String numeroDocumento;

    @Column(name = "nombre_cliente", nullable = false, length = 255)
    private String nombreCliente;

    @Column(name = "direccion_cliente", length = 255)
    private String direccionCliente;

    @Column(name = "telefono_cliente", length = 20)
    private String telefonoCliente;

    @Column(name = "correo_cliente", length = 100)
    private String correoCliente;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ContactoCliente> contactosCliente;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrdenVenta> ordenesVenta;


    // Constructores
    public Cliente() {
    }

    public Cliente(String tipoDocumento, String numeroDocumento, String nombreCliente, String direccionCliente, String telefonoCliente, String correoCliente) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        this.telefonoCliente = telefonoCliente;
        this.correoCliente = correoCliente;
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Set<ContactoCliente> getContactosCliente() {
        return contactosCliente;
    }

    public void setContactosCliente(Set<ContactoCliente> contactosCliente) {
        this.contactosCliente = contactosCliente;
    }

    public Set<OrdenVenta> getOrdenesVenta() {
        return ordenesVenta;
    }

    public void setOrdenesVenta(Set<OrdenVenta> ordenesVenta) {
        this.ordenesVenta = ordenesVenta;
    }

    // equals y hashCode (basado en numeroDocumento si está disponible, sino idCliente)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        if (numeroDocumento != null ? !numeroDocumento.equals(cliente.numeroDocumento) : cliente.numeroDocumento != null) {
            // Si numeroDocumento es diferente (y no nulo en ambos), verificar id si numeroDocumento es nulo en uno
            if (numeroDocumento == null || cliente.numeroDocumento == null) { // Uno de ellos es nulo, el otro no (ya que el primer if falló)
                return idCliente != null && idCliente.equals(cliente.idCliente);
            }
            return false;
        }
        // Si numeroDocumento es igual (o ambos nulos), y si son nulos, usar id
        if (numeroDocumento == null && cliente.numeroDocumento == null) {
            return idCliente != null && idCliente.equals(cliente.idCliente);
        }
        return true; // numeroDocumento es igual y no nulo
    }

    @Override
    public int hashCode() {
        int result = numeroDocumento != null ? numeroDocumento.hashCode() : 0;
        // Si numeroDocumento es nulo, basar el hash en el ID para diferenciar instancias no guardadas vs guardadas
        if (numeroDocumento == null) {
            result = 31 * result + (idCliente != null ? idCliente.hashCode() : 0);
        }
        return result;
    }


    // toString
    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", direccionCliente='" + direccionCliente + '\'' +
                ", telefonoCliente='" + telefonoCliente + '\'' +
                ", correoCliente='" + correoCliente + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }
}