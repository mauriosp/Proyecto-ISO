package com.apirest.backend.Exception;

public class ImagenException extends RuntimeException {
    public ImagenException(String mensaje) {
        super(mensaje);
    }

    // Subclase para errores de tipo de imagen inválido
    public static class TipoInvalidoException extends ImagenException {
        public TipoInvalidoException(String mensaje) {
            super(mensaje);
        }
    }

    // Subclase para errores de tamaño de imagen excedido
    public static class TamanoExcedidoException extends ImagenException {
        public TamanoExcedidoException(String mensaje) {
            super(mensaje);
        }
    }

    // Subclase para errores de almacenamiento de imagen
    public static class AlmacenamientoException extends ImagenException {
        public AlmacenamientoException(String mensaje) {
            super(mensaje);
        }
    }
}