import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { ProduccionPage } from '../../../pages/ProduccionPage';
import { useProduccion } from '../../hooks/useProduccion';
import userEvent from '@testing-library/user-event';

// Mock del hook useProduccion
vi.mock('../../hooks/useProduccion', () => ({
  useProduccion: vi.fn()
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
        observaciones: 'Observaciones',
        validado: true
      },
      {
        id: 2,
        empleadoId: 2,
        nombreEmpleado: 'María González',
        tipoTurnoId: 2,
        nombreTurno: 'Turno Noche',
        fechaRegistro: '2024-01-15',
        cantidadExtraidaToneladas: 12.3,
        ubicacionExtraccion: 'Zona B',
        observaciones: 'Observaciones',
        validado: false
      }
    ],
    isLoading: false,
    error: null,
    refetch: vi.fn()
  };

  const mockRegistrarProduccion = {
    mutate: vi.fn(),
    isLoading: false,
    error: null,
    data: null
  };

  const mockActualizarRegistro = {
    mutate: vi.fn(),
    isLoading: false,
    error: null,
    data: null
  };

  const mockEliminarRegistro = {
    mutate: vi.fn(),
    isLoading: false,
    error: null,
    data: null
  };

  const mockValidarRegistro = {
    mutate: vi.fn(),
    isLoading: false,
    error: null,
    data: null
  };

  beforeEach(() => {
    vi.clearAllMocks();

    (useProduccion as vi.MockedFunction<typeof useProduccion>).mockReturnValue({
      listarRegistros: vi.fn().mockReturnValue(mockListarRegistros),
      registrarProduccion: vi.fn().mockReturnValue(mockRegistrarProduccion),
      actualizarRegistro: vi.fn().mockReturnValue(mockActualizarRegistro),
      eliminarRegistro: vi.fn().mockReturnValue(mockEliminarRegistro),
      validarRegistro: vi.fn().mockReturnValue(mockValidarRegistro),
      obtenerRegistroPorId: vi.fn().mockReturnValue({
        data: null,
        isLoading: false,
        error: null
      })
    });
  });

  it('debería renderizar el título y los componentes principales de la página de producción', () => {
    render(<ProduccionPage />);

    expect(screen.getByText('Producción Diaria')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /registrar nueva producción/i })).toBeInTheDocument();
    expect(screen.getByTestId('registro-produccion-table')).toBeInTheDocument();
    expect(screen.getByTestId('registro-produccion-filter')).toBeInTheDocument();
  });

  it('debería mostrar la lista de registros de producción al cargar la página exitosamente', () => {
    render(<ProduccionPage />);

    // Verificar que se muestran los registros
    mockListarRegistros.data.forEach(registro => {
      expect(screen.getByText(registro.nombreEmpleado)).toBeInTheDocument();
      expect(screen.getByText(registro.nombreTurno)).toBeInTheDocument();
      expect(screen.getByText(registro.fechaRegistro)).toBeInTheDocument();
      expect(screen.getByText(registro.cantidadExtraidaToneladas.toString())).toBeInTheDocument();
    });
  });

  it('debería mostrar un indicador de carga mientras se obtienen los datos', () => {
    (useProduccion as vi.MockedFunction<typeof useProduccion>).mockReturnValue({
      listarRegistros: vi.fn().mockReturnValue({
        ...mockListarRegistros,
        isLoading: true
      }),
      registrarProduccion: vi.fn().mockReturnValue(mockRegistrarProduccion),
      actualizarRegistro: vi.fn().mockReturnValue(mockActualizarRegistro),
      eliminarRegistro: vi.fn().mockReturnValue(mockEliminarRegistro),
      validarRegistro: vi.fn().mockReturnValue(mockValidarRegistro),
      obtenerRegistroPorId: vi.fn().mockReturnValue({
        data: null,
        isLoading: false,
        error: null
      })
    });

    render(<ProduccionPage />);

    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  it('debería permitir crear un nuevo registro de producción y mostrarlo en la tabla', async () => {
    const user = userEvent.setup();
    render(<ProduccionPage />);

    // Hacer clic en el botón de registrar nueva producción
    const registrarButton = screen.getByRole('button', { name: /registrar nueva producción/i });
    await user.click(registrarButton);

    // Verificar que se abre el formulario
    expect(screen.getByText('Registro de Producción')).toBeInTheDocument();

    // Simular el llenado del formulario y envío
    const empleadoSelect = screen.getByLabelText(/empleado/i);
    await user.selectOptions(empleadoSelect, '1');

    const fechaInput = screen.getByLabelText(/fecha/i);
    fireEvent.change(fechaInput, { target: { value: '2024-01-20' } });

    const turnoSelect = screen.getByLabelText(/turno/i);
    await user.selectOptions(turnoSelect, '2');

    const cantidadInput = screen.getByLabelText(/cantidad extraída/i);
    await user.type(cantidadInput, '18.7');

    const ubicacionInput = screen.getByLabelText(/ubicación de extracción/i);
    await user.type(ubicacionInput, 'Zona C');

    const guardarButton = screen.getByRole('button', { name: /guardar/i });
    await user.click(guardarButton);

    // Verificar que se llamó a la mutación de registro
    expect(mockRegistrarProduccion.mutate).toHaveBeenCalledWith({
      empleadoId: 1,
      fechaRegistro: '2024-01-20',
      tipoTurnoId: 2,
      cantidadExtraidaToneladas: 18.7,
      ubicacionExtraccion: 'Zona C',
      observaciones: ''
    });
  });

  it('debería permitir eliminar un registro de producción', async () => {
    const user = userEvent.setup();
    render(<ProduccionPage />);

    // Buscar el botón de eliminar del segundo registro
    const deleteButtons = screen.getAllByRole('button', { name: /eliminar/i });
    await user.click(deleteButtons[1]);

    // Verificar que se llamó a la mutación de eliminación con el ID correcto
    expect(mockEliminarRegistro.mutate).toHaveBeenCalledWith(2);
  });

  it('debería permitir validar un registro de producción', async () => {
    const user = userEvent.setup();
    render(<ProduccionPage />);

    // Buscar el botón de validación del primer registro
    const validateButtons = screen.getAllByRole('button', { name: /validar/i });
    await user.click(validateButtons[0]);

    // Verificar que se llamó a la mutación de validación con el ID correcto
    expect(mockValidarRegistro.mutate).toHaveBeenCalledWith(1);
  });

  it('debería aplicar filtros y actualizar la lista de registros', async () => {
    const user = userEvent.setup();
    render(<ProduccionPage />);

    // Rellenar los filtros
    const empleadoSelect = screen.getByLabelText(/empleado/i);
    await user.selectOptions(empleadoSelect, '1');

    const fechaInicioInput = screen.getByLabelText(/fecha inicio/i);
    fireEvent.change(fechaInicioInput, { target: { value: '2024-01-01' } });

    const fechaFinInput = screen.getByLabelText(/fecha fin/i);
    fireEvent.change(fechaFinInput, { target: { value: '2024-01-31' } });

    const turnoSelect = screen.getByLabelText(/turno/i);
    await user.selectOptions(turnoSelect, '2');

    const aplicarButton = screen.getByRole('button', { name: /aplicar filtros/i });
    await user.click(aplicarButton);

    // Verificar que se llamó a la función de listado con los filtros aplicados
    const listarRegistrosMock = (useProduccion as vi.MockedFunction<typeof useProduccion>).mock
      .returnValue({ ...mockListarRegistros, refetch: vi.fn() });
    
    // Simular que refetch se llamó con los parámetros de filtro
    expect(listarRegistrosMock.listarRegistros).toHaveBeenCalled();
  });

  it('debería manejar el estado de error en la obtención de registros', () => {
    (useProduccion as vi.MockedFunction<typeof useProduccion>).mockReturnValue({
      listarRegistros: vi.fn().mockReturnValue({
        ...mockListarRegistros,
        error: new Error('Error al cargar registros')
      }),
      registrarProduccion: vi.fn().mockReturnValue(mockRegistrarProduccion),
      actualizarRegistro: vi.fn().mockReturnValue(mockActualizarRegistro),
      eliminarRegistro: vi.fn().mockReturnValue(mockEliminarRegistro),
      validarRegistro: vi.fn().mockReturnValue(mockValidarRegistro),
      obtenerRegistroPorId: vi.fn().mockReturnValue({
        data: null,
        isLoading: false,
        error: null
      })
    });

    render(<ProduccionPage />);

    expect(screen.getByText('Error al cargar registros')).toBeInTheDocument();
  });
});