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
import { useUserContext } from "../context/user/UserContext";
import { User } from "../models/user";
import NumberInputWithButtons from "./NumberInputWithButtons";

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
  const [propertyLocation, setPropertyLocation] = useState<
    Property["location"]
  >(property.location);

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
      <NavigationButtons
        onBack={setPrevStage}
        canContinue={!!propertyLocation}
      />
    </form>
  );
};

export const AdvertisementPicturesStage = () => {
  const { advertisement, setNextStage, setPrevStage } =
    useAdvertisementContext();
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
        className="form-button px-6 flex items-center justify-center gap-4 max-w-80 bg-accent hover:bg-slate-800 text-white border-2 font-semibold"
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
        <NavigationButtons
          onBack={setPrevStage}
          canContinue={advertisementPictures.length > 0}
        />
      </form>
    </div>
  );
};

export const AdvertisementTitleStage = () => {
  const { advertisement, setNextStage, setPrevStage } =
    useAdvertisementContext();
  const [advertisementTitle, setAdvertisementTitle] = useState(
    advertisement.title || ""
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setNextStage({ advertisement: { title: advertisementTitle } });
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAdvertisementTitle(e.target.value);
  };

  return (
    <form
      className="flex flex-col items-center w-10/12 gap-10"
      onSubmit={handleSubmit}
    >
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
      <NavigationButtons
        onBack={setPrevStage}
        canContinue={!!advertisementTitle}
      />
    </form>
  );
};

export const AdvertisementDescriptionStage = () => {
  const { advertisement, setNextStage, setPrevStage } =
    useAdvertisementContext();
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

  return (
    <form className="flex flex-col w-10/12 gap-10" onSubmit={handleSubmit}>
      <StageContainer title="¿Cómo describirías tu propiedad?">
        <div className="flex flex-col items-center gap-10 h-full w-full m-auto">
          <textarea
            id="advertisementDescriptionInput"
            onChange={handleChange}
            className="outline-none font-medium resize-none border-2 border-accent w-full h-64 px-4 py-3 rounded-md"
            value={advertisementDescription}
            placeholder="Escribe una descripción detallada de tu propiedad"
          />
        </div>
      </StageContainer>
      <NavigationButtons
        onBack={setPrevStage}
        canContinue={!!advertisementDescription}
      />
    </form>
  );
};

export const AdvertismentInformationStage = () => {
  const { property, setNextStage, setPrevStage } = useAdvertisementContext();
  const [bedrooms, setBedrooms] = useState<string>(
    property.bedrooms ? property.bedrooms.toString() : ""
  );
  const [bathrooms, setBathrooms] = useState<string>(
    property.bathrooms ? property.bathrooms.toString() : ""
  );
  const [area, setArea] = useState<string>(
    property.area ? property.area.toString() : ""
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setNextStage({
      property: {
        bedrooms: parseInt(bedrooms) || 0,
        bathrooms: parseInt(bathrooms) || 0,
        area: parseInt(area) || 0,
      },
    });
  };

  const processInput = (value: string) => {
    // Elimina ceros a la izquierda si hay mas de un dígito
    if (value.length > 1) {
      return value.replace(/^0+/, "");
    }
    return value;
  };

  const handleBedroomsChange = (e: { target: { value: string } }) => {
    const value = processInput(e.target.value);
    setBedrooms(value);
  };

  const handleBathroomsChange = (e: { target: { value: string } }) => {
    const value = processInput(e.target.value);
    setBathrooms(value);
  };

  const handleAreaChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = processInput(e.target.value);
    setArea(value);
  };

  const canContinue = parseInt(area) > 0;

  return (
    <form className="flex flex-col w-10/12 gap-10" onSubmit={handleSubmit}>
      <StageContainer
        title="Información de la propiedad"
        subtitle="Por favor indica los detalles básicos de tu propiedad"
      >
        <div className="flex flex-col gap-8 max-w-lg mx-auto w-full">
          <div className="form-group flex items-center justify-between">
            <label htmlFor="bedroomsInput" className="block text-lg font-medium">
              Habitaciones
            </label>
            <div className="flex items-center gap-3">
              <NumberInputWithButtons
                id="bedroomsInput"
                value={bedrooms === "" ? 0 : parseInt(bedrooms)}
                onChange={handleBedroomsChange}
              />
            </div>
          </div>

          <div className="form-group flex items-center justify-between">
            <label htmlFor="bathroomsInput" className="block text-lg font-medium">
              Baños
            </label>
            <div className="flex items-center gap-3">
              <NumberInputWithButtons
                id="bathroomsInput"
                value={bathrooms === "" ? 0 : parseInt(bathrooms)}
                onChange={handleBathroomsChange}
              />
            </div>
          </div>

          <div className="form-group flex items-center justify-between">
            <label htmlFor="areaInput" className="block text-lg font-medium">
              Área en m²
            </label>
            <input
              id="areaInput"
              type="number"
              min="0"
              value={area}
              onChange={handleAreaChange}
              className="outline-none font-medium border-accent border-2 rounded-md p-2 w-36"
              placeholder="Área en metros cuadrados"
            />
          </div>
        </div>
      </StageContainer>
      <NavigationButtons onBack={setPrevStage} canContinue={canContinue} />
    </form>
  );
};

