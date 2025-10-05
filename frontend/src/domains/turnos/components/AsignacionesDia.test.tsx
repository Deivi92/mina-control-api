
import { render, screen } from '@testing-library/react';
import { AsignacionesDia } from './AsignacionesDia';
import { RegistroAsistencia, EstadoAsistencia } from '../types';
import { Empleado } from '../../empleado/types';

// Mock de datos
const mockEmpleados: Empleado[] = [
    { id: 1, nombres: 'Carlos', apellidos: 'Rojas' },
    { id: 2, nombres: 'Ana', apellidos: 'Gomez' },
];

const mockAsistencias: RegistroAsistencia[] = [
    { id: 101, asignacionTurnoId: 1, horaEntrada: null, horaSalida: null, horasTrabajadas: null, estado: EstadoAsistencia.PENDIENTE, observaciones: null },
    { id: 102, asignacionTurnoId: 2, horaEntrada: '2024-05-23T08:05:00', horaSalida: null, horasTrabajadas: null, estado: EstadoAsistencia.ASISTIO, observaciones: null },
];

// Mock de un mapa de asignaciones que el componente podría recibir
const mockAsignaciones = new Map<number, { empleado: Empleado, asistencia: RegistroAsistencia }>();
mockAsignaciones.set(1, { empleado: mockEmpleados[0], asistencia: mockAsistencias[0] });
mockAsignaciones.set(2, { empleado: mockEmpleados[1], asistencia: mockAsistencias[1] });

describe('AsignacionesDia', () => {

  it('debería renderizar la lista de asignaciones del día', () => {
    render(<AsignacionesDia asignaciones={mockAsignaciones} isLoading={false} />);

    // Verificar que los nombres de los empleados están en el documento
    expect(screen.getByText(/Carlos Rojas/i)).toBeInTheDocument();
    expect(screen.getByText(/Ana Gomez/i)).toBeInTheDocument();

    // Verificar que los estados de asistencia se muestran
    expect(screen.getByText(EstadoAsistencia.PENDIENTE)).toBeInTheDocument();
    expect(screen.getByText(EstadoAsistencia.ASISTIO)).toBeInTheDocument();
  });

  it('debería mostrar un indicador de carga cuando isLoading es true', () => {
    render(<AsignacionesDia asignaciones={new Map()} isLoading={true} />);

    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  it('debería mostrar un mensaje cuando no hay asignaciones para el día', () => {
    render(<AsignacionesDia asignaciones={new Map()} isLoading={false} />);

    expect(screen.getByText(/no hay empleados asignados para esta fecha/i)).toBeInTheDocument();
  });

});
