package com.minacontrol.reportes.unit;

import com.minacontrol.reportes.dto.response.ReporteDTO;
import com.minacontrol.reportes.entity.ReporteGenerado;
import com.minacontrol.reportes.enums.FormatoReporte;
import com.minacontrol.reportes.enums.TipoReporte;
import com.minacontrol.reportes.mapper.ReporteMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pruebas Unitarias para ReporteMapper")
class ReporteMapperTest {

    private ReporteMapper reporteMapper;

    @BeforeEach
    void setUp() {
        reporteMapper = Mappers.getMapper(ReporteMapper.class);
    }

    @Test
    @DisplayName("Debe mapear ReporteGenerado a ReporteDTO correctamente")
    void should_MapEntityToDto_Correctly() {
        // Arrange
        ReporteGenerado entity = ReporteGenerado.builder()
                .id(1L)
                .tipoReporte(TipoReporte.PRODUCCION)
                .nombreReporte("Producción Mensual")
                .fechaGeneracion(LocalDateTime.of(2025, 7, 25, 10, 0))
                .fechaInicioDatos(LocalDate.of(2025, 6, 1))
                .fechaFinDatos(LocalDate.of(2025, 6, 30))
                .formato(FormatoReporte.PDF)
                .rutaArchivo("/reports/produccion_2025_06.pdf")
                .generadoPor(5L)
                .build();

        // Act
        ReporteDTO dto = reporteMapper.toDTO(entity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.tipoReporte()).isEqualTo(entity.getTipoReporte().name());
        assertThat(dto.nombreReporte()).isEqualTo(entity.getNombreReporte());
        assertThat(dto.fechaGeneracion()).isEqualTo(entity.getFechaGeneracion());
        assertThat(dto.formato()).isEqualTo(entity.getFormato().name());
        // La URL de descarga se construye en el servicio, por lo que aquí debería ser nula o vacía
        assertThat(dto.urlDescarga()).isNullOrEmpty();
    }

    @Test
    @DisplayName("Debe manejar valores nulos al mapear de Entidad a DTO")
    void should_HandleNulls_WhenMappingEntityToDto() {
        // Arrange
        ReporteGenerado entity = new ReporteGenerado();
        entity.setId(1L);

        // Act
        ReporteDTO dto = reporteMapper.toDTO(entity);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.tipoReporte()).isNull();
        assertThat(dto.nombreReporte()).isNull();
        assertThat(dto.formato()).isNull();
    }
}
