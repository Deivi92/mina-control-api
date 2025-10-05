
import { render, screen } from '@testing-library/react';
import { TiposTurnoTab } from './TiposTurnoTab';
import { useTiposTurno } from '../hooks/useTiposTurno';
import userEvent from '@testing-library/user-event';

// Mockear el hook para controlar sus salidas
vi.mock('../hooks/useTiposTurno');
const mockedUseTiposTurno = vi.mocked(useTiposTurno);

// Mockear los componentes hijos de forma más funcional
vi.mock('./TiposTurnoTable', () => ({
  TiposTurnoTable: ({ onEdit, onDelete, isLoading }) => (
    <div data-testid="tipos-turno-table" data-loading={isLoading}>
      <button onClick={() => onEdit({ id: 1, nombre: 'A Editar' })}>Editar Turno 1</button>
      <button onClick={() => onDelete(1)}>Eliminar Turno 1</button>
    </div>
  ),
}));
vi.mock('./TipoTurnoForm', () => ({
  TipoTurnoForm: ({ open, onSubmit, initialData }) => {
    if (!open) return null;
    return (
      <div data-testid="tipo-turno-form" data-initial-data={initialData ? 'true' : 'false'}>
        <button onClick={() => onSubmit({ nombre: 'Nuevo Turno' })}>Submit Form</button>
      </div>
    );
  },
}));

describe('TiposTurnoTab', () => {
  const mockCrearMutation = vi.fn();
  const mockActualizarMutation = vi.fn();
  const mockEliminarMutation = vi.fn();
  const user = userEvent.setup();

  beforeEach(() => {
    mockedUseTiposTurno.mockReturnValue({
      tiposTurnoQuery: { data: [], isLoading: false, isError: false },
      crearTipoTurnoMutation: { mutate: mockCrearMutation } as any,
      actualizarTipoTurnoMutation: { mutate: mockActualizarMutation } as any,
      eliminarTipoTurnoMutation: { mutate: mockEliminarMutation } as any,
    });
    vi.clearAllMocks();
  });

  it('debería pasar el estado de carga a TiposTurnoTable', () => {
    mockedUseTiposTurno.mockReturnValueOnce({
      ...mockedUseTiposTurno(),
      tiposTurnoQuery: { data: [], isLoading: true, isError: false },
    });
    render(<TiposTurnoTab />);
    expect(screen.getByTestId('tipos-turno-table')).toHaveAttribute('data-loading', 'true');
  });

  it('debería abrir el formulario en modo creación al hacer clic en "Crear Nuevo Tipo de Turno"', async () => {
    render(<TiposTurnoTab />);
    expect(screen.queryByTestId('tipo-turno-form')).not.toBeInTheDocument();
    await user.click(screen.getByRole('button', { name: /crear nuevo tipo de turno/i }));
    expect(screen.getByTestId('tipo-turno-form')).toBeInTheDocument();
    expect(screen.getByTestId('tipo-turno-form')).toHaveAttribute('data-initial-data', 'false');
  });

  it('debería llamar a la mutación de crear cuando el formulario hace submit en modo creación', async () => {
    render(<TiposTurnoTab />);
    await user.click(screen.getByRole('button', { name: /crear nuevo tipo de turno/i }));
    await user.click(screen.getByRole('button', { name: /submit form/i }));
    expect(mockCrearMutation).toHaveBeenCalledWith({ nombre: 'Nuevo Turno' }, expect.anything());
  });

  it('debería abrir el formulario en modo edición y llamar a la mutación de actualizar', async () => {
    render(<TiposTurnoTab />);
    await user.click(screen.getByRole('button', { name: /editar turno 1/i }));
    
    const form = screen.getByTestId('tipo-turno-form');
    expect(form).toBeInTheDocument();
    expect(form).toHaveAttribute('data-initial-data', 'true');

    await user.click(screen.getByRole('button', { name: /submit form/i }));
    expect(mockActualizarMutation).toHaveBeenCalledWith({ id: 1, data: { nombre: 'Nuevo Turno' } }, expect.anything());
  });

  it('debería llamar a la mutación de eliminar', async () => {
    vi.spyOn(window, 'confirm').mockImplementation(() => true);
    render(<TiposTurnoTab />);
    await user.click(screen.getByRole('button', { name: /eliminar turno 1/i }));
    expect(mockEliminarMutation).toHaveBeenCalledWith(1);
  });
});
