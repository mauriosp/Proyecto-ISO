import { useState } from "react";
import { Advertisement } from "../../models/advertisement";
import { Property } from "../../models/property";
import { AdvertisementContext } from "./AdvertisementContext";

export const AdvertisementProvider = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const [stage, setStage] = useState<number>(0);
  const [advertisement, setAdvertisementState] = useState<Advertisement>({
    title: "",
    description: "",
    price: 0,
    status: "",
    publicationDate: new Date(),
    property: null,
    images: [],
  });
  const [property, setPropertyState] = useState<Property>({
    type: "",
    location: "",
    area: 0,
    bathrooms: 0,
    bedrooms: 0,
  });

  const setAdvertisement = (newAdvertisement: Advertisement) => {
    setAdvertisementState(newAdvertisement);
  };

  const setProperty = (newProperty: Property) => {
    setPropertyState(newProperty);
  };

  const setNextStage = (updates: {
    advertisement?: Partial<Advertisement>;
    property?: Partial<Property>;
  }) => {
    if (updates.advertisement) {
      setAdvertisementState((prev) => ({ ...prev, ...updates.advertisement }));
    }
    if (updates.property) {
      setPropertyState((prev) => {
        const newProperty = { ...prev, ...updates.property };
        setAdvertisementState((prevAd) => ({
          ...prevAd,
          property: newProperty,
        }));
        return newProperty;
      });
    }
    setStage((prevStage) => prevStage + 1);
  };

  const setPrevStage = () => {
    setStage((prevStage) => (prevStage > 0 ? prevStage - 1 : 0));
  };

  return (
    <AdvertisementContext.Provider
      value={{
        stage,
        advertisement,
        property,
        setAdvertisement,
        setProperty,
        setNextStage,
        setPrevStage,
      }}
    >
      {children}
    </AdvertisementContext.Provider>
  );
};
