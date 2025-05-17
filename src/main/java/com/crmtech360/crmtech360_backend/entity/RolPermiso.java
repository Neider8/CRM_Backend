package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rolespermisos", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"rol_nombre", "id_permiso"})
})
public class RolPermiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol_permiso")
    private Integer idRolPermiso;

    @Column(name = "rol_nombre", nullable = false, length = 20)
    private String rolNombre; // 'Administrador', 'Gerente', 'Operario', 'Ventas'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_permiso", nullable = false)
    private Permiso permiso;


    // Constructores
    public RolPermiso() {
    }

    public RolPermiso(String rolNombre, Permiso permiso) {
        this.rolNombre = rolNombre;
        this.permiso = permiso;
    }

    // Getters y Setters
    public Integer getIdRolPermiso() {
        return idRolPermiso;
    }

    public void setIdRolPermiso(Integer idRolPermiso) {
        this.idRolPermiso = idRolPermiso;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }

    public Permiso getPermiso() {
        return permiso;
    }

    public void setPermiso(Permiso permiso) {
        this.permiso = permiso;
    }

    // equals y hashCode
    // La unicidad está dada por (rolNombre, id_permiso), o por idRolPermiso una vez asignado.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolPermiso that = (RolPermiso) o;
        if (idRolPermiso != null) {
            return idRolPermiso.equals(that.idRolPermiso);
        }
        // Si el ID no está, comparar por la combinación única de negocio
        return Objects.equals(rolNombre, that.rolNombre) &&
                Objects.equals(permiso, that.permiso); // Permiso debe tener equals/hashCode bien definidos
    }

    @Override
    public int hashCode() {
        if (idRolPermiso != null) {
            return Objects.hash(idRolPermiso);
        }
        return Objects.hash(rolNombre, permiso);
    }

    // toString
    @Override
    public String toString() {
        return "RolPermiso{" +
                "idRolPermiso=" + idRolPermiso +
                ", rolNombre='" + rolNombre + '\'' +
                ", permisoId=" + (permiso != null ? permiso.getIdPermiso() : "null") +
                '}';
    }
}