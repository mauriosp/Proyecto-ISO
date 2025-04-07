import { IconType } from "react-icons";
import { FaBuilding, FaHouse, FaHouseChimneyWindow, FaVectorSquare } from "react-icons/fa6";
import { IoBed } from "react-icons/io5";
import { PiFarmFill } from "react-icons/pi";
import { TbBuildingArch } from "react-icons/tb";
import { PropertyType } from "../models/property";

interface TipoInmueble {
    id: PropertyType;
    nombre: string;
    Icon: IconType;
}
export const tiposInmuebles:TipoInmueble[] = [
    { id: "house", nombre: "Casa", Icon: FaHouse },
    { id: "apartment", nombre: "Apartamento", Icon: FaBuilding },
    { id: "studio-apartment", nombre: "Apartaestudio", Icon: TbBuildingArch },
    { id: "cabin", nombre: "Cabaña", Icon: FaHouseChimneyWindow },
    { id: "lot", nombre: "Lote", Icon: FaVectorSquare},
    { id: "farm", nombre: "Finca", Icon: PiFarmFill},
    { id: "room", nombre: "Habitación", Icon: IoBed },
];