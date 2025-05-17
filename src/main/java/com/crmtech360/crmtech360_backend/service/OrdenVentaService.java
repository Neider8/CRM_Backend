package com.crmtech360.crmtech360_backend.service;

import com.crmtech360.crmtech360_backend.dto.DetalleOrdenVentaRequestDTO;
import com.crmtech360.crmtech360_backend.dto.DetalleOrdenVentaResponseDTO;
import com.crmtech360.crmtech360_backend.dto.OrdenVentaCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.OrdenVentaResponseDTO;
import com.crmtech360.crmtech360_backend.dto.OrdenVentaUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrdenVentaService {

    OrdenVentaResponseDTO createOrdenVenta(OrdenVentaCreateRequestDTO createRequestDTO);

    Page<OrdenVentaResponseDTO> findAllOrdenesVenta(Pageable pageable);

    OrdenVentaResponseDTO findOrdenVentaById(Integer id);

    List<OrdenVentaResponseDTO> findOrdenesVentaByClienteId(Integer idCliente);

    OrdenVentaResponseDTO updateOrdenVentaHeader(Integer id, OrdenVentaUpdateRequestDTO updateRequestDTO);

    // Métodos para gestionar los detalles de una orden existente
    DetalleOrdenVentaResponseDTO addDetalleToOrdenVenta(Integer idOrdenVenta, DetalleOrdenVentaRequestDTO detalleRequestDTO);

    DetalleOrdenVentaResponseDTO updateDetalleInOrdenVenta(Integer idOrdenVenta, Integer idDetalleVenta, DetalleOrdenVentaRequestDTO detalleRequestDTO);

    void removeDetalleFromOrdenVenta(Integer idOrdenVenta, Integer idDetalleVenta);

    // Considerar un método para anular la orden, que podría tener lógica específica
    void anularOrdenVenta(Integer id);

    // (Opcional) Eliminar físicamente, aunque anular suele ser preferido
    // void deleteOrdenVenta(Integer id);
}