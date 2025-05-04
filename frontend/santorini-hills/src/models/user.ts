import { EmailVerification } from "./emailVerification";

export interface User {
  id?: string;
  name: string;
  email: string;
  password: string | null;
  phone: string;
  profile?: "owner" | "renter" | "admin";
  isVerified?: boolean;
  photo?: string;
}

export interface UserPost {
  nombre: string;
  email: string;
  telefono: string;
  contraseña: string;
  tipoUsuario: string; // Ejemplo: "Propietario". Se puede usar un union type si se conocen más opciones.
  promCalificacion: number;
  intentosFallidos: number;
  estado: boolean;
  fechaBloqueo: string | null; // Representa fecha o null
  fotoPerfil: string;
  verificacionEmail: never[];
  calificacionUsuario: never[]; // Se puede tipar mejor según la estructura real
  auditoriaPerfil: never[]; // Se puede tipar mejor según la estructura real
}

export interface UserGet {
  id: string;
  nombre: string;
  email: string;
  telefono: string;
  fotoPerfil: string;
  tipoUsuario: "Propietario" | "Inquilino";
  promCalificacion: number;
  intentosFallidos: number;
  estado: boolean;
  fechaBloqueo: string | null; // Representa fecha o null
  verificacionEmail: EmailVerification[];
  calificacionUsuario: number; // Se puede tipar mejor según la estructura real
  auditoriaPerfil: never; // Se puede tipar mejor según la estructura real
}

export const userPresenter = (user: User): UserPost => {
  return {
    nombre: user.name,
    email: user.email,
    telefono: user.phone,
    contraseña: user.password || "",
    tipoUsuario: user.profile || "renter",
    promCalificacion: 0,
    intentosFallidos: 0,
    estado: user.isVerified || false,
    fechaBloqueo: null,
    fotoPerfil: user.photo || "",
    verificacionEmail: [],
    calificacionUsuario: [],
    auditoriaPerfil: [],
  };
};

export const userAdapter = (user: UserGet): User => {
  return {
    id: user.id,
    name: user.nombre,
    email: user.email,
    password: null,
    phone: user.telefono,
    profile: user.tipoUsuario as "owner" | "renter",
    isVerified: user.estado,
    photo: user.fotoPerfil || "",
  };
};
