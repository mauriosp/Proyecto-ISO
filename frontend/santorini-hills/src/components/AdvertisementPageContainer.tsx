import { useParams } from "react-router";
import { useEffect, useState } from "react";
import { getAdvertisementById } from "../utils/APICalls"; // Asegúrate de tener esta función para hacer la llamada a la API
import AdvertisementPage from "../pages/AdvertisementPage";
import { Advertisement } from "../models/advertisement"; // El modelo adecuado para un "advertisement"
import { advertisementFromDBWithProperty } from "../adapters/advertisementAdapter"; // Asegúrate de tener esto
import { getPropertyById } from "../utils/APICalls"; // Asegúrate de tener esta función para hacer la llamada a la API


const AdvertisementPageContainer = () => {
  const { id } = useParams<{ id: string }>();
  
  // Estado para manejar los datos y el estado de carga
  const [advertisement, setAdvertisement] = useState<Advertisement | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Llamado a la API para obtener el anuncio por ID
  useEffect(() => {
    const fetchAdvertisement = async () => {
      try {
        const response = await getAdvertisementById(id); // Obtén el anuncio
        const propertyResponse = await getPropertyById(response.idEspacio); // Obtén la propiedad usando el idEspacio
        
        // Adapta el anuncio con la propiedad
        const adaptedAdvertisement = advertisementFromDBWithProperty(response, propertyResponse);
        setAdvertisement(adaptedAdvertisement);
      } catch (error) {
        setError("Anuncio no encontrado o error al cargar los datos.");
      } finally {
        setLoading(false);
      }
    };

    fetchAdvertisement();
  }, [id]);

  if (loading) {
    return <p className="text-center">Cargando...</p>;
  }

  if (error) {
    return <p className="text-center text-red-500">{error}</p>;
  }

  if (!advertisement) {
    return <p className="text-center text-red-500">Anuncio no encontrado</p>;
  }

  return <AdvertisementPage advertisement={advertisement} />;
};

export default AdvertisementPageContainer;
