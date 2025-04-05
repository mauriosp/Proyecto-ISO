import { FaHouseChimney, FaInbox, FaUser } from "react-icons/fa6";
import { useUserContext } from "../context/user/UserContext";
import NavbarModalSwitch from "./NavbarModalSwitch";

const UserButtons = () => {
  const { user, logout } = useUserContext();
  return (
    <div className="flex items-center w-full text-accent gap-4">
      {user?.isVerified && (
        <button className="flex items-center justify-center gap-3 hover:cursor-pointer rounded-md py-3 px-4 h-min w-max hover:bg-slate-800 bg-accent text-white font-semibold transition-all">
          Publicar
          <FaHouseChimney size={18} />
        </button>
      )}
      <NavbarModalSwitch Icon={FaInbox}>
        <div className="flex flex-col gap-1"></div>
      </NavbarModalSwitch>

      <NavbarModalSwitch Icon={FaUser}>
        <div className="flex flex-col gap-1">
          <h2 className="text-sm font-semibold">{user?.name}</h2>
          <p className="text-xs text-neutral-500">{user?.email}</p>
          <p className="text-xs text-neutral-500">{user?.phone}</p>
        </div>
        <div className="flex flex-col gap-2 mt-4">
          <a href="#" className="hover:bg-black/5 p-2 rounded-md transition-all">Editar perfil</a>
          <a href="#" onClick={logout} className="font-medium text-red-500 p-2 rounded-md hover:text-white hover:bg-red-500 transition-all">Cerrar sesión</a>
        </div>
      </NavbarModalSwitch>
    </div>
  );
};

export default UserButtons;
