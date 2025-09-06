import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { DeleteEmpleadoDialog } from './DeleteEmpleadoDialog';

describe('DeleteEmpleadoDialog', () => {
  // Mock de las funciones que se pasarán como props
  const onConfirmMock = vi.fn();
  const onCancelMock = vi.fn();

  beforeEach(() => {
    // Limpiar los mocks antes de cada prueba
    onConfirmMock.mockClear();
    onCancelMock.mockClear();
  });

  it('no debería ser visible si el prop open es false', () => {
    render(
      <DeleteEmpleadoDialog
        open={false}
        onConfirm={onConfirmMock}
        onCancel={onCancelMock}
        empleadoNombre="Juan Pérez"
      />
    );
    // `queryByRole` es útil para verificar que un elemento NO está en el DOM
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
  });

  it('debería renderizar el diálogo con el título y nombre del empleado correctos cuando open es true', () => {
    render(
      <DeleteEmpleadoDialog
        open={true}
        onConfirm={onConfirmMock}
        onCancel={onCancelMock}
        empleadoNombre="Juan Pérez"
      />
    );
    expect(screen.getByRole('dialog')).toBeInTheDocument();
    expect(screen.getByText('Confirmar Eliminación')).toBeInTheDocument();
    // Verificamos que el nombre del empleado se muestra en el contenido
    expect(screen.getByText(/¿Estás seguro de que deseas eliminar al empleado Juan Pérez?/)).toBeInTheDocument();
  });

  it('debería llamar a onConfirm cuando el usuario hace clic en el botón de Eliminar', () => {
    render(
      <DeleteEmpleadoDialog
        open={true}
        onConfirm={onConfirmMock}
        onCancel={onCancelMock}
        empleadoNombre="Juan Pérez"
      />
    );
    fireEvent.click(screen.getByRole('button', { name: /Eliminar/i }));
    expect(onConfirmMock).toHaveBeenCalledTimes(1);
  });

  it('debería llamar a onCancel cuando el usuario hace clic en el botón de Cancelar', () => {
    render(
      <DeleteEmpleadoDialog
        open={true}
        onConfirm={onConfirmMock}
        onCancel={onCancelMock}
        empleadoNombre="Juan Pérez"
      />
    );
    fireEvent.click(screen.getByRole('button', { name: /Cancelar/i }));
    expect(onCancelMock).toHaveBeenCalledTimes(1);
  });
});