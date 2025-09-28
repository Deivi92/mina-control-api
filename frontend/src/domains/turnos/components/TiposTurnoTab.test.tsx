
import { render, screen, fireEvent } from '@testing-library/react';
import { TiposTurnoTab } from './TiposTurnoTab';
import { useTiposTurno } from '../hooks/useTiposTurno';
import { TipoTurno } from '../types';

// Mockear el hook para controlar sus salidas
vi.mock('../hooks/useTiposTurno');
const mockedUseTiposTurno = vi.mocked(useTiposTurno);

// Mockear los componentes hijos para aislar el componente contenedor
vi.mock('./TiposTurnoTable', () => ({ TiposTurnoTable: (props) => <div data-testid="tipos-turno-table" {...props} /> }));
vi.mock('./TipoTurnoForm', () => ({ TipoTurnoForm: (props) => <div data-testid="tipo-turno-form" {...props} /> }));

describe('TiposTurnoTab', () => {
  const mockCrearMutation = vi.fn();
  const mockActualizarMutation = vi.fn();
  const mockEliminarMutation = vi.fn();

  beforeEach(() => {
    // Proporcionar una implementación base para el hook mockeado
    mockedUseTiposTurno.mockReturnValue({
      tiposTurnoQuery: {
        data: [],
        isLoading: false,
        isError: false,
      },
      crearTipoTurnoMutation: { mutate: mockCrearMutation } as any,
      actualizarTipoTurnoMutation: { mutate: mockActualizarMutation } as any,
      eliminarTipoTurnoMutation: { mutate: mockEliminarMutation } as any,
    });
    vi.clearAllMocks();
  });

  it('debería pasar los datos y el estado de carga a TiposTurnoTable', () => {
    const turnos: TipoTurno[] = [{ id: 1, nombre: 'Turno Mock', horaInicio: '08:00', horaFin: '16:00', color: '#FFF' }];
    mockedUseTiposTurno.mockReturnValueOnce({
        ...mockedUseTiposTurno(),
        tiposTurnoQuery: { data: turnos, isLoading: true, isError: false },
    });

    render(<TiposTurnoTab />);

    const table = screen.getByTestId('tipos-turno-table');
    expect(table).toHaveAttribute('turnos', turnos);
    expect(table).toHaveAttribute('isLoading', 'true');
  });

  it('debería abrir el formulario en modo creación al hacer clic en "Crear Nuevo Tipo de Turno"', async () => {
    render(<TiposTurnoTab />);

    await fireEvent.click(screen.getByRole('button', { name: /crear nuevo tipo de turno/i }));

    const form = screen.getByTestId('tipo-turno-form');
    expect(form).toHaveAttribute('open', 'true');
    expect(form).not.toHaveAttribute('initialData'); // No hay datos iniciales
  });

  it('debería llamar a la mutación de crear cuando el formulario hace submit en modo creación', () => {
    render(<TiposTurnoTab />);
    fireEvent.click(screen.getByRole('button', { name: /crear nuevo tipo de turno/i }));

    const form = screen.getByTestId('tipo-turno-form');
    const onSubmit = form.props.onSubmit;
    const newTurnoData = { nombre: 'Test' };
    onSubmit(newTurnoData);

    expect(mockCrearMutation).toHaveBeenCalledWith(newTurnoData);
  });

  it('debería abrir el formulario en modo edición y llamar a la mutación de actualizar', () => {
    const turnoToEdit: TipoTurno = { id: 1, nombre: 'A Editar', horaInicio: '08:00', horaFin: '16:00', color: '#FFF' };
    mockedUseTiposTurno.mockReturnValueOnce({
        ...mockedUseTiposTurno(),
        tiposTurnoQuery: { data: [turnoToEdit], isLoading: false, isError: false },
    });
    render(<TiposTurnoTab />);

    // Simular la llamada onEdit desde la tabla
    const table = screen.getByTestId('tipos-turno-table');
    const onEdit = table.props.onEdit;
    onEdit(turnoToEdit);

    // Verificar que el form se abre con datos
    const form = screen.getByTestId('tipo-turno-form');
    expect(form).toHaveAttribute('open', 'true');
    expect(form).toHaveAttribute('initialData', turnoToEdit);

    // Simular el submit del form
    const onSubmit = form.props.onSubmit;
    const updatedData = { nombre: 'Editado' };
    onSubmit(updatedData);

    expect(mockActualizarMutation).toHaveBeenCalledWith({ id: turnoToEdit.id, data: updatedData });
  });

  it('debería llamar a la mutación de eliminar', () => {
    const turnoIdToDelete = 1;
    render(<TiposTurnoTab />);

    // Simular la llamada onDelete desde la tabla
    const table = screen.getByTestId('tipos-turno-table');
    const onDelete = table.props.onDelete;
    onDelete(turnoIdToDelete);

    expect(mockEliminarMutation).toHaveBeenCalledWith(turnoIdToDelete);
  });
});
