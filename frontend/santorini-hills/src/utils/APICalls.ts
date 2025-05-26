import axios from "axios";
import { AdvertisementDB } from "../models/advertisementDB";
import { Property } from "../models/property";
import { PropertyDB } from "../models/propertyDB";
import { User, userAdapter, userPresenter } from "../models/user";
import { Advertisement } from "../models/advertisement";

const API_URL = "http://localhost:8080/UAO/apirest/";

// GET usuario por id
export async function getUserById(id: string) {
  const res = await axios.get(`${API_URL}Usuario/buscar/${id}`);
  return userAdapter(res.data);
}

// GET propiedad por id
export async function getPropertyById(id: string): Promise<PropertyDB> {
  const res = await axios.get(`${API_URL}Espacio/buscar/${id}`);
  return {
    _id: res.data.id,
    idPropietario: res.data.idPropietario,
    tipoEspacio: res.data.tipoEspacio,
    direccion: res.data.direccion,
    estado: res.data.estado,
    area: res.data.area,
    habitaciones: res.data.habitaciones,
    baños: res.data.baños,
    promCalificacion: res.data.promCalificacion,
    arrendamiento: res.data.arrendamiento,
  };
}


// GET aviso por id
export async function getAdvertisementById(id: string): Promise<AdvertisementDB> {
  const res = await axios.get(`${API_URL}Aviso/buscar/${id}`);
  return res.data;
}

// GET lista de avisos (normalizando el _id)
export async function listAdvertisements(): Promise<AdvertisementDB[]> {
  const res = await axios.get(`${API_URL}Aviso/listar`);
  return res.data.map((ad: any) => ({
    ...ad,
    _id: typeof ad._id === "object" && ad._id.$oid ? ad._id.$oid : ad._id,
  }));
}


// GET lista de propiedades
export async function listProperties(): Promise<PropertyDB[]> {
  const res = await axios.get(`${API_URL}Espacio/listar`);
  return res.data;
}

// POST usuario
export async function createUser(user: User) {
  const userData = userPresenter(user);
  const res = await axios.post(`${API_URL}Usuario/registro`, userData);
  console.log("Usuario creado:", res.data);
  return res.data;
}

// POST login
export async function loginUser(email: string, password: string) {
  try {
    const res = await axios.post(
      `${API_URL}Usuario/login?email=${encodeURIComponent(email)}&contraseña=${encodeURIComponent(password)}`
    );
    return res.data;
  } catch (error: any) {
    const backendMessage = error.response?.data || 'Error al iniciar sesión';
    throw new Error(typeof backendMessage === 'string' ? backendMessage : 'Error al iniciar sesión');
  }
}

// POST propiedad
export async function createProperty(property: Property) {
  // Adaptar property a formato backend
  const propertyData = {
    idPropietario: property.owner?.id || "",
    tipo: property.type,
    direccion: property.address,
    area: property.area,
    caracteristicas: `${property.bedrooms || 0} habitaciones, ${property.bathrooms || 0} baño(s)`,
    tipoEspacio: property.type,
    estado: "Disponible",
    promCalificacion: 0,
    arrendamiento: [],
  };
  const res = await axios.post(`${API_URL}Espacio/insertar`, propertyData);
  return res.data;
}

// POST aviso (form-data)
export async function createAdvertisement(ad: Advertisement) {
  const formData = new FormData();
  formData.append("tipoEspacio", ad.property?.type || "");
  formData.append("descripcion", ad.description);
  formData.append("precioMensual", ad.price.toString());
  formData.append("extraInfo", ad.extraInfo.join(", "));
  ad.images.forEach((img) => formData.append("imagenes", img));
  formData.append("titulo", ad.title);
  formData.append("direccion", ad.property?.address || "");
  formData.append("area", ad.property?.area?.toString() || "");
  formData.append("idUsuario", ad.owner?.id || "");
  if (ad.property?.bedrooms) formData.append("habitaciones", ad.property.bedrooms.toString());
  if (ad.property?.bathrooms) formData.append("baños", ad.property.bathrooms.toString());
  // Si hay mensajes
  // formData.append("mensaje", JSON.stringify(ad.mensaje));
  const res = await axios.post(`${API_URL}Aviso/crear`, formData);
  return res.data;
}

