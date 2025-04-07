import { useParams } from "react-router";
import testAdvertisements from "../testAdvertisements";
import { useUserContext } from "../context/user/UserContext";

const AdvertisementPage: React.FC = () => {
  const { id } = useParams<{ id: string }>(); // Obtener el ID desde la URL
  const advertisement = testAdvertisements.find((ad) => ad.id === Number(id)); // Buscar el anuncio

  const { user } = useUserContext();
  const isOwner = advertisement?.owner?.email === user?.email;

  if (!advertisement) {
    return <p className="text-center text-red-500">Anuncio no encontrado</p>;
  }



  return (
    <div className="flex flex-col items-center justify-center w-10/12 mx-auto mt-10">
      {advertisement.title}
      {isOwner && (<button className="form-button bg-accent text-white font-semibold hover:bg-slate-800">Editar</button>)}
    </div>
  );
};

export default AdvertisementPage;