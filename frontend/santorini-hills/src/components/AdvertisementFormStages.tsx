import { useState } from "react";
import { FaCamera, FaDollarSign, FaLocationDot } from "react-icons/fa6";
import { TbUpload } from "react-icons/tb";
import { tiposInmuebles } from "../assets/tiposInmuebles";
import { useAdvertisementContext } from "../context/advertisement/AdvertisementContext";
import { Property, PropertyType } from "../models/property";
import FormRadioOption from "./FormRadioOption";
import StageContainer from "./FormStageContainer";
import NavigationButtons from "./NavigationButtons";
import TextInput from "./TextInput";

// Reusable form components

export const AdvertisementTypeStage = () => {
  const { property, setNextStage } = useAdvertisementContext();
  const [propertyType, setPropertyType] = useState<PropertyType>(property.type);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPropertyType(e.target.value as PropertyType);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setNextStage({ property: { type: propertyType } });
  };

  return (
    <form className="flex flex-col w-10/12 gap-10" onSubmit={handleSubmit}>
      <StageContainer title="¿Cuál de estas opciones describe mejor tu espacio?">
        <div className="flex flex-wrap justify-center gap-4 mt-4 max-w-5xl mx-auto">
          {tiposInmuebles.map((tipo) => (
            <FormRadioOption
              key={tipo.id}
              id={tipo.id}
              name="tipoInmueble"
              value={tipo.id}
              checked={propertyType === tipo.id}
              onChange={handleChange}
            >
              <div className="min-w-44 h-20 flex flex-col gap-3">
                <tipo.Icon size={40} />
                <span className="font-medium text-current">{tipo.nombre}</span>
              </div>
            </FormRadioOption>
          ))}
        </div>
      </StageContainer>
      <NavigationButtons canContinue={!!propertyType} />
    </form>
  );
};

export const PropertyLocationStage = () => {
  const { property, setNextStage, setPrevStage } = useAdvertisementContext();
  const [propertyLocation, setPropertyLocation] = useState<Property["location"]>(property.location);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setNextStage({ property: { location: propertyLocation } });
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPropertyLocation(e.target.value as Property["location"]);
  };

  return (
    <form className="w-10/12" onSubmit={handleSubmit}>
      <StageContainer title="¿Dónde se encuentra tu propiedad?">
        <div className="flex flex-col gap-10 h-full w-max m-auto">
          <TextInput
            id="locationInput"
            value={propertyLocation}
            onChange={handleChange}
            placeholder="Escribe la dirección de tu propiedad"
            icon={FaLocationDot}
          />
        </div>
      </StageContainer>
      <NavigationButtons onBack={setPrevStage} canContinue={!!propertyLocation} />
    </form>
  );
};

export const AdvertisementPicturesStage = () => {
  const { advertisement, setNextStage, setPrevStage } = useAdvertisementContext();
  const [advertisementPictures, setAdvertisementPictures] = useState<File[]>(
    advertisement.images || []
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setNextStage({ advertisement: { images: advertisementPictures } });
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const filesArray = Array.from(e.target.files);
      setAdvertisementPictures((prevFilesArray) => {
        const availableSlots = 10 - prevFilesArray.length;
        const filesToAdd = filesArray.slice(0, availableSlots);
        return [...prevFilesArray, ...filesToAdd];
      });
    }
  };

  const FileUploadComponent = () => (
    <>
      <label
        htmlFor="advertisementPicturesInput"
        className="form-button flex items-center justify-center gap-4 max-w-80 bg-accent hover:bg-slate-800 text-white border-2 font-semibold"
      >
        <TbUpload size={20} />
        Cargar archivos
      </label>
      <input
        id="advertisementPicturesInput"
        type="file"
        accept="image/jpg, image/png, image/jpeg"
        multiple
        onChange={handleChange}
        className="hidden"
      />
    </>
  );

  return (
    <div className="flex flex-col space-y-4">
      <form className="w-10/12" onSubmit={handleSubmit}>
        <StageContainer title="Agrega algunas fotos de tu propiedad">
          <div className="flex flex-col items-center gap-10 h-full w-max m-auto">
            <FaCamera size={100} className="text-accent" />
            {advertisementPictures.length < 10 && <FileUploadComponent />}
            <p>
              {advertisementPictures.length > 0
                ? `${advertisementPictures.length} fotos seleccionadas`
                : "Puedes subir hasta 10 fotos"}
            </p>
          </div>
        </StageContainer>
        <NavigationButtons onBack={setPrevStage} canContinue={advertisementPictures.length > 0} />
      </form>
    </div>
  );
};

