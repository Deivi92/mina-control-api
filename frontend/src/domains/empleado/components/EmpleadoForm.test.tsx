import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { EmpleadoForm } from './EmpleadoForm';
import { Empleado } from '../types';

// Mock de un empleado para el modo edición
const mockEmpleado: Empleado = {
  id: 1,
  nombre: 'Juan',
  apellido: 'Pérez',
  email: 'juan@test.com',
  puesto: 'Minero',
  salario: 50000,
  estado: 'ACTIVO',
  fechaContratacion: '2023-01-15T00:00:00',
  fechaNacimiento: '1990-05-20T00:00:00',
  numeroIdentificacion: '12345'
};

describe('EmpleadoForm', () => {
  const onSubmitMock = vi.fn();
  const onCancelMock = vi.fn();

  beforeEach(() => {
    onSubmitMock.mockClear();
    onCancelMock.mockClear();
  });

  it('debería renderizar los campos del formulario vacíos en modo creación', () => {
    render(<EmpleadoForm onSubmit={onSubmitMock} onCancel={onCancelMock} />);
    
    expect(screen.getByLabelText(/Nombre/i)).toHaveValue('');
    expect(screen.getByLabelText(/Apellido/i)).toHaveValue('');
    expect(screen.getByLabelText(/Email/i)).toHaveValue('');
  });

  it('debería mostrar los valores iniciales del empleado en modo edición', () => {
    render(<EmpleadoForm initialData={mockEmpleado} onSubmit={onSubmitMock} onCancel={onCancelMock} />);
    
    expect(screen.getByLabelText(/Nombre/i)).toHaveValue('Juan');
    expect(screen.getByLabelText(/Apellido/i)).toHaveValue('Pérez');
    expect(screen.getByLabelText(/Email/i)).toHaveValue('juan@test.com');
    // El valor de la fecha se formatea a YYYY-MM-DD para el input type="date"
    expect(screen.getByLabelText(/Fecha de Nacimiento/i)).toHaveValue('1990-05-20');
  });

  it('debería actualizar el valor de un campo cuando el usuario escribe en él', () => {
    render(<EmpleadoForm onSubmit={onSubmitMock} onCancel={onCancelMock} />);
    
    const nombreInput = screen.getByLabelText(/Nombre/i);
    fireEvent.change(nombreInput, { target: { value: 'Carlos' } });
    
    expect(nombreInput).toHaveValue('Carlos');
  });

  it('debería llamar a la función onSubmit con los datos del formulario al hacer clic en guardar', () => {
    render(<EmpleadoForm onSubmit={onSubmitMock} onCancel={onCancelMock} />);
    
    // Simular que el usuario rellena el formulario
    fireEvent.change(screen.getByLabelText(/Nombre/i), { target: { value: 'Nuevo' } });
    fireEvent.change(screen.getByLabelText(/Apellido/i), { target: { value: 'Empleado' } });
    fireEvent.change(screen.getByLabelText(/Email/i), { target: { value: 'test@test.com' } });
    fireEvent.change(screen.getByLabelText(/Puesto/i), { target: { value: 'Tester' } });
    fireEvent.change(screen.getByLabelText(/Salario/i), { target: { value: '1000' } });
    fireEvent.change(screen.getByLabelText(/Número de Identificación/i), { target: { value: '123' } });
    fireEvent.change(screen.getByLabelText(/Fecha de Nacimiento/i), { target: { value: '2000-01-01' } });

    fireEvent.click(screen.getByRole('button', { name: /Guardar/i }));

    // Verificar que onSubmit fue llamado con los datos correctos
    expect(onSubmitMock).toHaveBeenCalledWith({
      nombre: 'Nuevo',
      apellido: 'Empleado',
      email: 'test@test.com',
      puesto: 'Tester',
      salario: 1000,
      numeroIdentificacion: '123',
      fechaNacimiento: '2000-01-01'
    });
  }, 15000);

  it('debería deshabilitar el botón de guardar mientras se está enviando (isSubmitting es true)', () => {
    render(<EmpleadoForm onSubmit={onSubmitMock} onCancel={onCancelMock} isSubmitting={true} />);
    expect(screen.getByRole('button', { name: /Guardar/i })).toBeDisabled();
  });

  it('debería llamar a la función onCancel cuando se hace clic en el botón de cancelar', () => {
    render(<EmpleadoForm onSubmit={onSubmitMock} onCancel={onCancelMock} />);
    fireEvent.click(screen.getByRole('button', { name: /Cancelar/i }));
    expect(onCancelMock).toHaveBeenCalledTimes(1);
  });
});