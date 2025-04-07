import { useModalContext } from "../context/modal/ModalContext";
import EditProfile from "../pages/EditProfile";
import Login from "../pages/Login";
import Register from "../pages/Register";

const Modal = () => {
  const { message, view, closeModal } = useModalContext();
  const handleBackgroundClick = () => {
    closeModal();
  };

  const handleModalClick = (e: React.MouseEvent<HTMLDivElement>) => {
    // Evita que el evento se propague al fondo
    e.stopPropagation();
  };
  return (
    <>
      <div onClick={handleBackgroundClick} className="w-screen h-screen bg-black/50 fixed top-0 left-0 z-50 flex justify-center items-center">
        <div onClick={handleModalClick} className="flex justify-center items-center p-8 rounded-3xl bg-neutral-100">
          <div className="min-w-md">
            <h3 className="text-center text-xl font-semibold my-10">
              {message}
            </h3>
            {view === "login" && <Login />}
            {view === "register" && <Register />}
            {view === "edit" && <EditProfile />}
            {/* <Verification/> */}
          </div>
        </div>
      </div>
    </>
  );
};

export default Modal;
