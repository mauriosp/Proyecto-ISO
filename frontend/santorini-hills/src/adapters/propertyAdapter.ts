import { Property } from "../models/property";
import { PropertyDB } from "../models/propertyDB";

// Convierte un Property del frontend a PropertyDB para el backend
export function propertyToDB(property: Property): PropertyDB {
  return {
    idPropietario: property.owner?.id || "",
    direccion: property.address,
    area: property.area,
    tipoEspacio: property.type === "apartment" ? "Apartamento" :
                property.type === "house" ? "Casa" :
                property.type === "room" ? "Habitación" :
                property.type === "storage" ? "Bodega" :
                property.type === "lot" ? "Parqueo" : "Apartamento",
    estado: "Disponible",
    // caracteristicas, promCalificacion, Arrendamiento pueden ser agregados si se requiere
  };
}
