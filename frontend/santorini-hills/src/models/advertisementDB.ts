// Modelo que representa la estructura de la colección "Aviso" en MongoDB
export interface AdvertisementDB {
  _id?: string; // objectId como string
  idEspacio: string; // objectId como string
  titulo: string;
  descripcion: string;
  precio: number;
  estado: "Activo" | "Inactivo";
  fechaPublicacion: string; // ISO date string
  imagenes: File; // Puede ser un string separado por comas o un solo string
  mensaje?: Array<{
    idUsuario: string;
    contenido: string;
    fechaMensaje: string;
    estadoMensaje: boolean;
    respuestaMensaje?: {
      fechaRespuesta: string;
      contenido: string;
    };
  }>;
  extraInfo?: Array<string>;
}