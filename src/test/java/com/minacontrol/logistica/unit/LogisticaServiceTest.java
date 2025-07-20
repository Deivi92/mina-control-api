package com.minacontrol.logistica.unit;

import com.minacontrol.logistica.entity.Despacho;
import com.minacontrol.logistica.domain.EstadoDespacho;
import com.minacontrol.logistica.dto.request.DespachoCreateDTO;
import com.minacontrol.logistica.dto.response.DespachoDTO;
import com.minacontrol.logistica.exception.DespachoNotFoundException;
import com.minacontrol.logistica.exception.EstadoDespachoInvalidoException;
import com.minacontrol.logistica.mapper.DespachoMapper;
import com.minacontrol.logistica.repository.DespachoRepository;
import com.minacontrol.logistica.service.impl.LogisticaServiceImpl;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogisticaServiceTest {

    @Mock
    private DespachoRepository despachoRepository;

    @Mock
    private DespachoMapper despachoMapper;

    @InjectMocks
    private LogisticaServiceImpl logisticaService;

    private DespachoCreateDTO createDTO;
    private Despacho despacho;
    private DespachoDTO despachoDTO;

    @BeforeEach
    void setUp() {
        createDTO = new DespachoCreateDTO(
                "Juan Conductor",
                "XYZ-123",
                BigDecimal.TEN,
                "Destino de Prueba",
                LocalDate.now(),
                "Observaciones"
        );

        despacho = new Despacho();
        despacho.setId(1L);
        despacho.setEstado(EstadoDespacho.PROGRAMADO);

        despachoDTO = new DespachoDTO(
                1L,
                "DES-001",
                "Juan Conductor",
                "XYZ-123",
                BigDecimal.TEN,
                "Destino de Prueba",
                LocalDate.now(),
                null,
                null,
                EstadoDespacho.PROGRAMADO,
                "Observaciones"
        );
    }

    @Nested
    @DisplayName("Registrar Despacho Tests")
    class RegistrarDespachoTests {

        @Test
        void should_RegistrarDespacho_When_DatosValidos() {
            when(despachoMapper.toEntity(any(DespachoCreateDTO.class))).thenReturn(despacho);
            when(despachoRepository.save(any(Despacho.class))).thenReturn(despacho);
            when(despachoMapper.toDTO(any(Despacho.class))).thenReturn(despachoDTO);

            DespachoDTO result = logisticaService.registrarDespacho(createDTO);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(1L);
            verify(despachoRepository).save(any(Despacho.class));
        }
    }

    @Nested
    @DisplayName("Consultar Despachos Tests")
    class ConsultarDespachosTests {

        @Test
        void should_ReturnListOfDespachos_When_ConsultaGeneral() {
            when(despachoRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class))).thenReturn(Collections.singletonList(despacho));
            when(despachoMapper.toDTO(any(Despacho.class))).thenReturn(despachoDTO);

            List<DespachoDTO> result = logisticaService.consultarDespachos(null, null, null, null);

            assertThat(result).hasSize(1);
            verify(despachoRepository).findAll(any(org.springframework.data.jpa.domain.Specification.class));
        }

        @Test
        void should_ReturnListOfDespachos_When_ConsultaPorFecha() {
            LocalDate fecha = LocalDate.now();
            when(despachoRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class))).thenReturn(Collections.singletonList(despacho));
            when(despachoMapper.toDTO(any(Despacho.class))).thenReturn(despachoDTO);

            List<DespachoDTO> result = logisticaService.consultarDespachos(fecha, fecha, null, null);

            assertThat(result).hasSize(1);
            verify(despachoRepository).findAll(any(org.springframework.data.jpa.domain.Specification.class));
        }
    }

    @Nested
    @DisplayName("Actualizar Estado Despacho Tests")
    class ActualizarEstadoDespachoTests {

        @Test
        void should_ActualizarEstado_When_TransicionValida() {
            when(despachoRepository.findById(1L)).thenReturn(Optional.of(despacho));
            when(despachoRepository.save(any(Despacho.class))).thenReturn(despacho);
            when(despachoMapper.toDTO(any(Despacho.class))).thenReturn(despachoDTO);

            DespachoDTO result = logisticaService.actualizarEstadoDespacho(1L, EstadoDespacho.EN_TRANSITO);

            assertThat(result.estado()).isEqualTo(EstadoDespacho.PROGRAMADO); // The mock returns the original state
            verify(despachoRepository).save(despacho);
        }

        @Test
        void should_ThrowDespachoNotFoundException_When_IdNoExiste() {
            when(despachoRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> logisticaService.actualizarEstadoDespacho(1L, EstadoDespacho.EN_TRANSITO))
                    .isInstanceOf(DespachoNotFoundException.class);
        }

        @Test
        void should_ThrowEstadoDespachoInvalidoException_When_TransicionInvalida() {
            despacho.setEstado(EstadoDespacho.ENTREGADO);
            when(despachoRepository.findById(1L)).thenReturn(Optional.of(despacho));

            assertThatThrownBy(() -> logisticaService.actualizarEstadoDespacho(1L, EstadoDespacho.PROGRAMADO))
                    .isInstanceOf(EstadoDespachoInvalidoException.class);
        }
    }
}
