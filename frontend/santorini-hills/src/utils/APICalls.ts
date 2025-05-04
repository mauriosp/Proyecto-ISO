import axios from "axios";
import { User, userAdapter, userPresenter, UserGet } from "../models/user";
import { Advertisement } from "../models/advertisement";

export const register = async (user: User) => {
  try {
    const userPost = userPresenter(user);
    console.log(userPost);
    const response = await axios.post("/auth/register", userPost);
    return response.data;
  } catch (error) {
    return error;
  }
};

export const login = async (email: string, password: string): Promise<User | Error> => {
  try {
    const response = await axios.post("/auth/login", { email, password });
    
    // Verificar que la respuesta es exitosa y contiene los datos del usuario
    if (response.data && response.data.usuario) {
      // Adaptar el usuario recibido del backend al formato del frontend
      const adaptedUser = userAdapter(response.data.usuario as UserGet);
      return adaptedUser;
    }
    
    throw new Error("Formato de respuesta inválido");
  } catch (error) {
    if (axios.isAxiosError(error)) {
      if (error.response) {
        // El servidor respondió con un código de estado fuera del rango de 2xx
        throw new Error(error.response.data.message || "Error en la autenticación");
      } else if (error.request) {
        // La petición fue hecha pero no se recibió respuesta
        throw new Error("No se recibió respuesta del servidor");
      } else {
        // Error al configurar la petición
        throw new Error("Error al realizar la petición");
      }
    }
    throw error; // Otro tipo de error
  }
}

export const publishAdvertisement = async (advertisement: Advertisement) => {
  try {
    const response = await axios.post("/advertisement", advertisement);
    console.log("Response from publishAdvertisement:", response.data);
    return response.data;
  } catch (error) {
    console.error("Error publishing advertisement:", error);
    throw error;
  }
}

export const updateAdvertisement = async (id: string | number, advertisement: Advertisement) => {
  try {
    const response = await axios.put(`/advertisement/${id}`, advertisement);
    return response.data;
  } catch (error) {
    console.error("Error updating advertisement:", error);
    throw error;
  }
};