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
export async function getPropertyById(id: string): Promise<Property> {
  const res = await axios.get(`${API_URL}Espacio/buscar/${id}`);
  // Aquí puedes importar y usar propertyFromDB si necesitas composición
  // pero para evitar dependencias circulares, solo retorna el objeto plano
  return {
    id: res.data._id,
    type: res.data.tipoEspacio === "Apartamento" ? "apartment" :
          res.data.tipoEspacio === "Casa" ? "house" :
          res.data.tipoEspacio === "Habitación" ? "room" :
          res.data.tipoEspacio === "Bodega" ? "cabin" :
          res.data.tipoEspacio === "Parqueo" ? "lot" : "apartment",
    location: null,
    address: res.data.direccion,
    area: res.data.area,
    bathrooms: 0,
    bedrooms: 0,
    // El owner se compone en el presenter
  };
}

// GET aviso por id
export async function getAdvertisementById(id: string): Promise<AdvertisementDB> {
  const res = await axios.get(`${API_URL}Aviso/buscar/${id}`);
  return res.data;
}

// GET lista de avisos
export async function listAdvertisements(): Promise<AdvertisementDB[]> {
  const res = await axios.get(`${API_URL}Aviso/listar`);
  return res.data;
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
  return res.data;
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

