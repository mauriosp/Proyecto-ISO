import { createContext, useContext } from "react";
import { Advertisement } from "../../models/advertisement";
import { Property } from "../../models/property";

type AdvertisementContextType = {
  stage: number;
  advertisement: Advertisement;
  property: Property;
  setAdvertisement: (advertisement: Advertisement) => void;
  setProperty: (property: Property) => void;
  setNextStage: (updates: {
    advertisement?: Partial<Advertisement>;
    property?: Partial<Property>;
  }) => void;
  setPrevStage: () => void;
};

export const AdvertisementContext = createContext<AdvertisementContextType>({
  stage: 0,
  advertisement: {
    title: "",
    description: "",
    price: 0,
    status: null,
    publicationDate: new Date(),
    property: {
      id: "",
      type: "",
      address: "",
      location:  null,
      area: 0,
      bedrooms: 0,
      bathrooms: 0,
    },
    images: [],
    owner: undefined,
    extraInfo: [],
  },
  property: {
    id: "",
    type: "",
    location: { latitude: 0, longitude: 0 },
    address: "",
    area: 0,
    bathrooms: 0,
    bedrooms: 0,
  },
  setAdvertisement: () => {},
  setProperty: () => {},
  setNextStage: () => {},
  setPrevStage: () => {},
});

export const useAdvertisementContext = () => {
  const context = useContext(AdvertisementContext);
  if (!context) {
    throw new Error(
      "useAdvertisementContext must be used within an AdvertisementProvider"
    );
  }
  return context;
};
