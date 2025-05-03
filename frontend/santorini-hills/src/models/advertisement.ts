import { Property } from "./property";
import { User } from "./user";

export interface Advertisement {
    id?: number;
    title: string;
    description: string;
    price: number;
    status: "available" | "taken" | "pending" | null;
    publicationDate: Date;
    property: Property | null;
    owner?: User;
    images: string[];
}

export interface AdvertisementPost {
    descripcion: string;
    precioMensual: number;
    condicionesAdicionales: string;
    imagenes: string[];
    titulo: string;
    fechaPublicacion: string;
    idPropietario: string;
    idPropiedad: string;    
}

export interface AdvertisementGet {
    id: {timestamp: number; date: string};
    idPropietario: {timestamp: number; date: string};
    idPropiedad: {timestamp: number; date: string};
    images: string[];
}

