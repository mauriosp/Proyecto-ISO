package com.apirest.backend.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apirest.backend.Model.Espacio;
import com.apirest.backend.Repository.EspacioRepository;

@Service
public class EspacioServiceImpl implements IEspacioService {

    @Autowired
    private EspacioRepository espacioRepository;

    @Autowired
    private IUsuarioService usuarioService;

    @Override
    public List<Espacio> listarEspacios() {
        return espacioRepository.findAll();
    }

    @Override
    public Optional<Espacio> buscarEspacioPorId(String id) {
        return espacioRepository.findById(id);
    }

    @Override
    public Optional<Espacio> buscarEspacioPorDireccionYPropietario(String direccion, ObjectId idPropietario) {
        String idPropietarioStr = idPropietario.toHexString();
        return espacioRepository.findByDireccionAndIdPropietario(direccion, idPropietarioStr);
    }

    @Override
    public Espacio guardarEspacio(Espacio espacio) {
        return espacioRepository.save(espacio);
    }

    @Override
    public void eliminarEspacio(String id) {
        espacioRepository.deleteById(id);
    }

    @Override
    public Espacio crearEspacio(ObjectId idUsuario, String tipoEspacio, int baños, int habitaciones, String direccion, BigDecimal area) {
        // Validar que el usuario exista
        if (!usuarioService.existeUsuarioPorId(idUsuario)) {
            throw new IllegalArgumentException("El usuario con el ID proporcionado no existe.");
        }

        // Validar que la dirección no esté ya registrada
        if (espacioRepository.existsByDireccion(direccion)) {
            throw new IllegalArgumentException("Ya existe un espacio registrado con la misma dirección.");
        }

        // Validar que el tipo de espacio no sea nulo o vacío
        if (tipoEspacio == null || tipoEspacio.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de espacio no puede estar vacío.");
        }

        // Validar que la dirección no sea nula o vacía
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía.");
        }

        // Validar que el área sea mayor a 0
        if (area == null || area.doubleValue() <= 0) {
            throw new IllegalArgumentException("El área debe ser mayor a 0.");
        }

        // Crear un nuevo espacio
        Espacio nuevoEspacio = new Espacio();
        nuevoEspacio.setIdPropietario(idUsuario); // Establecer el ID del propietario
        nuevoEspacio.setTipoEspacio(tipoEspacio); // Establecer el campo tipoEspacio
        nuevoEspacio.setHabitaciones(habitaciones); // Establecer el número de habitaciones
        nuevoEspacio.setBaños(baños); // Establecer el número de baños
        nuevoEspacio.setDireccion(direccion); // Establecer la dirección
        nuevoEspacio.setArea(area.doubleValue()); // Establecer el área
        nuevoEspacio.setEstado("Disponible"); // Establecer el estado como "Disponible"

        // Guardar el espacio en la base de datos
        return espacioRepository.save(nuevoEspacio);
    }

    @Override
    public Espacio editarEspacio(String idEspacio, String tipoEspacio, int baños, int habitaciones, String direccion, BigDecimal area) {
        // Buscar el espacio por ID
        Optional<Espacio> espacioExistente = espacioRepository.findById(idEspacio);
        if (espacioExistente.isEmpty()) {
            throw new IllegalArgumentException("El espacio con el ID proporcionado no existe.");
        }

        Espacio espacio = espacioExistente.get();

        // Validar que el tipo de espacio no sea nulo o vacío
        if (tipoEspacio == null || tipoEspacio.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de espacio no puede estar vacío.");
        }

        // Validar que la dirección no sea nula o vacía
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía.");
        }

        // Validar que el área sea mayor a 0
        if (area == null || area.doubleValue() <= 0) {
            throw new IllegalArgumentException("El área debe ser mayor a 0.");
        }

        // Actualizar los datos del espacio
        espacio.setTipoEspacio(tipoEspacio);
        espacio.setHabitaciones(habitaciones); // Actualizar el número de habitaciones
        espacio.setBaños(baños); // Actualizar el número de baños
        espacio.setDireccion(direccion);
        espacio.setArea(area.doubleValue());

        // Guardar los cambios en la base de datos
        return espacioRepository.save(espacio);
    }
}
