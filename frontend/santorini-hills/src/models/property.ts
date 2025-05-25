import { User } from "./user";

export type PropertyType =
  | "house"
  | "apartment"
  | "studio-apartment"
  | "parking"
  | "lot"
  | "storage"
  | "room"
  | "";

export interface Property {
  id: string;
  type: PropertyType;
  location: { latitude: number; longitude: number } | null;
  address: string;
  area: number; // in square meters
  bathrooms: number;
  bedrooms: number;
  owner?: User;
}