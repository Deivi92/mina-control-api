
import { render, screen, fireEvent } from '@testing-library/react';
import { TipoTurnoForm } from './TipoTurnoForm';
import { TipoTurnoRequest } from '../types';

describe('TipoTurnoForm', () => {
  const mockOnSubmit = vi.fn();
  const mockOnClose = vi.fn();

  beforeEach(() => {
    mockOnSubmit.mockClear();
    mockOnClose.mockClear();
  });

  it('debería renderizar los campos del formulario vacíos para crear', () => {
    render(<TipoTurnoForm open={true} onClose={mockOnClose} onSubmit={mockOnSubmit} />);

    expect(screen.getByLabelText(/nombre/i)).toHaveValue('');
    expect(screen.getByLabelText(/hora de inicio/i)).toHaveValue('');
    expect(screen.getByLabelText(/hora de fin/i)).toHaveValue('');
  });

  it('debería renderizar el formulario con valores iniciales para editar', () => {
    const initialData: TipoTurnoRequest = {
      nombre: 'Turno Existente',
      horaInicio: '09:00',
      horaFin: '17:00',
      color: '#FF0000'
    };
    render(<TipoTurnoForm open={true} onClose={mockOnClose} onSubmit={mockOnSubmit} initialData={initialData} />);

    expect(screen.getByLabelText(/nombre/i)).toHaveValue('Turno Existente');
    expect(screen.getByLabelText(/hora de inicio/i)).toHaveValue('09:00');
    expect(screen.getByLabelText(/hora de fin/i)).toHaveValue('17:00');
  });

  it('debería permitir al usuario rellenar el formulario', async () => {
    render(<TipoTurnoForm open={true} onClose={mockOnClose} onSubmit={mockOnSubmit} />);

    const nombreInput = screen.getByLabelText(/nombre/i);
    await fireEvent.change(nombreInput, { target: { value: 'Nuevo Turno de Tarde' } });
    expect(nombreInput).toHaveValue('Nuevo Turno de Tarde');
  });

  it('debería llamar a onSubmit con los datos del formulario cuando se hace clic en guardar', async () => {
    render(<TipoTurnoForm open={true} onClose={mockOnClose} onSubmit={mockOnSubmit} />);
    
    await fireEvent.change(screen.getByLabelText(/nombre/i), { target: { value: 'Turno de Noche' } });
    await fireEvent.change(screen.getByLabelText(/hora de inicio/i), { target: { value: '22:00' } });
    await fireEvent.change(screen.getByLabelText(/hora de fin/i), { target: { value: '06:00' } });
    // Suponiendo que el color tiene un valor por defecto o se puede interactuar con él

    await fireEvent.click(screen.getByRole('button', { name: /guardar/i }));

    expect(mockOnSubmit).toHaveBeenCalledWith({
      nombre: 'Turno de Noche',
      horaInicio: '22:00',
      horaFin: '06:00',
      color: expect.any(String), // El color puede ser más complejo de testear dependiendo del componente
    });
  });

  it('debería mostrar errores de validación si los campos están vacíos al enviar', async () => {
    // Esta prueba depende de la librería de manejo de formularios (ej. React Hook Form)
    // y cómo muestra los errores. Se asume que los errores se muestran en el DOM.
    render(<TipoTurnoForm open={true} onClose={mockOnClose} onSubmit={mockOnSubmit} />);

    await fireEvent.click(screen.getByRole('button', { name: /guardar/i }));

    // Ejemplo de aserción, puede variar
    expect(await screen.findByText(/el nombre es requerido/i)).toBeInTheDocument();
  });

  it('debería llamar a onClose cuando se hace clic en el botón de cancelar', async () => {
    render(<TipoTurnoForm open={true} onClose={mockOnClose} onSubmit={mockOnSubmit} />);

    await fireEvent.click(screen.getByRole('button', { name: /cancelar/i }));

    expect(mockOnClose).toHaveBeenCalledTimes(1);
  });
});
