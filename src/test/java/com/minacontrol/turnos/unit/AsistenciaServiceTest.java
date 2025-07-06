
package com.minacontrol.turnos.unit;

import com.minacontrol.turnos.dto.request.RegistrarAsistenciaDTO;
import com.minacontrol.turnos.dto.response.RegistroAsistenciaDTO;
import com.minacontrol.turnos.entity.AsignacionTurno;
import com.minacontrol.turnos.entity.RegistroAsistencia;
import com.minacontrol.turnos.enums.TipoRegistro;
import com.minacontrol.turnos.exception.AsignacionTurnoInvalidaException;
import com.minacontrol.turnos.exception.RegistroAsistenciaInvalidoException;
import com.minacontrol.turnos.mapper.RegistroAsistenciaMapper;
import com.minacontrol.turnos.repository.AsignacionTurnoRepository;
import com.minacontrol.turnos.repository.RegistroAsistenciaRepository;
import com.minacontrol.turnos.service.impl.AsistenciaServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias para AsistenciaService")
class AsistenciaServiceTest {

    @Mock
    private RegistroAsistenciaRepository registroAsistenciaRepository;

    @Mock
    private AsignacionTurnoRepository asignacionTurnoRepository;

    @Mock
    private RegistroAsistenciaMapper registroAsistenciaMapper;

    @InjectMocks
    private AsistenciaServiceImpl asistenciaService;

    @Nested
    @DisplayName("CU-TUR-007: Registrar Entrada/Salida de Asistencia")
    class RegistrarAsistenciaTests {

        @Test
        void should_registrarEntrada_when_datosSonValidos() {
            // Arrange
            var dto = new RegistrarAsistenciaDTO(1L, TipoRegistro.ENTRADA);
            var asignacion = new AsignacionTurno();
            var registro = new RegistroAsistencia();
            var registroDTO = new RegistroAsistenciaDTO(1L, 1L, LocalDate.now(), LocalTime.now(), null, 0, "ENTRADA");

            when(asignacionTurnoRepository.findConflictosDeHorario(anyLong(), any(), any())).thenReturn(java.util.List.of(asignacion));
            when(registroAsistenciaRepository.findByEmpleadoIdAndFecha(anyLong(), any())).thenReturn(Optional.empty());
            when(registroAsistenciaRepository.save(any(RegistroAsistencia.class))).thenReturn(registro);
            when(registroAsistenciaMapper.toDTO(any(RegistroAsistencia.class))).thenReturn(registroDTO);

            // Act
            RegistroAsistenciaDTO result = asistenciaService.registrarAsistencia(dto);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.estado()).isEqualTo("ENTRADA");
        }

        @Test
        void should_throwAsignacionTurnoInvalidaException_when_empleadoNoTieneTurnoHoy() {
            // Arrange
            var dto = new RegistrarAsistenciaDTO(1L, TipoRegistro.ENTRADA);
            when(asignacionTurnoRepository.findConflictosDeHorario(anyLong(), any(), any())).thenReturn(java.util.Collections.emptyList());

            // Act & Assert
            assertThrows(AsignacionTurnoInvalidaException.class, () -> {
                asistenciaService.registrarAsistencia(dto);
            });
        }

        @Test
        void should_throwRegistroAsistenciaInvalidoException_when_yaExisteEntrada() {
            // Arrange
            var dto = new RegistrarAsistenciaDTO(1L, TipoRegistro.ENTRADA);
            var asignacion = new AsignacionTurno();
            var registroExistente = new RegistroAsistencia();

            when(asignacionTurnoRepository.findConflictosDeHorario(anyLong(), any(), any())).thenReturn(java.util.List.of(asignacion));
            when(registroAsistenciaRepository.findByEmpleadoIdAndFecha(anyLong(), any())).thenReturn(Optional.of(registroExistente));

            // Act & Assert
            assertThrows(RegistroAsistenciaInvalidoException.class, () -> {
                asistenciaService.registrarAsistencia(dto);
            });
        }
    }
}
