package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.dto.*;
import com.crmtech360.crmtech360_backend.entity.Insumo;
import com.crmtech360.crmtech360_backend.entity.InventarioInsumo;
import com.crmtech360.crmtech360_backend.entity.MovimientoInventarioInsumo;
import com.crmtech360.crmtech360_backend.repository.InsumoRepository;
import com.crmtech360.crmtech360_backend.repository.InventarioInsumoRepository;
import com.crmtech360.crmtech360_backend.repository.MovimientoInventarioInsumoRepository;
import com.crmtech360.crmtech360_backend.service.InventarioInsumoService;
import com.crmtech360.crmtech360_backend.exception.BadRequestException;
import com.crmtech360.crmtech360_backend.exception.DuplicateResourceException;
import com.crmtech360.crmtech360_backend.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventarioInsumoServiceImpl implements InventarioInsumoService {

    private static final Logger log = LoggerFactory.getLogger(InventarioInsumoServiceImpl.class);

    private final InventarioInsumoRepository inventarioInsumoRepository;
    private final MovimientoInventarioInsumoRepository movimientoInsumoRepository;
    private final InsumoRepository insumoRepository;

    public InventarioInsumoServiceImpl(InventarioInsumoRepository inventarioInsumoRepository,
                                       MovimientoInventarioInsumoRepository movimientoInsumoRepository,
                                       InsumoRepository insumoRepository) {
        this.inventarioInsumoRepository = inventarioInsumoRepository;
        this.movimientoInsumoRepository = movimientoInsumoRepository;
        this.insumoRepository = insumoRepository;
    }

    @Override
    public InventarioInsumoResponseDTO createInventarioInsumo(InventarioInsumoCreateRequestDTO createRequestDTO) {
        log.info("Creando registro de inventario para insumo ID {} en ubicación {}", createRequestDTO.getIdInsumo(), createRequestDTO.getUbicacionInventario());

        Insumo insumo = insumoRepository.findById(createRequestDTO.getIdInsumo())
                .orElseThrow(() -> new ResourceNotFoundException("Insumo", "id", createRequestDTO.getIdInsumo()));

        inventarioInsumoRepository.findByUbicacionInventarioAndInsumo(createRequestDTO.getUbicacionInventario(), insumo)
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("InventarioInsumo", "ubicacion/insumo", createRequestDTO.getUbicacionInventario() + "/" + insumo.getIdInsumo());
                });

        InventarioInsumo inventario = new InventarioInsumo();
        inventario.setInsumo(insumo);
        inventario.setUbicacionInventario(createRequestDTO.getUbicacionInventario());
        inventario.setCantidadStock(createRequestDTO.getCantidadStock());

        InventarioInsumo savedInventario = inventarioInsumoRepository.save(inventario);
        log.info("Registro de inventario insumo creado con ID {}", savedInventario.getIdInventarioInsumo());
        return mapToResponseDTO(savedInventario);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventarioInsumoResponseDTO> findAllInventarioInsumos(Pageable pageable) {
        log.info("Buscando todos los registros de inventario de insumos.");
        return inventarioInsumoRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioInsumoResponseDTO findInventarioInsumoById(Integer idInventarioInsumo) {
        log.info("Buscando registro de inventario de insumo por ID {}", idInventarioInsumo);
        InventarioInsumo inventario = findInventarioInsumoEntityById(idInventarioInsumo);
        return mapToResponseDTO(inventario);
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioInsumoResponseDTO findByInsumoAndUbicacion(Integer idInsumo, String ubicacion) {
        log.info("Buscando inventario para insumo ID {} en ubicación '{}'", idInsumo, ubicacion);
        Insumo insumo = insumoRepository.findById(idInsumo)
                .orElseThrow(() -> new ResourceNotFoundException("Insumo", "id", idInsumo));
        InventarioInsumo inventario = inventarioInsumoRepository.findByUbicacionInventarioAndInsumo(ubicacion, insumo)
                .orElseThrow(() -> new ResourceNotFoundException("InventarioInsumo", "insumo/ubicacion", idInsumo + "/" + ubicacion));
        return mapToResponseDTO(inventario);
    }

    @Override
    public MovimientoInventarioInsumoResponseDTO registrarMovimiento(MovimientoInventarioInsumoCreateRequestDTO movDTO) {
        log.info("Registrando movimiento de inventario insumo: {} unidades para inventario ID {}", movDTO.getCantidadMovimiento(), movDTO.getIdInventarioInsumo());

        InventarioInsumo inventario = findInventarioInsumoEntityById(movDTO.getIdInventarioInsumo());
        BigDecimal cantidadActual = inventario.getCantidadStock();
        BigDecimal cantidadMovimiento = movDTO.getCantidadMovimiento();

        if ("Salida".equalsIgnoreCase(movDTO.getTipoMovimiento())) {
            if (cantidadActual.compareTo(cantidadMovimiento) < 0) {
                log.warn("Stock insuficiente para salida. Inventario ID {}, Stock Actual {}, Salida Solicitada {}",
                        inventario.getIdInventarioInsumo(), cantidadActual, cantidadMovimiento);
                throw new BadRequestException("Stock insuficiente para el insumo " + inventario.getInsumo().getNombreInsumo() +
                        " en ubicación " + inventario.getUbicacionInventario() +
                        ". Stock actual: " + cantidadActual + ", Salida solicitada: " + cantidadMovimiento);
            }
            inventario.setCantidadStock(cantidadActual.subtract(cantidadMovimiento));
        } else if ("Entrada".equalsIgnoreCase(movDTO.getTipoMovimiento())) {
            inventario.setCantidadStock(cantidadActual.add(cantidadMovimiento));
        } else {
            throw new BadRequestException("Tipo de movimiento inválido: " + movDTO.getTipoMovimiento());
        }

        inventarioInsumoRepository.save(inventario);

        MovimientoInventarioInsumo movimiento = new MovimientoInventarioInsumo();
        movimiento.setInventarioInsumo(inventario);
        movimiento.setTipoMovimiento(movDTO.getTipoMovimiento());
        movimiento.setCantidadMovimiento(movDTO.getCantidadMovimiento());
        movimiento.setDescripcionMovimiento(movDTO.getDescripcionMovimiento());

        MovimientoInventarioInsumo savedMovimiento = movimientoInsumoRepository.save(movimiento);
        log.info("Movimiento ID {} registrado. Nuevo stock para inventario ID {}: {}", savedMovimiento.getIdMovimientoInsumo(), inventario.getIdInventarioInsumo(), inventario.getCantidadStock());
        return mapMovimientoToResponseDTO(savedMovimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovimientoInventarioInsumoResponseDTO> findMovimientosByInventarioInsumoId(Integer idInventarioInsumo, Pageable pageable) {
        log.info("Buscando movimientos para inventario insumo ID {}", idInventarioInsumo);
        if (!inventarioInsumoRepository.existsById(idInventarioInsumo)) {
            throw new ResourceNotFoundException("InventarioInsumo", "id", idInventarioInsumo);
        }
        Page<MovimientoInventarioInsumo> movimientos = movimientoInsumoRepository.findByInventarioInsumoIdInventarioInsumo(idInventarioInsumo, pageable);
        return movimientos.map(this::mapMovimientoToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getStockActual(Integer idInventarioInsumo) {
        log.info("Consultando stock actual para inventario insumo ID {}", idInventarioInsumo);
        InventarioInsumo inventario = findInventarioInsumoEntityById(idInventarioInsumo);
        return inventario.getCantidadStock();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioInsumoResponseDTO> findInventariosByInsumoId(Integer idInsumo) {
        log.info("Buscando todos los registros de inventario para el insumo ID {}", idInsumo);
        if (!insumoRepository.existsById(idInsumo)) {
            throw new ResourceNotFoundException("Insumo", "id", idInsumo);
        }
        List<InventarioInsumo> inventarios = inventarioInsumoRepository.findByInsumoIdInsumo(idInsumo);
        return inventarios.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    // --- Métodos de Ayuda y Mapeo Privados ---
    private InventarioInsumo findInventarioInsumoEntityById(Integer id) {
        return inventarioInsumoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventarioInsumo", "id", id));
    }

    private InventarioInsumoResponseDTO mapToResponseDTO(InventarioInsumo entity) {
        InsumoSummaryDTO insumoSummary = null;
        if (entity.getInsumo() != null) {
            Insumo i = entity.getInsumo();
            insumoSummary = new InsumoSummaryDTO(i.getIdInsumo(), i.getNombreInsumo(), i.getUnidadMedidaInsumo());
        }
        return new InventarioInsumoResponseDTO(
                entity.getIdInventarioInsumo(),
                entity.getUbicacionInventario(),
                insumoSummary,
                entity.getCantidadStock(),
                entity.getUltimaActualizacion()
        );
    }

    private MovimientoInventarioInsumoResponseDTO mapMovimientoToResponseDTO(MovimientoInventarioInsumo entity) {
        InventarioInsumoRefDTO inventarioRef = null;
        if (entity.getInventarioInsumo() != null) {
            InventarioInsumo ii = entity.getInventarioInsumo();
            String insumoNombre = (ii.getInsumo() != null) ? ii.getInsumo().getNombreInsumo() : "N/A";
            inventarioRef = new InventarioInsumoRefDTO(ii.getIdInventarioInsumo(), ii.getUbicacionInventario(), insumoNombre);
        }
        return new MovimientoInventarioInsumoResponseDTO(
                entity.getIdMovimientoInsumo(),
                entity.getTipoMovimiento(),
                inventarioRef,
                entity.getCantidadMovimiento(),
                entity.getFechaMovimiento(),
                entity.getDescripcionMovimiento()
        );
    }
}