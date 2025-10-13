import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { RegistroProduccionFilter } from './RegistroProduccionFilter';
import userEvent from '@testing-library/user-event';

describe('RegistroProduccionFilter', () => {
  const mockEmpleados = [
    { id: 1, nombres: 'Juan', apellidos: 'Pérez', numeroIdentificacion: '12345678' },
    { id: 2, nombres: 'María', apellidos: 'González', numeroIdentificacion: '87654321' }
  ];
  const mockTiposTurno = [
    { id: 1, nombre: 'Turno Día', descripcion: 'Turno de día', activo: true },
    { id: 2, nombre: 'Turno Noche', descripcion: 'Turno de noche', activo: true }
  ];

  const mockOnFilter = vi.fn();

  const defaultProps = {
    empleados: mockEmpleados,
    tiposTurno: mockTiposTurno,
    onFilter: mockOnFilter
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('debería renderizar los controles de filtro correctamente', () => {
    render(<RegistroProduccionFilter {...defaultProps} />);

    expect(screen.getByLabelText(/empleado/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/fecha inicio/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/fecha fin/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/turno/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /aplicar filtros/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /limpiar filtros/i })).toBeInTheDocument();
  });

  it('debería permitir al usuario seleccionar un empleado', async () => {
    const user = userEvent.setup();
    render(<RegistroProduccionFilter {...defaultProps} />);

    const empleadoSelect = screen.getByLabelText(/empleado/i);
    await user.click(empleadoSelect);
    
    const option = screen.getByText('Juan Pérez');
    await user.click(option);
  });

  it('debería permitir al usuario ingresar un rango de fechas', () => {
    render(<RegistroProduccionFilter {...defaultProps} />);

    const fechaInicioInput = screen.getByLabelText(/fecha inicio/i);
    const fechaFinInput = screen.getByLabelText(/fecha fin/i);

    fireEvent.change(fechaInicioInput, { target: { value: '2024-01-01' } });
    fireEvent.change(fechaFinInput, { target: { value: '2024-01-31' } });

    expect(fechaInicioInput).toHaveValue('2024-01-01');
    expect(fechaFinInput).toHaveValue('2024-01-31');
  });

  it('debería permitir al usuario seleccionar un turno', async () => {
    const user = userEvent.setup();
    render(<RegistroProduccionFilter {...defaultProps} />);

    const turnoSelect = screen.getByLabelText(/turno/i);
    await user.click(turnoSelect);
    
    const option = screen.getByText('Turno Noche');
    await user.click(option);
  });

  it('debería llamar a onFilter con los valores correctos cuando se hace clic en aplicar filtros', async () => {
    const user = userEvent.setup();
    render(<RegistroProduccionFilter {...defaultProps} />);

    // Rellenar los filtros
    const empleadoSelect = screen.getByLabelText(/empleado/i);
    await user.click(empleadoSelect);
    const empleadoOption = screen.getByText('Juan Pérez');
    await user.click(empleadoOption);

    const fechaInicioInput = screen.getByLabelText(/fecha inicio/i);
    fireEvent.change(fechaInicioInput, { target: { value: '2024-01-01' } });

    const fechaFinInput = screen.getByLabelText(/fecha fin/i);
    fireEvent.change(fechaFinInput, { target: { value: '2024-01-31' } });

    const turnoSelect = screen.getByLabelText(/turno/i);
    await user.click(turnoSelect);
    const turnoOption = screen.getByText('Turno Noche');
    await user.click(turnoOption);

    const aplicarButton = screen.getByRole('button', { name: /aplicar filtros/i });
    await user.click(aplicarButton);

    // Verificar que onFilter fue llamado con los valores correctos
    expect(mockOnFilter).toHaveBeenCalledWith({
      empleadoId: 1,
      fechaInicio: '2024-01-01',
      fechaFin: '2024-01-31',
      tipoTurnoId: 2
    });
  });

  it('debería llamar a onFilter con valores vacíos cuando se hace clic en limpiar filtros', async () => {
    const user = userEvent.setup();
    render(<RegistroProduccionFilter {...defaultProps} />);

    const limpiarButton = screen.getByRole('button', { name: /limpiar filtros/i });
    await user.click(limpiarButton);

    // Verificar que onFilter fue llamado con valores vacíos
    expect(mockOnFilter).toHaveBeenCalledWith({
      empleadoId: undefined,
      fechaInicio: undefined,
      fechaFin: undefined,
      tipoTurnoId: undefined
    });
  });

  it('debería preseleccionar los valores si se pasan como props', () => {
    const initialValues = {
      empleadoId: 2,
      fechaInicio: '2024-02-01',
      fechaFin: '2024-02-28',
      tipoTurnoId: 1
    };

    render(<RegistroProduccionFilter {...defaultProps} initialValues={initialValues} />);

    const empleadoSelect = screen.getByLabelText(/empleado/i);
    const fechaInicioInput = screen.getByLabelText(/fecha inicio/i);
    const fechaFinInput = screen.getByLabelText(/fecha fin/i);
    const turnoSelect = screen.getByLabelText(/turno/i);

    // Para selects de MUI, verificamos el contenido de texto en lugar del valor
    expect(empleadoSelect).toHaveTextContent('María González');
    expect(fechaInicioInput).toHaveValue('2024-02-01');
    expect(fechaFinInput).toHaveValue('2024-02-28');
    expect(turnoSelect).toHaveTextContent('Turno Día');
  });
});