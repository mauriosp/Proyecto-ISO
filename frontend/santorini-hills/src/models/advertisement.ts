import { Property } from "./property";
import { User } from "./user";

export interface Advertisement {
    id: string;
    title: string;
    description: string;
    price: number;
    status: "available" | "taken" | null;
    publicationDate: Date;
    property: Property;
    owner?: User;
    images: (string | File)[];
    extraInfo: string[];
}