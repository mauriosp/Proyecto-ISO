import { Property } from "./property";
import { User } from "./user";

export interface Advertisement {
    title: string;
    description: string;
    price: number;
    status: "available" | "taken" | "pending" | null;
    publicationDate: Date;
    property: Property | null;
    owner?: User;
    images: string[];
}

export interface AdvertisementPost extends Advertisement{
    images: string[];
}

export interface AdvertisementResponse extends Advertisement {
    id: number;
    images: string[];
}

