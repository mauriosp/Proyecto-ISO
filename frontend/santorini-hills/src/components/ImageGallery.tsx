import React from "react";

interface ImageGalleryProps {
  imageUrls: string[];
}

const ImageGallery: React.FC<ImageGalleryProps> = ({ imageUrls }) => {
  return (
    <div className="grid grid-cols-4 grid-rows-2 gap-2 h-[560px]">
      <div className="col-span-2 row-span-2 flex items-center justify-center">
        <img
          src={imageUrls[0]}
          alt="Imagen principal"
          className="h-full object-cover rounded-l-2xl"
        />
      </div>
      <div>
        <img src={imageUrls[1]} alt="Imagen 2" className="h-full object-cover" />
      </div>
      <div>
        <img
          src={imageUrls[2]}
          alt="Imagen 3"
          className="h-full object-cover rounded-tr-2xl"
        />
      </div>
      <div>
        <img src={imageUrls[3]} alt="Imagen 4" className="h-full object-cover" />
      </div>
      <button className="hover:cursor-pointer relative">
        {imageUrls.length > 5 && (
          <div className="text-white bg-black/50 z-10 size-full absolute rounded-br-2xl flex flex-col items-center justify-center gap-2 p-4">
            <h3 className="text-7xl font-semibold text-center pt-2">
              +{imageUrls.length - 5}
            </h3>
            <p className="text-xl font-semibold text-center">Ver más</p>
          </div>
        )}
        <img
          src={imageUrls[4]}
          alt="Imagen 5"
          className="h-full object-cover rounded-br-2xl"
        />
      </button>
    </div>
  );
};

export default ImageGallery;
