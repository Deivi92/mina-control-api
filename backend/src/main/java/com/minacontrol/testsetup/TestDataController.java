package com.minacontrol.testsetup;

import com.minacontrol.autenticacion.dto.request.RegistroUsuarioCreateDTO;
import com.minacontrol.autenticacion.service.IServicioAutenticacion;
import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import java.util.Optional;
import com.minacontrol.autenticacion.exception.UsuarioYaExisteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Este controlador solo existe para facilitar las pruebas E2E.
 * Proporciona endpoints para sembrar la base de datos con datos de prueba.
 * ¡IMPORTANTE! Está anotado con @Profile("!prod") para que NUNCA exista en un entorno de producción.
 */
@RestController
@RequestMapping("/api/test-data")
@Profile("!prod")
public class TestDataController {

    private final IServicioAutenticacion servicioAutenticacion;
    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public TestDataController(IServicioAutenticacion servicioAutenticacion, EmpleadoRepository empleadoRepository) {
        this.servicioAutenticacion = servicioAutenticacion;
        this.empleadoRepository = empleadoRepository;
    }

    /**
     * Endpoint que el `globalSetup` de Playwright llamará para sembrar la base de datos.
     * Crea un empleado y usuario administrador por defecto.
     */
    @PostMapping("/setup")
    public ResponseEntity<String> setupTestData() {
        try {
            // 1. Asegurar que el Empleado admin existe y no tiene usuario asociado
            Optional<Empleado> existingEmpleado = empleadoRepository.findByEmail("admin@minacontrol.com");
            Empleado adminEmpleado;

            if (existingEmpleado.isEmpty()) {
                // Si no existe, crear el empleado admin
                adminEmpleado = Empleado.builder()
                        .nombres("Admin")
                        .apellidos("User")
                        .email("admin@minacontrol.com")
                        .numeroIdentificacion("00000000")
                        .telefono("000-0000")
                        .cargo("Administrador")
                        .fechaContratacion(java.time.LocalDate.now())
                        .salarioBase(java.math.BigDecimal.ZERO)
                        .estado(com.minacontrol.empleado.enums.EstadoEmpleado.ACTIVO)
                        .rolSistema(com.minacontrol.empleado.enums.RolSistema.ADMINISTRADOR)
                        .tieneUsuario(false) // Importante: inicialmente no tiene usuario
                        .build();
                empleadoRepository.save(adminEmpleado);
            } else {
                adminEmpleado = existingEmpleado.get();
                if (adminEmpleado.isTieneUsuario()) {
                    // Si ya tiene usuario, el setup ya está hecho para este empleado
                    return ResponseEntity.ok("El empleado admin ya tiene una cuenta de usuario. Setup no necesario.");
                }
            }

            // 2. Intentar registrar el usuario admin
            RegistroUsuarioCreateDTO adminUser = new RegistroUsuarioCreateDTO(
                "admin@minacontrol.com",
                "admin"
            );
            servicioAutenticacion.registrarUsuario(adminUser); // Esto actualizará tieneUsuario a true

            return ResponseEntity.ok("Datos de prueba (empleado y usuario admin) creados exitosamente.");
        } catch (UsuarioYaExisteException e) {
            // Esto significa que el usuario ya fue registrado (posiblemente en una ejecución anterior)
            return ResponseEntity.ok("El usuario admin ya existe. Setup completado.");
        } catch (Exception e) {
            // Capturar cualquier otra excepción inesperada
            return ResponseEntity.internalServerError().body("Error crítico durante el setup de datos de prueba: " + e.getMessage());
        }
    }
}
