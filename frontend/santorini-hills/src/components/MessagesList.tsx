import { useState } from "react";

interface Mensaje {
  idUsuario: string;
  contenido: string;
  fechaMensaje: string;
  estadoMensaje: boolean;
  respuestaMensaje?: {
    fechaRespuesta: string;
    contenido: string;
  };
}

interface MessagesListProps {
  mensajes: Mensaje[];
  onResponder: (index: number, respuesta: string) => void;
}

export default function MessagesList({ mensajes, onResponder }: MessagesListProps) {
  const [mensajeSeleccionado, setMensajeSeleccionado] = useState<number | null>(null);
  const [respuesta, setRespuesta] = useState("");

  const handleResponder = () => {
    if (mensajeSeleccionado !== null) {
      onResponder(mensajeSeleccionado, respuesta);
      setRespuesta("");
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
                {new Date(mensajes[mensajeSeleccionado].respuestaMensaje.fechaRespuesta).toLocaleString()}
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
