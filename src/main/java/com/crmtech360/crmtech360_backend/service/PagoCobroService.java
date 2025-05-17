package com.crmtech360.crmtech360_backend.service;

import com.crmtech360.crmtech360_backend.dto.PagoCobroCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.PagoCobroResponseDTO;
import com.crmtech360.crmtech360_backend.dto.PagoCobroUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PagoCobroService {

    PagoCobroResponseDTO createPagoCobro(PagoCobroCreateRequestDTO createRequestDTO);

    Page<PagoCobroResponseDTO> findAllPagosCobros(Pageable pageable);

    PagoCobroResponseDTO findPagoCobroById(Integer idPagoCobro);

    List<PagoCobroResponseDTO> findPagosCobrosByTipoTransaccion(String tipoTransaccion, Pageable pageable);

    List<PagoCobroResponseDTO> findPagosByOrdenCompraId(Integer idOrdenCompra, Pageable pageable);

    List<PagoCobroResponseDTO> findCobrosByOrdenVentaId(Integer idOrdenVenta, Pageable pageable);

    PagoCobroResponseDTO updatePagoCobro(Integer idPagoCobro, PagoCobroUpdateRequestDTO updateRequestDTO);

    void anularPagoCobro(Integer idPagoCobro);

    // void deletePagoCobro(Integer idPagoCobro); // Generalmente se anula, no se borra.
}