export interface User {
    name: string;
    email: string;
    password: string;
    phone: string;
    profile?: "owner" | "renter";
    isVerified?: boolean;
    photo?:string;
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
    auditoriaPerfil: never[];    // Se puede tipar mejor según la estructura real
  }