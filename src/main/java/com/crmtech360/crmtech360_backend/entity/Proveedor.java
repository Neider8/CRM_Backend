package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @Column(name = "nombre_comercial_proveedor", nullable = false, length = 255)
    private String nombreComercialProveedor;

    @Column(name = "razon_social_proveedor", length = 255)
    private String razonSocialProveedor;

    @Column(name = "nit_proveedor", nullable = false, unique = true, length = 20)
    private String nitProveedor;

    @Column(name = "direccion_proveedor", length = 255)
    private String direccionProveedor;

    @Column(name = "telefono_proveedor", length = 20)
    private String telefonoProveedor;

    @Column(name = "correo_proveedor", length = 100)
    private String correoProveedor;

    @Column(name = "contacto_principal_proveedor", length = 255)
    private String contactoPrincipalProveedor;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrdenCompra> ordenesCompra;

    // Constructores
    public Proveedor() {
    }

    public Proveedor(String nombreComercialProveedor, String razonSocialProveedor, String nitProveedor, String direccionProveedor, String telefonoProveedor, String correoProveedor, String contactoPrincipalProveedor) {
        this.nombreComercialProveedor = nombreComercialProveedor;
        this.razonSocialProveedor = razonSocialProveedor;
        this.nitProveedor = nitProveedor;
        this.direccionProveedor = direccionProveedor;
        this.telefonoProveedor = telefonoProveedor;
        this.correoProveedor = correoProveedor;
        this.contactoPrincipalProveedor = contactoPrincipalProveedor;
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
    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreComercialProveedor() {
        return nombreComercialProveedor;
    }

    public void setNombreComercialProveedor(String nombreComercialProveedor) {
        this.nombreComercialProveedor = nombreComercialProveedor;
    }

    public String getRazonSocialProveedor() {
        return razonSocialProveedor;
    }

    public void setRazonSocialProveedor(String razonSocialProveedor) {
        this.razonSocialProveedor = razonSocialProveedor;
    }

    public String getNitProveedor() {
        return nitProveedor;
    }

    public void setNitProveedor(String nitProveedor) {
        this.nitProveedor = nitProveedor;
    }

    public String getDireccionProveedor() {
        return direccionProveedor;
    }

    public void setDireccionProveedor(String direccionProveedor) {
        this.direccionProveedor = direccionProveedor;
    }

    public String getTelefonoProveedor() {
        return telefonoProveedor;
    }

    public void setTelefonoProveedor(String telefonoProveedor) {
        this.telefonoProveedor = telefonoProveedor;
    }

    public String getCorreoProveedor() {
        return correoProveedor;
    }

    public void setCorreoProveedor(String correoProveedor) {
        this.correoProveedor = correoProveedor;
    }

    public String getContactoPrincipalProveedor() {
        return contactoPrincipalProveedor;
    }

    public void setContactoPrincipalProveedor(String contactoPrincipalProveedor) {
        this.contactoPrincipalProveedor = contactoPrincipalProveedor;
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

    public Set<OrdenCompra> getOrdenesCompra() {
        return ordenesCompra;
    }

    public void setOrdenesCompra(Set<OrdenCompra> ordenesCompra) {
        this.ordenesCompra = ordenesCompra;
    }


    // equals y hashCode (basado en nitProveedor si está disponible, sino idProveedor)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proveedor proveedor = (Proveedor) o;
        if (nitProveedor != null ? !nitProveedor.equals(proveedor.nitProveedor) : proveedor.nitProveedor != null) {
            if (nitProveedor == null || proveedor.nitProveedor == null) {
                return idProveedor != null && idProveedor.equals(proveedor.idProveedor);
            }
            return false;
        }
        if (nitProveedor == null && proveedor.nitProveedor == null) {
            return idProveedor != null && idProveedor.equals(proveedor.idProveedor);
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = nitProveedor != null ? nitProveedor.hashCode() : 0;
        if (nitProveedor == null) {
            result = 31 * result + (idProveedor != null ? idProveedor.hashCode() : 0);
        }
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "Proveedor{" +
                "idProveedor=" + idProveedor +
                ", nombreComercialProveedor='" + nombreComercialProveedor + '\'' +
                ", nitProveedor='" + nitProveedor + '\'' +
                '}';
    }
}