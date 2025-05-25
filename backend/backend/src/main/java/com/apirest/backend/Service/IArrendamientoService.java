package com.apirest.backend.Service;

import java.util.List;

import com.apirest.backend.Dto.CancelarArrendamientoDTO;
import com.apirest.backend.Dto.CrearArrendamientoDTO;
import com.apirest.backend.Dto.ModificarArrendamientoDTO;
import com.apirest.backend.Model.Arrendamiento;

public interface IArrendamientoService {
    
    /**
     * Registra un nuevo acuerdo de arrendamiento
     * @param crearArrendamientoDTO Datos del arrendamiento a crear
     * @return Arrendamiento creado
     */
    Arrendamiento registrarArrendamiento(CrearArrendamientoDTO crearArrendamientoDTO);
    
    /**
     * Modifica un acuerdo de arrendamiento existente
     * @param modificarArrendamientoDTO Datos del arrendamiento a modificar
     * @return Arrendamiento modificado
     */
    Arrendamiento modificarArrendamiento(ModificarArrendamientoDTO modificarArrendamientoDTO);
    
    /**
     * Cancela un acuerdo de arrendamiento
     * @param cancelarArrendamientoDTO Datos para cancelar el arrendamiento
     * @return Arrendamiento cancelado
     */
    Arrendamiento cancelarArrendamiento(CancelarArrendamientoDTO cancelarArrendamientoDTO);
    
    /**
     * Obtiene todos los arrendamientos activos de un propietario
     * @param idPropietario ID del propietario
     * @return Lista de arrendamientos activos
     */
    List<Arrendamiento> obtenerArrendamientosActivosPorPropietario(String idPropietario);
    
    /**
     * Obtiene todos los arrendamientos de un espacio específico
     * @param idEspacio ID del espacio
     * @return Lista de arrendamientos del espacio
     */
    List<Arrendamiento> obtenerArrendamientosPorEspacio(String idEspacio);
    
    /**
     * Verifica si un espacio está disponible para las fechas dadas
     * @param idEspacio ID del espacio
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return true si está disponible, false si no
     */
    boolean verificarDisponibilidadEspacio(String idEspacio, java.util.Date fechaInicio, java.util.Date fechaFin);
}
