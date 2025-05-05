// Modelo que representa la estructura de la colección "Usuario" en MongoDB
export interface UserDB {
  _id?: string;
  nombre: string;
  email: string;
  telefono: string;
  contraseña: string;
  tipoUsuario: "Propietario" | "Interesado";
  promCalificacion?: number;
  intentosFallidos?: number;
  estado?: boolean;
  fechaBloqueo?: string | null;
  fotoPerfil?: string;
  verificacionEmail?: Array<{
    token: string;
    fechaCreacion: string;
    fechaExpiracion: string;
    tipoVerificacion: "Registro" | "Bloqueo" | "RESET_PASSWORD";
    verificado: boolean;
  }>;
  califiacionUsuario?: Array<{
    idusuarioCalifica: string;
    puntuacion: number;
    fecha: string;
    comentario?: string;
  }>;
  auditoriaPerfil?: Array<{
    fechaModificacion: string;
    campoModificado: "nombre" | "email" | "telefono" | "contraseña" | "fotoPerfil";
    valorAnterior?: string;
    valorNuevo?: string;
  }>;
}