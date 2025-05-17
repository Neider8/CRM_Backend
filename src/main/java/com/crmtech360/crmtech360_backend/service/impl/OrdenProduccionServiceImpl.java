package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.dto.*;
import com.crmtech360.crmtech360_backend.entity.*;
import com.crmtech360.crmtech360_backend.repository.*;
import com.crmtech360.crmtech360_backend.service.OrdenProduccionService;
import com.crmtech360.crmtech360_backend.exception.BadRequestException;
import com.crmtech360.crmtech360_backend.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate; // <--- AÑADIR ESTE IMPORT
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrdenProduccionServiceImpl implements OrdenProduccionService {

    private static final Logger log = LoggerFactory.getLogger(OrdenProduccionServiceImpl.class);

    private final OrdenProduccionRepository ordenProduccionRepository;
    private final TareaProduccionRepository tareaProduccionRepository;
    private final OrdenVentaRepository ordenVentaRepository;
    private final EmpleadoRepository empleadoRepository;

    public OrdenProduccionServiceImpl(OrdenProduccionRepository ordenProduccionRepository,
                                      TareaProduccionRepository tareaProduccionRepository,
                                      OrdenVentaRepository ordenVentaRepository,
                                      EmpleadoRepository empleadoRepository) {
        this.ordenProduccionRepository = ordenProduccionRepository;
        this.tareaProduccionRepository = tareaProduccionRepository;
        this.ordenVentaRepository = ordenVentaRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public OrdenProduccionResponseDTO createOrdenProduccion(OrdenProduccionCreateRequestDTO createRequestDTO) {
        log.info("Creando nueva orden de producción para orden de venta ID: {}", createRequestDTO.getIdOrdenVenta());

        OrdenVenta ordenVenta = ordenVentaRepository.findById(createRequestDTO.getIdOrdenVenta())
                .orElseThrow(() -> new ResourceNotFoundException("OrdenVenta", "id", createRequestDTO.getIdOrdenVenta()));

        if (!"Confirmada".equals(ordenVenta.getEstadoOrden()) && !"En Producción".equals(ordenVenta.getEstadoOrden())) {
            log.warn("Intento de crear orden de producción para orden de venta ID {} con estado {}", ordenVenta.getIdOrdenVenta(), ordenVenta.getEstadoOrden());
            throw new BadRequestException("La orden de venta debe estar en estado 'Confirmada' para iniciar producción.");
        }

        OrdenProduccion ordenProduccion = new OrdenProduccion();
        ordenProduccion.setOrdenVenta(ordenVenta);
        ordenProduccion.setFechaInicioProduccion(createRequestDTO.getFechaInicioProduccion()); // Asume que el DTO usa LocalDate
        ordenProduccion.setFechaFinEstimadaProduccion(createRequestDTO.getFechaFinEstimadaProduccion()); // Asume que el DTO usa LocalDate
        ordenProduccion.setObservacionesProduccion(createRequestDTO.getObservacionesProduccion());
        ordenProduccion.setEstadoProduccion("Pendiente");
        ordenProduccion.setTareasProduccion(new HashSet<>());

        if (!"En Producción".equals(ordenVenta.getEstadoOrden())) {
            ordenVenta.setEstadoOrden("En Producción");
            ordenVentaRepository.save(ordenVenta);
        }

        OrdenProduccion savedOrdenProduccion = ordenProduccionRepository.save(ordenProduccion);
        log.info("Orden de producción creada con ID: {}", savedOrdenProduccion.getIdOrdenProduccion());
        return mapToResponseDTO(savedOrdenProduccion);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrdenProduccionResponseDTO> findAllOrdenesProduccion(Pageable pageable) {
        log.info("Buscando todas las órdenes de producción, página: {}, tamaño: {}", pageable.getPageNumber(), pageable.getPageSize());
        return ordenProduccionRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenProduccionResponseDTO> findOrdenesProduccionByOrdenVentaId(Integer idOrdenVenta) {
        log.info("Buscando órdenes de producción para la orden de venta ID: {}", idOrdenVenta);
        if (!ordenVentaRepository.existsById(idOrdenVenta)) {
            throw new ResourceNotFoundException("OrdenVenta", "id", idOrdenVenta);
        }
        List<OrdenProduccion> ordenes = ordenProduccionRepository.findByOrdenVentaIdOrdenVenta(idOrdenVenta);
        return ordenes.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenProduccionResponseDTO findOrdenProduccionById(Integer id) {
        log.info("Buscando orden de producción con ID: {}", id);
        OrdenProduccion ordenProduccion = findOrdenProduccionEntityById(id);
        return mapToResponseDTO(ordenProduccion);
    }

    @Override
    public OrdenProduccionResponseDTO updateOrdenProduccionHeader(Integer id, OrdenProduccionUpdateRequestDTO updateRequestDTO) {
        log.info("Actualizando cabecera de orden de producción ID: {}", id);
        OrdenProduccion ordenProduccion = findOrdenProduccionEntityById(id);

        if ("Anulada".equals(ordenProduccion.getEstadoProduccion()) || "Terminada".equals(ordenProduccion.getEstadoProduccion())) {
            throw new BadRequestException("No se puede modificar una orden de producción en estado '" + ordenProduccion.getEstadoProduccion() + "'.");
        }

        if (updateRequestDTO.getFechaInicioProduccion() != null) {
            ordenProduccion.setFechaInicioProduccion(updateRequestDTO.getFechaInicioProduccion()); // Asume que el DTO usa LocalDate
        }
        if (updateRequestDTO.getFechaFinEstimadaProduccion() != null) {
            ordenProduccion.setFechaFinEstimadaProduccion(updateRequestDTO.getFechaFinEstimadaProduccion()); // Asume que el DTO usa LocalDate
        }
        if (updateRequestDTO.getFechaFinRealProduccion() != null) {
            ordenProduccion.setFechaFinRealProduccion(updateRequestDTO.getFechaFinRealProduccion()); // Asume que el DTO usa LocalDate
            if (!"Anulada".equals(ordenProduccion.getEstadoProduccion()) && updateRequestDTO.getEstadoProduccion() == null) {
                ordenProduccion.setEstadoProduccion("Terminada");
            }
        }
        if (updateRequestDTO.getEstadoProduccion() != null) {
            ordenProduccion.setEstadoProduccion(updateRequestDTO.getEstadoProduccion());
        }
        if (updateRequestDTO.getObservacionesProduccion() != null) {
            ordenProduccion.setObservacionesProduccion(updateRequestDTO.getObservacionesProduccion());
        }

        OrdenProduccion updatedOrdenProduccion = ordenProduccionRepository.save(ordenProduccion);
        log.info("Cabecera de orden de producción ID: {} actualizada.", updatedOrdenProduccion.getIdOrdenProduccion());
        return mapToResponseDTO(updatedOrdenProduccion);
    }

    @Override
    public TareaProduccionResponseDTO addTareaToOrdenProduccion(Integer idOrdenProduccion, TareaProduccionCreateRequestDTO tareaRequestDTO) {
        log.info("Añadiendo tarea a orden de producción ID: {}", idOrdenProduccion);
        OrdenProduccion ordenProduccion = findOrdenProduccionEntityById(idOrdenProduccion);

        if ("Anulada".equals(ordenProduccion.getEstadoProduccion()) || "Terminada".equals(ordenProduccion.getEstadoProduccion())) {
            throw new BadRequestException("No se pueden añadir tareas a una orden de producción en estado '" + ordenProduccion.getEstadoProduccion() + "'.");
        }
        if (tareaRequestDTO.getIdOrdenProduccion() != null && !tareaRequestDTO.getIdOrdenProduccion().equals(idOrdenProduccion)) {
            throw new BadRequestException("El ID de la orden de producción en el cuerpo de la tarea no coincide con el ID en la ruta.");
        }

        TareaProduccion nuevaTarea = new TareaProduccion();
        nuevaTarea.setOrdenProduccion(ordenProduccion);
        nuevaTarea.setNombreTarea(tareaRequestDTO.getNombreTarea());
        nuevaTarea.setDuracionEstimadaTarea(tareaRequestDTO.getDuracionEstimadaTarea());
        nuevaTarea.setObservacionesTarea(tareaRequestDTO.getObservacionesTarea());
        nuevaTarea.setEstadoTarea("Pendiente");

        if (tareaRequestDTO.getIdEmpleado() != null) {
            Empleado empleado = empleadoRepository.findById(tareaRequestDTO.getIdEmpleado())
                    .orElseThrow(() -> new ResourceNotFoundException("Empleado", "id", tareaRequestDTO.getIdEmpleado()));
            nuevaTarea.setEmpleado(empleado);
        }

        if ("Pendiente".equals(ordenProduccion.getEstadoProduccion())) {
            ordenProduccion.setEstadoProduccion("En Proceso");
            if (ordenProduccion.getFechaInicioProduccion() == null) {
                ordenProduccion.setFechaInicioProduccion(LocalDate.now()); // USA LocalDate
            }
            ordenProduccionRepository.save(ordenProduccion);
        }

        TareaProduccion savedTarea = tareaProduccionRepository.save(nuevaTarea);
        log.info("Tarea ID: {} añadida a orden de producción ID: {}", savedTarea.getIdTareaProduccion(), idOrdenProduccion);
        return mapTareaToResponseDTO(savedTarea);
    }

    @Override
    public TareaProduccionResponseDTO updateTareaInOrdenProduccion(Integer idOrdenProduccion, Integer idTarea, TareaProduccionUpdateRequestDTO tareaRequestDTO) {
        log.info("Actualizando tarea ID: {} en orden de producción ID: {}", idTarea, idOrdenProduccion);
        OrdenProduccion ordenProduccion = findOrdenProduccionEntityById(idOrdenProduccion);
        if ("Anulada".equals(ordenProduccion.getEstadoProduccion()) || "Terminada".equals(ordenProduccion.getEstadoProduccion())) {
            throw new BadRequestException("No se pueden actualizar tareas de una orden de producción en estado '" + ordenProduccion.getEstadoProduccion() + "'.");
        }

        // LLAMADA CORREGIDA
        TareaProduccion tareaToUpdate = tareaProduccionRepository.findByIdTareaProduccionAndOrdenProduccion_IdOrdenProduccion(idTarea, idOrdenProduccion)
                .orElseThrow(() -> new ResourceNotFoundException("TareaProduccion", "idTarea/idOrdenProduccion", idTarea + "/" + idOrdenProduccion));

        if (tareaRequestDTO.getIdEmpleado() != null) {
            Empleado empleado = empleadoRepository.findById(tareaRequestDTO.getIdEmpleado())
                    .orElseThrow(() -> new ResourceNotFoundException("Empleado", "id", tareaRequestDTO.getIdEmpleado()));
            tareaToUpdate.setEmpleado(empleado);
        } // Considerar lógica para desasignar empleado si idEmpleado es null en el DTO

        if (tareaRequestDTO.getNombreTarea() != null) {
            tareaToUpdate.setNombreTarea(tareaRequestDTO.getNombreTarea());
        }
        if (tareaRequestDTO.getFechaInicioTarea() != null) {
            tareaToUpdate.setFechaInicioTarea(tareaRequestDTO.getFechaInicioTarea());
        }
        if (tareaRequestDTO.getFechaFinTarea() != null) {
            tareaToUpdate.setFechaFinTarea(tareaRequestDTO.getFechaFinTarea());
            if (!"Bloqueada".equals(tareaToUpdate.getEstadoTarea()) && tareaRequestDTO.getEstadoTarea() == null) {
                tareaToUpdate.setEstadoTarea("Completada");
            }
        }
        if (tareaRequestDTO.getDuracionEstimadaTarea() != null) {
            tareaToUpdate.setDuracionEstimadaTarea(tareaRequestDTO.getDuracionEstimadaTarea());
        }
        if (tareaRequestDTO.getDuracionRealTarea() != null) {
            tareaToUpdate.setDuracionRealTarea(tareaRequestDTO.getDuracionRealTarea());
        }
        if (tareaRequestDTO.getEstadoTarea() != null) {
            tareaToUpdate.setEstadoTarea(tareaRequestDTO.getEstadoTarea());
        }
        if (tareaRequestDTO.getObservacionesTarea() != null) {
            tareaToUpdate.setObservacionesTarea(tareaRequestDTO.getObservacionesTarea());
        }

        TareaProduccion updatedTarea = tareaProduccionRepository.save(tareaToUpdate);
        log.info("Tarea ID: {} actualizada en orden de producción ID: {}", updatedTarea.getIdTareaProduccion(), idOrdenProduccion);
        return mapTareaToResponseDTO(updatedTarea);
    }

    @Override
    public void removeTareaFromOrdenProduccion(Integer idOrdenProduccion, Integer idTarea) {
        log.info("Eliminando tarea ID: {} de orden de producción ID: {}", idTarea, idOrdenProduccion);
        OrdenProduccion ordenProduccion = findOrdenProduccionEntityById(idOrdenProduccion);
        if ("Anulada".equals(ordenProduccion.getEstadoProduccion()) || "Terminada".equals(ordenProduccion.getEstadoProduccion())) {
            throw new BadRequestException("No se pueden eliminar tareas de una orden de producción en estado '" + ordenProduccion.getEstadoProduccion() + "'.");
        }

        // LLAMADA CORREGIDA
        TareaProduccion tareaToRemove = tareaProduccionRepository.findByIdTareaProduccionAndOrdenProduccion_IdOrdenProduccion(idTarea, idOrdenProduccion)
                .orElseThrow(() -> new ResourceNotFoundException("TareaProduccion", "idTarea/idOrdenProduccion", idTarea + "/" + idOrdenProduccion));

        ordenProduccion.getTareasProduccion().remove(tareaToRemove);
        tareaProduccionRepository.delete(tareaToRemove);
        log.info("Tarea ID: {} eliminada de orden de producción ID: {}", idTarea, idOrdenProduccion);
    }

    @Override
    public void anularOrdenProduccion(Integer id) {
        log.info("Anulando orden de producción ID: {}", id);
        OrdenProduccion ordenProduccion = findOrdenProduccionEntityById(id);

        if ("Terminada".equals(ordenProduccion.getEstadoProduccion())) {
            throw new BadRequestException("No se puede anular una orden de producción ya terminada.");
        }
        ordenProduccion.getTareasProduccion().forEach(tarea -> {
            if ("Pendiente".equals(tarea.getEstadoTarea()) || "En Curso".equals(tarea.getEstadoTarea())) {
                tarea.setEstadoTarea("Bloqueada");
                tarea.setObservacionesTarea((tarea.getObservacionesTarea() != null ? tarea.getObservacionesTarea() : "") + " Anulada debido a anulación de OP.");
                tareaProduccionRepository.save(tarea);
            }
        });

        ordenProduccion.setEstadoProduccion("Anulada");
        ordenProduccionRepository.save(ordenProduccion);
        log.info("Orden de producción ID: {} anulada.", id);
    }

    private OrdenProduccion findOrdenProduccionEntityById(Integer id) {
        return ordenProduccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrdenProduccion", "id", id));
    }

    private OrdenProduccionResponseDTO mapToResponseDTO(OrdenProduccion entity) {
        OrdenProduccionResponseDTO dto = new OrdenProduccionResponseDTO();
        dto.setIdOrdenProduccion(entity.getIdOrdenProduccion());

        if (entity.getOrdenVenta() != null) {
            OrdenVenta ov = entity.getOrdenVenta();
            String clienteNombre = (ov.getCliente() != null) ? ov.getCliente().getNombreCliente() : "N/A";
            // Asegúrate que OrdenVentaSummaryDTO use LocalDateTime si fechaPedido es LocalDateTime
            dto.setOrdenVenta(new OrdenVentaSummaryDTO(ov.getIdOrdenVenta(), ov.getFechaPedido(), clienteNombre));
        }

        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaInicioProduccion(entity.getFechaInicioProduccion()); // Es LocalDate
        dto.setFechaFinEstimadaProduccion(entity.getFechaFinEstimadaProduccion()); // Es LocalDate
        dto.setFechaFinRealProduccion(entity.getFechaFinRealProduccion()); // Es LocalDate
        dto.setEstadoProduccion(entity.getEstadoProduccion());
        dto.setObservacionesProduccion(entity.getObservacionesProduccion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());

        List<TareaProduccionResponseDTO> tareasDTO = (entity.getTareasProduccion() == null) ? new ArrayList<>() :
                entity.getTareasProduccion().stream()
                        .map(this::mapTareaToResponseDTO)
                        .collect(Collectors.toList());
        dto.setTareas(tareasDTO);
        return dto;
    }

    private TareaProduccionResponseDTO mapTareaToResponseDTO(TareaProduccion tarea) {
        if (tarea == null) return null;
        TareaProduccionResponseDTO dto = new TareaProduccionResponseDTO();
        dto.setIdTareaProduccion(tarea.getIdTareaProduccion());
        if(tarea.getOrdenProduccion() != null){
            dto.setIdOrdenProduccion(tarea.getOrdenProduccion().getIdOrdenProduccion());
        }

        if (tarea.getEmpleado() != null) {
            Empleado emp = tarea.getEmpleado();
            dto.setEmpleado(new EmpleadoSummaryDTO(emp.getIdEmpleado(), emp.getNumeroDocumento(), emp.getNombreEmpleado()));
        }

        dto.setNombreTarea(tarea.getNombreTarea());
        dto.setFechaInicioTarea(tarea.getFechaInicioTarea()); // Es LocalDateTime
        dto.setFechaFinTarea(tarea.getFechaFinTarea()); // Es LocalDateTime
        dto.setDuracionEstimadaTarea(tarea.getDuracionEstimadaTarea()); // Es LocalTime
        dto.setDuracionRealTarea(tarea.getDuracionRealTarea()); // Es LocalTime
        dto.setEstadoTarea(tarea.getEstadoTarea());
        dto.setObservacionesTarea(tarea.getObservacionesTarea());
        return dto;
    }
}