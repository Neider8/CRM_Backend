package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.dto.*;
import com.crmtech360.crmtech360_backend.entity.*;
import com.crmtech360.crmtech360_backend.repository.*;
import com.crmtech360.crmtech360_backend.service.OrdenCompraService;
import com.crmtech360.crmtech360_backend.exception.BadRequestException;
import com.crmtech360.crmtech360_backend.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private static final Logger log = LoggerFactory.getLogger(OrdenCompraServiceImpl.class);

    private final OrdenCompraRepository ordenCompraRepository;
    private final DetalleOrdenCompraRepository detalleOrdenCompraRepository;
    private final ProveedorRepository proveedorRepository;
    private final InsumoRepository insumoRepository;

    public OrdenCompraServiceImpl(OrdenCompraRepository ordenCompraRepository,
                                  DetalleOrdenCompraRepository detalleOrdenCompraRepository,
                                  ProveedorRepository proveedorRepository,
                                  InsumoRepository insumoRepository) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.detalleOrdenCompraRepository = detalleOrdenCompraRepository;
        this.proveedorRepository = proveedorRepository;
        this.insumoRepository = insumoRepository;
    }

    @Override
    public OrdenCompraResponseDTO createOrdenCompra(OrdenCompraCreateRequestDTO createRequestDTO) {
        log.info("Creando nueva orden de compra para proveedor ID: {}", createRequestDTO.getIdProveedor());

        Proveedor proveedor = proveedorRepository.findById(createRequestDTO.getIdProveedor())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor", "id", createRequestDTO.getIdProveedor()));

        OrdenCompra ordenCompra = new OrdenCompra();
        ordenCompra.setProveedor(proveedor);
        ordenCompra.setFechaEntregaEstimadaCompra(createRequestDTO.getFechaEntregaEstimadaCompra());
        ordenCompra.setObservacionesCompra(createRequestDTO.getObservacionesCompra());
        ordenCompra.setEstadoCompra("Pendiente"); // Estado inicial
        ordenCompra.setDetallesOrdenCompra(new HashSet<>());

        BigDecimal totalOrdenCalculado = BigDecimal.ZERO;

        for (DetalleOrdenCompraRequestDTO detalleDTO : createRequestDTO.getDetalles()) {
            Insumo insumo = insumoRepository.findById(detalleDTO.getIdInsumo())
                    .orElseThrow(() -> new ResourceNotFoundException("Insumo", "id", detalleDTO.getIdInsumo()));

            DetalleOrdenCompra detalle = new DetalleOrdenCompra();
            detalle.setOrdenCompra(ordenCompra);
            detalle.setInsumo(insumo);
            detalle.setCantidadCompra(detalleDTO.getCantidadCompra());
            detalle.setPrecioUnitarioCompra(detalleDTO.getPrecioUnitarioCompra());

            BigDecimal subtotal = detalleDTO.getPrecioUnitarioCompra().multiply(BigDecimal.valueOf(detalleDTO.getCantidadCompra()));
            detalle.setSubtotalCompra(subtotal);

            ordenCompra.getDetallesOrdenCompra().add(detalle);
            totalOrdenCalculado = totalOrdenCalculado.add(subtotal);
        }

        ordenCompra.setTotalCompra(totalOrdenCalculado);
        OrdenCompra savedOrdenCompra = ordenCompraRepository.save(ordenCompra);
        log.info("Orden de compra creada con ID: {}", savedOrdenCompra.getIdOrdenCompra());
        return mapToResponseDTO(savedOrdenCompra);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrdenCompraResponseDTO> findAllOrdenesCompra(Pageable pageable) {
        log.info("Buscando todas las órdenes de compra, página: {}, tamaño: {}", pageable.getPageNumber(), pageable.getPageSize());
        return ordenCompraRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraResponseDTO> findOrdenesCompraByProveedorId(Integer idProveedor) {
        log.info("Buscando órdenes de compra para proveedor ID: {}", idProveedor);
        if (!proveedorRepository.existsById(idProveedor)) {
            throw new ResourceNotFoundException("Proveedor", "id", idProveedor);
        }
        List<OrdenCompra> ordenes = ordenCompraRepository.findByProveedorIdProveedor(idProveedor);
        return ordenes.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenCompraResponseDTO findOrdenCompraById(Integer id) {
        log.info("Buscando orden de compra con ID: {}", id);
        OrdenCompra ordenCompra = findOrdenCompraEntityById(id);
        return mapToResponseDTO(ordenCompra);
    }

    @Override
    public OrdenCompraResponseDTO updateOrdenCompraHeader(Integer id, OrdenCompraUpdateRequestDTO updateRequestDTO) {
        log.info("Actualizando cabecera de orden de compra ID: {}", id);
        OrdenCompra ordenCompra = findOrdenCompraEntityById(id);

        if ("Anulada".equals(ordenCompra.getEstadoCompra()) || "Recibida Total".equals(ordenCompra.getEstadoCompra())) {
            throw new BadRequestException("No se puede modificar una orden de compra en estado '" + ordenCompra.getEstadoCompra() + "'.");
        }

        if (updateRequestDTO.getFechaEntregaEstimadaCompra() != null) {
            ordenCompra.setFechaEntregaEstimadaCompra(updateRequestDTO.getFechaEntregaEstimadaCompra());
        }
        if (updateRequestDTO.getFechaEntregaRealCompra() != null) {
            ordenCompra.setFechaEntregaRealCompra(updateRequestDTO.getFechaEntregaRealCompra());
        }
        if (updateRequestDTO.getEstadoCompra() != null) {
            ordenCompra.setEstadoCompra(updateRequestDTO.getEstadoCompra());
            if("Recibida Total".equals(updateRequestDTO.getEstadoCompra()) && ordenCompra.getFechaEntregaRealCompra() == null) {
                ordenCompra.setFechaEntregaRealCompra(java.time.LocalDate.now());
            }
        }
        if (updateRequestDTO.getObservacionesCompra() != null) {
            ordenCompra.setObservacionesCompra(updateRequestDTO.getObservacionesCompra());
        }

        OrdenCompra updatedOrdenCompra = ordenCompraRepository.save(ordenCompra);
        log.info("Cabecera de orden de compra ID: {} actualizada.", updatedOrdenCompra.getIdOrdenCompra());
        return mapToResponseDTO(updatedOrdenCompra);
    }

    @Override
    public DetalleOrdenCompraResponseDTO addDetalleToOrdenCompra(Integer idOrdenCompra, DetalleOrdenCompraRequestDTO detalleRequestDTO) {
        log.info("Añadiendo detalle a orden de compra ID: {}", idOrdenCompra);
        OrdenCompra ordenCompra = findOrdenCompraEntityById(idOrdenCompra);

        if (!("Pendiente".equals(ordenCompra.getEstadoCompra()) || "Enviada".equals(ordenCompra.getEstadoCompra()))) {
            throw new BadRequestException("Solo se pueden añadir detalles a órdenes de compra en estado 'Pendiente' o 'Enviada'. Estado actual: " + ordenCompra.getEstadoCompra());
        }

        Insumo insumo = insumoRepository.findById(detalleRequestDTO.getIdInsumo())
                .orElseThrow(() -> new ResourceNotFoundException("Insumo", "id", detalleRequestDTO.getIdInsumo()));

        DetalleOrdenCompra nuevoDetalle = new DetalleOrdenCompra();
        nuevoDetalle.setOrdenCompra(ordenCompra);
        nuevoDetalle.setInsumo(insumo);
        nuevoDetalle.setCantidadCompra(detalleRequestDTO.getCantidadCompra());
        nuevoDetalle.setPrecioUnitarioCompra(detalleRequestDTO.getPrecioUnitarioCompra());
        BigDecimal subtotal = detalleRequestDTO.getPrecioUnitarioCompra().multiply(BigDecimal.valueOf(detalleRequestDTO.getCantidadCompra()));
        nuevoDetalle.setSubtotalCompra(subtotal);

        DetalleOrdenCompra savedDetalle = detalleOrdenCompraRepository.save(nuevoDetalle);
        ordenCompra.getDetallesOrdenCompra().add(savedDetalle);
        recalculateTotalOrdenCompra(ordenCompra);
        ordenCompraRepository.save(ordenCompra);

        log.info("Detalle ID: {} añadido a orden de compra ID: {}", savedDetalle.getIdDetalleCompra(), idOrdenCompra);
        return mapDetalleToResponseDTO(savedDetalle);
    }

    @Override
    public DetalleOrdenCompraResponseDTO updateDetalleInOrdenCompra(Integer idOrdenCompra, Integer idDetalleCompra, DetalleOrdenCompraRequestDTO detalleRequestDTO) {
        log.info("Actualizando detalle ID: {} en orden de compra ID: {}", idDetalleCompra, idOrdenCompra);
        OrdenCompra ordenCompra = findOrdenCompraEntityById(idOrdenCompra);

        if (!("Pendiente".equals(ordenCompra.getEstadoCompra()) || "Enviada".equals(ordenCompra.getEstadoCompra()))) {
            throw new BadRequestException("Solo se pueden actualizar detalles de órdenes de compra en estado 'Pendiente' o 'Enviada'. Estado actual: " + ordenCompra.getEstadoCompra());
        }

        // LLAMADA CORREGIDA
        DetalleOrdenCompra detalleToUpdate = detalleOrdenCompraRepository.findByIdDetalleCompraAndOrdenCompra_IdOrdenCompra(idDetalleCompra, idOrdenCompra)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleOrdenCompra", "idDetalleCompra/idOrdenCompra", idDetalleCompra + "/" + idOrdenCompra));

        Insumo insumo = insumoRepository.findById(detalleRequestDTO.getIdInsumo())
                .orElseThrow(() -> new ResourceNotFoundException("Insumo", "id", detalleRequestDTO.getIdInsumo()));

        detalleToUpdate.setInsumo(insumo);
        detalleToUpdate.setCantidadCompra(detalleRequestDTO.getCantidadCompra());
        detalleToUpdate.setPrecioUnitarioCompra(detalleRequestDTO.getPrecioUnitarioCompra());
        BigDecimal subtotal = detalleRequestDTO.getPrecioUnitarioCompra().multiply(BigDecimal.valueOf(detalleRequestDTO.getCantidadCompra()));
        detalleToUpdate.setSubtotalCompra(subtotal);

        DetalleOrdenCompra updatedDetalle = detalleOrdenCompraRepository.save(detalleToUpdate);
        recalculateTotalOrdenCompra(ordenCompra);
        ordenCompraRepository.save(ordenCompra);

        log.info("Detalle ID: {} actualizado en orden de compra ID: {}", updatedDetalle.getIdDetalleCompra(), idOrdenCompra);
        return mapDetalleToResponseDTO(updatedDetalle);
    }

    @Override
    public void removeDetalleFromOrdenCompra(Integer idOrdenCompra, Integer idDetalleCompra) {
        log.info("Eliminando detalle ID: {} de orden de compra ID: {}", idDetalleCompra, idOrdenCompra);
        OrdenCompra ordenCompra = findOrdenCompraEntityById(idOrdenCompra);

        if (!("Pendiente".equals(ordenCompra.getEstadoCompra()) || "Enviada".equals(ordenCompra.getEstadoCompra()))) {
            throw new BadRequestException("Solo se pueden eliminar detalles de órdenes de compra en estado 'Pendiente' o 'Enviada'. Estado actual: " + ordenCompra.getEstadoCompra());
        }

        // LLAMADA CORREGIDA
        DetalleOrdenCompra detalleToRemove = detalleOrdenCompraRepository.findByIdDetalleCompraAndOrdenCompra_IdOrdenCompra(idDetalleCompra, idOrdenCompra)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleOrdenCompra", "idDetalleCompra/idOrdenCompra", idDetalleCompra + "/" + idOrdenCompra));

        ordenCompra.getDetallesOrdenCompra().remove(detalleToRemove);
        detalleOrdenCompraRepository.delete(detalleToRemove);

        recalculateTotalOrdenCompra(ordenCompra);
        ordenCompraRepository.save(ordenCompra);
        log.info("Detalle ID: {} eliminado de orden de compra ID: {}", idDetalleCompra, idOrdenCompra);
    }


    @Override
    public void anularOrdenCompra(Integer id) {
        log.info("Anulando orden de compra ID: {}", id);
        OrdenCompra ordenCompra = findOrdenCompraEntityById(id);

        if ("Recibida Total".equals(ordenCompra.getEstadoCompra()) || "Recibida Parcial".equals(ordenCompra.getEstadoCompra())) {
            throw new BadRequestException("No se puede anular una orden de compra que ya ha sido parcial o totalmente recibida. Considere una devolución.");
        }

        ordenCompra.setEstadoCompra("Anulada");
        ordenCompraRepository.save(ordenCompra);
        log.info("Orden de compra ID: {} anulada.", id);
    }

    private OrdenCompra findOrdenCompraEntityById(Integer id) {
        return ordenCompraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrdenCompra", "id", id));
    }

    private void recalculateTotalOrdenCompra(OrdenCompra ordenCompra) {
        BigDecimal nuevoTotal = BigDecimal.ZERO;
        List<DetalleOrdenCompra> detallesActualizados = detalleOrdenCompraRepository.findByOrdenCompra(ordenCompra);
        for (DetalleOrdenCompra detalle : detallesActualizados) {
            nuevoTotal = nuevoTotal.add(detalle.getSubtotalCompra());
        }
        ordenCompra.setTotalCompra(nuevoTotal);
    }

    private OrdenCompraResponseDTO mapToResponseDTO(OrdenCompra entity) {
        OrdenCompraResponseDTO dto = new OrdenCompraResponseDTO();
        dto.setIdOrdenCompra(entity.getIdOrdenCompra());

        if (entity.getProveedor() != null) {
            Proveedor prov = entity.getProveedor();
            dto.setProveedor(new ProveedorSummaryDTO(prov.getIdProveedor(), prov.getNitProveedor(), prov.getNombreComercialProveedor()));
        }

        dto.setFechaPedidoCompra(entity.getFechaPedidoCompra());
        dto.setFechaEntregaEstimadaCompra(entity.getFechaEntregaEstimadaCompra());
        dto.setFechaEntregaRealCompra(entity.getFechaEntregaRealCompra());
        dto.setEstadoCompra(entity.getEstadoCompra());
        dto.setTotalCompra(entity.getTotalCompra());
        dto.setObservacionesCompra(entity.getObservacionesCompra());
        dto.setFechaActualizacion(entity.getFechaActualizacion());

        List<DetalleOrdenCompraResponseDTO> detallesDTO = (entity.getDetallesOrdenCompra() == null) ? new ArrayList<>() :
                entity.getDetallesOrdenCompra().stream()
                        .map(this::mapDetalleToResponseDTO)
                        .collect(Collectors.toList());
        dto.setDetalles(detallesDTO);
        return dto;
    }

    private DetalleOrdenCompraResponseDTO mapDetalleToResponseDTO(DetalleOrdenCompra detalle) {
        if (detalle == null) return null;
        DetalleOrdenCompraResponseDTO dto = new DetalleOrdenCompraResponseDTO();
        dto.setIdDetalleCompra(detalle.getIdDetalleCompra());

        if (detalle.getInsumo() != null) {
            Insumo ins = detalle.getInsumo();
            dto.setInsumo(new InsumoSummaryDTO(ins.getIdInsumo(), ins.getNombreInsumo(), ins.getUnidadMedidaInsumo()));
        }

        dto.setCantidadCompra(detalle.getCantidadCompra());
        dto.setPrecioUnitarioCompra(detalle.getPrecioUnitarioCompra());
        dto.setSubtotalCompra(detalle.getSubtotalCompra());
        return dto;
    }
}