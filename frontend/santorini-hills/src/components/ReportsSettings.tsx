import { useEffect, useState } from "react";
import { getAllReportes, updateReporteEstado, getAdvertisementById } from "../utils/APICalls";
import { reporteDB } from "../models/reporteDB"; // Asegúrate de que este modelo tenga los campos correctos
import { AdvertisementDB } from "../models/advertisementDB";
import { Link } from "react-router-dom";
import { formatNumber } from "../utils/parseNumbers";

interface ReporteExtendido {
  reporte: reporteDB;
  aviso: AdvertisementDB | null;
}

const ReportsSettings = () => {
  const [reportesExtendidos, setReportesExtendidos] = useState<ReporteExtendido[]>([]);

  const fetchReportes = async () => {
    try {
      const reportes = await getAllReportes();

      const reportesConAvisos: ReporteExtendido[] = await Promise.all(
        reportes.map(async (reporte: reporteDB) => {
          try {
            console.log(reporte.idAviso)
            const aviso = await getAdvertisementById(reporte.idAviso);
            return { reporte, aviso };
          } catch (e) {
            console.error(`No se pudo cargar el aviso ${reporte.idAviso}`);
            return { reporte, aviso: null };
          }
        })
      );

      setReportesExtendidos(reportesConAvisos);
    } catch (error) {
      console.error("Error al cargar los reportes:", error);
    }
  };

  useEffect(() => {
    fetchReportes();
  }, []);

  const handleEstadoChange = async (id: string, estado: string) => {
    try {
      await updateReporteEstado(id, estado);
      fetchReportes();
    } catch (error) {
      console.error(`Error al actualizar estado del reporte ${id}`, error);
    }
  };

  return (
    <div>
      <h2 className="text-2xl font-bold mb-4">Reportes</h2>
      <div className="flex flex-col gap-4">
        {reportesExtendidos.map(({ reporte, aviso }) => (
          <div key={reporte.id} className="bg-white shadow-md rounded-lg flex gap-4 p-4">
            {aviso && (
              <Link
                to={`/advertisements/${aviso.id}`}
                className="w-1/3 h-36 overflow-hidden rounded-lg"
              >
                <img
                  src={aviso.imagenes?.split(",")[0] || "/placeholder.jpg"}
                  alt={aviso.titulo}
                  className="w-full h-full object-cover rounded-lg"
                />
              </Link>
            )}

            <div className="flex flex-col justify-between flex-1">
              <div>
                <h3 className="text-lg font-semibold">{reporte.motivoEReporte}</h3>
                <p className="text-gray-700">{reporte.descripcion}</p>
                <p className="text-sm text-gray-500 mt-1">
                  Estado actual: <span className="font-semibold capitalize">{reporte.estado}</span>
                </p>

                {aviso && (
                  <div className="mt-2">
                    <p className="text-md font-medium">{aviso.titulo}</p>
                    <p className="text-green-600 font-semibold">${formatNumber(aviso.precio)}</p>
                  </div>
                )}
              </div>

              <div className="flex gap-2 mt-4">
                <button
                  onClick={() => handleEstadoChange(reporte.id, "Valido")}
                  className="px-3 py-1 bg-green-600 hover:bg-green-700 text-white rounded transition"
                >
                  Aceptar
                </button>
                <button
                  onClick={() => handleEstadoChange(reporte.id, "Invalido")}
                  className="px-3 py-1 bg-red-600 hover:bg-red-700 text-white rounded transition"
                >
                  Rechazar
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ReportsSettings;
