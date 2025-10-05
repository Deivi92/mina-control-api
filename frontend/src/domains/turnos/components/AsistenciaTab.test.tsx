
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

// Mockear componentes hijos de forma más robusta para las pruebas
vi.mock('./AsignacionesDia', () => ({ 
  AsignacionesDia: (props) => <div data-testid="asignaciones-dia" data-loading={props.isLoading} data-asignaciones-size={props.asignaciones.size} /> 
}));

// Este mock ahora incluye un botón para simular el submit desde el test
vi.mock('./AsignacionTurnoForm', () => ({ 
  AsignacionTurnoForm: (props) => {
    if (!props.open) return null;
    return (
      <div data-testid="asignacion-form">
        <button onClick={() => props.onSubmit({ empleadoId: 1, tipoTurnoId: 1, fechaInicio: '2024-05-23', fechaFin: '2024-05-23' })}>
          Simular Submit
        </button>
      </div>
    );
  }
}));

describe('AsistenciaTab', () => {
  const mockAsignarMutation = vi.fn();
  const mockRegistrarMutation = vi.fn();
  const mockGestionarExcepcionMutation = vi.fn();

  beforeEach(() => {
    // Configuración base de los hooks mockeados
    mockedUseAsistencia.mockReturnValue({
      asistenciaQuery: { data: [], isLoading: false, isError: false },
      asignarEmpleadoMutation: { mutate: mockAsignarMutation } as any,
      registrarAsistenciaMutation: { mutate: mockRegistrarMutation } as any,
      gestionarExcepcionMutation: { mutate: mockGestionarExcepcionMutation } as any,
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

  it('debería pasar un Map de asignaciones procesado a AsignacionesDia', () => {
    const mockEmpleado = {
      id: 1,
      nombres: 'Juan',
      apellidos: 'Perez',
      numeroIdentificacion: '12345',
      email: 'juan@test.com',
      telefono: '555-1234',
      cargo: 'Minero',
      fechaContratacion: '2023-01-01',
      salarioBase: 1000,
      rolSistema: 'EMPLEADO',
      estado: 'ACTIVO',
    };
    // Actualizar la estructura de datos para reflejar que ahora el backend devuelve empleadoId
    const mockAsistenciaData = [{ id: 101, empleadoId: 1, estado: 'ASISTIO' }];
    
    // Asegurarse de que useEmpleados devuelve el empleado correspondiente
    mockedUseEmpleados.mockReturnValueOnce({ 
      empleadosQuery: { data: [mockEmpleado], isLoading: false } 
    } as any);

    mockedUseAsistencia.mockReturnValueOnce({
      ...mockedUseAsistencia(),
      asistenciaQuery: { data: mockAsistenciaData, isLoading: false, isError: false },
    } as any);

    render(<AsistenciaTab />);
    const asignacionesDia = screen.getByTestId('asignaciones-dia');
    
    expect(asignacionesDia).toHaveAttribute('data-asignaciones-size', '1');
  });

  it('debería abrir el formulario de asignación al hacer clic en el botón', async () => {
    render(<AsistenciaTab />);
    
    // El formulario no debe estar en el DOM inicialmente
    expect(screen.queryByTestId('asignacion-form')).not.toBeInTheDocument();

    await fireEvent.click(screen.getByRole('button', { name: /asignar turno/i }));

    // Ahora el formulario (o su mock) debe ser visible
    expect(screen.getByTestId('asignacion-form')).toBeInTheDocument();
  });

  it('debería llamar a la mutación de asignar cuando el formulario hace submit', async () => {
    render(<AsistenciaTab />);
    await fireEvent.click(screen.getByRole('button', { name: /asignar turno/i }));

    // El mock del formulario ahora contiene un botón para simular el submit
    const simularSubmitBtn = screen.getByRole('button', { name: /simular submit/i });
    await fireEvent.click(simularSubmitBtn);

    expect(mockAsignarMutation).toHaveBeenCalledWith(
      expect.objectContaining({ empleadoId: 1, tipoTurnoId: 1, fechaInicio: '2024-05-23', fechaFin: '2024-05-23' }),
      expect.objectContaining({ onSuccess: expect.any(Function) })
    );
  });

  it('debería cambiar los parámetros de fecha del hook useAsistencia cuando el usuario cambia la fecha', async () => {
    render(<AsistenciaTab />);
    const datePicker = screen.getByLabelText(/fecha/i);

    await fireEvent.change(datePicker, { target: { value: '2024-10-15' } });

    // La aserción es que el hook useAsistencia fue llamado con los nuevos parámetros.
    expect(mockedUseAsistencia).toHaveBeenCalledWith(expect.objectContaining({ fechaInicio: '2024-10-15', fechaFin: '2024-10-15' }));
  });
});
