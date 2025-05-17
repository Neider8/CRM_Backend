package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.InventarioInsumo;
import com.crmtech360.crmtech360_backend.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioInsumoRepository extends JpaRepository<InventarioInsumo, Integer> {
    Optional<InventarioInsumo> findByUbicacionInventarioAndInsumo(String ubicacionInventario, Insumo insumo);
    List<InventarioInsumo> findByInsumo(Insumo insumo);
    List<InventarioInsumo> findByInsumoIdInsumo(Integer idInsumo);
    List<InventarioInsumo> findByUbicacionInventarioIgnoreCase(String ubicacionInventario);
    List<InventarioInsumo> findByInsumoAndCantidadStockLessThan(Insumo insumo, BigDecimal cantidadMinima);
    List<InventarioInsumo> findByInsumoNombreInsumoIgnoreCase(String nombreInsumo);
    // Para encontrar insumos cuyo stock actual está por debajo de su stock_minimo_insumo definido en la tabla Insumos
    @Query("SELECT ii FROM InventarioInsumo ii WHERE ii.cantidadStock < ii.insumo.stockMinimoInsumo")
    List<InventarioInsumo> findInsumosBajoStockMinimo();
}