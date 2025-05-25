import { useState } from "react";
import { ModalContext } from "./ModalContext";
import { ModalView } from "./ModalContext";

export const ModalProvider: React.FC<React.PropsWithChildren<object>> = ({
  children,
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [message, setMessage] = useState("Prueba");
  const [view, setView] = useState<ModalView | null>(null);
  const [currentAdId, setCurrentAdId] = useState<string | null>(null); // 👈 NUEVO

  const openModal = (view: ModalView) => {
    setView(view);
    setIsOpen(true);
  };

  const closeModal = () => {
    setView(null);
    setIsOpen(false);
    setCurrentAdId(null); // 👈 Opcional: limpiar el ID al cerrar
  };

  return (
    <ModalContext.Provider
      value={{
        isOpen,
        message,
        view,
        currentAdId,      // 👈 NUEVO
        setMessage,
        setCurrentAdId,   // 👈 NUEVO
        openModal,
        closeModal,
      }}
    >
      {children}
    </ModalContext.Provider>
  );
};
