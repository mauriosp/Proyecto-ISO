package com.apirest.backend.Service;

import org.springframework.web.multipart.MultipartFile;

import com.apirest.backend.Model.Usuario;

public interface IFotoPerfilService {
    /**
     * Actualiza la foto de perfil de un usuario
     * @param usuarioId ID del usuario
     * @param archivo Archivo de imagen para la foto de perfil
     * @return Usuario con la foto de perfil actualizada
     */
    Usuario actualizarFotoPerfil(String usuarioId, MultipartFile archivo);

    /**
     * Elimina la foto de perfil de un usuario
     * @param usuarioId ID del usuario
     * @return Usuario con la foto de perfil eliminada
     */
    Usuario eliminarFotoPerfil(String usuarioId);
}