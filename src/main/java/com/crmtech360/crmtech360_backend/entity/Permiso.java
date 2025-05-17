package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "permisos")
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Integer idPermiso;

    @Column(name = "nombre_permiso", nullable = false, unique = true, length = 100)
    private String nombrePermiso;

    // Relación para RolesPermisos (lado uno de @OneToMany)
    // No es estrictamente necesario tener esta colección aquí si solo navegas desde RolesPermisos.
    // Pero si quieres navegar desde Permiso a RolesPermisos, la incluyes.
    // @OneToMany(mappedBy = "permiso", cascade = CascadeType.ALL, orphanRemoval = true)
    // private Set<RolPermiso> rolesPermisos;


    // Constructores
    public Permiso() {
    }

    public Permiso(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
    }

    // Getters y Setters
    public Integer getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(Integer idPermiso) {
        this.idPermiso = idPermiso;
    }

    public String getNombrePermiso() {
        return nombrePermiso;
    }

    public void setNombrePermiso(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
    }

    // public Set<RolPermiso> getRolesPermisos() {
    //     return rolesPermisos;
    // }

    // public void setRolesPermisos(Set<RolPermiso> rolesPermisos) {
    //     this.rolesPermisos = rolesPermisos;
    // }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permiso permiso = (Permiso) o;
        if (nombrePermiso != null ? !nombrePermiso.equals(permiso.nombrePermiso) : permiso.nombrePermiso != null) return false;
        return idPermiso != null ? idPermiso.equals(permiso.idPermiso) : permiso.idPermiso == null;
    }

    @Override
    public int hashCode() {
        int result = idPermiso != null ? idPermiso.hashCode() : 0;
        result = 31 * result + (nombrePermiso != null ? nombrePermiso.hashCode() : 0);
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "Permiso{" +
                "idPermiso=" + idPermiso +
                ", nombrePermiso='" + nombrePermiso + '\'' +
                '}';
    }
}