package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.dto.*;
import com.crmtech360.crmtech360_backend.entity.Insumo;
import com.crmtech360.crmtech360_backend.entity.InsumoPorProducto;
import com.crmtech360.crmtech360_backend.entity.InsumoPorProductoId;
import com.crmtech360.crmtech360_backend.entity.Producto;
import com.crmtech360.crmtech360_backend.repository.InsumoPorProductoRepository;
import com.crmtech360.crmtech360_backend.repository.InsumoRepository;
import com.crmtech360.crmtech360_backend.repository.ProductoRepository;
import com.crmtech360.crmtech360_backend.service.ProductoService;
import com.crmtech360.crmtech360_backend.exception.ResourceNotFoundException; // Placeholder
import com.crmtech360.crmtech360_backend.exception.DuplicateResourceException; // Placeholder
import com.crmtech360.crmtech360_backend.exception.BadRequestException; // Placeholder
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;
    private final InsumoRepository insumoRepository; // Necesario para validar insumos
    private final InsumoPorProductoRepository insumoPorProductoRepository; // Para gestionar BOM

    public ProductoServiceImpl(ProductoRepository productoRepository,
                               InsumoRepository insumoRepository,
                               InsumoPorProductoRepository insumoPorProductoRepository) {
        this.productoRepository = productoRepository;
        this.insumoRepository = insumoRepository;
        this.insumoPorProductoRepository = insumoPorProductoRepository;
    }

    @Override
    public ProductoResponseDTO createProducto(ProductoCreateRequestDTO productoCreateRequestDTO) {
        log.info("Intentando crear producto con referencia: {}", productoCreateRequestDTO.getReferenciaProducto());
        productoRepository.findByReferenciaProducto(productoCreateRequestDTO.getReferenciaProducto())
                .ifPresent(existing -> {
                    log.warn("Intento de crear producto duplicado por referencia: {}", productoCreateRequestDTO.getReferenciaProducto());
                    throw new DuplicateResourceException("Producto", "referenciaProducto", productoCreateRequestDTO.getReferenciaProducto());
                });

        Producto producto = mapToEntity(productoCreateRequestDTO);
        Producto savedProducto = productoRepository.save(producto);
        log.info("Producto creado con ID: {}", savedProducto.getIdProducto());
        return mapToResponseDTO(savedProducto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoResponseDTO> findAllProductos(Pageable pageable) {
        log.info("Buscando todos los productos, página: {}, tamaño: {}", pageable.getPageNumber(), pageable.getPageSize());
        return productoRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO findProductoById(Integer id) {
        log.info("Buscando producto con ID: {}", id);
        Producto producto = findProductoEntityById(id);
        return mapToResponseDTO(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO findProductoByReferencia(String referencia) {
        log.info("Buscando producto con referencia: {}", referencia);
        Producto producto = productoRepository.findByReferenciaProducto(referencia)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado con referencia: {}", referencia);
                    return new ResourceNotFoundException("Producto", "referenciaProducto", referencia);
                });
        return mapToResponseDTO(producto);
    }

    @Override
    public ProductoResponseDTO updateProducto(Integer id, ProductoUpdateRequestDTO updateDTO) {
        log.info("Intentando actualizar producto con ID: {}", id);
        Producto productoToUpdate = findProductoEntityById(id);

        // Actualizar campos permitidos
        if (updateDTO.getNombreProducto() != null) {
            productoToUpdate.setNombreProducto(updateDTO.getNombreProducto());
        }
        if (updateDTO.getDescripcionProducto() != null) {
            productoToUpdate.setDescripcionProducto(updateDTO.getDescripcionProducto());
        }
        if (updateDTO.getTallaProducto() != null) {
            productoToUpdate.setTallaProducto(updateDTO.getTallaProducto());
        }
        if (updateDTO.getColorProducto() != null) {
            productoToUpdate.setColorProducto(updateDTO.getColorProducto());
        }
        if (updateDTO.getTipoProducto() != null) {
            productoToUpdate.setTipoProducto(updateDTO.getTipoProducto());
        }
        if (updateDTO.getGeneroProducto() != null) {
            productoToUpdate.setGeneroProducto(updateDTO.getGeneroProducto());
        }
        if (updateDTO.getCostoProduccion() != null) {
            productoToUpdate.setCostoProduccion(updateDTO.getCostoProduccion());
        }
        if (updateDTO.getPrecioVenta() != null) {
            productoToUpdate.setPrecioVenta(updateDTO.getPrecioVenta());
        }
        if (updateDTO.getUnidadMedidaProducto() != null) {
            productoToUpdate.setUnidadMedidaProducto(updateDTO.getUnidadMedidaProducto());
        }
        // Referencia no se actualiza
        // Fechas manejadas por @PreUpdate

        Producto updatedProducto = productoRepository.save(productoToUpdate);
        log.info("Producto actualizado con ID: {}", updatedProducto.getIdProducto());
        return mapToResponseDTO(updatedProducto);
    }

    @Override
    public void deleteProducto(Integer id) {
        log.info("Intentando eliminar producto con ID: {}", id);
        Producto producto = findProductoEntityById(id);
        // Considerar relaciones: DetallesOrdenVenta, InsumosPorProducto, InventarioProductos
        // ON DELETE CASCADE o RESTRICT en BD deben manejarse. Si no, verificar aquí.
        // Por ejemplo, no permitir borrar si hay stock o está en órdenes activas.
        // (Simplificación: asumimos que la BD o futuras validaciones lo manejan)
        productoRepository.delete(producto);
        log.info("Producto eliminado con ID: {}", id);
    }


    // --- Implementación Métodos BOM ---

    @Override
    public InsumoPorProductoResponseDTO addInsumoToProducto(Integer idProducto, InsumoPorProductoRequestDTO bomItemDto) {
        log.info("Añadiendo insumo ID {} al producto ID {}", bomItemDto.getIdInsumo(), idProducto);
        Producto producto = findProductoEntityById(idProducto);
        Insumo insumo = findInsumoEntityById(bomItemDto.getIdInsumo());

        // Verificar que el DTO sea consistente si incluye IDs redundantes
        if (bomItemDto.getIdProducto() != null && !bomItemDto.getIdProducto().equals(idProducto)) {
            throw new BadRequestException("ID del producto en el item no coincide con el ID del producto en la ruta.");
        }

        InsumoPorProductoId bomId = new InsumoPorProductoId(idProducto, insumo.getIdInsumo());
        if (insumoPorProductoRepository.existsById(bomId)) {
            log.warn("Intento de añadir insumo duplicado ID {} al producto ID {}", insumo.getIdInsumo(), idProducto);
            throw new DuplicateResourceException("InsumoPorProducto", "producto/insumo", idProducto + "/" + insumo.getIdInsumo());
        }

        InsumoPorProducto bomEntity = new InsumoPorProducto();
        bomEntity.setId(bomId);
        bomEntity.setProducto(producto);
        bomEntity.setInsumo(insumo);
        bomEntity.setCantidadRequerida(bomItemDto.getCantidadRequerida());

        InsumoPorProducto savedBomItem = insumoPorProductoRepository.save(bomEntity);
        log.info("Insumo ID {} añadido al BOM del producto ID {}", insumo.getIdInsumo(), idProducto);
        return mapBomItemToResponseDTO(savedBomItem);
    }

    @Override
    public InsumoPorProductoResponseDTO updateInsumoInProducto(Integer idProducto, Integer idInsumo, InsumoPorProductoRequestDTO bomItemDto) {
        log.info("Actualizando cantidad del insumo ID {} en producto ID {}", idInsumo, idProducto);
        // Verificar que el DTO sea consistente
        if ((bomItemDto.getIdProducto() != null && !bomItemDto.getIdProducto().equals(idProducto)) ||
                (bomItemDto.getIdInsumo() != null && !bomItemDto.getIdInsumo().equals(idInsumo))) {
            throw new BadRequestException("IDs en el item no coinciden con los IDs en la ruta.");
        }

        InsumoPorProductoId bomId = new InsumoPorProductoId(idProducto, idInsumo);
        InsumoPorProducto bomEntity = insumoPorProductoRepository.findById(bomId)
                .orElseThrow(() -> new ResourceNotFoundException("InsumoPorProducto", "producto/insumo", idProducto + "/" + idInsumo));

        bomEntity.setCantidadRequerida(bomItemDto.getCantidadRequerida());
        InsumoPorProducto updatedBomItem = insumoPorProductoRepository.save(bomEntity);
        log.info("Cantidad actualizada para insumo ID {} en producto ID {}", idInsumo, idProducto);
        return mapBomItemToResponseDTO(updatedBomItem);
    }

    @Override
    public void removeInsumoFromProducto(Integer idProducto, Integer idInsumo) {
        log.info("Eliminando insumo ID {} del BOM del producto ID {}", idInsumo, idProducto);
        InsumoPorProductoId bomId = new InsumoPorProductoId(idProducto, idInsumo);
        if (!insumoPorProductoRepository.existsById(bomId)) {
            throw new ResourceNotFoundException("InsumoPorProducto", "producto/insumo", idProducto + "/" + idInsumo);
        }
        insumoPorProductoRepository.deleteById(bomId);
        log.info("Insumo ID {} eliminado del BOM del producto ID {}", idInsumo, idProducto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsumoPorProductoResponseDTO> getInsumosForProducto(Integer idProducto) {
        log.info("Buscando BOM para producto ID {}", idProducto);
        // Verificar que el producto existe
        findProductoEntityById(idProducto);
        List<InsumoPorProducto> bomList = insumoPorProductoRepository.findByIdIdProducto(idProducto);
        return bomList.stream()
                .map(this::mapBomItemToResponseDTO)
                .collect(Collectors.toList());
    }


    // --- Métodos de Ayuda y Mapeo Privados ---

    private Producto findProductoEntityById(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Producto", "id", id);
                });
    }

    private Insumo findInsumoEntityById(Integer id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Insumo no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Insumo", "id", id);
                });
    }

    private Producto mapToEntity(ProductoCreateRequestDTO dto) {
        Producto entity = new Producto();
        entity.setReferenciaProducto(dto.getReferenciaProducto());
        entity.setNombreProducto(dto.getNombreProducto());
        entity.setDescripcionProducto(dto.getDescripcionProducto());
        entity.setTallaProducto(dto.getTallaProducto());
        entity.setColorProducto(dto.getColorProducto());
        entity.setTipoProducto(dto.getTipoProducto());
        entity.setGeneroProducto(dto.getGeneroProducto());
        entity.setCostoProduccion(dto.getCostoProduccion());
        entity.setPrecioVenta(dto.getPrecioVenta());
        entity.setUnidadMedidaProducto(dto.getUnidadMedidaProducto() != null ? dto.getUnidadMedidaProducto() : "Unidad");
        entity.setInsumosPorProducto(new HashSet<>()); // Inicializar colección
        return entity;
    }

    private ProductoResponseDTO mapToResponseDTO(Producto entity) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setIdProducto(entity.getIdProducto());
        dto.setReferenciaProducto(entity.getReferenciaProducto());
        dto.setNombreProducto(entity.getNombreProducto());
        dto.setDescripcionProducto(entity.getDescripcionProducto());
        dto.setTallaProducto(entity.getTallaProducto());
        dto.setColorProducto(entity.getColorProducto());
        dto.setTipoProducto(entity.getTipoProducto());
        dto.setGeneroProducto(entity.getGeneroProducto());
        dto.setCostoProduccion(entity.getCostoProduccion());
        dto.setPrecioVenta(entity.getPrecioVenta());
        dto.setUnidadMedidaProducto(entity.getUnidadMedidaProducto());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        return dto;
    }

    private InsumoPorProductoResponseDTO mapBomItemToResponseDTO(InsumoPorProducto bomEntity) {
        InsumoPorProductoResponseDTO dto = new InsumoPorProductoResponseDTO();
        dto.setIdProducto(bomEntity.getProducto().getIdProducto());
        dto.setReferenciaProducto(bomEntity.getProducto().getReferenciaProducto()); // Útil para mostrar
        dto.setNombreProducto(bomEntity.getProducto().getNombreProducto()); // Útil para mostrar
        dto.setIdInsumo(bomEntity.getInsumo().getIdInsumo());
        dto.setNombreInsumo(bomEntity.getInsumo().getNombreInsumo()); // Útil para mostrar
        dto.setUnidadMedidaInsumo(bomEntity.getInsumo().getUnidadMedidaInsumo()); // Útil para mostrar
        dto.setCantidadRequerida(bomEntity.getCantidadRequerida());
        return dto;
    }

}