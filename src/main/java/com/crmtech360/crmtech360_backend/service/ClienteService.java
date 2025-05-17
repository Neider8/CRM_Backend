package com.crmtech360.crmtech360_backend.service;

import com.crmtech360.crmtech360_backend.dto.ClienteRequestDTO;
import com.crmtech360.crmtech360_backend.dto.ClienteResponseDTO;
import com.crmtech360.crmtech360_backend.dto.ContactoClienteRequestDTO; // Para manejar contactos
import com.crmtech360.crmtech360_backend.dto.ContactoClienteResponseDTO; // Para manejar contactos
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ClienteService {

    ClienteResponseDTO createCliente(ClienteRequestDTO clienteRequestDTO);

    Page<ClienteResponseDTO> findAllClientes(Pageable pageable);

    ClienteResponseDTO findClienteById(Integer id);

    ClienteResponseDTO findClienteByNumeroDocumento(String numeroDocumento);

    ClienteResponseDTO updateCliente(Integer id, ClienteRequestDTO clienteRequestDTO);

    void deleteCliente(Integer id);

    // --- Métodos para Contactos del Cliente ---

    ContactoClienteResponseDTO addContactoToCliente(Integer idCliente, ContactoClienteRequestDTO contactoDTO);

    ContactoClienteResponseDTO updateContactoCliente(Integer idCliente, Integer idContacto, ContactoClienteRequestDTO contactoDTO);

    void deleteContactoCliente(Integer idCliente, Integer idContacto);

    List<ContactoClienteResponseDTO> findContactosByClienteId(Integer idCliente);

}