import { useEffect, useState } from "react";
import { useModalContext } from "../context/modal/ModalContext";
import { getAdvertisementById } from "../utils/APICalls"; // Ajusta el path si es necesario
import { responderMensaje } from "../utils/APICalls";

interface Mensaje {
  usuarioId: string;
  avisoId: string;
  contenido: string;
  fechaMensaje: string;
  estadoMensaje: boolean;
  respuestaMensaje?: {
    fechaRespuesta: string;
    contenido: string;
  };
}

export default function MessagesList() {
  const { currentAdId } = useModalContext();
  const [mensajes, setMensajes] = useState<Mensaje[]>([]);
  const [mensajeSeleccionado, setMensajeSeleccionado] = useState<number | null>(null);
  const [respuesta, setRespuesta] = useState("");

  // Obtener los mensajes del aviso al cargar el componente
  useEffect(() => {
    const fetchMensajes = async () => {
      if (!currentAdId) return;
      try {
        const aviso = await getAdvertisementById(currentAdId);
        setMensajes(aviso.mensaje || []);
      } catch (error) {
        console.error("Error al cargar los mensajes:", error);
      }
    };

    fetchMensajes();
  }, [currentAdId]);

  const handleResponder = async () => {
    if (mensajeSeleccionado === null || !respuesta.trim()) return;

    const index = mensajeSeleccionado;
    const avisoId = currentAdId;

    try {
      // Llamada real al backend para responder el mensaje
      await responderMensaje(avisoId, index, respuesta);

      // Actualización local para reflejar la respuesta inmediatamente en la UI
      const updated = [...mensajes];
      updated[index].respuestaMensaje = {
        contenido: respuesta,
        fechaRespuesta: new Date().toISOString(),
      };
      updated[index].estadoMensaje = true;
      setMensajes(updated);
      setRespuesta("");
    } catch (error) {
      console.error("Error al responder el mensaje:", error);
    }
  };


  return (
    <div className="space-y-6">
      <h3 className="text-xl font-semibold">Mensajes del aviso</h3>

      <ul className="space-y-2">
        {mensajes.map((msg, index) => (
          <li
            key={index}
            onClick={() => setMensajeSeleccionado(index)}
            className={`cursor-pointer p-3 rounded border ${
              mensajeSeleccionado === index ? "border-blue-500 bg-blue-50" : "border-gray-300"
            }`}
          >
            <div className="text-sm text-gray-600">📅 {new Date(msg.fechaMensaje).toLocaleString()}</div>
            <div className="text-sm font-medium truncate">📝 {msg.contenido}</div>
            <div className="text-xs text-gray-500">
              Estado: {msg.estadoMensaje ? "✅ Atendido" : "🕒 Pendiente"}
            </div>
          </li>
        ))}
      </ul>

      {mensajeSeleccionado !== null && (
        <div className="border-t pt-4 mt-4 space-y-2">
          <h4 className="font-semibold">Mensaje seleccionado</h4>
          <p>{mensajes[mensajeSeleccionado].contenido}</p>

          <div className="text-sm text-gray-600">
            Fecha: {new Date(mensajes[mensajeSeleccionado].fechaMensaje).toLocaleString()}
          </div>

          {mensajes[mensajeSeleccionado].respuestaMensaje ? (
            <div className="mt-2 p-3 bg-gray-100 rounded">
              <h5 className="font-semibold">Respuesta</h5>
              <p>{mensajes[mensajeSeleccionado].respuestaMensaje.contenido}</p>
              <div className="text-sm text-gray-500">
                Respondido el:{" "}
                {new Date(
                  mensajes[mensajeSeleccionado].respuestaMensaje.fechaRespuesta
                ).toLocaleString()}
              </div>
            </div>
          ) : (
            <div className="space-y-2 mt-2">
              <textarea
                placeholder="Escribe tu respuesta..."
                value={respuesta}
                onChange={(e) => setRespuesta(e.target.value)}
                className="w-full p-2 bg-white text-sm rounded border border-gray-300"
                rows={3}
              />
              <button
                onClick={handleResponder}
                disabled={!respuesta.trim()}
                className="form-button bg-green-600 text-white px-4 py-2 rounded disabled:opacity-50"
              >
                Responder mensaje
              </button>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
