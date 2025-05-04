import { useState } from "react";
import { IoFilter } from "react-icons/io5";
import AdvertisementCard from "../components/AdvertisementCard";
import { testAdvertisements } from "../testAdvertisements";
import FilterModal from "../components/FilterModal";

const Home = () => {
  const [isFilterOpen, setIsFilterOpen] = useState(false);
  const [filters, setFilters] = useState({
    type: "",
    minPrice: "",
    maxPrice: "",
    bedrooms: "",
    bathrooms: "",
  });

  const handleApplyFilters = (newFilters: typeof filters) => {
    setFilters(newFilters);
    setIsFilterOpen(false);
  };

  const filteredAds = testAdvertisements.filter((ad) => {
    if (filters.type && ad.property?.type !== filters.type) return false;
    if (filters.minPrice && ad.price < Number(filters.minPrice)) return false;
    if (filters.maxPrice && ad.price > Number(filters.maxPrice)) return false;
    if (filters.bedrooms && ad.property?.bedrooms !== Number(filters.bedrooms)) return false;
    if (filters.bathrooms && ad.property?.bathrooms !== Number(filters.bathrooms)) return false;
    return true;
  });

  return (
    <div className="flex flex-col items-center justify-center w-10/12 mx-auto mt-10">
      <section className="w-full">
        <div className="flex items-center justify-between">
          <h2 className="font-semibold text-2xl">Destacados</h2>
          <div>
            <button
              className="form-button border-2 border-accent px-4 text-accent hover:text-white hover:bg-accent"
              onClick={() => setIsFilterOpen(true)}
            >
              <div className="flex items-center gap-3">Filtros <IoFilter /></div>
            </button>
          </div>
        </div>
        <div className="flex flex-wrap gap-10 mt-10 overflow-x-auto">
          {filteredAds.map((advertisement) => (
            <AdvertisementCard key={advertisement.id} advertisement={advertisement} />
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
