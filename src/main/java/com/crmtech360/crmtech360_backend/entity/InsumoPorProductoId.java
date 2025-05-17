package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InsumoPorProductoId implements Serializable {

    private static final long serialVersionUID = 1L; // Importante para Serializable

    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "id_insumo")
    private Integer idInsumo;

    // Constructores
    public InsumoPorProductoId() {
    }

    public InsumoPorProductoId(Integer idProducto, Integer idInsumo) {
        this.idProducto = idProducto;
        this.idInsumo = idInsumo;
    }

    // Getters y Setters
    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsumoPorProductoId that = (InsumoPorProductoId) o;
        return Objects.equals(idProducto, that.idProducto) &&
                Objects.equals(idInsumo, that.idInsumo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto, idInsumo);
    }
}