import React from "react";
import { tiposInmuebles } from "../assets/tiposInmuebles";
import { IoClose } from "react-icons/io5";

interface FilterModalProps {
    filters: {
        type: string;
        minPrice: string;
        maxPrice: string;
        bedrooms: string;
        bathrooms: string;
    };
    onApply: (filters: FilterModalProps["filters"]) => void;
    onClose: () => void;
}

const FilterModal: React.FC<FilterModalProps> = ({ filters, onApply, onClose }) => {
    const [localFilters, setLocalFilters] = React.useState(filters);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setLocalFilters((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onApply(localFilters);
    };

    const handleClear = () => {
        setLocalFilters({ type: "", minPrice: "", maxPrice: "", bedrooms: "", bathrooms: "" });
    };

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40">
            <form className="bg-white rounded-xl shadow-lg p-8 min-w-[320px] w-full max-w-md relative" onSubmit={handleSubmit}>
                <button type="button" className="absolute top-3 right-3 text-2xl text-neutral-500 hover:text-accent" onClick={onClose}>
                    <IoClose />
                </button>
                <h3 className="text-xl font-semibold mb-4">Filtrar resultados</h3>
                <div className="flex flex-col gap-4">
                    <div>
                        <label className="block font-medium mb-1">Tipo de propiedad</label>
                        <select name="type" value={localFilters.type} onChange={handleChange} className="w-full border rounded p-2">
                            <option value="">Todos</option>
                            {tiposInmuebles.map((tipo) => (
                                <option key={tipo.id} value={tipo.id}>{tipo.nombre}</option>
                            ))}
                        </select>
                    </div>
                    <div className="flex gap-2">
                        <div className="flex-1">
                            <label className="block font-medium mb-1">Precio mínimo</label>
                            <input name="minPrice" type="number" min="0" value={localFilters.minPrice} onChange={handleChange} className="w-full border rounded p-2" />
                        </div>
                        <div className="flex-1">
                            <label className="block font-medium mb-1">Precio máximo</label>
                            <input name="maxPrice" type="number" min="0" value={localFilters.maxPrice} onChange={handleChange} className="w-full border rounded p-2" />
                        </div>
                    </div>
                    <div className="flex gap-2">
                        <div className="flex-1">
                            <label className="block font-medium mb-1">Habitaciones</label>
                            <input name="bedrooms" type="number" min="0" value={localFilters.bedrooms} onChange={handleChange} className="w-full border rounded p-2" />
                        </div>
                        <div className="flex-1">
                            <label className="block font-medium mb-1">Baños</label>
                            <input name="bathrooms" type="number" min="0" value={localFilters.bathrooms} onChange={handleChange} className="w-full border rounded p-2" />
                        </div>
                    </div>
                </div>
                <div className="flex justify-between gap-2 mt-6">
                    <button type="button" className="form-button border border-accent text-accent bg-white hover:bg-accent hover:text-white" onClick={handleClear}>Limpiar</button>
                    <button type="submit" className="form-button bg-accent text-white hover:bg-slate-800">Aplicar filtros</button>
                </div>
            </form>
        </div>
    );
};

export default FilterModal;
