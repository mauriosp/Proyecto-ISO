import { useState } from "react";
import { useParams } from "react-router-dom";
import { sendMessage } from "../utils/APICalls";
import { useModalContext } from "../context/modal/ModalContext";

export default function MessageForm() {
  const [contenido, setContenido] = useState("");
  const [mensajeEnviado, setMensajeEnviado] = useState(false);

  const { id } = useParams<{ id: string }>();
  const avisoId = id || "";

  const storedUser = localStorage.getItem("loggedUser");
  const usuarioId = storedUser ? JSON.parse(storedUser).id : "";

  const { closeModal } = useModalContext();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    console.log("📤 Enviando mensaje:", {
      usuarioId,
      avisoId,
      contenido,
    });

    try {
      await sendMessage(usuarioId, avisoId, contenido);
      console.log("✅ Mensaje enviado correctamente.");
      setContenido("");
      setMensajeEnviado(true);

      // Esperar 2 segundos y cerrar el modal
      setTimeout(() => {
        closeModal();
      }, 1500);
    } catch (error) {
      console.error("❌ Error enviando el mensaje:", error);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="flex justify-center">
        <h3 className="text-lg font-semibold">Escribe tu mensaje</h3>
      </div>

      <textarea
        placeholder="Escribe tu mensaje aquí..."
        value={contenido}
        onChange={(e) => setContenido(e.target.value)}
        className="w-full p-2 bg-white text-sm rounded border border-gray-300"
        rows={4}
      />

      <div className="flex justify-center">
        <button
          type="submit"
          disabled={!contenido.trim()}
          className="form-button bg-accent text-white px-6 py-2 font-semibold w-full max-w-xs hover:bg-slate800 disabled:opacity-50"
        >
          Enviar mensaje
        </button>
      </div>

      {mensajeEnviado && (
        <p className="text-center text-green-600 font-medium">
          ✅ Mensaje enviado. Cerrando...
        </p>
      )}
    </form>
  );
}
