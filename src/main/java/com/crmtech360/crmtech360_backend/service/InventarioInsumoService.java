package com.crmtech360.crmtech360_backend.service;

import com.crmtech360.crmtech360_backend.dto.InventarioInsumoCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.InventarioInsumoResponseDTO;
import com.crmtech360.crmtech360_backend.dto.MovimientoInventarioInsumoCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.MovimientoInventarioInsumoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;

public interface InventarioInsumoService {

    InventarioInsumoResponseDTO createInventarioInsumo(InventarioInsumoCreateRequestDTO createRequestDTO);

    Page<InventarioInsumoResponseDTO> findAllInventarioInsumos(Pageable pageable);

    InventarioInsumoResponseDTO findInventarioInsumoById(Integer idInventarioInsumo);

    InventarioInsumoResponseDTO findByInsumoAndUbicacion(Integer idInsumo, String ubicacion);

    MovimientoInventarioInsumoResponseDTO registrarMovimiento(MovimientoInventarioInsumoCreateRequestDTO movimientoRequestDTO);

    Page<MovimientoInventarioInsumoResponseDTO> findMovimientosByInventarioInsumoId(Integer idInventarioInsumo, Pageable pageable);

    BigDecimal getStockActual(Integer idInventarioInsumo);

    List<InventarioInsumoResponseDTO> findInventariosByInsumoId(Integer idInsumo);
}