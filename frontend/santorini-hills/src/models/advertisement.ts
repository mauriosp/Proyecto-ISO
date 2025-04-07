import { Property } from "./property";

export interface Advertisement {
    title: string;
    description: string;
    price: number;
    status: string;
    publicationDate: Date;
    property: Property | null;
    images: File[];

}