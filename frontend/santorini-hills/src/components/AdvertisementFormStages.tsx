import { useState } from "react";
import { tiposInmuebles } from "../assets/tiposInmuebles";
import { useAdvertisementContext } from "../context/advertisement/AdvertisementContext";
import FormRadioOption from "./FormRadioOption";
import { PropertyType } from "../models/property";

export const AdvertisementTypeStage = () => {
  const { property, setNextStage } = useAdvertisementContext();

  const [propertyType, setPropertyType] = useState<PropertyType>(property.type);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPropertyType(e.target.value as PropertyType);};

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setNextStage({ property: { type: propertyType } });
  };

  return (
    <form className="flex flex-col gap-10" onSubmit={handleSubmit}>
      <h2 className="text-center text-2xl font-semibold">
        ¿Cuál de estas opciones describe mejor tu espacio?
      </h2>
      <div className="flex flex-wrap justify-center gap-4 mt-4 max-w-5xl">
        {tiposInmuebles.map((tipo) => (
          <FormRadioOption
            key={tipo.id}
            id={tipo.id}
            name="tipoInmueble"
            value={tipo.id}
            onChange={handleChange}
          >
            <div className="min-w-44 h-20 flex flex-col gap-3">
              <tipo.Icon size={40} />
              <span className="font-medium text-current">{tipo.nombre}</span>
            </div>
          </FormRadioOption>
        ))}
      </div>
      <div className="flex w-40 h-12 self-end">
        {propertyType && <button className="form-button hover:bg-slate-800 bg-accent text-white font-semibold">
          Siguiente
        </button>}
      </div>
    </form>
  );
};

export const PropertyDetailsStage = () => {
  return (
    <form
      className="flex flex-col space-y-4"
      onSubmit={(e) => e.preventDefault()}
    >
      Stage 2
    </form>
  );
};

export const PublishReviewStage = () => {
  return <div className="flex flex-col space-y-4"></div>;
};
