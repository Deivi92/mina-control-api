
package com.minacontrol.turnos.unit;

import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.turnos.dto.request.AsignacionTurnoCreateDTO;
import com.minacontrol.turnos.dto.request.TipoTurnoCreateDTO;
import com.minacontrol.turnos.dto.response.AsignacionTurnoDTO;
import com.minacontrol.turnos.dto.response.TipoTurnoDTO;
import com.minacontrol.turnos.entity.AsignacionTurno;
import com.minacontrol.turnos.entity.TipoTurno;
import com.minacontrol.turnos.exception.AsignacionTurnoInvalidaException;
import com.minacontrol.turnos.exception.TurnoAlreadyExistsException;
import com.minacontrol.turnos.exception.TurnoInvalidoException;
import com.minacontrol.turnos.exception.TurnoNoEncontradoException;
import com.minacontrol.turnos.mapper.AsignacionTurnoMapper;
import com.minacontrol.turnos.mapper.TipoTurnoMapper;
import com.minacontrol.turnos.repository.AsignacionTurnoRepository;
import com.minacontrol.turnos.repository.TipoTurnoRepository;
import com.minacontrol.turnos.service.impl.TurnoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias para TurnoService")
class TipoTurnoServiceTest {

    @Mock
    private TipoTurnoRepository tipoTurnoRepository;

    @Mock
    private AsignacionTurnoRepository asignacionTurnoRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private TipoTurnoMapper tipoTurnoMapper;

    @Mock
    private AsignacionTurnoMapper asignacionTurnoMapper;

    @InjectMocks
    private TurnoServiceImpl turnoService;

    @Nested
    @DisplayName("CU-TUR-001: Crear Tipo de Turno")
    class CrearTipoTurnoTests {

        @Test
        void should_crearTipoTurno_when_datosSonValidos() {
            // Arrange
            var createDTO = new TipoTurnoCreateDTO("Turno de Mañana", LocalTime.of(8, 0), LocalTime.of(16, 0), "Turno diurno estándar");
            var turno = TipoTurno.builder().id(1L).nombre("Turno de Mañana").build();
            var turnoDTO = new TipoTurnoDTO(1L, "Turno de Mañana", LocalTime.of(8, 0), LocalTime.of(16, 0), "Turno diurno estándar", true);

            when(tipoTurnoRepository.findByNombre(anyString())).thenReturn(Optional.empty());
            when(tipoTurnoMapper.toEntity(any(TipoTurnoCreateDTO.class))).thenReturn(turno);
            when(tipoTurnoRepository.save(any(TipoTurno.class))).thenReturn(turno);
            when(tipoTurnoMapper.toDTO(any(TipoTurno.class))).thenReturn(turnoDTO);

            // Act
            TipoTurnoDTO result = turnoService.crearTipoTurno(createDTO);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.nombre()).isEqualTo(createDTO.nombre());
            verify(tipoTurnoRepository).findByNombre(createDTO.nombre());
            verify(tipoTurnoRepository).save(any(TipoTurno.class));
        }

        @Test
        void should_throwTurnoAlreadyExistsException_when_nombreYaExiste() {
            // Arrange
            var createDTO = new TipoTurnoCreateDTO("Turno Existente", LocalTime.of(8, 0), LocalTime.of(16, 0), null);
            when(tipoTurnoRepository.findByNombre("Turno Existente")).thenReturn(Optional.of(new TipoTurno()));

            // Act & Assert
            assertThrows(TurnoAlreadyExistsException.class, () -> {
                turnoService.crearTipoTurno(createDTO);
            });
            verify(tipoTurnoRepository, never()).save(any());
        }

