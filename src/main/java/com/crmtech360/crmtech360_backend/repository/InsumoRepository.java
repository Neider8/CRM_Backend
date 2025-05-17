package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Integer> {
    Optional<Insumo> findByNombreInsumoIgnoreCase(String nombreInsumo);
    List<Insumo> findByNombreInsumoContainingIgnoreCase(String nombreInsumo);
    List<Insumo> findByUnidadMedidaInsumoIgnoreCase(String unidadMedida);
    List<Insumo> findByStockMinimoInsumoGreaterThanEqual(Integer stockMinimo);
    List<Insumo> findAllByOrderByNombreInsumoAsc();
    boolean existsByNombreInsumoIgnoreCase(String nombreInsumo);
}