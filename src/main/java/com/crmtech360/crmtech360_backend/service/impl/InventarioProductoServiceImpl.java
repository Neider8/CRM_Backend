package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.dto.*;
import com.crmtech360.crmtech360_backend.entity.InventarioProducto;
import com.crmtech360.crmtech360_backend.entity.MovimientoInventarioProducto;
import com.crmtech360.crmtech360_backend.entity.Producto;
import com.crmtech360.crmtech360_backend.repository.InventarioProductoRepository;
import com.crmtech360.crmtech360_backend.repository.MovimientoInventarioProductoRepository;
import com.crmtech360.crmtech360_backend.repository.ProductoRepository;
import com.crmtech360.crmtech360_backend.service.InventarioProductoService;
import com.crmtech360.crmtech360_backend.exception.BadRequestException;
import com.crmtech360.crmtech360_backend.exception.DuplicateResourceException;
import com.crmtech360.crmtech360_backend.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventarioProductoServiceImpl implements InventarioProductoService {

    private static final Logger log = LoggerFactory.getLogger(InventarioProductoServiceImpl.class);

    private final InventarioProductoRepository inventarioProductoRepository;
    private final MovimientoInventarioProductoRepository movimientoInventarioProductoRepository;
    private final ProductoRepository productoRepository;

    public InventarioProductoServiceImpl(InventarioProductoRepository inventarioProductoRepository,
                                         MovimientoInventarioProductoRepository movimientoInventarioProductoRepository,
                                         ProductoRepository productoRepository) {
        this.inventarioProductoRepository = inventarioProductoRepository;
        this.movimientoInventarioProductoRepository = movimientoInventarioProductoRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public InventarioProductoResponseDTO createInventarioProducto(InventarioProductoCreateRequestDTO createRequestDTO) {
        log.info("Creando registro de inventario para producto ID {} en ubicación {}", createRequestDTO.getIdProducto(), createRequestDTO.getUbicacionInventario());

        Producto producto = productoRepository.findById(createRequestDTO.getIdProducto())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", createRequestDTO.getIdProducto()));

        inventarioProductoRepository.findByUbicacionInventarioAndProducto(createRequestDTO.getUbicacionInventario(), producto)
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("InventarioProducto", "ubicacion/producto", createRequestDTO.getUbicacionInventario() + "/" + producto.getIdProducto());
                });

        InventarioProducto inventario = new InventarioProducto();
        inventario.setProducto(producto);
        inventario.setUbicacionInventario(createRequestDTO.getUbicacionInventario());
        inventario.setCantidadStock(createRequestDTO.getCantidadStock()); // Cantidad inicial

        InventarioProducto savedInventario = inventarioProductoRepository.save(inventario);
        log.info("Registro de inventario producto creado con ID {}", savedInventario.getIdInventarioProducto());
        return mapToResponseDTO(savedInventario);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventarioProductoResponseDTO> findAllInventarioProductos(Pageable pageable) {
        log.info("Buscando todos los registros de inventario de productos.");
        return inventarioProductoRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioProductoResponseDTO findInventarioProductoById(Integer idInventarioProducto) {
        log.info("Buscando registro de inventario de producto por ID {}", idInventarioProducto);
        InventarioProducto inventario = findInventarioProductoEntityById(idInventarioProducto);
        return mapToResponseDTO(inventario);
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioProductoResponseDTO findByProductoAndUbicacion(Integer idProducto, String ubicacion) {
        log.info("Buscando inventario para producto ID {} en ubicación '{}'", idProducto, ubicacion);
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", idProducto));
        InventarioProducto inventario = inventarioProductoRepository.findByUbicacionInventarioAndProducto(ubicacion, producto)
                .orElseThrow(() -> new ResourceNotFoundException("InventarioProducto", "producto/ubicacion", idProducto + "/" + ubicacion));
        return mapToResponseDTO(inventario);
    }


    @Override
    public MovimientoInventarioProductoResponseDTO registrarMovimiento(MovimientoInventarioProductoCreateRequestDTO movDTO) {
        log.info("Registrando movimiento de inventario producto: {} unidades para inventario ID {}", movDTO.getCantidadMovimiento(), movDTO.getIdInventarioProducto());

        InventarioProducto inventario = findInventarioProductoEntityById(movDTO.getIdInventarioProducto());
        int cantidadActual = inventario.getCantidadStock();
        int cantidadMovimiento = movDTO.getCantidadMovimiento();

        if ("Salida".equalsIgnoreCase(movDTO.getTipoMovimiento())) {
            if (cantidadActual < cantidadMovimiento) {
                log.warn("Stock insuficiente para salida. Inventario ID {}, Stock Actual {}, Salida Solicitada {}",
                        inventario.getIdInventarioProducto(), cantidadActual, cantidadMovimiento);
                throw new BadRequestException("Stock insuficiente para el producto " + inventario.getProducto().getNombreProducto() +
                        " en ubicación " + inventario.getUbicacionInventario() +
                        ". Stock actual: " + cantidadActual + ", Salida solicitada: " + cantidadMovimiento);
            }
            inventario.setCantidadStock(cantidadActual - cantidadMovimiento);
        } else if ("Entrada".equalsIgnoreCase(movDTO.getTipoMovimiento())) {
            inventario.setCantidadStock(cantidadActual + cantidadMovimiento);
        } else {
            throw new BadRequestException("Tipo de movimiento inválido: " + movDTO.getTipoMovimiento());
        }

        // Guardar el inventario actualizado (actualiza ultima_actualizacion por @PreUpdate)
        inventarioProductoRepository.save(inventario);

        MovimientoInventarioProducto movimiento = new MovimientoInventarioProducto();
        movimiento.setInventarioProducto(inventario);
        movimiento.setTipoMovimiento(movDTO.getTipoMovimiento());
        movimiento.setCantidadMovimiento(movDTO.getCantidadMovimiento());
        movimiento.setDescripcionMovimiento(movDTO.getDescripcionMovimiento());
        // fechaMovimiento es manejado por @PrePersist en la entidad Movimiento...

        MovimientoInventarioProducto savedMovimiento = movimientoInventarioProductoRepository.save(movimiento);
        log.info("Movimiento ID {} registrado. Nuevo stock para inventario ID {}: {}", savedMovimiento.getIdMovimientoProducto(), inventario.getIdInventarioProducto(), inventario.getCantidadStock());
        return mapMovimientoToResponseDTO(savedMovimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MovimientoInventarioProductoResponseDTO> findMovimientosByInventarioProductoId(Integer idInventarioProducto, Pageable pageable) {
        log.info("Buscando movimientos para inventario producto ID {}", idInventarioProducto);
        if (!inventarioProductoRepository.existsById(idInventarioProducto)) {
            throw new ResourceNotFoundException("InventarioProducto", "id", idInventarioProducto);
        }
        Page<MovimientoInventarioProducto> movimientos = movimientoInventarioProductoRepository.findByInventarioProductoIdInventarioProducto(idInventarioProducto, pageable);
        return movimientos.map(this::mapMovimientoToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getStockActual(Integer idInventarioProducto) {
        log.info("Consultando stock actual para inventario producto ID {}", idInventarioProducto);
        InventarioProducto inventario = findInventarioProductoEntityById(idInventarioProducto);
        return inventario.getCantidadStock();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioProductoResponseDTO> findInventariosByProductoId(Integer idProducto) {
        log.info("Buscando todos los registros de inventario para el producto ID {}", idProducto);
        if (!productoRepository.existsById(idProducto)) {
            throw new ResourceNotFoundException("Producto", "id", idProducto);
        }
        List<InventarioProducto> inventarios = inventarioProductoRepository.findByProductoIdProducto(idProducto);
        return inventarios.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }


    // --- Métodos de Ayuda y Mapeo Privados ---
    private InventarioProducto findInventarioProductoEntityById(Integer id) {
        return inventarioProductoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventarioProducto", "id", id));
    }

    private InventarioProductoResponseDTO mapToResponseDTO(InventarioProducto entity) {
        ProductoSummaryDTO productoSummary = null;
        if (entity.getProducto() != null) {
            Producto p = entity.getProducto();
            productoSummary = new ProductoSummaryDTO(p.getIdProducto(), p.getReferenciaProducto(), p.getNombreProducto(), p.getTallaProducto(), p.getColorProducto());
        }
        return new InventarioProductoResponseDTO(
                entity.getIdInventarioProducto(),
                entity.getUbicacionInventario(),
                productoSummary,
                entity.getCantidadStock(),
                entity.getUltimaActualizacion()
        );
    }

    private MovimientoInventarioProductoResponseDTO mapMovimientoToResponseDTO(MovimientoInventarioProducto entity) {
        InventarioProductoRefDTO inventarioRef = null;
        if (entity.getInventarioProducto() != null) {
            InventarioProducto ip = entity.getInventarioProducto();
            String prodRef = (ip.getProducto() != null) ? ip.getProducto().getReferenciaProducto() : "N/A";
            inventarioRef = new InventarioProductoRefDTO(ip.getIdInventarioProducto(), ip.getUbicacionInventario(), prodRef);
        }
        return new MovimientoInventarioProductoResponseDTO(
                entity.getIdMovimientoProducto(),
                entity.getTipoMovimiento(),
                inventarioRef,
                entity.getCantidadMovimiento(),
                entity.getFechaMovimiento(),
                entity.getDescripcionMovimiento()
        );
    }
}