// PUT aviso (form-data)
export async function updateAdvertisement(id: string, ad: Advertisement) {
  const formData = new FormData();
  formData.append("id", id); // Asegúrate de que el backend espera el ID en el body
  formData.append("tipoEspacio", ad.property?.type || "");
  formData.append("descripcion", ad.description);
  formData.append("precioMensual", ad.price.toString());
  formData.append("extraInfo", ad.extraInfo.join(", "));
  ad.images.forEach((img) => formData.append("imagenes", img));
  formData.append("titulo", ad.title);
  formData.append("direccion", ad.property?.address || "");
  formData.append("area", ad.property?.area?.toString() || "");
  formData.append("idUsuario", ad.owner?.id || "");
  if (ad.property?.bedrooms) formData.append("habitaciones", ad.property.bedrooms.toString());
  if (ad.property?.bathrooms) formData.append("baños", ad.property.bathrooms.toString());
  
  const res = await axios.put(`${API_URL}Aviso/editar/${id}`, formData);
  return res.data;
}

export async function sendMessage(usuarioId: string, avisoId: string, contenido: string) {
  const formData = new FormData();
  formData.append("usuarioId", usuarioId);
  formData.append("avisoId", avisoId);
  formData.append("contenido", contenido);

  const res = await axios.post(`${API_URL}Notificaciones/enviar`, formData);
  return res.data;
}

export async function getMessagesByUserAndAd(usuarioId: string, avisoId: string) {
  const res = await axios.get(`${API_URL}Notificaciones/usuario/${usuarioId}/aviso/${avisoId}`);
  return res.data;
}

export async function responderMensaje(avisoId: string, indiceMensaje: number, contenido: string) {
  const payload = { contenido };

  const res = await axios.post(
    `${API_URL}Notificaciones/aviso/${avisoId}/mensaje/${indiceMensaje}/responder`,
    payload,
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  return res.data;
}

export interface Reporte {
  id?: string; // El backend usa ObjectId, lo manejamos como string aquí
  descripcion: string;
  idAviso: string;
  idUsuario: string;
  motivoEReporte: string;
  comentarioAdicional: string;
  fechaReporte?: string; // opcional si el backend lo autogenera
  estado?: string;       // opcional si el backend lo asigna
}

export async function crearReporte(reporte: Reporte): Promise<Reporte> {
  const res = await axios.post(`${API_URL}reportes`, reporte, {
    headers: {
      "Content-Type": "application/json",
    },
  });
  return res.data;
}

export async function eliminarCuenta(id: string): Promise<string> {
  try {
    const response = await axios.delete(`${API_URL}Usuario/eliminar/${id}`);
    return response.data; // "Cuenta eliminada correctamente"
  } catch (error: any) {
    if (error.response && error.response.data) {
      throw new Error(error.response.data); // Error controlado desde el backend
    } else {
      throw new Error("Error al conectar con el servidor.");
    }
  }
}

interface PerfilUpdatePayload {
  id: string;
  nombre?: string;
  telefono?: string;
  fotoPerfil?: string; // ya es URL string, no archivo
}

export const actualizarPerfil = async ({ id, nombre, telefono, fotoPerfil }: PerfilUpdatePayload) => {
  const formData = new FormData();
  if (nombre) formData.append("nombre", nombre);
  if (telefono) formData.append("telefono", telefono);
  if (fotoPerfil) formData.append("fotoPerfil", fotoPerfil); // string

  return await axios.put(
    `${API_URL}Usuario/actualizarPerfil/${id}`,
    formData
  );
};

export async function getAllReportes() {
  const res = await axios.get(`${API_URL}reportes`);
  return res.data; // o res.data.map(reporteAdapter) si usas un adapter
}

export async function getReporteById(id: string) {
  const res = await axios.get(`${API_URL}reportes/${id}`);
  return res.data; // o reporteAdapter(res.data)
}

export async function updateReporteEstado(id: string, estado: string) {
  const res = await axios.put(`${API_URL}reportes/${id}/estado`, null, {
    params: { estado },
  });
  return res.data; // o reporteAdapter(res.data)
}

export async function deleteReporte(id: string) {
  await axios.delete(`${API_URL}reportes/${id}`);
}

export async function getReportesByAviso(idAviso: string) {
  const res = await axios.get(`${API_URL}reportes/aviso/${idAviso}`);
  return res.data; // o res.data.map(reporteAdapter)
}

export async function getMensajesByUsuario(usuarioId: string) {
  const res = await axios.get(`${API_URL}Notificaciones/usuario/${usuarioId}/todos`);
  return res.data;
}

export async function enviarCorreoVerificacion(userId: string, email: string) {
  try {
    const res = await axios.post(
      `${API_URL}VerificacionEmail/enviar?userId=${userId}&email=${encodeURIComponent(email)}`
    );
    return res.data;
  } catch (error) {
    throw new Error("Ha ocurrido un error inesperado: ");
  }
}
