
package com.minacontrol.produccion.unit;

import com.minacontrol.produccion.dto.request.RegistroProduccionCreateDTO;
import com.minacontrol.produccion.dto.response.RegistroProduccionDTO;
import com.minacontrol.produccion.entity.RegistroProduccion;
import com.minacontrol.produccion.exception.RegistroProduccionDuplicateException;
import com.minacontrol.produccion.exception.RegistroProduccionNotFoundException;
import com.minacontrol.produccion.exception.RegistroProduccionValidatedException;
import com.minacontrol.produccion.mapper.ProduccionMapper;
import com.minacontrol.produccion.repository.RegistroProduccionRepository;
import com.minacontrol.produccion.service.impl.ProduccionServiceImpl;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.turnos.repository.TipoTurnoRepository;
import com.minacontrol.empleado.exception.EmpleadoNotFoundException;
import com.minacontrol.turnos.exception.TurnoNoEncontradoException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias para ProduccionService")
public class ProduccionServiceTest {

    @Mock
    private RegistroProduccionRepository registroProduccionRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private TipoTurnoRepository tipoTurnoRepository;

    @Mock
    private ProduccionMapper produccionMapper;

    @InjectMocks
    private ProduccionServiceImpl produccionService;

    private RegistroProduccionCreateDTO createDTO;
    private RegistroProduccion registroProduccion;
    private RegistroProduccionDTO registroProduccionDTO;

    @BeforeEach
    void setUp() {
        createDTO = new RegistroProduccionCreateDTO(
                1L,
                1L,
                LocalDate.now(),
                new BigDecimal("10.5"),
                "Sector A",
                "Sin observaciones"
        );

        registroProduccion = RegistroProduccion.builder()
                .id(1L)
                .empleadoId(1L)
                .tipoTurnoId(1L)
                .fechaRegistro(LocalDate.now())
                .cantidadExtraidaToneladas(new BigDecimal("10.5"))
                .ubicacionExtraccion("Sector A")
                .observaciones("Sin observaciones")
                .validado(false)
                .build();
        
        registroProduccionDTO = new RegistroProduccionDTO(1L, 1L, "Juan Perez", 1L, "Turno Dia", LocalDate.now(), new BigDecimal("10.5"), "Sector A", "Sin observaciones", false);
    }

    @Nested
    @DisplayName("CU-PRO-001: Registrar Producción")
    class RegistrarProduccionTests {

        @Test
        @DisplayName("Debe registrar la producción exitosamente cuando los datos son válidos")
        void should_RegistrarProduccion_When_DatosValidos() {
            // Arrange
            when(empleadoRepository.existsById(1L)).thenReturn(true);
            when(tipoTurnoRepository.existsById(1L)).thenReturn(true);
            when(registroProduccionRepository.existsByEmpleadoIdAndTipoTurnoIdAndFechaRegistro(1L, 1L, LocalDate.now())).thenReturn(false);
            when(produccionMapper.toEntity(any(RegistroProduccionCreateDTO.class))).thenReturn(registroProduccion);
            when(registroProduccionRepository.save(any(RegistroProduccion.class))).thenReturn(registroProduccion);
            when(produccionMapper.toDTO(any(RegistroProduccion.class))).thenReturn(registroProduccionDTO);

            // Act
            RegistroProduccionDTO result = produccionService.registrarProduccion(createDTO);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
            verify(registroProduccionRepository).save(registroProduccion);
        }

