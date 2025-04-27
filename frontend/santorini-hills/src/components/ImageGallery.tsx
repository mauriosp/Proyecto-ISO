import React, { useState, useEffect } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCircleChevronLeft,
  faCircleChevronRight,
  faCircleXmark,
} from "@fortawesome/free-solid-svg-icons";

interface ImageGalleryProps {
  imageUrls: string[];
}

const ImageGallery: React.FC<ImageGalleryProps> = ({ imageUrls }) => {
  const [slideNumber, setSlideNumber] = useState<number>(0);
  const [openModal, setOpenModal] = useState<boolean>(false);

  // Deshabilita el scroll del body cuando el modal está abierto
  useEffect(() => {
    if (openModal) {
      document.body.style.overflow = "hidden";
    } else {
      document.body.style.overflow = "unset";
    }
    return () => {
      document.body.style.overflow = "unset";
    };
  }, [openModal]);

  // Abre el modal y establece la imagen actual
  const handleOpenModal = (index: number) => {
    setSlideNumber(index);
    setOpenModal(true);
  };

  // Cierra el modal
  const handleCloseModal = () => {
    setOpenModal(false);
  };

  // Navega a la imagen anterior (navegación circular)
  const prevSlide = () => {
    if (slideNumber === 0) {
      setSlideNumber(imageUrls.length - 1);
    } else {
      setSlideNumber(slideNumber - 1);
    }
  };

  // Navega a la imagen siguiente (navegación circular)
  const nextSlide = () => {
    if (slideNumber + 1 === imageUrls.length) {
      setSlideNumber(0);
    } else {
      setSlideNumber(slideNumber + 1);
    }
  };

  return (
    <>
      {/* Galería de imágenes en grilla */}
      <div className="grid grid-cols-4 grid-rows-2 gap-2 h-[560px]">
        <div
          className="col-span-2 row-span-2 flex items-center justify-center hover:cursor-pointer"
          onClick={() => handleOpenModal(0)}
        >
          <img
            src={imageUrls[0]}
            alt="Imagen principal"
            className="h-full object-cover rounded-l-2xl"
          />
        </div>
        <div className="hover:cursor-pointer" onClick={() => handleOpenModal(1)}>
          <img
            src={imageUrls[1]}
            alt="Imagen 2"
            className="h-full object-cover"
          />
        </div>
        <div className="hover:cursor-pointer" onClick={() => handleOpenModal(2)}>
          <img
            src={imageUrls[2]}
            alt="Imagen 3"
            className="h-full object-cover rounded-tr-2xl"
          />
        </div>
        <div className="hover:cursor-pointer" onClick={() => handleOpenModal(3)}>
          <img
            src={imageUrls[3]}
            alt="Imagen 4"
            className="h-full object-cover"
          />
        </div>
        <div
          className="hover:cursor-pointer relative"
          onClick={() => handleOpenModal(4)}
        >
          {imageUrls.length > 5 && (
            <div className="absolute inset-0 z-10 flex flex-col items-center justify-center gap-2 bg-black/50 rounded-br-2xl p-4">
              <h3 className="text-7xl font-semibold text-center text-white">
                +{imageUrls.length - 5}
              </h3>
              <p className="text-xl font-semibold text-center text-white">
                Ver más
              </p>
            </div>
          )}
          <img
            src={imageUrls[4]}
            alt="Imagen 5"
            className="h-full object-cover rounded-br-2xl"
          />
        </div>
      </div>

      {/* Modal de visualización */}
      {openModal && (
        <div className="fixed inset-0 z-50 flex flex-col items-center justify-center bg-black/90">
          {/* Botón para cerrar */}
          <FontAwesomeIcon
            icon={faCircleXmark}
            className="absolute top-8 right-8 text-white text-3xl cursor-pointer opacity-60 hover:opacity-100"
            onClick={handleCloseModal}
          />
          {/* Botón para imagen anterior - más centrado */}
          <FontAwesomeIcon
            icon={faCircleChevronLeft}
            className="absolute left-1/9 top-1/2 -translate-y-1/2 text-white text-3xl cursor-pointer opacity-60 hover:opacity-100"
            onClick={prevSlide}
          />
          {/* Botón para imagen siguiente - más centrado */}
          <FontAwesomeIcon
            icon={faCircleChevronRight}
            className="absolute right-1/9 top-1/2 -translate-y-1/2 text-white text-3xl cursor-pointer opacity-60 hover:opacity-100"
            onClick={nextSlide}
          />
          <div className="max-h-[80vh] max-w-full flex flex-col items-center justify-center">
            <img
              src={imageUrls[slideNumber]}
              alt={`Imagen ${slideNumber + 1}`}
              className="object-contain max-h-[80vh] max-w-full"
            />
            {/* Contador de imágenes */}
            <div className="mt-4 text-white">
              Imagen {slideNumber + 1} de {imageUrls.length}
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default ImageGallery;
