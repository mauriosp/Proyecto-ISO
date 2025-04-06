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
  type: PropertyType; // e.g., "Apartment", "House", etc.
  address: string;
  area: number; // in square meters
  bathrooms: number;
  bedrooms: number;
}
