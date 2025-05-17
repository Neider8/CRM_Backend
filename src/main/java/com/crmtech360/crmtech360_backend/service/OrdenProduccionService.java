package com.crmtech360.crmtech360_backend.service;

import com.crmtech360.crmtech360_backend.dto.OrdenProduccionCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.OrdenProduccionResponseDTO;
import com.crmtech360.crmtech360_backend.dto.OrdenProduccionUpdateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.TareaProduccionCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.TareaProduccionResponseDTO;
import com.crmtech360.crmtech360_backend.dto.TareaProduccionUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrdenProduccionService {

    OrdenProduccionResponseDTO createOrdenProduccion(OrdenProduccionCreateRequestDTO createRequestDTO);

    Page<OrdenProduccionResponseDTO> findAllOrdenesProduccion(Pageable pageable);

    List<OrdenProduccionResponseDTO> findOrdenesProduccionByOrdenVentaId(Integer idOrdenVenta);

    OrdenProduccionResponseDTO findOrdenProduccionById(Integer id);

    OrdenProduccionResponseDTO updateOrdenProduccionHeader(Integer id, OrdenProduccionUpdateRequestDTO updateRequestDTO);

    // Métodos para gestionar las tareas de una orden de producción
    TareaProduccionResponseDTO addTareaToOrdenProduccion(Integer idOrdenProduccion, TareaProduccionCreateRequestDTO tareaRequestDTO);

    TareaProduccionResponseDTO updateTareaInOrdenProduccion(Integer idOrdenProduccion, Integer idTarea, TareaProduccionUpdateRequestDTO tareaRequestDTO);

    void removeTareaFromOrdenProduccion(Integer idOrdenProduccion, Integer idTarea);

    void anularOrdenProduccion(Integer id);

    // (Opcional) Eliminar físicamente, aunque anular es preferido
    // void deleteOrdenProduccion(Integer id);
}