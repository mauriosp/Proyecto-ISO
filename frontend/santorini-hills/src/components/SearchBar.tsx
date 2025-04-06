import { FaSearch } from "react-icons/fa";
import { tiposInmuebles } from "../assets/tiposInmuebles";

const SearchBar = () => {
  return (
    <div className="flex items-center justify-between gap-2 border-neutral-500 border-2 rounded-md p-2">
      <div className="flex flex-1 items-center gap-2">
          <select className="w-24 text-sm outline-none text-center" name="city">
            {tiposInmuebles.map((tipo) => (
              <option key={tipo.id} value={tipo.nombre}>
                {tipo.nombre}
              </option>
            ))}
          </select>
          <input
            className="outline-none border-l-2 w-full border-neutral-500 pl-4"
            type="text"
            placeholder="Busca por ubicación"
          />
      </div>
      <button className="hover:cursor-pointer bg-accent hover:bg-slate-800 transition-all p-2 text-white rounded-md">
        <FaSearch size={16} />
      </button>
    </div>
  );
};

export default SearchBar;