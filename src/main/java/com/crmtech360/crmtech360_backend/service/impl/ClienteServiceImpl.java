package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.dto.*;
import com.crmtech360.crmtech360_backend.entity.Cliente;
import com.crmtech360.crmtech360_backend.entity.ContactoCliente;
import com.crmtech360.crmtech360_backend.repository.ClienteRepository;
import com.crmtech360.crmtech360_backend.repository.ContactoClienteRepository;
import com.crmtech360.crmtech360_backend.service.ClienteService;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteServiceImpl.class);

    private final ClienteRepository clienteRepository;
    private final ContactoClienteRepository contactoClienteRepository; // Inyectar también este

    public ClienteServiceImpl(ClienteRepository clienteRepository, ContactoClienteRepository contactoClienteRepository) {
        this.clienteRepository = clienteRepository;
        this.contactoClienteRepository = contactoClienteRepository;
    }

    @Override
    public ClienteResponseDTO createCliente(ClienteRequestDTO clienteRequestDTO) {
        log.info("Intentando crear cliente con documento: {}", clienteRequestDTO.getNumeroDocumento());
        clienteRepository.findByNumeroDocumento(clienteRequestDTO.getNumeroDocumento())
                .ifPresent(existing -> {
                    log.warn("Intento de crear cliente duplicado por documento: {}", clienteRequestDTO.getNumeroDocumento());
                    throw new DuplicateResourceException("Cliente", "numeroDocumento", clienteRequestDTO.getNumeroDocumento());
                });

        Cliente cliente = mapToEntity(clienteRequestDTO);
        Cliente savedCliente = clienteRepository.save(cliente);
        log.info("Cliente creado con ID: {}", savedCliente.getIdCliente());
        return mapToResponseDTO(savedCliente); // Mapear a DTO incluyendo contactos vacíos inicialmente
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> findAllClientes(Pageable pageable) {
        log.info("Buscando todos los clientes, página: {}, tamaño: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Cliente> clientePage = clienteRepository.findAll(pageable);
        return clientePage.map(this::mapToResponseDTO); // map preserva la paginación
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO findClienteById(Integer id) {
        log.info("Buscando cliente con ID: {}", id);
        Cliente cliente = findClienteEntityById(id);
        return mapToResponseDTO(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO findClienteByNumeroDocumento(String numeroDocumento) {
        log.info("Buscando cliente con documento: {}", numeroDocumento);
        Cliente cliente = clienteRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> {
                    log.warn("Cliente no encontrado con documento: {}", numeroDocumento);
                    return new ResourceNotFoundException("Cliente", "numeroDocumento", numeroDocumento);
                });
        return mapToResponseDTO(cliente);
    }

    @Override
    public ClienteResponseDTO updateCliente(Integer id, ClienteRequestDTO clienteRequestDTO) {
        log.info("Intentando actualizar cliente con ID: {}", id);
        Cliente clienteToUpdate = findClienteEntityById(id);

        // Verificar si el número de documento se está cambiando y si ya existe en otro cliente
        if (!clienteToUpdate.getNumeroDocumento().equals(clienteRequestDTO.getNumeroDocumento())) {
            clienteRepository.findByNumeroDocumento(clienteRequestDTO.getNumeroDocumento())
                    .ifPresent(existing -> {
                        log.warn("Intento de actualizar a número de documento duplicado: {}", clienteRequestDTO.getNumeroDocumento());
                        throw new DuplicateResourceException("Cliente", "numeroDocumento", clienteRequestDTO.getNumeroDocumento());
                    });
            clienteToUpdate.setNumeroDocumento(clienteRequestDTO.getNumeroDocumento());
        }

        // Actualizar otros campos
        clienteToUpdate.setTipoDocumento(clienteRequestDTO.getTipoDocumento());
        clienteToUpdate.setNombreCliente(clienteRequestDTO.getNombreCliente());
        clienteToUpdate.setDireccionCliente(clienteRequestDTO.getDireccionCliente());
        clienteToUpdate.setTelefonoCliente(clienteRequestDTO.getTelefonoCliente());
        clienteToUpdate.setCorreoCliente(clienteRequestDTO.getCorreoCliente());
        // Fechas de creación/actualización manejadas por @PrePersist/@PreUpdate

        Cliente updatedCliente = clienteRepository.save(clienteToUpdate);
        log.info("Cliente actualizado con ID: {}", updatedCliente.getIdCliente());
        return mapToResponseDTO(updatedCliente);
    }

    @Override
    public void deleteCliente(Integer id) {
        log.info("Intentando eliminar cliente con ID: {}", id);
        Cliente cliente = findClienteEntityById(id);
        // ON DELETE CASCADE en ContactosCliente y OrdenesVenta debería funcionar en BD.
        // Si no, se necesitaría lógica adicional aquí o manejo de excepciones de integridad.
        clienteRepository.delete(cliente);
        log.info("Cliente eliminado con ID: {}", id);
    }

    // --- Implementación de Métodos para Contactos ---

    @Override
    public ContactoClienteResponseDTO addContactoToCliente(Integer idCliente, ContactoClienteRequestDTO contactoDTO) {
        log.info("Añadiendo contacto al cliente ID: {}", idCliente);
        Cliente cliente = findClienteEntityById(idCliente);

        // Validar que idCliente en DTO coincida (o que no venga, si se prefiere)
        if (contactoDTO.getIdCliente() != null && !contactoDTO.getIdCliente().equals(idCliente)) {
            log.warn("Conflicto de ID de cliente al añadir contacto. Path ID: {}, DTO ID: {}", idCliente, contactoDTO.getIdCliente());
            throw new BadRequestException("El idCliente en el cuerpo del contacto no coincide con el ID en la ruta.");
        }


        ContactoCliente nuevoContacto = new ContactoCliente();
        nuevoContacto.setCliente(cliente); // Asociar al cliente encontrado
        nuevoContacto.setNombreContacto(contactoDTO.getNombreContacto());
        nuevoContacto.setCargoContacto(contactoDTO.getCargoContacto());
        nuevoContacto.setTelefonoContacto(contactoDTO.getTelefonoContacto());
        nuevoContacto.setCorreoContacto(contactoDTO.getCorreoContacto());

        ContactoCliente savedContacto = contactoClienteRepository.save(nuevoContacto);
        log.info("Contacto añadido con ID: {} para cliente ID: {}", savedContacto.getIdContacto(), idCliente);

        return mapContactoToResponseDTO(savedContacto);
    }

    @Override
    public ContactoClienteResponseDTO updateContactoCliente(Integer idCliente, Integer idContacto, ContactoClienteRequestDTO contactoDTO) {
        log.info("Actualizando contacto ID: {} para cliente ID: {}", idContacto, idCliente);
        // Verificar que el cliente existe
        findClienteEntityById(idCliente);
        // Buscar el contacto específico
        ContactoCliente contactoToUpdate = contactoClienteRepository.findById(idContacto)
                .orElseThrow(() -> new ResourceNotFoundException("ContactoCliente", "idContacto", idContacto));

        // Verificar que el contacto pertenece al cliente especificado
        if (!contactoToUpdate.getCliente().getIdCliente().equals(idCliente)) {
            log.error("Intento de actualizar contacto ID {} que no pertenece al cliente ID {}", idContacto, idCliente);
            throw new BadRequestException("El contacto especificado no pertenece al cliente.");
        }

        // Validar que idCliente en DTO coincida o sea null
        if (contactoDTO.getIdCliente() != null && !contactoDTO.getIdCliente().equals(idCliente)) {
            log.warn("Conflicto de ID de cliente al actualizar contacto. Path ID: {}, DTO ID: {}", idCliente, contactoDTO.getIdCliente());
            throw new BadRequestException("El idCliente en el cuerpo del contacto no coincide con el ID del cliente.");
        }

        // Actualizar campos del contacto
        contactoToUpdate.setNombreContacto(contactoDTO.getNombreContacto());
        contactoToUpdate.setCargoContacto(contactoDTO.getCargoContacto());
        contactoToUpdate.setTelefonoContacto(contactoDTO.getTelefonoContacto());
        contactoToUpdate.setCorreoContacto(contactoDTO.getCorreoContacto());

        ContactoCliente updatedContacto = contactoClienteRepository.save(contactoToUpdate);
        log.info("Contacto actualizado con ID: {} para cliente ID: {}", updatedContacto.getIdContacto(), idCliente);
        return mapContactoToResponseDTO(updatedContacto);
    }

    @Override
    public void deleteContactoCliente(Integer idCliente, Integer idContacto) {
        log.info("Eliminando contacto ID: {} del cliente ID: {}", idContacto, idCliente);
        // Verificar que el cliente existe
        findClienteEntityById(idCliente);
        // Buscar el contacto específico
        ContactoCliente contactoToDelete = contactoClienteRepository.findById(idContacto)
                .orElseThrow(() -> new ResourceNotFoundException("ContactoCliente", "idContacto", idContacto));

        // Verificar que el contacto pertenece al cliente especificado
        if (!contactoToDelete.getCliente().getIdCliente().equals(idCliente)) {
            log.error("Intento de eliminar contacto ID {} que no pertenece al cliente ID {}", idContacto, idCliente);
            throw new BadRequestException("El contacto especificado no pertenece al cliente.");
        }

        contactoClienteRepository.delete(contactoToDelete);
        log.info("Contacto eliminado con ID: {}", idContacto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactoClienteResponseDTO> findContactosByClienteId(Integer idCliente) {
        log.info("Buscando contactos para el cliente ID: {}", idCliente);
        // Verificar que el cliente existe primero
        findClienteEntityById(idCliente);
        List<ContactoCliente> contactos = contactoClienteRepository.findByClienteIdCliente(idCliente);
        return contactos.stream()
                .map(this::mapContactoToResponseDTO)
                .collect(Collectors.toList());
    }

    // --- Métodos de Ayuda y Mapeo Privados ---

    private Cliente findClienteEntityById(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cliente no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Cliente", "id", id);
                });
    }

    private Cliente mapToEntity(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setTipoDocumento(dto.getTipoDocumento());
        cliente.setNumeroDocumento(dto.getNumeroDocumento());
        cliente.setNombreCliente(dto.getNombreCliente());
        cliente.setDireccionCliente(dto.getDireccionCliente());
        cliente.setTelefonoCliente(dto.getTelefonoCliente());
        cliente.setCorreoCliente(dto.getCorreoCliente());
        // Las fechas se manejan con @PrePersist/@PreUpdate
        // La lista de contactos se maneja por separado
        cliente.setContactosCliente(new HashSet<>()); // Inicializar colección
        return cliente;
    }

    private ClienteResponseDTO mapToResponseDTO(Cliente entity) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setIdCliente(entity.getIdCliente());
        dto.setTipoDocumento(entity.getTipoDocumento());
        dto.setNumeroDocumento(entity.getNumeroDocumento());
        dto.setNombreCliente(entity.getNombreCliente());
        dto.setDireccionCliente(entity.getDireccionCliente());
        dto.setTelefonoCliente(entity.getTelefonoCliente());
        dto.setCorreoCliente(entity.getCorreoCliente());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());

        // Mapear contactos asociados (si la carga es EAGER o si se cargan explícitamente)
        Set<ContactoClienteResponseDTO> contactosDTO = (entity.getContactosCliente() == null) ? new HashSet<>() :
                entity.getContactosCliente().stream()
                        .map(this::mapContactoToResponseDTO)
                        .collect(Collectors.toSet());
        dto.setContactosCliente(contactosDTO);

        return dto;
    }

    private ContactoClienteResponseDTO mapContactoToResponseDTO(ContactoCliente contacto) {
        if (contacto == null) return null;
        ContactoClienteResponseDTO dto = new ContactoClienteResponseDTO();
        dto.setIdContacto(contacto.getIdContacto());
        dto.setNombreContacto(contacto.getNombreContacto());
        dto.setCargoContacto(contacto.getCargoContacto());
        dto.setTelefonoContacto(contacto.getTelefonoContacto());
        dto.setCorreoContacto(contacto.getCorreoContacto());
        // No incluimos idCliente aquí porque se asume que está dentro del contexto del cliente
        return dto;
    }

}