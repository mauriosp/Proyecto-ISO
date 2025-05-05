import { Property } from "../models/property";
import { PropertyDB } from "../models/propertyDB";
import { getUserById } from "../utils/APICalls";

// Convierte un PropertyDB recibido del backend a Property del frontend, recuperando el owner por idPropietario
export async function propertyFromDB(propertyDB: PropertyDB): Promise<Property> {
  let owner = undefined;
  if (propertyDB.idPropietario) {
    try {
      owner = await getUserById(propertyDB.idPropietario);
    } catch (e) {
      owner = undefined;
      console.error("Error fetching owner:", e);
    }
  }
  return {
    id: propertyDB._id,
    type: propertyDB.tipoEspacio === "Apartamento" ? "apartment" :
          propertyDB.tipoEspacio === "Casa" ? "house" :
          propertyDB.tipoEspacio === "Habitación" ? "room" :
          propertyDB.tipoEspacio === "Bodega" ? "cabin" :
          propertyDB.tipoEspacio === "Parqueo" ? "lot" : "apartment",
    location: null,
    address: propertyDB.direccion,
    area: propertyDB.area,
    bathrooms: 0,
    bedrooms: 0,
    owner,
  };
}
