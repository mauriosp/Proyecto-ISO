import { Advertisement } from "../models/advertisement";
import { AdvertisementDB } from "../models/advertisementDB";
import { propertyFromDB } from "./propertyPresenter";
import { getPropertyById } from "../utils/APICalls";

// Convierte un AdvertisementDB recibido del backend a Advertisement del frontend, recuperando la property por idEspacio
export async function advertisementFromDB(adDB: AdvertisementDB): Promise<Advertisement> {
  let property = null;
  if (adDB.idEspacio) {
    try {
      const propertyDB = await getPropertyById(adDB.idEspacio);
      property = await propertyFromDB(propertyDB as any);
    } catch (e) {
      property = null;
      console.error("Error fetching property:", e);
    }
  }
  return {
    id: adDB._id ? adDB._id as any : undefined,
    title: adDB.titulo,
    description: adDB.descripcion,
    price: adDB.precio,
    status: adDB.estado === "Activo" ? "available" : "taken",
    publicationDate: new Date(adDB.fechaPublicacion),
    property,
    images: adDB.imagenes ? adDB.imagenes.split(",") : [],
    extraInfo: adDB.extraInfo || [],
  };
}
