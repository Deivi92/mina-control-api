import { Outlet } from 'react-router-dom';

function App() {
  return (
    <>
      {/* En el futuro, aquí podríamos tener un Navbar o un Layout principal */}
      <main>
        {/* Outlet es el componente de react-router que renderiza la ruta hija que corresponda. */}
        <Outlet />
      </main>
    </>
  );
}

export default App;