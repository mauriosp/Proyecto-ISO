import { createContext, useContext } from "react";

type ModalContextType = {
  isOpen: boolean;
  message: string;
  setMessage: (message: string) => void;
  openModal: () => void;
  closeModal: () => void;
};

export const ModalContext = createContext<ModalContextType>({
  isOpen: false,
  message: "",
  setMessage: () => {},
  openModal: () => {},
  closeModal: () => {},
});

export const useModalContext = () => {
  const context = useContext(ModalContext);
  if (!context) {
    throw new Error("useModalContext must be used within a ModalProvider");
  }
  return context;
};