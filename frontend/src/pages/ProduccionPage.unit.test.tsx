import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { ProduccionPage } from './ProduccionPage';
import { useProduccion } from '../domains/produccion/hooks/useProduccion';
import { useEmpleados } from '../domains/empleado/hooks/useEmpleados';
import { useTiposTurno } from '../domains/turnos/hooks/useTiposTurno';
import userEvent from '@testing-library/user-event';

// Mock de hooks
vi.mock('../domains/produccion/hooks/useProduccion', () => ({
  useProduccion: vi.fn(),
}));

vi.mock('../domains/empleado/hooks/useEmpleados', () => ({
  useEmpleados: vi.fn(() => ({
    empleadosQuery: { data: [], isLoading: false },
  })),
}));

vi.mock('../domains/turnos/hooks/useTiposTurno', () => ({
  useTiposTurno: vi.fn(() => ({
    tiposTurnoQuery: { data: [], isLoading: false },
  })),
}));

// Mock de servicios
vi.mock('../../services/produccion.service', () => ({
  produccionService: {
    listarRegistros: vi.fn(),
    registrarProduccion: vi.fn(),
    obtenerRegistroPorId: vi.fn(),
    actualizarRegistro: vi.fn(),
    eliminarRegistro: vi.fn(),
    validarRegistro: vi.fn()
  }
}));

describe('ProduccionPage - Pruebas de Integración', () => {
  // Mock de datos con valores únicos para evitar conflictos
  const mockListarRegistros = {
    data: [
      {
        id: 1,
        empleadoId: 1,
        nombreEmpleado: 'Juan Pérez',
        tipoTurnoId: 1,
        nombreTurno: 'Turno Día',
        fechaRegistro: '2024-01-15',
        cantidadExtraidaToneladas: 15.5,
        ubicacionExtraccion: 'Zona A',
        observaciones: 'Observaciones 1',
        validado: true
      },
      {
        id: 2,
        empleadoId: 2,
        nombreEmpleado: 'María González',
        tipoTurnoId: 2,
        nombreTurno: 'Turno Noche',
        fechaRegistro: '2024-01-16', // Fecha única
        cantidadExtraidaToneladas: 12.3,
        ubicacionExtraccion: 'Zona B',
        observaciones: 'Observaciones 2',
        validado: false
      }
    ],
    isLoading: false,
    error: null,
    refetch: vi.fn()
  };

  const mockMutations = {
    mutate: vi.fn(),
    isPending: false,
  };

  beforeEach(() => {
    vi.clearAllMocks();

    // Mock principal de useProduccion
    (useProduccion as vi.Mock).mockReturnValue({
      listarRegistros: vi.fn().mockReturnValue(mockListarRegistros),
      registrarProduccion: vi.fn().mockReturnValue(mockMutations),
      actualizarRegistro: vi.fn().mockReturnValue(mockMutations),
      eliminarRegistro: vi.fn().mockReturnValue(mockMutations),
      validarRegistro: vi.fn().mockReturnValue(mockMutations),
      obtenerRegistroPorId: vi.fn().mockReturnValue({ data: null, isLoading: false, error: null })
    });

    // Mock de los hooks para filtros con datos
    (useEmpleados as vi.Mock).mockReturnValue({
      empleadosQuery: { data: [{ id: 1, nombre: 'Juan Pérez' }], isLoading: false }
    });
    (useTiposTurno as vi.Mock).mockReturnValue({
      tiposTurnoQuery: { data: [{ id: 1, nombre: 'Turno Día' }], isLoading: false }
    });

    // Mock de window.confirm
    vi.spyOn(window, 'confirm').mockReturnValue(true);
  });

  it('debería renderizar el título y los componentes principales', () => {
    render(<ProduccionPage />);
    expect(screen.getByText('Producción Diaria')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /registrar nueva producción/i })).toBeInTheDocument();
  });

  it('debería mostrar la lista de registros de producción', () => {
    render(<ProduccionPage />);
    expect(screen.getByText('Juan Pérez')).toBeInTheDocument();
    expect(screen.getByText('María González')).toBeInTheDocument();
  });

  it('debería mostrar un indicador de carga', () => {
    (useProduccion as vi.Mock).mockReturnValueOnce({
      listarRegistros: vi.fn().mockReturnValue({ ...mockListarRegistros, isLoading: true }),
      //... resto de funciones mockeadas para evitar errores
      registrarProduccion: vi.fn().mockReturnValue(mockMutations),
      actualizarRegistro: vi.fn().mockReturnValue(mockMutations),
      eliminarRegistro: vi.fn().mockReturnValue(mockMutations),
      validarRegistro: vi.fn().mockReturnValue(mockMutations),
      obtenerRegistroPorId: vi.fn().mockReturnValue({ data: null, isLoading: false, error: null })
    });
    render(<ProduccionPage />);
    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  it('debería manejar el estado de error', () => {
    (useProduccion as vi.Mock).mockReturnValueOnce({
      listarRegistros: vi.fn().mockReturnValue({ ...mockListarRegistros, error: new Error('Error de red') }),
      //... resto de funciones mockeadas
      registrarProduccion: vi.fn().mockReturnValue(mockMutations),
      actualizarRegistro: vi.fn().mockReturnValue(mockMutations),
      eliminarRegistro: vi.fn().mockReturnValue(mockMutations),
      validarRegistro: vi.fn().mockReturnValue(mockMutations),
      obtenerRegistroPorId: vi.fn().mockReturnValue({ data: null, isLoading: false, error: null })
    });
    render(<ProduccionPage />);
    expect(screen.getByText(/Error al cargar/i)).toBeInTheDocument();
  });

  it('debería permitir eliminar un registro', async () => {
    const user = userEvent.setup();
    render(<ProduccionPage />);
    const deleteButtons = screen.getAllByRole('button', { name: /eliminar/i });
    await user.click(deleteButtons[0]);
    expect(window.confirm).toHaveBeenCalled();
    expect(mockMutations.mutate).toHaveBeenCalledWith(1);
  });
});