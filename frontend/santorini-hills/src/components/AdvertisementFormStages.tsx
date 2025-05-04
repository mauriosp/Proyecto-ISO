import { AnimatePresence, motion } from "framer-motion";
import { useState } from "react";
import { FaCamera, FaDollarSign, FaLocationDot, FaPlus, FaTrash } from "react-icons/fa6";
import { TbUpload } from "react-icons/tb";
import { redirect, useLocation, useParams } from "react-router";
import { tiposInmuebles } from "../assets/tiposInmuebles";
import { useAdvertisementContext } from "../context/advertisement/AdvertisementContext";
import { useUserContext } from "../context/user/UserContext";
import { Property, PropertyType } from "../models/property";
import AdvertisementPage from "../pages/AdvertisementPage";
import { publishAdvertisement, updateAdvertisement } from "../utils/APICalls";
import { formatNumber } from "../utils/parseNumbers";
import { uploadFileToFirebase } from "../utils/UploadFileToFirebase";
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
    if (!propertyType) return; // Solo avanza si hay tipo seleccionado
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
  >(property.location || { latitude: 0, longitude: 0 });
  const [propertyAddress, setPropertyAddress] = useState<string>(
    property.address || ""
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!propertyLocation || propertyLocation.latitude === 0) return; // Solo avanza si hay ubicación válida
    setNextStage({
      property: {
        location: propertyLocation,
        address: propertyAddress,
      },
    });
  };

  const handlePlaceSelected = (locationData: {
    latitude: number;
    longitude: number;
    address: string;
  }) => {
    // Extraer solo las propiedades de ubicación requeridas para el tipo Property.location
    setPropertyLocation({
      latitude: locationData.latitude,
      longitude: locationData.longitude,
    });

    // Establecer la dirección como propiedad separada
    setPropertyAddress(locationData.address);
  };

  return (
    <form className="w-10/12" onSubmit={handleSubmit}>
      <StageContainer title="¿Dónde se encuentra tu propiedad?">
        <div className="flex flex-col gap-10 h-full w-full max-w-3xl m-auto">
          <PlaceInput
            Icon={FaLocationDot}
            onPlaceSelected={handlePlaceSelected}
          />
          {propertyAddress && (
            <div className="text-center">
              <p className="text-sm text-gray-600">Dirección seleccionada</p>
              <p className="font-medium">{propertyAddress}</p>
            </div>
          )}
        </div>
      </StageContainer>
      <NavigationButtons
        onBack={setPrevStage}
        canContinue={!!propertyLocation && propertyLocation.latitude !== 0}
      />
    </form>
  );
};

