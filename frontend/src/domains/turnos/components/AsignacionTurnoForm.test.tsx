
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { AsignacionTurnoForm } from './AsignacionTurnoForm';
import { Empleado } from '../../empleado/types';
import { TipoTurno } from '../types';

describe('AsignacionTurnoForm', () => {
  const mockOnSubmit = vi.fn();
  const mockOnClose = vi.fn();

  const empleados: Empleado[] = [
    { id: 1, nombres: 'Juan', apellidos: 'Perez', numeroIdentificacion: '1', email: 'a@a.com', cargo: 'a', fechaContratacion: 'a', salarioBase: 1, rolSistema: 'EMPLEADO', estado: 'ACTIVO' },
  ];
  const tiposTurno: TipoTurno[] = [
    { id: 1, nombre: 'Turno Diurno', horaInicio: '08:00', horaFin: '16:00', color: '#FFF' },
  ];

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('debería renderizar los campos del formulario', () => {
    render(
      <AsignacionTurnoForm
        open={true}
        onClose={mockOnClose}
        onSubmit={mockOnSubmit}
        empleados={empleados}
        tiposTurno={tiposTurno}
        isSubmitting={false}
      />
    );

    expect(screen.getByRole('combobox', { name: /empleado/i })).toBeInTheDocument();
    expect(screen.getByRole('combobox', { name: /tipo de turno/i })).toBeInTheDocument();
    expect(screen.getByLabelText(/fecha inicio/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/fecha fin/i)).toBeInTheDocument();
  });

  it('debería llamar a onSubmit con los datos seleccionados', async () => {
    const user = userEvent.setup();
    render(
        <AsignacionTurnoForm
          open={true}
          onClose={mockOnClose}
          onSubmit={mockOnSubmit}
          empleados={empleados}
          tiposTurno={tiposTurno}
          isSubmitting={false}
        />
      );

    // Seleccionar empleado
    await user.click(screen.getByRole('combobox', { name: /empleado/i }));
    await user.click(await screen.findByText('Juan Perez'));

    // Seleccionar tipo de turno
    await user.click(screen.getByRole('combobox', { name: /tipo de turno/i }));
    await user.click(await screen.findByText('Turno Diurno'));

    // Ingresar fechas
    const fechaInicioInput = screen.getByLabelText(/fecha inicio/i);
    await user.clear(fechaInicioInput);
    await user.type(fechaInicioInput, '2024-05-23');
    
    const fechaFinInput = screen.getByLabelText(/fecha fin/i);
    await user.clear(fechaFinInput);
    await user.type(fechaFinInput, '2024-05-25');

    // Enviar formulario
    await user.click(screen.getByRole('button', { name: /asignar/i }));

    await waitFor(() => {
      expect(mockOnSubmit).toHaveBeenCalledTimes(1);
    });

    expect(mockOnSubmit).toHaveBeenCalledWith(
      expect.objectContaining({
        empleadoId: 1,
        tipoTurnoId: 1,
        fechaInicio: '2024-05-23',
        fechaFin: '2024-05-25',
      }),
      expect.anything() // handleSubmit de RHF pasa el evento como segundo argumento
    );
  });

  it('debería llamar a onClose al hacer clic en cancelar', async () => {
    render(
        <AsignacionTurnoForm
          open={true}
          onClose={mockOnClose}
          onSubmit={mockOnSubmit}
          empleados={empleados}
          tiposTurno={tiposTurno}
          isSubmitting={false}
        />
      );

      await userEvent.click(screen.getByRole('button', { name: /cancelar/i }));
      expect(mockOnClose).toHaveBeenCalledTimes(1);
  });

});
