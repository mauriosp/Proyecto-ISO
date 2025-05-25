package com.apirest.backend.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.apirest.backend.Model.Espacio;

@Repository
public interface EspacioRepository extends MongoRepository<Espacio, String> {
    boolean existsByDireccion(String direccion);
    Optional<Espacio> findByDireccionAndIdPropietario(String direccion, String idPropietario);
        
    /**
     * Buscar espacios que pertenecen a un propietario específico
     */
    List<Espacio> findByIdPropietario(ObjectId idPropietario);
    
    /**
     * Buscar espacios que tienen arrendamientos activos
     */
    @Query("{ 'Arrendamiento.estado': 'Activo' }")
    List<Espacio> findEspaciosConArrendamientosActivos();
    
    /**
     * Buscar espacios de un propietario que tienen arrendamientos activos
     */
    @Query("{ 'idPropietario': ?0, 'Arrendamiento.estado': 'Activo' }")
    List<Espacio> findEspaciosConArrendamientosActivosByPropietario(ObjectId idPropietario);
    
    /**
     * Buscar espacios que tienen arrendamientos en un rango de fechas específico
     * (para verificar disponibilidad)
     */
    @Query("{ '_id': ?0, 'Arrendamiento': { $elemMatch: { " +
           "'estado': { $in: ['Activo', 'Pendiente'] }, " +
           "'fechaInicio': { $lt: ?2 }, " +
           "'fechaSalida': { $gt: ?1 } } } }")
    Optional<Espacio> findEspacioConArrendamientoEnRangoFechas(
        ObjectId idEspacio, Date fechaInicio, Date fechaSalida);
    
    /**
     * Buscar espacio que tiene un arrendamiento específico de un usuario
     */
    @Query("{ 'Arrendamiento.idUsuario': ?0 }")
    List<Espacio> findEspaciosByArrendatario(ObjectId idArrendatario);
    
    /**
     * Buscar espacios con arrendamientos que pueden ser modificados
     */
    @Query("{ 'idPropietario': ?0, 'Arrendamiento.estado': { $in: ['Activo', 'Pendiente'] } }")
    List<Espacio> findEspaciosConArrendamientosModificablesByPropietario(ObjectId idPropietario);
    
    /**
     * Contar total de arrendamientos activos de un propietario
     */
    @Query(value = "{ 'idPropietario': ?0, 'Arrendamiento.estado': 'Activo' }", count = true)
    long countArrendamientosActivosByPropietario(ObjectId idPropietario);
    
    /**
     * Buscar espacios disponibles (sin arrendamientos activos)
     */
    @Query("{ 'estado': 'Disponible', " +
           "$or: [ " +
           "  { 'Arrendamiento': { $exists: false } }, " +
           "  { 'Arrendamiento': { $size: 0 } }, " +
           "  { 'Arrendamiento.estado': { $nin: ['Activo', 'Pendiente'] } } " +
           "] }")
    List<Espacio> findEspaciosDisponibles();
    
    /**
     * Buscar espacios con arrendamientos que expiran pronto
     */
    @Query("{ 'Arrendamiento': { $elemMatch: { " +
           "'estado': 'Activo', " +
           "'fechaSalida': { $gte: ?0, $lte: ?1 } } } }")
    List<Espacio> findEspaciosConArrendamientosProximosAVencer(Date fechaInicio, Date fechaFin);
}