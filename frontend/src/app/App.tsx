import React from 'react';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { theme } from './styles/theme';

function App() {
  return (
    <ThemeProvider theme={theme}>
      {/* CssBaseline es un componente de MUI que resetea los estilos del 
          navegador para que sean consistentes y aplica el color de fondo 
          de nuestro tema. */}
      <CssBaseline />
      <div>
        <h1>MinaControl Pro - Frontend</h1>
        <p>El proyecto ha sido inicializado correctamente.</p>
      </div>
    </ThemeProvider>
  );
}

export default App;
