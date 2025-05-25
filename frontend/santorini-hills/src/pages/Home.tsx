import { useEffect, useState } from "react";
import { IoFilter } from "react-icons/io5";
import AdvertisementCard from "../components/AdvertisementCard";
import FilterModal from "../components/FilterModal";
import { listAdvertisements, getPropertyById } from "../utils/APICalls";
import { Advertisement } from "../models/advertisement";
import { advertisementFromDBWithProperty } from "../adapters/advertisementAdapter";

const Home = () => {
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const [advertisements, setAdvertisements] = useState<Advertisement[]>([]);
  const [loading, setLoading] = useState(true);
  const [filters, setFilters] = useState({
    type: "",
    minPrice: "",
    maxPrice: "",
    bedrooms: "",
    bathrooms: "",
  });

  useEffect(() => {
    const fetchAds = async () => {
      try {
        const adsFromBackend = await listAdvertisements();

        const adsWithProperties = await Promise.all(
          adsFromBackend.map(async (ad) => {
            const propertyId =
              typeof ad.idEspacio === "object" && ad.idEspacio?.$oid
                ? ad.idEspacio.$oid
                : ad.idEspacio;

            const property = await getPropertyById(propertyId);
            return advertisementFromDBWithProperty(ad, property);
          })
        );

        setAdvertisements(adsWithProperties);
      } catch (error) {
        console.error("Error al obtener avisos o propiedades:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchAds();
  }, []);

  const handleApplyFilters = (newFilters: typeof filters) => {
    setFilters(newFilters);
    setIsFilterOpen(false);
  };

  const filteredAds = advertisements.filter((ad) => {
    if (filters.type && ad.property?.type !== filters.type) return false;
    if (filters.minPrice && ad.price < Number(filters.minPrice)) return false;
    if (filters.maxPrice && ad.price > Number(filters.maxPrice)) return false;
    if (filters.bedrooms && ad.property?.bedrooms !== Number(filters.bedrooms)) return false;
    if (filters.bathrooms && ad.property?.bathrooms !== Number(filters.bathrooms)) return false;
    return true;
  });

  if (loading) {
    return <div className="text-center mt-10">Cargando avisos...</div>;
  }

  return (
    <div className="flex flex-col items-center justify-center w-10/12 mx-auto mt-10">
      <section className="w-full">
        <div className="flex items-center justify-between">
          <h2 className="font-semibold text-2xl">Destacados</h2>
          <button
            className="form-button border-2 border-accent px-4 text-accent hover:text-white hover:bg-accent"
            onClick={() => setIsFilterOpen(true)}
          >
            <div className="flex items-center gap-3">
              Filtros <IoFilter />
            </div>
          </button>
        </div>
        <div className="flex flex-wrap gap-10 mt-10 overflow-x-auto">
          {filteredAds.map((ad) => (
            <AdvertisementCard key={ad.id} advertisement={ad} />
          ))}
        </div>
      </section>

      {isFilterOpen && (
        <FilterModal
          filters={filters}
          onApply={handleApplyFilters}
          onClose={() => setIsFilterOpen(false)}
        />
      )}
    </div>
  );
};

export default Home;
