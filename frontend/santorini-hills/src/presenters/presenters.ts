import { User, UserPost } from "../models/user";

export const userPresenter = (user: User): UserPost => {
    return {
        nombre: user.name,
        email: user.email,
        telefono: user.phone,
        contraseña: user.password,
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
}

