import { Advertisement } from "../models/advertisement";
import { AdvertisementDB } from "../models/advertisementDB";
import { PropertyDB } from "../models/propertyDB";
import { PropertyType } from "../models/property";

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

type Oid = { $oid: string };

function extractId(id: unknown): string {
  if (typeof id === "object" && id !== null && "$oid" in id) {
    return (id as Oid).$oid;
  }
  return id as string;
}

function mapTipoEspacioToPropertyType(value: string): PropertyType {
  switch (value) {
    case "Apartamento":
      return "apartment";
    case "Casa":
      return "house";
    case "Habitación":
      return "room";
    case "Parqueo":
      return "parking";
    case "Bodega":
      return "storage";
    default:
      return "apartment"; // valor por defecto
  }
}

// Convierte un AdvertisementDB del backend a Advertisement para el frontend
export function advertisementFromDBWithProperty(
  ad: AdvertisementDB,
  property: PropertyDB // Asegúrate de que `property` se pase correctamente aquí
): Advertisement {
  return {
    id: ad.id, // Aquí tomas el id del anuncio
    title: ad.titulo,
    description: ad.descripcion,
    price: ad.precio,
    status: ad.estado === "Activo" ? "available" : "taken",
    publicationDate: new Date(ad.fechaPublicacion),
    images: Array.isArray(ad.imagenes) ? ad.imagenes : [ad.imagenes],
    extraInfo: Array.isArray(ad.extraInfo)
      ? ad.extraInfo
      : typeof ad.extraInfo === "string"
      ? ad.extraInfo.split(",").map(e => e.trim())
      : [],
    property: {
      id: property._id, // Aquí se toma el id de la propiedad
      type: mapTipoEspacioToPropertyType(property.tipoEspacio),
      address: property.direccion,
      location: null, // Aquí puedes agregar la ubicación si la tienes
      area: property.area,
      bedrooms: property.habitaciones, // Asegúrate de que esto existe en 'property'
      bathrooms: property.baños, // Lo mismo aquí
    },
  };
}
