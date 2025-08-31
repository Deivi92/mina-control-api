import axios from 'axios';
import { login, register, refreshToken, logout } from './auth.service';
import { LoginRequest } from '../types';

// Mockeamos el módulo completo de Axios
jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('AuthService', () => {

  // Limpiamos los mocks después de cada prueba para evitar interferencias
  afterEach(() => {
    jest.clearAllMocks();
  });

  // Pruebas para la función de LOGIN
  describe('login', () => {
    it('debería existir la función login', () => {
      expect(login).toBeDefined();
    });

    it('debería llamar a axios.post con la URL y los datos correctos', async () => {
      // TODO: Implementar esta prueba
    });

    it('debería devolver los tokens en caso de éxito', async () => {
      // TODO: Implementar esta prueba
    });

    it('debería lanzar un error si la llamada a la API falla', async () => {
      // TODO: Implementar esta prueba
    });
  });

  // Pruebas para la función de REGISTRO
  describe('register', () => {
    it('debería existir la función register', () => {
      expect(register).toBeDefined();
    });

    it('debería llamar a axios.post con la URL y los datos de registro correctos', async () => {
      // TODO: Implementar esta prueba
    });
  });

  // Pruebas para la función de REFRESH TOKEN
  describe('refreshToken', () => {
    it('debería existir la función refreshToken', () => {
      expect(refreshToken).toBeDefined();
    });

    it('debería llamar a axios.post para refrescar el token', async () => {
      // TODO: Implementar esta prueba
    });
  });

  // Pruebas para la función de LOGOUT
  describe('logout', () => {
    it('debería existir la función logout', () => {
      expect(logout).toBeDefined();
    });

    it('debería llamar a axios.post para hacer logout', async () => {
      // TODO: Implementar esta prueba
    });
  });

});