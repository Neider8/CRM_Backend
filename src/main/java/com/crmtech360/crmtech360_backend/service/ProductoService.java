package com.crmtech360.crmtech360_backend.service;

import com.crmtech360.crmtech360_backend.dto.InsumoPorProductoRequestDTO;
import com.crmtech360.crmtech360_backend.dto.InsumoPorProductoResponseDTO;
import com.crmtech360.crmtech360_backend.dto.ProductoCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.ProductoResponseDTO;
import com.crmtech360.crmtech360_backend.dto.ProductoUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductoService {

    ProductoResponseDTO createProducto(ProductoCreateRequestDTO productoCreateRequestDTO);

    Page<ProductoResponseDTO> findAllProductos(Pageable pageable);

    ProductoResponseDTO findProductoById(Integer id);

    ProductoResponseDTO findProductoByReferencia(String referencia);

    ProductoResponseDTO updateProducto(Integer id, ProductoUpdateRequestDTO productoUpdateRequestDTO);

    void deleteProducto(Integer id);

    // --- Métodos para gestionar Insumos por Producto (BOM) ---

    InsumoPorProductoResponseDTO addInsumoToProducto(Integer idProducto, InsumoPorProductoRequestDTO bomItemDto);

    InsumoPorProductoResponseDTO updateInsumoInProducto(Integer idProducto, Integer idInsumo, InsumoPorProductoRequestDTO bomItemDto);

    void removeInsumoFromProducto(Integer idProducto, Integer idInsumo);

    List<InsumoPorProductoResponseDTO> getInsumosForProducto(Integer idProducto);

}