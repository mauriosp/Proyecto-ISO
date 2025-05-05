import { Property } from "./property";
import { User } from "./user";

export interface Advertisement {
    id?: string;
    title: string;
    description: string;
    price: number;
    status: "available" | "taken" | "pending" | null;
    publicationDate: Date;
    property: Property | null;
    owner?: User;
    images: string[];
    extraInfo: string[];
}