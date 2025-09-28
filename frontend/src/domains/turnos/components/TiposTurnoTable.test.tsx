
import { render, screen, fireEvent } from '@testing-library/react';
import { TiposTurnoTable } from './TiposTurnoTable';
import { TipoTurno } from '../types';

describe('TiposTurnoTable', () => {
  const mockOnEdit = vi.fn();
  const mockOnDelete = vi.fn();

  const turnos: TipoTurno[] = [
    { id: 1, nombre: 'Turno de Mañana', horaInicio: '06:00:00', horaFin: '14:00:00', color: '#FFD700' },
    { id: 2, nombre: 'Turno de Tarde', horaInicio: '14:00:00', horaFin: '22:00:00', color: '#FFA500' },
  ];

  beforeEach(() => {
    mockOnEdit.mockClear();
    mockOnDelete.mockClear();
  });

  it('debería renderizar la tabla con los datos de los turnos', () => {
    render(<TiposTurnoTable turnos={turnos} onEdit={mockOnEdit} onDelete={mockOnDelete} isLoading={false} />);

    // Verificar que los nombres de los turnos están en la tabla
    expect(screen.getByText('Turno de Mañana')).toBeInTheDocument();
    expect(screen.getByText('Turno de Tarde')).toBeInTheDocument();

    // Verificar que las horas están
    expect(screen.getByText('06:00:00')).toBeInTheDocument();
    expect(screen.getByText('22:00:00')).toBeInTheDocument();
  });

  it('debería mostrar un indicador de carga cuando isLoading es true', () => {
    render(<TiposTurnoTable turnos={[]} onEdit={mockOnEdit} onDelete={mockOnDelete} isLoading={true} />);

    // Asume que se muestra un CircularProgress de MUI, que tiene el rol "progressbar"
    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  it('debería mostrar un mensaje cuando no hay turnos', () => {
    render(<TiposTurnoTable turnos={[]} onEdit={mockOnEdit} onDelete={mockOnDelete} isLoading={false} />);

    expect(screen.getByText(/no se encontraron tipos de turno/i)).toBeInTheDocument();
  });

  it('debería llamar a onEdit con el turno correcto cuando se hace clic en editar', async () => {
    render(<TiposTurnoTable turnos={turnos} onEdit={mockOnEdit} onDelete={mockOnDelete} isLoading={false} />);

    // Busca el botón de editar en la fila del "Turno de Mañana"
    const editButtons = screen.getAllByLabelText(/editar/i);
    await fireEvent.click(editButtons[0]); // Clic en el primer botón de editar

    expect(mockOnEdit).toHaveBeenCalledTimes(1);
    expect(mockOnEdit).toHaveBeenCalledWith(turnos[0]);
  });

  it('debería llamar a onDelete con el id correcto cuando se hace clic en eliminar', async () => {
    render(<TiposTurnoTable turnos={turnos} onEdit={mockOnEdit} onDelete={mockOnDelete} isLoading={false} />);

    // Busca el botón de eliminar en la fila del "Turno de Tarde"
    const deleteButtons = screen.getAllByLabelText(/eliminar/i);
    await fireEvent.click(deleteButtons[1]); // Clic en el segundo botón de eliminar

    expect(mockOnDelete).toHaveBeenCalledTimes(1);
    expect(mockOnDelete).toHaveBeenCalledWith(turnos[1].id);
  });
});
