package com.minacontrol.testsetup;

import com.minacontrol.autenticacion.dto.request.RegistroUsuarioCreateDTO;
import com.minacontrol.autenticacion.service.IServicioAutenticacion;
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

    @Autowired
    public TestDataController(IServicioAutenticacion servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;
    }

    /**
     * Endpoint que el `globalSetup` de Playwright llamará para sembrar la base de datos.
     * Crea un usuario administrador por defecto.
     */
    @PostMapping("/setup")
    public ResponseEntity<String> setupTestData() {
        try {
            // Reutilizamos el DTO y el servicio de registro existentes para crear el usuario.
            // Esto asegura que se aplique toda la lógica de negocio (ej. hasheo de contraseña).
            RegistroUsuarioCreateDTO adminUser = new RegistroUsuarioCreateDTO(
                "admin@minacontrol.com",
                "admin"
            );
            servicioAutenticacion.registrarUsuario(adminUser);
            return ResponseEntity.ok("Datos de prueba (usuario admin) creados exitosamente.");
        } catch (Exception e) {
            // Si el usuario ya existe, el servicio lanzará una excepción.
            // En el contexto de las pruebas, esto no es un error, solo significa que el setup ya se hizo.
            // Por lo tanto, devolvemos un 200 OK para que el test continúe.
            return ResponseEntity.ok("Los datos de prueba ya existen o ocurrió un error no crítico: " + e.getMessage());
        }
    }
}
