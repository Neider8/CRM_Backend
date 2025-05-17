package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size; // Importar Size

// DTO para actualizar datos específicos del usuario, como el rol o el empleado asociado.
// La actualización de contraseña debería tener su propio DTO y endpoint.
// El nombre de usuario generalmente no se cambia.
@Schema(description = "DTO para la solicitud de actualización de datos específicos de una cuenta de Usuario existente. " +
        "Permite cambiar el empleado asociado y/o el rol del usuario. " +
        "El nombre de usuario y la contraseña se gestionan por separado.")
public class UsuarioUpdateRequestDTO {

    @Schema(description = "Nuevo identificador único del Empleado al que se vinculará esta cuenta de usuario. " +
            "Si se proporciona, reemplazará la asociación existente. " +
            "Enviar nulo podría desvincular al usuario de un empleado, si la lógica de negocio lo permite.",
            example = "11",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional, solo se actualiza si se envía
            nullable = true)
    private Integer idEmpleado;

    @Schema(description = "Nuevo rol principal que se asignará al usuario. Debe ser uno de los roles definidos en el sistema.",
            example = "Gerente", // O "GERENTE" dependiendo de la convención de tus roles
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional, solo se actualiza si se envía
            nullable = true)
    @Pattern(regexp = "Administrador|Gerente|Operario|Ventas", message = "Rol inválido. Valores permitidos: Administrador, Gerente, Operario, Ventas.")
    @Size(max = 50, message = "El rol del usuario no debe exceder los 50 caracteres.") // Añadido por consistencia
    private String rolUsuario;

    // Constructores
    public UsuarioUpdateRequestDTO() {
    }

    public UsuarioUpdateRequestDTO(Integer idEmpleado, String rolUsuario) {
        this.idEmpleado = idEmpleado;
        this.rolUsuario = rolUsuario;
    }

    // Getters y Setters
    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }
    public String getRolUsuario() { return rolUsuario; }
    public void setRolUsuario(String rolUsuario) { this.rolUsuario = rolUsuario; }
}