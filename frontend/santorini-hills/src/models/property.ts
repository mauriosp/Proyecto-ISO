import { User } from "./user";

export type PropertyType =
  | "house"
  | "apartment"
  | "studio-apartment"
  | "cabin"
  | "lot"
  | "farm"
  | "room"
  | "";

export interface Property {
  id?: string;
  type: PropertyType;
  location: { latitude: number; longitude: number };
  address: string;
  area: number; // in square meters
  bathrooms: number;
  bedrooms: number;
  extraInfo?: string[];
}

export interface PropertyPost {
  idPropietario: string;
  tipo: string; // Ejemplo: "Apartamento"
  direccion: string;
  localizacion: { latitud: number; longitud: number };
  area: number;
  caracteristicas: string[];
  baños: number;
  habitaciones: number;
  tipoEspacio: string; // Puede coincidir con "tipo" o utilizarse para afinar la categoría
  estado: string; // Ejemplo: "Disponible"
  promCalificacion: number;
  arrendamiento: never[]; // Se puede tipar según la estructura real de cada contrato de arrendamiento
}

export interface PropertyGet {
  id: {
    timestamp: number;
    date: string;
  };
  idPropietario: {
    timestamp: number;
    date: string;
  };
  tipo: string; // Ejemplo: "Apartamento"
  direccion: string;
  localizacion: { latitud: number; longitud: number };
  area: number;
  caracteristicas: string[];
  baños: number;
  habitaciones: number;
  tipoEspacio: string;
  estado: string;
  arrendamiento: never[];
}

export const propertyPresenter = (
  property: Property,
  propietario: User
): PropertyPost => {
  return {
    idPropietario: propietario.id || "",
    tipo: property.type,
    direccion: property.address,
    localizacion: {
      latitud: property.location.latitude,
      longitud: property.location.longitude,
    },
    area: property.area,
    caracteristicas: property.extraInfo || [],
    baños: property.bathrooms,
    habitaciones: property.bedrooms,
    tipoEspacio: property.type,
    estado: "disponible",
    promCalificacion: 0,
    arrendamiento: [],
  };
};

export const propertyAdapter = (property: PropertyGet): Property => {
  return {
    id: property.id?.timestamp.toString(),
    type: property.tipo as PropertyType,
    location: {
      latitude: property.localizacion.latitud,
      longitude: property.localizacion.longitud,
    },
    address: property.direccion,
    area: property.area,
    bathrooms: property.baños,
    bedrooms: property.habitaciones,
    extraInfo: property.caracteristicas,
  };
};