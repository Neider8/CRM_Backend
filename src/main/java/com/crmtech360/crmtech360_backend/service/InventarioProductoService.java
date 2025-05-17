package com.crmtech360.crmtech360_backend.service;

import com.crmtech360.crmtech360_backend.dto.InventarioProductoCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.InventarioProductoResponseDTO;
// Quitamos InventarioProductoUpdateDTO si solo se actualiza por movimiento
import com.crmtech360.crmtech360_backend.dto.MovimientoInventarioProductoCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.MovimientoInventarioProductoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface InventarioProductoService {

    InventarioProductoResponseDTO createInventarioProducto(InventarioProductoCreateRequestDTO createRequestDTO);

    Page<InventarioProductoResponseDTO> findAllInventarioProductos(Pageable pageable);

    InventarioProductoResponseDTO findInventarioProductoById(Integer idInventarioProducto);

    InventarioProductoResponseDTO findByProductoAndUbicacion(Integer idProducto, String ubicacion);

    // La actualización del stock se hace a través de movimientos.
    // Se podría tener un método para cambiar ubicación si es estrictamente necesario y no hay stock.
    // InventarioProductoResponseDTO updateInventarioProductoUbicacion(Integer idInventarioProducto, String nuevaUbicacion);

    MovimientoInventarioProductoResponseDTO registrarMovimiento(MovimientoInventarioProductoCreateRequestDTO movimientoRequestDTO);

    Page<MovimientoInventarioProductoResponseDTO> findMovimientosByInventarioProductoId(Integer idInventarioProducto, Pageable pageable);

    Integer getStockActual(Integer idInventarioProducto);

    List<InventarioProductoResponseDTO> findInventariosByProductoId(Integer idProducto);

    // void deleteInventarioProducto(Integer idInventarioProducto); // Generalmente no se borra, se deja en 0 stock.
}