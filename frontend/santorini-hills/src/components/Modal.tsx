import { useModalContext } from "../context/modal/ModalContext";
import EditProfile from "../pages/EditProfile";
import Login from "../pages/Login";
import Register from "../pages/Register";
import Verification from "../pages/Verification";
import { motion, AnimatePresence } from "framer-motion";

const Modal = () => {
  const { message, view, closeModal } = useModalContext();

  const handleBackgroundClick = () => {
    closeModal();
  };

  const handleModalClick = (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();
  };

  return (
    <AnimatePresence>
      {view && (
        <motion.div
          key="modal-background"
          onClick={handleBackgroundClick}
          className="w-screen h-screen bg-black/50 fixed top-0 left-0 z-50 flex justify-center items-center"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
        >
          <motion.div
            key="modal-content"
            onClick={handleModalClick}
            className="flex justify-center items-center p-8 rounded-3xl bg-neutral-100 shadow-xl"
            initial={{ scale: 0.9, opacity: 0 }}
            animate={{ scale: 1, opacity: 1 }}
            exit={{ scale: 0.9, opacity: 0 }}
            transition={{ duration: 0.2 }}
          >
            <div className="min-w-md">
              <h3 className="text-center text-xl font-semibold my-10">
                {message}
              </h3>
              {view === "login" && <Login />}
              {view === "register" && <Register />}
              {view === "edit" && <EditProfile />}
              {view === "verification" && <Verification />}
            </div>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  );
};

export default Modal;
