package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Optional<Producto> findByReferenciaProducto(String referenciaProducto);
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombreProducto);
    List<Producto> findByTipoProductoIgnoreCase(String tipoProducto);
    List<Producto> findByGeneroProductoIgnoreCase(String generoProducto);
    List<Producto> findByColorProductoIgnoreCase(String colorProducto);
    List<Producto> findByTallaProductoIgnoreCase(String tallaProducto);
    List<Producto> findByPrecioVentaBetween(BigDecimal minPrecio, BigDecimal maxPrecio);
    List<Producto> findByCostoProduccionBetween(BigDecimal minCosto, BigDecimal maxCosto);
    List<Producto> findByUnidadMedidaProductoIgnoreCase(String unidadMedida);
    List<Producto> findByTipoProductoAndGeneroProductoAllIgnoreCase(String tipoProducto, String generoProducto);
    List<Producto> findAllByOrderByNombreProductoAsc();
    boolean existsByReferenciaProducto(String referenciaProducto);
}