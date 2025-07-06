package com.minacontrol.empleado.unit;

import com.minacontrol.empleado.dto.request.EmpleadoRequest;
import com.minacontrol.empleado.dto.response.EmpleadoResponse;
import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.enums.EstadoEmpleado;
import com.minacontrol.empleado.enums.RolSistema;
import com.minacontrol.empleado.exception.EmpleadoAlreadyExistsException;
import com.minacontrol.empleado.exception.EmpleadoNotFoundException;
import com.minacontrol.empleado.mapper.EmpleadoMapper;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.empleado.service.impl.EmpleadoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private EmpleadoMapper empleadoMapper;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    private Empleado empleado;
    private EmpleadoRequest empleadoRequest;
    private EmpleadoResponse empleadoResponse;

    @BeforeEach
    void setUp() {
        empleado = Empleado.builder()
                .id(1L)
                .nombres("Juan")
                .apellidos("Perez")
                .numeroIdentificacion("123456789")
                .email("juan.perez@example.com")
                .telefono("1234567890")
                .cargo("Operador")
                .fechaContratacion(LocalDate.of(2023, 1, 1))
                .salarioBase(new BigDecimal("1000.00"))
                .estado(EstadoEmpleado.ACTIVO)
                .rolSistema(RolSistema.EMPLEADO)
                .tieneUsuario(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        empleadoRequest = new EmpleadoRequest(
                "Juan", "Perez", "123456789", "juan.perez@example.com",
                "1234567890", "Operador", LocalDate.of(2023, 1, 1),
                new BigDecimal("1000.00"), RolSistema.EMPLEADO
        );

        empleadoResponse = new EmpleadoResponse(
                1L, "Juan", "Perez", "123456789", "juan.perez@example.com",
                "1234567890", "Operador", LocalDate.of(2023, 1, 1),
                new BigDecimal("1000.00"), EstadoEmpleado.ACTIVO, RolSistema.EMPLEADO,
                false, LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Nested
    @DisplayName("CU-EMP-001: Crear Empleado")
    class CrearEmpleadoTests {

        @Test
        @DisplayName("Debe crear un empleado exitosamente con datos válidos")
        void should_CreateEmployee_When_ValidData() {
            // Arrange
            when(empleadoRepository.existsByNumeroIdentificacion(anyString())).thenReturn(false);
            when(empleadoRepository.existsByEmail(anyString())).thenReturn(false);
            when(empleadoMapper.toEntity(any(EmpleadoRequest.class))).thenReturn(empleado);
            when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);
            when(empleadoMapper.toResponse(any(Empleado.class))).thenReturn(empleadoResponse);

            // Act
            EmpleadoResponse result = empleadoService.crearEmpleado(empleadoRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.nombres()).isEqualTo(empleado.getNombres());
            assertThat(result.numeroIdentificacion()).isEqualTo(empleado.getNumeroIdentificacion());

            verify(empleadoRepository).existsByNumeroIdentificacion(empleadoRequest.numeroIdentificacion());
            verify(empleadoRepository).existsByEmail(empleadoRequest.email());
            verify(empleadoRepository).save(any(Empleado.class));
            verify(empleadoMapper).toEntity(any(EmpleadoRequest.class));
            verify(empleadoMapper).toResponse(any(Empleado.class));
        }

        @Test
        @DisplayName("Debe lanzar EmpleadoAlreadyExistsException si el número de identificación ya existe")
        void should_ThrowException_When_NumeroIdentificacionAlreadyExists() {
            // Arrange
            when(empleadoRepository.existsByNumeroIdentificacion(anyString())).thenReturn(true);

            // Act & Assert
            assertThrows(EmpleadoAlreadyExistsException.class, () -> empleadoService.crearEmpleado(empleadoRequest));

            verify(empleadoRepository).existsByNumeroIdentificacion(empleadoRequest.numeroIdentificacion());
            verify(empleadoRepository, never()).existsByEmail(anyString());
            verify(empleadoRepository, never()).save(any(Empleado.class));
        }

        @Test
        @DisplayName("Debe lanzar EmpleadoAlreadyExistsException si el email ya existe")
        void should_ThrowException_When_EmailAlreadyExists() {
            // Arrange
            when(empleadoRepository.existsByNumeroIdentificacion(anyString())).thenReturn(false);
            when(empleadoRepository.existsByEmail(anyString())).thenReturn(true);

            // Act & Assert
            assertThrows(EmpleadoAlreadyExistsException.class, () -> empleadoService.crearEmpleado(empleadoRequest));

            verify(empleadoRepository).existsByNumeroIdentificacion(empleadoRequest.numeroIdentificacion());
            verify(empleadoRepository).existsByEmail(empleadoRequest.email());
            verify(empleadoRepository, never()).save(any(Empleado.class));
        }
    }

    @Nested
    @DisplayName("CU-EMP-002: Listar Empleados")
    class ListarEmpleadosTests {

        @Test
        @DisplayName("Debe devolver todos los empleados cuando no se aplica ningún filtro")
        void should_ReturnAllEmployees_When_NoFilterApplied() {
            // Arrange
            when(empleadoRepository.findAll()).thenReturn(List.of(empleado));
            when(empleadoMapper.toResponse(any(Empleado.class))).thenReturn(empleadoResponse);

            // Act
            List<EmpleadoResponse> result = empleadoService.listarEmpleados(null, null);

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.get(0).id()).isEqualTo(empleado.getId());
            verify(empleadoRepository).findAll();
            verify(empleadoMapper).toResponse(empleado);
        }

        @Test
        @DisplayName("Debe devolver una lista vacía cuando no existen empleados")
        void should_ReturnEmptyList_When_NoEmployeesExist() {
            // Arrange
            when(empleadoRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<EmpleadoResponse> result = empleadoService.listarEmpleados(null, null);

            // Assert
            assertThat(result).isEmpty();
            verify(empleadoRepository).findAll();
            verify(empleadoMapper, never()).toResponse(any(Empleado.class));
        }
    }

    @Nested
    @DisplayName("CU-EMP-003: Actualizar Empleado")
    class ActualizarEmpleadoTests {

        @Test
        @DisplayName("Debe actualizar el empleado exitosamente con datos válidos")
        void should_UpdateEmployee_When_ValidData() {
            // Arrange
            when(empleadoRepository.findById(anyLong())).thenReturn(Optional.of(empleado));
            when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);
            when(empleadoMapper.toResponse(any(Empleado.class))).thenReturn(empleadoResponse);
            doNothing().when(empleadoMapper).updateEntityFromRequest(any(EmpleadoRequest.class), any(Empleado.class));


            // Act
            EmpleadoResponse result = empleadoService.actualizarEmpleado(1L, empleadoRequest);

            // Assert
            assertThat(result).isNotNull();
            verify(empleadoRepository).findById(1L);
            verify(empleadoMapper).updateEntityFromRequest(empleadoRequest, empleado);
            verify(empleadoRepository).save(empleado);
            verify(empleadoMapper).toResponse(empleado);
        }

        @Test
        @DisplayName("Debe lanzar EmpleadoNotFoundException cuando el empleado a actualizar no existe")
        void should_ThrowException_When_EmployeeToUpdateDoesNotExist() {
            // Arrange
            when(empleadoRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EmpleadoNotFoundException.class, () -> empleadoService.actualizarEmpleado(1L, empleadoRequest));
            verify(empleadoRepository).findById(1L);
            verify(empleadoRepository, never()).save(any(Empleado.class));
        }
    }

    @Nested
    @DisplayName("CU-EMP-004: Cambiar Estado de Empleado")
    class CambiarEstadoEmpleadoTests {

        @Test
        @DisplayName("Debe cambiar el estado del empleado")
        void should_ChangeEmployeeStatus() {
            // Arrange
            when(empleadoRepository.findById(anyLong())).thenReturn(Optional.of(empleado));
            when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);
            when(empleadoMapper.toResponse(any(Empleado.class))).thenReturn(empleadoResponse);

            // Act
            empleadoService.cambiarEstadoEmpleado(1L, EstadoEmpleado.INACTIVO);

            // Assert
            verify(empleadoRepository).findById(1L);
            verify(empleadoRepository).save(empleado);
            assertThat(empleado.getEstado()).isEqualTo(EstadoEmpleado.INACTIVO);
        }
    }

    @Nested
    @DisplayName("CU-EMP-005: Consultar Perfil Personal")
    class ConsultarPerfilPersonalTests {

        @Test
        @DisplayName("Debe devolver el perfil personal del empleado")
        void should_ReturnPersonalProfile() {
            // Arrange
            when(empleadoRepository.findByEmail(anyString())).thenReturn(Optional.of(empleado));
            when(empleadoMapper.toResponse(any(Empleado.class))).thenReturn(empleadoResponse);

            // Act
            EmpleadoResponse result = empleadoService.obtenerPerfilPersonal("juan.perez@example.com");

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.email()).isEqualTo("juan.perez@example.com");
            verify(empleadoRepository).findByEmail("juan.perez@example.com");
        }
    }

    @Nested
    @DisplayName("CU-EMP-006: Eliminar (Desactivar) Empleado")
    class EliminarEmpleadoTests {

        @Test
        @DisplayName("Debe desactivar un empleado")
        void should_DeactivateEmployee() {
            // Arrange
            when(empleadoRepository.findById(anyLong())).thenReturn(Optional.of(empleado));

            // Act
            empleadoService.eliminarEmpleado(1L);

            // Assert
            verify(empleadoRepository).findById(1L);
            verify(empleadoRepository).save(empleado);
            assertThat(empleado.getEstado()).isEqualTo(EstadoEmpleado.INACTIVO);
        }
    }

    @Nested
    @DisplayName("CU-EMP-007: Obtener Empleado por ID")
    class ObtenerEmpleadoPorIdTests {

        @Test
        @DisplayName("Debe devolver un empleado por ID si existe")
        void should_ReturnEmployeeById_When_EmployeeExists() {
            // Arrange
            when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));
            when(empleadoMapper.toResponse(any(Empleado.class))).thenReturn(empleadoResponse);

            // Act
            EmpleadoResponse result = empleadoService.obtenerEmpleadoPorId(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
            verify(empleadoRepository).findById(1L);
            verify(empleadoMapper).toResponse(empleado);
        }

        @Test
        @DisplayName("Debe lanzar EmpleadoNotFoundException si el empleado por ID no existe")
        void should_ThrowException_When_EmployeeByIdDoesNotExist() {
            // Arrange
            when(empleadoRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EmpleadoNotFoundException.class, () -> empleadoService.obtenerEmpleadoPorId(1L));
            verify(empleadoRepository).findById(1L);
            verify(empleadoMapper, never()).toResponse(any(Empleado.class));
        }
    }
}
