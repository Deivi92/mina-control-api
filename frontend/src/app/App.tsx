import { Outlet } from 'react-router-dom';
import { AuthProvider } from '../auth/hooks/useAuth'; // Importar AuthProvider

function App() {
  return (
    <AuthProvider> {/* Envolver la app con AuthProvider DENTRO del Router */}
      {/* En el futuro, aquí podríamos tener un Navbar o un Layout principal */}
      <main>
        {/* Outlet es el componente de react-router que renderiza la ruta hija que corresponda. */}
        <Outlet />
      </main>
    </AuthProvider>
  );
}

export default App;