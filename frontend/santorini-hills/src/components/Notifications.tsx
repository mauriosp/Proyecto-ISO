import { useEffect, useState } from "react";
import { getMensajesByUsuario } from "../utils/APICalls";

const Notifications = () => {
  const [mensajes, setMensajes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [mensajeAbierto, setMensajeAbierto] = useState<number | null>(null);

  useEffect(() => {
    const fetchMensajes = async () => {
      const storedUser = localStorage.getItem("loggedUser");
      const usuarioId = storedUser ? JSON.parse(storedUser).id : "";

      if (!usuarioId) {
        console.warn("No hay usuario logueado.");
        setLoading(false);
        return;
      }

      try {
        const data = await getMensajesByUsuario(usuarioId);
        setMensajes(data);
      } catch (error) {
        console.error("Error al obtener los mensajes:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchMensajes();
  }, []);

  const toggleMensaje = (index: number) => {
    setMensajeAbierto(prev => (prev === index ? null : index));
  };

  if (loading) {
    return <p>Cargando notificaciones...</p>;
  }

  if (mensajes.length === 0) {
    return <p>No tienes notificaciones.</p>;
  }

  return (
    <div className="w-full p-4">
      <ul className="space-y-3 w-full">
        {mensajes.map((mensaje: any, index: number) => {
          const abierto = mensajeAbierto === index;
          const contenidoRespuesta = mensaje.respuestaMensaje?.contenido ?? "Mensaje sin contenido";
          const contenidoOriginal = mensaje.contenido ?? "Mensaje sin contenido";
          const fecha = mensaje.respuestaMensaje?.fechaRespuesta
            ? new Date(mensaje.respuestaMensaje.fechaRespuesta).toLocaleString()
            : "Fecha no disponible";

          return (
            <li
              key={index}
              className={`bg-white shadow rounded p-4 border-l-4 border-blue-500 cursor-pointer w-full transition-all duration-300 ${
                abierto ? "bg-blue-50" : ""
              }`}
              onClick={() => toggleMensaje(index)}
            >
              <div className="flex flex-col gap-1">
                <p className="text-sm text-blue-600 font-semibold truncate">
                  {abierto ? contenidoRespuesta : contenidoOriginal}
                </p>
                <p className="text-xs text-gray-500">{fecha}</p>
              </div>

              {abierto && (
                <div className="mt-4 flex flex-col gap-3">
                  <p className="text-gray-700 text-sm whitespace-pre-wrap">{contenidoRespuesta}</p>
                  <button
                    className="w-full px-4 py-2 text-sm bg-blue-600 text-white rounded hover:bg-blue-700 transition"
                    onClick={(e) => {
                      e.stopPropagation();
                      alert("Contrato de arrendamiento abierto.");
                    }}
                  >
                    Arrendar
                  </button>

                </div>
              )}
            </li>
          );
        })}

      </ul>
    </div>
  );
};

export default Notifications;
