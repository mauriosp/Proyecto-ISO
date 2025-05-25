import { createContext, useContext } from "react";
export type ModalView = "login" | "register" | "edit" | "verification" | "report" | "message" | "list";

type ModalContextType = {
  isOpen: boolean;
  message: string;
  view: ModalView | null;
  currentAdId: string | null;
  setCurrentAdId: (id: string | null) => void; // Nuevo método para establecer el ID del anuncio actual
  setMessage: (message: string) => void;
  openModal: (view: ModalView) => void;
  closeModal: () => void;
};

export const ModalContext = createContext<ModalContextType>({
  isOpen: false,
  view: null,
  message: "",
  currentAdId: null,
  setCurrentAdId: () => {}, // Nuevo método para establecer el ID del anuncio actual
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