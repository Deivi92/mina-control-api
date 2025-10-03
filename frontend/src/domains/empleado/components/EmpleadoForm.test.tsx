import React from 'react';
import { render, screen, fireEvent, act, waitFor } from '@testing-library/react';
import { EmpleadoForm } from './EmpleadoForm';
import { Empleado, EmpleadoRequest } from '../types';

// Mock de un empleado para el modo edición
const mockEmpleado: Empleado = {
  id: 1,
  nombres: 'Juan',
  apellidos: 'Pérez',
  email: 'juan@test.com',
  telefono: '123456789',
  cargo: 'Minero',
  fechaContratacion: '2023-01-15T00:00:00',
  salarioBase: 50000,
  rolSistema: 'EMPLEADO',
  estado: 'ACTIVO',
  numeroIdentificacion: 12345
};

describe('EmpleadoForm', () => {
  const onSubmitMock = vi.fn();
  const onCancelMock = vi.fn();

  beforeEach(() => {
    onSubmitMock.mockClear();
    onCancelMock.mockClear();
  });

  it('debería renderizar los campos del formulario vacíos en modo creación', async () => {
    render(<EmpleadoForm onSubmit={onSubmitMock} onCancel={onCancelMock} />);
    
    expect(await screen.findByLabelText(/Nombres/i)).toHaveValue('');
    expect(await screen.findByLabelText(/Apellidos/i)).toHaveValue('');
    expect(await screen.findByLabelText(/Correo Electrónico/i)).toHaveValue('');
  }, 10000);

  it('debería mostrar los valores iniciales del empleado en modo edición', () => {
    render(<EmpleadoForm initialData={mockEmpleado} onSubmit={onSubmitMock} onCancel={onCancelMock} />);
    
    expect(screen.getByLabelText(/Nombres/i)).toHaveValue('Juan');
    expect(screen.getByLabelText(/Apellidos/i)).toHaveValue('Pérez');
    expect(screen.getByLabelText(/Correo Electrónico/i)).toHaveValue('juan@test.com');
    expect(screen.getByLabelText(/Cargo/i)).toHaveValue('Minero');
    expect(screen.getByLabelText(/Fecha de Contratación/i)).toHaveValue('2023-01-15');
    expect(screen.getByLabelText(/Teléfono/i)).toHaveValue('123456789');
  }, 10000);

  it('debería actualizar el valor de un campo cuando el usuario escribe en él', async () => {
    render(<EmpleadoForm onSubmit={onSubmitMock} onCancel={onCancelMock} />);
    
    const nombreInput = screen.getByLabelText(/Nombres/i);
    await act(async () => {
      fireEvent.change(nombreInput, { target: { value: 'Carlos' } });
    });
    
    expect(nombreInput).toHaveValue('Carlos');
  }, 10000);

  it('debería llamar a la función onSubmit con los datos del formulario al hacer clic en guardar', async () => {
    render(<EmpleadoForm onSubmit={onSubmitMock} onCancel={onCancelMock} />);
    
    // Simular que el usuario rellena el formulario
    const nombresInput = await screen.findByLabelText(/Nombres/i);
    await act(async () => {
      fireEvent.change(nombresInput, { target: { value: 'Nuevo' } });
    });
    
    await act(async () => {
      fireEvent.change(screen.getByLabelText(/Apellidos/i), { target: { value: 'Empleado' } });
    });
    await act(async () => {
      fireEvent.change(screen.getByLabelText(/Correo Electrónico/i), { target: { value: 'test@test.com' } });
    });
    await act(async () => {
      fireEvent.change(screen.getByLabelText(/Cargo/i), { target: { value: 'Tester' } });
    });
    await act(async () => {
      fireEvent.change(screen.getByLabelText(/Salario Base/i), { target: { value: '1000' } });
    });
    await act(async () => {
      fireEvent.change(screen.getByLabelText(/Número de Identificación/i), { target: { value: '12345678' } });
    });
    await act(async () => {
      fireEvent.change(screen.getByLabelText(/Fecha de Contratación/i), { target: { value: '2024-01-01' } });
    });
    await act(async () => {
      fireEvent.change(screen.getByLabelText(/Teléfono/i), { target: { value: '123456789' } });
    });
    
    // Seleccionar el rol
    const rolSelect = screen.getByLabelText(/Rol en el Sistema/i);
    await act(async () => {
      fireEvent.mouseDown(rolSelect);
    });
    
    const empleadoOption = screen.getByRole('option', { name: /Empleado/i });
    await act(async () => {
      fireEvent.click(empleadoOption);
    });
    
    // Esperar a que el botón esté habilitado (esperando validación)
    await waitFor(() => {
      expect(screen.getByRole('button', { name: /Guardar/i })).not.toBeDisabled();
    });
    
    await act(async () => {
      fireEvent.click(screen.getByRole('button', { name: /Guardar/i }));
    });
    
    // Verificar que onSubmit fue llamado
    await waitFor(() => {
      expect(onSubmitMock).toHaveBeenCalled();
    });
    
    // Verificar que onSubmit fue llamado con los datos correctos
    expect(onSubmitMock).toHaveBeenCalledWith({
      nombres: 'Nuevo',
      apellidos: 'Empleado',
      email: 'test@test.com',
      cargo: 'Tester',
      salarioBase: 1000,
      numeroIdentificacion: '12345678', // El backend espera string
      fechaContratacion: '2024-01-01',
      telefono: '123456789',
      rolSistema: 'EMPLEADO'
    } as EmpleadoRequest);
  }, 20000);

  it('debería deshabilitar el botón de guardar mientras se está enviando (isSubmitting es true)', async () => {
    render(<EmpleadoForm onSubmit={onSubmitMock} onCancel={onCancelMock} isSubmitting={true} />);
    expect(await screen.findByRole('button', { name: /Guardar/i })).toBeDisabled();
  }, 10000);

  it('debería llamar a la función onCancel cuando se hace clic en el botón de cancelar', () => {
    render(<EmpleadoForm onSubmit={onSubmitMock} onCancel={onCancelMock} />);
    fireEvent.click(screen.getByRole('button', { name: /Cancelar/i }));
    expect(onCancelMock).toHaveBeenCalledTimes(1);
  });
});