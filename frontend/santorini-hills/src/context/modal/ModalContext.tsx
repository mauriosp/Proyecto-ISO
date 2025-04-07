import { createContext, useContext } from "react";
export type ModalView = "login" | "register" | "edit"

type ModalContextType = {
  isOpen: boolean;
  message: string;
  view: ModalView | null;
  setMessage: (message: string) => void;
  openModal: (view: ModalView) => void;
  closeModal: () => void;
};

export const ModalContext = createContext<ModalContextType>({
  isOpen: false,
  view: null,
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