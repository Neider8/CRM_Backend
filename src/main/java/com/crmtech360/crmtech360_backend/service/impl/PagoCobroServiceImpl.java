package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.dto.*;
import com.crmtech360.crmtech360_backend.entity.OrdenCompra;
import com.crmtech360.crmtech360_backend.entity.OrdenVenta;
import com.crmtech360.crmtech360_backend.entity.PagoCobro;
import com.crmtech360.crmtech360_backend.repository.OrdenCompraRepository;
import com.crmtech360.crmtech360_backend.repository.OrdenVentaRepository;
import com.crmtech360.crmtech360_backend.repository.PagoCobroRepository;
import com.crmtech360.crmtech360_backend.service.PagoCobroService;
import com.crmtech360.crmtech360_backend.exception.BadRequestException;
import com.crmtech360.crmtech360_backend.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl; // No se usa directamente aquí
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate; // Asegúrate de tener este import si se usa directamente
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PagoCobroServiceImpl implements PagoCobroService {

    private static final Logger log = LoggerFactory.getLogger(PagoCobroServiceImpl.class);

    private final PagoCobroRepository pagoCobroRepository;
    private final OrdenVentaRepository ordenVentaRepository;
    private final OrdenCompraRepository ordenCompraRepository;

    public PagoCobroServiceImpl(PagoCobroRepository pagoCobroRepository,
                                OrdenVentaRepository ordenVentaRepository,
                                OrdenCompraRepository ordenCompraRepository) {
        this.pagoCobroRepository = pagoCobroRepository;
        this.ordenVentaRepository = ordenVentaRepository;
        this.ordenCompraRepository = ordenCompraRepository;
    }

    @Override
    public PagoCobroResponseDTO createPagoCobro(PagoCobroCreateRequestDTO createRequestDTO) {
        log.info("Registrando nueva transacción: tipo '{}', monto {}", createRequestDTO.getTipoTransaccion(), createRequestDTO.getMontoTransaccion());

        PagoCobro pagoCobro = new PagoCobro();
        pagoCobro.setTipoTransaccion(createRequestDTO.getTipoTransaccion());
        pagoCobro.setFechaPagoCobro(createRequestDTO.getFechaPagoCobro()); // Este es LocalDate
        pagoCobro.setMetodoPago(createRequestDTO.getMetodoPago());
        pagoCobro.setMontoTransaccion(createRequestDTO.getMontoTransaccion());
        pagoCobro.setReferenciaTransaccion(createRequestDTO.getReferenciaTransaccion());
        pagoCobro.setEstadoTransaccion(createRequestDTO.getEstadoTransaccion());
        pagoCobro.setObservacionesTransaccion(createRequestDTO.getObservacionesTransaccion());

        if ("Cobro".equalsIgnoreCase(createRequestDTO.getTipoTransaccion())) {
            if (createRequestDTO.getIdOrdenVenta() == null) {
                throw new BadRequestException("Para un 'Cobro', se requiere el ID de la Orden de Venta.");
            }
            if (createRequestDTO.getIdOrdenCompra() != null) {
                throw new BadRequestException("Un 'Cobro' no puede estar asociado a una Orden de Compra.");
            }
            OrdenVenta ordenVenta = ordenVentaRepository.findById(createRequestDTO.getIdOrdenVenta())
                    .orElseThrow(() -> new ResourceNotFoundException("OrdenVenta", "id", createRequestDTO.getIdOrdenVenta()));
            pagoCobro.setOrdenVenta(ordenVenta);
        } else if ("Pago".equalsIgnoreCase(createRequestDTO.getTipoTransaccion())) {
            if (createRequestDTO.getIdOrdenCompra() == null) {
                throw new BadRequestException("Para un 'Pago', se requiere el ID de la Orden de Compra.");
            }
            if (createRequestDTO.getIdOrdenVenta() != null) {
                throw new BadRequestException("Un 'Pago' no puede estar asociado a una Orden de Venta.");
            }
            OrdenCompra ordenCompra = ordenCompraRepository.findById(createRequestDTO.getIdOrdenCompra())
                    .orElseThrow(() -> new ResourceNotFoundException("OrdenCompra", "id", createRequestDTO.getIdOrdenCompra()));
            pagoCobro.setOrdenCompra(ordenCompra);
        } else {
            throw new BadRequestException("Tipo de transacción desconocido: " + createRequestDTO.getTipoTransaccion());
        }

        PagoCobro savedPagoCobro = pagoCobroRepository.save(pagoCobro);
        log.info("Transacción ID {} registrada exitosamente.", savedPagoCobro.getIdPagoCobro());
        return mapToResponseDTO(savedPagoCobro);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PagoCobroResponseDTO> findAllPagosCobros(Pageable pageable) {
        log.info("Buscando todas las transacciones de pagos/cobros.");
        return pagoCobroRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoCobroResponseDTO findPagoCobroById(Integer idPagoCobro) {
        log.info("Buscando transacción por ID: {}", idPagoCobro);
        PagoCobro pagoCobro = findPagoCobroEntityById(idPagoCobro);
        return mapToResponseDTO(pagoCobro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoCobroResponseDTO> findPagosCobrosByTipoTransaccion(String tipoTransaccion, Pageable pageable) {
        log.info("Buscando transacciones por tipo: {}", tipoTransaccion);
        // LLAMADA CORREGIDA para el error "Expected 1 argument but found 2"
        Page<PagoCobro> page = pagoCobroRepository.findByTipoTransaccionIgnoreCase(tipoTransaccion, pageable);
        return page.getContent().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoCobroResponseDTO> findPagosByOrdenCompraId(Integer idOrdenCompra, Pageable pageable) {
        log.info("Buscando pagos para la orden de compra ID: {}", idOrdenCompra);
        if (!ordenCompraRepository.existsById(idOrdenCompra)) {
            throw new ResourceNotFoundException("OrdenCompra", "id", idOrdenCompra);
        }
        // LLAMADA CORREGIDA
        Page<PagoCobro> page = pagoCobroRepository.findByOrdenCompra_IdOrdenCompraAndTipoTransaccionIgnoreCase(idOrdenCompra, "Pago", pageable);
        return page.getContent().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoCobroResponseDTO> findCobrosByOrdenVentaId(Integer idOrdenVenta, Pageable pageable) {
        log.info("Buscando cobros para la orden de venta ID: {}", idOrdenVenta);
        if (!ordenVentaRepository.existsById(idOrdenVenta)) {
            throw new ResourceNotFoundException("OrdenVenta", "id", idOrdenVenta);
        }
        // LLAMADA CORREGIDA
        Page<PagoCobro> page = pagoCobroRepository.findByOrdenVenta_IdOrdenVentaAndTipoTransaccionIgnoreCase(idOrdenVenta, "Cobro", pageable);
        return page.getContent().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }


    @Override
    public PagoCobroResponseDTO updatePagoCobro(Integer idPagoCobro, PagoCobroUpdateRequestDTO updateRequestDTO) {
        log.info("Actualizando transacción ID: {}", idPagoCobro);
        PagoCobro pagoCobro = findPagoCobroEntityById(idPagoCobro);

        if ("Anulado".equals(pagoCobro.getEstadoTransaccion())) {
            throw new BadRequestException("No se puede modificar una transacción anulada.");
        }

        if (updateRequestDTO.getFechaPagoCobro() != null) {
            pagoCobro.setFechaPagoCobro(updateRequestDTO.getFechaPagoCobro());
        }
        if (updateRequestDTO.getMetodoPago() != null) {
            pagoCobro.setMetodoPago(updateRequestDTO.getMetodoPago());
        }
        if (updateRequestDTO.getReferenciaTransaccion() != null) {
            pagoCobro.setReferenciaTransaccion(updateRequestDTO.getReferenciaTransaccion());
        }
        if (updateRequestDTO.getEstadoTransaccion() != null) {
            pagoCobro.setEstadoTransaccion(updateRequestDTO.getEstadoTransaccion());
        }
        if (updateRequestDTO.getObservacionesTransaccion() != null) {
            pagoCobro.setObservacionesTransaccion(updateRequestDTO.getObservacionesTransaccion());
        }

        PagoCobro updatedPagoCobro = pagoCobroRepository.save(pagoCobro);
        log.info("Transacción ID {} actualizada.", updatedPagoCobro.getIdPagoCobro());
        return mapToResponseDTO(updatedPagoCobro);
    }

    @Override
    public void anularPagoCobro(Integer idPagoCobro) {
        log.info("Anulando transacción ID: {}", idPagoCobro);
        PagoCobro pagoCobro = findPagoCobroEntityById(idPagoCobro);

        if ("Anulado".equals(pagoCobro.getEstadoTransaccion())) {
            throw new BadRequestException("La transacción ya está anulada.");
        }
        pagoCobro.setEstadoTransaccion("Anulado");
        pagoCobroRepository.save(pagoCobro);
        log.info("Transacción ID {} anulada.", idPagoCobro);
    }

    private PagoCobro findPagoCobroEntityById(Integer id) {
        return pagoCobroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PagoCobro", "id", id));
    }

    private PagoCobroResponseDTO mapToResponseDTO(PagoCobro entity) {
        PagoCobroResponseDTO dto = new PagoCobroResponseDTO();
        dto.setIdPagoCobro(entity.getIdPagoCobro());
        dto.setTipoTransaccion(entity.getTipoTransaccion());
        dto.setFechaRegistroTransaccion(entity.getFechaRegistroTransaccion());
        dto.setFechaPagoCobro(entity.getFechaPagoCobro()); // LocalDate
        dto.setMetodoPago(entity.getMetodoPago());
        dto.setMontoTransaccion(entity.getMontoTransaccion());
        dto.setReferenciaTransaccion(entity.getReferenciaTransaccion());
        dto.setEstadoTransaccion(entity.getEstadoTransaccion());
        dto.setObservacionesTransaccion(entity.getObservacionesTransaccion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());

        if (entity.getOrdenVenta() != null) {
            OrdenVenta ov = entity.getOrdenVenta();
            String clienteNombre = (ov.getCliente() != null) ? ov.getCliente().getNombreCliente() : "N/A";
            dto.setOrdenVenta(new OrdenVentaSummaryDTO(ov.getIdOrdenVenta(), ov.getFechaPedido(), clienteNombre));
        }
        if (entity.getOrdenCompra() != null) {
            OrdenCompra oc = entity.getOrdenCompra();
            String proveedorNombre = (oc.getProveedor() != null) ? oc.getProveedor().getNombreComercialProveedor() : "N/A";
            dto.setOrdenCompra(new OrdenCompraSummaryDTO(oc.getIdOrdenCompra(), oc.getFechaPedidoCompra(), proveedorNombre));
        }
        return dto;
    }
}