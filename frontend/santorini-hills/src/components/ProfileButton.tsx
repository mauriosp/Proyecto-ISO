import { FaHouseChimney, FaInbox, FaUser } from "react-icons/fa6";
import { useUserContext } from "../context/user/UserContext";
import NavbarModalSwitch from "./NavbarModalSwitch";
import Separator from "./Separator";
import { useModalContext, ModalView } from "../context/modal/ModalContext";
import { FaExclamationCircle } from "react-icons/fa";
import { Link } from "react-router";

const UserButtons = () => {
  const { user, logout } = useUserContext();
  const { openModal } = useModalContext();
  const handleClick = (view: ModalView) => {
    openModal(view);
  };

  const handlePostClick = () => {
    if (user && !user?.isVerified) {
      openModal("verification");
    }
  }
  return (
    <div className="flex items-center w-full text-accent gap-4">
      {user?.profile === "owner" && (
        <Link to={"postAdvertisement"} onClick={handlePostClick} className="flex items-center justify-center gap-3 hover:cursor-pointer rounded-md py-3 px-4 h-min w-max hover:bg-slate-800 bg-accent text-white font-semibold transition-all">
          Publicar
          <FaHouseChimney size={18} />
        </Link>
      )}
      <NavbarModalSwitch Icon={FaInbox}>
        <div className="flex flex-col gap-1">
          <h2 className="text-lg font-semibold">Notificaciones</h2>
          <Separator />
          <div className="flex flex-col items-center justify-center gap-4 text-neutral-500 text-center py-20">
            <FaInbox size={32} />
            <p className="text-sm">Nada por aquí...</p>
          </div>
        </div>
      </NavbarModalSwitch>
      <NavbarModalSwitch Icon={FaUser}>
        <div className="flex flex-col gap-3">
          <h2 className="text-lg font-semibold">Mi perfil</h2>
          <Separator />
          <div>
            <div className="flex items-center gap-1">
              <h3 className="text-sm font-medium">{user?.name}</h3>{" "}
              {!user?.isVerified && (
                <div className="relative group">
                  <FaExclamationCircle className="text-amber-500" size={12} />
                  <div className="absolute left-1/2 -translate-x-1/2 top-full mt-1 w-max opacity-0 group-hover:opacity-100 transition-opacity bg-slate-800 text-white text-xs rounded py-1 px-2 pointer-events-none">
                    Correo sin verificar
                  </div>
                </div>
              )}
            </div>
            <p className="text-xs text-neutral-500">{user?.email}</p>
            <p className="text-xs text-neutral-500">{user?.phone}</p>
          </div>
        </div>
        <div className="flex flex-col gap-1 mt-4">
          <a
            href="#"
            onClick={() => handleClick("edit")}
            className="hover:bg-black/10 p-2 rounded-md transition-all"
          >
            Editar perfil
          </a>
          <a
            href="#"
            onClick={logout}
            className="font-medium text-red-500 p-2 rounded-md hover:text-white hover:bg-red-500 transition-all"
          >
            Cerrar sesión
          </a>
        </div>
      </NavbarModalSwitch>
    </div>
  );
};

export default UserButtons;