export const AdvertisementTitleStage = () => {
  const { advertisement, setNextStage, setPrevStage } = useAdvertisementContext();
  const [advertisementTitle, setAdvertisementTitle] = useState(advertisement.title || "");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setNextStage({ advertisement: { title: advertisementTitle } });
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAdvertisementTitle(e.target.value);
  };

  return (
    <form className="flex flex-col items-center w-10/12 gap-10" onSubmit={handleSubmit}>
      <StageContainer 
        title="Ahora, ponle un título a tu propiedad" 
        subtitle="El título debe ser breve y directo. Resalta lo mejor de tu propiedad"
      >
        <input
          id="advertisementTitleInput"
          type="text"
          onChange={handleChange}
          className="outline-none font-medium border-accent border-2 rounded-md p-2 w-full max-w-lg mx-auto"
          value={advertisementTitle}
          placeholder="Escribe el título de tu propiedad"
        />
      </StageContainer>
      <NavigationButtons onBack={setPrevStage} canContinue={!!advertisementTitle} />
    </form>
  );
};

export const AdvertisementDescriptionStage = () => {
  const { advertisement, setNextStage, setPrevStage } = useAdvertisementContext();
  const [advertisementDescription, setAdvertisementDescription] = useState(
    advertisement.description || ""
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setNextStage({ advertisement: { description: advertisementDescription } });
  };

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setAdvertisementDescription(e.target.value);
  };

  const TextareaInput = () => (
    <textarea
      id="advertisementDescriptionInput"
      onChange={handleChange}
      className="outline-none font-medium resize-none border-2 border-accent w-full h-64 px-4 py-3 rounded-md"
      value={advertisementDescription}
      placeholder="Escribe una descripción detallada de tu propiedad"
    />
  );

  return (
    <form className="flex flex-col w-10/12 gap-10" onSubmit={handleSubmit}>
      <StageContainer title="¿Cómo describirías tu propiedad?">
        <div className="flex flex-col items-center gap-10 h-full w-full m-auto">
          <TextareaInput />
        </div>
      </StageContainer>
      <NavigationButtons onBack={setPrevStage} canContinue={!!advertisementDescription} />
    </form>
  );
};

export const AdvertisementPriceStage = () => {
  const { advertisement, setNextStage, setPrevStage } = useAdvertisementContext();
  const [advertisementPrice, setAdvertisementPrice] = useState(
    advertisement.price || 0
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Advertisement:", advertisement);
    console.log("Property:", advertisement.property);
    setNextStage({ advertisement: { price: advertisementPrice } });
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/\./g, ""); // Remove dots for parsing
    if (/^\d*$/.test(value)) {
      setAdvertisementPrice(Number(value));
    }
  };

  const formatNumber = (value: number) => {
    return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
  };

  return (
    <form className="flex flex-col w-10/12 gap-10" onSubmit={handleSubmit}>
      <StageContainer title="¿Cuál es el precio de tu propiedad?">
        <div className="flex flex-col items-center gap-10 h-full w-max m-auto">
          <TextInput 
            id="advertisementPriceInput"
            value={formatNumber(advertisementPrice)}
            onChange={handleChange}
            placeholder="Escribe el precio de tu propiedad"
            icon={FaDollarSign}
          />
        </div>
      </StageContainer>
      <NavigationButtons onBack={setPrevStage} canContinue={advertisementPrice > 0} />
    </form>
  );
};
