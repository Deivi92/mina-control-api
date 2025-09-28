
import { useState } from 'react';
import { Box, Tabs, Tab, Paper, Typography } from '@mui/material';
import { TiposTurnoTab } from '../domains/turnos/components/TiposTurnoTab';
import { AsistenciaTab } from '../domains/turnos/components/AsistenciaTab';

function TabPanel(props: { children?: React.ReactNode; index: number; value: number }) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ p: 3 }}>
          {children}
        </Box>
      )}
    </div>
  );
}

export const TurnosPage = () => {
  const [value, setValue] = useState(0);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  return (
    <Paper elevation={3} sx={{ p: 2, mt: 2 }}>
        <Typography variant="h4" sx={{ mb: 2 }}>Gestión de Turnos</Typography>
        <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
            <Tabs value={value} onChange={handleChange} aria-label="pestañas de gestión de turnos">
                <Tab label="Administrar Tipos de Turno" id="simple-tab-0" />
                <Tab label="Control de Asistencia" id="simple-tab-1" />
            </Tabs>
        </Box>
        <TabPanel value={value} index={0}>
            <TiposTurnoTab />
        </TabPanel>
        <TabPanel value={value} index={1}>
            <AsistenciaTab />
        </TabPanel>
    </Paper>
  );
};
