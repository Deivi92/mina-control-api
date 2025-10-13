import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import { RegistroProduccionTable } from './RegistroProduccionTable';
import userEvent from '@testing-library/user-event';

describe('RegistroProduccionTable', () => {
  const mockRegistros = [
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
  ];

  const mockOnEdit = vi.fn();
  const mockOnDelete = vi.fn();
  const mockOnValidate = vi.fn();

  const defaultProps = {
    registros: mockRegistros,
    onEdit: mockOnEdit,
    onDelete: mockOnDelete,
    onValidate: mockOnValidate,
    isLoading: false
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('debería renderizar la tabla con los datos de los registros de producción', () => {
    render(<RegistroProduccionTable {...defaultProps} />);

    // Verificar que se muestran los registros
    mockRegistros.forEach(registro => {
      expect(screen.getByText(registro.nombreEmpleado)).toBeInTheDocument();
      expect(screen.getByText(registro.nombreTurno)).toBeInTheDocument();
      // Usar getAllByText para manejar valores duplicados
      const fechaElements = screen.getAllByText(registro.fechaRegistro);
      expect(fechaElements).toHaveLength(2); // Se espera que aparezca dos veces
      expect(screen.getByText(registro.cantidadExtraidaToneladas.toString())).toBeInTheDocument();
      expect(screen.getByText(registro.ubicacionExtraccion)).toBeInTheDocument();
    });
  });

  it('debería mostrar un indicador de carga cuando isLoading es true', () => {
    render(<RegistroProduccionTable {...defaultProps} isLoading={true} />);

    // Verificar que se muestra un spinner o indicador de carga
    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  it('debería mostrar un mensaje cuando no hay registros', () => {
    render(<RegistroProduccionTable {...defaultProps} registros={[]} />);

    expect(screen.getByText('No se encontraron registros de producción')).toBeInTheDocument();
  });

  it('debería llamar a onEdit con el registro correcto cuando se hace clic en editar', async () => {
    const user = userEvent.setup();
    render(<RegistroProduccionTable {...defaultProps} />);

    // Buscar el botón de editar del primer registro
    const editButtons = screen.getAllByRole('button', { name: /editar/i });
    await user.click(editButtons[0]);

    expect(mockOnEdit).toHaveBeenCalledWith(mockRegistros[0]);
  });

  it('debería llamar a onDelete con el id correcto cuando se hace clic en eliminar', async () => {
    const user = userEvent.setup();
    render(<RegistroProduccionTable {...defaultProps} />);

    // Buscar el botón de eliminar del segundo registro
    const deleteButtons = screen.getAllByRole('button', { name: /eliminar/i });
    await user.click(deleteButtons[1]);

    expect(mockOnDelete).toHaveBeenCalledWith(mockRegistros[1].id);
  });

  it('debería llamar a onValidate con el id correcto cuando se hace clic en el botón de validación', async () => {
    const user = userEvent.setup();
    render(<RegistroProduccionTable {...defaultProps} />);

    // Buscar el botón de validación del primer registro
    const validateButtons = screen.getAllByRole('button', { name: /validar/i });
    await user.click(validateButtons[0]);

    expect(mockOnValidate).toHaveBeenCalledWith(mockRegistros[0].id);
  });

  it('debería mostrar el estado de validación correctamente', () => {
    render(<RegistroProduccionTable {...defaultProps} />);

    // Verificar que se muestra la información de validación
    // Buscar por el contenido de texto de las celdas de validación
    
    // El primer registro está validado (validado: true), por lo que debería tener un ícono
    const firstRow = screen.getAllByRole('row')[1]; // [0] es el header, [1] es el primer registro
    const firstValidacionCell = firstRow.cells[5]; // Columna de validación (índice 5)
    // Verificamos que haya contenido en la celda (el ícono de validación)
    expect(firstValidacionCell).not.toHaveTextContent('-');
    
    // El segundo registro no está validado (validado: false), por lo que debería tener un guión
    const secondRow = screen.getAllByRole('row')[2]; // [0] header, [1] primer registro, [2] segundo registro
    const secondValidacionCell = secondRow.cells[5]; // Columna de validación
    expect(secondValidacionCell).toHaveTextContent('-');
  });
});