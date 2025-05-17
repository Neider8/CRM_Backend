package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.InventarioProducto;
import com.crmtech360.crmtech360_backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioProductoRepository extends JpaRepository<InventarioProducto, Integer> {
    Optional<InventarioProducto> findByUbicacionInventarioAndProducto(String ubicacionInventario, Producto producto);
    List<InventarioProducto> findByProducto(Producto producto);
    List<InventarioProducto> findByProductoIdProducto(Integer idProducto);
    List<InventarioProducto> findByUbicacionInventarioIgnoreCase(String ubicacionInventario);
    List<InventarioProducto> findByProductoAndCantidadStockLessThan(Producto producto, Integer cantidadMinima);
    List<InventarioProducto> findByCantidadStockLessThanEqual(Integer cantidad);
    List<InventarioProducto> findByProductoReferenciaProductoIgnoreCase(String referenciaProducto);
    // Consulta para encontrar productos cuyo stock actual está por debajo de un "stock mínimo" ideal (no en la tabla Inventario, sino en Producto)
    // Esto es un ejemplo más avanzado, podría necesitar un DTO o un Join más explícito si el stock_minimo estuviera en Producto.
    // Para ahora, asumimos que el "stock mínimo" es un parámetro.
    @Query("SELECT ip FROM InventarioProducto ip WHERE ip.cantidadStock < ?1")
    List<InventarioProducto> findProductosConStockBajo(Integer stockMinimoParametro);
}