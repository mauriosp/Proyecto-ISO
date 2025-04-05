import { ModalView, useModalContext } from "../context/modal/ModalContext";

const AuthButtons = () => {
  const { openModal } = useModalContext();
  const handleClick = (view:ModalView) => {
    openModal(view);
  };
  return (
    <div className="flex w-full gap-4">
      <button
        onClick={()=>handleClick("login")}
        className="hover:cursor-pointer rounded-md py-2 px-3 h-min w-max hover:bg-black/5 transition-all"
      >
        Inicia sesión
      </button>
      <button
        onClick={()=>handleClick("register")}
        className="hover:cursor-pointer rounded-md py-2 px-3 h-min w-max hover:bg-slate-800 bg-accent text-white font-semibold transition-all"
      >
        Crea tu cuenta
      </button>
    </div>
  );
};

export default AuthButtons;
