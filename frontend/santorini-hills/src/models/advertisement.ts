import { Property } from "./property";

export interface Advertisement {
    id?: number;
    title: string;
    description: string;
    price: number;
    status: string;
    publicationDate: Date;
    property: Property | null;
    images: File[];

}