package com.apirest.backend;

import com.apirest.backend.Model.Aviso;
import com.apirest.backend.Model.Mensaje;
import com.apirest.backend.Repository.AvisoRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.apirest.backend.Service.NotificacionServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificacionServiceImplTest {

    @Mock
    private AvisoRepository avisoRepository;

    @InjectMocks
    private NotificacionServiceImpl notificacionService;

    private ObjectId usuarioId;
    private ObjectId avisoId;
    private Aviso aviso;

    @BeforeEach
    void setUp() {
        usuarioId = new ObjectId();
        avisoId = new ObjectId();
        aviso = new Aviso();
        aviso.setId(avisoId);
        aviso.setMensaje(new ArrayList<>());
    }

    @Test
    void testEnviarNotificacion() {
        // Arrange
        String contenido = "Test notification";
        // Cambia esto para usar String en lugar de ObjectId
        when(avisoRepository.findById(avisoId.toString())).thenReturn(Optional.of(aviso));
        when(avisoRepository.save(any(Aviso.class))).thenReturn(aviso);

        // Act
        notificacionService.enviarNotificacion(usuarioId, contenido, avisoId);

        // Assert
        assertEquals(1, aviso.getMensaje().size());
        assertEquals(contenido, aviso.getMensaje().get(0).getContenido());
        assertEquals(usuarioId, aviso.getMensaje().get(0).getIdUsuario());
        assertFalse(aviso.getMensaje().get(0).isEstadoMensaje());
        verify(avisoRepository, times(1)).save(aviso);
    }

    @Test
    void testObtenerNotificacionesUsuario() {
        // Arrange
        ObjectId otroUsuarioId = new ObjectId();
        
        Mensaje mensaje1 = new Mensaje();
        mensaje1.setIdUsuario(usuarioId);
        mensaje1.setContenido("Mensaje 1");
        
        Mensaje mensaje2 = new Mensaje();
        mensaje2.setIdUsuario(otroUsuarioId);
        mensaje2.setContenido("Mensaje 2");
        
        aviso.getMensaje().add(mensaje1);
        aviso.getMensaje().add(mensaje2);
        
        when(avisoRepository.findById(avisoId.toString())).thenReturn(Optional.of(aviso));

        // Act
        List<Mensaje> result = notificacionService.obtenerNotificacionesUsuario(usuarioId, avisoId);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Mensaje 1", result.get(0).getContenido());
    }

    @Test
    void testMarcarComoLeida() {
        // Arrange
        Mensaje mensaje = new Mensaje();
        mensaje.setIdUsuario(usuarioId);
        mensaje.setContenido("Test");
        mensaje.setEstadoMensaje(false);
        
        aviso.getMensaje().add(mensaje);
        
        when(avisoRepository.findById(avisoId.toString())).thenReturn(Optional.of(aviso));

        // Act
        notificacionService.marcarComoLeida(avisoId, 0);

        // Assert
        assertTrue(aviso.getMensaje().get(0).isEstadoMensaje());
        verify(avisoRepository, times(1)).save(aviso);
    }
}