export const AdvertisementPicturesStage = () => {
  const { advertisement, setNextStage, setPrevStage } =
    useAdvertisementContext();

  const [advertisementPictures, setAdvertisementPictures] = useState<File[]>(
    []
  );
  const [imageUrls, setImageUrls] = useState<string[]>(
    advertisement.images || []
  );
  const [isUploading, setIsUploading] = useState(false);

  const [showConfirmDelete, setShowConfirmDelete] = useState(false);
  const [selectedImageIndex, setSelectedImageIndex] = useState<number | null>(
    null
  );
  const [selectedImageIsUploaded, setSelectedImageIsUploaded] =
    useState<boolean>(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (advertisementPictures.length + imageUrls.length < 5) return; // Solo avanza si hay al menos 5 imágenes
    setIsUploading(true);

    try {
      const urls = await Promise.all(
        advertisementPictures.map((file) => uploadFileToFirebase(file))
      );
      const allUrls = [...imageUrls, ...urls];
      setNextStage({ advertisement: { images: allUrls } });
    } catch (error) {
      console.error("Error uploading images:", error);
    } finally {
      setIsUploading(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      const filesArray = Array.from(e.target.files);
      setAdvertisementPictures((prevFilesArray) => {
        const availableSlots = 10 - (prevFilesArray.length + imageUrls.length);
        const filesToAdd = filesArray.slice(0, availableSlots);
        return [...prevFilesArray, ...filesToAdd];
      });
    }
  };

  const confirmRemoveImage = () => {
    if (selectedImageIndex === null) return;

    if (selectedImageIsUploaded) {
      setImageUrls((prev) => prev.filter((_, i) => i !== selectedImageIndex));
    } else {
      setAdvertisementPictures((prev) =>
        prev.filter((_, i) => i !== selectedImageIndex)
      );
    }

    // Reset confirm state
    setSelectedImageIndex(null);
    setSelectedImageIsUploaded(false);
    setShowConfirmDelete(false);
  };

  const FileUploadComponent = () => (
    <>
      <label
        htmlFor="advertisementPicturesInput"
        className="form-button px-6 flex items-center justify-center gap-4 max-w-80 bg-accent hover:bg-slate-800 text-white border-2 font-semibold cursor-pointer"
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

  const handleImageClick = (index: number, isUploaded: boolean) => {
    setSelectedImageIndex(index);
    setSelectedImageIsUploaded(isUploaded);
    setShowConfirmDelete(true);
  };

  return (
    <div className="flex flex-col space-y-4">
      <form className="w-10/12" onSubmit={handleSubmit}>
        <StageContainer title="Agrega algunas fotos de tu propiedad">
          <div className="flex flex-col items-center gap-6 h-full w-max m-auto">
            <FaCamera size={100} className="text-accent" />
            {advertisementPictures.length + imageUrls.length < 10 && (
              <FileUploadComponent />
            )}

            <p>
              {advertisementPictures.length + imageUrls.length > 0
                ? `${advertisementPictures.length + imageUrls.length
                } fotos seleccionadas`
                : "Selecciona al menos 5 fotos"}
            </p>

            {isUploading && <p className="text-accent">Subiendo imágenes...</p>}

            {(advertisementPictures.length > 0 || imageUrls.length > 0) && (
              <div className="grid grid-cols-3 gap-4 mt-4">
                {/* Previews de imágenes nuevas */}
                <AnimatePresence>
                  {advertisementPictures.map((file, idx) => (
                    <motion.div
                      key={`new-${idx}`}
                      layout
                      initial={{ opacity: 0, scale: 0.9 }}
                      animate={{ opacity: 1, scale: 1 }}
                      exit={{ opacity: 0, scale: 0.8 }}
                      transition={{ duration: 0.2 }}
                      className="relative group w-32 h-32 cursor-pointer"
                      onClick={() => handleImageClick(idx, false)}
                    >
                      <img
                        src={URL.createObjectURL(file)}
                        alt={`Nueva imagen ${idx + 1}`}
                        className="w-full h-full object-cover rounded"
                      />
                      <div className="absolute inset-0 bg-black/50 opacity-0 group-hover:opacity-100 transition-opacity rounded flex items-center justify-center text-white text-sm font-semibold">
                        Eliminar
                      </div>
                    </motion.div>
                  ))}

                  {imageUrls.map((url, idx) => (
                    <motion.div
                      key={`saved-${idx}`}
                      layout
                      initial={{ opacity: 0, scale: 0.9 }}
                      animate={{ opacity: 1, scale: 1 }}
                      exit={{ opacity: 0, scale: 0.8 }}
                      transition={{ duration: 0.2 }}
                      className="relative group w-32 h-32 cursor-pointer"
                      onClick={() => handleImageClick(idx, true)}
                    >
                      <img
                        src={url}
                        alt={`Imagen guardada ${idx + 1}`}
                        className="w-full h-full object-cover rounded"
                      />
                      <div className="absolute inset-0 bg-black/50 opacity-0 group-hover:opacity-100 transition-opacity rounded flex items-center justify-center text-white text-sm font-semibold">
                        Eliminar
                      </div>
                    </motion.div>
                  ))}
                </AnimatePresence>
              </div>
            )}
          </div>
        </StageContainer>

        <NavigationButtons
          onBack={setPrevStage}
          canContinue={
            advertisementPictures.length + imageUrls.length >= 5 && !isUploading
          }
        />
      </form>

      <AnimatePresence>
        {showConfirmDelete && (
          <motion.div
            className="fixed inset-0 bg-black/50 flex items-center justify-center z-50"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
          >
            <motion.div
              className="bg-white p-6 rounded-lg shadow-lg max-w-sm w-full text-center space-y-4"
              initial={{ scale: 0.8, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              exit={{ scale: 0.8, opacity: 0 }}
              transition={{ duration: 0.25 }}
            >
              <h3 className="text-lg font-semibold">¿Eliminar esta imagen?</h3>
              <p className="text-gray-600 text-sm">
                Esta acción no se puede deshacer.
              </p>
              <div className="flex justify-center gap-4 mt-4">
                <button
                  onClick={() => setShowConfirmDelete(false)}
                  className="form-button border-2 border-gray-400 text-gray-600 hover:bg-gray-100"
                >
                  Cancelar
                </button>
                <button
                  onClick={confirmRemoveImage}
                  className="form-button bg-red-600 text-white hover:bg-red-700"
                >
                  Eliminar
                </button>
              </div>
            </motion.div>
          </motion.div>
        )}
      </AnimatePresence>
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
    if (!advertisementTitle) return; // Solo avanza si hay título
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
          autoComplete="off"
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
    if (!advertisementDescription) return; // Solo avanza si hay descripción
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
    if (!(parseInt(area) > 0)) return; // Solo avanza si el área es mayor a 0
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
            <label
              htmlFor="bedroomsInput"
              className="block text-lg font-medium"
            >
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
            <label
              htmlFor="bathroomsInput"
              className="block text-lg font-medium"
            >
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
    if (!(advertisementPrice > 0)) return; // Solo avanza si el precio es mayor a 0
    setNextStage({ advertisement: { price: advertisementPrice } });
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/\./g, "");
    if (/^\d*$/.test(value)) {
      setAdvertisementPrice(Number(value));
    }
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

export const AdvertisementExtraInfoStage = () => {
  const { advertisement, setNextStage, setPrevStage } =
    useAdvertisementContext();
  const [extraInfo, setExtraInfo] = useState<string[]>(
    advertisement.extraInfo || []
  );
  const [newExtraInfo, setNewExtraInfo] = useState<string>("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (extraInfo.length <= 1) return; // Solo avanza si hay al menos 2 keypoints
    setNextStage({ advertisement: { extraInfo } });
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewExtraInfo(e.target.value);
  };

  const handleAddExtraInfo = () => {
    if (newExtraInfo.trim() !== "") {
      setExtraInfo((prev) => [...prev, newExtraInfo]);
      setNewExtraInfo("");
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      handleAddExtraInfo();
    }
  };

  const handleDeleteKeypoint = (e: React.MouseEvent) => {
    e.stopPropagation(); // Evitar que el evento se propague al contenedor padre
    const keypointToDelete = (e.target as HTMLButtonElement).closest("div")?.textContent;
    if (keypointToDelete) {
      setExtraInfo((prev) => prev.filter((keypoint) => keypoint !== keypointToDelete));
    }
  };

  return (
    <form className="flex flex-col w-10/12 gap-10" onSubmit={handleSubmit}>
      <StageContainer title="Puntos clave de tu propiedad">
        <div className="flex flex-col gap-3">{extraInfo.map(keypoint => (
          <div className="flex items-center justify-between w-xl border-2 border-neutral-400 rounded-md px-5 py-3" key={keypoint}>
            <h4 className="font-semibold text-neutral-800">{keypoint}</h4>
            <button type="button" className="text-neutral-500 hover:cursor-pointer hover:text-red-600 transition-all" onClick={handleDeleteKeypoint}><FaTrash /></button>
          </div>
        ))}
        </div>
        <div className="flex items-center gap-3 h-full w-max m-auto">
          <TextInput
            id="extraInfoInput"
            value={newExtraInfo}
            onChange={handleChange}
            onKeyDown={handleKeyDown}
            placeholder="Escribe un punto clave de tu propiedad"
          />
          <button
            type="button"
            onClick={handleAddExtraInfo}
            className="form-button aspect-square h-full p-3 bg-accent text-white hover:bg-slate-800"
          >
            <FaPlus />
          </button>
        </div>
      </StageContainer>
      <NavigationButtons onBack={setPrevStage} canContinue={extraInfo.length > 1} />
    </form>
  );
}

export const AdvertisementPreviewStage = () => {
  const { advertisement, setPrevStage, setAdvertisement } =
    useAdvertisementContext();
  const { user } = useUserContext();
  const location = useLocation();
  const { id } = useParams();
  const isEdit = location.pathname.startsWith("/edit/");

  const handleConfirm = () => {
    if (!user) {
      console.error("No hay usuario autenticado para asignar como owner.");
      return;
    }
    const advertisementWithOwnerAndStatus = {
      ...advertisement,
      owner: user,
      status: "pending" as const,
    };
    setAdvertisement(advertisementWithOwnerAndStatus);
    if (isEdit && id) {
      updateAdvertisement(id, advertisementWithOwnerAndStatus)
        .then((response) => {
          redirect("/advertisement/" + response.id);
        })
        .catch((error) => {
          console.error("Error al actualizar el anuncio:", error);
        });
    } else {
      publishAdvertisement(advertisementWithOwnerAndStatus)
        .then((response) => {
          redirect("/advertisement/" + response.id);
        })
        .catch((error) => {
          console.error("Error al publicar el anuncio:", error);
        });
    }
  };

  return (
    <section className="flex flex-col items-center w-10/12">
      <div className="flex items-center justify-between w-10/12 gap-6">
        <span className="text-neutral-600">Vista previa de tu anuncio</span>
        <div className="min-w-56">
          <div className="flex gap-4">
            <button
              type="button"
              onClick={setPrevStage}
              className="px-4 form-button max-w-40 hover:bg-black/10 text-accent border-2 border-accent font-semibold"
            >
              Atrás
            </button>
            <button
              type="button"
              className="min-w-max form-button px-4 bg-accent text-white hover:bg-slate-800"
              onClick={handleConfirm}
            >
              {isEdit ? "Publicar cambios" : "Publicar"}
            </button>
          </div>
        </div>
      </div>
      <AdvertisementPage advertisement={advertisement} demoPage />
    </section>
  );
};