        @Test
        @DisplayName("Debe lanzar EmpleadoNotFoundException si el empleado no existe")
        void should_LanzarExcepcion_When_EmpleadoNoExiste() {
            // Arrange
            when(empleadoRepository.existsById(1L)).thenReturn(false);

            // Act & Assert
            assertThrows(EmpleadoNotFoundException.class, () -> produccionService.registrarProduccion(createDTO));
            verify(registroProduccionRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar TurnoNoEncontradoException si el turno no existe")
        void should_LanzarExcepcion_When_TurnoNoExiste() {
            // Arrange
            when(empleadoRepository.existsById(1L)).thenReturn(true);
            when(tipoTurnoRepository.existsById(1L)).thenReturn(false);

            // Act & Assert
            assertThrows(TurnoNoEncontradoException.class, () -> produccionService.registrarProduccion(createDTO));
            verify(registroProduccionRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar RegistroProduccionDuplicateException si ya existe un registro")
        void should_LanzarExcepcion_When_RegistroDuplicado() {
            // Arrange
            when(empleadoRepository.existsById(1L)).thenReturn(true);
            when(tipoTurnoRepository.existsById(1L)).thenReturn(true);
            when(registroProduccionRepository.existsByEmpleadoIdAndTipoTurnoIdAndFechaRegistro(1L, 1L, LocalDate.now())).thenReturn(true);

            // Act & Assert
            assertThrows(RegistroProduccionDuplicateException.class, () -> produccionService.registrarProduccion(createDTO));
            verify(registroProduccionRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("CU-PRO-002: Consultar Producción")
    class ConsultarProduccionTests {

        @Test
        @DisplayName("Debe devolver todos los registros cuando no hay filtros")
        void should_DevolverTodosLosRegistros_When_SinFiltros() {
            // Arrange
            when(registroProduccionRepository.findAll()).thenReturn(List.of(registroProduccion));
            when(produccionMapper.toDTO(any(RegistroProduccion.class))).thenReturn(registroProduccionDTO);

            // Act
            List<RegistroProduccionDTO> result = produccionService.listarRegistros(null, null, null);

            // Assert
            assertThat(result).hasSize(1);
            verify(registroProduccionRepository).findAll();
        }

        @Test
        @DisplayName("Debe devolver registros filtrados por empleado")
        void should_DevolverRegistrosFiltrados_When_PorEmpleadoId() {
            // Arrange
            when(registroProduccionRepository.findByEmpleadoId(1L)).thenReturn(List.of(registroProduccion));
            when(produccionMapper.toDTO(any(RegistroProduccion.class))).thenReturn(registroProduccionDTO);

            // Act
            List<RegistroProduccionDTO> result = produccionService.listarRegistros(1L, null, null);

            // Assert
            assertThat(result).hasSize(1);
            verify(registroProduccionRepository).findByEmpleadoId(1L);
        }
    }

    @Nested
    @DisplayName("CU-PRO-003: Obtener Producción por ID")
    class ObtenerProduccionPorIdTests {

        @Test
        @DisplayName("Debe devolver un registro cuando el ID existe")
        void should_DevolverRegistro_When_IdExiste() {
            // Arrange
            when(registroProduccionRepository.findById(1L)).thenReturn(Optional.of(registroProduccion));
            when(produccionMapper.toDTO(any(RegistroProduccion.class))).thenReturn(registroProduccionDTO);

            // Act
            RegistroProduccionDTO result = produccionService.obtenerRegistroPorId(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Debe lanzar RegistroProduccionNotFoundException cuando el ID no existe")
        void should_LanzarExcepcion_When_IdNoExiste() {
            // Arrange
            when(registroProduccionRepository.findById(99L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(RegistroProduccionNotFoundException.class, () -> produccionService.obtenerRegistroPorId(99L));
        }
    }

    @Nested
    @DisplayName("CU-PRO-004: Actualizar Producción")
    class ActualizarProduccionTests {

        @Test
        @DisplayName("Debe actualizar un registro de producción existente y no validado")
        void should_ActualizarProduccion_When_RegistroExisteYNoValidado() {
            // Arrange
            when(registroProduccionRepository.findById(1L)).thenReturn(Optional.of(registroProduccion));
            when(registroProduccionRepository.save(any(RegistroProduccion.class))).thenReturn(registroProduccion);
            when(produccionMapper.toDTO(any(RegistroProduccion.class))).thenReturn(registroProduccionDTO);

            // Act
            RegistroProduccionDTO result = produccionService.actualizarRegistro(1L, createDTO);

            // Assert
            assertThat(result).isNotNull();
            verify(registroProduccionRepository).save(registroProduccion);
            verify(produccionMapper).updateFromDTO(createDTO, registroProduccion);
        }

        @Test
        @DisplayName("Debe lanzar RegistroProduccionValidatedException al intentar actualizar un registro validado")
        void should_LanzarExcepcion_When_IntentandoActualizarRegistroValidado() {
            // Arrange
            registroProduccion.setValidado(true);
            when(registroProduccionRepository.findById(1L)).thenReturn(Optional.of(registroProduccion));

            // Act & Assert
            assertThrows(RegistroProduccionValidatedException.class, () -> produccionService.actualizarRegistro(1L, createDTO));
            verify(registroProduccionRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("CU-PRO-005: Eliminar Producción")
    class EliminarProduccionTests {

        @Test
        @DisplayName("Debe eliminar un registro si no está validado")
        void should_EliminarRegistro_When_RegistroNoValidado() {
            // Arrange
            when(registroProduccionRepository.findById(1L)).thenReturn(Optional.of(registroProduccion));
            doNothing().when(registroProduccionRepository).delete(registroProduccion);

            // Act & Assert
            assertDoesNotThrow(() -> produccionService.eliminarRegistro(1L));
            verify(registroProduccionRepository).delete(registroProduccion);
        }

        @Test
        @DisplayName("Debe lanzar RegistroProduccionValidatedException al intentar eliminar un registro validado")
        void should_LanzarExcepcion_When_IntentandoEliminarRegistroValidado() {
            // Arrange
            registroProduccion.setValidado(true);
            when(registroProduccionRepository.findById(1L)).thenReturn(Optional.of(registroProduccion));

            // Act & Assert
            assertThrows(RegistroProduccionValidatedException.class, () -> produccionService.eliminarRegistro(1L));
            verify(registroProduccionRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("CU-PRO-006: Validar Producción")
    class ValidarProduccionTests {
        
        @Test
        @DisplayName("Debe validar un registro y cambiar su estado a 'validado'")
        void should_ValidarProduccion_And_ChangeStatus() {
            // Arrange
            when(registroProduccionRepository.findById(1L)).thenReturn(Optional.of(registroProduccion));
            when(registroProduccionRepository.save(any(RegistroProduccion.class))).thenAnswer(inv -> inv.getArgument(0));
            when(produccionMapper.toDTO(any(RegistroProduccion.class))).thenAnswer(inv -> {
                RegistroProduccion rp = inv.getArgument(0);
                return new RegistroProduccionDTO(rp.getId(), rp.getEmpleadoId(), "Juan Perez", rp.getTipoTurnoId(), "Turno Dia", rp.getFechaRegistro(), rp.getCantidadExtraidaToneladas(), rp.getUbicacionExtraccion(), rp.getObservaciones(), rp.isValidado());
            });

            // Act
            RegistroProduccionDTO result = produccionService.validarRegistro(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.validado()).isTrue();
            verify(registroProduccionRepository).save(registroProduccion);
        }

        @Test
        @DisplayName("Debe devolver el registro sin cambios si ya está validado (idempotencia)")
        void should_NoHacerNada_When_RegistroYaEstaValidado() {
            // Arrange
            registroProduccion.setValidado(true);
            when(registroProduccionRepository.findById(1L)).thenReturn(Optional.of(registroProduccion));
            when(produccionMapper.toDTO(registroProduccion)).thenReturn(new RegistroProduccionDTO(1L, 1L, "Juan Perez", 1L, "Turno Dia", LocalDate.now(), new BigDecimal("10.5"), "Sector A", "Sin observaciones", true));


            // Act
            RegistroProduccionDTO result = produccionService.validarRegistro(1L);

            // Assert
            assertThat(result.validado()).isTrue();
            verify(registroProduccionRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar RegistroProduccionNotFoundException si el registro a validar no existe")
        void should_ThrowException_When_RegistroToValidateNotFound() {
            // Arrange
            when(registroProduccionRepository.findById(99L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(RegistroProduccionNotFoundException.class, () -> produccionService.validarRegistro(99L));
        }
    }
}
