package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.InsumoPorProducto;
import com.crmtech360.crmtech360_backend.entity.InsumoPorProductoId;
import com.crmtech360.crmtech360_backend.entity.Producto;
import com.crmtech360.crmtech360_backend.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InsumoPorProductoRepository extends JpaRepository<InsumoPorProducto, InsumoPorProductoId> {
    List<InsumoPorProducto> findByProducto(Producto producto);
    List<InsumoPorProducto> findByIdIdProducto(Integer idProducto); // Buscar por la parte del ID compuesto
    List<InsumoPorProducto> findByInsumo(Insumo insumo);
    List<InsumoPorProducto> findByIdIdInsumo(Integer idInsumo); // Buscar por la parte del ID compuesto
    Optional<InsumoPorProducto> findByProductoAndInsumo(Producto producto, Insumo insumo);
}