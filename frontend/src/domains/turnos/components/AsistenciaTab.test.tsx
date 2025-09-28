
import { render, screen, fireEvent } from '@testing-library/react';
import { AsistenciaTab } from './AsistenciaTab';
import { useAsistencia } from '../hooks/useAsistencia';
import { useEmpleados } from '../../empleado/hooks/useEmpleados'; // Asumiendo un hook de empleados
import { useTiposTurno } from '../hooks/useTiposTurno';

// Mockear todos los hooks de datos
vi.mock('../hooks/useAsistencia');
vi.mock('../../empleado/hooks/useEmpleados');
vi.mock('../hooks/useTiposTurno');

const mockedUseAsistencia = vi.mocked(useAsistencia);
const mockedUseEmpleados = vi.mocked(useEmpleados);
const mockedUseTiposTurno = vi.mocked(useTiposTurno);

// Mockear componentes hijos para aislar el contenedor
vi.mock('./AsignacionesDia', () => ({ AsignacionesDia: (props) => <div data-testid="asignaciones-dia" {...props} /> }));
vi.mock('./AsignacionTurnoForm', () => ({ AsignacionTurnoForm: (props) => <div data-testid="asignacion-form" {...props} /> }));

describe('AsistenciaTab', () => {
  const mockAsignarMutation = vi.fn();

  beforeEach(() => {
    // Configuración base de los hooks mockeados
    mockedUseAsistencia.mockReturnValue({
      asistenciaQuery: { data: [], isLoading: false, isError: false },
      asignarEmpleadoMutation: { mutate: mockAsignarMutation } as any,
    } as any);
    mockedUseEmpleados.mockReturnValue({ empleadosQuery: { data: [], isLoading: false } } as any);
    mockedUseTiposTurno.mockReturnValue({ tiposTurnoQuery: { data: [], isLoading: false } } as any);
    vi.clearAllMocks();
  });

  it('debería renderizar los controles principales como el selector de fecha', () => {
    render(<AsistenciaTab />);
    expect(screen.getByLabelText(/fecha/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /asignar turno/i })).toBeInTheDocument();
  });

  it('debería pasar los datos de asistencia procesados a AsignacionesDia', () => {
    const mockAsistencia = [/* ... */];
    mockedUseAsistencia.mockReturnValueOnce({
        ...mockedUseAsistencia(),
        asistenciaQuery: { data: mockAsistencia, isLoading: false, isError: false },
    });

    render(<AsistenciaTab />);
    const asignacionesDia = screen.getByTestId('asignaciones-dia');
    // La prop 'asignaciones' debería ser un Map procesado
    expect(asignacionesDia.props.asignaciones).toBeInstanceOf(Map);
  });

  it('debería abrir el formulario de asignación al hacer clic en el botón', async () => {
    render(<AsistenciaTab />);
    await fireEvent.click(screen.getByRole('button', { name: /asignar turno/i }));

    const form = screen.getByTestId('asignacion-form');
    expect(form.props.open).toBe(true);
  });

  it('debería llamar a la mutación de asignar cuando el formulario hace submit', () => {
    render(<AsistenciaTab />);
    fireEvent.click(screen.getByRole('button', { name: /asignar turno/i }));

    const form = screen.getByTestId('asignacion-form');
    const onSubmit = form.props.onSubmit;
    const asignacionData = { empleadoId: 1, tipoTurnoId: 1, fecha: '2024-05-23' };
    onSubmit(asignacionData);

    expect(mockAsignarMutation).toHaveBeenCalledWith(asignacionData);
  });

  it('debería cambiar los parámetros de fecha del hook useAsistencia cuando el usuario cambia la fecha', async () => {
    render(<AsistenciaTab />);
    const datePicker = screen.getByLabelText(/fecha/i);

    await fireEvent.change(datePicker, { target: { value: '2024-10-15' } });

    // La aserción es que el hook useAsistencia fue llamado con los nuevos parámetros.
    expect(mockedUseAsistencia).toHaveBeenCalledWith(expect.objectContaining({ fechaInicio: '2024-10-15', fechaFin: '2024-10-15' }));
  });
});
