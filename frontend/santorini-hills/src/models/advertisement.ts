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
    images: File[];
    owner?: User;
}