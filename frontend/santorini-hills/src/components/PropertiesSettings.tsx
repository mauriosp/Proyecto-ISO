import { Link } from "react-router";
import { useUserContext } from "../context/user/UserContext";
import { testAdvertisements } from "../testAdvertisements";
import { formatNumber } from "../utils/parseNumbers";
import { ModalView, useModalContext } from "../context/modal/ModalContext";

const PropertiesSettings = () => {
    const { openModal } = useModalContext();
      const handleClick = (view:ModalView) => {
        openModal(view);
    };

    const { user } = useUserContext();
    // Mostrar todas las propiedades si el usuario es admin, solo las propias si no
    const properties = user?.profile === "admin"
        ? testAdvertisements
        : testAdvertisements.filter(ad => ad.owner?.id === user?.id); // Simulación de la API
    return (
        <div>
            <div className="flex flex-col gap-4">
                {properties.map((property) => (
                    <div key={property.id} className="bg-white shadow-md rounded-lg flex gap-3">
                        <Link to={`/advertisements/${property.id}`} className="w-1/3 h-48 overflow-hidden">
                            <img src={property.images[0]} alt={property.title} className="h-full w-full object-cover rounded-l-lg mb-4" />
                        </Link>
                        <div className="flex flex-col justify-between flex-1 p-4">
                            <div>
                                <h2 className="text-xl font-semibold flex items-center gap-2">
                                    {property.title}
                                </h2>
                                <p
                                    className="mt-1 text-gray-700 gap-2 overflow-hidden line-clamp-3"
                                >
                                    {property.description}
                                </p>
                                <div className="flex items-center gap-4 mt-2">
                                    <span className="flex items-center gap-1 text-green-600 font-semibold">
                                        ${formatNumber(property.price)}
                                    </span>
                                    <span className={`flex items-center gap-1 text-sm ${property.status === 'available' ? 'text-accent' : 'text-red-500'}`}>
                                        {property.status === 'available' ? 'Disponible' : property.status === 'taken' ? 'Ocupada' : 'Pendiente'}
                                    </span>
                                </div>
                            </div>
                            <div className="flex gap-2 mt-4">
                                <Link to={`/edit/${property.id}`} className="flex items-center gap-1 px-3 py-1 bg-accent hover:bg-slate-800 hover:cursor-pointer text-white rounded transition">
                                    Editar
                                </Link>
                                <button onClick={()=>handleClick("list")} className="flex items-center gap-1 px-3 py-1 bg-accent hover:bg-slate-800 hover:cursor-pointer text-white rounded transition">
                                    Mensajes
                                </button>
                                <button className="flex items-center gap-1 px-3 py-1 border-2 border-accent hover:bg-red-500 hover:cursor-pointer hover:border-red-700 hover:text-white rounded transition">
                                    Eliminar
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default PropertiesSettings