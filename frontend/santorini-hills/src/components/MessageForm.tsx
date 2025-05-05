import { useState } from "react";

interface MessageFormProps {
  idAviso: string;
  idUsuario: string;
}

export default function MessageForm({ idAviso, idUsuario }: MessageFormProps) {
  const [mensaje, setMensaje] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const jsonPreview = {
      idAviso,
      idUsuario,
      mensaje,
      fechaMensaje: new Date().toISOString(),
    };

    console.log("📤 JSON del mensaje:");
    console.log(JSON.stringify(jsonPreview, null, 2));

    // Opcional: limpiar campo
    setMensaje("");
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="flex justify-center">
        <h3 className="text-lg font-semibold">Escribe tu mensaje</h3>
      </div>

      <textarea
        placeholder="Escribe tu mensaje aquí..."
        value={mensaje}
        onChange={(e) => setMensaje(e.target.value)}
        className="w-full p-2 bg-white text-sm rounded border border-gray-300"
        rows={4}
      />

      <div className="flex justify-center">
        <button
          type="submit"
          disabled={!mensaje.trim()}
          className="form-button bg-accent text-white px-6 py-2 font-semibold w-full max-w-xs hover:bg-slate800 disabled:opacity-50"
        >
          Enviar mensaje
        </button>
      </div>
    </form>
  );
}
