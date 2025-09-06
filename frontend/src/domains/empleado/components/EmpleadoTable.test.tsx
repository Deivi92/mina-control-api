import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { EmpleadoTable } from './EmpleadoTable';
import { Empleado } from '../types';

// Mock de los datos de empleados para las pruebas
const mockEmpleados: Empleado[] = [
  { id: 1, nombre: 'Juan', apellido: 'Pérez', email: 'juan@test.com', puesto: 'Minero', salario: 50000, estado: 'ACTIVO', fechaContratacion: '2023-01-15', fechaNacimiento: '1990-05-20' },
  { id: 2, nombre: 'Ana', apellido: 'García', email: 'ana@test.com', puesto: 'Geóloga', salario: 60000, estado: 'INACTIVO', fechaContratacion: '2022-11-10', fechaNacimiento: '1988-12-01' },
];

describe('EmpleadoTable', () => {
  const onEditMock = vi.fn();
  const onDeleteMock = vi.fn();

  it('debería mostrar un indicador de carga (spinner) cuando isLoading es true', () => {
    render(<EmpleadoTable empleados={[]} isLoading={true} isError={false} onEdit={onEditMock} onDelete={onDeleteMock} />);
    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  it('debería mostrar un mensaje de error cuando isError es true', () => {
    render(<EmpleadoTable empleados={[]} isLoading={false} isError={true} onEdit={onEditMock} onDelete={onDeleteMock} />);
    expect(screen.getByText(/Error al cargar los empleados/i)).toBeInTheDocument();
  });

  it('debería mostrar un mensaje específico si la lista de empleados está vacía y no está cargando', () => {
    render(<EmpleadoTable empleados={[]} isLoading={false} isError={false} onEdit={onEditMock} onDelete={onDeleteMock} />);
    expect(screen.getByText(/No se encontraron empleados/i)).toBeInTheDocument();
  });

  it('debería renderizar la tabla con la lista de empleados proporcionada', () => {
    render(<EmpleadoTable empleados={mockEmpleados} isLoading={false} isError={false} onEdit={onEditMock} onDelete={onDeleteMock} />);
    
    // Verificar encabezados
    expect(screen.getByText('Nombre')).toBeInTheDocument();
    expect(screen.getByText('Puesto')).toBeInTheDocument();
    expect(screen.getByText('Estado')).toBeInTheDocument();

    // Verificar contenido de las filas
    expect(screen.getByText('Juan Pérez')).toBeInTheDocument();
    expect(screen.getByText('Ana García')).toBeInTheDocument();
  });

  it('debería llamar a la función onEdit con el empleado correcto al hacer clic en el botón de editar', () => {
    render(<EmpleadoTable empleados={mockEmpleados} isLoading={false} isError={false} onEdit={onEditMock} onDelete={onDeleteMock} />);
    
    // Busca todos los botones con la etiqueta "editar"
    const editButtons = screen.getAllByLabelText(/editar/i);
    fireEvent.click(editButtons[0]); // Clic en el primer botón de editar (fila de Juan)
    expect(onEditMock).toHaveBeenCalledWith(mockEmpleados[0]);
  });

  it('debería llamar a la función onDelete con el empleado correcto al hacer clic en el botón de eliminar', () => {
    render(<EmpleadoTable empleados={mockEmpleados} isLoading={false} isError={false} onEdit={onEditMock} onDelete={onDeleteMock} />);
    
    const deleteButtons = screen.getAllByLabelText(/eliminar/i);
    fireEvent.click(deleteButtons[0]); // Clic en el primer botón de eliminar (fila de Juan)
    expect(onDeleteMock).toHaveBeenCalledWith(mockEmpleados[0]);
  });
});