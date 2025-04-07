package com.apirest.backend.Service;

import com.apirest.backend.Model.Aviso;
import org.springframework.web.multipart.MultipartFile;

public interface IImagenAvisoService {
    /**
     * Agrega una imagen a un aviso existente
     * @param avisoId ID del aviso
     * @param archivo Archivo de imagen a agregar
     * @return Aviso actualizado con la nueva imagen
     */
    Aviso agregarImagenAAviso(String avisoId, MultipartFile archivo);

    /**
     * Elimina una imagen de un aviso
     * @param avisoId ID del aviso
     * @param nombreImagen Nombre de la imagen a eliminar
     * @return Aviso actualizado sin la imagen
     */
    Aviso eliminarImagenDeAviso(String avisoId, String nombreImagen);
}