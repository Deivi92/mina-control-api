import React from 'react';
import {
  Box,
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from '@mui/material';
import type { ProduccionFilters } from '../types';

interface RegistroProduccionFilterProps {
  empleados: { id: number; nombres: string; apellidos: string; numeroIdentificacion: string }[];
  tiposTurno: { id: number; nombre: string; descripcion: string; activo: boolean }[];
  onFilter: (filters: ProduccionFilters) => void;
  initialValues?: ProduccionFilters;
}

export const RegistroProduccionFilter: React.FC<RegistroProduccionFilterProps> = ({
  empleados,
  tiposTurno,
  onFilter,
  initialValues = {},
}) => {
  const [filters, setFilters] = React.useState<ProduccionFilters>(initialValues);

  const handleChange = (field: keyof ProduccionFilters, value: any) => {
    setFilters(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleAplicarFiltros = () => {
    onFilter(filters);
  };

  const handleLimpiarFiltros = () => {
    const emptyFilters: ProduccionFilters = {
      empleadoId: undefined,
      fechaInicio: undefined,
      fechaFin: undefined,
      tipoTurnoId: undefined
    };
    setFilters(emptyFilters);
    onFilter(emptyFilters);
  };

  return (
    <Box 
      component="form" 
      sx={{ 
        display: 'flex', 
        gap: 2, 
        mb: 3, 
        p: 2, 
        border: '1px solid #e0e0e0', 
        borderRadius: 1 
      }}
      data-testid="registro-produccion-filter"
    >
      <FormControl fullWidth>
        <InputLabel id="empleado-select-label">Empleado</InputLabel>
        <Select
          labelId="empleado-select-label"
          value={filters.empleadoId || ''}
          label="Empleado"
          onChange={(e) => handleChange('empleadoId', e.target.value ? Number(e.target.value) : undefined)}
        >
          <MenuItem value="">
            <em>Todos</em>
          </MenuItem>
          {empleados.map((empleado) => (
            <MenuItem key={empleado.id} value={empleado.id}>
              {empleado.nombres} {empleado.apellidos}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      <TextField
        label="Fecha Inicio"
        type="date"
        value={filters.fechaInicio || ''}
        onChange={(e) => handleChange('fechaInicio', e.target.value || undefined)}
        InputLabelProps={{ shrink: true }}
      />

      <TextField
        label="Fecha Fin"
        type="date"
        value={filters.fechaFin || ''}
        onChange={(e) => handleChange('fechaFin', e.target.value || undefined)}
        InputLabelProps={{ shrink: true }}
      />

      <FormControl fullWidth>
        <InputLabel id="turno-select-label">Turno</InputLabel>
        <Select
          labelId="turno-select-label"
          value={filters.tipoTurnoId || ''}
          label="Turno"
          onChange={(e) => handleChange('tipoTurnoId', e.target.value ? Number(e.target.value) : undefined)}
        >
          <MenuItem value="">
            <em>Todos</em>
          </MenuItem>
          {tiposTurno.map((turno) => (
            <MenuItem key={turno.id} value={turno.id}>
              {turno.nombre}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      <Box sx={{ display: 'flex', gap: 1, alignItems: 'flex-end' }}>
        <Button 
          variant="contained" 
          onClick={handleAplicarFiltros}
          sx={{ height: '56px' }}
        >
          Aplicar Filtros
        </Button>
        <Button 
          variant="outlined" 
          onClick={handleLimpiarFiltros}
          sx={{ height: '56px' }}
        >
          Limpiar Filtros
        </Button>
      </Box>
    </Box>
  );
};