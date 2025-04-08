import { useParams } from "react-router";
import testAdvertisements from "../testAdvertisements";
import ImageGallery from "../components/ImageGallery";
import PropertyFeatures from "../components/PropertyFeature";
import ContactBox from "../components/ContactBox";
import CommentsSection from "../components/CommentSection";
import AdvertisementCard from "../components/AdvertisementCard";

const AdvertisementPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const advertisement = testAdvertisements.find((ad) => ad.id === Number(id));

  if (!advertisement) {
    return <p className="text-center text-red-500">Anuncio no encontrado</p>;
  }

  return (
    <div className="max-w-6xl mx-auto p-4 flex flex-col gap-4 mt-5">
      <ImageGallery imageUrls={advertisement.images} />

      <div className="flex gap-10 mt-4">
        {/* left panel */}
        <div className="flex flex-col flex-1 gap-4">
          <section>
            <h1 className="text-3xl font-semibold">{advertisement.title}</h1>
            <PropertyFeatures
              bedrooms={advertisement.property?.bedrooms}
              bathrooms={advertisement.property?.bathrooms}
              area={advertisement.property?.area || 0}
            />
            <p className="mt-4">{advertisement.description}</p>
          </section>
          <section>
            <h2 className="text-3xl font-medium mt-4">Ubicación</h2>
            <div className="mt-4 rounded-xl h-64 bg-green-400"></div>
          </section>
          <section className="w-full">
  <h2 className="text-3xl font-medium mt-4">
    Quizás te pueda interesar
  </h2>
  <div className="relative w-full mt-4">
    <div className="absolute w-full overflow-x-scroll pb-4 hide-scrollbar">
      <div className="flex gap-4" style={{ width: "max-content" }}>
        {testAdvertisements
          .filter(ad => ad.id !== Number(id)) // Filtrar el anuncio actual
          .slice(0, 6) // Limitar a 6 recomendaciones
          .map((ad) => (
            <div className="w-[280px] flex-shrink-0" key={ad.id}>
              <AdvertisementCard advertisement={ad} />
            </div>
          ))}
      </div>
    </div>
  </div>
  {/* Espaciador para mantener la altura correcta */}
  <div className="h-[350px]"></div>
</section>
        </div>

        {/* Right panel */}
        <div className="flex flex-col gap-8 w-1/3">
          <ContactBox price={advertisement.price} />
          <CommentsSection />
        </div>
      </div>
    </div>
  );
};

export default AdvertisementPage;
