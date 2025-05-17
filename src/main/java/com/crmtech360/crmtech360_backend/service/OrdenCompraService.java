package com.crmtech360.crmtech360_backend.service;

import com.crmtech360.crmtech360_backend.dto.DetalleOrdenCompraRequestDTO;
import com.crmtech360.crmtech360_backend.dto.DetalleOrdenCompraResponseDTO;
import com.crmtech360.crmtech360_backend.dto.OrdenCompraCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.OrdenCompraResponseDTO;
import com.crmtech360.crmtech360_backend.dto.OrdenCompraUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrdenCompraService {

    OrdenCompraResponseDTO createOrdenCompra(OrdenCompraCreateRequestDTO createRequestDTO);

    Page<OrdenCompraResponseDTO> findAllOrdenesCompra(Pageable pageable);

    OrdenCompraResponseDTO findOrdenCompraById(Integer id);

    List<OrdenCompraResponseDTO> findOrdenesCompraByProveedorId(Integer idProveedor);

    OrdenCompraResponseDTO updateOrdenCompraHeader(Integer id, OrdenCompraUpdateRequestDTO updateRequestDTO);

    // Métodos para gestionar los detalles de una orden de compra existente
    DetalleOrdenCompraResponseDTO addDetalleToOrdenCompra(Integer idOrdenCompra, DetalleOrdenCompraRequestDTO detalleRequestDTO);

    DetalleOrdenCompraResponseDTO updateDetalleInOrdenCompra(Integer idOrdenCompra, Integer idDetalleCompra, DetalleOrdenCompraRequestDTO detalleRequestDTO);

    void removeDetalleFromOrdenCompra(Integer idOrdenCompra, Integer idDetalleCompra);

    void anularOrdenCompra(Integer id);

    // void deleteOrdenCompra(Integer id); // Opcional, anular es preferido
}