package com.minacontrol.empleado.service;

import com.minacontrol.empleado.dto.EmpleadoRequestDTO;
import com.minacontrol.empleado.dto.EmpleadoResponseDTO;
import com.minacontrol.empleado.model.Empleado;
import com.minacontrol.empleado.model.EstadoEmpleado;
import com.minacontrol.empleado.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceImplTest {

    @Mock
    private EmpleadoRepository empleadoRepository; // Mockeamos el repositorio

    @InjectMocks
    private EmpleadoServiceImpl empleadoService; // Instancia de la clase a probar, con mocks inyectados

    private EmpleadoRequestDTO empleadoRequestDTO;
    private Empleado empleado;
    private EmpleadoResponseDTO empleadoResponseDTO;

    @BeforeEach
    void setUp() {
        empleado = new Empleado();
        empleado.setId(1L);
        empleado.setNumeroDocumento("123456789");
        empleado.setNombres("Juan");
        empleado.setApellidos("Perez");
        empleado.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        empleado.setCargo("Minero");
        empleado.setFechaIngreso(LocalDate.of(2020, 1, 1));
        empleado.setSalarioBase(new BigDecimal("2000.00"));
        empleado.setEstado(EstadoEmpleado.ACTIVO);
        empleado.setEmail("juan.perez@example.com");
        empleado.setTelefono("3001234567");

        // Inicializar empleadoRequestDTO
        empleadoRequestDTO = new EmpleadoRequestDTO(
                "123456789",
                "Juan",
                "Perez",
                LocalDate.of(1990, 1, 1),
                "Minero",
                LocalDate.of(2020, 1, 1),
                new BigDecimal("2000.00"),
                EstadoEmpleado.ACTIVO,
                "juan.perez@example.com",
                "3001234567"
        );

        empleadoResponseDTO = new EmpleadoResponseDTO(
                1L, "123456789", "Juan", "Perez",
                LocalDate.of(1990, Month.JANUARY, 15), "Desarrollador",
                LocalDate.of(2023, Month.MARCH, 10), new BigDecimal("50000.00"),
                EstadoEmpleado.ACTIVO, "juan.perez@example.com", "3001234567"
        );
    }

    @Test
    @DisplayName("crearEmpleado - Debería guardar y retornar EmpleadoResponseDTO")
    void crearEmpleado_cuandoDatosSonValidos_deberiaGuardarYRetornarDTO() {
        // Arrange
        // Configurar el mock del repositorio para que devuelva el empleado cuando se llame a save
        // El 'any(Empleado.class)' asegura que no importa la instancia exacta, sino el tipo.
        // Podríamos ser más específicos si fuera necesario con ArgumentCaptor.
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleado);

        // Act
        EmpleadoResponseDTO resultado = empleadoService.crearEmpleado(empleadoRequestDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(empleado.getId(), resultado.getId());
        assertEquals(empleadoRequestDTO.getNombre(), resultado.getNombres());
        assertEquals(empleadoRequestDTO.getApellido(), resultado.getApellidos());
        assertEquals(empleadoRequestDTO.getEmail(), resultado.getEmail());

        // Verificar que el método save del repositorio fue llamado una vez con cualquier instancia de Empleado
        verify(empleadoRepository, times(1)).save(any(Empleado.class));
    }

    @Test
    @DisplayName("obtenerEmpleadoPorId - Debería retornar Optional con EmpleadoResponseDTO si existe")
    void obtenerEmpleadoPorId_cuandoIdExiste_deberiaRetornarOptionalConDTO() {
        // Arrange
        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));

        // Act
        Optional<EmpleadoResponseDTO> resultado = empleadoService.obtenerEmpleadoPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(empleado.getId(), resultado.get().getId());
        assertEquals(empleado.getNombres(), resultado.get().getNombres());
        verify(empleadoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerEmpleadoPorId - Debería retornar Optional vacío si no existe")
    void obtenerEmpleadoPorId_cuandoIdNoExiste_deberiaRetornarOptionalVacio() {
        // Arrange
        when(empleadoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<EmpleadoResponseDTO> resultado = empleadoService.obtenerEmpleadoPorId(99L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(empleadoRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("obtenerTodosLosEmpleados - Debería retornar lista de EmpleadoResponseDTO")
    void obtenerTodosLosEmpleados_deberiaRetornarListaDTO() {
        // Arrange
        Empleado empleado2 = new Empleado();
        empleado2.setId(2L);
        empleado2.setNombres("Ana");
        empleado2.setApellidos("Gomez");
        // ... (configurar otros campos si son relevantes para el DTO)

        List<Empleado> listaEmpleados = Arrays.asList(empleado, empleado2);
        when(empleadoRepository.findAll()).thenReturn(listaEmpleados);

        // Act
        List<EmpleadoResponseDTO> resultado = empleadoService.obtenerTodosLosEmpleados();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(empleado.getNombres(), resultado.get(0).getNombres());
        assertEquals(empleado2.getNombres(), resultado.get(1).getNombres());
        verify(empleadoRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("obtenerTodosLosEmpleados - Debería retornar lista vacía si no hay empleados")
    void obtenerTodosLosEmpleados_cuandoNoHayEmpleados_deberiaRetornarListaVacia() {
        // Arrange
        when(empleadoRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<EmpleadoResponseDTO> resultado = empleadoService.obtenerTodosLosEmpleados();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(empleadoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("actualizarEmpleado - Debería actualizar y retornar Optional con EmpleadoResponseDTO si existe")
    void actualizarEmpleado_cuandoIdExiste_deberiaActualizarYRetornarOptionalConDTO() {
        // Arrange
        EmpleadoRequestDTO requestActualizacion = new EmpleadoRequestDTO(
            "123456789", "Juan Alberto", "Perez Diaz", // Nombres y apellidos cambiados
            LocalDate.of(1990, Month.JANUARY, 15), "Desarrollador Senior", // Cargo cambiado
            LocalDate.of(2023, Month.MARCH, 10), new BigDecimal("60000.00"), // Salario cambiado
            EstadoEmpleado.ACTIVO, "juan.perez.new@example.com", "3009876543"
        );

        Empleado empleadoActualizado = new Empleado(); // Simula el empleado que se guardaría
        empleadoActualizado.setId(1L);
        empleadoActualizado.setNombres(requestActualizacion.getNombre());
        empleadoActualizado.setApellidos(requestActualizacion.getApellido());
        empleadoActualizado.setCargo(requestActualizacion.getCargo());
        empleadoActualizado.setSalarioBase(requestActualizacion.getSalario());
        empleadoActualizado.setEmail(requestActualizacion.getEmail());
        // ... otros campos que se actualizan

        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado)); // Empleado original encontrado
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(empleadoActualizado); // Devuelve el empleado "actualizado"

        // Act
        Optional<EmpleadoResponseDTO> resultado = empleadoService.actualizarEmpleado(1L, requestActualizacion);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("Juan Alberto", resultado.get().getNombres());
        assertEquals("Perez Diaz", resultado.get().getApellidos());
        assertEquals("Desarrollador Senior", resultado.get().getCargo());
        assertEquals(new BigDecimal("60000.00"), resultado.get().getSalarioBase()); // Corregido: getSalario -> getSalarioBase
        assertEquals("juan.perez.new@example.com", resultado.get().getEmail());

        verify(empleadoRepository, times(1)).findById(1L);
        verify(empleadoRepository, times(1)).save(any(Empleado.class));
    }

    @Test
    @DisplayName("actualizarEmpleado - Debería retornar Optional vacío si no existe")
    void actualizarEmpleado_cuandoIdNoExiste_deberiaRetornarOptionalVacio() {
        // Arrange
        when(empleadoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<EmpleadoResponseDTO> resultado = empleadoService.actualizarEmpleado(99L, empleadoRequestDTO);

        // Assert
        assertFalse(resultado.isPresent());
        verify(empleadoRepository, times(1)).findById(99L);
        verify(empleadoRepository, never()).save(any(Empleado.class)); // No se debe llamar a save si no se encuentra
    }

    @Test
    @DisplayName("eliminarEmpleado - Debería retornar true si el empleado existe y se elimina")
    void eliminarEmpleado_cuandoIdExiste_deberiaRetornarTrue() {
        // Arrange
        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(empleado));
        doNothing().when(empleadoRepository).deleteById(1L); // No hace nada cuando se llama a deleteById

        // Act
        boolean resultado = empleadoService.eliminarEmpleado(1L);

        // Assert
        assertTrue(resultado);
        verify(empleadoRepository, times(1)).findById(1L);
        verify(empleadoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("eliminarEmpleado - Debería retornar false si el empleado no existe")
    void eliminarEmpleado_cuandoIdNoExiste_deberiaRetornarFalse() {
        // Arrange
        when(empleadoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        boolean resultado = empleadoService.eliminarEmpleado(99L);

        // Assert
        assertFalse(resultado);
        verify(empleadoRepository, times(1)).findById(99L);
        verify(empleadoRepository, never()).deleteById(anyLong()); // No se debe llamar a delete si no se encuentra
    }
}
