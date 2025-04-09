import { useState } from "react";
import { BsCaretLeft, BsCaretRight } from "react-icons/bs";
import { FaCamera, FaDollarSign, FaLocationDot } from "react-icons/fa6";
import { MdOutlineBed } from "react-icons/md";
import { TbBath, TbMeterSquare, TbUpload } from "react-icons/tb";
import { tiposInmuebles } from "../assets/tiposInmuebles";
import { useAdvertisementContext } from "../context/advertisement/AdvertisementContext";
import { useUserContext } from "../context/user/UserContext";
import { Property, PropertyType } from "../models/property";
import { User } from "../models/user";
import FormRadioOption from "./FormRadioOption";
import StageContainer from "./FormStageContainer";
import NavigationButtons from "./NavigationButtons";
import NumberInputWithButtons from "./NumberInputWithButtons";
import PlaceInput from "./PlaceInput";
import TextInput from "./TextInput";

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

  const handlePlaceSelected = (address: string) => {
    setPropertyLocation(address);
  };

  return (
    <form className="w-10/12" onSubmit={handleSubmit}>
      <StageContainer title="¿Dónde se encuentra tu propiedad?">
        <div className="flex flex-col gap-10 h-full w-max m-auto">
          <PlaceInput Icon={FaLocationDot} onPlaceSelected={handlePlaceSelected} />
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
                : "Selecciona al menos 5 fotos"}
            </p>
          </div>
        </StageContainer>
        <NavigationButtons
          onBack={setPrevStage}
          canContinue={advertisementPictures.length >= 5}
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
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const { property } = advertisement;

  const handleConfirm = () => {

    const advertisementWithOwner = {
      ...advertisement,
      owner: user
    };
    console.log('Advertisement to be published:', advertisementWithOwner);
    setNextStage({ advertisement: { owner: user as User, status: "available" } });
  };

  const goToNextImage = () => {
    if (advertisement.images && currentImageIndex < advertisement.images.length - 1) {
      setCurrentImageIndex(currentImageIndex + 1);
    }
  };

  const goToPrevImage = () => {
    if (currentImageIndex > 0) {
      setCurrentImageIndex(currentImageIndex - 1);
    }
  };

  const selectImage = (index: number) => {
    setCurrentImageIndex(index);
  };

  // Obtener el tipo de propiedad en formato legible
  const propertyTypeObj = tiposInmuebles.find(tipo => tipo.id === property?.type);
  const propertyTypeName = propertyTypeObj ? propertyTypeObj.nombre : '';

  return (
    <div className="flex flex-col w-10/12 gap-6">
      <h2 className="text-2xl font-bold mb-4">Vista previa de tu anuncio</h2>

      {/* Imagen principal y galería */}
      <div className="w-full rounded-xl overflow-hidden bg-gray-100 relative">
        {/* Imagen principal */}
        {advertisement.images && advertisement.images.length > 0 ? (
          <div className="w-full h-96 relative">
            <img
              src={URL.createObjectURL(advertisement.images[currentImageIndex])}
              alt={advertisement.title}
              className="w-full h-full object-cover"
            />

            {/* Botones de navegación de imágenes */}
            {advertisement.images.length > 1 && (
              <>
                <button
                  onClick={goToPrevImage}
                  disabled={currentImageIndex === 0}
                  className="absolute left-4 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white rounded-full p-2 disabled:opacity-30"
                >
                  <BsCaretLeft />
                </button>
                <button
                  onClick={goToNextImage}
                  disabled={currentImageIndex === advertisement.images.length - 1}
                  className="absolute right-4 top-1/2 -translate-y-1/2 bg-white/80 hover:bg-white rounded-full p-2 disabled:opacity-30"
                >
                  <BsCaretRight />
                </button>
              </>
            )}
          </div>
        ) : (
          <div className="w-full h-96 bg-gray-200 flex items-center justify-center">
            <p className="text-gray-500">No hay imágenes disponibles</p>
          </div>
        )}

        {/* Miniaturas */}
        {advertisement.images && advertisement.images.length > 0 && (
          <div className="flex overflow-x-auto gap-2 p-2 bg-white">
            {advertisement.images.map((image, index) => (
              <div
                key={index}
                onClick={() => selectImage(index)}
                className={`w-20 h-20 flex-shrink-0 cursor-pointer ${index === currentImageIndex ? 'border-2 border-accent' : 'opacity-70'}`}
              >
                <img
                  src={URL.createObjectURL(image)}
                  alt={`Miniatura ${index + 1}`}
                  className="w-full h-full object-cover"
                />
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Contenido principal */}
      <div className="flex flex-col md:flex-row gap-8 mt-4">
        {/* Panel izquierdo: Detalles del anuncio */}
        <div className="flex flex-col flex-1 gap-6">
          <section>
            <h1 className="text-3xl font-semibold">{advertisement.title}</h1>
            <p className="text-gray-600">{advertisement.property?.location}</p>

            {/* Características de la propiedad */}
            <div className="flex gap-4 mt-2">
              <div className="flex items-center gap-1 text-accent">
                <MdOutlineBed size={20} />
                <p className="font-medium">{property?.bedrooms || 0}</p>
              </div>
              <div className="flex items-center gap-1 text-accent">
                <TbBath size={20} />
                <p className="font-medium">{property?.bathrooms || 0}</p>
              </div>
              <div className="flex items-center gap-1 text-accent">
                <TbMeterSquare size={20} />
                <p className="font-medium">{property?.area || 0} m²</p>
              </div>
              {propertyTypeName && (
                <div className="flex items-center gap-1">
                  <span className="font-medium text-gray-600">{propertyTypeName}</span>
                </div>
              )}
            </div>

            {/* Descripción */}
            <div className="mt-6">
              <h2 className="text-xl font-medium mb-2">Descripción</h2>
              <p className="text-gray-700">{advertisement.description}</p>
            </div>
          </section>
        </div>

        {/* Panel derecho: Precio y contacto */}
        <div className="w-full md:w-1/3">
          <div className="bg-white shadow-md rounded-xl p-6">
            <h2 className="text-2xl font-bold text-accent">
              ${advertisement.price.toLocaleString()}
            </h2>
            <div className="mt-4 border-t pt-4">
              <div className="flex items-center gap-2">
                <div className="w-12 h-12 bg-accent rounded-full flex items-center justify-center text-white">
                  {user?.name ? user.name.charAt(0).toUpperCase() : "?"}
                </div>
                <div>
                  <p className="font-medium">{user?.name || "Usuario"}</p>
                  <p className="text-sm text-gray-500">{user?.email}</p>
                </div>
              </div>
              <p className="mt-3 text-sm text-gray-500">
                Esta previsualización muestra cómo verán los usuarios tu anuncio.
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Botones de navegación */}
      <div className="flex justify-between mt-8 w-full">
        <button
          type="button"
          onClick={setPrevStage}
          className="form-button px-8 border-accent text-accent hover:bg-accent hover:text-white"
        >
          Atrás
        </button>
        <button
          type="button"
          onClick={handleConfirm}
          className="form-button px-8 bg-accent hover:bg-slate-800 text-white"
        >
          Publicar anuncio
        </button>
      </div>
    </div>
  );
};