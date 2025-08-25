import com.minacontrol.empleado.entity.Empleado;
import com.minacontrol.empleado.enums.EstadoEmpleado;
import com.minacontrol.empleado.enums.RolSistema;
import com.minacontrol.empleado.exception.EmpleadoNotFoundException;
import com.minacontrol.turnos.dto.request.AsignacionTurnoCreateDTO;
import com.minacontrol.turnos.dto.response.AsignacionTurnoDTO;
import com.minacontrol.turnos.entity.AsignacionTurno;
import com.minacontrol.turnos.entity.TipoTurno;
import com.minacontrol.turnos.exception.AsignacionTurnoInvalidaException;
import com.minacontrol.turnos.exception.TurnoNoEncontradoException;
import com.minacontrol.turnos.mapper.AsignacionTurnoMapper;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import com.minacontrol.turnos.repository.AsignacionTurnoRepository;
import com.minacontrol.turnos.repository.TipoTurnoRepository;
import com.minacontrol.turnos.service.impl.TurnoServiceImpl;
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
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AsignacionTurnoServiceTest {

    @Mock private AsignacionTurnoRepository asignacionTurnoRepository;
    @Mock private EmpleadoRepository empleadoRepository;
    @Mock private TipoTurnoRepository tipoTurnoRepository;
    @Mock private AsignacionTurnoMapper asignacionTurnoMapper;

    @InjectMocks private TurnoServiceImpl turnoService;

    private Empleado empleado;
    private TipoTurno tipoTurno;
    private AsignacionTurnoCreateDTO createDTO;
    private AsignacionTurno asignacionTurno;

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
                .estado(EstadoEmpleado.ACTIVO) // Asegurar que el empleado está activo
                .rolSistema(RolSistema.EMPLEADO)
                .tieneUsuario(false)
                .build();

        tipoTurno = TipoTurno.builder()
                .id(1L)
                .nombre("Turno Diurno")
                .horaInicio(LocalTime.of(8, 0))
                .horaFin(LocalTime.of(17, 0))
                .descripcion("Jornada de día")
                .activo(true)
                .build();

        createDTO = new AsignacionTurnoCreateDTO(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(7));
        asignacionTurno = new AsignacionTurno(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(7));
    }

    @Nested
    @DisplayName("CU-TUR-006: Asignar Empleado a Turno")
    class AsignarEmpleadoTests {

        @Test
        @DisplayName("Debe asignar un empleado a un turno correctamente")
        void should_AssignEmployeeToTurno_When_DataIsValid() {
            // Arrange
            when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));
            when(tipoTurnoRepository.findById(1L)).thenReturn(Optional.of(tipoTurno));
            when(asignacionTurnoRepository.findConflictosDeHorario(any(), any(), any())).thenReturn(Collections.emptyList());
            when(asignacionTurnoMapper.toEntity(any(AsignacionTurnoCreateDTO.class))).thenReturn(asignacionTurno);
            when(asignacionTurnoRepository.save(any(AsignacionTurno.class))).thenReturn(asignacionTurno);
            when(asignacionTurnoMapper.toDTO(any(AsignacionTurno.class))).thenReturn(new AsignacionTurnoDTO(1L, 1L, 1L, LocalDate.now(), LocalDate.now().plusDays(7)));

            // Act
            AsignacionTurnoDTO result = turnoService.asignarEmpleadoATurno(createDTO);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.empleadoId()).isEqualTo(1L);
            assertThat(result.tipoTurnoId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Debe lanzar EmpleadoNotFoundException si el empleado no existe")
        void should_ThrowEmpleadoNotFoundException_When_EmpleadoDoesNotExist() {
            // Arrange
            when(empleadoRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EmpleadoNotFoundException.class, () -> {
                turnoService.asignarEmpleadoATurno(createDTO);
            });
        }

        @Test
        @DisplayName("Debe lanzar TurnoNoEncontradoException si el turno no existe")
        void should_ThrowTurnoNoEncontradoException_When_TurnoDoesNotExist() {
            // Arrange
            when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));
            when(tipoTurnoRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(TurnoNoEncontradoException.class, () -> {
                turnoService.asignarEmpleadoATurno(createDTO);
            });
        }
    }
}
