// Modelo que representa la estructura de la colección "Espacio" en MongoDB
export interface PropertyDB {
  _id?: string;
  idPropietario: string;
  direccion: string;
  area: number;
  caracteristicas?: string;
  tipoEspacio: "Apartamento" | "Casa" | "Habitación" | "Parqueo" | "Bodega";
  estado: "Disponible" | "Ocupado" | "Mantenimiento" | "Reservado";
  promCalificacion?: number;
  Arrendamiento?: Array<{
    idUsuario: string;
    fechaSalida: string;
    fechaInicio: string;
    estado: "Activo" | "Completado" | "Cancelado" | "Pendiente";
    terminos?: string;
    calificacionEspacio?: {
      puntuacion: number;
      fecha: string;
      comentario?: string;
    };
  }>;
}