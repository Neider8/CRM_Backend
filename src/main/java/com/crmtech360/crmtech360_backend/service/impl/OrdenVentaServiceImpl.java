package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.dto.*;
import com.crmtech360.crmtech360_backend.entity.Cliente;
import com.crmtech360.crmtech360_backend.entity.DetalleOrdenVenta;
import com.crmtech360.crmtech360_backend.entity.OrdenVenta;
import com.crmtech360.crmtech360_backend.entity.Producto;
import com.crmtech360.crmtech360_backend.repository.ClienteRepository;
import com.crmtech360.crmtech360_backend.repository.DetalleOrdenVentaRepository;
import com.crmtech360.crmtech360_backend.repository.OrdenVentaRepository;
import com.crmtech360.crmtech360_backend.repository.ProductoRepository;
import com.crmtech360.crmtech360_backend.service.OrdenVentaService;
import com.crmtech360.crmtech360_backend.exception.BadRequestException;
import com.crmtech360.crmtech360_backend.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Asegúrate de tener el import para LocalDate si lo usas directamente,
// aunque en este archivo parece que solo viene a través de los DTOs.
// import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrdenVentaServiceImpl implements OrdenVentaService {

    private static final Logger log = LoggerFactory.getLogger(OrdenVentaServiceImpl.class);

    private final OrdenVentaRepository ordenVentaRepository;
    private final DetalleOrdenVentaRepository detalleOrdenVentaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public OrdenVentaServiceImpl(OrdenVentaRepository ordenVentaRepository,
                                 DetalleOrdenVentaRepository detalleOrdenVentaRepository,
                                 ClienteRepository clienteRepository,
                                 ProductoRepository productoRepository) {
        this.ordenVentaRepository = ordenVentaRepository;
        this.detalleOrdenVentaRepository = detalleOrdenVentaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public OrdenVentaResponseDTO createOrdenVenta(OrdenVentaCreateRequestDTO createRequestDTO) {
        log.info("Creando nueva orden de venta para cliente ID: {}", createRequestDTO.getIdCliente());

        Cliente cliente = clienteRepository.findById(createRequestDTO.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", createRequestDTO.getIdCliente()));

        OrdenVenta ordenVenta = new OrdenVenta();
        ordenVenta.setCliente(cliente);
        ordenVenta.setFechaEntregaEstimada(createRequestDTO.getFechaEntregaEstimada());
        ordenVenta.setObservacionesOrden(createRequestDTO.getObservacionesOrden());
        ordenVenta.setEstadoOrden("Pendiente");
        ordenVenta.setDetallesOrdenVenta(new HashSet<>());

        BigDecimal totalOrdenCalculado = BigDecimal.ZERO;

        for (DetalleOrdenVentaRequestDTO detalleDTO : createRequestDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getIdProducto())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", detalleDTO.getIdProducto()));

            DetalleOrdenVenta detalle = new DetalleOrdenVenta();
            detalle.setOrdenVenta(ordenVenta);
            detalle.setProducto(producto);
            detalle.setCantidadProducto(detalleDTO.getCantidadProducto());

            BigDecimal precioUnitario = (detalleDTO.getPrecioUnitarioVenta() != null) ?
                    detalleDTO.getPrecioUnitarioVenta() : producto.getPrecioVenta();
            detalle.setPrecioUnitarioVenta(precioUnitario);

            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(detalleDTO.getCantidadProducto()));
            detalle.setSubtotalDetalle(subtotal);

            ordenVenta.getDetallesOrdenVenta().add(detalle);
            totalOrdenCalculado = totalOrdenCalculado.add(subtotal);
        }

        ordenVenta.setTotalOrden(totalOrdenCalculado);
        OrdenVenta savedOrdenVenta = ordenVentaRepository.save(ordenVenta);
        log.info("Orden de venta creada con ID: {}", savedOrdenVenta.getIdOrdenVenta());
        return mapToResponseDTO(savedOrdenVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrdenVentaResponseDTO> findAllOrdenesVenta(Pageable pageable) {
        log.info("Buscando todas las órdenes de venta, página: {}, tamaño: {}", pageable.getPageNumber(), pageable.getPageSize());
        return ordenVentaRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenVentaResponseDTO findOrdenVentaById(Integer id) {
        log.info("Buscando orden de venta con ID: {}", id);
        OrdenVenta ordenVenta = findOrdenVentaEntityById(id);
        return mapToResponseDTO(ordenVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenVentaResponseDTO> findOrdenesVentaByClienteId(Integer idCliente) {
        log.info("Buscando órdenes de venta para cliente ID: {}", idCliente);
        if (!clienteRepository.existsById(idCliente)) {
            throw new ResourceNotFoundException("Cliente", "id", idCliente);
        }
        List<OrdenVenta> ordenes = ordenVentaRepository.findByClienteIdCliente(idCliente);
        return ordenes.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }


    @Override
    public OrdenVentaResponseDTO updateOrdenVentaHeader(Integer id, OrdenVentaUpdateRequestDTO updateRequestDTO) {
        log.info("Actualizando cabecera de orden de venta ID: {}", id);
        OrdenVenta ordenVenta = findOrdenVentaEntityById(id);

        if ("Anulada".equals(ordenVenta.getEstadoOrden()) || "Entregada".equals(ordenVenta.getEstadoOrden())) {
            throw new BadRequestException("No se puede modificar una orden en estado '" + ordenVenta.getEstadoOrden() + "'.");
        }

        if (updateRequestDTO.getFechaEntregaEstimada() != null) {
            ordenVenta.setFechaEntregaEstimada(updateRequestDTO.getFechaEntregaEstimada());
        }
        if (updateRequestDTO.getEstadoOrden() != null) {
            ordenVenta.setEstadoOrden(updateRequestDTO.getEstadoOrden());
        }
        if (updateRequestDTO.getObservacionesOrden() != null) {
            ordenVenta.setObservacionesOrden(updateRequestDTO.getObservacionesOrden());
        }

        OrdenVenta updatedOrdenVenta = ordenVentaRepository.save(ordenVenta);
        log.info("Cabecera de orden de venta ID: {} actualizada.", updatedOrdenVenta.getIdOrdenVenta());
        return mapToResponseDTO(updatedOrdenVenta);
    }

    @Override
    public DetalleOrdenVentaResponseDTO addDetalleToOrdenVenta(Integer idOrdenVenta, DetalleOrdenVentaRequestDTO detalleRequestDTO) {
        log.info("Añadiendo detalle a orden de venta ID: {}", idOrdenVenta);
        OrdenVenta ordenVenta = findOrdenVentaEntityById(idOrdenVenta);

        if ("Anulada".equals(ordenVenta.getEstadoOrden()) || "Entregada".equals(ordenVenta.getEstadoOrden()) || "En Producción".equals(ordenVenta.getEstadoOrden())) {
            throw new BadRequestException("No se pueden añadir detalles a una orden en estado '" + ordenVenta.getEstadoOrden() + "'.");
        }

        Producto producto = productoRepository.findById(detalleRequestDTO.getIdProducto())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", detalleRequestDTO.getIdProducto()));

        DetalleOrdenVenta nuevoDetalle = new DetalleOrdenVenta();
        nuevoDetalle.setOrdenVenta(ordenVenta);
        nuevoDetalle.setProducto(producto);
        nuevoDetalle.setCantidadProducto(detalleRequestDTO.getCantidadProducto());
        BigDecimal precioUnitario = (detalleRequestDTO.getPrecioUnitarioVenta() != null) ?
                detalleRequestDTO.getPrecioUnitarioVenta() : producto.getPrecioVenta();
        nuevoDetalle.setPrecioUnitarioVenta(precioUnitario);
        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(detalleRequestDTO.getCantidadProducto()));
        nuevoDetalle.setSubtotalDetalle(subtotal);

        DetalleOrdenVenta savedDetalle = detalleOrdenVentaRepository.save(nuevoDetalle);
        ordenVenta.getDetallesOrdenVenta().add(savedDetalle);
        recalculateTotalOrdenVenta(ordenVenta);
        ordenVentaRepository.save(ordenVenta);

        log.info("Detalle ID: {} añadido a orden de venta ID: {}", savedDetalle.getIdDetalleOrden(), idOrdenVenta);
        return mapDetalleToResponseDTO(savedDetalle);
    }

    @Override
    public DetalleOrdenVentaResponseDTO updateDetalleInOrdenVenta(Integer idOrdenVenta, Integer idDetalleVenta, DetalleOrdenVentaRequestDTO detalleRequestDTO) {
        log.info("Actualizando detalle ID: {} en orden de venta ID: {}", idDetalleVenta, idOrdenVenta);
        OrdenVenta ordenVenta = findOrdenVentaEntityById(idOrdenVenta);

        if ("Anulada".equals(ordenVenta.getEstadoOrden()) || "Entregada".equals(ordenVenta.getEstadoOrden()) || "En Producción".equals(ordenVenta.getEstadoOrden())) {
            throw new BadRequestException("No se pueden actualizar detalles de una orden en estado '" + ordenVenta.getEstadoOrden() + "'.");
        }

        // LLAMADA CORREGIDA
        DetalleOrdenVenta detalleToUpdate = detalleOrdenVentaRepository.findByIdDetalleOrdenAndOrdenVenta_IdOrdenVenta(idDetalleVenta, idOrdenVenta)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleOrdenVenta", "idDetalleOrden/idOrdenVenta", idDetalleVenta + "/" + idOrdenVenta));

        Producto producto = productoRepository.findById(detalleRequestDTO.getIdProducto())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", detalleRequestDTO.getIdProducto()));

        detalleToUpdate.setProducto(producto);
        detalleToUpdate.setCantidadProducto(detalleRequestDTO.getCantidadProducto());
        BigDecimal precioUnitario = (detalleRequestDTO.getPrecioUnitarioVenta() != null) ?
                detalleRequestDTO.getPrecioUnitarioVenta() : producto.getPrecioVenta();
        detalleToUpdate.setPrecioUnitarioVenta(precioUnitario);
        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(detalleRequestDTO.getCantidadProducto()));
        detalleToUpdate.setSubtotalDetalle(subtotal);

        DetalleOrdenVenta updatedDetalle = detalleOrdenVentaRepository.save(detalleToUpdate);
        recalculateTotalOrdenVenta(ordenVenta);
        ordenVentaRepository.save(ordenVenta);

        log.info("Detalle ID: {} actualizado en orden de venta ID: {}", updatedDetalle.getIdDetalleOrden(), idOrdenVenta);
        return mapDetalleToResponseDTO(updatedDetalle);
    }

    @Override
    public void removeDetalleFromOrdenVenta(Integer idOrdenVenta, Integer idDetalleVenta) {
        log.info("Eliminando detalle ID: {} de orden de venta ID: {}", idDetalleVenta, idOrdenVenta);
        OrdenVenta ordenVenta = findOrdenVentaEntityById(idOrdenVenta);

        if ("Anulada".equals(ordenVenta.getEstadoOrden()) || "Entregada".equals(ordenVenta.getEstadoOrden()) || "En Producción".equals(ordenVenta.getEstadoOrden())) {
            throw new BadRequestException("No se pueden eliminar detalles de una orden en estado '" + ordenVenta.getEstadoOrden() + "'.");
        }

        // LLAMADA CORREGIDA
        DetalleOrdenVenta detalleToRemove = detalleOrdenVentaRepository.findByIdDetalleOrdenAndOrdenVenta_IdOrdenVenta(idDetalleVenta, idOrdenVenta)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleOrdenVenta", "idDetalleOrden/idOrdenVenta", idDetalleVenta + "/" + idOrdenVenta));

        ordenVenta.getDetallesOrdenVenta().remove(detalleToRemove);
        detalleOrdenVentaRepository.delete(detalleToRemove);

        recalculateTotalOrdenVenta(ordenVenta);
        ordenVentaRepository.save(ordenVenta);
        log.info("Detalle ID: {} eliminado de orden de venta ID: {}", idDetalleVenta, idOrdenVenta);
    }

    @Override
    public void anularOrdenVenta(Integer id) {
        log.info("Anulando orden de venta ID: {}", id);
        OrdenVenta ordenVenta = findOrdenVentaEntityById(id);

        if ("Entregada".equals(ordenVenta.getEstadoOrden())) {
            throw new BadRequestException("No se puede anular una orden ya entregada.");
        }

        ordenVenta.setEstadoOrden("Anulada");
        ordenVentaRepository.save(ordenVenta);
        log.info("Orden de venta ID: {} anulada.", id);
    }

    private OrdenVenta findOrdenVentaEntityById(Integer id) {
        return ordenVentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrdenVenta", "id", id));
    }

    private void recalculateTotalOrdenVenta(OrdenVenta ordenVenta) {
        BigDecimal nuevoTotal = BigDecimal.ZERO;
        List<DetalleOrdenVenta> detallesActualizados = detalleOrdenVentaRepository.findByOrdenVenta(ordenVenta);

        for (DetalleOrdenVenta detalle : detallesActualizados) {
            nuevoTotal = nuevoTotal.add(detalle.getSubtotalDetalle());
        }
        ordenVenta.setTotalOrden(nuevoTotal);
    }

    private OrdenVentaResponseDTO mapToResponseDTO(OrdenVenta entity) {
        OrdenVentaResponseDTO dto = new OrdenVentaResponseDTO();
        dto.setIdOrdenVenta(entity.getIdOrdenVenta());

        if (entity.getCliente() != null) {
            Cliente cliente = entity.getCliente();
            dto.setCliente(new ClienteSummaryDTO(cliente.getIdCliente(), cliente.getNumeroDocumento(), cliente.getNombreCliente()));
        }

        dto.setFechaPedido(entity.getFechaPedido());
        dto.setFechaEntregaEstimada(entity.getFechaEntregaEstimada());
        dto.setEstadoOrden(entity.getEstadoOrden());
        dto.setTotalOrden(entity.getTotalOrden());
        dto.setObservacionesOrden(entity.getObservacionesOrden());
        dto.setFechaActualizacion(entity.getFechaActualizacion());

        List<DetalleOrdenVentaResponseDTO> detallesDTO = (entity.getDetallesOrdenVenta() == null) ? new ArrayList<>() :
                entity.getDetallesOrdenVenta().stream()
                        .map(this::mapDetalleToResponseDTO)
                        .collect(Collectors.toList());
        dto.setDetalles(detallesDTO);
        return dto;
    }

    private DetalleOrdenVentaResponseDTO mapDetalleToResponseDTO(DetalleOrdenVenta detalle) {
        if (detalle == null) return null;
        DetalleOrdenVentaResponseDTO dto = new DetalleOrdenVentaResponseDTO();
        dto.setIdDetalleOrden(detalle.getIdDetalleOrden());

        if (detalle.getProducto() != null) {
            Producto prod = detalle.getProducto();
            dto.setProducto(new ProductoSummaryDTO(prod.getIdProducto(), prod.getReferenciaProducto(), prod.getNombreProducto(), prod.getTallaProducto(), prod.getColorProducto()));
        }

        dto.setCantidadProducto(detalle.getCantidadProducto());
        dto.setPrecioUnitarioVenta(detalle.getPrecioUnitarioVenta());
        dto.setSubtotalDetalle(detalle.getSubtotalDetalle());
        return dto;
    }
}