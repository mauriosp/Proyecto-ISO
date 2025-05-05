import { User } from "../models/user";
import { UserDB } from "../models/userDB";

// Convierte un UserDB recibido del backend a User del frontend
export function userFromDB(userDB: UserDB): User {
  return {
    id: userDB._id,
    name: userDB.nombre,
    email: userDB.email,
    password: null,
    phone: userDB.telefono,
    profile: userDB.tipoUsuario === "Propietario" ? "owner" : "renter",
    isVerified: userDB.estado,
    photo: userDB.fotoPerfil || "",
  };
}
