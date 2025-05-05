import { User } from "../models/user";
import { UserDB } from "../models/userDB";

// Convierte un User del frontend a UserDB para el backend
export function userToDB(user: User): UserDB {
  return {
    nombre: user.name,
    email: user.email,
    telefono: user.phone,
    contraseña: user.password || "",
    tipoUsuario: user.profile === "owner" ? "Propietario" : "Interesado",
    promCalificacion: 0,
    intentosFallidos: 0,
    estado: user.isVerified || false,
    fechaBloqueo: null,
    fotoPerfil: user.photo || "",
    verificacionEmail: [],
    califiacionUsuario: [],
    auditoriaPerfil: [],
  };
}
