import { useParams } from "react-router";
import { testAdvertisements } from "../testAdvertisements";
import AdvertisementPage from "../pages/AdvertisementPage";

const AdvertisementPageContainer = () => {
  const { id } = useParams<{ id: string }>();

  // Llamado a la API para obtener el anuncio por ID
  const advertisement = testAdvertisements.find((ad) => ad.id === Number(id)); // Simulación de la API

  if (!advertisement) {
    return <p className="text-center text-red-500">Anuncio no encontrado</p>;
  }

  return <AdvertisementPage advertisement={advertisement} />;
};

export default AdvertisementPageContainer;
