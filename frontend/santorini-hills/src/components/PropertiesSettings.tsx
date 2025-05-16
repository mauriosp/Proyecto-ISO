import { Link } from "react-router";
import { useUserContext } from "../context/user/UserContext";
import { formatNumber } from "../utils/parseNumbers";
import { ModalView, useModalContext } from "../context/modal/ModalContext";
import { listAdvertisements, getPropertyById } from "../utils/APICalls";
import { useEffect, useState } from "react";
import { AdvertisementDB } from "../models/advertisementDB";
import { PropertyDB } from "../models/propertyDB";

interface AdWithProperty {
  ad: AdvertisementDB;
  property: PropertyDB | null;
}

const PropertiesSettings = () => {
    const { openModal } = useModalContext();
    const { user } = useUserContext();
    const [adsWithProperties, setAdsWithProperties] = useState<AdWithProperty[]>([]);

    const handleClick = (view: ModalView) => {
        openModal(view);
    };

    useEffect(() => {
        async function fetchData() {
            try {
                const ads = await listAdvertisements();

                // Obtener propiedades asociadas a cada anuncio
                const adsWithProps: AdWithProperty[] = await Promise.all(
                    ads.map(async (ad) => {
                        try {
                            const property = await getPropertyById(ad.idEspacio);
                            return { ad, property };
                        } catch (error) {
                            console.error(`Error cargando propiedad del anuncio ${ad._id}`, error);
                            return { ad, property: null };
                        }
                    })
                );

                // Filtrar por propietario si no es admin
                const filtered = user?.profile === "admin"
                    ? adsWithProps
                    : adsWithProps.filter(item => item.property?.idPropietario === user?.id);

                setAdsWithProperties(filtered);
            } catch (error) {
                console.error("Error al cargar los anuncios:", error);
            }
        }

        fetchData();
    }, [user]);

    return (
        <div>
            <div className="flex flex-col gap-4">
                {adsWithProperties.map(({ ad, property }) => (
                    
                    <div key={ad.id} className="bg-white shadow-md rounded-lg flex gap-3">
                        <Link to={`/advertisements/${ad.id}`} className="w-1/3 h-48 overflow-hidden">
                            <img
                                src={ad.imagenes?.split(",")[0] || "/placeholder.jpg"}
                                alt={ad.titulo}
                                className="h-full w-full object-cover rounded-l-lg mb-4"
                            />
                        </Link>
                        <div className="flex flex-col justify-between flex-1 p-4">
                            <div>
                                <h2 className="text-xl font-semibold flex items-center gap-2">
                                    {ad.titulo}
                                </h2>
                                <p className="mt-1 text-gray-700 gap-2 overflow-hidden line-clamp-3">
                                    {ad.descripcion}
                                </p>
                                <div className="flex items-center gap-4 mt-2">
                                    <span className="flex items-center gap-1 text-green-600 font-semibold">
                                        ${formatNumber(ad.precio)}
                                    </span>
                                    <span className={`flex items-center gap-1 text-sm ${
                                        property?.estado === 'Disponible'
                                            ? 'text-accent'
                                            : property?.estado === 'Ocupado'
                                            ? 'text-red-500'
                                            : 'text-yellow-500'
                                    }`}>
                                        {property?.estado === 'Disponible'
                                            ? 'Disponible'
                                            : property?.estado === 'Ocupado'
                                            ? 'Ocupada'
                                            : 'Pendiente'}
                                    </span>
                                </div>
                            </div>
                            <div className="flex gap-2 mt-4">
                                <Link
                                    to={`/edit/${ad.id}`}
                                    className="flex items-center gap-1 px-3 py-1 bg-accent hover:bg-slate-800 hover:cursor-pointer text-white rounded transition"
                                >
                                    Editar
                                </Link>
                                <button
                                    onClick={() => handleClick("list")}
                                    className="flex items-center gap-1 px-3 py-1 bg-accent hover:bg-slate-800 hover:cursor-pointer text-white rounded transition"
                                >
                                    Mensajes
                                </button>
                                <button
                                    className="flex items-center gap-1 px-3 py-1 border-2 border-accent hover:bg-red-500 hover:cursor-pointer hover:border-red-700 hover:text-white rounded transition"
                                >
                                    Eliminar
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default PropertiesSettings;