export const AdvertisementPriceStage = () => {
  const { advertisement, setNextStage, setPrevStage } =
    useAdvertisementContext();
  const [advertisementPrice, setAdvertisementPrice] = useState(
    advertisement.price || 0
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setNextStage({ advertisement: { price: advertisementPrice } });
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/\./g, "");
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
      <NavigationButtons
        onBack={setPrevStage}
        canContinue={advertisementPrice > 0}
      />
    </form>
  );
};

export const AdvertisementPreviewStage = () => {
  const { advertisement, setPrevStage, setNextStage } = useAdvertisementContext();
  const { user } = useUserContext();

  const handleConfirm = () => {
    const advertisementWithOwner = {
      ...advertisement,
      owner: user
    };
    console.log('Advertisement to be published:', advertisementWithOwner);
    setNextStage({ advertisement: { owner: user as User, status: "available" } });
  };

  return (
    <div className="flex flex-col w-10/12 gap-10">
      <StageContainer title="Vista previa de tu anuncio">
        <div className="grid md:grid-cols-2 gap-8 w-full">
          {/* Details Section */}
          <div className="flex flex-col gap-6">
            <h3 className="text-xl font-semibold border-b border-accent pb-2">Detalles del anuncio</h3>
            <div className="space-y-4">
              <div>
                <strong className="text-accent">Título:</strong>
                <p className="text-lg font-medium">{advertisement.title}</p>
              </div>
              <div>
                <strong className="text-accent">Descripción:</strong>
                <p className="text-base">{advertisement.description}</p>
              </div>
              <div>
                <strong className="text-accent">Ubicación:</strong>
                <p className="text-base">{advertisement.property?.location}</p>
              </div>
              <div>
                <strong className="text-accent">Precio:</strong>
                <p className="text-xl font-semibold">${advertisement.price.toLocaleString()}</p>
              </div>
            </div>
          </div>

          {/* Images Section */}
          <div className="flex flex-col gap-4">
            <h3 className="text-xl font-semibold border-b border-accent pb-2">Imágenes</h3>
            <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
              {advertisement.images?.length > 0 ? (
                advertisement.images.map((image, index) => (
                  <div key={index} className="aspect-square rounded-md overflow-hidden">
                    <img
                      src={URL.createObjectURL(image)}
                      alt={`Imagen ${index + 1}`}
                      className="w-full h-full object-cover"
                    />
                  </div>
                ))
              ) : (
                <p className="col-span-full text-center py-4">No hay imágenes disponibles.</p>
              )}
            </div>
          </div>
        </div>
      </StageContainer>

      <div className="flex justify-between mt-6 w-full">
        <button
          type="button"
          onClick={setPrevStage}
          className="form-button w-min px-8 border-accent text-accent hover:bg-accent hover:text-white"
        >
          Atrás
        </button>
        <button
          type="button"
          onClick={handleConfirm}
          className="form-button w-min px-8 bg-accent hover:bg-slate-800 text-white"
        >
          Confirmar
        </button>
      </div>
    </div>
  );
};