        @Test
        void should_throwTurnoInvalidoException_when_horaInicioEsPosteriorAHoraFin() {
            // Arrange
            var createDTO = new TipoTurnoCreateDTO("Turno Inválido", LocalTime.of(16, 0), LocalTime.of(8, 0), null);

            // Act & Assert
            assertThrows(TurnoInvalidoException.class, () -> {
                turnoService.crearTipoTurno(createDTO);
            });
            verify(tipoTurnoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("CU-TUR-002: Listar Tipos de Turno")
    class ListarTiposTurnoTests {

        @Test
        void should_retornarListaDeTurnos() {
            // Arrange
            var turno = TipoTurno.builder().id(1L).nombre("Turno de Mañana").build();
            var turnoDTO = new TipoTurnoDTO(1L, "Turno de Mañana", LocalTime.of(8, 0), LocalTime.of(16, 0), "desc", true);
            when(tipoTurnoRepository.findAll()).thenReturn(List.of(turno));
            when(tipoTurnoMapper.toDTO(any(TipoTurno.class))).thenReturn(turnoDTO);

            // Act
            List<TipoTurnoDTO> result = turnoService.listarTodosLosTiposDeTurno();

            // Assert
            assertThat(result).isNotEmpty();
            assertThat(result.get(0).nombre()).isEqualTo("Turno de Mañana");
            verify(tipoTurnoRepository).findAll();
        }

        @Test
        void should_retornarListaVacia_when_noExistenTurnos() {
            // Arrange
            when(tipoTurnoRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<TipoTurnoDTO> result = turnoService.listarTodosLosTiposDeTurno();

            // Assert
            assertThat(result).isEmpty();
            verify(tipoTurnoRepository).findAll();
        }
    }

    @Nested
    @DisplayName("CU-TUR-003: Obtener Tipo de Turno por ID")
    class ObtenerTipoTurnoPorIdTests {

        @Test
        void should_retornarTurno_when_idExiste() {
            // Arrange
            var turno = TipoTurno.builder().id(1L).nombre("Turno de Mañana").build();
            var turnoDTO = new TipoTurnoDTO(1L, "Turno de Mañana", LocalTime.of(8, 0), LocalTime.of(16, 0), "desc", true);
            when(tipoTurnoRepository.findById(1L)).thenReturn(Optional.of(turno));
            when(tipoTurnoMapper.toDTO(turno)).thenReturn(turnoDTO);

            // Act
            TipoTurnoDTO result = turnoService.obtenerTipoTurnoPorId(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
            verify(tipoTurnoRepository).findById(1L);
        }

        @Test
        void should_throwTurnoNoEncontradoException_when_idNoExiste() {
            // Arrange
            when(tipoTurnoRepository.findById(99L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(TurnoNoEncontradoException.class, () -> {
                turnoService.obtenerTipoTurnoPorId(99L);
            });
        }
    }

    @Nested
    @DisplayName("CU-TUR-004: Actualizar Tipo de Turno")
    class ActualizarTipoTurnoTests {

        @Test
        void should_actualizarTurno_when_datosSonValidos() {
            // Arrange
            var updateDTO = new TipoTurnoCreateDTO("Turno de Tarde Actualizado", LocalTime.of(14, 0), LocalTime.of(22, 0), "Nuevo desc");
            var turnoExistente = TipoTurno.builder().id(1L).nombre("Turno de Tarde").activo(true).build();
            var turnoActualizadoDTO = new TipoTurnoDTO(1L, "Turno de Tarde Actualizado", LocalTime.of(14, 0), LocalTime.of(22, 0), "Nuevo desc", true);

            when(tipoTurnoRepository.findById(1L)).thenReturn(Optional.of(turnoExistente));
            when(tipoTurnoRepository.findByNombre(anyString())).thenReturn(Optional.empty());
            when(tipoTurnoRepository.save(any(TipoTurno.class))).thenReturn(turnoExistente);
            when(tipoTurnoMapper.toDTO(any(TipoTurno.class))).thenReturn(turnoActualizadoDTO);

            // Act
            TipoTurnoDTO result = turnoService.actualizarTipoTurno(1L, updateDTO);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.nombre()).isEqualTo("Turno de Tarde Actualizado");
            verify(tipoTurnoRepository).findById(1L);
            verify(tipoTurnoRepository).save(turnoExistente);
        }

        @Test
        void should_throwTurnoNoEncontradoException_when_idNoExiste() {
            // Arrange
            var updateDTO = new TipoTurnoCreateDTO("Test", LocalTime.of(8, 0), LocalTime.of(16, 0), null);
            when(tipoTurnoRepository.findById(99L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(TurnoNoEncontradoException.class, () -> {
                turnoService.actualizarTipoTurno(99L, updateDTO);
            });
            verify(tipoTurnoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("CU-TUR-005: Eliminar Tipo de Turno")
    class EliminarTipoTurnoTests {

        @Test
        void should_eliminarTurno_when_noTieneAsignaciones() {
            // Arrange
            var turno = TipoTurno.builder().id(1L).nombre("Turno a Eliminar").build();
            when(tipoTurnoRepository.findById(1L)).thenReturn(Optional.of(turno));
            when(asignacionTurnoRepository.existsByTipoTurnoId(1L)).thenReturn(false);

            // Act
            turnoService.eliminarTipoTurno(1L);

            // Assert
            verify(tipoTurnoRepository).delete(turno);
        }

        @Test
        void should_throwIllegalStateException_when_turnoTieneAsignaciones() {
            // Arrange
            var turno = TipoTurno.builder().id(1L).nombre("Turno Asignado").build();
            when(tipoTurnoRepository.findById(1L)).thenReturn(Optional.of(turno));
            when(asignacionTurnoRepository.existsByTipoTurnoId(1L)).thenReturn(true);

            // Act & Assert
            assertThrows(IllegalStateException.class, () -> {
                turnoService.eliminarTipoTurno(1L);
            });
            verify(tipoTurnoRepository, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("CU-TUR-006: Asignar Empleado a Turno")
    class AsignarEmpleadoTests {

        @Test
        void should_asignarEmpleado_when_datosSonValidos() {
            // Arrange
            var createDTO = new AsignacionTurnoCreateDTO(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(30));
            var empleado = Empleado.builder().id(1L).estado(com.minacontrol.empleado.enums.EstadoEmpleado.ACTIVO).build();
            var tipoTurno = TipoTurno.builder().id(1L).activo(true).build();
            var asignacion = new AsignacionTurno();
            var asignacionDTO = new AsignacionTurnoDTO(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(30));

            when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));
            when(tipoTurnoRepository.findById(1L)).thenReturn(Optional.of(tipoTurno));
            when(asignacionTurnoRepository.findConflictosDeHorario(anyLong(), any(), any())).thenReturn(Collections.emptyList());
            when(asignacionTurnoMapper.toEntity(any(AsignacionTurnoCreateDTO.class))).thenReturn(asignacion);
            when(asignacionTurnoRepository.save(any(AsignacionTurno.class))).thenReturn(asignacion);
            when(asignacionTurnoMapper.toDTO(any(AsignacionTurno.class))).thenReturn(asignacionDTO);

            // Act
            AsignacionTurnoDTO result = turnoService.asignarEmpleadoATurno(createDTO);

            // Assert
            assertThat(result).isNotNull();
            verify(asignacionTurnoRepository).save(asignacion);
        }

        @Test
        void should_throwAsignacionTurnoInvalidaException_when_hayConflictoDeHorario() {
            // Arrange
            var createDTO = new AsignacionTurnoCreateDTO(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(30));
            var empleado = Empleado.builder().id(1L).estado(com.minacontrol.empleado.enums.EstadoEmpleado.ACTIVO).build();
            var tipoTurno = TipoTurno.builder().id(1L).activo(true).build();

            when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));
            when(tipoTurnoRepository.findById(1L)).thenReturn(Optional.of(tipoTurno));
            when(asignacionTurnoRepository.findConflictosDeHorario(anyLong(), any(), any())).thenReturn(List.of(new AsignacionTurno()));

            // Act & Assert
            assertThrows(AsignacionTurnoInvalidaException.class, () -> {
                turnoService.asignarEmpleadoATurno(createDTO);
            });
        }
    }
}
