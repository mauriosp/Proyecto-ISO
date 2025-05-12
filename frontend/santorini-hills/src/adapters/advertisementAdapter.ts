import { Advertisement } from "../models/advertisement";
import { AdvertisementDB } from "../models/advertisementDB";

// Convierte un Advertisement del frontend a AdvertisementDB para el backend
export function advertisementToDB(ad: Advertisement): AdvertisementDB {
  return {
    idEspacio: ad.property?.id || "",
    titulo: ad.title,
    descripcion: ad.description,
    precio: ad.price,
    estado: ad.status === "available" ? "Activo" : "Inactivo",
    fechaPublicacion: ad.publicationDate.toISOString(),
    imagenes: ad.images,
    extraInfo: ad.extraInfo,
    // mensaje: se puede mapear si tienes mensajes en el frontend
  };
}
