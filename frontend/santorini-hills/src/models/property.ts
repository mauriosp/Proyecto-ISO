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
  type: PropertyType;
  location: string;
  area: number; // in square meters
  bathrooms: number;
  bedrooms: number;
}
