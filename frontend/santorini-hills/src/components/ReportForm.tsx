import { useState } from "react";

interface ReportFormProps {
  idAviso: string;
  idUsuario: string;
}

const motivosPredefinidos = [
  "Información falsa",
  "Precio engañoso",
  "Contenido inapropiado",
  "Duplicado",
  "Propiedad ya no disponible",
];

export default function ReportForm({ idAviso, idUsuario }: ReportFormProps) {
  const [motivo, setMotivo] = useState("");
  const [otroMotivo, setOtroMotivo] = useState("");
  const [detalles, setDetalles] = useState("");

  const handleMotivoClick = (value: string) => {
    setMotivo(value);
    if (value !== "Otro") setOtroMotivo("");
  };

  const motivoFinal = motivo === "Otro" ? otroMotivo : motivo;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const jsonPreview = {
      idAviso,
      idUsuario,
      descripcion: detalles, // puedes agregar lógica para esto si lo usas
      motivoReporte: motivoFinal,
      comentarioAdicional: null,
      fechaReporte: new Date().toISOString(),
      estado: "abierto",
    };

    console.log(JSON.stringify(jsonPreview, null, 2));
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="flex justify-center">
        <h3 className="text-lg font-semibold">¿Por qué estás reportando este aviso?</h3>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
        {motivosPredefinidos.map((m) => (
          <button
            key={m}
            type="button"
            onClick={() => handleMotivoClick(m)}
            className={`form-button font-semibold px-4 py-2 rounded transition-colors shadow-sm ${
              motivo === m ? "bg-accent text-white shadow-md" : "bg-white text-accent hover:bg-accent hover:text-white"
            }`}
          >
            {m}
          </button>
        ))}

        <button
          type="button"
          onClick={() => handleMotivoClick("Otro")}
          className={`form-button font-semibold px-4 py-2 rounded transition-colors shadow-sm ${
            motivo === "Otro" ? "bg-accent text-white shadow-md" : "bg-white text-accent hover:bg-accent hover:text-white"
          }`}
        >
          Otro
        </button>
      </div>

      {motivo === "Otro" && (
        <input
          type="text"
          placeholder="Especifica el motivo"
          value={otroMotivo}
          onChange={(e) => setOtroMotivo(e.target.value)}
          className="w-full p-2 bg-white text-sm rounded border border-gray-300"
        />
      )}

      <textarea
        placeholder="Descripción"
        value={detalles}
        onChange={(e) => setDetalles(e.target.value)}
        className="w-full p-2 bg-white text-sm rounded border border-gray-300"
        rows={4}
      />

      <div className="flex justify-center">
        <button
          type="submit"
          disabled={!motivoFinal.trim() || !detalles.trim()}
          className="form-button bg-red-600 text-white px-6 py-2 font-semibold w-full max-w-xs disabled:opacity-50"
        >
          Enviar reporte
        </button>
      </div>
    </form>
  );
}
