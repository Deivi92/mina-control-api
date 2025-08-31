import { createTheme } from '@mui/material/styles';

// Paleta de colores inspirada en "MinaControl Pro"
const palette = {
  primary: {
    main: '#0D47A1', // Un azul oscuro, profesional y corporativo
    light: '#4274D3',
    dark: '#002171',
  },
  secondary: {
    main: '#FFAB00', // Un ámbar/dorado para acentos, como el oro
    light: '#FFDD4B',
    dark: '#C67C00',
  },
  background: {
    default: '#F5F5F5', // Un gris muy claro para el fondo general
    paper: '#FFFFFF',   // El fondo de los "Cards" y superficies
  },
};

// Creación del tema
export const theme = createTheme({
  palette,
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h1: {
      // Aquí aplicamos el gradiente que pediste
      fontSize: '2.5rem',
      fontWeight: 700,
      background: `linear-gradient(45deg, ${palette.primary.main} 30%, ${palette.secondary.main} 90%)`,
      WebkitBackgroundClip: 'text',
      WebkitTextFillColor: 'transparent',
    },
    h2: {
      fontSize: '2rem',
      fontWeight: 600,
    },
  },
  shape: {
    borderRadius: 8, // Bordes sutilmente redondeados
  },
  components: {
    // Sobrescribimos estilos por defecto de componentes específicos
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none', // Para que los botones no estén en MAYÚSCULAS
          fontWeight: 600,
        },
      },
    },
  },
});
