import { useState } from "react";
import { ModalContext } from "./ModalContext";

export const ModalProvider: React.FC<React.PropsWithChildren<object>> = ({ children }) => {
    const [isOpen, setIsOpen] = useState(false);
    const [message, setMessage] = useState("Prueba");
  
    const openModal = () => setIsOpen(true);
    const closeModal = () => setIsOpen(false);
  
    return (
      <ModalContext.Provider
        value={{ isOpen, message, setMessage, openModal, closeModal }}
      >
        {children}
      </ModalContext.Provider>
    );
  };