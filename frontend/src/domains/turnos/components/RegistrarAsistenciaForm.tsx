
import { useState } from 'react';
import { Box, Button, Autocomplete, TextField, Paper, Typography } from '@mui/material';
import type { Empleado } from '../../empleado/types';
import type { RegistrarAsistenciaRequest } from '../types';
import { TipoRegistro } from '../types';

interface Props {
  empleados: Empleado[];
  onRegister: (data: RegistrarAsistenciaRequest) => void;
  isSubmitting: boolean;
}

export const RegistrarAsistenciaForm = ({ empleados, onRegister, isSubmitting }: Props) => {
  const [selectedEmpleado, setSelectedEmpleado] = useState<Empleado | null>(null);

  const handleRegister = (tipo: TipoRegistro) => {
    if (selectedEmpleado) {
      onRegister({ empleadoId: selectedEmpleado.id, tipo });
    }
  };

  return (
    <Paper elevation={3} sx={{ p: 2, mt: 4 }}>
        <Typography variant="h6" sx={{ mb: 2 }}>Registro Rápido de Asistencia</Typography>
        <Autocomplete
            options={empleados}
            getOptionLabel={(option) => `${option.nombres} ${option.apellidos}`}
            value={selectedEmpleado}
            onChange={(_, newValue) => setSelectedEmpleado(newValue)}
            renderInput={(params) => <TextField {...params} label="Seleccionar Empleado" />}
            sx={{ mb: 2 }}
        />
        <Box sx={{ display: 'flex', gap: 2 }}>
            <Button 
                variant="contained" 
                color="success" 
                onClick={() => handleRegister(TipoRegistro.ENTRADA)} 
                disabled={!selectedEmpleado || isSubmitting}
                fullWidth
            >
                Registrar Entrada
            </Button>
            <Button 
                variant="contained" 
                color="error" 
                onClick={() => handleRegister(TipoRegistro.SALIDA)} 
                disabled={!selectedEmpleado || isSubmitting}
                fullWidth
            >
                Registrar Salida
            </Button>
        </Box>
    </Paper>
  );
};
