package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull; // Aunque idEmpleado es opcional, puede ser NotNull si la lógica de servicio lo espera así en ciertos casos.
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para la solicitud de creación de una nueva cuenta de Usuario.")
public class UsuarioCreateRequestDTO {

    @Schema(description = "Identificador único del Empleado al que se vinculará esta cuenta de usuario. " +
            "Este campo puede ser opcional dependiendo de si se permite crear usuarios no asociados a empleados.",
            example = "10",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional, la lógica de servicio decide si es mandatorio
            nullable = true)
    private Integer idEmpleado;

    @Schema(description = "Nombre de usuario único para el inicio de sesión. Debe tener entre 3 y 50 caracteres.",
            example = "carlos.rodriguez",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres.")
    private String nombreUsuario;

    @Schema(description = "Contraseña para la nueva cuenta de usuario. Debe tener al menos 8 caracteres.",
            example = "Contr4s3n4S3gur4!",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "password") // Indica que es un campo de contraseña
    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    // Considera añadir @Pattern aquí para forzar complejidad de contraseña si es necesario
    // ej. @Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", message="La contraseña debe ser segura.")
    private String contrasena;

    @Schema(description = "Rol principal que se asignará al nuevo usuario. Debe ser uno de los roles definidos en el sistema.",
            example = "Operario", // O podría ser "OPERARIO" dependiendo de la convención
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El rol del usuario no puede estar vacío.")
    // Es crucial que este Pattern coincida con los nombres de roles que usas internamente
    // y que UserDetailsServiceImpl carga como autoridades (ej. "ROLE_OPERARIO").
    // Si tus roles en BD son "OPERARIO", "ADMINISTRADOR", etc., el pattern es correcto.
    @Pattern(regexp = "Administrador|Gerente|Operario|Ventas", message = "Rol inválido. Valores permitidos: Administrador, Gerente, Operario, Ventas.")
    @Size(max = 50, message = "El rol del usuario no debe exceder los 50 caracteres.") // Añadido por consistencia
    private String rolUsuario;

    // Constructores
    public UsuarioCreateRequestDTO() {
    }

    public UsuarioCreateRequestDTO(Integer idEmpleado, String nombreUsuario, String contrasena, String rolUsuario) {
        this.idEmpleado = idEmpleado;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rolUsuario = rolUsuario;
    }

    // Getters y Setters
    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getRolUsuario() { return rolUsuario; }
    public void setRolUsuario(String rolUsuario) { this.rolUsuario = rolUsuario; }
}