import { useState } from "react";
import { ModalContext } from "./ModalContext";
import { ModalView } from "./ModalContext";

export const ModalProvider: React.FC<React.PropsWithChildren<object>> = ({
  children,
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [message, setMessage] = useState("Prueba");
  const [view, setView] = useState<ModalView | null>(null);

  const openModal = (view: ModalView) => {
    setView(view);
    setIsOpen(true);
  };
  const closeModal = () => {
    setView(null);
    setIsOpen(false);
  };

  return (
    <ModalContext.Provider
      value={{ isOpen, message, view, setMessage, openModal, closeModal }}
    >
      {children}
    </ModalContext.Provider>
  );
};
